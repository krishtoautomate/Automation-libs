package com.Listeners;
import java.util.Iterator;

import org.apache.log4j.Logger;
/**
* Created by Krish on 21.07.2018.
*/
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.DeviceManager.ConnectedDevices;
import com.DeviceManager.DeviceinfoProviderOld;

public class InvokedSuiteListener extends TestListenerAdapter implements ISuiteListener {

	private static Logger log = Logger.getLogger(Class.class.getName());
	
	@Override
	public void onFinish(ISuite suite) {
		
	}
	@Override
	public void onStart(ISuite suite) {
		
		ConnectedDevices devices = new ConnectedDevices();
		DeviceinfoProviderOld deviceInfo = new DeviceinfoProviderOld();
		
		try {
			deviceInfo.setDevices(devices.getIOSDevices());
			deviceInfo.setDevices(devices.getAndroidDevices());
		} catch (Exception e) {
			// ignore
		}
		
		log.info(deviceInfo.getDevices());
		
	}

	  
	@Override
	public void onFinish(ITestContext context) {
	    Iterator<ITestResult> skippedTestCases = context.getSkippedTests().getAllResults().iterator();
	    while (skippedTestCases.hasNext()) {
	        ITestResult skippedTestCase = skippedTestCases.next();
	        ITestNGMethod method = skippedTestCase.getMethod();
	        if (context.getSkippedTests().getResults(method).size() > 0) {
	        	log.info("Removing:" + skippedTestCase.getTestClass().toString());
	            skippedTestCases.remove();
	        }
	    }
	}

}
