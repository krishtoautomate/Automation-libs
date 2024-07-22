package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenshotManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.base.TestBaseWeb;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WebTest extends TestBaseWeb implements ITestBase {

    @Test
    public void Web_Test() {

        String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
        test.getModel().setName(String.format("%s", browserName));


//        System.out.println(driver instanceof AndroidDriver);
//        System.out.println(driver instanceof AppiumDriver);
//        if (!(driver instanceof AppiumDriver)) {
//
//        }


        //for remote but not working in docker due to port issue
//        Augmenter augmenter = new Augmenter();
//
//        driver = augmenter.augment(driver);
//
//        DevTools devTools = ((HasDevTools) driver).getDevTools();
//        devTools.createSession();
//
//        driver = augmenter.
//                addDriverAugmentation("chrome", HasAuthentication.class, (caps, exec) -> (whenThisMatches, useTheseCredentials) -> devTools.getDomains().network().addAuthHandler(whenThisMatches, useTheseCredentials)).augment(driver);

        byte[] credDecoded = Base64.getDecoder().decode("a3Jpc2gucGF2dWx1cjpOZW9sb2FkZXIyODI3JA==");
        String auth = new String(credDecoded, StandardCharsets.UTF_8);
        String username = auth.split(":")[0];
        String password = auth.split(":")[1];
//        ((HasAuthentication) driver).register(() -> new UsernameAndPassword(username, password));
//        driver.get("https://bca-csr-ui.int.bell.ca/eCareBellCa/BAT");
//        //        driver.get("https://"+auth+"@bca-csr-ui.int.bell.ca/eCareBellCa/BAT");
//        test.pass("web-page with auth loaded : " + driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());


//        driver = new Augmenter().augment(driver);

        //chrome dev tools
        driver.get("file:///Downloads/index.html");

        sleep(5);

//        String hostName = ((RemoteWebDriver) driver).get
//        String nodeUrl = (String) ((RemoteWebDriver) driver).getCapabilities().getCapability("webdriver.remote.sessionid");
//        System.out.println(nodeUrl);

        driver.get("https://www.google.com");
        test.pass("web-page loaded : " + driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());
        sleep(5);

//        System.out.println(Runtime.getRuntime().exec("SE_NODE_HOST"));//SE_NODE_HOST

//        driver.findElement(By.xpath("//*[@name='q']")).isDisplayed();
//
//        driver.findElement(By.xpath("//*[@name='btnG']")).isDisplayed();

//        DevTools devTools = ((HasDevTools) driver).getDevTools();
//        devTools.createSession();

        //file upload POC
//        driver.navigate().to("https://www.bell.ca/");
//        sleep(5);
//        test.pass("web-page loaded : " + driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());

//        sleep(5);
//
//        driver.findElement(By.tagName("input")).sendKeys("//files//Bell_Shop_NewCommerFlow_UnverifiedFormE2Evalidation.png");
//
//
//


//        String errorXML = driver.getPageSource();

        System.out.println("SessionId : " + ((RemoteWebDriver) driver).getSessionId());


//        test.info(MarkupHelper.createCodeBlock(errorXML));


    }
}
