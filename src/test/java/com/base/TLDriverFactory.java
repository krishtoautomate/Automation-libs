package com.base;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import com.Utilities.Constants;
import com.deviceinformation.DeviceInfo;
import com.deviceinformation.DeviceInfoImpl;
import com.deviceinformation.device.DeviceType;
import com.deviceinformation.exception.DeviceNotFoundException;
import com.deviceinformation.model.Device;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class TLDriverFactory {

  private static Logger log = Logger.getLogger(Class.class.getName());

  @SuppressWarnings("rawtypes")
  private ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();

  private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();
  private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
  AppiumManager appiumManager = new AppiumManager();

  int retry = 5;
  int interval = 1000;

  public synchronized void setDriver(AppiumDriverLocalService server, ITestContext Testctx)
      throws IOException, DeviceNotFoundException {

    Map<String, String> testParams = Testctx.getCurrentXmlTest().getAllParameters();

    String udid = testParams.get("udid");
    String platForm = testParams.get("platForm");

    int devicePort = 8100;
    devicePort = appiumManager.getDevicePort(udid);

    // String deviceName = deviceinfoProvider.getDeviceName();
    // String platForm = deviceinfoProvider.getPlatformName();

    if ("Android".equalsIgnoreCase(platForm)) {

      DeviceInfo deviceInfo = new DeviceInfoImpl(DeviceType.ANDROID);

      Device device = deviceInfo.getUdid(udid);

      String deviceName = device.getDeviceName();



      while (retry > 0) {
        try {

          desiredCapabilities =
              capabilitiesManager.loadJSONCapabilities(Constants.ANDROID_CAPABILITIES, "ANDROID");

          desiredCapabilities.setCapability("deviceName", deviceName);
          desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
          desiredCapabilities.setCapability("systemPort", devicePort);// sysPort

          tlDriver.set(new AndroidDriver<MobileElement>(new URL(server.getUrl().toString()),
              desiredCapabilities));
          break;
        } catch (Exception e) {
          // Decrement Retry interval
          retry--;
          log.info("\nAttempted: " + (60 - retry) + ". Failure to find device(" + udid
              + "), Retrying.....\n" + e);
          try {
            Thread.sleep(interval);
          } catch (InterruptedException e1) {
            // ignore
          }
        }
      }
    } else if ("iOS".equalsIgnoreCase(platForm)) {

      DeviceInfo deviceInfo = new DeviceInfoImpl(DeviceType.IOS);

      Device device = deviceInfo.getUdid(udid);

      device.getDeviceName();

      // if(!"Auto".equalsIgnoreCase(udid))
      appiumManager.uninstall_WDA(udid);

      desiredCapabilities =
          capabilitiesManager.loadJSONCapabilities(Constants.IOS_CAPABILITIES, "IOS");

      desiredCapabilities.setCapability("deviceName", "Test Device");
      desiredCapabilities.setCapability("udid", udid);
      desiredCapabilities.setCapability("wdaLocalPort", devicePort);

      tlDriver.set(
          new IOSDriver<MobileElement>(new URL(server.getUrl().toString()), desiredCapabilities));

    }
  }

  public synchronized WebDriver getDriver() {
    return tlDriver.get();
  }
}
