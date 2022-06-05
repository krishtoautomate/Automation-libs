package com.Listeners;

import com.ReportManager.ExtentTestManager;
import io.appium.java_client.AppiumDriver;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.DataManager.DeviceInfoReader;
import com.DataManager.TestDataManager;
import com.ReportManager.ReportBuilder;
import com.ReportManager.SlackReporter;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.Jira;
import com.base.ScreenShotManager;
import com.base.TestBase;
import io.appium.java_client.android.AndroidDriver;

public class TestListenerRT extends TestBase
    implements ISuiteListener, ITestListener, IInvokedMethodListener {

  protected ReportBuilder reporter = new ReportBuilder();

  Jira jiraReporter = new Jira();
  SlackReporter slackReporter = new SlackReporter();
  private static String testExecutionKey = null;

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
    String udid = testParams.get("udid");
    String platForm = testParams.get("platForm");

    DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
    String deviceName = deviceInfoReader.getString("name");


    String buildNo = System.getenv("BUILD_NUMBER");
    String environment = System.getenv("ENVIRONMENT");
    String testName = testResult.getMethod().getMethodName();
    String className = testResult.getTestClass().getName();

    Object testClass = testResult.getInstance();
    Logger log = ((TestBase) testClass).getLog();
    ExtentTest test = ExtentTestManager.getTest();

    test.log(Status.INFO, testName + " - Completed as Success");

    // Categories
    test.assignCategory(platForm);
    test.assignCategory(deviceName);
    test.assignCategory("Passed");

    // DB update
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String date_time = dtf.format(now);

    reporter.report(date_time, "MVM", buildNo, environment, testName, deviceName, platForm, "PASS",
        "");

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
      if (testExecutionKey != null) {
        jiraReporter.update_Test_Exec(testExecutionKey, testKey, "PASS", start, finish);
        log.info("Test execution " + testExecutionKey + " updated as PASS for test : " + testKey
            + " : " + className);
      }
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

    DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
    String deviceName = deviceInfoReader.getString("name");

    String buildNo = System.getenv("BUILD_NUMBER");
    String environment = System.getenv("ENVIRONMENT");
    String slackChannel = System.getenv("SLACK_CHANNEL");
    String testName = testResult.getMethod().getMethodName();
    String className = testResult.getTestClass().getName();
    String ssPath = null;

    Object testClass = testResult.getInstance();
    AppiumDriver driver = tlDriverFactory.getDriverInstance();
    Logger log = ((TestBase) testClass).getLog();
    ExtentTest test = ExtentTestManager.getTest();

    if (driver != null) {
      log.error("Test failed : " + className + " : " + udid + "_" + deviceName);
      try {
        ScreenShotManager screenShotManager = new ScreenShotManager(driver);
        String ScreenShot = screenShotManager.getScreenshot();
        ssPath = ScreenShot;
        test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable(),
            MediaEntityBuilder.createScreenCaptureFromPath(ScreenShot).build());

        String errorXML = driver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));

      } catch (WebDriverException e) {
        test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable());
      }

      // Categories
      test.assignCategory(platForm);
      test.assignCategory(deviceName);
      test.assignCategory("Failed");

      // DB
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();
      String date_time = dtf.format(now);

      // Emailable Test Summary
      reporter.report(date_time, "MVM", buildNo, environment, testName, deviceName, platForm,
          "FAIL", testResult.getThrowable().toString());

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
      if (testExecutionKey != null) {
        // JIRA
        jiraReporter.update_Test_Exec(testExecutionKey, testKey, "FAIL", start, finish);
        log.info("Test execution " + testExecutionKey + " updated as FAIL for test : " + testKey
            + " : " + className);

        // Slack
        String message = "*" + className + "* : <https://jira.bell.corp.bce.ca/browse/" + testKey
            + "|" + testKey + ">" + " failed due to `" + testResult.getThrowable() + "`";
        slackReporter.send_Failure_Data_To_Channel(message, Constants.REPORT_DIR + ssPath,
            slackChannel);
        log.info("Failure info for " + testKey + " sent to slack channel #" + slackChannel);
      }
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
    String className = testResult.getTestClass().getName();
    // DeviceInfo deviceInfo = new DeviceInfoImpl(DeviceType.ALL);
    // Device device = deviceInfo.getUdid(udid);
    // String deviceName = device.getDeviceName();

    DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
    String deviceName = deviceInfoReader.getString("name");

    String p_Testdata = testParams.get("p_Testdata");
    TestDataManager testData = new TestDataManager(p_Testdata);
    Object testClass = testResult.getInstance();
    AppiumDriver driver = tlDriverFactory.getDriverInstance();
    int index = driver instanceof AndroidDriver ? 0 : 1;

    Logger log = ((TestBase) testClass).getLog();
    log.warn("Test Skipped : " + className + " : " + udid + "_" + deviceName);

    ExtentTest test = ExtentTestManager.getTest();
    ExtentReports extent = ExtentTestManager.getTest().getExtent();

    try {
      extent.removeTest(test);
    } catch (Exception e) {
      // ignore
    }

    // JIRA
    String testKey = null;
    try {
      testKey = testData.getJsonValue(index, "testKey");
    } catch (Exception e) {
      // ignore
    }
    if (testKey != null) {
      if (testExecutionKey != null) {
        jiraReporter.update_Test_Exec(testExecutionKey, testKey, "TODO", "", "");
        log.info("Test execution " + testExecutionKey + " updated as TODO for test : " + testKey
            + " : " + className);
      }
    }
  }

  @Override
  public void onStart(ISuite suite) {
    reporter.initialize();// Emailable Report

    // Create Jira execution
    String buildNo = System.getenv("BUILD_NUMBER");
    String jobName = System.getenv("JOB_NAME");
    String buildUrl = System.getenv("BUILD_URL");
    String testPlanKey = System.getenv("TEST_PLAN_KEY");
    String testExecKey = System.getenv("TEST_EXECUTION");
    String slackChannel = System.getenv("SLACK_CHANNEL");
    String executionSummary = jobName + "-" + buildNo;
    String executionDescription = buildUrl;

    if (jobName != null && testPlanKey != null) {
      // JIRA
      testExecutionKey = testExecKey;
      if (testExecutionKey == null)
        testExecutionKey =
            jiraReporter.create_Test_Exec(executionSummary, executionDescription, testPlanKey);

      // Slack
      String message = ":virgin-mobile: Mobility Run started at: <" + executionDescription + "|"
          + executionSummary + ">" + "\n" + "Execution will be updated at "
          + "<https://jira.bell.corp.bce.ca/browse/" + testExecutionKey + "|" + testExecutionKey
          + ">";
      slackReporter.send_Message_To_Channel(message, slackChannel);
    }
  }

  @Override
  public void onFinish(ISuite suite) {
    String emailReport = Constants.EMAIL_REPORT;
    File file = new File(emailReport);
    if (file.delete()) {
      // delete if exists
    }
    String buildNo = System.getenv("BUILD_NUMBER");
    String buildUrl = System.getenv("BUILD_URL");
    String slackChannel = System.getenv("SLACK_CHANNEL");
    if (buildNo != null) {
      reporter.writeResults(emailReport);
    }

    if (testExecutionKey != null) {
      // Slack
      String message = ":virgin-mobile: Mobility Run has finished. Complete  " + "<" + buildUrl
          + "Automation_20HTML_20Report/|Jenkins Automation Report>"
          + " and <https://jira.bell.corp.bce.ca/browse/" + testExecutionKey
          + "|JIRA Test Execution> are now available";
      slackReporter.send_Message_To_Channel(message, slackChannel);
    }

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
