package com.base;

import com.Listeners.TestListener;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.SessionId;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppiumDriverManager {

    static Map<Long, AppiumDriver> driverMap = new ConcurrentHashMap<Long, AppiumDriver>();
    private static AppiumDriverLocalService server;
    private ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();
    private CapabilitiesManager capabilitiesManager = new CapabilitiesManager();

    private static Logger log = Logger.getLogger(TestListener.class.getName());

    public static synchronized AppiumDriver getDriverInstance() {
        return driverMap.get(Thread.currentThread().getId());
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

    public synchronized void setDriver() {

        ITestResult iTestResult = Reporter.getCurrentTestResult();
        Map<String, String> testParams =
                iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

        String platForm = testParams.get("platForm");
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

        try {
            if ("Android".equalsIgnoreCase(platForm)) {
                tlDriver.set(new AndroidDriver(new URL(REMOTE_HOST),
                        capabilitiesManager.setCapabilities("ANDROID")));
            } else if ("iOS".equalsIgnoreCase(platForm)) {
                tlDriver.set(new IOSDriver(new URL(REMOTE_HOST),
                        capabilitiesManager.setCapabilities("IOS")));
            }
        } catch (Exception e) {
            log.error("Message : " + e.getMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }

        driverMap.put(Thread.currentThread().getId(), tlDriver.get());

        log.info("SessionId : " + tlDriver.get().getSessionId());

        getDriverInstance().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    }

    public synchronized void setBrowserDriver() {

        ITestResult iTestResult = Reporter.getCurrentTestResult();
        Map<String, String> testParams =
                iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

        String platForm = testParams.get("platForm");
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

        try {
            if ("Android".equalsIgnoreCase(platForm)) {
                tlDriver.set(new AndroidDriver(new URL(REMOTE_HOST),
                        capabilitiesManager.setCapabilities("ANDROID-BROWSER")));
            } else if ("iOS".equalsIgnoreCase(platForm)) {
                tlDriver.set(new IOSDriver(new URL(REMOTE_HOST),
                        capabilitiesManager.setCapabilities("IOS-BROWSER")));
            }
        } catch (Exception e) {
            log.error("Message : " + e.getMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }

        driverMap.put(Thread.currentThread().getId(), tlDriver.get());

        log.info("SessionId : " + tlDriver.get().getSessionId());

        getDriverInstance().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    }
}
