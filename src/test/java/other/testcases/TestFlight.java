package other.testcases;

import com.Utilities.*;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.base.TestBase;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.imagecomparison.SimilarityMatchingOptions;
import io.appium.java_client.imagecomparison.SimilarityMatchingResult;
import io.qameta.allure.*;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import other.pages.TestFlightApp;

import java.io.File;
import java.io.IOException;

import static io.qameta.allure.SeverityLevel.CRITICAL;

public class TestFlight extends TestBase implements ITestBase {

    String className = this.getClass().getSimpleName();


    //    @Issue("MAEAUTO-xyz")
    @TmsLink("MAEAUTO-xyz")
    @Description("This test updates apps from Testflight")
    @Severity(CRITICAL)
    @Owner("Krish")
    @Epic("Testflight")
    @Feature("update")
    @Story("update iOS apps")
    @Test//(retryAnalyzer = com.Listeners.RetryAnalyzer.class)
    public void TestFlightUpdateScript() throws IOException {

        String udid = driver.getCapabilities().getCapability("udid").toString();
        test.getModel().setName(String.format("%s - %s", className, udid));
        TestFlightApp testFlightApp = new TestFlightApp(driver, test);
        MobileActions mobileActions = new MobileActions(driver, test);

        // dismiss open prompt
        testFlightApp.dismissAlert();

        mobileActions.resetApp();

        testFlightApp.dismissAlert();

        if (testFlightApp.verify_notNow_btn())
            testFlightApp.get_notNow_btn("Not Now Button - is Clicked").click();

        if (!driver.getCapabilities().getCapability("bundleId")
                .toString().contains("com.apple.TestFlight")) {
//            mobileActions.activateApp("com.apple.TestFlight");
            driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId", "com.apple.TestFlight"));
        }

        if (testFlightApp.verify_tryAgain_btn()) {
            testFlightApp.get_tryAgain_btn().click();
            testFlightApp.logmessage(Status.PASS, "'Try Again' Button - is Clicked");
        }

        if (testFlightApp.verify_notNow_btn())
            testFlightApp.get_notNow_btn("Not Now Button - is Clicked").click();

        if (testFlightApp.verify_continue_btn())
            testFlightApp.get_continue_btn("Continue Button - is Clicked").click();

        // for ipads
        if (!testFlightApp.verify_apps_h1()) {
            if (testFlightApp.verify_apps_back_btn())
                testFlightApp.get_apps_back_btn("back button - is Clicked").click();
        }

        testFlightApp.getPageSource();

        sleep(2);

        Assertions.contains(testFlightApp.get_apps_h1().getText(), "Apps", true);

        if (testFlightApp.verify_all_btns()) {
            for (WebElement button : testFlightApp.get_all_btns()) {
                String buttonText = button.getText();
                log.info("buttonText : " + buttonText);
                if (buttonText.equalsIgnoreCase("update")) {
                    button.click();
                    sleep(2);
                    testFlightApp.logmessage(Status.PASS, buttonText + " button clicked");
                }

                if (!testFlightApp.verify_apps_h1()) {
                    if (testFlightApp.verify_apps_back_btn()) {
                        testFlightApp.get_apps_back_btn("back button - is Clicked").click();
                    }
                }

                sleep(2);
            }
        } else {
            testFlightApp.logmessage(Status.WARNING, "No update buttons found");
        }


//        driver.setSetting(Setting.IMAGE_MATCH_THRESHOLD, 0.3);
//
//        if (testFlightApp.verify_update_img_btns()) {
//            testFlightApp.update_img_btns().get(0).click();
//            sleep(2);
//            testFlightApp.logmessage(Status.PASS, "UPDATE button clicked");
//
//            if (!testFlightApp.verify_apps_h1()) {
//                if (testFlightApp.verify_apps_back_btn()) {
//                    testFlightApp.get_apps_back_btn("back button - is Clicked").click();
//                }
//            }
//        }
//
//        testFlightApp.getPageSource();

        SimilarityMatchingOptions opts = new SimilarityMatchingOptions();

        opts.withEnabledVisualization();

//        driver.setSetting(Setting.FIX_IMAGE_TEMPLATE_SIZE, true);
//        driver.setSetting(Setting.FIX_IMAGE_FIND_SCREENSHOT_DIMENSIONS, true);

        File basematchImg = new File(Constants.EXTENT_REPORT_DIR +
                "img/5014e0c6-f696-4400-99f4-ce23bb65b653.png");
        File newImg = new File(Constants.EXTENT_REPORT_DIR + new ScreenshotManager(driver).getScreenshot());

        if (!basematchImg.exists()) {
            System.out.println("No base match found for check; capturing baseline instead of checking");
        }

        System.out.println(basematchImg);
        System.out.println(newImg);

        SimilarityMatchingResult res = driver.getImagesSimilarity(basematchImg, newImg, opts);

        System.out.println("matching score : " + res.getScore());

        if (res.getScore() < 0.999) {

            File failViz = new File(Constants.EXTENT_REPORT_DIR + "img/FAIL_img" + ".png");

            res.storeVisualization(failViz);

            test.pass("match score: " + res.getScore(), MediaEntityBuilder.createScreenCaptureFromPath("img/FAIL_img" + ".png").build());
        }
    }
}
