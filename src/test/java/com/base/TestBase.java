package com.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.DeviceManager.DeviceInfo;
import com.DeviceManager.DeviceinfoProvider;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;

/**
* Created by Krish on 06.06.2018.
*/
public class TestBase {

   protected WebDriver driver;
   protected Map<Long, WebDriver> driverMap = new ConcurrentHashMap<Long, WebDriver>();
   protected WebDriverWait wait;
   protected TLDriverFactory tlDriverFactory = new TLDriverFactory();
   protected static Logger log;
   protected ExtentHtmlReporter htmlReporter;
   protected static ExtentReports extent;
   protected ExtentTest test;
   protected ScreenShotManager screenShotManager;
   protected DeviceInfo deviceManager = new DeviceInfo();
   
   protected AppiumDriverLocalService server;
   
   public synchronized WebDriver getDriver() {
       return driver;
   }
   
   public synchronized ExtentTest getExtentTest() {
       return test;
   }
   
   public synchronized Logger getLog() {
       return log;
   }
   
   public synchronized ExtentReports getExtentReports() {
	   return extent;
   }
   
   /**
	* Executed once before all the tests
	*/
   @BeforeSuite(alwaysRun=true)
   public void setupSuit(ITestContext ctx) throws IOException, ParseException{
	   
	   String suiteName = ctx.getCurrentXmlTest().getSuite().getName();
	   
	   //Log4j
	   log = Logger.getLogger(suiteName);
	   
	   //create Report Folder in 'test-output'
	   File reportDir = new File(Constants.REPORT_DIR);
	   if(!reportDir.exists()) {
		   reportDir.mkdirs();
		   log.info("created Folder for Report: "+reportDir.getAbsolutePath().toString());
	   }
	   
	   //extent report
	   htmlReporter = new ExtentHtmlReporter(Constants.EXTENT_HTML_REPORT);
	   extent = new ExtentReports();
	   extent.attachReporter(htmlReporter);
	   
	   
	   htmlReporter.config().setDocumentTitle("AUTOMATION REPORT");
	   htmlReporter.config().setReportName("AUTOMATION REPORT"); 
	   htmlReporter.config().setTheme(Theme.STANDARD);
	   
	   //HOST INFO
	   extent.setSystemInfo("OS", Constants.HOST_OS);
	   extent.setSystemInfo("HostIPAddress", InetAddress.getLocalHost().getHostAddress());
	   extent.setSystemInfo("Host Name", InetAddress.getLocalHost().getHostName());
	   	   
   }
   
   @BeforeTest
   @Parameters({"udid"})
   public synchronized void BeforeTest(@Optional String udid, ITestContext iTestContext)  {
	   DeviceinfoProvider deviceinfoProvider = new DeviceinfoProvider(udid);
	   if(udid != null) {
		   iTestContext.setAttribute("udid", udid);
		   
		   AppiumManager appiumManager = new AppiumManager();
		   
		   int devicePort = deviceManager.getDevicePort(udid);
		   String deviceName = deviceinfoProvider.getDeviceName();
		   
		   iTestContext.setAttribute("deviceName", deviceName);
		   
		   if(appiumManager.isPortBusy(devicePort) == true) {
			   log.warn("device Busy : " + deviceName + ", udid : "+ udid+ ", devicePort : "+ devicePort);
			   throw new SkipException("device Busy : "+ " : " + deviceName + "_" +udid); 
		   }
	   }
   }
  
   @Parameters({"udid"})
   @AfterTest
   public synchronized void AfterTest(@Optional String udid, ITestContext Testctx)  {
	   AppiumManager appiumManager = new AppiumManager();
	   
	   int _port = deviceManager.getDevicePort(udid);
	   
	   if(appiumManager.isPortBusy(_port) == true) {
		   appiumManager.killPort(_port);
	   }
   }
   
   @SuppressWarnings("unchecked")
   @BeforeMethod
   @Parameters({"udid"})
   public synchronized void BeforeClass (@Optional String udid, ITestContext Testctx, Method method) {
	   
	   
	   AppiumManager appiumManager = new AppiumManager();
	   
	   String methodName = method.getName();
	   String _deviceName = "";
	   String platForm = "";
	   String platFormVersion = "";

	   if(udid != null ) {
		   server = appiumManager.AppiumService();
		   
		   server.start();
		   
		   //Create Session
		   try {
			   tlDriverFactory.setDriver(server, udid);
			   
			   driverMap.put(Thread.currentThread().getId(),tlDriverFactory.getDriver());
			
			   driver = driverMap.get(Long.valueOf(Thread.currentThread().getId()));
			   
		   } catch (Exception e) {
			   log.error(e.getLocalizedMessage());
			   throw new SkipException(e.getLocalizedMessage()); 
		   }
		   
		   if("Auto".equalsIgnoreCase(udid))
			   udid = ((AppiumDriver<MobileElement>) driver).getCapabilities().getCapability("udid").toString();
		   
		   DeviceinfoProvider deviceinfoProvider = new DeviceinfoProvider(udid);
		   
		   _deviceName = deviceinfoProvider.getDeviceName() + " : "+ udid;
		   platForm = deviceinfoProvider.getPlatformName();
		   platFormVersion = deviceinfoProvider.getPlatformVersion();
	   
		   //Report Content
		   test = extent.createTest(methodName+"("+platForm+")").assignDevice(_deviceName);
		   String[][] data = {
				{ "<b>TestCase : </b>", methodName},
				{ "<b>Device : </b>", _deviceName},
			    { "<b>UDID : </b>", udid },
			    { "<b>Platform : </b>", platForm },
			    { "<b>OsVersion : </b>", platFormVersion },
			    
			};
			
			test.info(MarkupHelper.createTable(data));
			
			log.info("Platform : "+platForm);
			log.info("device : "+_deviceName);
			log.info("udid : "+ udid);
	   }else {
		   test = extent.createTest(methodName);
		   test.info( "TestCase : "+ methodName);
		   
	   }
	   log.info("TestCase : " + methodName);
    }
	
	/**
	* Executed after Class
	*/
	@SuppressWarnings("unchecked")
	@AfterClass
	@Parameters({"udid"})
	public synchronized void AfterClass(@Optional String udid, ITestContext Testctx) {
		
		if(driver != null) {
			DeviceinfoProvider deviceinfoProvider = new DeviceinfoProvider(udid);
			String platForm = deviceinfoProvider.getPlatformName();
			try {
				if("Android".equalsIgnoreCase(platForm)) {
					((AndroidDriver<MobileElement>) driver).closeApp();
		    		((AndroidDriver<MobileElement>) driver).quit();
				}else {
					((AppiumDriver<MobileElement>) driver).terminateApp(((AppiumDriver<MobileElement>) driver).getCapabilities().getCapability("bundleId").toString());   
				}
				log.info("app close");
			} catch (Exception e) {
				//ignore
			}
	       test.info("THE END");
	       log.info("THE END");
	       
	       try {
	    	   tlDriverFactory.getDriver().quit();
	    	   log.info("TLdriver quit - done");
	       } catch (Exception e) {
				//ignore
	       }
	       
	       if(server.isRunning()) {
			   server.stop();
		   }
	    }	
	}
  
	/**
	* Executed once after all the tests
	*/
	@AfterSuite(alwaysRun = true)
	public void endSuit(ITestContext ctx) {
	   
		try {
			extent.flush(); //-----close extent-report
			log.info(Constants.EXTENT_HTML_REPORT);
		} catch (Exception e) {
			// ignore
		}

		
	}
	
}
