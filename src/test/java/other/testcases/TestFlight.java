package other.testcases;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.Utilities.ITestBase;
import com.Utilities.Utilities;
import com.aventstack.extentreports.Status;
import com.base.TestBase;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import other.pages.TestFlightApp;

public class TestFlight extends TestBase implements ITestBase {

  String className = this.getClass().getSimpleName();

  @SuppressWarnings("unchecked")
  @Test
  @Parameters({"udid"})
  public void TestFlightUpdateScript(@Optional String udid) {

    test.getModel().setName(String.format("%s - %s", className, udid));

    Utilities utils = new Utilities(driver, log, test);
    TestFlightApp testFlightApp = new TestFlightApp(driver, log, test);

    utils.dismissAlert();

    if (!((AppiumDriver<MobileElement>) driver).getCapabilities().getCapability("bundleId")
        .toString().contains("com.apple.TestFlight"))
      ((AppiumDriver<MobileElement>) driver).activateApp("com.apple.TestFlight");

    if (isElementDisplayed(testFlightApp.get_tryAgain_btn())) {
      testFlightApp.get_tryAgain_btn().click();
      utils.logmessage(Status.INFO, "'Try Again' Button - is Clicked");
    }

    if (isElementDisplayed(testFlightApp.get_continue_btn())) {
      testFlightApp.get_continue_btn().click();
      utils.logmessage(Status.INFO, "Continue Button - is Clicked");
    }

    // for ipads
    if (isElementDisplayed(testFlightApp.verify_apps_back_btn())) {
      testFlightApp.verify_apps_back_btn().click();
      utils.logmessage(Status.INFO, "App back Button - is Clicked");
    }

    // Verify title
    utils.AssertContains(testFlightApp.get_apps_h1().getText(), "Apps");

    List<WebElement> all_btns = testFlightApp.get_all_btns();

    try {
      log.info("Total 'UPDATE' or 'INSTALL' buttons : " + all_btns.size());

      for (int i = 0; i < all_btns.size(); i++) {
        String button = all_btns.get(i).getText();
        all_btns.get(i).click();
        utils.logmessage(Status.INFO, button + "- button clicked");
      }
      sleep(5);
    } catch (Exception e) {
      log.info("No 'UPDATE' or 'INSTALL' buttons found");
    }

  }
}
