package testUI.elements;

import static testUI.Utils.Performance.setTime;
import static testUI.Utils.WaitUntil.waitUntilClickable;
import static testUI.Utils.WaitUntil.waitUntilVisible;
import static testUI.collections.TestUI.EE;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.Quotes;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import testUI.Utils.TestUIException;
import testUI.collections.UICollection;

public class Element extends TestUI implements UIElement {

  protected AppiumDriver driver;

  protected By element;
  private By SelenideElement;
  private By iOSElement;
  private String accesibilityId;
  private String accesibilityIdiOS;
  private int index;
  private boolean collection;
  private long lastCommandTime;

  @Override
  public long getLastCommandTime() {
    return lastCommandTime;
  }

  public Element(By element, By SelenideElement, By iOSElement, int index, boolean collection,
      String accesibilityId, String accesibilityIdiOS) {
    this.element = element;
    this.SelenideElement = SelenideElement;
    this.iOSElement = iOSElement;
    this.index = index;
    this.collection = collection;
    this.accesibilityId = accesibilityId;
    this.accesibilityIdiOS = accesibilityIdiOS;
  }

  public Element(By element, By SelenideElement, By iOSElement, int index, boolean collection,
      String accesibilityId, String accesibilityIdiOS, long lastCommandTime) {
    this.element = element;
    this.SelenideElement = SelenideElement;
    this.iOSElement = iOSElement;
    this.index = index;
    this.collection = collection;
    this.accesibilityId = accesibilityId;
    this.accesibilityIdiOS = accesibilityIdiOS;
    this.lastCommandTime = lastCommandTime;
  }

  private Element(By element) {
    this.element = element;
    this.SelenideElement = element;
    this.iOSElement = element;
    this.index = 0;
    this.collection = false;
    this.accesibilityId = "";
    this.accesibilityIdiOS = "";
  }

  public Element(String accesibilityId) {
    this.index = 0;
    this.collection = false;
    this.accesibilityId = accesibilityId;
    this.accesibilityIdiOS = accesibilityId;
  }

  private Element getElementObject() {
    return new Element(element, SelenideElement, iOSElement, index, collection, accesibilityId,
        accesibilityIdiOS);
  }

  @Override
  public UIElement setElement(By element) {
    return new Element(element);
  }

  @Override
  public UIElement setElement(UIElement element) {
    return element;
  }

  // @Override
  // public UIElement navigateTo(String url) {
  // return navigate(url);
  // }

  @Override
  public UIElement setElement(String selector) {
    if (selector.contains(": ")) {
      return new Element(selector);
    }
    return new Element("accessibilityId: " + selector);
  }

  @Override
  public UICollection setCollection(By element) {
    return EE(element);
  }

  @Override
  public UICollection setCollection(String accesibilityId) {
    return EE(accesibilityId);
  }

  @Override
  public UIElement setSelenideElement(By selenideElement) {
    return new Element(element, selenideElement, iOSElement, 0, false, accesibilityId,
        accesibilityIdiOS);
  }

  @Override
  public UIElement setiOSElement(By iOSElement) {
    return new Element(element, SelenideElement, iOSElement, 0, false, accesibilityId, "");
  }

  @Override
  public UIElement setAndroidElement(By element) {
    return new Element(element, SelenideElement, iOSElement, 0, false, "", accesibilityIdiOS);
  }

  @Override
  public UIElement setAndroidElement(String accessibilityId) {
    if (accesibilityId.contains(": ")) {
      return new Element(null, SelenideElement, iOSElement, 0, false, accessibilityId,
          accesibilityIdiOS);
    }
    return new Element(null, SelenideElement, iOSElement, 0, false,
        "accessibilityId: " + accesibilityId, accesibilityIdiOS);
  }

  @Override
  public UIElement setiOSElement(String iOSElementAccId) {
    if (iOSElementAccId.contains(": ")) {
      return new Element(element, SelenideElement, null, 0, false, accesibilityId, iOSElementAccId);
    }
    return new Element(element, SelenideElement, null, 0, false, accesibilityId,
        "accessibilityId: " + iOSElementAccId);
  }

