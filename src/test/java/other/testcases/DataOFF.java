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

@Test
public class DataOFF extends TestBase implements ITestBase {


  @SuppressWarnings("unchecked")
  @Parameters({"platForm"})
  public void Data_OFF(@Optional String platForm) {

    MobileActions mobileActions = new MobileActions(driver, test);

    if (isIos) {
      try {
        // Turn-OFF wifi
        mobileActions.activateApp("com.apple.shortcuts");

        driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Data OFF']")).click();
        log.info("Data OFF");

        // Restart app
        mobileActions.resetApp();

        log.info("App Restarted");
      } catch (Exception e) {
        // ignore
      }
    }


  }
}
