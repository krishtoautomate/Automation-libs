package com.Listeners;

import com.DataManager.DeviceInfoReader;
import com.ReportManager.ExtentManager;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.Utilities.ScreenShotManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.base.AppiumDriverManager;
import com.base.Jira;
import io.appium.java_client.AppiumDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class TestListener extends TestListenerAdapter implements ISuiteListener, ITestListener,
        IInvokedMethodListener {

    //extends TestListenerAdapter
//  protected ReportBuilder reporter = new ReportBuilder();

    private static Logger log = Logger.getLogger(TestListener.class.getName());

//    Jira jiraReporter = new Jira();

    @Override
    public void onTestStart(ITestResult testResult) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateANDtime = sdf.format(date.getTime());
        String start = dateANDtime;
        testResult.setAttribute("start", start);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult testResult) {
        /*
         * get device details
         */
        Map<String, String> testParams =
                testResult.getTestContext().getCurrentXmlTest().getAllParameters();

        ExtentTest test = ExtentTestManager.getTest();

        System.getenv("BUILD_NUMBER");
        System.getenv("ENVIRONMENT");
        String testName = testResult.getMethod().getMethodName();

        test.log(Status.INFO, testName + " - Completed as Success");

        // DB update
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        dtf.format(now);

        // reporter.report(date_time, "LM", buildNo, environment, testName, deviceName, platForm,
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
            // jiraReporter.setTestInfo("testKey", testKey);
            // jiraReporter.setTestInfo("start", start);
            // jiraReporter.setTestInfo("finish", finish);
            // jiraReporter.setTestInfo("status", "PASS");
            // jiraReporter.setTestInfo("comment", testName+"("+platForm+")");

//            jiraReporter.addTest(testKey, start, finish, "PASS", testName );
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


        System.getenv("BUILD_NUMBER");
        System.getenv("ENVIRONMENT");
        String testName = testResult.getMethod().getMethodName();

        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
        ExtentTest test = ExtentTestManager.getTest();
//        DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
//        String deviceName = deviceInfoReader.getString("name");

        String deviceName = driver.getCapabilities().getCapability("deviceName").toString();

        if (driver != null) {
            log.error("Test failed : " + testName + " : " + udid + "_" + deviceName);
            try {
                ScreenShotManager screenShotManager = new ScreenShotManager(driver);
                String ScreenShot = screenShotManager.getScreenshot();

                test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(ScreenShot).build());

                String errorXML = driver.getPageSource();
                test.info(MarkupHelper.createCodeBlock(errorXML));

            } catch (WebDriverException e) {
                test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable());
            }
        }

        // DB
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        dtf.format(now);

        // Emailable Test Summary
        // reporter.report(date_time, "LM", buildNo, environment, testName, deviceName, platForm,
        // "FAIL",
        // testResult.getThrowable().toString());

//    }
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

//        if (testKey != null) {
//            String start = testResult.getAttribute("start").toString();
//            String finish = dateANDtime;
//            jiraReporter.addTest(testKey, start, finish, "FAIL", testName);
//        }

        ExtentManager.createReportFromJson(Constants.EXTENT_JSON_REPORT,Constants.EXTENT_HTML_REPORT);
    }

    @Override
    public synchronized void onTestSkipped(ITestResult testResult) {
        /*
         * get device details
         */
        Map<String, String> testParams =
                testResult.getTestContext().getCurrentXmlTest().getAllParameters();
        String udid = testParams.get("udid");
//        DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
//        String deviceName = deviceInfoReader.getString("name");

//    Logger log = LoggerManager.getLogger();
        if(udid!=null)
            log.warn("Test Skipped : " + testResult.getMethod().getMethodName() + " : " + udid );
        else
            log.warn("Test Skipped : " + testResult.getMethod().getMethodName());

        try {
            ExtentReports extent = ExtentTestManager.getTest().getExtent();
            extent.removeTest(ExtentTestManager.getTest());
        } catch (Exception e) {
            // ignore
        }

    }

    @Override
    public void onStart(ISuite suite) {
//    reporter.initialize();// Emailable Report

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateANDtime = sdf.format(date.getTime());

        // Jira report
        String buildNo = System.getenv("BUILD_NUMBER");
        String jobName = System.getenv("JOB_NAME");
        String buildUrl = System.getenv("BUILD_URL");
        String testPlanKey = System.getenv("TEST_PLAN_KEY");
        String excutionSummary = jobName + buildNo;
        String excutionDescription = buildUrl;
        String startDate = dateANDtime;
//        jiraReporter.setTestExecutionInfo("summary", excutionSummary);
//        jiraReporter.setTestExecutionInfo("testPlanKey", testPlanKey);// "MAEAUTO-311"
//        jiraReporter.setTestExecutionInfo("description", excutionDescription);
//        jiraReporter.setTestExecutionInfo("startDate", startDate);

    }

    @Override
    public void onFinish(ISuite suite) {
//    String emailReport = Constants.EMAIL_REPORT;
//    File file = new File(emailReport);
//    if (file.delete()) {
//      // delete if exists
//    }
//    String buildNo = System.getenv("BUILD_NUMBER");
//    if (buildNo != null) {
//      reporter.writeResults(emailReport);
//    }

        ExtentSparkReporter spark = new ExtentSparkReporter(Constants.EXTENT_HTML_REPORT)
                .viewConfigurer()
                .viewOrder().as(new ViewName[]{ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
                        ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
                .apply();

        ExtentReports extent = new ExtentReports();
        try {
            extent.createDomainFromJsonArchive(Constants.EXTENT_JSON_REPORT);

            extent.attachReporter(spark);
            extent.flush();

        } catch (IOException e) {
//            throw new CombinerException("Exception in creating merged JSON report.", e);
            System.out.println("Exception in creating merged JSON report."+e);
        }



        // Jira report
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateANDtime = sdf.format(date.getTime());
        String finishDate = dateANDtime;
//        jiraReporter.setTestExecutionInfo("finishDate", finishDate);
//        jiraReporter.addInfo();
//        jiraReporter.addTests();
//        jiraReporter.CreatejiraReport(Constants.JIRA_REPORT);

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
