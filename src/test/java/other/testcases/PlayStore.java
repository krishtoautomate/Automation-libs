package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.Utilities;
import com.aventstack.extentreports.Status;
import com.base.TestBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import other.pages.PlayStoreApp;


public class PlayStore extends TestBase implements ITestBase {

  String className = this.getClass().getSimpleName();

  @Test
  @Parameters({"udid"})
  public void Playstore_Update_Script(@Optional String udid) {

    test.getModel().setName(String.format("%s - %s", className, udid));

    Utilities utils = new Utilities(driver, test);
    PlayStoreApp playstoreapp = new PlayStoreApp(driver, test);

    sleep(2);

    // 1.0 - Click navigate button
    playstoreapp.get_accountLogo().click();
    utils.logmessage(Status.PASS, "Right Account logo Button clicked");

    sleep(2);

    if (playstoreapp.verify_manageAppsAndDevice_btn()) {
      playstoreapp.get_manageAppsAndDevice_btn().click();
      utils.logmessage(Status.PASS, "'Manage apps and device' link clicked");
    }

//    sleep(2);

    // 3.0 -
//    if (playstoreapp.verify_updates_refresh_btn()) {
//      playstoreapp.get_updates_refresh_btn().click();
//      utils.logmessage(Status.PASS, "'updates refresh Button' is clicked");
//    }

    // 4.0 - Click Update All button
    if (playstoreapp.verify_updateAll_btn()) {
      playstoreapp.get_updateAll_btn().click();
      sleep(3);
      utils.logmessage(Status.PASS, "'UPDATE ALL' button clicked");
      sleep(120);
    }


  }
}
