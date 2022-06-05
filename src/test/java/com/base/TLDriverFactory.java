package com.base;

import com.Driver.MobiDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;
import com.DataManager.DeviceInfoReader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class TLDriverFactory {

  // private static Logger log = Logger.getLogger(Class.class.getName());

  @SuppressWarnings("rawtypes")
  private ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();
  protected Map<Long, AppiumDriver> driverMap = new ConcurrentHashMap<Long, AppiumDriver>();
  private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();
  private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
  AppiumService appiumService = new AppiumService();
  private AppiumDriverLocalService server;

  int retry = 5;
  int interval = 1000;

  public synchronized void setDriver() throws MalformedURLException {

    ITestResult iTestResult = Reporter.getCurrentTestResult();
    Map<String, String> testParams =
        iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

    String platForm = testParams.get("platForm");
    String udid = testParams.get("udid");
    String REMOTE_HOST =
        testParams.get("REMOTE_HOST") == null ? "localhost" : testParams.get("REMOTE_HOST");

    if (REMOTE_HOST.equalsIgnoreCase("localhost")) {
      server = appiumService.AppiumServer();
      REMOTE_HOST = server.getUrl().toString();
    }

    int devicePort = 8100;
    DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
    String deviceName = deviceInfoReader.getString("name");
    String platformVersion = deviceInfoReader.getString("platformVersion");
    devicePort = deviceInfoReader.getInt("devicePort");

    desiredCapabilities = capabilitiesManager.loadJSONCapabilities();
    desiredCapabilities.setCapability("deviceName", deviceName);
    desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);

    if ("Android".equalsIgnoreCase(platForm)) {

      desiredCapabilities.setCapability("systemPort", devicePort);

      tlDriver.set(new AndroidDriver<MobileElement>(new URL(REMOTE_HOST), desiredCapabilities));

    } else if ("iOS".equalsIgnoreCase(platForm)) {

      desiredCapabilities.setCapability("wdaLocalPort", devicePort);

      tlDriver.set(new IOSDriver<MobileElement>(new URL(REMOTE_HOST), desiredCapabilities));

    }
    driverMap.put(Thread.currentThread().getId(), tlDriver.get());
  }

  private synchronized AppiumDriver getDriver() {
    return tlDriver.get();
  }

  public synchronized AppiumDriver getDriverInstance() {
    return driverMap.get(Long.valueOf(Thread.currentThread().getId()));
  }

  public synchronized void quit() {

    getDriverInstance().quit();

    if (server.isRunning())
      server.stop();

  }
}
