package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenshotManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseDeviceWeb;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import org.testng.annotations.Test;


public class MobileSubIdTest extends TestBaseDeviceWeb implements ITestBase {

  @Test
  public void Mobile_Browser_Test() {

    ((AndroidDriver) driver).setConnection(new ConnectionStateBuilder().withWiFiDisabled().withDataEnabled().build());

    sleep(5);

//    driver.get("https://bisp.bwanet.ca:4443/subid;appId=MBMSelfServe");
//    driver.get("https://bisp.bwanet.ca:4443/subid;appId=LMSelfServe");
    driver.get("https://bisp.bwanet.ca:4443/subid;appId=MVMSelfServe");

    sleep(5);

    test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());

    String errorXML = driver.getPageSource();

    test.info(MarkupHelper.createCodeBlock(errorXML));

    ((AndroidDriver) driver).setConnection(new ConnectionStateBuilder().withWiFiEnabled().withDataEnabled().build());

  }
}
