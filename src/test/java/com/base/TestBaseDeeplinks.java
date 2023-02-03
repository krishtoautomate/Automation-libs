package com.base;

import com.DataManager.DeviceInfoReader;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Krish on 06.06.2018.
 */
public class TestBaseDeeplinks {

    protected static Logger log;

    protected AppiumDriver driver;
    protected AppiumDriverManager tlDriverFactory = new AppiumDriverManager();
    protected ExtentTest test;
    protected boolean isAndroid = false;
    protected boolean isIos = false;

    /**
     * Executed once before all the tests
     */
    @BeforeSuite(alwaysRun = true)
    public void setupSuit(ITestContext ctx) {

//    String suiteName = ctx.getCurrentXmlTest().getSuite().getName();

        // Log4j
        log = Logger.getLogger(this.getClass().getName());

    }

    @BeforeMethod
    @Parameters({"udid", "platForm"})
    public synchronized void BeforeMethod(@Optional String udid, @Optional String platForm,
                                          ITestContext iTestContext, Method method) throws Exception {

        String methodName = method.getName();
        String className = this.getClass().getName();

        isAndroid = platForm.equalsIgnoreCase("Android");
        isIos = platForm.equalsIgnoreCase("iOS");

        /*
         * Test info
         */
        String deviceName = "";
//        String platformVersion = "";
        if (udid != null) {
            iTestContext.setAttribute("udid",udid);
            tlDriverFactory.setDriver(iTestContext);
            driver = AppiumDriverManager.getDriverInstance();

            if ("Auto".equalsIgnoreCase(udid)) {
                udid = driver.getCapabilities().getCapability("udid").toString();
            }
            iTestContext.setAttribute("udid", udid);

            DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
            deviceName = deviceInfoReader.getString("name");

            udid = driver.getCapabilities().getCapability("udid").toString();

            iTestContext.setAttribute("udid", udid);

//            DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
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

    @AfterMethod(alwaysRun = true)
    public synchronized void AfterMethod(ITestContext testctx) {
        driver = AppiumDriverManager.getDriverInstance();
        if (driver != null) {
            try {
                boolean isAndroid = driver instanceof AndroidDriver;
                if (isAndroid) {
                    ((AndroidDriver) driver).closeApp();
                } else {
                    String bundleId = getIOSActiveAppInfo();
                    ((IOSDriver) driver)
                            .terminateApp(bundleId);
//                    HashMap<String, String> args = new HashMap<>();
//                    args.put("bundleId", driver.getCapabilities().getCapability("bundleId").toString());
//                    driver.executeScript("mobile:terminateApp", args);
                }
                log.info("app close");
            } catch (Exception e) {
                // ignore
            }
            ExtentTestManager.getTest().info("THE END");
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

    public String getIOSActiveAppInfo() {
        String activeApp = "";
        try {
            String jsonResponse = driver.executeScript("mobile:activeAppInfo").toString();
            activeApp = (jsonResponse.split("bundleId=")[1]).replaceAll("}", "");
        } catch (Exception e) {
            //ignore
        }
        log.info("Active-app :" + activeApp);
        return activeApp;
    }

}
