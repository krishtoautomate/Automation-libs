package com.base;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.logging.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.DataManager.DeviceInfoReader;
import com.DataManager.TestDataManager;
import com.ReportManager.ExtentTestManager;
import com.ReportManager.LoggerManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by Krish on 06.06.2018.
 */
public class TestBase {

  protected static Logger log;
  protected AppiumDriver<MobileElement> driver;
  protected AppiumDriverManager tlDriverFactory = new AppiumDriverManager();
  protected ExtentTest test;
  protected boolean isAndroid = false;


  /**
   * Executed once before all the tests
   */
  @BeforeSuite(alwaysRun = true)
  public void setupSuit(ITestContext ctx) {
    // String suiteName = ctx.getCurrentXmlTest().getSuite().getName();
  }

  @SuppressWarnings("unchecked")
  @BeforeMethod
  @Parameters({"udid", "platForm"})
  public synchronized void Before(@Optional String udid, @Optional String platForm,
      ITestContext iTestContext, Method method) throws MalformedURLException {

    String methodName = method.getName();
    String className = this.getClass().getName();
    isAndroid = platForm.equalsIgnoreCase("Android");

    log = LoggerManager.startLogger(className);

    // Create Session
    log.info("creating session : " + className + " : " + udid);
    tlDriverFactory.setDriver();
    driver = AppiumDriverManager.getDriverInstance();

    /*
     * Test info
     */
    if ("Auto".equalsIgnoreCase(udid)) {
      udid = driver.getCapabilities().getCapability("udid").toString();
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
    String platformVersion = driver.getCapabilities().getCapability("platformVersion").toString();

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


  @AfterMethod
  @Parameters({"udid", "platForm"})
  public synchronized void After(@Optional String udid, @Optional String platForm) {

    if (driver != null) {
      try {
        if (isAndroid) {
          driver.closeApp();
        } else {
          driver.terminateApp(driver.getCapabilities().getCapability("bundleId").toString());
        }
        log.info("app close");
      } catch (Exception e) {
        // ignore
      }

      try {
        AppiumDriverManager.quit();
        log.info("driver quit - done");
      } catch (Exception e) {
        // ignore
      }
      test.info("THE END");
      log.info("THE END");

      try {
        ExtentTestManager.getTest().getExtent().flush();
      } catch (Exception e) {
        // ignore
      } finally {
        log.info(Constants.EXTENT_HTML_REPORT);
      }
    }
  }

  /**
   * Executed once after all the tests
   */
  @AfterSuite(alwaysRun = true)
  public void endSuit() {

    try {
      ExtentTestManager.getTest().getExtent().flush(); // -----close extent-report
    } catch (Exception e) {
      // ignore
    } finally {
      log.info(Constants.EXTENT_HTML_REPORT);
    }
  }

}
