package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenShotManager;
import com.Utilities.ScreenShotManagerWeb;
import com.Utilities.Utilities;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.AppiumDriverManager;
import com.base.DriverManager;
import com.base.TestBaseHybrid;
import com.base.TestBaseWeb;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.Test;
import other.pages.PlayStoreApp;

public class HybridTest extends TestBaseHybrid implements ITestBase {

    @Test
    public void Hybrid_Test(){

        driver = DriverManager.getWebDriverInstance();

        driver.get("http://bqatautomation.bell.corp.bce.ca:8080/");

        sleep(5);

        test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManagerWeb.getScreenshot()).build());

        PlayStoreApp playstoreapp = new PlayStoreApp(DriverManager.getAppiumDriverInstance(), test);


        sleep(5);

        String errorXML = driver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));


        // 1.0 - Click navigate button
        playstoreapp.get_accountLogo().click();
        playstoreapp.logmessage(Status.PASS, "Right Account logo Button clicked");

        sleep(5);

        driver = DriverManager.getAppiumDriverInstance();

        errorXML = driver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));
    }
}
