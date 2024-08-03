package com.Listeners;

import com.ReportManager.ExtentTestManager;
import com.aventstack.extentreports.ExtentTest;
import com.base.AppiumDriverManager;
import io.appium.java_client.AppiumDriver;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Created by Krish on 01.12.2018.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static int maxTry = 2;
    private int count = 0;

    @Override
    public boolean retry(ITestResult iTestResult) {
        switch (iTestResult.getStatus()) {
            case ITestResult.FAILURE:
//                System.out.println("--test failed--");
                break;

            case ITestResult.SKIP:
//                System.out.println("--test skipped--");
                break;

            case ITestResult.SUCCESS:
//                System.out.println("--test success--");
                break;

        }
        if (!iTestResult.isSuccess()) { // Check if test not succeed
            if (count < maxTry) { // Check if maxtry count is reached
                count++; // Increase the maxTry count by 1
//                iTestResult.setStatus(ITestResult.FAILURE); // Mark test as failed

                try {
                    AppiumDriver driver = AppiumDriverManager.getDriverInstance();
                    ExtentTest test = ExtentTestManager.getTest();
                    String errorXML = driver.getPageSource();
                    if (errorXML.contains("Technical issue") || errorXML.contains(" error")) {
//                        System.out.println("retrying : " + count);
                        test.assignCategory("Retry");
                        return true;
                    }
                } catch (Exception e) {
//                    System.out.println("retrying : " + count);
                    return true;
                }
//                return true; // Tells TestNG to re-run the test
            }
//            else {
//                iTestResult.setStatus(ITestResult.FAILURE); // If maxCount reached,test marked as failed
//            }
        }
        return false;
    }

}
