package com.base;

import com.DataManager.DeviceInfoReader;
import com.DataManager.TestDataManager;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Created by Krish on 06.06.2018.
 */
public class TestBase {

  @SuppressWarnings("rawtypes")
  protected AppiumDriver driver;
  protected TLDriverFactory tlDriverFactory = new TLDriverFactory();
  protected static Logger log;
  protected ExtentTest test;
  boolean isAndroid = false;

  public synchronized AppiumDriver getDriver() {
    return driver;
  }
  public Logger getLog() {
    return log;
  }

  /**
   * Executed once before all the tests
   */
  @BeforeSuite(alwaysRun = true)
  public void setupSuit(ITestContext ctx) {

//    ctx.getCurrentXmlTest().getSuite().getName();

    // Log4j
    // log = Logger.getLogger(suiteName);

    // Logback
    log = LoggerFactory.getLogger(this.getClass());
  }

  @SuppressWarnings("unchecked")
  @BeforeMethod
  @Parameters({"udid", "platForm"})
  public synchronized void BeforeClass(@Optional String udid, @Optional String platForm,
      ITestContext iTestContext, Method method) throws MalformedURLException {

    String methodName = method.getName();
    String className = this.getClass().getName();
    isAndroid = platForm.equalsIgnoreCase("Android");

    // Create Session
    log.info("creating session : " + className + " : " + udid);
    tlDriverFactory.setDriver();
    driver = tlDriverFactory.getDriverInstance();

    /*
     * Test info
     */
    if ("Auto".equalsIgnoreCase(udid)) {
      udid =
          ((AppiumDriver<MobileElement>) driver).getCapabilities().getCapability("udid").toString();

    }

    iTestContext.setAttribute("udid", udid);

    Map<String, String> testParams = iTestContext.getCurrentXmlTest().getAllParameters();
    String pTestData = testParams.get("p_Testdata");
    TestDataManager testData = new TestDataManager(pTestData);
    int index = driver instanceof AndroidDriver ? 0 : 1;
    String testKey = testData.getJsonValue(index, "testKey");
    ITestResult result = Reporter.getCurrentTestResult();
    result.setAttribute("testKey", testKey);

    DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
    String deviceName = deviceInfoReader.getString("name");
    String platformVersion = ((AppiumDriver<MobileElement>) driver).getCapabilities()
        .getCapability("platformVersion").toString();

    // Report Content
    test = ExtentTestManager.startTest(methodName + "(" + platForm + ")");

    log.info("Test Details : " + className + " : " + platForm + " : " + deviceName);
    test.assignDevice(deviceName);

    String[][] data = {{"<b>TestCase : </b>", className}, {"<b>Device : </b>", deviceName},
        {"<b>UDID : </b>", udid}, {"<b>Platform : </b>", platForm},
        {"<b>OsVersion : </b>", platformVersion}, {"<b>Jira test-key : </b>",
        "<a href=" + Constants.JIRA_URL + testKey + ">" + testKey + "</a>"}};

    test.info(MarkupHelper.createTable(data));
  }

  /**
   * Executed after Class
   */
  @SuppressWarnings("unchecked")
  @AfterMethod
  @Parameters({"udid", "platForm"})
  public synchronized void AfterClass(@Optional String udid, @Optional String platForm) {

    if (driver != null) {
      try {
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
        tlDriverFactory.quit();
        log.info("driver quit - done");
      } catch (Exception e) {
        // ignore
      }

    }

    try {
      ExtentTestManager.getTest().getExtent().flush();
      log.info(Constants.EXTENT_HTML_REPORT);
    } catch (Exception ign) {
      // ignore
    }
  }

  /**
   * Executed once after all the tests
   */
  @AfterSuite(alwaysRun = true)
  public void endSuit() {

    try {
      ExtentTestManager.getTest().getExtent().flush(); // -----close extent-report
      log.info(Constants.EXTENT_HTML_REPORT);
    } catch (Exception e) {
      // ignore
    }
  }

}
