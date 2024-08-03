package com.Listeners;

import com.ReportManager.ExtentTestManager;
import com.ReportManager.SlackReporter;
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
import java.util.Date;
import java.util.TimeZone;

public class TestListenerDeeplinks extends TestListenerAdapter
        implements ISuiteListener, ITestListener, IInvokedMethodListener {

    private static Logger log = Logger.getLogger(TestListenerDeeplinks.class.getName());

    private static String testExecutionKey = null;
    Jira jiraReporter = new Jira();
    SlackReporter slackReporter = new SlackReporter();

    @Override
    public void onTestStart(ITestResult iTestResult) {

        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            sdf.setTimeZone(TimeZone.getTimeZone("EST"));
            String timeNow = sdf.format(date.getTime());
            iTestResult.setAttribute("start", timeNow);
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public synchronized void onTestSuccess(ITestResult testResult) {
        try {
            /*
             * get device details
             */

            String testName = testResult.getMethod().getMethodName();
            String className = testResult.getTestClass().getName();

            ExtentTest test = ExtentTestManager.getTest();

            test.log(Status.INFO, testName + " - Completed as Success");

            // Jira
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            sdf.setTimeZone(TimeZone.getTimeZone("EST"));
            String timeNow = sdf.format(date.getTime());

            String testKey = null;
            try {
                testKey = testResult.getAttribute("testKey").toString();
            } catch (Exception e) {
                log.warn("testKey not found : " + className);
            }

//            log.info("Test Pass : " + testKey);

            if (testKey != null) {
                String start = testResult.getAttribute("start").toString();
                String finish = timeNow;
                if (testExecutionKey != null) {
                    jiraReporter.update_Test_Exec(testExecutionKey, testKey, "PASS", start, finish);
                    log.info("Test execution " + testExecutionKey + " updated as PASS for test : " + testKey
                            + " : " + className);
                }
            }
        } catch (Exception e) {
            log.error("Jira api error");
        }
    }

    @Override
    public synchronized void onTestFailure(ITestResult testResult) {

        /*
         * get device details
         */
//        Map<String, String> testParams =
//                testResult.getTestContext().getCurrentXmlTest().getAllParameters();
        String udid = testResult.getTestContext().getAttribute("udid").toString();

        String testName = testResult.getMethod().getMethodName();
        String className = testResult.getTestClass().getName();

        try {
            // String slackChannel = System.getenv("SLACK_CHANNEL");

            AppiumDriver driver = AppiumDriverManager.getDriverInstance();

            ExtentTest test = ExtentTestManager.getTest();

            if (driver != null) {

                udid = driver.getCapabilities().getCapability("udid").toString();

                log.error("Test failed : " + className + " : " + udid);

                try {
                    // Unique name to screen-shot
                    String imgPath = new ScreenshotManager(driver).getScreenshot();

                    test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable(),
                            MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());

                } catch (Exception e) {
                    //test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable());
                }

                try {
                    String errorXML = driver.getPageSource();
                    test.info(MarkupHelper.createCodeBlock(errorXML));

                    if (errorXML.contains("Technical issue") || errorXML.contains(" error")) {
                        test.assignCategory("server errors");
                    }

                    if (errorXML.contains("com.sec.android.app.launcher") || errorXML.contains("type=\"XCUIElementTypeApplication\" name=\" \")")) {
                        test.assignCategory("Crash");
                    }
                } catch (Exception ign) {
                    log.info("Failed to get page source : " + "\n" + ign.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            log.info("Failed to get error details : " + "\n" + e.getLocalizedMessage());
        }

        try {
            // Jira
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            sdf.setTimeZone(TimeZone.getTimeZone("EST"));
            String dateANDtime = sdf.format(date.getTime());

            String testKey = "";
            try {
                testKey = testResult.getAttribute("testKey").toString();
            } catch (Exception e) {
                log.warn("testKey not found : " + className);
            }
//            log.info("Test Fail : "+ testKey);
            if (testKey != null) {
                String start = testResult.getAttribute("start").toString();
                String finish = dateANDtime;
                if (testExecutionKey != null) {
                    // JIRA
                    jiraReporter.update_Test_Exec(testExecutionKey, testKey, "FAIL", start, finish);
                    log.info("Test execution " + testExecutionKey + " updated as FAIL for test : " + testKey
                            + " : " + className);
                }
            }
        } catch (Exception e) {
            log.error("jira api error : " + e.getMessage());
        }


        // // Slack
        // String message = "*" + className + "* : <https://<jira-url>/browse/" +
        // testKey
        // + "|" + testKey + ">" + " failed due to `" + testResult.getThrowable() + "`";
        // slackReporter.send_Failure_Data_To_Channel(message, Constants.REPORT_DIR + ssPath,
        // slackChannel);
        // log.info("Failure info for " + testKey + " sent to slack channel #" + slackChannel);

    }

    @Override
    public synchronized void onTestSkipped(ITestResult testResult) {
        /*
         * get device details
         */
        String udid = testResult.getAttribute("udid").toString();

        String className = testResult.getTestClass().getName();

        log.warn("Test Skipped : " + className + " : " + udid);
        try {
            ExtentTest test = ExtentTestManager.getTest();
            ExtentReports extent = ExtentTestManager.getTest().getExtent();
            extent.removeTest(test);
        } catch (Exception e) {
            // ignore
        }

        try {
            // JIRA
            String testKey = "";
            try {
                testKey = testResult.getAttribute("testKey").toString();
            } catch (Exception e) {
                log.warn("testKey not found : " + className);
            }
//            log.info("Test Skip : "+ testKey);
            if (testKey != null) {
                if (testExecutionKey != null) {
                    jiraReporter.update_Test_Exec(testExecutionKey, testKey, "TODO", "", "");
                    System.out.println("----------------------------------------------------------------");
                    log.warn(
                            "Test execution " + testExecutionKey + " updated as TODO for test : " + testKey
                                    + " : " + className);
                    System.out.println("----------------------------------------------------------------");
                }
            }
        } catch (Exception e) {
            log.error("Jira api error : " + e.getMessage());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStart(ISuite suite) {

        try {
            // Create Jira execution
            String buildNo = System.getenv("BUILD_NUMBER");
            String jobName = System.getenv("JOB_NAME");
            String jobUrl = System.getenv("JOB_URL");
            String buildUrl = System.getenv("BUILD_URL");
            String testPlanKey = System.getenv("TEST_PLAN_KEY");
            String testExecKey = System.getenv("TEST_EXECUTION");
            String slackChannel = System.getenv("SLACK_CHANNEL");
            String executionSummary = jobName + "-" + buildNo;
            String executionDescription = buildUrl;

            Date myDate = new Date();
            SimpleDateFormat mdyFormat = new SimpleDateFormat("MMddyyyy");
            String dateFolder = mdyFormat.format(myDate);

            String realTimeLink = "<" + jobUrl + "ws/test-output/" + dateFolder
                    + "/AUTOMATION_REPORT.html|Real Time Jenkins Report>";

            if (jobName != null && testPlanKey != null) {
                // JIRA
                testExecutionKey = testExecKey;
                if (testExecutionKey == null) {
                    testExecutionKey =
                            jiraReporter.create_Test_Exec(executionSummary, executionDescription, testPlanKey);
                }

                // Slack
                String message = Constants.SLACK_BRAND + " Mobility Run started at: <" + executionDescription
                        + "|" + executionSummary + ">" + "\n" + "Execution will be updated at "
                        + "<" + Constants.JIRA_URL + testExecutionKey + "|" + testExecutionKey
                        + ">" + "\n" + "Check " + realTimeLink + " for failure details";
                slackReporter.send_Message_To_Channel(message, slackChannel);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        try {

            System.getenv("BUILD_NUMBER");
            String buildUrl = System.getenv("BUILD_URL");
            String slackChannel = System.getenv("SLACK_CHANNEL");

            if (testExecutionKey != null) {
                // Slack
                String message = Constants.SLACK_BRAND + " Mobility Run has finished. Complete  " + "<" + buildUrl
                        + "Automation_20HTML_20Report/|Jenkins Automation Report>"
                        + " and <" + Constants.JIRA_URL + testExecutionKey
                        + "|JIRA Test Execution> are now available";
                slackReporter.send_Message_To_Channel(message, slackChannel);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // TODO Auto-generated method stub
//        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
//
//        String udid = driver.getCapabilities().getCapability("udid").toString();
//
//        log.info("udid : " + udid);


    }
}