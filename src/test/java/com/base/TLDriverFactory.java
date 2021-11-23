package com.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
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

  private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();
  private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
  AppiumManager appiumManager = new AppiumManager();
  AppiumDriverLocalService server;

  int retry = 5;
  int interval = 1000;

  public synchronized void setDriver() throws MalformedURLException {

    // server = appiumManager.AppiumService();

    ITestResult iTestResult = Reporter.getCurrentTestResult();
    Map<String, String> testParams =
        iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

    String udid = testParams.get("udid");
    String platForm = testParams.get("platForm");
    String remoteHost = testParams.get("remoteHost");

    int devicePort = 8100;
    DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
    String deviceName = deviceInfoReader.getString("name");
    String platformVersion = deviceInfoReader.getString("platformVersion");
    devicePort = deviceInfoReader.getInt("devicePort");

    if ("Android".equalsIgnoreCase(platForm)) {

      desiredCapabilities = capabilitiesManager.loadJSONCapabilities("ANDROID");

      desiredCapabilities.setCapability("deviceName", deviceName);
      desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
      desiredCapabilities.setCapability("systemPort", devicePort);// sysPort
      // desiredCapabilities.setCapability("platformVersion", platformVersion == null ? "8.1.0" :
      // platformVersion);

      tlDriver.set(new AndroidDriver<MobileElement>(new URL(server.getUrl().toString()),
          desiredCapabilities));

    } else if ("iOS".equalsIgnoreCase(platForm)) {

      desiredCapabilities = capabilitiesManager.loadJSONCapabilities("IOS");

      desiredCapabilities.setCapability("deviceName", deviceName);
      desiredCapabilities.setCapability("udid", udid);
      desiredCapabilities.setCapability("wdaLocalPort", devicePort);
      desiredCapabilities.setCapability("platformVersion",
          platformVersion == null ? "12.2" : platformVersion);

      tlDriver.set(new IOSDriver<MobileElement>(new URL(remoteHost), desiredCapabilities));

    }
  }

  public synchronized WebDriver getDriver() {
    return tlDriver.get();
  }

  public synchronized void quit() {

    tlDriver.get().quit();

    if (server.isRunning())
      server.stop();

  }
}
