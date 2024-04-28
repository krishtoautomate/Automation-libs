package com.Utilities;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.io.File;
import java.util.UUID;

public class ScreenshotManager {

    private static Logger log = Logger.getLogger(ScreenshotManager.class.getName());
    private WebDriver driver;

    public ScreenshotManager(WebDriver driver) {
        this.driver = driver;
    }

    public String getScreenshot() {

        UUID uuid = UUID.randomUUID();
        UUID uuid1 = UUID.randomUUID();

        String imgPath = "img/" + uuid + uuid1 + ".PNG";

        try {
            File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            File filePath = new File(Constants.EXTENT_REPORT_DIR + imgPath);

            FileUtils.moveFile(screenShot, filePath);
        } catch (Exception e) {
            log.error("screen-shot service failed!!!");
        }
        return imgPath;
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] screenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

}
