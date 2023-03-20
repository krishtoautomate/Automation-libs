package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenShotManagerWeb;
import com.Utilities.Utilities;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import other.pages.PlayStoreApp;


public class MobileBrowserTest extends TestBase implements ITestBase {

  String className = this.getClass().getSimpleName();

  @Test
  @Parameters({"udid"})
  public void Mobile_Browser_Test(@Optional String udid) {

    test.getModel().setName(String.format("%s - %s", className, udid));

    try {
      driver.get("https://mybell.bell.ca/Login");
    } catch (Exception e) {
      test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManagerWeb.getScreenshot()).build());
    }

    sleep(10);

    test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotManagerWeb.getScreenshot()).build());


    String errorXML = driver.getPageSource();

//        System.out.println("SessionId : " + ((RemoteWebDriver) driver).getSessionId());

    test.info(MarkupHelper.createCodeBlock(errorXML));


  }
}
