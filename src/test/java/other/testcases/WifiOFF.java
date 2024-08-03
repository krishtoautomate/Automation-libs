package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.MobileActions;
import com.base.Log;
import com.base.TestBase;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class WifiOFF extends TestBase implements ITestBase {


  @SuppressWarnings("unchecked")
  @Test
  @Parameters({"platForm"})
  public void Wifi_off(@Optional String platForm) {

    MobileActions mobileActions = new MobileActions(driver, test);

    if ("ios".equalsIgnoreCase(platForm)) {
      try {
        // Turn-OFF wifi
        mobileActions.activateApp("com.apple.shortcuts");

        driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Wifi OFF']")).click();
        log.info("WIFI OFF");

        // Restart app
        mobileActions.resetApp();

        log.info("App Restarted");
      } catch (Exception e) {
        // ignore
      }
    }


  }
}
