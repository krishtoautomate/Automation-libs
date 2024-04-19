package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.MobileActions;
import com.aventstack.extentreports.Status;
import com.base.TestBase;
import org.testng.annotations.Test;
import other.pages.SettingsApp;


public class SetDisplayTimeout extends TestBase implements ITestBase {


    @Test
    public void Set_Display_Timeout() {

        MobileActions mobileActions = new MobileActions(driver, test);
//        Utilities utils = new Utilities(driver, test);

        SettingsApp settingsApp = new SettingsApp(driver, test);

        settingsApp.logmessage(Status.PASS, "--Start--");

        // Turn-OFF wifi
        mobileActions.activateApp("com.apple.Preferences");
        sleep(5);
        mobileActions.terminateApp("com.apple.Preferences");
        sleep(5);
        mobileActions.activateApp("com.apple.Preferences");

        sleep(10);

        settingsApp.logmessage(Status.PASS, "app launch");

        mobileActions.swipe(MobileActions.Direction.UP);

        if(settingsApp.verify_displayCell()) {
            settingsApp.get_displayCell().click();
            settingsApp.logmessage(Status.PASS, "'Display & Brightness' button is clicked");
        }

        mobileActions.swipe(MobileActions.Direction.UP, 2);

        if(settingsApp.verify_autoLockCell()) {
            settingsApp.get_autoLockCell().click();
            settingsApp.logmessage(Status.PASS, "'Auto-Lock' button is clicked");
        }

        sleep(4);

        settingsApp.get_5minutes().click();
        settingsApp.logmessage(Status.PASS, "'5 minutes' button is clicked");

        sleep(1);

        mobileActions.terminateApp("com.apple.preferences");
        settingsApp.logmessage(Status.PASS, "'close app'");

    }
}
