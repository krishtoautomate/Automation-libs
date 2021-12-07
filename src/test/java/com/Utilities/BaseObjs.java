package com.Utilities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
/**
 * Created by Krish on 21.07.2018.
 */
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.restassured.response.Response;

public class BaseObjs<T> implements ITestBase {
  protected WebDriver driver;
  protected WebDriverWait wait;
  protected Logger log;
  protected ExtentTest test;
  protected MobileActions mobileActions;


  protected BaseObjs(WebDriver driver, Logger log, ExtentTest test) {
    this.driver = driver;
    this.log = log;
    this.test = test;
    mobileActions = new MobileActions(driver, log, test);
    wait = new WebDriverWait(driver, 10);
    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
  }

  protected BaseObjs(Logger log, ExtentTest test) {
    this.log = log;
    this.test = test;
  }

  protected WebElement get_Element(By locator, String elementDesc) {
    WebElement ele = null;
    try {
      ele = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    } catch (Exception e) {
      String errorMessage = elementDesc + " - Not found in " + this.getClass().getName();
      logmessage(Status.FAIL, errorMessage);
      Assert.fail(errorMessage);
    }
    return ele;
  }

  protected WebElement verify_Element(By locator) {
    WebElement ele = null;
    try {
      ele =
          new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(locator));
    } catch (StaleElementReferenceException e) {
      // ignore
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  protected List<WebElement> get_Elements(By locator, String elementDesc) {
    List<WebElement> eles = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
      eles = driver.findElements(locator);
    } catch (Exception e) {
      String errorMessage = elementDesc + " - Not found in " + this.getClass().getName();
      logmessage(Status.FAIL, errorMessage);
      Assert.fail(errorMessage);
    }
    return eles;
  }

