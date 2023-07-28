package com.Listeners;

import com.DataManager.TestDataManager;
import com.ReportManager.ExtentTestManager;
import com.ReportManager.SlackReporter;
import com.Utilities.Constants;
import com.Utilities.ScreenShotManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.AppiumDriverManager;
import com.base.DriverManager;
import com.base.Jira;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestListenerRT extends TestListenerAdapter
        implements ISuiteListener, ITestListener, IInvokedMethodListener {

    private static Logger log = Logger.getLogger(TestListenerRT.class.getName());

    private static String testExecutionKey = null;
    Jira jiraReporter = new Jira();
    SlackReporter slackReporter = new SlackReporter();

    @Override
    public void onTestStart(ITestResult iTestResult) {

        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            sdf.setTimeZone(TimeZone.getTimeZone("EST"));
            String timeNow = sdf.format(date.getTime());
            iTestResult.setAttribute("start", timeNow);
        } catch (Exception e) {
            // ignore
        }

        try {
            AppiumDriver driver = AppiumDriverManager.getDriverInstance();

            String udid = driver.getCapabilities().getCapability("udid").toString();

//            String[] params = {"udid", udid};
//            iTestResult.setParameters(params);

            log.info("udid : " + udid);
        } catch (Exception e) {
            //ignore
        }
    }

    @Override
    public synchronized void onTestSuccess(ITestResult testResult) {
        try {
            /*
             * get device details
             */

            String testName = testResult.getMethod().getMethodName();
            String className = testResult.getTestClass().getName();

            ExtentTest test = ExtentTestManager.getTest();

            test.log(Status.INFO, testName + " - Completed as Success");

            // Jira
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            sdf.setTimeZone(TimeZone.getTimeZone("EST"));
            String timeNow = sdf.format(date.getTime());

            Map<String, String> testParams =
                    testResult.getTestContext().getCurrentXmlTest().getAllParameters();
            String p_Testdata = testParams.get("p_Testdata");
            TestDataManager testData = new TestDataManager(p_Testdata);
            String testKey = testData.get("testKey");
            try {
                testKey = testResult.getAttribute("testKey").toString();
            } catch (Exception e) {
                testKey = testData.get("testKey");
                log.warn("testKey not found : " + className);
            }

            if (testKey != null) {
                String start = testResult.getAttribute("start").toString();
                String finish = timeNow;
                if (testExecutionKey != null) {
                    jiraReporter.update_Test_Exec(testExecutionKey, testKey.trim(), "PASS", start, finish);
                    log.info("Test execution " + testExecutionKey + " updated as PASS for test : " + testKey
                            + " : " + className);
                }
            }
        } catch (Exception e) {
            log.error("Jira api error");
        }
    }

    @Override
    public synchronized void onTestFailure(ITestResult testResult) {

        /*
         * get device details
         */
        Map<String, String> testParams =
                testResult.getTestContext().getCurrentXmlTest().getAllParameters();
//        String udid = testParams.get("udid");
        String p_Testdata = testParams.get("p_Testdata");
        TestDataManager testData = new TestDataManager(p_Testdata);

        String testName = testResult.getMethod().getMethodName();
        String className = testResult.getTestClass().getName();


        // String slackChannel = System.getenv("SLACK_CHANNEL");

        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
        WebDriver webDriver = DriverManager.getWebDriverInstance();


        ExtentTest test = ExtentTestManager.getTest();

        if (driver != null) {
            String udid = driver.getCapabilities().getCapability("udid").toString();
            log.error("Test failed : " + className + " : " + udid);

            try {
                // Unique name to screen-shot
                String imgPath = ScreenShotManager.getScreenshot(driver);

                test.fail("Failed details : " + testName + "\n" + testResult.getThrowable().getMessage());
                test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());

            } catch (Exception e) {
                test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable());
            }


            try {
                String errorXML = driver.getPageSource();
                test.info(MarkupHelper.createCodeBlock(errorXML));

                if (errorXML.contains("Technical issue") || errorXML.contains(" error")) {
                    test.assignCategory("server errors");//Internal server error
                }

                if (errorXML.contains("com.sec.android.app.launcher") || errorXML.contains("type=\"XCUIElementTypeApplication\" name=\" \")")) {
                    test.assignCategory("Crash");
                }
            } catch (Exception ign) {
//                test.assignCategory("Crash");
                log.info("Failed to get page source : " + "\n" + ign.getLocalizedMessage());
            }
        }

        if (webDriver != null) {
            log.error("Test failed : " + className);

            try {

                String imgPath = ScreenShotManager.getScreenshot(driver);

                test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());

            } catch (Exception e) {
                test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable());
            }

            try {
                String errorXML = webDriver.getPageSource();
                test.info(MarkupHelper.createCodeBlock(errorXML));
            } catch (Exception ign) {
                test.assignCategory("Crash");
                log.info("Failed to get page source : " + "\n" + ign.getLocalizedMessage());
            }
        }


        try {
            // Jira
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            sdf.setTimeZone(TimeZone.getTimeZone("EST"));
            String dateANDtime = sdf.format(date.getTime());

            String testKey = testData.get("testKey");
            try {
                testKey = testResult.getAttribute("testKey").toString();
            } catch (Exception e) {
                log.warn("testKey not found : " + className);
            }

            if (testKey != null) {
                String start = testResult.getAttribute("start").toString();
                String finish = dateANDtime;
                if (testExecutionKey != null) {
                    // JIRA
                    jiraReporter.update_Test_Exec(testExecutionKey, testKey.trim(), "FAIL", start, finish);
                    log.info("Test execution " + testExecutionKey + " updated as FAIL for test : " + testKey
                            + " : " + className);
                }
            }
        } catch (Exception e) {
            log.error("jira api error : " + e.getMessage());
        }


        // // Slack
        // String message = "*" + className + "* : <"+Constants.JIRA_URL +
        // testKey
        // + "|" + testKey + ">" + " failed due to `" + testResult.getThrowable() + "`";
        // slackReporter.send_Failure_Data_To_Channel(message, Constants.REPORT_DIR + ssPath,
        // slackChannel);
        // log.info("Failure info for " + testKey + " sent to slack channel #" + slackChannel);

