package com.Utilities;

import com.DataManager.TestDataManager;
import com.ReportManager.LoggerManager;
import com.Utilities.Constants;
import com.base.AppiumDriverManager;
import com.base.Log;
import io.appium.java_client.AppiumDriver;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.log4j.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class ScreenShotManager {

  private static Logger log = Logger.getLogger(ScreenShotManager.class.getName());


  public synchronized String getScreenshot() {

    AppiumDriver driver = AppiumDriverManager.getDriverInstance();

    File ScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

    UUID uuid = UUID.randomUUID();

    String imgPath = "img/" + uuid + "_" + Constants.TIME_NOW + ".PNG";

    File filePath = new File(Constants.REPORT_DIR + imgPath);

    try {
      FileUtils.moveFile(ScreenShot, filePath);
    } catch (IOException e) {
      log.error("Screenshot not Found!!!");
    }
    return imgPath;
  }

}
