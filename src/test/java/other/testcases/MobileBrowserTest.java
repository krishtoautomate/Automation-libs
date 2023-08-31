package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenShotManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBase;
import com.base.TestBaseDeviceWeb;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class MobileBrowserTest extends TestBaseDeviceWeb implements ITestBase {

  String className = this.getClass().getSimpleName();

  @Test
  public void Mobile_Browser_Test() {

    String udid = driver.getCapabilities().getCapability("udid").toString();

    test.getModel().setName(String.format("%s - %s", className, udid));

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


  }
}
