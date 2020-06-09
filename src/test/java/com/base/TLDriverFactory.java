package com.base;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
* Created by Krish on 21.07.2018.
**/
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.DeviceManager.DeviceInfo;
import com.DeviceManager.DeviceinfoProvider;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
 


 
public class TLDriverFactory{
 
 
    private ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    
    private DeviceInfo deviceManager = new DeviceInfo();
    private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();
	private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    
    public synchronized void setDriver(AppiumDriverLocalService server, String udid) throws FileNotFoundException, IOException {
    	
    	DeviceinfoProvider deviceinfoProvider = new DeviceinfoProvider(udid);
    	
    	int devicePort = 8100;
    	devicePort = deviceManager.getDevicePort(udid);
    	String deviceName = deviceinfoProvider.getDeviceName();
    	String platForm = deviceinfoProvider.getPlatformName();
	
    	if("Android".equalsIgnoreCase(platForm)) {
    		//adb shell dumpsys window windows | grep -E 'mCurrentFocus'
        	//adb -s BPN0218515003355 shell getprop
        	
        	//load capabilites from json File
        	desiredCapabilities = capabilitiesManager.loadJSONCapabilities(Constants.ANDROID_CAPABILITIES , "ANDROID");
        	
			desiredCapabilities.setCapability("deviceName",deviceName);
			desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
			desiredCapabilities.setCapability("systemPort", devicePort);//sysPort	
			
			tlDriver.set(new AndroidDriver<MobileElement>(new URL(server.getUrl().toString()), desiredCapabilities));
//    		tlDriver.set(new AndroidDriver<MobileElement>(new URL("http://192.168.0.54:4444/wd/hub"), desiredCapabilities));
    		
        }else if("iOS".equalsIgnoreCase(platForm)) {
        	
        	//load capabilites from json File
        	desiredCapabilities = capabilitiesManager.loadJSONCapabilities(Constants.IOS_CAPABILITIES , "IOS");
        	
        	desiredCapabilities.setCapability("deviceName", deviceName);
        	desiredCapabilities.setCapability("udid", udid);
        	desiredCapabilities.setCapability("wdaLocalPort", devicePort);
        	
			tlDriver.set(new AppiumDriver<MobileElement>(new URL(server.getUrl().toString()), desiredCapabilities)); 
//        	tlDriver.set(new AppiumDriver<MobileElement>(new URL("http://192.168.0.54:4444/wd/hub"), desiredCapabilities));
        }
    }
 
    public synchronized WebDriver getDriver() {
        return tlDriver.get();
    }
}
