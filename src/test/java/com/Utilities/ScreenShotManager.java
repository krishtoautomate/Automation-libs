package com.Utilities;

import com.Utilities.Constants;
import com.base.AppiumDriverManager;
import io.appium.java_client.AppiumDriver;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenShotManager {

  private static final Logger log = LoggerFactory.getLogger(Class.class.getName());

  AppiumDriver driver = AppiumDriverManager.getDriverInstance();

  public synchronized String getScreenshot() {

    File ScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

    UUID uuid = UUID.randomUUID();

    String imgPath = "img/" + uuid.toString() + "_" + Constants.TIME_NOW + ".PNG";

    File filePath = new File(Constants.REPORT_DIR + imgPath);

    try {
      FileUtils.moveFile(ScreenShot, filePath);
    } catch (IOException e) {
      log.error("screenShot not Found!!!");
    }
    return imgPath;
  }

}
