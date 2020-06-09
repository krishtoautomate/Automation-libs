package com.DeviceManager;



import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

import com.DataManager.JsonFileReader;

public class DeviceInfo {

	private String deviceName;
	private String platformName;
	private String platformVersion;
	private String deviceColour;
	private int devicePort; //wdaPort or systemPort
	
	private static Logger log = Logger.getLogger(Class.class.getName());
    
	private static final String deviceInfo = "src/test/resources/" +"deviceInfo.json";
	

	/**
	 * @return the deviceName
	 */
	public synchronized String getDeviceName(String deviceUdid) {
		
		try {
			JsonFileReader JsonFileReader = new JsonFileReader(deviceInfo);
			
			int index = JsonFileReader.getObjIndex("udid", deviceUdid);
				
			this.deviceName = JsonFileReader.getJsonValue(index, "name");
		} catch (IOException | ParseException e) {
			log.error("get deviceName failed! "+ "\n" + e.getLocalizedMessage() );
			
		}
		return deviceName;
	}

	/**
	 * @return the platformName
	 */
	public synchronized String getPlatformName(String deviceUdid) {
		
		try {
			JsonFileReader JsonFileReader = new JsonFileReader(deviceInfo);
			
			int index = JsonFileReader.getObjIndex("udid", deviceUdid);
				
			this.platformName = JsonFileReader.getJsonValue(index, "platformName");
		} catch (IOException | ParseException e) {
			log.error("get deviceName failed! "+ "\n" + e.getLocalizedMessage() );
			
		}
		return platformName;
	}

	/**
	 * @return the platformVersion
	 */
	public synchronized String getPlatformVersion(String deviceUdid) {
		
		try {
			JsonFileReader JsonFileReader = new JsonFileReader(deviceInfo);
			
			int index = JsonFileReader.getObjIndex("udid", deviceUdid);
				
			this.platformVersion = JsonFileReader.getJsonValue(index, "platformVersion");
		} catch (IOException | ParseException e) {
			log.error("get deviceName failed! "+ "\n" + e.getLocalizedMessage() );
			
		}
		return platformVersion;
	}

	/**
	 * @return the deviceColour
	 */
	public synchronized String getDeviceColour(String deviceUdid) {
		
		try {
			JsonFileReader JsonFileReader = new JsonFileReader(deviceInfo);
			
			int index = JsonFileReader.getObjIndex("udid", deviceUdid);
				
			this.deviceColour = JsonFileReader.getJsonValue(index, "colour");
		} catch (IOException | ParseException e) {
			log.error("get deviceName failed! "+ "\n" + e.getLocalizedMessage() );
			
		}
		return deviceColour;
	}

	/**
	 * @return the devicePort
	 */
	public synchronized int getDevicePort(String deviceUdid) {
		try {
			JsonFileReader JsonFileReader = new JsonFileReader(deviceInfo);
			
			int index = JsonFileReader.getObjIndex("udid", deviceUdid);
				
			this.devicePort = Integer.valueOf(JsonFileReader.getJsonValue(index, "devicePort"));
		} catch (IOException | ParseException e) {
			log.error("get deviceName failed! "+ "\n" + e.getLocalizedMessage() );
			
		}
		return devicePort;
	}
}
