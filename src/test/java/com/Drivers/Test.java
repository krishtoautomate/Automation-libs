package com.Drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Test {

    static AppiumDriver driver;

    public static void main(String[] args) throws Exception {
        URL appiumServerUrl = new URL("http://172.21.34.239:5555/wd/hub");

        Map<String, Object> capabilitiesMap = new HashMap<>();
        capabilitiesMap.put("systemPort", 8512);
        capabilitiesMap.put("appium:automationName", "uiautomator2");
        capabilitiesMap.put("deviceName", "LGE LG-H873");
        capabilitiesMap.put("appium:platformVersion", "7.0");
        capabilitiesMap.put("appium:udid", "LGH873b2af0494");
        capabilitiesMap.put("platformName", "ANDROID");
        capabilitiesMap.put("appium:appPackage", "com.android.vending");
        capabilitiesMap.put("appium:appActivity", "com.android.vending.AssetBrowserActivity");

        DesiredCapabilities capabilities = new DesiredCapabilities(capabilitiesMap);
        driver = new AndroidDriver(appiumServerUrl, capabilities);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        System.out.println("SessionId : " + driver.getSessionId());

        Thread.sleep(10000);

        By accountLogo = By.xpath(
                "(//android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ImageView[contains(@resource-id, '0_resource_name_obfuscated')])[1]");

//        driver.findElementAndTap(accountLogo);


        Thread.sleep(5000);

        driver.quit();
    }

}
