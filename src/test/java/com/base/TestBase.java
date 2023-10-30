package com.base;

import com.DataManager.TestDataManager;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Krish on 06.06.2018.
 */
public class TestBase {

    protected static Logger log;
    protected AppiumDriver driver;
    protected DriverManager driverManager = new DriverManager();
    protected ExtentTest test;
    protected boolean isAndroid = false;
    protected boolean isIos = false;
    protected boolean isFrench = false;

    /**
     * Executed once before all the tests
     */
    @BeforeSuite(alwaysRun = true)
    public void setupSuit(ITestContext ctx) {
//        String suiteName = ctx.getCurrentXmlTest().getSuite().getName();


        log = Logger.getLogger(this.getClass().getName());
    }


    @BeforeMethod(alwaysRun = true)
    @Parameters({"udid", "platForm"})
    public synchronized void Before(@Optional String udid, @Optional String platForm,
                                    ITestContext iTestContext, Method method) {

        String methodName = method.getName();
        String className = this.getClass().getName();
        isAndroid = platForm.equalsIgnoreCase("Android");
        isIos = platForm.equalsIgnoreCase("iOS");

        if (udid != null)
            GlobalMapper.setUdid(udid);
        GlobalMapper.setTestName(className);
        driverManager.setDriver("Appium");
        driver = AppiumDriverManager.getDriverInstance();

        try {
            isFrench = driver.getCapabilities().getCapability("language").toString().equalsIgnoreCase("fr");
        } catch (Exception e) {
            isFrench = false;
        }

        Map<String, String> testParams = iTestContext.getCurrentXmlTest().getAllParameters();
        String pTestData = testParams.get("p_Testdata");
        TestDataManager testData = new TestDataManager(pTestData);
        int index = driver instanceof AndroidDriver ? 0 : 1;
        String testKey = testData.get(index, "testKey");
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("testKey", testKey);

        // Report Content
        test = ExtentTestManager.startTest(methodName + "(" + platForm + ")");

        log.info("Test started : " + className);
        String _udid = driver.getCapabilities().getCapability("udid").toString();
        String deviceName = driver.getCapabilities().getCapability("deviceName").toString();
        String platformVersion = driver.getCapabilities().getCapability("platformVersion").toString();
        test.assignDevice(deviceName);

        String[][] data = {{"<b>TestCase : </b>", className}, {"<b>Device-Name : </b>", deviceName},
                {"<b>UDID : </b>", _udid},
                {"<b>Platform : </b>", platForm},
                {"<b>OsVersion : </b>", platformVersion},
                {"<b>Jira test-key : </b>",
                        "<a target=\"blank\" href=" + Constants.JIRA_URL + testKey + ">" + testKey + "</a>"}
        };

        test.info(MarkupHelper.createTable(data));
    }


    @AfterMethod(alwaysRun = true)
    public synchronized void After() {

        if (driver != null) {
            try {
                AppiumDriverManager.quit();
                log.info("driver quit - done");
            } catch (Exception e) {
                // ignore
            }

            try {
                ExtentTestManager.getTest().info("THE END");
                ExtentTestManager.flush();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * Executed once after all the tests
     */
    @AfterSuite(alwaysRun = true)
    public void endSuit() {
        try {
            ExtentTestManager.flush(); // -----close extent-report
        } catch (Exception e) {
            // ignore
        }
    }

}
