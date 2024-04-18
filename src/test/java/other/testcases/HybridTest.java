package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenshotManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.DriverManager;
import com.base.TestBaseHybrid;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import other.pages.PlayStoreApp;

public class HybridTest extends TestBaseHybrid implements ITestBase {

    @Test
    public void Hybrid_Test(){

        WebDriver webDriver = DriverManager.getWebDriverInstance();

        webDriver.get("http://bqatautomation.bell.corp.bce.ca:8080/");

        sleep(5);

        test.pass("web-page loaded : "+webDriver.getCurrentUrl(),
                MediaEntityBuilder.createScreenCaptureFromPath(
                        new ScreenshotManager(webDriver).getScreenshot()).build());

        AppiumDriver appiumDriver = DriverManager.getAppiumDriverInstance();

        PlayStoreApp playstoreapp = new PlayStoreApp(appiumDriver, test);


        sleep(5);

        String errorXML = appiumDriver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));


        // 1.0 - Click navigate button
        playstoreapp.get_accountLogo().click();
        playstoreapp.logmessage(Status.PASS, "Right Account logo Button clicked");

        sleep(5);

        errorXML = appiumDriver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));
    }
}
