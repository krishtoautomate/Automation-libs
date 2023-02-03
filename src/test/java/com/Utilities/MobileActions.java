package com.Utilities;

import com.ReportManager.ExtentTestManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.base.Log;
import com.google.common.collect.ImmutableList;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.ElementOption;
import java.time.Duration;
import java.util.HashMap;

import io.appium.java_client.touch.offset.PointOption;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.testng.Assert;

public class MobileActions implements ITestBase {

  private AppiumDriver driver;
  private WebDriver webDriver;
  private Logger log;
  private ExtentTest test;

  boolean isAndroid = false;
  boolean isIOS = false;

  public MobileActions(AppiumDriver driver,ExtentTest test) {
    this.driver = driver;
    this.log = Logger.getLogger(this.getClass().getName());
    this.isAndroid = driver instanceof AndroidDriver;
    this.isIOS = driver instanceof IOSDriver;
    this.test = test;
  }

  public MobileActions(WebDriver driver,ExtentTest test) {
    this.webDriver = driver;
    this.log = Logger.getLogger(this.getClass().getName());
    this.test = test;
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
   * type each char of String in Android text-box CommandExecutionHelper.execute(this,
   * pressKeyCodeCommand(key));
   */
  @SuppressWarnings("rawtypes")
  public void sendKeys(String keys) {
    Actions a = new Actions(driver);
    a.sendKeys(keys);
    a.perform();
  }

  public void swipe(Direction dir, int... xSwipes) {
    // Animation default time:
    //  - Android: 300 ms
    //  - iOS: 200 ms
    // final value depends on your app and could be greater

    int xTimes = xSwipes.length > 0 ? xSwipes[0] : 1;

//        final int PRESS_TIME = 500; // ms

    int edgeBorder = 90; // better avoid edges
    // init screen variables
    Dimension dims = driver.manage().window().getSize();

    PointOption pointOptionEnd = PointOption.point(dims.width / 4, edgeBorder);

    // init start point = center of screen
    PointOption pointOptionStart = PointOption.point(dims.width / 2, dims.height / 2);


    switch (dir) {
      case DOWN: // center of footer
        pointOptionEnd = PointOption.point(dims.width / 2, dims.height - edgeBorder);
        break;
      case UP: // center of header
        pointOptionEnd = PointOption.point(dims.width / 2, dims.height / 4);
        break;
      case LEFT: // center of left side
        pointOptionEnd = PointOption.point(edgeBorder, dims.height / 2);
        break;
      case RIGHT: // center of right side
        pointOptionEnd = PointOption.point(dims.width - edgeBorder, dims.height / 2);
        break;
      default:
        log.error("swipe() - dir: '" + dir + "' NOT supported");
    }

    // execute swipe using TouchAction
    for (int x = 0; x < xTimes; x++) {
      try {
        new TouchAction((PerformsTouchActions) driver)
                .longPress(pointOptionStart)
                // a bit more reliable when we add small wait
                //                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
                .moveTo(pointOptionEnd)
                .release()
                .perform();
      } catch (Exception e) {
        log.error("swipeScreen(): TouchAction FAILED");
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void activateApp(String bundleId) {
    if (isIOS) {
      ((IOSDriver)driver).activateApp(bundleId);
//      HashMap<String, String> args = new HashMap<>();
//      args.put("bundleId", bundleId);
//      driver.executeScript("mobile:activateApp", args);
    }
  }

  public String getIOSActiveAppInfo() {
    String activeApp = "";
    try {
      String jsonResponse = driver.executeScript("mobile:activeAppInfo").toString();
      activeApp = (jsonResponse.split("bundleId=")[1]).replaceAll("}", "");
    } catch (Exception e) {
      //ignore
    }
    log.info("Active-app :" + activeApp);
    return activeApp;
  }

  @SuppressWarnings("unchecked")
  public void terminateApp(String bundleId) {
    boolean isAndroid = driver instanceof AndroidDriver;
    if (!isAndroid) {
//      driver.terminateApp(bundleId);
      HashMap<String, String> args = new HashMap<>();
      args.put("bundleId", bundleId);
      driver.executeScript("mobile:terminateApp", args);
    }
  }

  @SuppressWarnings("unchecked")
  public void resetApp() {
    String bundleId = driver.getCapabilities().getCapability("bundleId").toString();
    if (isIOS) {
      terminateApp(bundleId);
      sleep(2);
      activateApp(bundleId);
    }
  }

  @SuppressWarnings("unchecked")
  public void activateAndroidDeepLink(String deepLinkUrl) {
    String packageId = driver.getCapabilities()
        .getCapability("appPackage").toString();
    HashMap<String, String> event = new HashMap<>();
    event.put("url", deepLinkUrl);
    event.put("package", packageId);
    driver.executeScript("mobile:deepLink", event);
  }

  @SuppressWarnings("unchecked")
  public void activateSafariApp() {
    activateApp("com.apple.mobilesafari");
  }

  @SuppressWarnings("unchecked")
  public void terminateSafariApp() {
    terminateApp("com.apple.mobilesafari");
  }


  @SuppressWarnings("unchecked")
  public void launchUrl(String platForm, String url) {
    driver.get(url);
  }

  /*
   * Get Home Screen on Android
   */
  @SuppressWarnings("unchecked")
  public void get_androidHomeScreen(String platform) {
    if ("Android".equalsIgnoreCase(platform)) {
      ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.HOME));
    }
  }

  /**
   * close keyboard for ios and Android
   */
  @SuppressWarnings("unchecked")
  public void hidekeyboard() {

    try {
      if (this.isAndroid) {
        ((AndroidDriver) driver).hideKeyboard();
      }else{
        By done_btn = By.xpath(
            "//XCUIElementTypeButton[@name='Done' or @name='next:' or @name='Next:' or @name='Next']");

          driver.findElement(done_btn).click();
      }
    } catch (Exception e) {
      log.warn("Hide iOS keyboard failed!!!");
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

  public void dismissAlert() {
    boolean isAndroid = driver instanceof AndroidDriver;

    if (!isAndroid) {
      try {
        HashMap<String, String> args = new HashMap<>();
        args.put("action", "dismiss");
        driver.executeScript("mobile: alert", args);

        // driver.switchTo().alert().dismiss();
      } catch (Exception e) {
        // ignore
      }
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
    touchAction().tap(ElementOption.element(element)).perform();
  }

  public void tapAtPoint(Point point) {
    PointerInput input = new PointerInput(Kind.TOUCH, "finger1");
    Sequence tap = new Sequence(input, 0);
    tap.addAction(input.createPointerMove(Duration.ZERO, Origin.viewport(), point.x, point.y));
    tap.addAction(input.createPointerDown(MouseButton.LEFT.asArg()));
    tap.addAction(new Pause(input, Duration.ofMillis(200)));
    tap.addAction(input.createPointerUp(MouseButton.LEFT.asArg()));
    driver.perform(ImmutableList.of(tap));
  }

  public void doubleTapElement(WebElement element) {
    touchAction().tap(ElementOption.element(element)).perform().tap(ElementOption.element(element))
        .perform();
  }

  /*
   * Double tap
   */
  public void doubleTapByElement(WebElement element) {
    Point point = element.getRect().getPoint();
    touchAction().tap(ElementOption.point(point)).perform().tap(ElementOption.point(point))
        .perform();
  }

  /*
   * isElement within screen bounds
   */
  public Boolean isElementDisplayedOnScreen(WebElement element) {

    Boolean isElementDisplayedOnScreen = false;

    try {
      Point point = element.getRect().getPoint();
      int eX = point.getX();
      int eY = point.getY();
      Dimension windowSize = driver.manage().window().getSize();
      int deviceWidth = windowSize.getWidth();
      int deviceHeight = windowSize.getHeight();

      isElementDisplayedOnScreen = (eX < deviceWidth | eY < deviceHeight);
    } catch (Exception e) {
      // ignore
    }
    return isElementDisplayedOnScreen;
  }

  /**
   * scroll to find with gester control
   */
  public void scrollDowntoFind(By Locator) {

    Dimension size = driver.manage().window().getSize();

    int y_start = (int) (size.height * 0.6);
    int y_end = (int) (size.height * 0.4);
    int x = size.width / 2;

    for (int i = 0; i < 5; i++) {
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

  /**
   * scroll to find with gester control
   */
  public void scrollDowntoFind2(By Locator) {

    Dimension size = driver.manage().window().getSize();

    int y_start = (int) (size.height * 0.7);
    int y_end = (int) (size.height * 0.3);
    int x = size.width / 2;

    for (int i = 0; i < 5; i++) {
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

  /**
   * scroll to find with gester control
   */
  public void scrollDowntoFind(By Locator, int scrolls) {

    Dimension size = driver.manage().window().getSize();

    int y_start = (int) (size.height * 0.6);
    int y_end = (int) (size.height * 0.3);
    int x = size.width / 2;

    for (int i = 0; i < scrolls; i++) {
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
        if (isElementDisplayed(driver.findElement(Locator))) {
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

  public void scrollUptoFind2(By Locator) {
    Dimension size = driver.manage().window().getSize();

    int y_start = (int) (size.height * 0.4);
    int y_end = (int) (size.height * 0.8);
    int x = (int) (size.width * 0.95);

    int y_start1 = (int) (size.height * 0.2);
    int y_end1 = (int) (size.height * 0.4);

    for (int i = 0; i < 6; i++) {
      try {
        sleep(2);
        if (isElementDisplayed(driver.findElement(Locator))) {
          break;
        } else {
          touchAction().longPress(ElementOption.point(x, y_start))
              .moveTo(ElementOption.point(x, y_end)).release().perform();

          touchAction().longPress(ElementOption.point(x, y_start1))
              .moveTo(ElementOption.point(x, y_end1)).release().perform();
        }
      } catch (Exception e) {
        touchAction().longPress(ElementOption.point(x, y_start))
            .moveTo(ElementOption.point(x, y_end)).release().perform();
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

  /**
   * scroll from Element1 to Element2
   */
  public void scrollFromElementOneToElementTwo(WebElement element1, WebElement element2,
      int xScrolls) {

    Point point1 = element1.getLocation();
    Point point2 = element2.getLocation();

    int mX = point1.getX();
    int yStart = point1.getY();
    int yEnd = point2.getY();

    for (int i = 0; i < xScrolls; i++) {
      try {
        sleep(2);
        touchAction().longPress(ElementOption.point(mX, yStart))
            .moveTo(ElementOption.point(mX, yEnd)).release().perform();
      } catch (Exception e) {
        touchAction().longPress(ElementOption.point(mX, yStart))
            .moveTo(ElementOption.point(mX, yEnd)).release().perform();
      }
    }
  }

  /**
   * scroll from Element1 to the bottom
   */
  public void scrollFromElementOneToTheBottom(WebElement element1, int xScrolls) {

    Dimension size = driver.manage().window().getSize();
    Point point1 = element1.getLocation();

    int mX = point1.getX();
    int yStart = point1.getY();
    int yEnd = (int) (size.height * 0.9);

    for (int i = 0; i < xScrolls; i++) {
      try {
        sleep(2);
        touchAction().longPress(ElementOption.point(mX, yStart))
            .moveTo(ElementOption.point(mX, yEnd)).release().perform();
      } catch (Exception e) {
        touchAction().longPress(ElementOption.point(mX, yStart))
            .moveTo(ElementOption.point(mX, yEnd)).release().perform();
      }
    }
  }

  /*
   * Scroll Function(UP, DOWN, LEFT, Right)
   */
  public void scroll(String direction) {

    Dimension size = driver.manage().window().getSize();

    if (direction == "UP") {

      int y_start = (int) (size.height * 0.4);
      int y_end = (int) (size.height * 0.8);
      int x = size.width / 2;

      touchAction().longPress(ElementOption.point(x, y_start)).moveTo(ElementOption.point(x, y_end))
          .release().perform();

    } else if (direction == "DOWN") {

      int y_start = (int) (size.height * 0.8);
      int y_end = (int) (size.height * 0.4);

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
        activateApp("com.apple.shortcuts");

        driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Wifi OFF']")).click();
        log.info("WIFI OFF");
        // Restart app
//        driver.resetApp();
        resetApp();
        log.info("App Restarted");
      } catch (Exception e) {
        // ignore
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void toggleWifiON(String platForm) {
    if ("ios".equalsIgnoreCase(platForm)) {
      try {
        // Turn-ON wifi
        activateApp("com.apple.Preferences");
        driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Wifi ON']")).click();
        log.info("WIFI ON");
        sleep(5);
      } catch (Exception e) {
        // ignore
      }

      try {
        // Restart app
        resetApp();
        log.info("App Restarted");
      } catch (Exception e) {
        // ignore
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void execute_OpenCurrentApp() {
    String BundleID = driver.getCapabilities()
        .getCapability("bundleId").toString();
    activateApp(BundleID);
    log.info("Switched back to App");
  }

  public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;
  }


}
