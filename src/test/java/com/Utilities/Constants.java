package com.Utilities;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Constants {
	
	 public static final String APPIUM_IP_ADDRESS = "127.0.0.1";
	
	 public static final String HOST_OS = System.getProperty("os.name");
	 
	 public static final String USER_DIR = System.getProperty("user.dir");
	 public static final String TEST_RESOURCES = USER_DIR+"/src/test/resources/";
	
     public static final String OUTPUT_DIRECTORY = USER_DIR + "/test-output/";

     public static final String DATE_NOW=  new SimpleDateFormat("MMddyyyy").format(new Date());
     public static final String TIME_NOW = new SimpleDateFormat("HH.mm.ss.SSS").format(new Date());

     public static final String SCREENSHOTS_DIRECTORY = OUTPUT_DIRECTORY + "/" + DATE_NOW + "/img/";
     
     public static final String NO_SCREENSHOTS_AVAILABLE = USER_DIR + "/src/test/resources/No_screenshot_available.jpg";
    
     public static final String EXTENT_REPORT_CONFIG = USER_DIR+"/src/main/resources/extent-config.xml";
     public static final String REPORT_DIR = OUTPUT_DIRECTORY  +DATE_NOW + "/";
     public static final String EXTENT_HTML_REPORT = REPORT_DIR+ "AUTOMATION_REPORT"+ ".html";
	 public static final String EMAIL_REPORT_TEMPLATE =  USER_DIR + "/src/main/resources/ReportTemplate.html";
	 public static final String EMAIL_REPORT = REPORT_DIR+ "EmailableReport"+ ".html";
	
	 public static final String IOS_CAPABILITIES = USER_DIR+"/src/test/resources/capabilities.json";
	 public static final String ANDROID_CAPABILITIES = USER_DIR+"/src/test/resources/capabilities.json";
	 public static final String ANDROID_HOME = "/usr/local/share/android-sdk/";
	 public static final String ADB = ANDROID_HOME+ "platform-tools/adb";
	 
	 public static final String CAPABILITIES = USER_DIR+"/build/capabilities.json";
	 
	 public static final String NODE_PATH = "/usr/local/bin/node";
	 public static final String APPIUM_PATH = "/usr/local/bin/appium";
	 
	 public static final String API_HOST = "api.luckymobile.ca";
	 public static final String BASE_URI = "https://"+API_HOST+"/channelluckyext";
	 public static final String LANGUAGE = "fr-ca";
	 
	 public static final String VOUCHER_FILE= "/Users/Shared/Jenkins/NativeApp/vouchersLucky.txt";
	 
	 
	 public static final String HOST_IP_ADDRESS() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "Unknown Host";
		}
	 }
}