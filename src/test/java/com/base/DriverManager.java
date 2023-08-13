package com.base;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Krish on 21.05.2018.
 */

public class DriverManager {

    static Map<Long, WebDriver> webDriverMap = new ConcurrentHashMap<Long, WebDriver>();
    private static ThreadLocal<WebDriver> tlWebDriver = new ThreadLocal<>();

    private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();

    private AppiumDriverManager appiumDriverManager = new AppiumDriverManager();

    /*
    @platform = Web/Appium
     */
    public static synchronized WebDriver getWebDriverInstance() {
        return webDriverMap.get(Thread.currentThread().getId());
    }

    public static synchronized AppiumDriver getAppiumDriverInstance() {
        return AppiumDriverManager.getDriverInstance();
    }

    /*
     @platform = Web/Appium
     */
    public synchronized void setDriver(String platform) {

        if (platform.equalsIgnoreCase("Web")) {

            ITestResult iTestResult = Reporter.getCurrentTestResult();
            Map<String, String> testParams =
                    iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

            String browser = testParams.get("browser") == null ? "chrome" : testParams.get("browser");

            String REMOTE_HOST =
                    testParams.get("REMOTE_HOST_WEB") == null ?
                            testParams.get("REMOTE_HOST") :
                            testParams.get("REMOTE_HOST_WEB");

            if (browser.equalsIgnoreCase("chrome")) {
                DesiredCapabilities capabilities = capabilitiesManager.setCapabilities("CHROME");

                OptionsManager OptionsManager = new OptionsManager();
                ChromeOptions options = OptionsManager.getChromeOptions(capabilities);

                System.out.println("options : " + options);

                try {
                    if ("localhost".equalsIgnoreCase(REMOTE_HOST)) {
                        tlWebDriver.set(new ChromeDriver(options));
                    } else {
                        tlWebDriver.set(new RemoteWebDriver(new URL(REMOTE_HOST), options));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (browser.equalsIgnoreCase("firefox")) {
                DesiredCapabilities capabilities = capabilitiesManager.setCapabilities("FIREFOX");

                OptionsManager optionsManager = new OptionsManager();
                FirefoxOptions options = optionsManager.getFirefoxOptions(capabilities);

                System.out.println("options : " + options);

                try {
                    if ("localhost".equalsIgnoreCase(REMOTE_HOST)) {
                        tlWebDriver.set(new FirefoxDriver(options));
                    } else {
                        tlWebDriver.set(new RemoteWebDriver(new URL(REMOTE_HOST), options));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            //map
            webDriverMap.put(Thread.currentThread().getId(), tlWebDriver.get());

            System.out.println("Session Id : " + ((RemoteWebDriver) tlWebDriver.get()).getSessionId());

            tlWebDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        }

        if (platform.equalsIgnoreCase("Appium")) {
            appiumDriverManager.setDriver();
        }
    }

}
