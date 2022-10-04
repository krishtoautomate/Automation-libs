package other.testcases;

import com.Utilities.ITestBase;
import com.base.Log;
import com.base.TestBase;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class WifiON extends TestBase implements ITestBase {


  @SuppressWarnings("unchecked")
  @Test
  @Parameters({"platForm"})
  public void Wifi_ON(@Optional String platForm) {

    if ("ios".equalsIgnoreCase(platForm)) {
      try {
        // Turn-OFF WIFI
        driver.activateApp("com.apple.shortcuts");

        driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Wifi ON']")).click();
        log.info("WIFI ON");

        // Restart app
        driver.resetApp();

        log.info("App Restarted");
      } catch (Exception e) {
        // ignore
      }
    }


  }
}
