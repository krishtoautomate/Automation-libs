package com.base;

import com.DataManager.TestDataManager;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * Created by Krish on 06.06.2018. Modified : 11.06.2018
 */
public class TestBaseWeb {

    public static Logger log;
    public WebDriver driver;
    protected DriverManager tlDriverFactory = new DriverManager();
    protected ExtentTest test;

    @BeforeSuite
    public void setupSuit(ITestContext ctx) {

//    String suiteName = ctx.getCurrentXmlTest().getSuite().getName();
//
//    // Log4j
        log = Logger.getLogger(this.getClass().getName());

    }


    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public synchronized void setupTest(@Optional String browser, ITestContext iTestContext,
                                       Method method) throws MalformedURLException {

        String className = this.getClass().getName();
        String methodName = method.getName();

//    log = LoggerManager.startLogger(className);

        // Set & Get ThreadLocal Driver with Browser
        tlDriverFactory.setDriver("Web");
        driver = DriverManager.getDriverInstance("Web");

        // Create Test in extent-Report
        test = ExtentTestManager.startTest(methodName);

        Map<String, String> testParams = iTestContext.getCurrentXmlTest().getAllParameters();
        String pTestData = testParams.get("p_Testdata");
        TestDataManager testData = new TestDataManager(pTestData);
        String testKey = testData.get("testKey");
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("testKey", testKey);

        log.info("Test Details : " + className);
        String[][] data = {{"<b>TestCase : </b>", className}, {"<b>Browser : </b>", browser},
                {"<b>Jira test-key : </b>",
                        "<a href=" + Constants.JIRA_URL + testKey + ">" + testKey + "</a>"}};

        test.info(MarkupHelper.createTable(data));

        log.info("Test Started : " + className);

    }


    @Parameters({"browser"})
    @AfterMethod(alwaysRun = true)
    public synchronized void tearDown(ITestContext context, String browser) {

        log.info("AfterTest : " + context.getCurrentXmlTest().getName());

        if (driver != null) {
            try {
                driver.close();
            } catch (Exception ign) {
                // ignore
            }

            try {
                driver.quit();
            } catch (Exception ign) {
                // ignore
            }

            try {
                ExtentTestManager.getTest().getExtent().flush();
            } catch (Exception ign) {
                // ignore
            } finally {
                log.info(Constants.EXTENT_HTML_REPORT);
            }
        }
        test.log(Status.INFO, "Test Completed : " + context.getCurrentXmlTest().getName());
    }

    @AfterSuite(alwaysRun = true)
    public void endSuit() {
        try {
            ExtentTestManager.getTest().getExtent().flush();
        } catch (Exception e) {
            // ignore
        } finally {
            log.info(Constants.EXTENT_HTML_REPORT);
        }
    }

}