//        ExtentManager.createReportFromJson(Constants.EXTENT_JSON_REPORT, Constants.EXTENT_HTML_REPORT);

    }

    @Override
    public synchronized void onTestSkipped(ITestResult testResult) {
        /*
         * get device details
         */
        Map<String, String> testParams =
                testResult.getTestContext().getCurrentXmlTest().getAllParameters();
//        String udid = testParams.get("udid");

        String className = testResult.getTestClass().getName();
        String testName = testResult.getName();
        String p_Testdata = testParams.get("p_Testdata");
        TestDataManager testData = new TestDataManager(p_Testdata);

        log.warn("Test Skipped : " + className + "." + testName);

//        try {
////            ExtentTest test = ExtentTestManager.getTest();
//            log.warn("Removing Skipped Test : " + testName);
//            ExtentReports extent = ExtentTestManager.getTest().getExtent();
//            extent.removeTest(testName);
//            extent.flush();
//        } catch (Exception e) {
//            // ignore
//        }

        try {
            // JIRA
            String testKey = testData.get("testKey");
            try {
                testKey = testResult.getAttribute("testKey").toString();
            } catch (Exception e) {
                log.warn("testKey not found : " + className);
                testKey = testData.get("testKey");
            }
            if (testKey != null) {
                if (testExecutionKey != null) {
                    jiraReporter.update_Test_Exec(testExecutionKey, testKey.trim(), "TODO", "", "");
                    System.out.println("----------------------------------------------------------------");
                    log.warn(
                            "Test execution " + testExecutionKey + " updated as TODO for test : " + testKey
                                    + " : " + className);
                    System.out.println("----------------------------------------------------------------");
                }
            }
        } catch (Exception e) {
            log.error("Jira api error : " + e.getMessage());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStart(ISuite suite) {

        try {
            String jsonPath = suite.getParameter("p_Testdata");
            (new JSONParser()).parse(new FileReader(jsonPath));
        } catch (ParseException | NullPointerException | IOException e) {
            log.error("Json data file error");
            throw new RuntimeException("Json file error");
        }

        try {
            // Create Jira execution
            String buildNo = System.getenv("BUILD_NUMBER");
            String jobName = System.getenv("JOB_NAME");
            String jobUrl = System.getenv("JOB_URL");
            String buildUrl = System.getenv("BUILD_URL");
            String testPlanKey = System.getenv("TEST_PLAN_KEY");
            String testExecKey = System.getenv("TEST_EXECUTION");
            String slackChannel = System.getenv("SLACK_CHANNEL");
            String executionSummary = jobName + "-" + buildNo;
            String executionDescription = buildUrl;

            Date myDate = new Date();
            SimpleDateFormat mdyFormat = new SimpleDateFormat("MMddyyyy");
            String dateFolder = mdyFormat.format(myDate);

            String realTimeLink = "<" + jobUrl + "ws/test-output/" + dateFolder
                    + "/" + buildNo + "|Real Time Jenkins Report>";

//            String jiraAuth = System.getenv("JIRA_AUTH");
//            System.out.println("jiraAuth : " + jiraAuth);
            System.out.println("testPlanKey : " + testPlanKey);

            if (testPlanKey != null) {
                // JIRA
                testExecutionKey = testExecKey;
                if (testExecutionKey == null) {
                    testExecutionKey =
                            jiraReporter.create_Test_Exec(executionSummary, executionDescription, testPlanKey.trim());
                }
                System.out.println("testExecutionKey : " + testExecutionKey);

                // Slack
                String message = Constants.SLACK_BRAND + " Mobility Run started at: <" + executionDescription
                        + "|" + executionSummary + ">" + "\n" + "Execution will be updated at "
                        + "<"+Constants.JIRA_URL + testExecutionKey + "|" + testExecutionKey
                        + ">" + "\n" + "Check " + realTimeLink + " for failure details";
                slackReporter.send_Message_To_Channel(message, slackChannel);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void onFinish(ITestContext context) {

        Iterator<ITestResult> skippedTestCases = context.getSkippedTests().getAllResults().iterator();
        while (skippedTestCases.hasNext()) {
            ITestResult skippedTestCase = skippedTestCases.next();
            ITestNGMethod method = skippedTestCase.getMethod();
            if (context.getSkippedTests().getResults(method).size() > 0) {
                log.info("Skipped Test :" + skippedTestCase.getTestClass().toString());
//                skippedTestCases.remove();
            }
        }
    }

    @Override
    public void onFinish(ISuite suite) {

        try {
            System.getenv("BUILD_NUMBER");
            String buildUrl = System.getenv("BUILD_URL");
            String slackChannel = System.getenv("SLACK_CHANNEL");

            if (testExecutionKey != null) {
                // Slack
                String message = Constants.SLACK_BRAND + " Mobility Run has finished. Complete  " + "<" + buildUrl
                        + "Automation_20HTML_20Report/|Jenkins Automation Report>"
                        + " and <"+Constants.JIRA_URL + testExecutionKey
                        + "|JIRA Test Execution> are now available";
                slackReporter.send_Message_To_Channel(message, slackChannel);
            }
        } catch (Exception e) {
            // ignore
        }

        String folderPath = Constants.REPORT_DIR; // Replace with the actual folder path
//        List<String> jsonFiles = new ArrayList<>();
        try {
//            Files.walk(Paths.get(folderPath))
//                    .filter(Files::isRegularFile)
//                    .filter(path -> path.toString().endsWith(".html"))
//                    .forEach(path -> jsonFiles.add(path.toString()));
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".html"))
                    .forEach(System.out::println);
//            System.out.println(Arrays.deepToString(jsonFiles.toArray()));
//            ExtentManager.createHTMLReportFromJsonReports(jsonFiles, Constants.EXTENT_HTML_REPORT);
        } catch (Exception e) {
            //ignore
        }


    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
//        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
//
//        String udid = driver.getCapabilities().getCapability("udid").toString();
//
//        log.info("udid : " + udid);

    }


}
