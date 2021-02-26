package com.base;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.DataManager.TestDataManager;
import com.DeviceManager.DeviceDAO;
import com.DeviceManager.DeviceInfo;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;

/**
 * Created by Krish on 06.06.2018.
 */
public class TestBase {

	@SuppressWarnings("rawtypes")
	protected AppiumDriver driver;
	protected Map<Long, WebDriver> driverMap = new ConcurrentHashMap<Long, WebDriver>();
	protected WebDriverWait wait;
	protected TLDriverFactory tlDriverFactory = new TLDriverFactory();
	protected static Logger log;
//   protected ExtentHtmlReporter htmlReporter;
	protected ExtentSparkReporter htmlReporter;
	protected static ExtentReports extent;
	protected ExtentTest test;
	protected ScreenShotManager screenShotManager;
	protected DeviceInfo deviceManager = new DeviceInfo();
	AppiumManager appiumManager = new AppiumManager();

	int retry = 10;
	int interval = 1000;

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
	@BeforeSuite(alwaysRun = true)
	public void setupSuit(ITestContext ctx) {

		String suiteName = ctx.getCurrentXmlTest().getSuite().getName();

		// Log4j
		log = Logger.getLogger(suiteName);

		// create Report Folder in 'test-output'
		File reportDir = new File(Constants.REPORT_DIR);
		if (!reportDir.exists()) {
			reportDir.mkdirs();
			log.info("created Folder for Report: " + reportDir.getAbsolutePath().toString());
		}

		// extent report
		extent = new ExtentReports();
		htmlReporter = new ExtentSparkReporter(Constants.EXTENT_HTML_REPORT).viewConfigurer().viewOrder()
				.as(new ViewName[] { ViewName.DASHBOARD, ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
						ViewName.EXCEPTION, ViewName.LOG })
				.apply();
		extent.attachReporter(htmlReporter);

		htmlReporter.config().setDocumentTitle("AUTOMATION REPORT");
		htmlReporter.config().setReportName("AUTOMATION REPORT");
		htmlReporter.config().setTheme(Theme.STANDARD);

		// HOST INFO
		extent.setSystemInfo("OS", Constants.HOST_OS);
		extent.setSystemInfo("HostIPAddress", Constants.HOST_IP_ADDRESS());
		extent.setSystemInfo("Host Name", Constants.HOST_NAME());

	}

