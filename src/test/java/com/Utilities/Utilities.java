package com.Utilities;


import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;


public class Utilities extends BaseObjs<Utilities> {


    public Utilities(AppiumDriver driver, ExtentTest test) {
        super(driver, test);
    }

    public Utilities(WebDriver driver, ExtentTest test) {
        super(driver, test);
    }

}
