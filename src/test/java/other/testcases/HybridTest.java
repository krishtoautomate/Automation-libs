package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenShotManager;
import com.Utilities.ScreenShotManagerWeb;
import com.Utilities.Utilities;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseHybrid;
import com.base.TestBaseWeb;
import org.testng.annotations.Test;
import other.pages.PlayStoreApp;

public class HybridTest extends TestBaseHybrid implements ITestBase {

    @Test
    public void Hybrid_Test(){
        webDriver.get("http://bqatautomation.bell.corp.bce.ca:8080/");

        sleep(5);

        test.pass("web-page loaded : "+webDriver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManagerWeb.getScreenshot()).build());

        Utilities utils = new Utilities(driver, test);
        PlayStoreApp playstoreapp = new PlayStoreApp(driver, test);

        sleep(5);

        String errorXML = webDriver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));

        // 1.0 - Click navigate button
        playstoreapp.get_accountLogo().click();
        utils.logmessage(Status.PASS, "Right Account logo Button clicked");

        sleep(5);

        errorXML = driver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));
    }
}