  protected List<WebElement> verify_Elements(By locator) {
    List<WebElement> eles = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
      eles = driver.findElements(locator);
    } catch (StaleElementReferenceException e) {
      // ignore
    } catch (Exception e) {
      // ignore
    }
    return eles;
  }

  public void dismissAlert() {
    Boolean isAndroid = driver instanceof AndroidDriver;

    if (!isAndroid) {
      try {
        HashMap<String, String> args = new HashMap<>();
        args.put("action", "dismiss");
        ((RemoteWebDriver) driver).executeScript("mobile: alert", args);

        // driver.switchTo().alert().dismiss();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  /**
   * Verifies if String contains other String
   */
  public void AssertContains(String Actual, String Expected) {

    Boolean found = false;

    String actual = Actual.replaceAll("\n", " ");
    String[] arrOfExpected = Expected.split("\\|");

    for (String expected : arrOfExpected) {
      if (StringUtils.containsIgnoreCase(actual.trim(), expected.trim())
          | StringUtils.containsIgnoreCase(expected, actual.trim()) && !actual.isEmpty()) {
        logmessage(Status.PASS,
            "Verification Success : '" + actual.trim() + "' Vs '" + expected.trim() + "'");
        found = true;
        break;
      }
    }

    if (!found) {
      String message = "Verification failed : '" + actual + "' Vs '" + Expected + "'";
      logmessage(Status.FAIL, message);
      Assert.fail(message);
    }
  }

  /**
   * Verifies if String not same as other String
   */
  public void AssertNotEquals(String Actual, String Expected) {

    if (!Actual.equals(Expected) || !Expected.equals(Actual)) {
      logmessage(Status.INFO, "Verification Success : " + Actual + " not-equals " + Expected);
    } else {
      logmessage(Status.FAIL, "Verification failed : " + Actual + " equals " + Expected);
      Assert.fail("Verification failed : " + Actual + " equals " + Expected);
    }
  }

  /**
   * Verifies if String same as other String
   */
  public void AssertEquals(String Actual, String Expected) {
    logmessage(Status.INFO, "Actual : " + Actual + ", Expected : " + Expected);
    if (Actual.equals(Expected) || Expected.equals(Actual)) {
      logmessage(Status.INFO, Actual + " equals " + Expected);
    } else {
      logmessage(Status.FAIL, Actual + " DONOT equals " + Expected);
      Assert.fail(Actual + " DONOT equals " + Expected);
    }
  }

  /**
   * Verifies if Float same as other Float
   */
  public void AssertEquals(Float Actual, Float Expected) {
    logmessage(Status.INFO, "Actual : " + Actual + ", Expected : " + Expected);
    if (Actual.equals(Expected) || Expected.equals(Actual)) {
      logmessage(Status.INFO, Actual + " equals " + Expected);
    } else {
      logmessage(Status.FAIL, Actual + " DONOT equals " + Expected);
      Assert.fail(Actual + " DONOT equals " + Expected);
    }
  }

  /**
   * Verifies if boolean value same as other boolean
   */
  public void AssertEquals(Boolean Actual, Boolean Expected) {
    logmessage(Status.INFO, "Actual : " + Actual + ", Expected : " + Expected);
    if (Actual.equals(Expected) || Expected.equals(Actual)) {
      logmessage(Status.INFO, Actual + " equals " + Expected);
    } else {
      logmessage(Status.FAIL, Actual + " DONOT equals " + Expected);
      Assert.fail(Actual + " DONOT equals " + Expected);
    }
  }

  public synchronized String takeScreenshot() {

    // Unique name to screen-shot
    UUID uuid = UUID.randomUUID();
    String imgPath = "img/" + uuid.toString() + "(" + Constants.TIME_NOW + ")" + ".PNG";

    File ScreenShot = new File(Constants.NO_SCREENSHOTS_AVAILABLE);
    try {
      ScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

      FileUtils.moveFile(ScreenShot, new File(Constants.REPORT_DIR + imgPath));

    } catch (WebDriverException | IOException e) {
      log.error("TakesScreenshot service failed!!!");

      try {
        FileUtils.copyFile(ScreenShot, new File(Constants.REPORT_DIR + imgPath));
      } catch (IOException e1) {
        // ignore
      }
    }
    return imgPath;
  }

  /**
   * Creates logs into Log4j and extent-Report with Screen-shot
   */
  @SuppressWarnings("static-access")
  public synchronized void logmessage(Status Status, String message) {
    log.info(message);
    try {
      String imgPath = takeScreenshot();

      if (Status == Status.FAIL) {
        test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
      } else if (Status == Status.INFO) {
        test.info(message);
      } else {
        test.pass(message, MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
      }
    } catch (WebDriverException e) {
      if (Status == Status.FAIL) {
        test.fail(message);
      } else {
        test.pass(message);
      }
    }
  }

  /**
   * Creates logs into Log4j and extent-Report with Screen-shot
   */
  @SuppressWarnings("static-access")
  public synchronized void log(Status Status, String message) {
    log.info(message);
    try {

      String screenShot = "";
      try {
        screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
      } catch (Exception e) {
        log.error("TakesScreenshot service failed!!!");
      }

      if (Status == Status.FAIL) {
        test.fail(message,
            MediaEntityBuilder.createScreenCaptureFromBase64String(screenShot).build());
      } else if (Status == Status.INFO) {
        test.info(message);
      } else {
        test.pass(message,
            MediaEntityBuilder.createScreenCaptureFromBase64String(screenShot).build());
      }
    } catch (WebDriverException e) {
      if (Status == Status.FAIL) {
        test.fail(message);
      } else {
        test.pass(message);
      }
    }
  }

  /**
   * Creates logs into Log4j and extent-Report with Screen-shot
   */
  @SuppressWarnings("static-access")
  protected synchronized void report(Status Status, String message, WebElement element) {
    log.info(message);
    try {
      String imgPath = imageGraphicScreenshot(element);

      if (Status == Status.FAIL) {
        test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
      } else if (Status == Status.INFO) {
        test.info(message);
      } else {
        test.pass(message, MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
      }
    } catch (WebDriverException e) {
      if (Status == Status.FAIL) {
        test.fail(message);
      } else if (Status == Status.INFO) {
        test.info(message);
      } else {
        test.pass(message);
      }
    }
  }

  protected boolean switchToNativeContext(String context) {
    ArrayList<String> contexts =
        new ArrayList<String>(((AppiumDriver<?>) driver).getContextHandles());
    for (String cntext : contexts) {
      if (cntext.contains(context)) {
        ((AppiumDriver<?>) driver).context(cntext);
        return true;
      }
    }
    return false;
  }

  protected WebElement get_Element(By locator, String elementDesc, String action) {
    WebElement ele = null;
    try {
      ele = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
      if (action != null | ele != null | action.equalsIgnoreCase("NONE")) {
        report(Status.PASS, action, ele);
      }
    } catch (Exception e) {
      String errorMessage = elementDesc + " - Not found in " + this.getClass().getName();
      logmessage(Status.FAIL, errorMessage);
      Assert.fail(errorMessage);
    }
    return ele;
  }

  public synchronized String imageGraphicScreenshot(WebElement element) {

    boolean isAndroid = driver instanceof AndroidDriver ? true : false;

    int x = element.getRect().getX();
    int y = element.getRect().getY();

    int width = element.getRect().getWidth();
    int height = element.getRect().getHeight();

    UUID uuid = UUID.randomUUID();
    String imgPath = "img/" + uuid.toString() + "_" + Constants.TIME_NOW + "_" + ".PNG";

    File ScreenShot = new File(Constants.NO_SCREENSHOTS_AVAILABLE);
    try {
      ScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

      if (isAndroid) {
        // Crop the entire page screenshot to get only element screenshot
        BufferedImage fullImg = ImageIO.read(ScreenShot);

        Graphics2D g2d = fullImg.createGraphics();
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(5));

        // g2d.translate(x, y);

        g2d.drawRect(x, y, width, height);
        g2d.dispose();

        ImageIO.write(fullImg, "png", ScreenShot);
      }

      // Copy the element screenshot to disk
      FileUtils.moveFile(ScreenShot, new File(Constants.REPORT_DIR + imgPath));
    } catch (WebDriverException | IOException e) {
      log.error("TakesScreenshot service failed!!!");

      try {
        FileUtils.copyFile(ScreenShot, new File(Constants.REPORT_DIR + imgPath));
      } catch (IOException e1) {
        // ignore
      }
    }

    return imgPath;
  }

  /**
   * type text in a inputElement
   */
  protected void type(String text, By element) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(element));

    WebElement object = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", object);

    driver.findElement(element).sendKeys(text);
  }

  /**
   * click on element
   */
  protected void click(By element) {
    wait.until(ExpectedConditions.elementToBeClickable(element));
    WebElement object = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", object);
    driver.findElement(element).click();
  }

  /**
   * find an element
   */
  protected void find(By element) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    WebElement object = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", object);
    driver.findElement(element);
  }

  /**
   * wait for visibility of locator and timeINSeconds-to-wait
   */
  protected void waitForVisibilityOf(By locator, Integer... timeOutInSeconds) {
    int attempts = 0;
    while (attempts < 2) {
      try {
        waitFor(ExpectedConditions.visibilityOfElementLocated(locator),
            (timeOutInSeconds.length > 0 ? timeOutInSeconds[0] : null));
        break;
      } catch (StaleElementReferenceException e) {

      }
      attempts++;
    }
  }

  /**
   * wait for expected condition for timeINSeconds-to-wait
   */
  protected void waitFor(ExpectedCondition<WebElement> condition, Integer timeOutInSeconds) {
    timeOutInSeconds = timeOutInSeconds != null ? timeOutInSeconds : 30;
    wait.until(condition);
  }

  By remindMeLater_btn = By.xpath(
      "//android.widget.Button[contains(@resource-id, '0_resource_name_obfuscated') and @text='Not now'] | "
          + "//android.widget.Button[@text='REMIND ME LATER' or @text='Remind me later'] | "
          + "//android.widget.Button[contains(@resource-id, 'remindMeLatterButton') or @text='REMIND ME LATER' or @text='Remind me later']");

  private WebElement verify_remindMeLater_btn() {
    mobileActions.waitForProgressBarToDisappear();
    return verify_Element(remindMeLater_btn);
  }

  By notNow_btn = MobileBy.iOSNsPredicateString("label == 'Not Now' AND visible =1");

  private WebElement verify_notNow_btn() {
    mobileActions.waitForProgressBarToDisappear();
    return verify_Element(notNow_btn);
  }

  public void dismissAppRating() {
    boolean isAndroid = driver instanceof AndroidDriver;
    if (isAndroid) {
      if (isElementDisplayed(verify_remindMeLater_btn())) {
        verify_remindMeLater_btn().click();
        logmessage(Status.INFO, "'REMIND ME LATER' button clicked - for Rate my app!");
      }
    } else {
      if (isElementDisplayed(verify_notNow_btn())) {
        verify_notNow_btn().click();
        logmessage(Status.INFO, "'Not Now' button clicked - for Rate my app!");
      }
    }
  }

  public void VERIFY_API_STATUS(Response response) {
    if (response.getStatusCode() != HttpStatus.SC_OK) {
      test.fail(MarkupHelper.createCodeBlock(response.getBody().asString()));
      Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
    } else {
      test.pass("RESPONSE STATUS_CODE : " + response.getStatusCode());
    }
  }

  public void VERIFY_API_CONTAINS(Response response, String message) {
    if ((response.getBody().asString().toLowerCase()).contains(message.toLowerCase()) == false) {
      test.fail(MarkupHelper.createCodeBlock(response.prettyPrint()));
      Assert.assertTrue(response.getBody().asString().contains(message));
    } else {
      test.pass("Verification - RESPONSE contains : " + message);
    }
  }

  public void VERIFY_API_VALUE(String expected, String actual, Response response) {
    if ((expected.toLowerCase()).contains(actual.toLowerCase()) == false) {
      test.fail(MarkupHelper.createCodeBlock(response.prettyPrint()));
      Assert.assertTrue(response.getBody().asString().contains(actual));
    } else {
      test.pass("Verification - RESPONSE contains : " + actual);
    }
  }

  /**
   *
   * Method for Expected Deep Link Title //XCUIElementTypeOther/XCUIElementTypeStaticText[@label]
   */

  By expected_title = By.xpath(
      "//android.widget.FrameLayout[contains(@resource-id, 'design_bottom_sheet')]/*/android.widget.TextView | //*[contains(@resource-id,'headerBarTitleTextView') and not(contains(@text,'('))]|//*[contains(@resource-id,'toolbar')]/android.widget.TextView |"
          + "//*[contains(@resource-id,'id/headerTitleTextView')] | //*[contains(@resource-id,'titleTextView')] |//*[contains(@resource-id,'topbar')]//android.widget.TextView[@index='1'] |//*[@name='LongHeaderTableView.headerView']|"
          + "//XCUIElementTypeNavigationBar/XCUIElementTypeStaticText[@label] |//XCUIElementTypeNavigationBar/XCUIElementTypeStaticText/XCUIElementTypeStaticText[@label] | //XCUIElementTypeNavigationBar/XCUIElementTypeStaticText/XCUIElementTypeStaticText[string-length(@label)>0] |"
          + "//*[@name='selectTopUpMethodLabel'] |//*[@name='AddOnInfoView.titleLabel']");

  public WebElement get_expected_title() {
    WebElement ele = null;

    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(expected_title));
      ele = driver.findElement(expected_title);

    } catch (Exception e) {
      String message = "Expected Title Not found";
      logmessage(Status.FAIL, message);
      Assert.fail(message);
    }
    return ele;
  }

  /*
   * Convert Image to Base64 recognition
   */

  public String convertImgToBase64(String imgPath) {
    URL refImgUrl = getClass().getClassLoader().getResource(imgPath);
    File refImgFile;
    String base64 = "";
    try {
      refImgFile = Paths.get(refImgUrl.toURI()).toFile();
      base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(refImgFile.toPath()));
    } catch (URISyntaxException | IOException e) {
      log.error("image error");
    }
    return base64;
  }

}
