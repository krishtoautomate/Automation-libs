package com.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
import com.mobileActions.MobileActions;

import io.appium.java_client.android.AndroidDriver;
import io.restassured.response.Response;



public class BaseObjs<T> {
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

  protected BaseObjs() {

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

  public void dismissAlert() {
    Boolean isAndroid = driver instanceof AndroidDriver ? true : false;

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
   * Clears the text Field
   */
  protected void clear(By element) {
    WebElement object = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", object);
    driver.findElement(element).clear();
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
   * TRUE - If element is Selected
   */
  public boolean isElementSelected(WebElement element) {
    Boolean isSelected = false;
    try {
      isSelected = element.isSelected() ? true : false;
    } catch (Exception e) {
      // ignore
    }
    return isSelected;
  }

  /**
   * TRUE - If element is Enabled
   */
  public boolean isElementEnabled(WebElement element) {
    Boolean isEnabled = false;
    try {
      isEnabled = element.isEnabled() ? true : false;
    } catch (Exception e) {
      isEnabled = false;
    }
    return isEnabled;
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
   * Verifies if String contains other String
   */
  public void AssertContains(String Actual, String Expected) {

    String actual = Actual.replaceAll("\n", " ");
    String expected = Expected;

    if (StringUtils.containsIgnoreCase(actual, expected)
        | StringUtils.containsIgnoreCase(expected, actual) && !actual.isEmpty()) {
      logmessage(Status.INFO, "Verification Success : '" + actual + "' Vs '" + expected + "'");
    } else {
      logmessage(Status.FAIL, "Verification fail : '" + actual + "' Vs '" + expected + "'");
      Assert.fail("Verification fail : " + actual + " Vs " + expected);
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
   * Creates logs into Log4j and extent-Report with Screen-shot
   */
  @SuppressWarnings("static-access")
  public synchronized void logmessage(Status Status, String message) {
    log.info(message);
    try {
      String imgPath = takeScreenshot();

      if (Status == Status.FAIL) {
        test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
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


  /*
   * Skip 'Rate MY App' prompt
   */
  By remindMeLater_btn = By.xpath(
      "//android.widget.Button[contains(@resource-id, 'remindMeLatterButton') or @text='REMIND ME LATER' or @text='Remind me later']");

  public WebElement get_remindMeLater_btn() {
    WebElement ele = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(remindMeLater_btn));
      ele = driver.findElement(remindMeLater_btn);
    } catch (Exception e) {
      // ignore
    }
    return ele;
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

}
