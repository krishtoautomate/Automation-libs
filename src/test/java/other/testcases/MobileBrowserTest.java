package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.ScreenshotManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseDeviceWeb;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;


public class MobileBrowserTest extends TestBaseDeviceWeb implements ITestBase {

  String className = this.getClass().getSimpleName();

  @Test
  public void Mobile_Browser_Test() {

    String udid = driver.getCapabilities().getCapability("udid").toString();

    test.getModel().setName(String.format("%s - %s", className, udid));

    driver.get("https://mybell.bell.ca/Login");
    log("url launched :  https://mybell.bell.ca/Login");

    sleep(10);

    driver.navigate().to("https://www.virginplus.ca/en/home/index.html");
    log("url launched : https://www.virginplus.ca/en/home/index.html");

    test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(new ScreenshotManager(driver).getScreenshot()).build());

    String errorXML = driver.getPageSource();

    test.info(MarkupHelper.createCodeBlock(errorXML));

  }

//  @Step("{message}")
  public void log(final String message) {
    Allure.step(message, (step) -> {
      Allure.addAttachment("screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
//      screenshot();
    });
  }

  @Attachment(value = "Screenshot", type = "image/png")
  public byte[] screenshot() {
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
  }
}
