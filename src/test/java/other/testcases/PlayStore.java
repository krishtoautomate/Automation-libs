package other.testcases;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.Utilities.ITestBase;
import com.Utilities.Utilities;
import com.aventstack.extentreports.Status;
import com.base.TestBase;

import other.pages.PlayStoreApp;


public class PlayStore extends TestBase implements ITestBase {

  String className = this.getClass().getSimpleName();

  @Test
  @Parameters({"udid"})
  public void PlaystoreUpdateScript(@Optional String udid) {

    test.getModel().setName(String.format("%s - %s", className, udid));

    Utilities utils = new Utilities(driver, log, test);
    PlayStoreApp playstoreapp = new PlayStoreApp(driver, log, test);

    sleep(2);

    // 1.0 - Click navigate button
    playstoreapp.get_accountLogo().click();
    utils.logmessage(Status.PASS, "Right Account logo Button clicked");

    sleep(2);

    // 2.0 - Click 'My apps & games' link
    playstoreapp.get_myApps_and_games_btn().click();
    utils.logmessage(Status.PASS, "'My apps & games' link clicked");

    sleep(2);

    // 3.0 -
    if (isElementDisplayed(playstoreapp.get_updates_refresh_btn())) {
      playstoreapp.get_updates_refresh_btn().click();
      utils.logmessage(Status.PASS, "'updates refresh Button' is clicked");
    }

    // 4.0 - Click Update All button
    if (isElementDisplayed(playstoreapp.get_updateAll_btn())) {
      playstoreapp.get_updateAll_btn().click();
      sleep(3);
      utils.logmessage(Status.PASS, "'UPDATE ALL' button clicked");
    } else {
      utils.logmessage(Status.PASS, "'UPDATE ALL' button is NOT FOUND");
    }


  }
}
