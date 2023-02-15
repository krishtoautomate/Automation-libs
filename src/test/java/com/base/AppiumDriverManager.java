package com.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.remote.SessionId;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppiumDriverManager {

    static Map<Long, AppiumDriver> driverMap = new ConcurrentHashMap<Long, AppiumDriver>();
    private static AppiumDriverLocalService server;
    private ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();
    private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();

    public static synchronized AppiumDriver getDriverInstance() {
        return driverMap.get(Long.valueOf(Thread.currentThread().getId()));
    }

    public static synchronized void quit() {
        getDriverInstance().quit();

        if (server.isRunning()) {
            server.stop();
        }
    }

    public SessionId getSessionId() {
        return getDriverInstance().getSessionId();
    }

    public synchronized void setDriver() throws IOException {

        ITestResult iTestResult = Reporter.getCurrentTestResult();
        Map<String, String> testParams =
                iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();
//        iTestResult.getTestContext().getAttribute("udid");

        String platForm = testParams.get("platForm");
//        String udid = testParams.get("udid");
        if (platForm == null)
            return;

        String REMOTE_HOST =
                testParams.get("REMOTE_HOST") == null ? "localhost" : testParams.get("REMOTE_HOST");

        if (REMOTE_HOST.equalsIgnoreCase("localhost")) {
            AppiumService appiumService = new AppiumService();
            server = appiumService.AppiumServer();
            server.start();
            REMOTE_HOST = server.getUrl().toString();
        }

        if ("Android".equalsIgnoreCase(platForm)) {
//            iTestContext.setAttribute("platForm", "ANDROID");
            tlDriver.set(new AndroidDriver(new URL(REMOTE_HOST),
                    capabilitiesManager.setCapabilities("ANDROID")));
        } else if ("iOS".equalsIgnoreCase(platForm)) {
//            iTestContext.setAttribute("platForm", "IOS");
            tlDriver.set(new IOSDriver(new URL(REMOTE_HOST),
                    capabilitiesManager.setCapabilities("IOS")));
        }

        driverMap.put(Thread.currentThread().getId(), tlDriver.get());

        // System.out.println("Thread Id : "+ Thread.currentThread().getId());
        System.out.println("SessionId : " + tlDriver.get().getSessionId());

        getDriverInstance().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    }
}
