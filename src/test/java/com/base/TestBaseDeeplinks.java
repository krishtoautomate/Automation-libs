package com.base;

import com.DataManager.DeviceInfoReader;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Created by Krish on 06.06.2018.
 */
public class TestBaseDeeplinks {

  protected static Logger log;
  protected static ExtentReports extent;
  @SuppressWarnings("rawtypes")
  protected AppiumDriver driver;
  protected TLDriverFactory tlDriverFactory = new TLDriverFactory();
  protected ExtentSparkReporter htmlReporter;
  protected ExtentTest test;


  protected AppiumDriverLocalService server;

  public synchronized AppiumDriver getDriver() {
    return driver;
  }

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

    ctx.getCurrentXmlTest().getSuite().getName();

    // Log4j
    // log = Logger.getLogger(suiteName);

    // Logback
    log = LoggerFactory.getLogger(this.getClass());

    // create Report Folder in 'test-output'
    File reportDir = new File(Constants.REPORT_DIR);
    if (!reportDir.exists()) {
      reportDir.mkdirs();
      log.info("created Folder for Report: " + reportDir.getAbsolutePath().toString());
    }

    // extent report
    extent = new ExtentReports();
    htmlReporter = new ExtentSparkReporter(Constants.EXTENT_HTML_REPORT).viewConfigurer()
        .viewOrder().as(new ViewName[]{ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
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

  @SuppressWarnings("unchecked")
  @BeforeMethod
  @Parameters({"udid", "platForm"})
  public synchronized void BeforeMethod(@Optional String udid, @Optional String platForm,
      ITestContext iTestContext, Method method) throws Exception {

    String methodName = method.getName();
    String className = this.getClass().getName();

    if (udid != null) {

      // Create Session
      log.info("creating session : " + className + " : " + udid);

      tlDriverFactory.setDriver();
      driver = tlDriverFactory.getDriverInstance();

      /*
       * Test info
       */
      if ("Auto".equalsIgnoreCase(udid)) {
        udid = ((AppiumDriver<MobileElement>) driver).getCapabilities().getCapability("udid")
            .toString();
      }

      iTestContext.setAttribute("udid", udid);

      DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
      String deviceName = deviceInfoReader.getString("name");
      String platformVersion = deviceInfoReader.getString("platformVersion");

      // Report Content
      test = extent.createTest(methodName + "(" + platForm + ")").assignDevice(deviceName);

      log.info("Test Details : " + className + " : " + platForm + " : " + deviceName);
      String[][] data = {{"<b>TestCase : </b>", className}, {"<b>Device : </b>", deviceName},
          {"<b>UDID : </b>", udid}, {"<b>Platform : </b>", platForm},
          {"<b>OsVersion : </b>", platformVersion}};

      test.info(MarkupHelper.createTable(data));

    } else {
      test = extent.createTest(methodName);
      test.info("TestCase : " + methodName);
    }

  }

  /**
   * Executed after Class
   */
  @SuppressWarnings("unchecked")
  @AfterMethod
  @Parameters({"udid", "platForm"})
  public synchronized void AfterMethod(@Optional String udid, @Optional String platForm,
      ITestContext Testctx) {

    if (driver != null) {
      try {
        if ("Android".equalsIgnoreCase(platForm)) {
          ((AndroidDriver<MobileElement>) driver).closeApp();
          ((AndroidDriver<MobileElement>) driver).quit();
        } else {
          ((AppiumDriver<MobileElement>) driver).terminateApp(((AppiumDriver<MobileElement>) driver)
              .getCapabilities().getCapability("bundleId").toString());
          ((AndroidDriver<MobileElement>) driver).quit();
        }
        log.info("app close");
      } catch (Exception e) {
        // ignore
      }
      test.info("THE END");
      log.info("THE END");

      try {
        tlDriverFactory.quit();
        log.info("TLdriver quit - done");
      } catch (Exception e) {
        // ignore
      }

    }

    try {
      extent.flush(); // -----close extent-report
      log.info(Constants.EXTENT_HTML_REPORT);
    } catch (Exception e) {
      // ignore
    }
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
