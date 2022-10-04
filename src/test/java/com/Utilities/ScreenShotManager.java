package com.Utilities;

import com.ReportManager.LoggerManager;
import com.base.AppiumDriverManager;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

public class ScreenShotManager {

    private static final Logger log = LoggerManager.getLogger();

    public static synchronized String getScreenshot() {

        AppiumDriver driver = AppiumDriverManager.getDriverInstance();

        File ScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        UUID uuid = UUID.randomUUID();

        String imgPath = "img/" + uuid + "_" + Constants.TIME_NOW + ".PNG";

        File filePath = new File(Constants.REPORT_DIR + imgPath);

        try {
            FileUtils.moveFile(ScreenShot, filePath);
        } catch (IOException e) {
            log.severe("ScreenShot service failed!!!");
        }
        return imgPath;
    }

}
