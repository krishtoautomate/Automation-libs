package com.ReportManager;

import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {

    private static ExtentReports extentReports;
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    static Map<ExtentTest, ExtentReports> extentReportMap = new HashMap<ExtentTest, ExtentReports>();
//    static ExtentReports extent = ExtentManager.createExtentReports();
    static int testCount=0;
    static int reportCount=0;
    static int reportIndex = 0;

    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized ExtentReports getExtentReport() {
        return extentReportMap.get(getTest());
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentReports extent = ExtentManager.createExtentReports();
        ExtentTest test = extent.createTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    public static synchronized ExtentTest startTest(String testName) {
        testCount++;
        int testsPerReport = Constants.EXTENT_TEST_LIMIT;
//        if(testCount<testsPerReport)
//            extentReports = ExtentManager.createExtentReports();//default
        if ((testCount - 1) % testsPerReport == 0) {
            reportIndex++;
            String htmlReport = Constants.EXTENT_HTML_CUSTOM_REPORT.replace("INDEX", String.valueOf(reportIndex));
            extentReports = ExtentManager.createExtentReports(htmlReport);
            System.out.println("Report : "+ htmlReport);
        }
        ExtentTest test = extentReports.createTest(testName);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        extentReportMap.put(test, extentReports);
        return test;
    }

    public static synchronized void flush() {
        getTest().getExtent().flush();
        extentReportMap.get(getTest()).flush();
    }

}
