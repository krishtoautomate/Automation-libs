package testUI.elements;

import static java.lang.Math.abs;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import testUI.Utils.TestUIException;

public class Scrolling extends TestUI implements SlideActions {
  private By AppiumElement;
  private By iOSElement;
  private By SelenideElement;
  private String accesibilityId;
  private String accesibilityIdiOS;
  private int index;
  private boolean collection;

  protected AppiumDriver driver;

  protected Scrolling(By AppiumElement, By SelenideElement, By iOSElement, int index,
      boolean collection, String accesibilityId, String accesibilityIdiOS) {
    this.AppiumElement = AppiumElement;
    this.iOSElement = iOSElement;
    this.SelenideElement = SelenideElement;
    this.accesibilityId = accesibilityId;
    this.accesibilityIdiOS = accesibilityIdiOS;
    this.index = index;
    this.collection = collection;
  }

  private Element getElementObject() {
    return new Element(AppiumElement, SelenideElement, iOSElement, index, collection,
        accesibilityId, accesibilityIdiOS);
  }

  @Override
  public UIElement customSwipeUp(int PixelGap, int numberOfSwipes) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      for (int i = 0; i < numberOfSwipes; i++) {
        TouchAction action = new TouchAction(driver);
        int startY = 500;
        PixelGap = abs(PixelGap);
        int endY = 500 - PixelGap;
        if (endY < 0) {
          endY = 100;
          startY = endY + PixelGap;
        }
        action.press(PointOption.point(40, startY))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
            .moveTo(PointOption.point(40, endY)).release().perform();
      }
      // } else {
      // getSelenide(SelenideElement, index, collection).scrollIntoView(true);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      throw new TestUIException(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public UIElement customSwipeDown(int PixelGap, int numberOfSwipes) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      for (int i = 0; i < numberOfSwipes; i++) {
        TouchAction action = new TouchAction(driver);
        int startY = 500;
        PixelGap = abs(PixelGap);
        int endY = 500 + PixelGap;
        action.press(PointOption.point(40, startY))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
            .moveTo(PointOption.point(40, endY)).release().perform();
      }
      // } else {
      // getSelenide(SelenideElement, index, collection).scrollIntoView(true);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      throw new TestUIException(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public UIElement swipeLeft(int PixelGap, int startX, int startY) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      TouchAction action = new TouchAction(driver);
      PixelGap = abs(PixelGap);
      int endX = startX - PixelGap;
      action.press(PointOption.point(startX, startY))
          .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
          .moveTo(PointOption.point(endX, startY)).release().perform();
      // } else {
      // getSelenide(SelenideElement, index, collection).scrollIntoView(true);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      throw new TestUIException(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public UIElement swipeRigt(int PixelGap, int startX, int startY) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      TouchAction action = new TouchAction(driver);
      PixelGap = abs(PixelGap);
      int endX = startX + PixelGap;
      action.press(PointOption.point(startX, startY))
          .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
          .moveTo(PointOption.point(endX, startY)).release().perform();
      // } else {
      // getSelenide(SelenideElement, index, collection).scrollIntoView(true);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      throw new TestUIException(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public UIElement view(boolean upCenter) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(" + upCenter + ");",
          getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, AppiumElement,
              index, collection));
      // } else {
      // getSelenide(SelenideElement, index, collection).scrollIntoView(upCenter);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      throw new TestUIException(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public UIElement view(String options) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(" + options + ");",
          getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, AppiumElement,
              index, collection));
      // } else {
      // getSelenide(SelenideElement, index, collection).scrollIntoView(options);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      throw new TestUIException(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public UIElement click() {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      ((JavascriptExecutor) driver).executeScript(
          "arguments[0].scrollIntoView("
              + "{behavior: \"smooth\", block: \"center\", inline: \"nearest\"});",
          getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, AppiumElement,
              index, collection));
      getElement(accesibilityIdiOS, accesibilityId, iOSElement, AppiumElement, index, collection)
          .click();
      // } else {
      // getSelenide(SelenideElement, index, collection).
      // scrollIntoView(
      // "{behavior: \"smooth\", block: \"center\", inline: \"nearest\"}")
      // .click();
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      throw new TestUIException(e.getMessage());
    }
    return getElementObject();
  }
}
