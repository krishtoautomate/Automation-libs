package com.base;

import com.Utilities.Constants;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.testng.ITestResult;
import org.testng.Reporter;

@SuppressWarnings("rawtypes")
public class AppiumDriverManager {

  private static AppiumDriverLocalService server;
  static Map<Long, AppiumDriver> driverMap = new ConcurrentHashMap<Long, AppiumDriver>();
  private ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();
  private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();

  public synchronized void setDriver() throws MalformedURLException {

    ITestResult iTestResult = Reporter.getCurrentTestResult();
    Map<String, String> testParams =
        iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

    String platForm = testParams.get("platForm");
    String REMOTE_HOST =
        testParams.get("REMOTE_HOST") == null ? "localhost" : testParams.get("REMOTE_HOST");

    if (REMOTE_HOST.equalsIgnoreCase("localhost")) {
      AppiumService appiumService = new AppiumService();
      server = appiumService.AppiumServer();
      server.start();
      REMOTE_HOST = server.getUrl().toString();
    }

    if ("Android".equalsIgnoreCase(platForm)) {
      tlDriver.set(new AndroidDriver(new URL(REMOTE_HOST),
          capabilitiesManager.setCapabilities()));
    } else if ("iOS".equalsIgnoreCase(platForm)) {
      tlDriver.set(new IOSDriver(new URL(REMOTE_HOST),
          capabilitiesManager.setCapabilities()));
    }

    driverMap.put(Thread.currentThread().getId(), tlDriver.get());

    // System.out.println("Thread Id : "+ Thread.currentThread().getId());
    // System.out.println("Session Id : "+ tlDriver.get().getSessionId());

    getDriverInstance().manage().timeouts().implicitlyWait(Constants.IMPLICITLYWAIT,
        TimeUnit.SECONDS);

  }

  public static synchronized AppiumDriver getDriverInstance() {
    return driverMap.get(Long.valueOf(Thread.currentThread().getId()));
  }

  public static synchronized void quit() {
    getDriverInstance().quit();

    if (server.isRunning()) {
      server.stop();
    }

  }

//  private boolean isAndroid() {
//    ITestResult iTestResult = Reporter.getCurrentTestResult();
//    Map<String, String> testParams =
//        iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();
//    return "Android".equalsIgnoreCase(testParams.get("platForm"));
//  }
}
