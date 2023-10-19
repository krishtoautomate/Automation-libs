package com.Utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.*;

public class BaseObjs<T> implements ITestBase {

    protected WebDriver driver;
    protected Logger log;
    protected ExtentTest test;

    protected boolean isAndroid;
    protected boolean isIOS;
    protected MobileActions mobileActions;


    protected BaseObjs(AppiumDriver driver, ExtentTest test) {
        this.driver = driver;
        this.isAndroid = driver instanceof AndroidDriver;
        this.isIOS = driver instanceof IOSDriver;
        this.log = Logger.getLogger(this.getClass().getName());
        this.test = test;
        mobileActions = new MobileActions(driver, test);
    }

    protected BaseObjs(WebDriver driver, ExtentTest test) {
        this.driver = driver;
        this.log = Logger.getLogger(this.getClass().getName());
        this.test = test;
    }

    protected BaseObjs(ExtentTest test) {
        this.test = test;
    }

    protected WebElement get_Element(By by, String elementDesc) {
        try {
            return driver.findElement(by);
        } catch (Exception e) {
            //ignore
        }
        String errorMessage = elementDesc + " - Not found in " + this.getClass().getName();
        logmessage(Status.FAIL, errorMessage);
        Assert.fail(errorMessage);
        return null;
    }

    protected WebElement get_Element(By by, String elementDesc, String action) {
        WebElement element = get_Element(by, elementDesc);
        if (action.length() > 4) {
            mobileActions.takeScreenshotAndDraw(by, action);
//            report(Status.PASS, action, element);
        }
        return element;
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
    protected List<WebElement> get_Elements(By by, String elementDesc) {
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
                ((AppiumDriver) driver).executeScript("mobile: alert", args);

                // driver.switchTo().alert().dismiss();
            } catch (Exception e) {
                // ignore
            }

            // dismiss open prompt
            try {
                driver.switchTo().alert().dismiss();
            } catch (Exception ign) {
                // ignore
            }
        }
    }

    public boolean isElementWithinScreen(By by) {
        WebElement element = driver.findElement(by);
        Point location = element.getLocation();
        Dimension size = driver.manage().window().getSize();
        int screenWidth = size.width;
        int screenHeight = size.height;
        int elementWidth = element.getSize().width;
        int elementHeight = element.getSize().height;
        int x = location.x;
        int y = location.y;
        return (x >= 0 && x + elementWidth <= screenWidth && y >= 0 && y + elementHeight <= screenHeight);
    }

    public void scrollDown(int duration) {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);
        TouchAction touchAction = new TouchAction((PerformsTouchActions) driver);
        touchAction.press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
                .moveTo(PointOption.point(startX, endY))
                .release()
                .perform();
    }


    public void scrollTillElementWithinScreen(By by, int maxScrolls) {
        int scrolls = 0;
        while (!isElementWithinScreen(by) && scrolls < maxScrolls) {
            scrollDown(1000); // scroll down for 1 second
            scrolls++;
        }
        if (scrolls == maxScrolls) {
            //throw new NoSuchElementException("Element not found or not within screen dimensions after scrolling down " + maxScrolls + " times");
        }
    }



    /**
     * Creates logs into Log4j and extent-Report with Screen-shot
     */
    @SuppressWarnings("static-access")
    public synchronized void logmessage(Status Status, String message) {
        log.info(message);
        try {
            String imgPath = ScreenShotManager.getScreenshot(driver);

            if (Status == Status.FAIL) {
                test.fail(message);
                test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
            } else if (Status == Status.INFO) {
                test.info(message);
            } else {
                test.pass(message);
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

    public void getPageSource() {
        String errorXML = driver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));
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

    public synchronized String imageGraphicScreenshot(WebElement element) {

        int x = element.getRect().getX();
        int y = element.getRect().getY();

        int width = element.getRect().getWidth();
        int height = element.getRect().getHeight();

        UUID uuid = UUID.randomUUID();
        String imgPath = ScreenShotManager.getScreenshot(driver);

        File ScreenShot = new File(Constants.NO_SCREENSHOTS_AVAILABLE);
        try {
            ScreenShot = new File(imgPath);

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
//            FileUtils.moveFile(ScreenShot, new File(Constants.REPORT_DIR + imgPath));
        } catch (Exception e) {
            log.error("TakesScreenshot service failed!!!");

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

    public String getIOSActiveAppInfo() {
        String activeApp = "";
        try {
            String jsonResponse = ((AppiumDriver) driver).executeScript("mobile:activeAppInfo").toString();
            activeApp = (jsonResponse.split("bundleId=")[1]).replaceAll("}", "");
        } catch (Exception e) {
            //ignore
        }
        log.info("Active-app :" + activeApp);
        return activeApp;
    }

}
