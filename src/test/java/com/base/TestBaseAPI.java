package com.base;

import java.io.File;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

/**
 * Created by Krish on 06.06.2018.
 */
public class TestBaseAPI {

  protected static Logger log;
  protected ExtentSparkReporter htmlReporter;
  protected static ExtentReports extent;
  protected ExtentTest test;

  public synchronized ExtentTest getExtentTest() {
    return test;
  }

  public synchronized Logger getLog() {
    return log;
  }

  public synchronized ExtentReports getExtentReports() {
    return extent;
  }

  /**
   * Executed once before all the tests
   */
  @BeforeSuite(alwaysRun = true)
  public void setupSuit(ITestContext ctx) {

    String suiteName = ctx.getCurrentXmlTest().getSuite().getName();

    // Log4j
    log = Logger.getLogger(suiteName);

    // create Report Folder in 'test-output'
    File reportDir = new File(Constants.REPORT_DIR);
    if (!reportDir.exists()) {
      reportDir.mkdirs();
      log.info("created Folder for Report: " + reportDir.getAbsolutePath().toString());
    }

    // extent report
    extent = new ExtentReports();
    htmlReporter = new ExtentSparkReporter(Constants.EXTENT_HTML_REPORT).viewConfigurer()
        .viewOrder().as(new ViewName[] {ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
            ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
        .apply();
    extent.attachReporter(htmlReporter);

    htmlReporter.config().setDocumentTitle("AUTOMATION REPORT");
    htmlReporter.config().setReportName("AUTOMATION REPORT");
    htmlReporter.config().setTheme(Theme.STANDARD);

    // HOST INFO
    extent.setSystemInfo("OS", Constants.HOST_OS);
    extent.setSystemInfo("HostIPAddress", Constants.HOST_IP_ADDRESS());
    extent.setSystemInfo("Host Name", Constants.HOST_NAME());

  }

  @BeforeMethod
  @Parameters({"udid"})
  public synchronized void BeforeClass(@Optional String udid, ITestContext iTestContext,
      Method method) {

    String methodName = method.getName();
    String className = this.getClass().getName();

    test = extent.createTest(methodName);
    test.info("TestCase : " + className);

  }

  /**
   * Executed once after all the tests
   */
  @AfterSuite(alwaysRun = true)
  public void endSuit(ITestContext ctx) {

    try {
      extent.flush(); // -----close extent-report
      log.info(Constants.EXTENT_HTML_REPORT);
    } catch (Exception e) {
      // ignore
    }
  }
}
