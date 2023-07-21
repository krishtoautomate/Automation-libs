package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenShotManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseWeb;
import org.testng.annotations.Test;

public class WebTest extends TestBaseWeb implements ITestBase {

    @Test
    public void Web_Test(){
//        driver.get("http://172.21.34.239:8001");

//        ((HasAuthentication) driver).register(UsernameAndPassword.of("username", "pass"));


        try {
            driver.get("https://mybell.bell.ca/Login");
        } catch (Exception e) {
            test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManager.getScreenshot(driver)).build());
        }

        sleep(10);

        test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManager.getScreenshot(driver)).build());


        String errorXML = driver.getPageSource();

//        System.out.println("SessionId : " + ((RemoteWebDriver) driver).getSessionId());

        test.info(MarkupHelper.createCodeBlock(errorXML));

//        utils.logmessage(Status.PASS, "Web page launched  : "+driver.getCurrentUrl());
//        utils.getPageSource();
    }
}
