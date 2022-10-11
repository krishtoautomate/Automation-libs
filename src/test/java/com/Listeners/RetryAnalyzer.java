package com.Listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Created by Krish on 01.12.2018.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static int maxTry = 3;
    private int count = 0;

    @Override
    public boolean retry(ITestResult iTestResult) {
        switch (iTestResult.getStatus()) {
            case ITestResult.FAILURE:
                System.out.println("--test failed--");
                break;

            case ITestResult.SKIP:
                System.out.println("--test skipped--");
                break;

            case ITestResult.SUCCESS:
                System.out.println("--test success--");
                break;

        }
        if (!iTestResult.isSuccess()) { // Check if test not succeed
            if (count < maxTry) { // Check if maxtry count is reached
                count++; // Increase the maxTry count by 1
                iTestResult.setStatus(ITestResult.FAILURE); // Mark test as failed
                return true; // Tells TestNG to re-run the test
            } else {
                iTestResult.setStatus(ITestResult.FAILURE); // If maxCount reached,test marked as failed
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS); // If test passes, TestNG marks it as passed
        }
        return false;
    }

}
