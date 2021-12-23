package com.base;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.Utilities.Constants;

public class ScreenShotManager {

  private WebDriver driver;

  private static final Logger log = LoggerFactory.getLogger(Class.class.getName());


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
