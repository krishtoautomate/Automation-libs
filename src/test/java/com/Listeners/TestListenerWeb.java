package com.Listeners;

import com.ReportManager.ExtentTestManager;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.DriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.*;

import java.io.File;
import java.io.IOException;

public class TestListenerWeb extends TestListenerAdapter
        implements ISuiteListener, ITestListener, IInvokedMethodListener {

    private static Logger log = Logger.getLogger(TestListener.class.getName());

    @Override
    public void onTestStart(ITestResult testResult) {

    }

    @Override
    public synchronized void onTestSuccess(ITestResult testResult) {

        String testName = testResult.getMethod().getMethodName();

        ExtentTest test = ExtentTestManager.getTest();

        test.log(Status.INFO, testName + " - Completed as Success");

    }

    @Override
    public synchronized void onTestFailure(ITestResult testResult) {

        String testName = testResult.getMethod().getMethodName();
        String className = testResult.getTestClass().getName();

        WebDriver driver = DriverManager.getDriverInstance("Web");
        ExtentTest test = ExtentTestManager.getTest();

        if (driver != null) {
            log.error("Test failed : " + className);

            try {
                // Unique name to screen-shot
                String imgPath = "img/" + className + "_" + Constants.TIME_NOW + "_" + ".PNG";

                File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                FileUtils.moveFile(screenShot, new File(Constants.REPORT_DIR + imgPath));

                test.fail("Failed Test case : " + testName + "\n" + testResult.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());

            } catch (WebDriverException | IOException e) {
                test.fail("Failed Test case : " + testName + "\n" + e.getMessage());
            }

            try {
                String errorXML = driver.getPageSource();
                test.info(MarkupHelper.createCodeBlock(errorXML));
            } catch (Exception e) {
                // ignore
            }


        }

    }

    @Override
    public synchronized void onTestSkipped(ITestResult testResult) {

        String className = testResult.getTestClass().getName();

        log.warn("Test Skipped : " + className);
        try {
            ExtentTest test = ExtentTestManager.getTest();
            ExtentReports extent = ExtentTestManager.getTest().getExtent();


            extent.removeTest(test);
        } catch (Exception e) {
            // ignore
        }

    }

    @Override
    public void onStart(ISuite suite) {

        // Date date = new Date();
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        // sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        // String dateANDtime = sdf.format(date.getTime());

    }

    @Override
    public void onFinish(ISuite suite) {

    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // TODO Auto-generated method stub

    }


}
