package com.base;

import com.DataManager.TestDataManager;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.lang.reflect.Method;
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
                                       Method method) {

        String className = this.getClass().getName();
        String methodName = method.getName();

//    log = LoggerManager.startLogger(className);

        // Set & Get ThreadLocal Driver with Browser
        iTestContext.setAttribute("platform", "Web");
        tlDriverFactory.setDriver("Web");
        driver = DriverManager.getWebDriverInstance();

//        System.out.println("SessionId : " + ((RemoteWebDriver) driver).getSessionId());

        // Create Test in extent-Report
        test = ExtentTestManager.startTest(methodName);

        Map<String, String> testParams = iTestContext.getCurrentXmlTest().getAllParameters();
        String pTestData = testParams.get("p_Testdata");
        TestDataManager testData = new TestDataManager(pTestData);
        String testKey = testData.get("testKey");
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("testKey", testKey);
        browser = browser == null ? testParams.get("platForm") : testParams.get("browser");
        String sessionId = String.valueOf(((RemoteWebDriver) driver).getSessionId());

        String[][] data = {
                {"<b>TestCase : </b>", className},
                {"<b>Browser : </b>", browser},
                {"<b>SessionId : </b>", sessionId},
                {"<b>Jira test-key : </b>",
                        "<a href=" + Constants.JIRA_URL + testKey + ">" + testKey + "</a>"}
        };

        test.info(MarkupHelper.createTable(data));

        log.info("Test Started : " + className);
    }

    @AfterMethod(alwaysRun = true)
    public synchronized void tearDown(ITestContext context) {

        log.info("AfterTest : " + context.getCurrentXmlTest().getName());

        driver = DriverManager.getWebDriverInstance();
        if (driver != null) {
            try {
                DriverManager.getWebDriverInstance().close();
            } catch (Exception ign) {
                // ignore
            }

            try {
                DriverManager.getWebDriverInstance().quit();
            } catch (Exception ign) {
                // ignore
            }

            try {
                ExtentTestManager.flush();
            } catch (Exception ign) {
                // ignore
            }
            finally {
                log.info(Constants.EXTENT_HTML_REPORT);
            }
        }

        if (test != null)
            test.log(Status.INFO, "Test Completed : " + context.getCurrentXmlTest().getName());

    }

    @AfterSuite(alwaysRun = true)
    public void endSuit() {
        try {
            ExtentTestManager.flush();
        } catch (Exception e) {
            // ignore
        } finally {
            log.info(Constants.EXTENT_HTML_REPORT);
        }
    }

}
