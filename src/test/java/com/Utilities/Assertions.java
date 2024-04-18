package com.Utilities;

import com.ReportManager.ExtentTestManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.base.AppiumDriverManager;
import com.base.TestBase;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

public class Assertions extends TestBase {

    public static void contains(String actualText, String expectedText, boolean... takeScreenshot) {

        AppiumDriver driver = AppiumDriverManager.getDriverInstance();

        boolean isScreenshot = (takeScreenshot.length > 0) ? takeScreenshot[0] : false;

        String actual = actualText.replaceAll("\n", " ");
        String[] arrOfExpected = expectedText.split("\\|");

        for (String expected : arrOfExpected) {
            if (StringUtils.containsIgnoreCase(actual.trim(), expected.trim())
                    | StringUtils.containsIgnoreCase(expected, actual.trim())
                    && !actual.isEmpty()) {

                if (isScreenshot)
                    ExtentTestManager.getTest()
                            .pass("Verification Success : '" + actual.trim() + "' Vs '" + expectedText + "'", MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());
                else
                    ExtentTestManager.getTest()
                            .pass("Verification Success : '" + actual.trim() + "' Vs '" + expected.trim() + "'");
                return;
            }
        }

        String message =
                "Verification failed : Actual : '" + actual + "' \n " + "Expected : '" + expectedText + "'";
        ExtentTestManager.getTest()
                .fail("<p style='color:red;'>" + message + "</p>", MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());
        Assert.fail(message);

    }

    public static void softAssertcontains(String Actual, String Expected) {

        AppiumDriver driver = AppiumDriverManager.getDriverInstance();

        String actual = Actual.replaceAll("\n", " ");
        String[] arrOfExpected = Expected.split("\\|");

        for (String expected : arrOfExpected) {
            if (StringUtils.containsIgnoreCase(actual.trim(), expected.trim())
                    | StringUtils.containsIgnoreCase(expected, actual.trim()) && !actual.isEmpty()) {
                ExtentTestManager.getTest().info(
                        "Verification Success : '" + actual.trim() + "' matches '" + expected.trim() + "'");
                return;
            }
        }

        String message =
                "Verification failed : 'Actual : " + actual + " \n " + "Expected : " + Expected + "'";
        ExtentTestManager.getTest().warning(message, MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());
    }

    public static void softAssertTrue(boolean isTrue, String message) {
        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
        if (isTrue) {
            ExtentTestManager.getTest().info(message);
        } else {
            ExtentTestManager.getTest().fail(message, MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());
        }
    }

    public static void assertTrue(boolean expression, String message) {
        if (expression) {
            ExtentTestManager.getTest().info(message);
        } else {
            ExtentTestManager.getTest().fail("failed : " + message);
            Assert.assertTrue(expression, "failed : " + message);
        }
    }

    public static void assertFalse(boolean expression, String message) {
        if (!expression) {
            ExtentTestManager.getTest().info(message);
        } else {
            ExtentTestManager.getTest().fail(message + " failed");
            Assert.assertTrue(expression, message);
        }
    }

    public static void equals(String actualText, String expectedText) {
        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
        if (!actualText.equals(expectedText)) {
            String message =
                    "Verification failed : 'Actual : " + actualText + " \n " + "Expected : " + expectedText + "'";
            ExtentTestManager.getTest()
                    .fail("<p style='color:red;'>" + message + "</p>", MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());
            Assert.fail(message);
        }
    }

    public static void equals(int actual, int expected) {
        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
        if (!(actual == expected)) {
            String message =
                    "Verification failed : 'Actual : " + actual + " \n " + "Expected : " + expected + "'";
            ExtentTestManager.getTest()
                    .fail("<p style='color:red;'>" + message + "</p>", MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());
            Assert.fail(message);
        }
    }

    public static void equals(Float actual, Float expected) {
        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
        if (!(actual == expected)) {
            String message =
                    "Verification failed : 'Actual : " + actual + " \n " + "Expected : " + expected + "'";
            ExtentTestManager.getTest()
                    .fail("<p style='color:red;'>" + message + "</p>", MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());
            Assert.fail(message);
        }
    }

    public static void fail(String message) {
        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
        ExtentTestManager.getTest()
                .fail(message, MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());
        Assert.fail(message);
    }
}
