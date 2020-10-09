package com.DeviceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.base.Constants;

public class DeviceinfoProvider{
	
	
	
	public String udid;
	public String brand;
	public String os;
	public String deviceName;
	public String platformName;
	public String platformVersion;
	public String deviceModel;
	public String deviceColour;
	
	public DeviceinfoProvider(String udid) {
		super();
		this.udid = udid;
	}
	
	public synchronized String getBrand() {
		if(runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
		        + " shell getprop net.bt.name").contains("Android"))
			this.brand = runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb -s " +
					this.udid +
		             " shell getprop ro.product.brand").replaceAll("\n", "");
		else
			this.brand = "Apple";
		return brand;
	}
	
	public synchronized void uninstall_WDA() {
		if(runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
        + " shell getprop net.bt.name").contains("Android")) {
			//ignore
			runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb -s "+this.udid+" uninstall io.appium.uiautomator2.server");
			runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb -s "+this.udid+" uninstall io.appium.uiautomator2.server.test");
			runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb -s "+this.udid+" uninstall io.appium.settings");
		}else {
			runCommandThruProcess("/usr/local/bin/ideviceinstaller -u "+this.udid+" -U com.facebook.WebDriverAgentRunner.xctrunner");
		}
	}
	
	public synchronized String getOs() {
		if(runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
                + " shell getprop net.bt.name").contains("Android"))
			this.os = "Android";
		else
			this.os = "iPhone OS";
			
		return os;
	}
	
	public synchronized String getDeviceName() {
		if(runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
                + " shell getprop net.bt.name").contains("Android")) {
			
			String device = runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
                    + " shell getprop ro.product.brand").replaceAll("\n", "") +" "+
                    runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
   	                     + " shell getprop ro.product.model")
   	              .replaceAll("\n", "") + " "+this.udid;
			this.deviceName = device;
		}else {
			String deviceModel = getDeviceModel();
			if("iPhone6,1".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 5s";
			else if("iPhone6,2".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 5s";
			else if("iPhone7,1".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 6 Plus";
			else if("iPhone7,2".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 6";
			else if("iPhone8,1".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 6s";
			else if("iPhone8,2".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 6s Plus";
			else if("iPhone8,4".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone SE";
			else if("iPhone9,1".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 7";
			else if("iPhone9,2".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 7 Plus";
			else if("iPhone9,3".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 7";
			else if("iPhone9,4".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 7 Plus";
			else if("iPhone10,1".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 8";
			else if("iPhone10,2".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 8 Plus";
			else if("iPhone10,3".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone X";
			else if("iPhone10,4".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 8";
			else if("iPhone10,5".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 8 Plus";
			else if("iPhone10,6".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone X";
			else if("iPhone11,2".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone XS";
			else if("iPhone11,4".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone XS Max";
			else if("iPhone11,6".equalsIgnoreCase(deviceModel))
				this. deviceName = "iPhone XS Max";
			else if("iPhone11,8".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone XR";
			else if("iPhone12,1".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 11";
			else if("iPhone12,3".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 11 Pro";
			else if("iPhone12,8".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone SE 2";
			else
				this.deviceName = deviceModel;
						 
		}
				 
		return deviceName;
	}
	
	public synchronized String getPlatformName() {
		if(runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
                    + " shell getprop net.bt.name").contains("Android"))
			this.platformName = "Android";
		else
			this.platformName = "ios";
		
		return platformName;
	}
	public synchronized String getPlatformVersion() {
		if(runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
                + " shell getprop net.bt.name").contains("Android")) 
			this.platformVersion = runCommandThruProcess(
		             Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid  + " shell getprop ro.build.version.release")
		             .replaceAll("\\s+", "");
		else
			this.platformVersion = runCommandThruProcess(
		    		 "/usr/local/bin/ideviceinfo -u" + this.udid + "  -k ProductVersion")
					.replaceAll("\\s+", "");
		
			
		
		return platformVersion;
	}
	public synchronized String getDeviceModel() {
		if(runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
                + " shell getprop net.bt.name").contains("Android")) 
			this.deviceModel = runCommandThruProcess(Constants.ANDROID_HOME+"/platform-tools/adb -s " + this.udid
                    + " shell getprop ro.product.model")
             .replaceAll("\\s+", "");
		else
			this.deviceModel = runCommandThruProcess(
		    		 "/usr/local/bin/ideviceinfo -u" + this.udid + "  -k ProductType")
		              .replaceAll("\\s+", "");
		
		return deviceModel;
	}

	private String runCommandThruProcess(String command) {
	    BufferedReader br;
	    String allLine = "";
		try {
			br = getBufferedReader(command);
		
			String line;
	     
		    while ((line = br.readLine()) != null) {
		        allLine = allLine + "" + line + "\n";
		    }
		} catch (IOException e) {
			// ignore
		}
	    return allLine;
	 }
	
	 private BufferedReader getBufferedReader(String command) throws IOException {
	     
	     final Process process =  execForProcessToExecute(command);
	     
	     InputStream is = process.getInputStream();
	     InputStreamReader isr = new InputStreamReader(is);
	     return new BufferedReader(isr);
	 }
	
	 private Process execForProcessToExecute(String cmd) throws IOException {
		 
		 Process process = Runtime.getRuntime()
	    	      .exec(cmd);
	     return process;
	 }
	 

	

}
