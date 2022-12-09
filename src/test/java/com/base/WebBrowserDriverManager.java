package com.base;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Krish on 21.05.2018.
 */

public class WebBrowserDriverManager {

    static Map<Long, WebDriver> driverMap = new ConcurrentHashMap<Long, WebDriver>();
    private static OptionsManager optionsManager = new OptionsManager();
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static synchronized WebDriver getDriverInstance() {
        return driverMap.get(Long.valueOf(Thread.currentThread().getId()));
    }

    public static synchronized void quit() {
        getDriverInstance().quit();
    }

    protected synchronized void setDriver() throws MalformedURLException {

        ITestResult iTestResult = Reporter.getCurrentTestResult();
        Map<String, String> testParams =
                iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

        String browser = testParams.get("browser");

        String REMOTE_HOST =
                testParams.get("REMOTE_HOST") == null ? "localhost" : testParams.get("REMOTE_HOST");

        if(browser==null)
            return;
        if (browser.equalsIgnoreCase("chrome")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            capabilities.setCapability(ChromeOptions.CAPABILITY, optionsManager.getChromeOptions());

//            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

            capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");

            capabilities.setCapability("chrome.switches",
                    Arrays.asList("--ignore-certificate-errors" + "," + "--web-security=false" + ","
                            + "--ssl-protocol=any" + "," + "--ignore-ssl-errors=true"));

            ChromeOptions options = new ChromeOptions();
            options.merge(capabilities);

            if ("localhost".equalsIgnoreCase(REMOTE_HOST)) {
//                tlDriver.set(new ChromeDriver(options));
                tlDriver.set(new ChromeDriver());
            } else {
                tlDriver.set(new RemoteWebDriver(new URL(REMOTE_HOST), options));
            }
        } else if (browser.equalsIgnoreCase("ie")) {


            // EdgeOptions options = new EdgeOptions();
            // Capabilities cap = new DesiredCapabilities();

            // options.setCapability("user-agent", "GomezAgent");

            // EdgeProfile
            // For Local Usage
            tlDriver.set(new InternetExplorerDriver());


        }
        driverMap.put(Thread.currentThread().getId(), tlDriver.get());

        getDriverInstance().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    }

}
