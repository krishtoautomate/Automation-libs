package com.DeviceManager;

import org.json.JSONArray;

public class DeviceinfoProviderOld {
	
	JSONArray devices = new JSONArray();;
	String deviceName;
	String platForm;
	String osVersion;
	String udid;
	String name;
	String deviceModel;
	String brand;
	
	public synchronized final JSONArray getDevices() {
		return devices;
	}
	public synchronized void setDevices(JSONArray deviceArray) {
        for (int i = 0; i < deviceArray.length(); i++)
        	this.devices.put(deviceArray.get(i));
	    
	}
	public synchronized String getDeviceName(int i) {
		return this.getDevices().getJSONObject(i).get("name").toString();
	}
	
	public synchronized String getPlatForm(int i) {
		return this.getDevices().getJSONObject(i).get("os").toString();
	}
	
	public synchronized String getOsVersion(int i) {
		return this.getDevices().getJSONObject(i).get("osVersion").toString();
	}

	public synchronized String getUdid(int i) {
		return this.getDevices().getJSONObject(i).get("udid").toString();
	}

	public synchronized String getName(int i) {
		return this.getDevices().getJSONObject(i).get("name").toString();
	}

	public synchronized String getDeviceModel(int i) {
		return this.getDevices().getJSONObject(i).get("deviceModel").toString();
	}

	public synchronized String getBrand(int i) {
		return this.getDevices().getJSONObject(i).get("brand").toString();
	}
	
}

