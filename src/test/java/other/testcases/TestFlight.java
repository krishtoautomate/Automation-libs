package other.testcases;

import com.Utilities.Assertions;
import com.Utilities.ITestBase;
import com.Utilities.MobileActions;
import com.Utilities.Utilities;
import com.aventstack.extentreports.Status;
import com.base.TestBase;
import org.testng.annotations.Test;
import other.pages.TestFlightApp;

public class TestFlight extends TestBase implements ITestBase {

    String className = this.getClass().getSimpleName();


    @Test//(retryAnalyzer = com.Listeners.RetryAnalyzer.class)
    public void TestFlightUpdateScript() {

        String udid = driver.getCapabilities().getCapability("udid").toString();
        test.getModel().setName(String.format("%s - %s", className, udid));
        Utilities utils = new Utilities(driver, test);
        TestFlightApp testFlightApp = new TestFlightApp(driver, test);
        MobileActions mobileActions = new MobileActions(driver, test);

        // dismiss open prompt
        utils.dismissAlert();

        mobileActions.resetApp();

        utils.dismissAlert();

        if (testFlightApp.verify_notNow_btn()) {
            testFlightApp.get_notNow_btn().click();
            utils.logmessage(Status.PASS, "Not Now Button - is Clicked");
        }

        if (!driver.getCapabilities().getCapability("bundleId")
                .toString().contains("com.apple.TestFlight")) {
            mobileActions.activateApp("com.apple.TestFlight");
        }

        if (testFlightApp.verify_tryAgain_btn()) {
            testFlightApp.get_tryAgain_btn().click();
            utils.logmessage(Status.PASS, "'Try Again' Button - is Clicked");
        }

        if (testFlightApp.verify_notNow_btn()) {
            testFlightApp.get_notNow_btn().click();
            utils.logmessage(Status.PASS, "Not Now Button - is Clicked");
        }

        if (testFlightApp.verify_continue_btn()) {
            testFlightApp.get_continue_btn().click();
            utils.logmessage(Status.PASS, "Continue Button - is Clicked");
        }

        // for ipads
        if (!testFlightApp.verify_apps_h1()) {
            if (testFlightApp.verify_apps_back_btn()) {
                testFlightApp.get_apps_back_btn().click();
                utils.logmessage(Status.PASS, "back button - is Clicked");
            }
        }

        utils.getPageSource();

        Assertions.contains(testFlightApp.get_apps_h1().getText(), "Apps", true);

        if (testFlightApp.verify_all_btns()) {
            String buttonText = testFlightApp.get_all_btns().get(0).getText();
            testFlightApp.get_all_btns().get(0).click();
            sleep(5);
            utils.logmessage(Status.PASS, buttonText + " button clicked");

            if (!testFlightApp.verify_apps_h1()) {
                if (testFlightApp.verify_apps_back_btn()) {
                    testFlightApp.get_apps_back_btn().click();
                    utils.logmessage(Status.PASS, "back button - is Clicked");
                }
            }
        }

        sleep(10);

        utils.getPageSource();

        sleep(30);


//        ((IOSDriver)driver).activateApp("com.apple.mobilesafari");
//
//        sleep(5);
//
//        utils.logmessage(Status.PASS, "safari launched");
//
//
//
//        driver.getPageSource();
//
//        Set<String> contextNames = ((SupportsContextSwitching) driver).getContextHandles();
//
//        System.out.println("contextNames : "+ contextNames);
//
//        sleep(10);
//
//        contextNames = ((SupportsContextSwitching) driver).getContextHandles();
//
//        System.out.println("contextNames reload : "+ contextNames);
//
//        sleep(10);


//        System.out.println("contextNames : "+ driver.executeScript("mobile:getContexts"));
//
//        sleep(10);
//
//        System.out.println("contextNames : "+ driver.executeScript("mobile:getContexts"));


    }
}
