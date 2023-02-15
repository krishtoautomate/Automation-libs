package other.pages;

import com.Drivers.LocatorBy;
import com.Utilities.BaseObjs;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TestFlightApp extends BaseObjs<TestFlightApp> {

    // TestFlight
    By continue_btn = AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeButton' AND name IN {'Continue Button','Not Now'}");
    By tryAgain_btn = AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeButton' AND name=='Try Again'");

    By all_btns = AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label IN {'UPDATE','INSTALL'}`]");
    By apps_h1 = LocatorBy.all(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'Apps'"));

    By apps_back_btn = AppiumBy.iOSNsPredicateString(
            "type=='XCUIElementTypeButton' AND name=='BackButton' AND label=='Apps'");

    public TestFlightApp(AppiumDriver driver, ExtentTest test) {
        super(driver, test);
    }

    public boolean verify_continue_btn() {
        return isElementDisplayed(continue_btn);
    }

    public WebElement get_continue_btn() {
        return get_Element(continue_btn, "continue' button");
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

    public WebElement get_apps_back_btn() {
        return get_Element(apps_back_btn, "Back button");
    }

    public WebElement get_apps_h1() {
        return get_Element(apps_h1, "'Apps' title text");
    }

    @SuppressWarnings("unchecked")
    public List<WebElement> get_all_btns() {
        return get_Elements(all_btns, "All buttons");
    }

    public boolean verify_all_btns() {
        return isElementDisplayed(all_btns);
    }

}
