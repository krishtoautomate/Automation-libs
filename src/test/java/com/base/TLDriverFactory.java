package com.base;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;

import com.DeviceManager.DeviceDAO;
import com.DeviceManager.DeviceInfo;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
  
public class TLDriverFactory{
 
	private static Logger log = Logger.getLogger(Class.class.getName());
	
    @SuppressWarnings("rawtypes")
	private ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();
    
    private DeviceInfo deviceManager = new DeviceInfo();
    private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();
	private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
	
	int retry = 5;
	int interval = 1000;
    
    public synchronized void setDriver(AppiumDriverLocalService server, ITestContext Testctx) {
    	
    	Map<String, String> testParams = Testctx.getCurrentXmlTest().getAllParameters();
    	
    	String udid = testParams.get("udid");
    	String platForm = testParams.get("platForm");
    	
    	DeviceDAO deviceinfoProvider = new DeviceDAO(udid);
    	
    	int devicePort = 8100;
    	devicePort = deviceManager.getDevicePort(udid);
    	
    	String deviceName = deviceinfoProvider.getDeviceName();
//    	String platForm = deviceinfoProvider.getPlatformName();
    	
    	if("Android".equalsIgnoreCase(platForm)) {
    		
    		while(retry > 0 ) {
        		try {
		        	
		        	desiredCapabilities = capabilitiesManager.loadJSONCapabilities(Constants.ANDROID_CAPABILITIES , "ANDROID");
		        	
					desiredCapabilities.setCapability("deviceName",deviceName);
					desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
					desiredCapabilities.setCapability("systemPort", devicePort);//sysPort	
					
					tlDriver.set(new AndroidDriver<MobileElement>(new URL(server.getUrl().toString()), desiredCapabilities));
					break;
        		} catch (Exception e) {
        		   //Decrement Retry interval
    		       retry--;
    		       log.info("\nAttempted: " + (60-retry) + ". Failure to find device("+udid+"), Retrying.....\n"+e);
    		       try {
    		    	   Thread.sleep(interval);
				   } catch (InterruptedException e1) {
						// ignore
				   }
        		}
    		}
        }else if("iOS".equalsIgnoreCase(platForm)) {
        	
//        	if(!"Auto".equalsIgnoreCase(udid))
        	deviceinfoProvider.uninstall_WDA();
        	
        	desiredCapabilities = capabilitiesManager.loadJSONCapabilities(Constants.IOS_CAPABILITIES , "IOS");
        	
        	desiredCapabilities.setCapability("deviceName", deviceName);
        	desiredCapabilities.setCapability("udid", udid);
        	desiredCapabilities.setCapability("wdaLocalPort", devicePort);
        	
			try {
				tlDriver.set(new IOSDriver<MobileElement>(new URL(server.getUrl().toString()), desiredCapabilities));
			} catch (MalformedURLException e) {
				log.error("AppiumDriver failed to start!");
			}
        }
    }
 
    public synchronized WebDriver getDriver() {
        return tlDriver.get();
    }
}