	@BeforeTest
	@Parameters({ "udid" })
	public synchronized void BeforeTest(@Optional String udid, ITestContext iTestContext) {
		if (udid != null) {
			if (!udid.equalsIgnoreCase("auto")) {

				DeviceDAO deviceinfoProvider = new DeviceDAO(udid);

				String deviceName = deviceinfoProvider.getDeviceName();

				iTestContext.setAttribute("udid", udid);
				iTestContext.setAttribute("deviceName", deviceName);

				int devicePort = deviceManager.getDevicePort(udid);
				if (appiumManager.isPortBusy(devicePort)) {
					log.warn("device Busy : " + deviceName + ", udid : " + udid + ", devicePort : " + devicePort);
					throw new SkipException("device Busy : " + " : " + deviceName + "_" + udid);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@BeforeMethod
	@Parameters({ "udid" })
	public synchronized void BeforeClass(@Optional String udid, ITestContext iTestContext, Method method) {

		String methodName = method.getName();
		String className = this.getClass().getName();
		String deviceName = "";
		String platForm = "";
		String platFormVersion = "";
		int devicePort = deviceManager.getDevicePort(udid);

		if (udid != null) {

			server = appiumManager.AppiumService();

			if (server.isRunning())
				server.stop();

			while (retry > 0) {
				try {
					server.start();

					if (server.isRunning())
						break;
				} catch (AppiumServerHasNotBeenStartedLocallyException e) {
					log.error(e.getLocalizedMessage());

					retry--;
					log.info("\nAttempted: " + (60 - retry) + ", Appium Failed to start, Retrying...\n" + e);
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e1) {
						// Ignore
					}
					server = appiumManager.AppiumService();
				}
			}

			// Create Session
			try {
				tlDriverFactory.setDriver(server, iTestContext);

				driverMap.put(Thread.currentThread().getId(), tlDriverFactory.getDriver());

				driver = (AppiumDriver<MobileElement>) driverMap.get(Long.valueOf(Thread.currentThread().getId()));

			} catch (Exception e) {
				if (appiumManager.isPortBusy(devicePort)) {
					appiumManager.killPort(devicePort);
				}

				log.error("session failed : " + e.getLocalizedMessage());
				throw new SkipException("session failed : " + e.getLocalizedMessage());
			}

			/*
			 * Test info
			 */
			if ("Auto".equalsIgnoreCase(udid))
				udid = ((AppiumDriver<MobileElement>) driver).getCapabilities().getCapability("udid").toString();

			iTestContext.setAttribute("udid", udid);
			DeviceDAO deviceinfoProvider = new DeviceDAO(udid);
			deviceName = deviceinfoProvider.getDeviceName();
			platForm = deviceinfoProvider.getPlatformName();
			platFormVersion = deviceinfoProvider.getosVersion();

			Map<String, String> testParams = iTestContext.getCurrentXmlTest().getAllParameters();
			String p_Testdata = testParams.get("p_Testdata");
			TestDataManager testData = new TestDataManager(p_Testdata, className, platForm);
			int index = driver instanceof AndroidDriver ? 0 : 1;
			String testKey = testData.getJsonValue(index, "testKey");

			// Report Content
			test = extent.createTest(methodName + "(" + platForm + ")").assignDevice(deviceName);

			log.info("Test Details : " + methodName + " : " + platForm + " : " + deviceName);
			String[][] data = { { "<b>TestCase : </b>", className }, { "<b>Device : </b>", deviceName },
					{ "<b>UDID : </b>", udid }, { "<b>Platform : </b>", platForm },
					{ "<b>OsVersion : </b>", platFormVersion }, { "<b>Jira test-key : </b>", testKey }

			};

			test.info(MarkupHelper.createTable(data));

		} else {
			test = extent.createTest(methodName);
			test.info("TestCase : " + methodName);
		}

	}

	/**
	 * Executed after Class
	 */
	@SuppressWarnings("unchecked")
	@AfterMethod
	@Parameters({ "udid" })
	public synchronized void AfterClass(@Optional String udid, ITestContext Testctx) {

		if (driver != null) {
			DeviceDAO deviceinfoProvider = new DeviceDAO(udid);
			String platForm = deviceinfoProvider.getPlatformName();
			try {
				if ("Android".equalsIgnoreCase(platForm)) {
					((AndroidDriver<MobileElement>) driver).closeApp();
					((AndroidDriver<MobileElement>) driver).quit();
				} else {
					((AppiumDriver<MobileElement>) driver).terminateApp(((AppiumDriver<MobileElement>) driver)
							.getCapabilities().getCapability("bundleId").toString());
				}
				log.info("app close");
			} catch (Exception e) {
				// ignore
			}
			test.info("THE END");
			log.info("THE END");

			try {
				tlDriverFactory.getDriver().quit();
				log.info("TLdriver quit - done");
			} catch (Exception e) {
				// ignore
			}

			if (server.isRunning()) {
				server.stop();
			}

			int _port = deviceManager.getDevicePort(udid);
			if (appiumManager.isPortBusy(_port)) {
				appiumManager.killPort(_port);
			}

		}
	}

	/**
	 * Executed once after all the tests
	 */
	@AfterSuite(alwaysRun = true)
	public void endSuit(ITestContext ctx) {

		try {
			extent.flush(); // -----close extent-report
			log.info(Constants.EXTENT_HTML_REPORT);
		} catch (Exception e) {
			// ignore
		}
	}

}
