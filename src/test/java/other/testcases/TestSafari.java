package other.testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class TestSafari {


    public static void main(String[] args) throws MalformedURLException {
        // Set the Desired Capabilities
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("browserName", "safari");
//        caps.setCapability("platformName", "Android");
//        caps.setCapability("automationName", "uiautomator2");
////        caps.setCapability("platformVersion", "14");
//        caps.setCapability("appPackage", "ca.bell.selfserve.mybellmobile");
//        caps.setCapability("appActivity", "ca.bell.selfserve.mybellmobile.ui.splash.view.SplashActivity");
////        caps.setCapability("appName", "MyBell");
//        caps.setCapability("noReset", false);
//        caps.setCapability("appium:fullReset", false);
////        caps.setCapability("appium:udid", "1B221FDF6001L3");
//        //caps.setCapability("autoGrantPermissions", true);
//        //caps.setCapability(MobileCapabilityType.ACCEPT_SSL_CERTS, true);
//        //caps.setCapability(MobileCapabilityType.ACCEPT_INSECURE_CERTS, true);

        // Initialize the Appium Driver
        WebDriver driver = new RemoteWebDriver(new URL("http://192.168.2.27:4444"), desiredCapabilities);

//        WebDriver driver = new SafariDriver();


        // Perform actions on the Calculator app
        // For example, you can find elements by their IDs and perform calculations

        // Close the driver
        driver.quit();
    }
}
