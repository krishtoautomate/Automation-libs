package other.testcases;

import com.Utilities.ITestBase;
import com.base.TestBase;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Test
public class DataOFF extends TestBase implements ITestBase {


  @SuppressWarnings("unchecked")
  @Parameters({"platForm"})
  public void Data_OFF(@Optional String platForm) {

    if ("ios".equalsIgnoreCase(platForm)) {
      try {
        // Turn-OFF wifi
        driver.activateApp("com.apple.shortcuts");

        driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Data OFF']")).click();
        log.info("Data OFF");

        // Restart app
        driver.resetApp();

        log.info("App Restarted");
      } catch (Exception e) {
        // ignore
      }
    }


  }
}
