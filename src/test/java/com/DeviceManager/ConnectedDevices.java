package com.DeviceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConnectedDevices {
	
	private static Logger log = Logger.getLogger(Class.class.getName());
	
	public static void main(String[] args) throws Exception {
		
		ConnectedDevices devices = new ConnectedDevices();
		DeviceinfoProviderOld deviceInfo = new DeviceinfoProviderOld();
		
		deviceInfo.setDevices(devices.getIOSDevices());
		deviceInfo.setDevices(devices.getAndroidDevices());
		//log.info(deviceInfo.getDevices());
		
//		ObjectMapper mapper = new ObjectMapper();
//		Object jsonObject = mapper.readValue(deviceInfo.getDevices().toString(), Object.class);
//		String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
		log.info(deviceInfo.getDevices());
	}
	
	public JSONArray getIOSDevices() throws Exception {
		 JSONObject deviceObj = new JSONObject();
		 JSONArray devicesArray = new JSONArray();
		 String output = runCommandThruProcess("/usr/local/bin/idevice_id -l");
		 String[] lines = output.split("\n");
	        
         for (int i = 0; i < lines.length; i++) {
             lines[i] = lines[i].replaceAll("device", "");
             String deviceID = lines[i];
             deviceObj = getDeviceIosInfo(deviceID);
//             devices.add(deviceInfo.toString());
             devicesArray.put(deviceObj);
         }
         return devicesArray;
	 }
	 
	 public JSONArray getAndroidDevices() throws Exception {
		 
		JSONObject deviceInfo = new JSONObject();
		JSONArray devices = new JSONArray();
		 	
		startADB(); // start adb service
		String output = runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb devices");
		String[] lines = output.split("\n");
		
		if (lines.length <= 1) {
		    //stopADB();
		} else {
		    for (int i = 1; i < lines.length; i++) {
		        lines[i] = lines[i].replaceAll("\\s+", "");
		
		        if (lines[i].contains("device")) {
		            lines[i] = lines[i].replaceAll("device", "");
		            String deviceID = lines[i];
		            deviceInfo = getAndroidDeviceInfo(deviceID);
		            devices.put(deviceInfo);
		        }
		    }
		}
		return devices;
	}
	 
	 public JSONObject getDeviceIosInfo(String deviceID) throws InterruptedException, IOException {
		 
		 DeviceinfoProvider deviceinfoProvider = new DeviceinfoProvider(deviceID);
		 
	     String brand = "Apple";
	     String os = deviceinfoProvider.getOs();
	     String osVersion = deviceinfoProvider.getPlatformVersion();
	     String deviceModel = deviceinfoProvider.getDeviceModel();
	     
	     String deviceName = deviceinfoProvider.getDeviceName();
	     
	     boolean isDevice = true;
	    
	     JSONObject adbDevices = new JSONObject();
	     adbDevices.put("name",deviceName);
	     adbDevices.put("osVersion",osVersion);
	     adbDevices.put("brand",brand);
	     adbDevices.put("udid",deviceID);
	     adbDevices.put("isDevice",isDevice);
	     adbDevices.put("deviceModel",deviceModel);
	     adbDevices.put("os",os);
	     
	     return adbDevices;
	 }
	 
	 
	
	
	private void startADB() throws Exception {
	    String output = runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb start-server");
	    String[] lines = output.split("\n");
	    if (lines[0].contains("internal or external command")) {
	        System.out.println("Please set ANDROID_HOME in your system variables");
	    }
	}
	
	private JSONObject getAndroidDeviceInfo(String deviceID) throws InterruptedException, IOException {
		
		DeviceinfoProvider deviceinfoProvider = new DeviceinfoProvider(deviceID);
		
		 String deviceModel = deviceinfoProvider.getDeviceModel();
	     String brand = deviceinfoProvider.getBrand();
	     String osVersion = deviceinfoProvider.getPlatformVersion();
	     String deviceName = brand + " " + deviceModel;
	     String apiLevel =
	             runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb -s " + deviceID
	                     + " shell getprop ro.build.version.sdk")
	                     .replaceAll("\n", "");
	     String deviceManufacturer = runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb -s " +
	             deviceID +
	             " shell getprop ro.product.manufacturer")
	    		 .replaceAll("\n", "");
	
	     boolean isDevice = true;
	    
	     JSONObject adbDevices = new JSONObject();
	     adbDevices.put("name",deviceName);
	     adbDevices.put("osVersion",osVersion);
	     adbDevices.put("apiLevel",apiLevel);
	     adbDevices.put("brand",brand);
	     adbDevices.put("udid",deviceID);
	     adbDevices.put("isDevice",isDevice);
	     adbDevices.put("deviceModel",deviceModel);
	     adbDevices.put("deviceManufacturer",deviceManufacturer);
	     adbDevices.put("os","android");
	     
	     return adbDevices;
	 }
	
	private String runCommandThruProcess(String command)
         throws InterruptedException, IOException {
	     BufferedReader br = getBufferedReader(command);
	     String line;
	     String allLine = "";
	     while ((line = br.readLine()) != null) {
	         allLine = allLine + "" + line + "\n";
	     }
	     return allLine;
	 }
	
	 private BufferedReader getBufferedReader(String command) throws IOException {
	     
	     final Process process =  Runtime.getRuntime()
	    	      .exec(command);
	     
	     InputStream is = process.getInputStream();
	     InputStreamReader isr = new InputStreamReader(is);
	     return new BufferedReader(isr);
	 }

}

