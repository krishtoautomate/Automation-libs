package com.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
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
    private AppiumDriverManager appiumDriverManager = new AppiumDriverManager();
    private WebDriverManager webDriverManager = new WebDriverManager();

    /*
    @platform = Web/Appium
     */
    public static synchronized WebDriver getWebDriverInstance() {
        return WebDriverManager.getDriverInstance();
    }

    public static synchronized AppiumDriver getAppiumDriverInstance() {
        return AppiumDriverManager.getDriverInstance();
    }

    /*
     @driverType = Web/Appium
     */
    public synchronized void setDriver(String driverType) {
        if (driverType.equalsIgnoreCase("Web")) {
            webDriverManager.setDriver();
        } else if (driverType.equalsIgnoreCase("Appium")) {
            appiumDriverManager.setDriver();
        } else if (driverType.equalsIgnoreCase("Appium-Browser")) {
            appiumDriverManager.setBrowserDriver();
        }
    }

}
