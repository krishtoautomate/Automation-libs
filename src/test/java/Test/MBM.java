package Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.DataManager.TestDataManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class MBM {
	
	static AppiumDriver<MobileElement> driver;
	static WebDriverWait wait;
	
	static By loginUser = By.xpath("//android.widget.EditText[contains(@resource-id, 'usernameEditText')]");
	static By loginPwd = By.xpath("//android.widget.EditText[contains(@resource-id, 'passwordEditText')]");
//	static By keepMeLoginIn_btn = By.xpath("//android.widget.Switch[contains(@resource-id, 'keepMeLoggedInSwitch')]");
	static By LoginIn_btn = By.xpath("//*[contains(@resource-id,'loginButton')]");
	
	static By more_btn = By.xpath("//android.widget.TextView[contains(@resource-id,'id/more')]");
	
	protected static AppiumDriverLocalService server;
	
	
	
	public static void main(String[] args) throws MalformedURLException {
		
//		TestDataManager TestDataManager = new com.DataManager.TestDataManager(filePath, className, platformName);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		 
		
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		
		desiredCapabilities.setCapability("deviceName","Android");
		desiredCapabilities.setCapability(MobileCapabilityType.UDID, "R58N70GGVZH");//device id //adb devices commnad
		desiredCapabilities.setCapability("platformName", "Android");
		desiredCapabilities.setCapability("platformVersion", "8.1.0");
		desiredCapabilities.setCapability("automationName", "uiautomator2");
		desiredCapabilities.setCapability("autoLaunch", true);
		desiredCapabilities.setCapability("noReset", true);
		desiredCapabilities.setCapability("fullReset", false);
		
//		desiredCapabilities.setCapability("appWaitDuration", 5);
//		desiredCapabilities.setCapability("androidDeviceReadyTimeout", 5);
		
//		desiredCapabilities.setCapability("avdReadyTimeout", 5);
		
		
		
		desiredCapabilities.setCapability("appPackage", "ca.bell.selfserve.mybellmobile.preprod");
		desiredCapabilities.setCapability("appActivity", "ca.bell.selfserve.mybellmobile.ui.splash.view.SplashActivity");
		
//		desiredCapabilities.setCapability("appPackage", "ca.virginmobile.myaccount.virginmobile.preprod");//5sec
//		desiredCapabilities.setCapability("appActivity", "ca.virginmobile.myaccount.virginmobile.ui.splash.view.SplashActivity");
		
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
//		driver = new AndroidDriver<MobileElement>(new URL(server.getUrl().toString()), desiredCapabilities);
		
		System.out.println(dtf.format(LocalDateTime.now())); 
		
		wait = new WebDriverWait(driver, 5);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(loginUser));
		driver.findElement(loginUser).sendKeys("automation5");
		System.out.println("user : automation5");
		
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(loginPwd));
		driver.findElement(loginPwd).sendKeys("Quebec2021");
		System.out.println("password : Quebec2021");
		
//		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(keepMeLoginIn_btn));
//		driver.findElement(keepMeLoginIn_btn).click();
//		System.out.println("KeepMeLoggedIn : Disabled");
		
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(LoginIn_btn));
		driver.findElement(LoginIn_btn).click();
		System.out.println(dtf.format(LocalDateTime.now()) + " : LogIn : Clicked");
		
		//Click 'More' button
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(more_btn));
		driver.findElement(more_btn).click();
//		((AndroidDriver<MobileElement>) driver).findElementByAndroidUIAutomator("new UiSelector().resourceId(\"id/more\")").click();
		System.out.println(dtf.format(LocalDateTime.now()) + " : More button : Clicked");
		
		

	}

}
