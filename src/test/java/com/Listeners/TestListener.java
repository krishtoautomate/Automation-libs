package com.Listeners;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.DeviceManager.DeviceDAO;
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
		/*
		 * get device details
		 */
		Map<String, String> testParams = tr.getTestContext().getCurrentXmlTest().getAllParameters();
		String udid = testParams.get("udid");
		DeviceDAO deviceinfoProvider = new DeviceDAO(udid);
		String deviceName = deviceinfoProvider.getDeviceName();
		String platForm = deviceinfoProvider.getPlatformName();
		String buildNo = System.getenv("BUILD_NUMBER");
		String environment = System.getenv("ENVIRONMENT");
		
		Object testClass = tr.getInstance();
		ExtentTest test = ((TestBase) testClass).getExtentTest();
		
		test.log(Status.INFO, tr.getMethod().getMethodName() + " - Completed as Success");
		
	    //Categories
        test.assignCategory(platForm);
        test.assignCategory(deviceName);
        test.assignCategory("Passed");
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		String date_time = dtf.format(now);
        
      //Emailable Test Summary
//		reporter.report(date_time, "MBM", buildNo, environment, tr.getMethod().getMethodName(), deviceName, platForm, "PASS", tr.getThrowable().toString());
	}

	@Override
	public synchronized void onTestFailure(ITestResult tr) {
		/*
		 * get device details
		*/
		Map<String, String> testParams = tr.getTestContext().getCurrentXmlTest().getAllParameters();
		String udid = testParams.get("udid");
		DeviceDAO deviceinfoProvider = new DeviceDAO(udid);
		String deviceName = deviceinfoProvider.getDeviceName();
		String platForm = deviceinfoProvider.getPlatformName();
		String buildNo = System.getenv("BUILD_NUMBER");
		String environment = System.getenv("ENVIRONMENT");
		
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
				
			} catch (WebDriverException e) {			
				test.fail("Failed Test case : " + tr.getMethod().getMethodName() + "\n" + tr.getThrowable());
				
			}
			
		    //Categories
	        test.assignCategory(platForm);
	        test.assignCategory(deviceName);
			test.assignCategory("Failed");
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();
			String date_time = dtf.format(now);
				
			//Emailable Test Summary
//			reporter.report(date_time, "MBM", buildNo, environment, tr.getMethod().getMethodName(), deviceName, platForm, "FAIL", tr.getThrowable().toString());
			
		}
	}

	@Override
	public synchronized void onTestSkipped(ITestResult tr) {
		/*
		 * get device details
		 */
		Map<String, String> testParams = tr.getTestContext().getCurrentXmlTest().getAllParameters();
		String udid = testParams.get("udid");
		DeviceDAO deviceinfoProvider = new DeviceDAO(udid);
		String deviceName = deviceinfoProvider.getDeviceName();
		
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
		reporter.initialize();//Emailable Report
	}

	@Override
	public void onFinish(ISuite suite) {
		String emailReport = Constants.EMAIL_REPORT;
		File file = new File(emailReport);
        if(file.delete()) {
        	//delete if exists
        }
        String buildNo = System.getenv("BUILD_NUMBER");
        if(buildNo != null)
			reporter.writeResults(emailReport);
	}	
	
}
