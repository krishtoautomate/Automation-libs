package com.DeviceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConnectedDevices {
	
	private static Logger log = Logger.getLogger(Class.class.getName());
	
	public static void main(String[] args) throws Exception {
		
		ConnectedDevices devices = new ConnectedDevices();
		DeviceinfoProviderOld deviceInfo = new DeviceinfoProviderOld();
		
		deviceInfo.setDevices(devices.getAllIOSDevicesInfo());
		deviceInfo.setDevices(devices.getAllAndroidDevicesInfo());
		
		
	}
	
	private void startADB() {
	    runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb start-server");
	}
	
	/*
	 * List all ios devices
	 */
	public ArrayList<String> getIdevices() {
		 
		ArrayList<String> deviceList = new ArrayList<String>();
		
		String output = runCommandThruProcess("/usr/local/bin/idevice_id -l");
		 
		String[] lines = output.split("\n");
	        
	    for (String device : lines) {
	    	deviceList.add(device); 
	    }
	    
	    return deviceList;
	}
	
	/*
	 * List all Android devices
	 */
	public ArrayList<String> getADBdevices() {
		 
		ArrayList<String> deviceList = new ArrayList<String>();
		 	
		startADB(); // start adb service
		
		String output = runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb devices");
		
		String[] lines = output.split("\n");
		
	    for (int i = 1; i < lines.length; i++) {
	    	lines[i] = lines[i].split("\\s+")[0];

            deviceList.add(lines[i]);
	    }
	    
		return deviceList;
	}
	
	public JSONArray getAllIOSDevicesInfo() {
		 JSONObject deviceInfo = new JSONObject();
		 JSONArray devices = new JSONArray();
		 
		 ArrayList<String> deviceList = getIdevices();
		 
		 for (String device : deviceList) {
		        deviceInfo = getAndroidDeviceInfo(device);
		        devices.put(deviceInfo);
		 }
         return devices;
	}
	
	public JSONArray getAllAndroidDevicesInfo() {
		 
		JSONObject deviceInfo = new JSONObject();
		JSONArray devices = new JSONArray();
		
		ArrayList<String> deviceList = getADBdevices();
				
	    for (String device : deviceList) {
	        deviceInfo = getAndroidDeviceInfo(device);
	        devices.put(deviceInfo);
	    }
		
		return devices;
	}
	 
	public JSONObject getDeviceIosInfo(String deviceID) {
		 
		DeviceinfoProvider deviceinfoProvider = new DeviceinfoProvider(deviceID);
		 
	    String brand = deviceinfoProvider.getBrand();
	    String os = deviceinfoProvider.getOs();
	    String osVersion = deviceinfoProvider.getPlatformVersion();
	    String deviceModel = deviceinfoProvider.getDeviceModel();
	    String deviceName = deviceinfoProvider.getDeviceName();
	     
	    JSONObject adbDevices = new JSONObject();
	    adbDevices.put("udid",deviceID);
	    adbDevices.put("name",deviceName);
	    adbDevices.put("os",os);
	    adbDevices.put("osVersion",osVersion);
	    adbDevices.put("brand",brand);
	    adbDevices.put("deviceModel",deviceModel);
	    
	    return adbDevices;
	}
	 
	
	
	private JSONObject getAndroidDeviceInfo(String deviceID)  {
		DeviceinfoProvider deviceinfoProvider = new DeviceinfoProvider(deviceID);
		
		String deviceModel = deviceinfoProvider.getDeviceModel();
		String brand = deviceinfoProvider.getBrand();
	    String osVersion = deviceinfoProvider.getPlatformVersion();
	    String deviceName = brand + " " + deviceModel;
	    String os = deviceinfoProvider.getOs();
	    
	    JSONObject adbDevices = new JSONObject();
	    adbDevices.put("udid",deviceID);
	    adbDevices.put("name",deviceName);
	    adbDevices.put("os",os);
	    adbDevices.put("osVersion",osVersion);
	    adbDevices.put("brand",brand);
	    adbDevices.put("deviceModel",deviceModel);
	    
	    return adbDevices;
	 }
	
	private String runCommandThruProcess(String command) {
	     BufferedReader br = getBufferedReader(command);
	     String line = "";
	     String allLine = "";
	     try {
			while ((line = br.readLine()) != null) {
				allLine = allLine + "" + line + "\n";
			}
		} catch (IOException e) {
			log.info("command failed!");
		}
	    return allLine;
	 }
	
	 private BufferedReader getBufferedReader(String command) {
	     
		 Process process = null;
		 try {
			 process = Runtime.getRuntime()
				      .exec(command);
		 } catch (IOException e) {
			log.info("Runtime command failed!");
		 }
	     
	     InputStream is = process.getInputStream();
	     InputStreamReader isr = new InputStreamReader(is);
	     
	     return new BufferedReader(isr);
	 }

}

