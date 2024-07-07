package com.Utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MobileActions implements ITestBase {

    boolean isAndroid = false;
    boolean isIOS = false;
    private AppiumDriver driver;
    private WebDriver webDriver;
    private Logger log;
    private ExtentTest test;

    public MobileActions(AppiumDriver driver, ExtentTest test) {
        this.driver = driver;
        this.log = Logger.getLogger(this.getClass().getName());
        this.isAndroid = driver instanceof AndroidDriver;
        this.isIOS = driver instanceof IOSDriver;
        this.test = test;
    }

    public MobileActions(WebDriver driver, ExtentTest test) {
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

    public void takeScreenshotAndDraw(By by, String message) {
        // Take screenshot
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            BufferedImage image = ImageIO.read(srcFile);

            // Get element location and size
            WebElement element = driver.findElement(by);
            Point location = element.getLocation();
            Dimension size = element.getSize();

            // Get screen size
            Dimension screenSize = driver.manage().window().getSize();

            // Calculate scaling and translation factors
            double scaleX = (double) image.getWidth() / screenSize.width;
            double scaleY = (double) image.getHeight() / screenSize.height;

            // Adjust location for top status bar and navigation bar (iOS only)
            int xOffset = 0;
            int yOffset = 0;
            if (isIOS) {
                double translateX = -driver.findElement(AppiumBy.xpath("//XCUIElementTypeApplication")).getLocation().getX();
                double translateY = -driver.findElement(AppiumBy.xpath("//XCUIElementTypeApplication")).getLocation().getY();

                // Scale and translate element location and size
                location = new Point((int) (location.x * scaleX + translateX), (int) (location.y * scaleY + translateY));
                size = new Dimension((int) (size.width * scaleX), (int) (size.height * scaleY));

                java.awt.Rectangle statusBarRect = new java.awt.Rectangle(0, 0, size.width, (int) (20 * scaleY));
                java.awt.Rectangle navigationBarRect = new java.awt.Rectangle(0, (int) (20 * scaleY), size.width, (int) (44 * scaleY));
                try {
                    if (image.getSubimage(location.x, location.y, 1, 1).getRGB(0, 0) != Color.BLACK.getRGB()) {
                        if (image.getSubimage(statusBarRect.x, statusBarRect.y, 1, 1).getRGB(0, 0) == Color.BLACK.getRGB()) {
                            yOffset += (int) (20 * scaleY);
                        }
                        if (image.getSubimage(navigationBarRect.x, navigationBarRect.y, 1, 1).getRGB(0, 0) == Color.BLACK.getRGB()) {
                            yOffset += (int) (44 * scaleY);
                        }
                    }
                } catch (Exception e) {
                    //ignore
                }
            }

            // Draw circle around element
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.RED);
            graphics.setStroke(new BasicStroke((float) (3 * scaleX)));
            graphics.drawRect(location.x, location.y + yOffset, size.width, size.height);
            graphics.dispose();

            // Save screenshot to file
            ImageIO.write(image, "png", srcFile);
            UUID uuid = UUID.randomUUID();
            String imgPath = "img/" + uuid + "_" + ".PNG";
            FileUtils.moveFile(srcFile, new File(Constants.EXTENT_REPORT_DIR + imgPath));
            test.pass(message);
            test.pass(message, MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
        } catch (IOException e) {
            //ignore
        }
        log.info(message);
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

        int xTimes = xSwipes.length > 0 ? xSwipes[0] : 1;
        try {
            // init screen variables
            Dimension dims = driver.manage().window().getSize();

            int startX = dims.width / 2;
            int startY = dims.height / 2;
            int endX = dims.width / 2;
            int endY = dims.height / 2;

            switch (dir) {
                case DOWN: // center of footer
                    endY = (int) (dims.height * 0.7);
                    break;
                case UP: // bottom of header
                    startY = (int) (dims.height / 1.4);
                    break;
                case LEFT: // center of left side
                    endX = (int) (dims.width * 1.2);
                    break;
                case RIGHT: // center of right side
                    startX = dims.width / 3;
                    break;
                default:
                    log.error("swipe() - dir: '" + dir + "' NOT supported");
            }

            // execute swipe using TouchAction
            for (int x = 0; x < xTimes; x++) {

                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence scroll = new Sequence(finger, 0);
                scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
                scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                scroll.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY));
                scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(List.of(scroll));

                sleep(2);

            }
        } catch (Exception e) {
            log.error("failed to perform swipe actions");
        }
    }

    @SuppressWarnings("unchecked")
    public void activateApp(String bundleId) {
        if (isIOS) {
//            ((IOSDriver) driver).activateApp(bundleId);
//      HashMap<String, String> args = new HashMap<>();
//      args.put("bundleId", bundleId);
            driver.executeScript("mobile: activateApp", ImmutableMap.of("bundleId", bundleId));
        }
    }

    public String getIOSActiveAppInfo() {
        String activeApp = "";
        try {
            String jsonResponse = driver.executeScript("mobile: activeAppInfo").toString();
            activeApp = (jsonResponse.split("bundleId=")[1]).replaceAll("}", "");
        } catch (Exception e) {
            //ignore
        }
        log.info("Active-app :" + activeApp);
        return activeApp;
    }

    @SuppressWarnings("unchecked")
    public void terminateApp(String bundleId) {
//        boolean isAndroid = driver instanceof AndroidDriver;
        if (isIOS) {
//      driver.terminateApp(bundleId);
//            HashMap<String, String> args = new HashMap<>();
//            args.put("bundleId", bundleId);
            driver.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", bundleId));
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
    public void openDeeplink(String deepLinkUrl) {
        HashMap<String, String> event = new HashMap<>();
        event.put("url", deepLinkUrl);
        if (isAndroid) {
            String packageId = driver.getCapabilities()
                    .getCapability("appPackage").toString();

            event.put("package", packageId);
        } else {
            String bundleId = driver.getCapabilities()
                    .getCapability("bundleId").toString();
            event.put("bundleId", bundleId);

        }
        driver.executeScript("mobile: deepLink", event);
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
            } else {
                By done_btn = AppiumBy.iOSClassChain(
                        "**/XCUIElementTypeButton[`name IN {'Done', 'next:', 'Next:', 'Next'}`]");

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

    public synchronized void imageGraphicScreenshot(int x, int y) {

        int width = 50;
        int height = 50;

        String imgPath = new ScreenshotManager(driver).getScreenshot();

//        File screenShot = new File(Constants.NO_SCREENSHOTS_AVAILABLE);
        try {
            File screenShot = new File(Constants.EXTENT_REPORT_DIR + imgPath);

            if (isAndroid) {
                // Crop the entire page screenshot to get only element screenshot
                BufferedImage fullImg = ImageIO.read(screenShot);

                Graphics2D g2d = fullImg.createGraphics();
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(5));

                // g2d.translate(x, y);

                g2d.drawRect(x, y, width, height);
                g2d.dispose();

                ImageIO.write(fullImg, "png", screenShot);
            }

            // Copy the element screenshot to disk
//            FileUtils.moveFile(screenShot, new File(Constants.SCREENSHOTS_DIRECTORY + imgPath));
            test.pass("Tapped on ", MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
            Allure.addAttachment("screenshot", new ByteArrayInputStream(Files.readAllBytes(Paths.get(Constants.EXTENT_REPORT_DIR + imgPath))));
        } catch (Exception e) {
            log.error("draw screenshot service failed!!!");
        }
    }

    /*
     * provide Element Center x and y coordinates
     */
    public void tapByCoordinates(int X, int Y) {
        log.info("x : '" + X + "' ;y : '" + Y + "'");
        try {
            if (isAndroid)
                imageGraphicScreenshot(X, Y);
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), X, Y));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(List.of(tap));
        } catch (Exception e) {
            log.error("failed to tap at coordinates : " + "x : '" + X + "' ;y : '" + Y + "'");
        }
    }

    public void swipeByCoordinates(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 0);
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(scroll));
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

    /*
     * Double tap
     */
    public void doubleTapByElement(WebElement element, String action) {
        if (action.length() > 4)
            takeScreenshotAndDraw(element, action);
        new Actions(driver).moveToElement(element).doubleClick().perform();
    }

    public void takeScreenshotAndDraw(WebElement element, String message) {
        // Take screenshot
        try {
            String imgPath = new ScreenshotManager(driver).getScreenshot();
            File srcFile = new File(Constants.EXTENT_REPORT_DIR + imgPath);
//                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage image = ImageIO.read(srcFile);

            // Get element location and size
//            WebElement element = driver.findElement(by);
            Point location = element.getLocation();
            Dimension size = element.getSize();

            // Get screen size
            Dimension screenSize = driver.manage().window().getSize();

            // Calculate scaling and translation factors
            double scaleX = (double) image.getWidth() / screenSize.width;
            double scaleY = (double) image.getHeight() / screenSize.height;

            // Adjust location for top status bar and navigation bar (iOS only)
            int xOffset = 0;
            int yOffset = 0;
            if (isIOS) {
                double translateX = -driver.findElement(AppiumBy.xpath("//XCUIElementTypeApplication")).getLocation().getX();
                double translateY = -driver.findElement(AppiumBy.xpath("//XCUIElementTypeApplication")).getLocation().getY();

                // Scale and translate element location and size
                location = new Point((int) (location.x * scaleX + translateX), (int) (location.y * scaleY + translateY));
                size = new Dimension((int) (size.width * scaleX), (int) (size.height * scaleY));

                Rectangle statusBarRect = new Rectangle(0, 0, size.width, (int) (20 * scaleY));
                Rectangle navigationBarRect = new Rectangle(0, (int) (20 * scaleY), size.width, (int) (44 * scaleY));
                try {
                    if (image.getSubimage(location.x, location.y, 1, 1).getRGB(0, 0) != Color.BLACK.getRGB()) {
                        if (image.getSubimage(statusBarRect.x, statusBarRect.y, 1, 1).getRGB(0, 0) == Color.BLACK.getRGB()) {
                            yOffset += (int) (20 * scaleY);
                        }
                        if (image.getSubimage(navigationBarRect.x, navigationBarRect.y, 1, 1).getRGB(0, 0) == Color.BLACK.getRGB()) {
                            yOffset += (int) (44 * scaleY);
                        }
                    }
                } catch (Exception e) {
                    //ignore
                }
            }

            // Draw circle around element
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.RED);
            graphics.setStroke(new BasicStroke((float) (5 * scaleX)));
            graphics.drawRect(location.x, location.y + yOffset, size.width, size.height);
            graphics.dispose();

            // Save screenshot to file
            ImageIO.write(image, "png", srcFile);
            UUID uuid = UUID.randomUUID();
            String modImgPath = "img/" + uuid + "_" + ".PNG";
            FileUtils.moveFile(srcFile, new File(Constants.EXTENT_REPORT_DIR + modImgPath));
            test.pass(message)
                    .pass(message, MediaEntityBuilder.createScreenCaptureFromPath(modImgPath).build());

            Allure.step((message), (step) -> {
                Allure.addAttachment("screenshot", new ByteArrayInputStream(Files.readAllBytes(Paths.get(Constants.EXTENT_REPORT_DIR + modImgPath))));
            });
        } catch (Exception e) {
            test.pass(message);
        }
        log.info(message);
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

    public void scrollDowntoFind(By by, int... xScroll) {
        int xSwipes = xScroll.length > 0 ? xScroll[0] : 8;
        for (int i = 0; i < xSwipes; i++) {
            try {
                if (isElementDisplayed(by) && isElementDisplayedOnScreen(driver.findElement(by))) {
                    break;
                } else {
                    swipe(Direction.UP);
                }
            } catch (Exception e) {
                swipe(Direction.UP);
            }
        }
    }

    public void scrollUptoFind(By by, int... xScroll) {
        int xSwipes = xScroll.length > 0 ? xScroll[0] : 5;

        for (int i = 0; i < xSwipes; i++) {
            try {

                if (isElementDisplayed(by)) {
                    break;
                } else {
                    swipe(Direction.DOWN);
                }
            } catch (Exception e) {
                swipe(Direction.DOWN);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void toggleWifiOFF() {
        if (isIOS) {
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
    public void toggleWifiON() {
        if (isIOS) {
            try {
                // Turn-ON wifi
                activateApp("com.apple.Preferences");
                driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name=='Wifi ON'`]")).click();
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
