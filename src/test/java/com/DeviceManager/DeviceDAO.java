package com.DeviceManager;

import java.util.ArrayList;

import com.Utilities.ITestBase;
import com.base.Constants;

public class DeviceDAO implements ITestBase{
	
	ArrayList<String> adbDeviceList = new ArrayList<String>();
	ArrayList<String> iDeviceList = new ArrayList<String>();
	
	public String udid;
	public String brand;
	public String os;
	public String deviceName;
	public String platformName;
	public String osVersion;
	public String deviceModel;
	public String deviceColour;
	
	public DeviceDAO(String udid) {
		super();
		this.udid = udid;
	}
	
	public DeviceDAO() {
		super();
	}
	
	public synchronized void setUdid(String udid) {
		this.udid = udid;
	}
	
	/*
	 * get all Android device list
	 */
	public ArrayList<String> getADBdevices() {
		 
		runCommandThruProcess(Constants.ADB+" start-server"); //start adb service
		
		String output = runCommandThruProcess(Constants.ADB+" devices");
		
		String[] lines = output.split("\n");
		
	    for (int i = 1; i < lines.length; i++) {
	    	lines[i] = lines[i].split("\\s+")[0];

            this.adbDeviceList.add(lines[i]);
	    }
	    
		return this.adbDeviceList;
	}
	
	/*
	 * List all ios devices
	 */
	public ArrayList<String> getIdevices() {
		 
		String output = runCommandThruProcess(Constants.IDEVICE_ID+" -l");
		
		if(output.length()>0) {
			String[] lines = output.split("\n");
		     
		    for (String device : lines) {
		    	device.replace("\n", "");
		    	this.iDeviceList.add(device); 
		    }
		}
	    return this.iDeviceList;
	}
	
	public synchronized String getBrand() {
		if(this.getOs().equalsIgnoreCase("iPhone OS"))
			this.brand = "Apple";
		else
			this.brand = runCommandThruProcess(Constants.ADB+" -s " +
					this.udid +
		            " shell getprop ro.product.brand").replaceAll("\n", "");
		return brand;
	}
	
	//net.bt.name
	public synchronized String getOs() {
		if(runCommandThruProcess(Constants.ADB+" -s " +
				this.udid +
	            " shell getprop net.bt.name").replaceAll("\n", "").contains("Android"))
			this.os = "Android";
		else
			this.os = "iPhone OS";
			
		return os;
	}
	
	public synchronized String getDeviceModel() {
		if(this.getOs().equalsIgnoreCase("iPhone OS"))
			this.deviceModel = runCommandThruProcess(
					Constants.IDEVICEINFO+" -u" + this.udid + " -k ProductType");
		else
			this.deviceModel = runCommandThruProcess(Constants.ADB+" -s " + this.udid
                    + " shell getprop ro.product.model");
		
		return deviceModel.replaceAll("\\s+", "");
	}
	
	public synchronized String getDeviceName() {
		if(this.getOs().equalsIgnoreCase("iPhone OS")) {
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
			else if("iPhone13,1".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 12 Mini";
			else if("iPhone13,2".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 12";
			else if("iPhone13,3".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 12 Pro";
			else if("iPhone13,4".equalsIgnoreCase(deviceModel))
				this.deviceName = "iPhone 12 Pro Max";
			else
				this.deviceName = deviceModel;
		}else {
			String device = getBrand() +" "+
                    runCommandThruProcess(Constants.ADB+" -s " + this.udid
   	                     + " shell getprop ro.product.model")
   	              .replaceAll("\n", "");
			this.deviceName = device;
		}
				 
		return deviceName;
	}
	
	public synchronized String getPlatformName() {
		if(this.getOs().equalsIgnoreCase("iPhone OS"))
			this.platformName = "iOS";
		else
			this.platformName = "Android";
		
		return platformName;
	}
	
	public synchronized String getosVersion() {
		if(this.getOs().equalsIgnoreCase("iPhone OS"))
			this.osVersion = runCommandThruProcess(
		    		 Constants.IDEVICEINFO+" -u" + this.udid + " -k ProductVersion");
			
		else
			this.osVersion = runCommandThruProcess(
		             Constants.ADB+" -s " + this.udid  + " shell getprop ro.build.version.release");
		
		return osVersion.replaceAll("\\s+", "");
	}
	
	public synchronized void uninstall_WDA() {
		if(this.getOs().equalsIgnoreCase("iPhone OS"))
			runCommandThruProcess("/usr/local/bin/ideviceinstaller -u "+this.udid+" -U com.facebook.WebDriverAgentRunner.xctrunner");
		else {
			runCommandThruProcess(Constants.ADB+" -s "+this.udid+" uninstall io.appium.uiautomator2.server");
			runCommandThruProcess(Constants.ADB+" -s "+this.udid+" uninstall io.appium.uiautomator2.server.test");
			runCommandThruProcess(Constants.ADB+" -s "+this.udid+" uninstall io.appium.settings");
		}
	}
	
	

}
