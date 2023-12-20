package com.base;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;


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
