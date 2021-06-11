package com.base;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShotManager {

  private WebDriver driver;

  private static Logger log = Logger.getLogger(ScreenShotManager.class.getName());


  public ScreenShotManager(WebDriver driver) {
    this.driver = driver;
  }

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
