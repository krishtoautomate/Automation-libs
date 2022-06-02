package other.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import com.Utilities.BaseObjs;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.MobileBy;


public class PlayStoreApp extends BaseObjs<PlayStoreApp> {

  By accountLogo = By.xpath(
      "(//android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ImageView[contains(@resource-id, '0_resource_name_obfuscated')])[1]");

  By myApps_and_games_btn = MobileBy.AccessibilityId("My apps & games");

  By manageAppsAndDevice_btn = MobileBy
      .xpath("(//android.widget.FrameLayout/android.view.ViewGroup/android.widget.TextView)[1]");

  By updates_refresh_btn = MobileBy.AccessibilityId("Check for updates");
  By updateAll_btn = By.xpath(
      "//android.widget.Button[contains(@text,'UPDATE ALL') or contains(@text,'Update all')]");


  public PlayStoreApp(AppiumDriver driver, Logger log, ExtentTest test) {
    super(driver, log, test);
  }

  public WebElement get_accountLogo() {
    return get_Element(accountLogo, "Account logo(right cornor) button");
  }

  public WebElement verify_myApps_and_games_btn() {
    return verify_Element(myApps_and_games_btn);
  }

  public WebElement get_myApps_and_games_btn() {
    return get_Element(myApps_and_games_btn, "'My apps & games' button");
  }

  public WebElement verify_manageAppsAndDevice_btn() {
    return verify_Element(manageAppsAndDevice_btn);
  }

  public WebElement get_manageAppsAndDevice_btn() {
    return get_Element(manageAppsAndDevice_btn, "'Manage apps and device' button");
  }

  public WebElement get_updates_refresh_btn() {
    return verify_Element(updates_refresh_btn);
  }

  public WebElement verify_updateAll_btn() {
    return verify_Element(updateAll_btn);
  }

  public WebElement get_updateAll_btn() {
    return get_Element(updateAll_btn, "'Update all' button");
  }

}
