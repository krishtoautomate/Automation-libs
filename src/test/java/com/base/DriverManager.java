package com.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Krish on 21.05.2018.
 */

public class DriverManager {

    static Map<Long, WebDriver> webDriverMap = new ConcurrentHashMap<Long, WebDriver>();
    static Map<Long, AppiumDriver> appiumDriverMap = new ConcurrentHashMap<Long, AppiumDriver>();
    private static OptionsManager optionsManager = new OptionsManager();
    private static ThreadLocal<WebDriver> tlWebDriver = new ThreadLocal<>();
    private static AppiumDriverLocalService server;
    private ThreadLocal<AppiumDriver> tlAppiumDriver = new ThreadLocal<>();
    private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();

    /*
    @platform = Web/Appium
     */
    public static synchronized WebDriver getDriverInstance(@NotNull String platform) {
        if (platform.equalsIgnoreCase("Web"))
            return webDriverMap.get(Long.valueOf(Thread.currentThread().getId()));
        else
            return appiumDriverMap.get(Long.valueOf(Thread.currentThread().getId()));
    }

    /*
    @platform = Web/Appium
     */
    public static synchronized void quit(@NotNull String platform) {
        if (platform.equalsIgnoreCase("Web"))
            getDriverInstance("Web").quit();
        else
            getDriverInstance("Appium").quit();
    }

    /*
     @platform = Web/Appium
     */
    protected synchronized void setDriver(ITestContext iTestContext) throws IOException, ParseException {

        String platform = (String) iTestContext.getAttribute("platform");
        if (platform.equalsIgnoreCase("Web")) {

            ITestResult iTestResult = Reporter.getCurrentTestResult();
            Map<String, String> testParams =
                    iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

            String browser = testParams.get("browser");
            browser = browser == null ? testParams.get("platForm") : testParams.get("browser");

            String REMOTE_HOST =
                    testParams.get("REMOTE_HOST_WEB") == null ? "localhost" : testParams.get("REMOTE_HOST_WEB");

            if (platform == null)
                return;
            if (browser.equalsIgnoreCase("chrome")) {
                iTestContext.setAttribute("platForm", "CHROME");
                DesiredCapabilities capabilities = capabilitiesManager.setCapabilities(iTestContext);

                capabilities.setCapability("chrome.switches",
                        Arrays.asList("--ignore-certificate-errors" + "," + "--web-security=false" + ","
                                + "--ssl-protocol=any" + "," + "--ignore-ssl-errors=true"));

                ChromeOptions options = optionsManager.getChromeOptions(capabilities);

                if ("localhost".equalsIgnoreCase(REMOTE_HOST)) {
                    tlWebDriver.set(new ChromeDriver(options));
                } else {
                    tlWebDriver.set(new RemoteWebDriver(new URL(REMOTE_HOST), options));
                }
            } else if (browser.equalsIgnoreCase("ie")) {

                // EdgeOptions options = new EdgeOptions();
                // Capabilities cap = new DesiredCapabilities();

                // options.setCapability("user-agent", "GomezAgent");

                // EdgeProfile
                // For Local Usage
                tlWebDriver.set(new InternetExplorerDriver());
            }
            webDriverMap.put(Thread.currentThread().getId(), tlWebDriver.get());

            getDriverInstance("Web").manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        }

        if (platform.equalsIgnoreCase("Appium")) {

            ITestResult iTestResult = Reporter.getCurrentTestResult();
            Map<String, String> testParams =
                    iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

            String platForm = testParams.get("platForm");
            if (platForm == null)
                return;

            String REMOTE_HOST =
                    testParams.get("REMOTE_HOST") == null ? "localhost" : testParams.get("REMOTE_HOST");

            if (REMOTE_HOST.equalsIgnoreCase("localhost")) {
                AppiumService appiumService = new AppiumService();
                server = appiumService.AppiumServer();
                server.start();
                REMOTE_HOST = server.getUrl().toString();
            }

            if ("Android".equalsIgnoreCase(platForm)) {
                iTestContext.setAttribute("platForm", "ANDROID");
                tlAppiumDriver.set(new AndroidDriver(new URL(REMOTE_HOST),
                        capabilitiesManager.setCapabilities(iTestContext)));
            } else if ("iOS".equalsIgnoreCase(platForm)) {
                iTestContext.setAttribute("platForm", "IOS");
                tlAppiumDriver.set(new IOSDriver(new URL(REMOTE_HOST),
                        capabilitiesManager.setCapabilities(iTestContext)));
            }

            appiumDriverMap.put(Thread.currentThread().getId(), tlAppiumDriver.get());

            // System.out.println("Thread Id : "+ Thread.currentThread().getId());
            // System.out.println("Session Id : "+ tlDriver.get().getSessionId());

            getDriverInstance("Appium").manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        }
    }

}
