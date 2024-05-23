package other.testcases;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class TestAndroid {


    public static void main(String[] args) throws MalformedURLException {
        // Set the Desired Capabilities
        DesiredCapabilities caps = new DesiredCapabilities();
        //caps.setCapability("deviceName", "emulator-5554");
        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName", "uiautomator2");
//        caps.setCapability("platformVersion", "14");
        caps.setCapability("appPackage", "ca.bell.selfserve.mybellmobile");
        caps.setCapability("appActivity", "ca.bell.selfserve.mybellmobile.ui.splash.view.SplashActivity");
//        caps.setCapability("appName", "MyBell");
        caps.setCapability("noReset", false);
        caps.setCapability("appium:fullReset", false);
//        caps.setCapability("appium:udid", "1B221FDF6001L3");
        //caps.setCapability("autoGrantPermissions", true);
        //caps.setCapability(MobileCapabilityType.ACCEPT_SSL_CERTS, true);
        //caps.setCapability(MobileCapabilityType.ACCEPT_INSECURE_CERTS, true);

        // Initialize the Appium Driver
        AppiumDriver driver = new AndroidDriver(new URL("http://bqatautomation.bell.corp.bce.ca:3333"), caps);

        // Perform actions on the Calculator app
        // For example, you can find elements by their IDs and perform calculations

        // Close the driver
        driver.quit();
    }
}
