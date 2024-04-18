package com.base;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebDriverManager {

    static Map<Long, WebDriver> webDriverMap = new ConcurrentHashMap<Long, WebDriver>();
    private static ThreadLocal<WebDriver> tlWebDriver = new ThreadLocal<>();

    private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();
    private OptionsManager optionsManager = new OptionsManager();

    private static Logger log = Logger.getLogger(WebDriverManager.class.getName());


    /*
    @driverType = Web/Appium
     */
    public static synchronized WebDriver getDriverInstance() {
        return webDriverMap.get(Thread.currentThread().getId());
    }

    public synchronized void setDriver() {

        ITestResult iTestResult = Reporter.getCurrentTestResult();
        Map<String, String> testParams =
                iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

        String browser = testParams.get("browser");

        String REMOTE_HOST = testParams.get("REMOTE_HOST") == null ? "http://bqatautomation.bell.corp.bce.ca:5555" :
                testParams.get("REMOTE_HOST");

        if (browser == null || browser.equalsIgnoreCase("chrome")) {

            DesiredCapabilities capabilities = capabilitiesManager.setCapabilities("CHROME");

            ChromeOptions options = optionsManager.getChromeOptions();

            //add capabilities to options
            JSONObject jObject = new JSONObject(capabilities.toJson());
            for (Object keyStr : jObject.keySet()) {
                try {
                    options.setCapability(keyStr.toString(), jObject.get(keyStr));
                } catch (Exception e) {
                    log.error("failed to add capabilities to options");
                }
            }

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
}
