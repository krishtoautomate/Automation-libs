package com.base;

import com.DataManager.TestDataManager;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
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
 * Created by Krish on 06.06.2018. Modified : 11.06.2018
 */
public class TestBaseWeb {

  public static Logger log;
  public WebDriver driver;
  protected TLDriverFactoryWeb tlDriverFactory = new TLDriverFactoryWeb();
  protected ExtentTest test;

  @BeforeSuite
  public void setupSuit(ITestContext ctx) {

    // String suiteName = ctx.getCurrentXmlTest().getSuite().getName();

    // Create Log4J
    // log = Logger.getLogger(suiteName);

    // Logback
//    log = LoggerFactory.getLogger(this.getClass());

    log = LogManager.getLogManager().getLogger(this.getClass().getSimpleName());

  }


  @BeforeMethod(alwaysRun = true)
  @Parameters({"browser"})
  public synchronized void setupTest(@Optional String browser, ITestContext iTestContext,
      Method method) {

    // get TestName, ClassName and MethodName
    // String testName = Testctx.getCurrentXmlTest().getName();
    String className = this.getClass().getName();
    String methodName = method.getName();

    // Set & Get ThreadLocal Driver with Browser
    tlDriverFactory.setDriver();
    driver = tlDriverFactory.getDriverInstance();
//    driver = tlDriverFactory.getDriver();
//    driverMap.put(Thread.currentThread().getId(), tlDriverFactory.getDriver());
//    driver = driverMap.get(Long.valueOf(Thread.currentThread().getId()));

    // Create Test in extent-Report
    test = ExtentTestManager.startTest(methodName);

    Map<String, String> testParams = iTestContext.getCurrentXmlTest().getAllParameters();
    String pTestData = testParams.get("p_Testdata");
    TestDataManager testData = new TestDataManager(pTestData);
    int index = 0;
    String testKey = testData.getJsonValue(index, "testKey");
    ITestResult result = Reporter.getCurrentTestResult();
    result.setAttribute("testKey", testKey);

    log.info("Test Details : " + className);
    String[][] data = {{"<b>TestCase : </b>", className}, {"<b>Platform : </b>", browser},
        {"<b>Jira test-key : </b>",
            "<a href=" + Constants.JIRA_URL + testKey + ">" + testKey + "</a>"}};

    test.info(MarkupHelper.createTable(data));

    log.info("Test Started : " + className);

    test.log(Status.INFO, methodName);

  }


  @Parameters({"browser"})
  @AfterMethod()
  public synchronized void tearDown(ITestContext context, String browser) {
    log.info("|--AfterClass--|");
    log.info("AfterTest : " + context.getCurrentXmlTest().getName());

    // Removes the skipped tests
    // Iterator<ITestResult> skippedTestCases =
    // context.getSkippedTests().getAllResults().iterator();
    // while (skippedTestCases.hasNext()) {
    // ITestResult skippedTestCase = skippedTestCases.next();
    // ITestNGMethod method = skippedTestCase.getMethod();
    // if (context.getSkippedTests().getResults(method).size() > 0) {
    // log.info("Removing:" + skippedTestCase.getTestClass().toString());
    // skippedTestCases.remove();
    // }
    // }

    if (driver != null) {
      try {
        driver.close();
        tlDriverFactory.getDriverInstance().quit();
        ExtentTestManager.getTest().getExtent().flush();
      } catch (Exception ign) {
        // ignore
      }
    }
    test.log(Status.INFO, "Test Completed : " + context.getCurrentXmlTest().getName());
  }

  @AfterSuite(alwaysRun = true)
  public void endSuit() {
    try {
      ExtentTestManager.getTest().getExtent().flush();
      log.info(Constants.EXTENT_HTML_REPORT);
    } catch (Exception e) {
      // ignore
    }

  }

}
