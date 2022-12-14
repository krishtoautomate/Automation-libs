package other.pages;

import com.Driver.MobileBy;
import com.Utilities.BaseObjs;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SettingsApp extends BaseObjs<SettingsApp> {



    public SettingsApp(AppiumDriver driver, ExtentTest test) {
        super(driver, test);
    }

    By displayCell = AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label CONTAINS 'Display'`]");

    By autoLockCell = AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`label == 'Auto-Lock'`]");

    By minutes = AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`label IN {'5 Minutes','5 minutes'}`]");

    public boolean verify_displayCell(){
        return isElementDisplayed(displayCell);
    }

    public WebElement get_displayCell(){
        return get_Element(displayCell, "Display Cell");
    }

    public boolean verify_autoLockCell(){
        return isElementDisplayed(autoLockCell);
    }

    public WebElement get_autoLockCell(){
        return get_Element(autoLockCell, "Auto-lock Cell");
    }

    public WebElement get_5minutes(){
        return get_Element(minutes, "5 min option");
    }


}
