package other.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import com.Utilities.BaseObjs;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.MobileBy;

public class TestFlightApp extends BaseObjs<TestFlightApp> {

  // TestFlight
  By continue_btn = By.xpath("//XCUIElementTypeButton[@name='Continue Button']");
  By tryAgain_btn = By.xpath("//XCUIElementTypeButton[@name='Try Again']");
  By update_later_btn = By.xpath("//XCUIElementTypeButton[@name='Later']");
  By remindMeLater_btn = By.xpath("//XCUIElementTypeButton[@name='Remind Me Later']");

  By all_btns = MobileBy.xpath("//XCUIElementTypeButton[@name='UPDATE' or @name='INSTALL']");
  By apps_h1 = MobileBy.iOSNsPredicateString("label == 'Apps' AND visible =1");
  By open_btn = By.xpath("//XCUIElementTypeButton[@name='OPEN']");
  By update_btn = MobileBy
      .iOSNsPredicateString("type == 'XCUIElementTypeButton' AND name == 'UPDATE' AND visible =1");
  By install_btn = By.xpath("//XCUIElementTypeButton[@name='INSTALL']");

  // Apple ID pop-up
  By alert = By.xpath("//XCUIElementTypeAlert");
  By settings_btn = By.xpath("//XCUIElementTypeButton[@name='Settings']");
  By notNow_btn = By.xpath("//XCUIElementTypeButton[@name='Not Now']");
  By password_inputField = By.xpath("//XCUIElementTypeSecureTextField");
  By signIn_btn = By.xpath("//XCUIElementTypeButton[@name='Sign In' or @name='OK']");

  public TestFlightApp(AppiumDriver driver, Logger log, ExtentTest test) {
    super(driver, log, test);
  }

  public boolean verify_continue_btn() {
    return verify_Element(continue_btn);
  }

  public WebElement get_continue_btn() {
    return get_Element(continue_btn, "continue' button");
  }

  public boolean verify_tryAgain_btn() {
    return verify_Element(tryAgain_btn);
  }

  public WebElement get_tryAgain_btn() {
    return get_Element(update_later_btn, "update later button");
  }

  By apps_back_btn = MobileBy.iOSNsPredicateString(
      "type=='XCUIElementTypeButton' AND name=='BackButton' AND label=='Apps'");

  public boolean verify_apps_back_btn() {
    return verify_Element(apps_back_btn);
  }

  public MobileElement get_apps_back_btn() {
    return get_Element(apps_back_btn, "Back button");
  }

  public MobileElement get_apps_h1() {
    return get_Element(apps_h1, "'Apps' title text");
  }

  public List<MobileElement> get_all_btns() {
    return get_Elements(all_btns, "All buttons");
  }

  public boolean verify_all_btns() {
    return verify_Element(all_btns);
  }
  public List<WebElement> get_open_btn() {
    List<WebElement> eles = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(open_btn));
      eles = driver.findElements(open_btn);
    } catch (Exception e) {
      // ignore
    }
    return eles;
  }

  public List<WebElement> get_update_btn() {
    List<WebElement> eles = null;
    mobileActions.scrollDowntoFind(update_btn, 2);
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(update_btn));
      eles = driver.findElements(update_btn);
    } catch (Exception e) {
      // ignore
    }
    return eles;
  }

  // public List<WebElement> get_install_btn() {
  // List<WebElement> eles = null;
  // mobileActions.scrollDowntoFind(install_btn, 2);
  // try {
  // wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(install_btn));
  // eles = driver.findElements(install_btn);
  // } catch (Exception e) {
  // // ignore
  // }
  // return eles;
  // }

  public WebElement get_alert() {
    WebElement ele = null;
    try {
      waitForVisibilityOf(alert, 5);
      ele = driver.findElement(alert);
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  public WebElement get_notNow_btn() {
    WebElement ele = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(notNow_btn));
      ele = driver.findElement(notNow_btn);
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  public WebElement get_settings_btn() {
    WebElement ele = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(settings_btn));
      ele = driver.findElement(settings_btn);
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  public WebElement get_password_inputField() {
    WebElement ele = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(password_inputField));
      ele = driver.findElement(password_inputField);
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  public WebElement get_signIn_btn() {
    WebElement ele = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(signIn_btn));
      ele = driver.findElement(signIn_btn);
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }
}
