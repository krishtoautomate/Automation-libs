package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenShotManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseWeb;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.Map;

public class WebTest extends TestBaseWeb implements ITestBase {

    @Test
    public void Web_Test(){

//        ((HasAuthentication) driver).register(UsernameAndPassword.of("username", "pass"));

        String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
        test.getModel().setName(String.format("%s", browserName));

        try {
            driver.get("https://bell.ca");
//            driver.get("https://fesa-mybell.ids.int.bell.ca/Login");
//            driver.get("https://www.virginplus.ca/en/home/index.html");

        } catch (Exception e) {
            driver.navigate().to("https://www.virginplus.ca/en/home/index.html");
//            test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManager.getScreenshot(driver)).build());
        }

//        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
//        System.out.println(caps.getBrowserName());
//        System.out.println(caps.getBrowserVersion());

        sleep(10);

        test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManager.getScreenshot(driver)).build());

        String errorXML = driver.getPageSource();

//        System.out.println("SessionId : " + ((RemoteWebDriver) driver).getSessionId());

        test.info(MarkupHelper.createCodeBlock(errorXML));

//        utils.logmessage(Status.PASS, "Web page launched  : "+driver.getCurrentUrl());
//        utils.getPageSource();
    }
}
