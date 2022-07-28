package com.Utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.restassured.response.Response;
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
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import java.time.Duration;

public class BaseObjs<T> implements ITestBase {

  protected AppiumDriver driver;
  protected Logger log;
  protected ExtentTest test;
  protected MobileActions mobileActions;


  protected BaseObjs(AppiumDriver driver, Logger log, ExtentTest test) {
    this.driver = driver;
    this.log = log;
    this.test = test;
    mobileActions = new MobileActions(driver, log, test);
  }

  protected BaseObjs(Logger log, ExtentTest test) {
    this.log = log;
    this.test = test;
  }

  protected MobileElement get_Element(By by, String elementDesc) {
    WebElement ele = null;
    try {
      return (MobileElement) driver.findElement(by);
    } catch (Exception e) {
      String errorMessage = elementDesc + " - Not found in " + this.getClass().getName();
      logmessage(Status.FAIL, errorMessage);
      Assert.fail(errorMessage);
    }
    return null;
  }

  protected boolean isElementDisplayed(By by) {
    try {
//      return new WebDriverWait(driver, 5)
//          .until(ExpectedConditions.presenceOfElementLocated(locator)).isDisplayed();

//      new FluentWait(driver).withTimeout(Duration.ofSeconds(5))
//    .pollingEvery(Duration.ofSeconds(1))
//    .ignoring(NoSuchElementException.class)
//          .ignoring(StaleElementReferenceException.class).until(ExpectedConditions.presenceOfElementLocated(by));

      return driver.findElement(by).isDisplayed();
    } catch (Exception e) {
      // ignore
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  protected List<MobileElement> get_Elements(By by, String elementDesc) {
    try {
      return driver.findElements(by);
    } catch (Exception e) {
      String errorMessage = elementDesc + " - Not found in " + this.getClass().getName();
      logmessage(Status.FAIL, errorMessage);
      Assert.fail(errorMessage);
    }
    return null;
  }

  public void dismissAlert() {
    boolean isAndroid = driver instanceof AndroidDriver;

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
      log.severe("TakesScreenshot service failed!!!");

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
        log.severe("TakesScreenshot service failed!!!");
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
      ele = driver.findElement(locator);
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
      log.severe("TakesScreenshot service failed!!!");

      try {
        FileUtils.copyFile(ScreenShot, new File(Constants.REPORT_DIR + imgPath));
      } catch (IOException e1) {
        // ignore
      }
    }

    return imgPath;
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
      log.severe("image error");
    }
    return base64;
  }

}
