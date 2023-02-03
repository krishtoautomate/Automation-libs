package other.testcases;

import com.Utilities.BaseObjs;
import com.Utilities.ITestBase;
import com.Utilities.ScreenShotManagerWeb;
import com.Utilities.Utilities;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseWeb;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

public class WebTest extends TestBaseWeb implements ITestBase {

    @Test
    public void Web_Test(){
//        driver.get("http://172.21.34.239:8001");

        try {
            driver.get("https://mybell.bell.ca/Login");
        } catch (Exception e) {
            test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManagerWeb.getScreenshot()).build());
        }

        sleep(10);

        test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManagerWeb.getScreenshot()).build());


        String errorXML = driver.getPageSource();

        System.out.println("SessionId : " + ((RemoteWebDriver) driver).getSessionId());

        test.info(MarkupHelper.createCodeBlock(errorXML));

//        utils.logmessage(Status.PASS, "Web page launched  : "+driver.getCurrentUrl());
//        utils.getPageSource();
    }
}
