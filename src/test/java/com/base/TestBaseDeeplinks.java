package com.base;

import com.DataManager.DeviceInfoReader;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import java.lang.reflect.Method;
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

  @SuppressWarnings("rawtypes")
  protected AppiumDriver driver;
  protected AppiumDriverManager tlDriverFactory = new AppiumDriverManager();
  protected ExtentTest test;

  public synchronized Logger getLog() {
    return log;
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

  }

  @SuppressWarnings("unchecked")
  @BeforeMethod
  @Parameters({"udid", "platForm"})
  public synchronized void BeforeMethod(@Optional String udid, @Optional String platForm,
      ITestContext iTestContext, Method method) throws Exception {

    if (udid != null) {

      String methodName = method.getName();
      String className = this.getClass().getName();

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
      test = ExtentTestManager.startTest(methodName + "(" + platForm + ")")
          .assignDevice(deviceName);

      log.info("Test Details : " + className + " : " + platForm + " : " + deviceName);
      String[][] data = {{"<b>TestCase : </b>", className}, {"<b>Device : </b>", deviceName},
          {"<b>UDID : </b>", udid}, {"<b>Platform : </b>", platForm},
          {"<b>OsVersion : </b>", platformVersion}};

      test.info(MarkupHelper.createTable(data));

    }

  }

  @SuppressWarnings("unchecked")
  @AfterMethod
  public synchronized void AfterMethod(ITestContext testctx) {

    if (driver != null) {
      try {
        boolean isAndroid = driver instanceof AndroidDriver;
        if (isAndroid) {
          ((AndroidDriver<MobileElement>) driver).closeApp();
        } else {
          driver.terminateApp(((AppiumDriver<MobileElement>) driver)
              .getCapabilities().getCapability("bundleId").toString());
        }
        log.info("app close");
      } catch (Exception e) {
        // ignore
      }
      test.info("THE END");
      log.info("THE END");

      try {
        AppiumDriverManager.quit();
        log.info("driver quit - done");
      } catch (Exception e) {
        // ignore
      }

    }

    try {
      ExtentTestManager.getTest().getExtent().flush(); // -----close extent-report
    } catch (Exception e) {
      // ignore
    } finally {
      log.info(Constants.EXTENT_HTML_REPORT);
    }
  }

  /**
   * Executed once after all the tests
   */
  @AfterSuite(alwaysRun = true)
  public void endSuit(ITestContext ctx) {

    try {
      ExtentTestManager.getTest().getExtent().flush(); // -----close extent-report
    } catch (Exception e) {
      // ignore
    } finally {
      log.info(Constants.EXTENT_HTML_REPORT);
    }
  }

}
