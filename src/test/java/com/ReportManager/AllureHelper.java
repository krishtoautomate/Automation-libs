package com.ReportManager;

import com.base.AppiumDriverManager;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class AllureHelper {

    @Step("{stepDescription}")
    public static void logInfo(String stepDescription) {
        // No additional logic is required; the @Step annotation will handle logging the step
    }

    @Step("{stepDescription}")
    public static void logPass(String stepDescription) {
        // No additional logic is required; the @Step annotation will handle logging the step
        Allure.step(stepDescription, Status.PASSED);
        Allure.addAttachment("screenshot", new ByteArrayInputStream(captureScreenshot()));
    }

    @Step("{stepDescription}")
    public static void logFail(String stepDescription, Throwable throwable) {
        Allure.step(stepDescription, Status.FAILED);
        Allure.addAttachment("Failure screenshot", new ByteArrayInputStream(captureScreenshot()));
        Allure.addAttachment("Failure Details", throwable.toString());
    }

    @Step("{stepDescription}")
    public void logWarning(String stepDescription) {
        Allure.step(stepDescription, Status.BROKEN);
        Allure.addAttachment("Warning", stepDescription);
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] captureScreenshot() {
        AppiumDriver driver = AppiumDriverManager.getDriverInstance();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
