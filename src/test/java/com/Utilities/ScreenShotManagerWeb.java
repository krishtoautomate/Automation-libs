package com.Utilities;

import com.base.DriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ScreenShotManagerWeb {

    private static Logger log = Logger.getLogger(ScreenShotManagerWeb.class.getName());

    public static synchronized String getScreenshot() {

        WebDriver driver = DriverManager.getDriverInstance("Web");

        File ScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        UUID uuid = UUID.randomUUID();

        String imgPath = "img/" + uuid + "_" + Constants.TIME_NOW + ".PNG";

        File filePath = new File(Constants.REPORT_DIR + imgPath);

        try {
            FileUtils.moveFile(ScreenShot, filePath);
        } catch (IOException e) {
            log.error("ScreenShot service failed!!!");
        }
        return imgPath;
    }

}
