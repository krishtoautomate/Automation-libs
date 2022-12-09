package com.Utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class BaseObjsWeb<T> implements ITestBase {

    protected WebDriver driver;
    protected Logger log;
    protected ExtentTest test;
    protected MobileActions mobileActions;

    protected BaseObjsWeb(WebDriver driver, ExtentTest test) {
        this.driver = driver;
        this.log = Logger.getLogger(this.getClass().getName());
        this.test = test;
        mobileActions = new MobileActions(driver, test);
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
        try {
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            // ignore
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

    protected WebElement get_Element(By locator, String elementDesc, String action) {
        WebElement ele = null;
        try {
            ele = driver.findElement(locator);
            if (action != null || ele != null || action.equalsIgnoreCase("NONE")) {
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

        int x = element.getRect().getX();
        int y = element.getRect().getY();

        int width = element.getRect().getWidth();
        int height = element.getRect().getHeight();

        UUID uuid = UUID.randomUUID();
        String imgPath = "img/" + uuid + "_" + Constants.TIME_NOW + "_" + ".PNG";

        File ScreenShot = new File(Constants.NO_SCREENSHOTS_AVAILABLE);
        try {
            ScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Crop the entire page screenshot to get only element screenshot
            BufferedImage fullImg = ImageIO.read(ScreenShot);

            Graphics2D g2d = fullImg.createGraphics();
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(5));

            // g2d.translate(x, y);

            g2d.drawRect(x, y, width, height);
            g2d.dispose();

            ImageIO.write(fullImg, "png", ScreenShot);

            // Copy the element screenshot to disk
            FileUtils.moveFile(ScreenShot, new File(Constants.REPORT_DIR + imgPath));
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

}
