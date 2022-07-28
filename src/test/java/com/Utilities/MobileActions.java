package com.Utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableList;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.offset.ElementOption;
import java.time.Duration;
import java.util.HashMap;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.testng.Assert;

public class MobileActions implements ITestBase {

  private AppiumDriver driver;
  private Logger log;
  private ExtentTest test;

  public MobileActions(AppiumDriver driver, Logger log, ExtentTest test) {
    this.driver = driver;
    this.log = log;
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
  public void send_keys_android(String Value) {
    for (int i = 0; i < Value.length(); i++) {
      char c = Value.charAt(i);
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
        log.severe("SendKeys Failed!");
        Assert.fail("Send Keys failed");
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void activateApp(String platForm, String bundleId) {
    if ("ios".equalsIgnoreCase(platForm)) {
      ((AppiumDriver<MobileElement>) driver).activateApp(bundleId);
    }
  }

  @SuppressWarnings("unchecked")
  public void terminateApp(String bundleId) {
    boolean isAndroid = driver instanceof AndroidDriver;
    if (!isAndroid) {
      ((AppiumDriver<MobileElement>) driver).terminateApp(bundleId);
    }
  }

  @SuppressWarnings("unchecked")
  public void resetApp(String platForm, String bundleId) {
    if ("ios".equalsIgnoreCase(platForm)) {
      ((AppiumDriver<MobileElement>) driver).activateApp(bundleId);
      ((AppiumDriver<MobileElement>) driver).resetApp();
    }
  }

  @SuppressWarnings("unchecked")
  public void activateAndroidDeepLink(String deepLinkUrl) {
    String packageId = ((AppiumDriver<MobileElement>) driver).getCapabilities()
        .getCapability("appPackage").toString();
    HashMap<String, String> event = new HashMap<>();
    event.put("url", deepLinkUrl);
    event.put("package", packageId);
    ((AndroidDriver<MobileElement>) driver).executeScript("mobile:deepLink", event);
  }

  @SuppressWarnings("unchecked")
  public void activateSafariApp() {
    ((AppiumDriver<MobileElement>) driver).activateApp("com.apple.mobilesafari");
  }

  @SuppressWarnings("unchecked")
  public void terminateSafariApp() {
    ((AppiumDriver<MobileElement>) driver).terminateApp("com.apple.mobilesafari");
  }

  @SuppressWarnings("unchecked")
  public void closeApp() {
    ((AppiumDriver<MobileElement>) driver).closeApp();
  }

  @SuppressWarnings("unchecked")
  public void launchUrl(String platForm, String url) {
    ((AppiumDriver<MobileElement>) driver).get(url);
  }

  /*
   * Get Home Screen on Android
   */
  @SuppressWarnings("unchecked")
  public void get_androidHomeScreen(String platform) {
    if ("Android".equalsIgnoreCase(platform)) {
      ((AndroidDriver<MobileElement>) driver).pressKey(new KeyEvent(AndroidKey.HOME));
    }
  }

  /**
   * close keyboard for ios and Android
   */
  @SuppressWarnings("unchecked")
  public void hidekeyboard() {

    String platform = driver instanceof AndroidDriver ? "android" : "ios";

    if ("ios".equalsIgnoreCase(platform)) {
      By done_btn = By.xpath(
          "//XCUIElementTypeButton[@name='Done' or @name='next:' or @name='Next:' or @name='Next']");
      try {
        driver.findElement(done_btn).click();
      } catch (Exception e) {
        log.warning("Hide iOS keyboard failed!!!");
      }
    } else {
      try {
        ((AppiumDriver<MobileElement>) driver).hideKeyboard();
      } catch (Exception e) {
        log.info("Hide Android keyboard failed!!!");
      }
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

  public void sendKeys(String keys) {

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
    ((AppiumDriver<?>) driver).perform(ImmutableList.of(tap));
  }

  public void doubleTapElement(WebElement element) {
    touchAction().tap(ElementOption.element(element)).perform().tap(ElementOption.element(element))
        .perform();
  }

  /*
   * Double tap
   */
  public void doubleTapByElement(WebElement element) {
    Point point = ((MobileElement) element).getCenter();
    touchAction().tap(ElementOption.point(point)).perform().tap(ElementOption.point(point))
        .perform();
  }

  /*
   * isElement within screen bounds
   */
  public Boolean isElementDisplayedOnScreen(WebElement element) {

    Boolean isElementDisplayedOnScreen = false;

    try {
      Point point = ((MobileElement) element).getCenter();
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
        log.warning("scroll failed!!!");
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

    Point point1 = ((MobileElement) element1).getLocation();
    Point point2 = ((MobileElement) element2).getLocation();

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
    Point point1 = ((MobileElement) element1).getLocation();

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
  public void toggleWifiON(String platForm) {
    if ("ios".equalsIgnoreCase(platForm)) {
      try {
        // Turn-ON wifi
        ((AppiumDriver<MobileElement>) driver).activateApp("com.apple.shortcuts");
        driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Wifi ON']")).click();
        log.info("WIFI ON");
        sleep(10);
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

  @SuppressWarnings("unchecked")
  public void execute_OpenCurrentApp() {
    String BundleID = ((AppiumDriver<MobileElement>) driver).getCapabilities()
        .getCapability("bundleId").toString();
    ((AppiumDriver<MobileElement>) driver).activateApp(BundleID);
    log.info("Switched back to App");
  }


}
