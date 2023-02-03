package com.base;

import com.DataManager.DeviceInfoReader;
import com.DataManager.TestDataManager;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Krish on 06.06.2018.
 */
public class TestBaseHybrid {

    protected static Logger log;
    protected WebDriver webDriver;
    protected AppiumDriver driver;
    protected AppiumDriverManager tlDriverFactoryApp = new AppiumDriverManager();
    protected DriverManager tlDriverFactoryWeb = new DriverManager();
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

    @BeforeMethod
    @Parameters({"udid", "platForm", "browser"})
    public synchronized void Before(@Optional String udid, @Optional String platForm, @Optional String browser,
                                    ITestContext iTestContext, Method method) throws Exception {

        String methodName = method.getName();
        String className = this.getClass().getName();

        if (udid != null) {
            isAndroid = platForm.equalsIgnoreCase("Android");
            isIos = platForm.equalsIgnoreCase("iOS");
            UdidMapper.setUdid(udid);
            tlDriverFactoryApp.setDriver(iTestContext);
            driver = AppiumDriverManager.getDriverInstance();
        }

        tlDriverFactoryWeb.setDriver("Web");
        webDriver = DriverManager.getDriverInstance("Web");

        /*
         * Test info
         */
        String deviceName = "";
        if (udid != null) {
            if ("Auto".equalsIgnoreCase(udid)) {
                udid = driver.getCapabilities().getCapability("udid").toString();
            }
            iTestContext.setAttribute("udid", udid);

            DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
            deviceName = deviceInfoReader.getString("name");


            udid = driver.getCapabilities().getCapability("udid").toString();

            try {
                isFrench = driver.getCapabilities().getCapability("language").toString().equalsIgnoreCase("fr");
            } catch (Exception e) {
                isFrench = false;
            }
        }

        Map<String, String> testParams = iTestContext.getCurrentXmlTest().getAllParameters();
        String pTestData = testParams.get("p_Testdata");
        TestDataManager testData = new TestDataManager(pTestData);
        String testKey = testData.get("testKey");
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("testKey", testKey);

        // Report Content
        test = ExtentTestManager.startTest(methodName);
        test.assignDevice(deviceName);

        String[][] data = {{"<b>TestCase : </b>", className},
//                {"<b>Device-Name : </b>", deviceName},
//                {"<b>UDID : </b>", udid},
//                {"<b>Platform : </b>", platForm},
                {"<b>Jira test-key : </b>",
                        "<a href=" + Constants.JIRA_URL + testKey + ">" + testKey + "</a>"}};

        test.info(MarkupHelper.createTable(data));

        log.info("Test started : " + className);
    }


    @AfterMethod(alwaysRun = true)
    @Parameters({"udid"})
    public synchronized void After(@Optional String udid) {

        if (webDriver != null) {
            try {
                webDriver.close();
            } catch (Exception ign) {
                // ignore
            }

            try {
                DriverManager.quit("Web");
            } catch (Exception ign) {
                // ignore
            }
        }

        if (driver != null) {
            try {
                if (isAndroid) {
                    driver.close();
                }

                if (isIos) {
                    ((IOSDriver) driver).terminateApp(driver.getCapabilities().getCapability("bundleId").toString());
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
            ExtentTestManager.getTest().info("THE END");
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
