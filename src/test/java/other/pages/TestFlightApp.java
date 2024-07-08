package other.pages;

import com.Utilities.BaseObjs;
import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

public class TestFlightApp extends BaseObjs<TestFlightApp> {

    // TestFlight
    By continue_btn = AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeButton' AND name IN {'Continue','Continue Button'}");

    By notNow_btn = AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Not Now'`]");

    By tryAgain_btn = AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeButton' AND name=='Try Again'");

    By all_btns = AppiumBy.iOSClassChain("**/XCUIElementTypeButton");
    By apps_h1 = AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Apps'`]");

    By apps_back_btn = AppiumBy.iOSClassChain("**/XCUIElementTypeNavigationBar/XCUIElementTypeButton[`name IN {'BackButton', 'ToggleSidebar'}`]");

    public TestFlightApp(AppiumDriver driver, ExtentTest test) {
        super(driver, test);
    }

    public boolean verify_continue_btn() {
        return isElementDisplayed(continue_btn);
    }

    public WebElement get_continue_btn(String action) {
        return get_Element(continue_btn, "continue' button", action);
    }

    public boolean verify_notNow_btn() {
        return isElementDisplayed(notNow_btn);
    }

    public WebElement get_notNow_btn(String action) {
        return get_Element(notNow_btn, "Now now button", action);
    }

    public boolean verify_tryAgain_btn() {
        return isElementDisplayed(tryAgain_btn);
    }

    public WebElement get_tryAgain_btn() {
        return get_Element(tryAgain_btn, "try again button");
    }


    public boolean verify_apps_back_btn() {
        return isElementDisplayed(apps_back_btn);
    }

    public WebElement get_apps_back_btn(String action) {
        return get_Element(apps_back_btn, "Back button", action);
    }

    public WebElement get_apps_h1() {
        return get_Element(apps_h1, "'Apps' title text");
    }

    public boolean verify_apps_h1() {
        return isElementDisplayed(apps_h1);
    }

    @SuppressWarnings("unchecked")
    public List<WebElement> get_all_btns() {
        return get_Elements(all_btns, "All buttons");
    }

    public boolean verify_update_img_btns() throws IOException {
        File img = new File(Constants.RESOURCE_DIR + "/img/update_btn.png");
//        File img1 = new File(Constants.RESOURCE_DIR + "/img/update.png");
        String base64EncodedImageFile = Base64.getEncoder().encodeToString(Files.readAllBytes(img.toPath()));
//        String base64EncodedImageFile1 = Base64.getEncoder().encodeToString(Files.readAllBytes(img1.toPath()));
        By imageLocator = AppiumBy.image(base64EncodedImageFile);
        return isElementDisplayed(imageLocator);
    }

    public List<WebElement> update_img_btns() throws IOException {
        File img = new File(Constants.RESOURCE_DIR + "/img/update_btn.png");
        String base64EncodedImageFile = Base64.getEncoder().encodeToString(Files.readAllBytes(img.toPath()));
        By imageLocator = AppiumBy.image(base64EncodedImageFile);
        return get_Elements(imageLocator, "update button images");
    }

    public boolean verify_all_btns() {
        return isElementDisplayed(all_btns);
    }
}
