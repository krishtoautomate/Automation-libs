package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.MobileActions;
import com.Utilities.Utilities;
import com.aventstack.extentreports.Status;
import com.base.TestBase;
import io.appium.java_client.AppiumBy;
import org.testng.annotations.Test;


public class SetDisplayTimeout extends TestBase implements ITestBase {


    @Test
    public void Set_Display_Timeout() {

        MobileActions mobileActions = new MobileActions(driver, test);
        Utilities utils = new Utilities(driver, test);

        utils.logmessage(Status.PASS, "Start");

        // Turn-OFF wifi
        mobileActions.activateApp("com.apple.Preferences");

        mobileActions.swipe(MobileActions.Direction.UP);

        driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`label == 'Display & Brightness'`]")).click();
        utils.logmessage(Status.PASS, "'Display & Brightness' button is clicked");

        mobileActions.swipe(MobileActions.Direction.UP, 2);

        driver.findElement(AppiumBy.accessibilityId("Auto-Lock")).click();
        utils.logmessage(Status.PASS, "'Auto-Lock' button is clicked");

        sleep(1);

        driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`label == '5 minutes'`]")).click();
        utils.logmessage(Status.PASS, "'5 minutes' button is clicked");

        sleep(1);

        mobileActions.terminateApp("com.apple.preferences");
        utils.logmessage(Status.PASS, "'close app'");

    }
}
