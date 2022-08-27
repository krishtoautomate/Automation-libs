package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.Utilities;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBase;
import io.appium.java_client.MobileElement;
import java.util.List;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
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

    // accept open prompt
    try {
      driver.switchTo().alert().dismiss();
      utils.logmessage(Status.PASS, "dismiss alert");
    } catch (Exception ign) {
      // ignore
    }

    driver.resetApp();

    utils.dismissAlert();

    if (!driver.getCapabilities().getCapability("bundleId")
        .toString().contains("com.apple.TestFlight")) {
      driver.activateApp("com.apple.TestFlight");
    }

    if (testFlightApp.verify_tryAgain_btn()) {
      testFlightApp.get_tryAgain_btn().click();
      utils.logmessage(Status.PASS, "'Try Again' Button - is Clicked");
    }

    if (testFlightApp.verify_continue_btn()) {
      testFlightApp.get_continue_btn().click();
      utils.logmessage(Status.PASS, "Continue Button - is Clicked");
    }

    // for ipads
    if (testFlightApp.verify_apps_back_btn()) {

      utils.getPageSource();

      if (testFlightApp.verify_all_btns()) {
        testFlightApp.get_all_btns().get(0).click();
        utils.logmessage(Status.PASS,"UPDATE button clicked");
      }

      testFlightApp.get_apps_back_btn().click();
      utils.logmessage(Status.PASS, "App back Button - is Clicked");
    }

    sleep(4);
    // Verify title
    utils.AssertContains(testFlightApp.get_apps_h1().getText(), "Apps");

    utils.getPageSource();
    if (testFlightApp.verify_all_btns()) {

      List<MobileElement> all_btns = testFlightApp.get_all_btns();

      try {
        utils.logmessage(Status.PASS, "Total 'UPDATE' or 'INSTALL' buttons : " + all_btns.size());

        for (int i = 0; i < all_btns.size(); i++) {
          String button = all_btns.get(i).getText();

          utils.getPageSource();

          all_btns.get(i).click();
          utils.logmessage(Status.PASS, button + "- button clicked");
        }
        sleep(10);
      } catch (Exception e) {
        log.info("No 'UPDATE' or 'INSTALL' buttons found");
      }
      utils.getPageSource();

      all_btns = testFlightApp.get_all_btns();
      utils.getPageSource();
      try {
        utils.logmessage(Status.PASS, "Total 'UPDATE' or 'INSTALL' buttons : " + all_btns.size());

        for (int i = 0; i < all_btns.size(); i++) {
          String button = all_btns.get(i).getText();

          utils.getPageSource();

          all_btns.get(i).click();
          utils.logmessage(Status.PASS, button + "- button clicked");
        }
        sleep(10);
      } catch (Exception e) {
        log.info("No 'UPDATE' or 'INSTALL' buttons found");
      }
      utils.getPageSource();

      // for ipads
      if (testFlightApp.verify_apps_back_btn()) {
        testFlightApp.get_apps_back_btn().click();
        utils.logmessage(Status.PASS, "App back Button - is Clicked");
      }

      utils.getPageSource();
    }
  }
}
