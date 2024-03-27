package com.base;

import com.DataManager.TestDataManager;
import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    @Parameters({"udid", "platForm", "browser"})
    public synchronized void setupTest(@Optional String udid, @Optional String platForm, @Optional String browser, ITestContext iTestContext,
                                       Method method) {

        String className = this.getClass().getName();
        String methodName = method.getName();

        Map<String, String> testParams = iTestContext.getCurrentXmlTest().getAllParameters();
        String pTestData = testParams.get("p_Testdata");
        TestDataManager testData = new TestDataManager(pTestData);

        try {
            JSONObject jObject = (JSONObject) new JSONParser().parse(new FileReader(pTestData));
            org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) jObject.get(className);
            int index = "iOS".equalsIgnoreCase(platForm) ? 1 : 0;
            JSONObject jObjt = (JSONObject) jsonArray.get(index);
            JSONObject jAObject = (JSONObject) jObjt.get("parameters");
            platForm = jAObject.get("platForm").toString();
        } catch (Exception e) {
            //ignore
        }

//    log = LoggerManager.startLogger(className);

        // Set & Get ThreadLocal Driver with Browser
        iTestContext.setAttribute("platform", "Web");

        if(platForm!=null){
            if("Desktop".equalsIgnoreCase(platForm)){
                tlDriverFactory.setDriver("Web");
                driver = DriverManager.getWebDriverInstance();
            }else {
                if (udid != null)
                    GlobalMapper.setUdid(udid);

                tlDriverFactory.setDriver("Appium-Browser");
                driver = DriverManager.getAppiumDriverInstance();
            }
        }else{
            tlDriverFactory.setDriver("Web");
            driver = DriverManager.getWebDriverInstance();
        }

//        System.out.println("SessionId : " + ((RemoteWebDriver) driver).getSessionId());

        // Create Test in extent-Report
        test = ExtentTestManager.startTest(methodName);

        String testKey = testData.get("testKey");
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("testKey", testKey);
        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        browser = caps.getBrowserName();
        String browserVersion = caps.getBrowserVersion();
        String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();

        String[][] data = {
                {"<b>TestCase : </b>", className},
                {"<b>Browser : </b>", browser},
                {"<b>BrowserVersion : </b>", browserVersion},
                {"<b>SessionId : </b>", sessionId},
                {"<b>Jira test-key : </b>",
                        "<a target=\"blank\" href=" + Constants.JIRA_URL + testKey + ">" + testKey +"</a>"}
        };

        test.info(MarkupHelper.createTable(data));

        try {
            String _udid = ((AppiumDriver)driver).getCapabilities().getCapability("udid").toString();
            String deviceName = ((AppiumDriver)driver).getCapabilities().getCapability("deviceName").toString();

            if(_udid!=null){
                String[][] data1 = {
                        {"<b>deviceName : </b>", deviceName},
                        {"<b>udid : </b>", _udid},
                        {"<b>SessionId : </b>", sessionId}

                };

                test.info(MarkupHelper.createTable(data1));
            }
        } catch (Exception e) {
            //ignore
        }

        log.info("Test Started : " + className);
    }

    @AfterMethod(alwaysRun = true)
    @Parameters({"platForm"})
    public synchronized void tearDown(@Optional String platForm, ITestContext context) {

        log.info("AfterTest : " + context.getCurrentXmlTest().getName());

        String className = this.getClass().getName();

        Map<String, String> testParams = context.getCurrentXmlTest().getAllParameters();
        String pTestData = testParams.get("p_Testdata");

        try {
            JSONObject jObject = (JSONObject) new JSONParser().parse(new FileReader(pTestData));
            org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) jObject.get(className);
            int index = "iOS".equalsIgnoreCase(platForm) ? 1 : 0;
            JSONObject jObjt = (JSONObject) jsonArray.get(index);
            JSONObject jAObject = (JSONObject) jObjt.get("parameters");
            platForm = jAObject.get("platForm").toString();
        } catch (Exception e) {
            //ignore
        }

        if(platForm!=null){
            if("Desktop".equalsIgnoreCase(platForm))
                driver = DriverManager.getWebDriverInstance();
            else
                driver = DriverManager.getAppiumDriverInstance();
        }else{
            driver = DriverManager.getWebDriverInstance();
        }
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
                ExtentTestManager.flush();
            } catch (Exception ign) {
                // ignore
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
        }
    }

}
