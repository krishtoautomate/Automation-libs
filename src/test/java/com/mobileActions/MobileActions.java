package com.mobileActions;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.testng.Assert;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.offset.ElementOption;

public class MobileActions {

  private AppiumDriver driver;
  private WebDriverWait wait;
  private Logger log;
  private ExtentTest test;

  public MobileActions(AppiumDriver driver, Logger log, ExtentTest test) {
    this.driver = driver;
    this.log = log;
    this.test = test;
    wait = new WebDriverWait(driver, 30);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  /**
   * TRUE - If element is displayed
   */
  public boolean isElementDisplayed(WebElement element) {
    Boolean isDisplayed = false;
    try {
      isDisplayed = element.isDisplayed() ? true : false;
    } catch (Exception e) {
      // ignore
    }
    return isDisplayed;
  }

  /**
   * type each char of String in Android text-box CommandExecutionHelper.execute(this,
   * pressKeyCodeCommand(key)); new TouchActions(driver).sendKeys(keys)
   */
  @SuppressWarnings("rawtypes")
  public void send_keys(String keys) {

    boolean isAndroid = driver instanceof AndroidDriver;

    if (isAndroid) {
      for (int i = 0; i < keys.length(); i++) {
        char c = keys.charAt(i);
        String s = new StringBuilder().append(c).toString();

        if (s.equals("0")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_0));
        } else if (s.equals("1")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_1));
        } else if (s.equals("2")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_2));
        } else if (s.equals("3")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_3));
        } else if (s.equals("4")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_4));
        } else if (s.equals("5")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_5));
        } else if (s.equals("6")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_6));
        } else if (s.equals("7")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_7));
        } else if (s.equals("8")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_8));
        } else if (s.equals("9")) {
          ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DIGIT_9));
        } else {
          test.log(Status.FAIL, "SendKeys Failed!");
          log.error("SendKeys Failed!");
          Assert.fail("Send Keys failed");
        }
      }
    } else {
      new TouchActions(driver).sendKeys(keys);
    }
  }

  @SuppressWarnings("unchecked")
  public void activateApp(String platForm, String bundleId) {
    if ("ios".equalsIgnoreCase(platForm))
      ((AppiumDriver<MobileElement>) driver).activateApp(bundleId);
  }

  @SuppressWarnings("unchecked")
  public void terminateApp(String platForm, String bundleId) {
    if ("ios".equalsIgnoreCase(platForm))
      ((AppiumDriver<MobileElement>) driver).terminateApp(bundleId);
  }

  @SuppressWarnings("unchecked")
  public void resetApp(String platForm, String bundleId) {
    if ("ios".equalsIgnoreCase(platForm)) {
      ((AppiumDriver<MobileElement>) driver).activateApp(bundleId);
      ((AppiumDriver<MobileElement>) driver).resetApp();
    }
  }

  /*
   * Close ios Keyboard
   */
  // ((HasOnScreenKeyboard) driver).isKeyboardShown();
  // ((AppiumDriver<MobileElement>) driver).hideKeyboard();
  public WebElement get_done_btn() {
    By done_btn = By.xpath("//XCUIElementTypeButton[@name='Done']");
    WebElement ele = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(done_btn));
      ele = driver.findElement(done_btn);
    } catch (Exception e) {
      log.warn("'DONE' button - NotFound!!!");
    }
    return ele;
  }

  /**
   * close keyboard in Android
   */
  @SuppressWarnings("unchecked")
  public void hidekeyboard(String platForm) {
    try {
      if ("ios".equalsIgnoreCase(platForm)) {
        get_done_btn().click();
      } else {
        ((AppiumDriver<MobileElement>) driver).hideKeyboard();
      }
    } catch (Exception e) {
      log.warn("Failed to close keyboard!!!");
    }
  }

  /**
   * scroll to end of the page Ios by direction: down, up, left, right
   */
  public void Scrollios(String direction, WebElement element) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    HashMap<String, String> scrollObject = new HashMap<String, String>();

    scrollObject.clear();
    scrollObject.put("direction", direction);
    for (int i = 0; i < 5; i++) {

      if (element.getAttribute("visible").equals("true")) {
        break;
      }
      js.executeScript("mobile: scroll", scrollObject);
    }
  }

  @SuppressWarnings("rawtypes")
  public TouchAction touchAction() {
    return new TouchAction((PerformsTouchActions) driver);
  }

  public void tapByCoordinates(int X, int Y) {
    touchAction().tap(ElementOption.point(X, Y)).perform();
  }

  /*
   * provide Element Center x and y coordinates
   */
  public void tapByPoint(Point point) {
    touchAction().tap(ElementOption.point(point)).perform();
  }

  /*
   * Tap on center of element
   */
  public void tapByElement(WebElement element) {

    Point point = ((MobileElement) element).getCenter();

    touchAction().tap(ElementOption.point(point)).perform();
  }

  /**
   * Thread sleep for Certain Seconds
   */
  public void sleep(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      log.info("wait function failed!");
    }
  }

  protected boolean isElementDisplayed(By by) {
    try {
      return driver.findElement(by).isDisplayed();
    } catch (StaleElementReferenceException e) {
      // ignore
    } catch (Exception e) {
      // ignore
    }
    return false;
  }

  /**
   * scroll to find with gester control
   */
  public void scrollDowntoFind(By Locator) {
    scrollDowntoFind(Locator, 5);
  }

  public void scrollDowntoFind(By Locator, int xScrolls) {

    Dimension size = driver.manage().window().getSize();

    int y_start = (int) (size.height * 0.8);
    int y_end = (int) (size.height * 0.4);
    int x = size.width / 2;

    for (int i = 0; i < xScrolls; i++) {
      try {
        sleep(2);
        if (isElementDisplayed(driver.findElement(Locator))) {
          break;
        } else {
          touchAction().longPress(ElementOption.point(x, y_start))
              .moveTo(ElementOption.point(x, y_end)).release().perform();
        }
      } catch (Exception e) {
        touchAction().longPress(ElementOption.point(x, y_start))
            .moveTo(ElementOption.point(x, y_end)).release().perform();
      }
    }
  }

  public void scrollUptoFind(By Locator) {
    Dimension size = driver.manage().window().getSize();

    int y_start = (int) (size.height * 0.4);
    int y_end = (int) (size.height * 0.8);

    int x = size.width / 2;

    for (int i = 0; i < 5; i++) {
      try {
        sleep(2);
        if (isElementDisplayed(driver.findElement(Locator)) == true) {
          log.info(driver.findElement(Locator).getText() + " - isDisplayed");
          break;
        } else {
          touchAction().longPress(ElementOption.point(x, y_start))
              .moveTo(ElementOption.point(x, y_end)).release().perform();
        }
      } catch (Exception e) {
        log.warn("scroll failed!!!");
      }
    }
  }

  /**
   * scroll to find with gester control
   */
  public void scrollDown1X() {
    Dimension size = driver.manage().window().getSize();

    int y_start = size.height / 2;
    int y_end = size.height / 4;
    int x = size.width / 2;

    try {
      touchAction().longPress(ElementOption.point(x, y_start)).moveTo(ElementOption.point(x, y_end))
          .release().perform();

    } catch (Exception e) {
      touchAction().longPress(ElementOption.point(x, y_start)).moveTo(ElementOption.point(x, y_end))
          .release().perform();
    }

  }

  /*
   * Scroll Function(UP, DOWN, LEFT, Right)
   */
  public void scroll(String direction) {

    Dimension size = driver.manage().window().getSize();

    if (direction == "UP") {

      int y_start = (int) (size.height * 0.8);
      int y_end = (int) (size.height * 0.4);
      int x = size.width / 2;

      touchAction().longPress(ElementOption.point(x, y_start)).moveTo(ElementOption.point(x, y_end))
          .release().perform();

    } else if (direction == "DOWN") {

      int y_start = (int) (size.height * 0.4);
      int y_end = (int) (size.height * 0.8);
      int x = size.width / 2;

      touchAction().longPress(ElementOption.point(x, y_start)).moveTo(ElementOption.point(x, y_end))
          .release().perform();
    } else if (direction == "LEFT") {
      //
    } else {
      //
    }
  }

  /**
   * scroll by coordinates
   */
  public void scrollbyCoords(int x_start, int x_end, int y_start, int y_end, int x_scrolls) {
    for (int i = 0; i < x_scrolls; i++) {
      try {
        sleep(2);

        touchAction().longPress(ElementOption.point(x_start, y_start))
            .moveTo(ElementOption.point(x_end, y_end)).release().perform();

      } catch (Exception e) {
        touchAction().longPress(ElementOption.point(x_start, y_start))
            .moveTo(ElementOption.point(x_end, y_end)).release().perform();
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void toggleWifiOFF(String platForm) {
    if ("ios".equalsIgnoreCase(platForm)) {
      try {
        // Turn-OFF wifi
        ((AppiumDriver<MobileElement>) driver).activateApp("com.apple.shortcuts");

        driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Wifi OFF']")).click();
        log.info("WIFI OFF");
        // Restart app
        ((AppiumDriver<MobileElement>) driver).resetApp();
        // .activateApp(
        // ((AppiumDriver<MobileElement>)
        // driver).getCapabilities().getCapability("bundleId").toString());

        log.info("App Restarted");
      } catch (Exception e) {
        // ignore
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void toggleWifiON() {

    boolean isAndroid = driver instanceof AndroidDriver;
    if (!isAndroid) {
      try {
        // Turn-ON wifi
        ((AppiumDriver<MobileElement>) driver).activateApp("com.apple.shortcuts");

        driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Wifi ON']")).click();
        log.info("WIFI OFF");

      } catch (Exception e) {
        // ignore
      }

      try {
        // Restart app
        ((AppiumDriver<MobileElement>) driver).activateApp(((AppiumDriver<MobileElement>) driver)
            .getCapabilities().getCapability("bundleId").toString());
        log.info("App Restarted");
      } catch (Exception e) {
        // ignore
      }
    }
  }

}
