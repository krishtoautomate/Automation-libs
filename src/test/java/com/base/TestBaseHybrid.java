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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
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
    protected AppiumDriver appiumDriver;
    protected WebDriver webDriver;
    protected DriverManager tlDriverFactory = new DriverManager();
    protected ExtentTest test;
    protected boolean isAndroid = false;
    protected boolean isIos = false;
    protected boolean isFrench = false;
    String sessionId;

    /**
     * Executed once before all the tests
     */
    @BeforeSuite(alwaysRun = true)
    public void setupSuit(ITestContext ctx) {
//        String suiteName = ctx.getCurrentXmlTest().getSuite().getName();

        log = Logger.getLogger(this.getClass().getName());
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"udid", "platForm", "browser"})
    public synchronized void Before(@Optional String udid, @Optional String platForm, @Optional String browser,
                                    ITestContext iTestContext, Method method) {

        String methodName = method.getName();
        String className = this.getClass().getName();

        tlDriverFactory.setDriver("Web");
        webDriver = DriverManager.getWebDriverInstance();

        sessionId = String.valueOf(((RemoteWebDriver) webDriver).getSessionId());

        browser = ((RemoteWebDriver) webDriver).getCapabilities().getCapability("browserName").toString();

        String[][] webTable = {
                {"<b>TestCase : </b>", className},
                {"<b>Browser : </b>", browser},
                {"<b>SessionId : </b>", sessionId}
        };

        test = ExtentTestManager.startTest(methodName);

        test.info(MarkupHelper.createTable(webTable));

        if (platForm != null) {
            isAndroid = platForm.equalsIgnoreCase("Android");
            isIos = platForm.equalsIgnoreCase("iOS");
            GlobalMapper.setUdid(udid);
            tlDriverFactory.setDriver("Appium");
            appiumDriver = DriverManager.getAppiumDriverInstance();


            udid = appiumDriver.getCapabilities().getCapability("udid").toString();
            String deviceName = appiumDriver.getCapabilities().getCapability("deviceName").toString();
            String platformVersion = appiumDriver.getCapabilities().getCapability("platformVersion").toString();//appium:platformVersion

            iTestContext.setAttribute("udid", udid);

            try {
                isFrench = appiumDriver.getCapabilities().getCapability("language").toString().equalsIgnoreCase("fr");
            } catch (Exception e) {
                isFrench = false;
            }


            Map<String, String> testParams = iTestContext.getCurrentXmlTest().getAllParameters();
            String pTestData = testParams.get("p_Testdata");
            TestDataManager testData = new TestDataManager(pTestData);
            String testKey = "NA";
            testKey = testData.get("testKey");
            testData.get("testKey");
            ITestResult result = Reporter.getCurrentTestResult();
            result.setAttribute("testKey", testKey);

            // Report Content
            test.assignDevice(deviceName);

            String[][] mobileTable = {{"<b>TestCase : </b>", className},
                    {"<b>Device-Name : </b>", deviceName},
                    {"<b>UDID : </b>", udid},
                    {"<b>Platform : </b>", platForm},
                    {"<b>OsVersion : </b>", platformVersion},
                    {"<b>Jira test-key : </b>",
                            "<a href=" + Constants.JIRA_URL + testKey + ">" + testKey + "</a>"}};

            test.info(MarkupHelper.createTable(mobileTable));
        }

        log.info("Test started : " + className);
    }


    @AfterMethod(alwaysRun = true)
    public synchronized void After() {

        webDriver = DriverManager.getWebDriverInstance();
        if (webDriver != null) {
            try {
                webDriver.close();
            } catch (Exception ign) {
                // ignore
            } finally {
                try {
                    DriverManager.getWebDriverInstance().quit();
                } catch (Exception ign) {
                    // ignore
                }
            }
        }

        appiumDriver = DriverManager.getAppiumDriverInstance();
        if (appiumDriver != null) {
            try {
                if (isAndroid) {
                    ((AndroidDriver) appiumDriver).closeApp();
                }

                if (isIos) {
                    ((IOSDriver) appiumDriver).terminateApp(appiumDriver.getCapabilities().getCapability("bundleId").toString());
                }
                log.info("app close");
            } catch (Exception e) {
                // ignore
            }

            try {
                DriverManager.getAppiumDriverInstance().quit();
                log.info("driver quit - done");
            } catch (Exception e) {
                // ignore
            }
        }

        log.info("THE END");

        try {
            ExtentTestManager.getTest().info("THE END");
            ExtentTestManager.flush();
        } catch (Exception e) {
            // ignore
        }
        finally {
            log.info(Constants.EXTENT_HTML_REPORT);
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
        finally {
            log.info(Constants.EXTENT_HTML_REPORT);
        }
    }

}
