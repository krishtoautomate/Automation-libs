package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenshotManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseDeviceWeb;
import org.testng.annotations.Test;


public class MobileBrowserTest extends TestBaseDeviceWeb implements ITestBase {

  String className = this.getClass().getSimpleName();

  @Test
  public void Mobile_Browser_Test() {

    String udid = driver.getCapabilities().getCapability("udid").toString();

    test.getModel().setName(String.format("%s - %s", className, udid));

    driver.get("https://mybell.bell.ca/Login");

    sleep(10);

    driver.navigate().to("https://mybell.bell.ca/Login");

    test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());

    String errorXML = driver.getPageSource();

    test.info(MarkupHelper.createCodeBlock(errorXML));


  }
}
