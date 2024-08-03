package com.Listeners;

import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.Utilities.ScreenshotManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.AppiumDriverManager;
import com.base.Jira;
import io.appium.java_client.AppiumDriver;
import org.apache.log4j.Logger;
import org.testng.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class TestListener extends TestListenerAdapter
        implements ISuiteListener, ITestListener, IInvokedMethodListener {

    private static Logger log = Logger.getLogger(TestListener.class.getName());

    Jira jiraReporter = new Jira();

    @Override
    public void onTestStart(ITestResult iTestResult) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateANDtime = sdf.format(date.getTime());

        String start = dateANDtime;

        iTestResult.setAttribute("start", start);

        try {
            AppiumDriver driver = AppiumDriverManager.getDriverInstance();

            String udid = driver.getCapabilities().getCapability("udid").toString();

//            String[] params = {"udid", udid};
//            iTestResult.setParameters(params);

            log.info("udid : " + udid);
        } catch (Exception e) {
            //ignore
        }
    }

    @Override
    public synchronized void onTestSuccess(ITestResult testResult) {
        /*
         * get device details
         */
        Map<String, String> testParams =
                testResult.getTestContext().getCurrentXmlTest().getAllParameters();
        String platForm = testParams.get("platForm");

        System.getenv("BUILD_NUMBER");
        System.getenv("ENVIRONMENT");
        String testName = testResult.getMethod().getMethodName();

        Object testClass = testResult.getInstance();
        ExtentTest test = ExtentTestManager.getTest();

        test.log(Status.INFO, testName + " - Completed as Success");

        // Categories
        test.assignCategory(platForm);

        // DB update
        // DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        // LocalDateTime now = LocalDateTime.now();
        // dtf.format(now);

        // reporter.report(date_time, "MVM", buildNo, environment, testName, deviceName, platForm,
        // "PASS",
        // "");

        // Jira
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateANDtime = sdf.format(date.getTime());

        String testKey = null;
        try {
            testKey = testResult.getAttribute("testKey").toString();
        } catch (Exception e) {
            // ignore
        }

        if (testKey != null) {
            String start = testResult.getAttribute("start").toString();
            String finish = dateANDtime;

            jiraReporter.addTest(testKey, start, finish, "PASS", testName + "(" + platForm + ")");
        }
    }

    @Override
    public synchronized void onTestFailure(ITestResult testResult) {
        /*
         * get device details
         */
        Map<String, String> testParams =
                testResult.getTestContext().getCurrentXmlTest().getAllParameters();
        String udid = testParams.get("udid");
        String platForm = testParams.get("platForm");

        System.getenv("BUILD_NUMBER");
        System.getenv("ENVIRONMENT");
        String testName = testResult.getMethod().getMethodName();
        String className = testResult.getTestClass().getName();

        Object testClass = testResult.getInstance();
        AppiumDriver driver = AppiumDriverManager.getDriverInstance();

        ExtentTest test = ExtentTestManager.getTest();

        if (driver != null) {
            log.error("Test failed : " + className + " : " + udid);

            try {
                // Unique name to screen-shot
                String imgPath = new ScreenshotManager(driver).getScreenshot();

                test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());

            } catch (Exception e) {
                test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable());
            }

            try {
                String errorXML = driver.getPageSource();
                test.info(MarkupHelper.createCodeBlock(errorXML));
            } catch (Exception e) {

            }

            // Categories
            test.assignCategory(platForm);

            // DB
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            dtf.format(now);

            // Emailable Test Summary
            // reporter.report(date_time, "MVM", buildNo, environment, testName, deviceName, platForm,
            // "FAIL", testResult.getThrowable().toString());

        }
        // Jira
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateANDtime = sdf.format(date.getTime());

        String testKey = null;
        try {
            testKey = testResult.getAttribute("testKey").toString();
        } catch (Exception e) {
            // ignore
        }

        if (testKey != null) {
            String start = testResult.getAttribute("start").toString();
            String finish = dateANDtime;
            jiraReporter.addTest(testKey, start, finish, "FAIL", testName + "(" + platForm + ")");
        }

    }

    @Override
    public synchronized void onTestSkipped(ITestResult testResult) {
        /*
         * get device details
         */
        Map<String, String> testParams =
                testResult.getTestContext().getCurrentXmlTest().getAllParameters();
        String udid = testParams.get("udid");
        String platForm = testParams.get("platForm");

        String testName = testResult.getMethod().getMethodName();
        String className = testResult.getTestClass().getName();

        log.warn("Test Skipped : " + className + " : " + udid);

        try {
            ExtentTest test = ExtentTestManager.getTest();
            ExtentReports extent = ExtentTestManager.getTest().getExtent();

            extent.removeTest(test);
        } catch (Exception e) {
            // ignore
        }

        // Jira
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateANDtime = sdf.format(date.getTime());

        String testKey = null;
        try {
            testKey = testResult.getAttribute("testKey").toString();
        } catch (Exception e) {
            // ignore
        }

        if (testKey != null) {
            String start = testResult.getAttribute("start").toString();
            String finish = dateANDtime;
            jiraReporter.addTest(testKey, start, finish, "TODO", testName + "(" + platForm + ")");
        }
    }

    @Override
    public void onStart(ISuite suite) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateANDtime = sdf.format(date.getTime());

        // Jira report
        String buildNo = System.getenv("BUILD_NUMBER");
        String jobName = System.getenv("JOB_NAME");
        String buildUrl = System.getenv("BUILD_URL");
        String testPlanKey = System.getenv("TEST_PLAN_KEY");

        String excutionSummary = jobName + "-" + buildNo;
        String excutionDescription = buildUrl;
        String startDate = dateANDtime;

        jiraReporter.setTestExecutionInfo("summary", excutionSummary);
        jiraReporter.setTestExecutionInfo("testPlanKey", testPlanKey);// "MAEAUTO-320"
        jiraReporter.setTestExecutionInfo("description", excutionDescription);
        jiraReporter.setTestExecutionInfo("startDate", startDate);

    }

    @Override
    public void onFinish(ISuite suite) {
        // String emailReport = Constants.EMAIL_REPORT;
        // File file = new File(emailReport);
        // if (file.delete()) {
        // // delete if exists
        // }
        // String buildNo = System.getenv("BUILD_NUMBER");
        // if (buildNo != null)
        // reporter.writeResults(emailReport);

        // Jira report
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateANDtime = sdf.format(date.getTime());
        String finishDate = dateANDtime;
        jiraReporter.setTestExecutionInfo("finishDate", finishDate);
        String testExecKey = System.getenv("TEST_EXECUTION");
        // log.info("TEST_EXECUTION : "+testExecKey);
        if (testExecKey != null) {
            jiraReporter.addExecutionKey(testExecKey);
        }
        jiraReporter.addInfo();
        jiraReporter.addTests();
        jiraReporter.createJiraReport(Constants.JIRA_REPORT);

    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // TODO Auto-generated method stub

    }


}
