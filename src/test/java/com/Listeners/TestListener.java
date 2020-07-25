package com.Listeners;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.DeviceManager.DeviceInfo;
import com.ReportManager.ReportBuilder;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.base.Constants;
import com.base.ScreenShotManager;
import com.base.TestBase;

public class TestListener extends TestListenerAdapter implements ISuiteListener {
	
	protected ReportBuilder reporter = new ReportBuilder();
	
	@Override
	public synchronized void onTestSuccess(ITestResult tr) {
		// TODO Auto-generated method stub
		
		/*
		 * get device details
		 */
		Map<String, String> testParams = tr.getTestContext().getCurrentXmlTest().getAllParameters();
		DeviceInfo deviceManager = new DeviceInfo();
		String udid = testParams.get("udid");
		String deviceName = deviceManager.getDeviceName(udid);
		String platForm = deviceManager.getPlatformName(udid);
		
		Object testClass = tr.getInstance();
		ExtentTest test = ((TestBase) testClass).getExtentTest();
		
		test.log(Status.INFO, tr.getMethod().getMethodName() + " - Completed as Success");
		
	    //Categories
        test.assignCategory(platForm);
        test.assignCategory(deviceName);
        test.assignCategory("Passed");
        
	}

	@Override
	public synchronized void onTestFailure(ITestResult tr) {
		// TODO Auto-generated method stub
		DeviceInfo deviceManager = new DeviceInfo();
		
		/*
		 * get device details
		 */
		Map<String, String> testParams = tr.getTestContext().getCurrentXmlTest().getAllParameters();
		String udid = testParams.get("udid");
		String deviceName = deviceManager.getDeviceName(udid);
		String platForm = deviceManager.getPlatformName(udid);
		
		Object testClass = tr.getInstance();
		WebDriver driver = ((TestBase) testClass).getDriver();
		Logger log = ((TestBase) testClass).getLog();
		ExtentTest test = ((TestBase) testClass).getExtentTest();
		
		if(driver != null) {
			log.error("Test failed : "+ tr.getMethod().getMethodName() + " : "+ udid+"_"+deviceName);
			try {
				ScreenShotManager screenShotManager = new ScreenShotManager(driver);
				String ScreenShot = screenShotManager.getScreenshot();
				
				test.fail("Failed Test case : " + tr.getMethod().getMethodName() + " -- " + tr.getThrowable(), 
						MediaEntityBuilder.createScreenCaptureFromPath(ScreenShot).build());
				
			} catch (WebDriverException | IOException e) {			
				test.fail("Failed Test case : " + tr.getMethod().getMethodName() + "\n" + tr.getThrowable());
				
			}
			
		    //Categories
	        test.assignCategory(platForm);
	        test.assignCategory(deviceName);
			test.assignCategory("Failed");
				
			//Emailable Test Summary
			reporter.report(tr.getMethod().getMethodName(), deviceName, "FAIL", tr.getThrowable().toString());
			
		}
	}

	@Override
	public synchronized void onTestSkipped(ITestResult tr) {
		// TODO Auto-generated method stub
		
		/*
		 * get device details
		 */
		DeviceInfo deviceManager = new DeviceInfo();
		Map<String, String> testParams = tr.getTestContext().getCurrentXmlTest().getAllParameters();
		String udid = testParams.get("udid");
		String deviceName = deviceManager.getDeviceName(udid);
		
		Object testClass = tr.getInstance();
		Logger log = ((TestBase) testClass).getLog();
		log.warn("Test Skipped : "+ tr.getMethod().getMethodName() + " : "+ udid+"_"+deviceName);
		
		
		ExtentTest test = ((TestBase) testClass).getExtentTest();
		ExtentReports extent = ((TestBase) testClass).getExtentReports();
		try {
			extent.removeTest(test);
		} catch (Exception e) {
			// ignore
		}
	}

	@Override
	public void onStart(ISuite suite) {
		// TODO Auto-generated method stub
		reporter.initialize();//Emailable Report
		
		
	}

	@Override
	public void onFinish(ISuite suite) {
		String emailReport = Constants.EMAIL_REPORT;
		File file = new File(emailReport);
        if(file.delete()) {
        	//delete if exists}
        }
		reporter.writeResults(emailReport);
		
	}

	
	
}
