package other.testcases;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MBM {

    static AppiumDriver driver;
    static By loginUser =
            By.xpath("//android.widget.EditText[contains(@resource-id, 'usernameEditText')]");
    static By loginPwd =
            By.xpath("//android.widget.EditText[contains(@resource-id, 'passwordEditText')]");
    // static By keepMeLoginIn_btn = By.xpath("//android.widget.Switch[contains(@resource-id,
    // 'keepMeLoggedInSwitch')]");
    static By LoginIn_btn = By.xpath("//*[contains(@resource-id,'loginButton')]");
    static By more_btn = By.xpath("//android.widget.TextView[contains(@resource-id,'id/more')]");

    public static void main(String[] args) throws MalformedURLException {

        // TestDataManager TestDataManager = new com.DataManager.TestDataManager(filePath, className,
        // platformName);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        desiredCapabilities.setCapability("deviceName", "Android");
        desiredCapabilities.setCapability("udid", "0B271FDD4003AS");// device id
        // //adb devices
        // //
        // commnad
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("platformVersion", "8.1.0");
        desiredCapabilities.setCapability("automationName", "uiautomator2");
        desiredCapabilities.setCapability("autoLaunch", true);
        desiredCapabilities.setCapability("noReset", true);
        desiredCapabilities.setCapability("fullReset", false);
        desiredCapabilities.setCapability("ignoreUnimportantViews", true);

        // desiredCapabilities.setCapability("appWaitDuration", 5);
        // desiredCapabilities.setCapability("androidDeviceReadyTimeout", 5);

        // desiredCapabilities.setCapability("avdReadyTimeout", 5);

        desiredCapabilities.setCapability("appPackage", ".dev.projectD");
        desiredCapabilities.setCapability("appActivity",
                ".ui.splash.view.SplashActivity");

        // desiredCapabilities.setCapability("appPackage",
        // "ca..myaccount.");//5sec
        // desiredCapabilities.setCapability("appActivity",
        // "ca..myaccount..ui.splash.view.SplashActivity");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),
                desiredCapabilities);
        // driver = new AndroidDriver<MobileElement>(new URL(server.getUrl().toString()),
        // desiredCapabilities);

        System.out.println(dtf.format(LocalDateTime.now()));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.findElement(loginUser).sendKeys("user");
        System.out.println("user : user");

        driver.findElement(loginPwd).sendKeys("1234$");
        System.out.println("password : 1234$");

        // wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(keepMeLoginIn_btn));
        // driver.findElement(keepMeLoginIn_btn).click();
        // System.out.println("KeepMeLoggedIn : Disabled");

        driver.findElement(LoginIn_btn).click();
        System.out.println(dtf.format(LocalDateTime.now()) + " : LogIn : Clicked");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Click 'More' button
        driver.findElement(more_btn).click();
        // ((AndroidDriver<MobileElement>) driver).findElementByAndroidUIAutomator("new
        // UiSelector().resourceId(\"id/more\")").click();
        System.out.println(dtf.format(LocalDateTime.now()) + " : More button : Clicked");

    }

}
