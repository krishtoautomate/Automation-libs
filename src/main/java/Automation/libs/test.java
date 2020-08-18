package Automation.libs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class test {
	
	protected static WebDriver driver;

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		// TODO Auto-generated method stub
		
		String platForm = "Android";
		
		String udid = "R58N70GGVZH";
		

//		"androidInstallTimeout": 150000,
//		"deviceReadyTimeout":30,
//		"androidDeviceReadyTimeout":60,
//
//	    "appPackage":"ca.bell.selfserve.mybellmobile.preprod",
//	    "appActivity":"ca.bell.selfserve.mybellmobile.ui.splash.view.SplashActivity"
		
		
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		
		desiredCapabilities.setCapability("platformName","Android");
		desiredCapabilities.setCapability("platformVersion","8.1.0");
		desiredCapabilities.setCapability("automationName","uiautomator2");
		desiredCapabilities.setCapability("deviceName","Samsung");
		desiredCapabilities.setCapability("autoLaunch","true");
		desiredCapabilities.setCapability("noReset","true");
		desiredCapabilities.setCapability("fullReset","false");
		desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
//		desiredCapabilities.setCapability("systemPort", "");	
//		desiredCapabilities.setCapability("wdaLocalPort", "");
		desiredCapabilities.setCapability("appPackage","ca.bell.selfserve.mybellmobile.preprod");
		desiredCapabilities.setCapability("appActivity","ca.bell.selfserve.mybellmobile.ui.splash.view.SplashActivity");
		
		if("Android".equalsIgnoreCase(platForm))
			driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
		else
			driver = new AppiumDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
		
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Press any key to continue . . . ");
	    scan.nextLine();
		
//	    System.out.println(driver.getPageSource());
		
	    //close session
//		driver.quit();

	}

}
