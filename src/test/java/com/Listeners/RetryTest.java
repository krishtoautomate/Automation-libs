package com.Listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryTest implements IRetryAnalyzer {
    private int retryCount = 0;
    private final int maxRetryCount = 3;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            if (checkPageSourceForError()) {
                return true;
            }
        }
        return false;
    }
    private boolean checkPageSourceForError() {
        // Retrieve page source and check for "Internal Server Error"
        // Return true if the page source contains the error, false otherwise
        return false;
    }
}
