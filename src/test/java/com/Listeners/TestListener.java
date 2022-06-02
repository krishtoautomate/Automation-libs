package com.Listeners;

import com.DataManager.DeviceInfoReader;
import com.ReportManager.ExtentTestManager;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.ReportManager.ReportBuilder;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.Jira;
import com.base.ScreenShotManager;
import com.base.TestBase;
import com.deviceinformation.DeviceInfo;
import com.deviceinformation.DeviceInfoImpl;
import com.deviceinformation.device.DeviceType;
import com.deviceinformation.model.Device;
import io.appium.java_client.AppiumDriver;

public class TestListener extends TestBase implements ISuiteListener, ITestListener, IInvokedMethodListener {
//extends TestListenerAdapter
  protected ReportBuilder reporter = new ReportBuilder();

  Jira jiraReporter = new Jira();

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
    String platForm = testParams.get("platForm");

    ExtentTest test = ExtentTestManager.getTest();

    // Categories
    test.assignCategory(platForm);


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

    Object testClass = testResult.getInstance();
    AppiumDriver driver = ((TestBase) testClass).getDriver();
    Logger log = ((TestBase) testClass).getLog();
    ExtentTest test = ExtentTestManager.getTest();
    DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
    String deviceName = deviceInfoReader.getString("name");

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

      // Categories
      test.assignCategory(platForm);

      // DB
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();
      dtf.format(now);

      // Emailable Test Summary
      // reporter.report(date_time, "LM", buildNo, environment, testName, deviceName, platForm,
      // "FAIL",
      // testResult.getThrowable().toString());

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
    DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
    String deviceName = deviceInfoReader.getString("name");

    Object testClass = testResult.getInstance();
    Logger log = ((TestBase) testClass).getLog();
    log.warn("Test Skipped : " + testResult.getMethod().getMethodName() + " : " + udid + "_"
        + deviceName);

    // ExtentTest test = ((TestBase) testClass).getExtentTest();
    // ExtentReports extent = ((TestBase) testClass).getExtentReports();
    //
    // try {
    // extent.removeTest(test);
    // } catch (Exception e) {
    // // ignore
    // }
  }

  @Override
  public void onStart(ISuite suite) {
    reporter.initialize();// Emailable Report

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
    jiraReporter.setTestExecutionInfo("summary", excutionSummary);
    jiraReporter.setTestExecutionInfo("testPlanKey", testPlanKey);// "MAEAUTO-311"
    jiraReporter.setTestExecutionInfo("description", excutionDescription);
    jiraReporter.setTestExecutionInfo("startDate", startDate);

  }

  @Override
  public void onFinish(ISuite suite) {
    String emailReport = Constants.EMAIL_REPORT;
    File file = new File(emailReport);
    if (file.delete()) {
      // delete if exists
    }
    String buildNo = System.getenv("BUILD_NUMBER");
    if (buildNo != null)
      reporter.writeResults(emailReport);

    // Jira report
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    sdf.setTimeZone(TimeZone.getTimeZone("EST"));
    String dateANDtime = sdf.format(date.getTime());
    String finishDate = dateANDtime;
    jiraReporter.setTestExecutionInfo("finishDate", finishDate);
    jiraReporter.addInfo();
    jiraReporter.addTests();
    jiraReporter.CreatejiraReport(Constants.JIRA_REPORT);

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