  @Override
  public UIElement click() {
    long t = System.currentTimeMillis();
    getStringElement(accesibilityIdiOS, accesibilityId, iOSElement, element, SelenideElement);
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      if (!collection) {
        waitUntilClickable(getAppiumElement(iOSElement, element),
            getAccesibilityId(accesibilityIdiOS, accesibilityId));
      } else {
        waitUntilClickable(getAppiumElement(iOSElement, element),
            getAccesibilityId(accesibilityIdiOS, accesibilityId), index);
      }
      getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection).click();
      // } else {
      // getSelenide(SelenideElement, index, collection).click();
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    long finalTime = System.currentTimeMillis() - t;
    setTime(finalTime);
    // Logger.putLogDebug("Element '%s' was clicked after %d ms", stringElement, finalTime);
    return getElementObject();
  }

  @Override
  public UIElement doubleClick() {
    long t = System.currentTimeMillis();
    getStringElement(accesibilityIdiOS, accesibilityId, iOSElement, element, SelenideElement);
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      if (!collection) {
        waitUntilClickable(getAppiumElement(iOSElement, element),
            getAccesibilityId(accesibilityIdiOS, accesibilityId));
      } else {
        waitUntilClickable(getAppiumElement(iOSElement, element),
            getAccesibilityId(accesibilityIdiOS, accesibilityId), index);
      }
      getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection).click();
      getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection).click();
      // } else {
      // getSelenide(SelenideElement, index, collection).doubleClick();
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    long finalTime = System.currentTimeMillis() - t;
    setTime(finalTime);
    // Logger.putLogDebug("Element '%s' was double clicked after %d ms", stringElement, finalTime);
    return getElementObject();
  }

  @Override
  public Dimension getSize() {
    // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
    if (!collection) {
      waitUntilVisible(getAppiumElement(iOSElement, element),
          getAccesibilityId(accesibilityIdiOS, accesibilityId), 1, true);
    } else {
      waitUntilVisible(getAppiumElement(iOSElement, element),
          getAccesibilityId(accesibilityIdiOS, accesibilityId), index, 1, true);
    }
    return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
        .getSize();
    // } else {
    // try {
    // return getSelenide(SelenideElement, index, collection).getSize();
    // } catch (Throwable e) {
    // takeScreenshotsAllure();
    // TestUIException.handleError(e.getMessage());
    // return new Dimension(0, 0);
    // }
    // }
  }

  @Override
  public Point getLocation() {
    // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
    if (!collection) {
      waitUntilVisible(getAppiumElement(iOSElement, element),
          getAccesibilityId(accesibilityIdiOS, accesibilityId), 1000, true);
    } else {
      waitUntilVisible(getAppiumElement(iOSElement, element),
          getAccesibilityId(accesibilityIdiOS, accesibilityId), index, 1000, true);
    }
    return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
        .getLocation();
    // } else {
    // try {
    // return getSelenide(SelenideElement, index, collection).getLocation();
    // } catch (Throwable e) {
    // takeScreenshotsAllure();
    // TestUIException.handleError(e.getMessage());
    // return new Point(0, 0);
    // }
    // }
  }

  @Override
  public WaitAsserts waitFor(int Seconds) {
    return new WaitFor(element, SelenideElement, iOSElement, index, collection, accesibilityId,
        accesibilityIdiOS, Seconds);
  }

  @Override
  public String getText() {
    // try {
    // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
    return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
        .getText();
    // }
    // return getSelenide(SelenideElement, index, collection).getText();
    // } catch (Throwable e) {
    // takeScreenshotsAllure();
    // TestUIException.handleError(e.getMessage());
    // return "";
    // }
  }

  @Override
  public UIElement sendKeys(CharSequence charSequence) {
    System.currentTimeMillis();
    getStringElement(accesibilityIdiOS, accesibilityId, iOSElement, element, SelenideElement);
    // try {
    // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
    getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
        .sendKeys(charSequence);
    System.currentTimeMillis();
    // Logger.putLogDebug("Send keys '%s' to element '%s' after %d ms", charSequence, stringElement,
    // finalTime);
    return getElementObject();
  }

  @Override
  public UIElement selectElementByValue(String... values) {
    for (String value : values) {
      UIElement e = E(By.xpath("//option[@value = " + Quotes.escape(value) + "]"));
      e.waitFor(5).untilIsVisible();
      // if (Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      // if (!e.getSelenideElement().isSelected()) {
      // e.getSelenideElement().click();
      // }
      // } else {
      // if (!e.getMobileElement().isSelected()) {
      // e.getMobileElement().click();
      // }
      // }
    }

    return this;

  }

  @Override
  public UIElement setValueJs(String value) {
    System.currentTimeMillis();
    getStringElement(accesibilityIdiOS, accesibilityId, iOSElement, element, SelenideElement);
    click();
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + value + "';",
          getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, element, index,
              collection));
      // } else {
      // ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(
      // "arguments[0].value='" + value + "';", getSelenide(SelenideElement, index, collection));
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    System.currentTimeMillis();
    // Logger.putLogDebug("Set value '%s' to element '%s' after %d ms", value, stringElement,
    // finalTime);
    return getElementObject();
  }

  @Override
  public UIElement setValueJs(String value, boolean clickBeforeSetValue) {
    System.currentTimeMillis();
    getStringElement(accesibilityIdiOS, accesibilityId, iOSElement, element, SelenideElement);
    if (clickBeforeSetValue) {
      click();
    }
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      // ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + value + "';",
      // getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, element,
      // index, collection));
      // } else {
      // ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(
      // "arguments[0].value='" + value + "';", getSelenide(SelenideElement, index, collection));
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    System.currentTimeMillis();
    // Logger.putLogDebug("Set value '%s' to element '%s' after %d ms", value, stringElement,
    // finalTime);
    return getElementObject();
  }

  @Override
  public UIElement executeJsOverElement(String JsScript) {
    System.currentTimeMillis();
    getStringElement(accesibilityIdiOS, accesibilityId, iOSElement, element, SelenideElement);
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      // ((JavascriptExecutor) driver).executeScript(JsScript, getElementWithoutException(
      // accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection));
      // } else {
      // ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(JsScript,
      // getSelenide(SelenideElement, index, collection));
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    System.currentTimeMillis();
    // Logger.putLogDebug("Executed JS '%s' over element '%s' after %d ms", JsScript, stringElement,
    // finalTime);
    return getElementObject();
  }

  @Override
  public UIElement executeJs(String var1, Object... var2) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      // ((JavascriptExecutor) driver).executeScript(var1, var2);
      // } else {
      // ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(var1, var2);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public SlideActions scrollTo() {
    return new Scrolling(element, SelenideElement, iOSElement, index, collection, accesibilityId,
        accesibilityIdiOS);
  }

  @Override
  public UIElement swipe(int XCoordinate, int YCoordinate) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      TouchActions action = new TouchActions(driver);
      action.moveToElement(
          getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection),
          XCoordinate, YCoordinate);
      // } else {
      // getSelenide(SelenideElement, index, collection).scrollIntoView(true);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public UIElement swipeRight() {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      Dimension size = driver.manage().window().getSize();
      int endX = (int) (size.width * 0.8);
      TouchActions action = new TouchActions(driver);
      action
          .longPress(
              getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection))
          .move(endX,
              getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
                  .getLocation().getY())
          .release().perform();
      // } else {
      // getSelenide(SelenideElement, index, collection).scrollIntoView(true);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public UIElement swipeLeft() {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      Dimension size = driver.manage().window().getSize();
      int endX = (int) (size.width * 0.10);
      TouchActions action = new TouchActions(driver);
      action
          .longPress(
              getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection))
          .move(endX,
              getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
                  .getLocation().getY())
          .release().perform();
      // } else {
      // getSelenide(SelenideElement, index, collection).scrollIntoView(true);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public MobileElement getMobileElement() {
    try {
      return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection);
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
      return null;
    }
  }

  @Override
  public UIElement clear() {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection).clear();
      // } else {
      // getSelenide(SelenideElement, index, collection).clear();
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
    }
    return getElementObject();
  }

  @Override
  public String getCssValue(String cssValue) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
          .getCssValue(cssValue);
      // } else {
      // return getSelenide(SelenideElement, index, collection).getCssValue(cssValue);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
      return "";
    }
  }

  @Override
  public String getValue() {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
          .getAttribute("value");
      // } else {
      // return getSelenide(SelenideElement, index, collection).getValue();
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
      return "";
    }
  }

  @Override
  public String getName() {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
          .getAttribute("name");
      // } else {
      // return getSelenide(SelenideElement, index, collection).name();
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
      return "";
    }
  }

  @Override
  public String getAttribute(String Attribute) {
    try {
      // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
      return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection)
          .getAttribute(Attribute);
      // } else {
      // return getSelenide(SelenideElement, index, collection).getAttribute(Attribute);
      // }
    } catch (Throwable e) {
      takeScreenshotsAllure();
      TestUIException.handleError(e.getMessage());
      return "";
    }
  }

  @Override
  public boolean isVisible() {
    // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM))
    // return visible(getAppiumElement(iOSElement, element),
    // getAccesibilityId(accesibilityIdiOS, accesibilityId));
    // return getSelenide(SelenideElement, index, collection).isDisplayed();
    return true;
  }

  @Override
  public boolean isEnabled() {
    // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM))
    // return enable(getAppiumElement(iOSElement, element),
    // getAccesibilityId(accesibilityIdiOS, accesibilityId));
    // return getSelenide(SelenideElement, index, collection).isEnabled();
    return true;
  }

  @Override
  public boolean Exists() {
    // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM))
    // return exists(getAppiumElement(iOSElement, element),
    // getAccesibilityId(accesibilityIdiOS, accesibilityId));
    // return getSelenide(SelenideElement, index, collection).exists();
    return true;
  }

  @Override
  public Asserts shouldHave() {
    return null;
    // return new ShouldBe(element, SelenideElement, iOSElement, index, collection, accesibilityId,
    // accesibilityIdiOS, Configuration.timeout, true);

  }

  @Override
  public Asserts shouldBe() {
    return shouldHave();
  }

  @Override
  public Asserts should() {
    return shouldHave();
  }


  @Override
  public UIElement saveScreenshot(String path) {
    // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
    // if (driver.size() != 0) {
    // Configuration.driver = Math.min(Configuration.driver, driver.size());
    File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    try {
      FileUtils.copyFile(scrFile, new File("~/documents" + path));
    } catch (IOException e) {
      // Logger.putLogError("Could not save the screenshot in '" + "~/documents"
      // + path + "': " + e.getMessage());
    }
    // }}else{

    // File scrFile = ((TakesScreenshot) getSelenideDriver()).getScreenshotAs(OutputType.FILE);try
    // {
    // FileUtils.copyFile(scrFile, new File(Configuration.screenshotPath + path));
    // }catch(IOException e)
    // {
    // Logger.putLogError("Could not save the screenshot in '" + Configuration.screenshotPath
    // + path + "': " + e.getMessage());
    // }
    // }
    return getElementObject();
  }

  @Override
  public String getCurrentUrl() {
    // if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM))
    return driver.getCurrentUrl();
    // return getSelenideDriver().getCurrentUrl();
  }

  // public BrowserLogs getNetworkCalls() {
  // return new BrowserLogs().getNetworkCalls();
  // }

  @Override
  public void getBrowserLogs() {
    // new BrowserLogs().getBrowserLogs();
  }

  // public BrowserLogs getLastNetworkCalls(int LastX) {
  // return new BrowserLogs().getLastNetworkCalls(LastX);
  // }

  @Override
  public UIElement and() {
    return getElementObject();
  }

  @Override
  public UIElement and(String description) {
    System.out.println("\u001B[32m Working step ->   And " + description + "\u001B[0m");
    step = true;
    // if (Configuration.useAllure) {
    // Allure.step("And " + description);
    // }
    return getElementObject();
  }

  @Override
  public UIElement given() {
    return getElementObject();
  }

  @Override
  public UIElement given(String description) {
    System.out.println("\u001B[32m Working step -> Given " + description + "\u001B[0m");
    step = true;
    // if (Configuration.useAllure) {
    // Allure.step("Given " + description);
    // }
    return getElementObject();
  }

  @Override
  public UIElement then() {
    return getElementObject();
  }

  @Override
  public UIElement then(String description) {
    System.out.println("\u001B[32m Working step -> Then " + description + "\u001B[0m");
    step = true;
    // if (Configuration.useAllure) {
    // Allure.step("Then " + description);
    // }
    return getElementObject();
  }

  @Override
  public UIElement when(String description) {
    System.out.println("\u001B[32m Working step -> When " + description + "\u001B[0m");
    step = true;
    // if (Configuration.useAllure) {
    // Allure.step("When " + description);
    // }
    return getElementObject();
  }

  @Override
  public UIElement when() {
    return getElementObject();
  }


  // public com.codeborne.selenide.SelenideElement getSelenideElement() {
  // return getSelenide(SelenideElement, index, collection);
  // }

  private static boolean step = false;

  public static boolean getStep() {
    return step;
  }

  public static void setStep(boolean step) {
    Element.step = step;
  }

  @Override
  public UIElement navigateTo(String url) {
    // TODO Auto-generated method stub
    return getElementObject();
  }
}
