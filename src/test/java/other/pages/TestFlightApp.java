package other.pages;

import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.Utilities.BaseObjs;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.MobileBy;

public class TestFlightApp extends BaseObjs<TestFlightApp> {

  // TestFlight
  By continue_btn = By.xpath("//XCUIElementTypeButton[@name='Continue Button']");
  By tryAgain_btn = By.xpath("//XCUIElementTypeButton[@name='Try Again']");
  By update_later_btn = By.xpath("//XCUIElementTypeButton[@name='Later']");
  By remindMeLater_btn = By.xpath("//XCUIElementTypeButton[@name='Remind Me Later']");

  By all_btns = MobileBy.xpath("//XCUIElementTypeButton[@name='UPDATE']");
  By apps_h1 = MobileBy.iOSNsPredicateString("label == 'Apps' AND visible =1");
  By open_btn = By.xpath("//XCUIElementTypeButton[@name='OPEN']");
  By update_btn = By.xpath("//XCUIElementTypeButton[@name='UPDATE']");
  By install_btn = By.xpath("//XCUIElementTypeButton[@name='INSTALL']");

  // Apple ID pop-up
  By alert = By.xpath("//XCUIElementTypeAlert");
  By settings_btn = By.xpath("//XCUIElementTypeButton[@name='Settings']");
  By notNow_btn = By.xpath("//XCUIElementTypeButton[@name='Not Now']");
  By password_inputField = By.xpath("//XCUIElementTypeSecureTextField");
  By signIn_btn = By.xpath("//XCUIElementTypeButton[@name='Sign In' or @name='OK']");

  public TestFlightApp(WebDriver driver, Logger log, ExtentTest test) {
    super(driver, log, test);
  }

  public WebElement get_continue_btn() {
    WebElement ele = null;
    try {
      waitForVisibilityOf(continue_btn, 5);
      ele = driver.findElement(continue_btn);
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  public WebElement get_tryAgain_btn() {
    WebElement ele = null;
    try {
      waitForVisibilityOf(tryAgain_btn, 5);
      ele = driver.findElement(tryAgain_btn);
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  public WebElement get_update_later_btn() {
    WebElement ele = null;
    try {
      waitForVisibilityOf(update_later_btn, 5);
      ele = driver.findElement(update_later_btn);
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  @Override
  public WebElement get_remindMeLater_btn() {
    WebElement ele = null;
    try {
      waitForVisibilityOf(remindMeLater_btn, 5);
      ele = driver.findElement(remindMeLater_btn);
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  By apps_back_btn = MobileBy.iOSNsPredicateString(
      "type=='XCUIElementTypeButton' AND name=='BackButton' AND label=='Apps'");

  public WebElement verify_apps_back_btn() {
    return verify_Element(apps_back_btn);
  }

  public WebElement get_apps_h1() {
    return get_Element(apps_h1, "'Apps' title text");
  }

  public List<WebElement> get_all_btns() {
    List<WebElement> eles = null;
    mobileActions.scrollDowntoFind(all_btns, 2);
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(all_btns));
      eles = driver.findElements(all_btns);
    } catch (Exception e) {
      // ignore
    }
    return eles;
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

  // public List<WebElement> get_update_btn() {
  // List<WebElement> eles = null;
  // mobileActions.scrollDowntoFind(update_btn, 2);
  // try {
  // wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(update_btn));
  // eles = driver.findElements(update_btn);
  // } catch (Exception e) {
  // // ignore
  // }
  // return eles;
  // }

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
