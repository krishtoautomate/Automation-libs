package other.testcases;

import com.Utilities.ITestBase;
import com.Utilities.MobileActions;
import com.aventstack.extentreports.Status;
import com.base.TestBase;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import other.pages.TestFlightApp;

import static io.qameta.allure.SeverityLevel.CRITICAL;

public class TestFlight extends TestBase implements ITestBase {

    String className = this.getClass().getSimpleName();


//    @Issue("MAEAUTO-xyz")
    @TmsLink( value="MAEAUTO-xyz")
    @Description("This test updates apps from Testflight")
    @Severity(CRITICAL)
    @Owner("Krish")
    @Epic("Testflight")
    @Feature("update")
    @Story("update iOS apps")
    @Test//(retryAnalyzer = com.Listeners.RetryAnalyzer.class)
    public void TestFlightUpdateScript() {

        String udid = driver.getCapabilities().getCapability("udid").toString();
        test.getModel().setName(String.format("%s - %s", className, udid));
        TestFlightApp testFlightApp = new TestFlightApp(driver, test);
        MobileActions mobileActions = new MobileActions(driver, test);

        // dismiss open prompt
        testFlightApp.dismissAlert();

        mobileActions.resetApp();

        testFlightApp.dismissAlert();

        if (testFlightApp.verify_notNow_btn()) {
            testFlightApp.get_notNow_btn("Not Now Button - is Clicked").click();
        }

        if (!driver.getCapabilities().getCapability("bundleId")
                .toString().contains("com.apple.TestFlight")) {
//            mobileActions.activateApp("com.apple.TestFlight");
            driver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId", "com.apple.TestFlight"));
        }

        if (testFlightApp.verify_tryAgain_btn()) {
            testFlightApp.get_tryAgain_btn().click();
            testFlightApp.logmessage(Status.PASS, "'Try Again' Button - is Clicked");
        }

        if (testFlightApp.verify_notNow_btn()) {
            testFlightApp.get_notNow_btn("Not Now Button - is Clicked").click();
        }

        if (testFlightApp.verify_continue_btn()) {
            testFlightApp.get_continue_btn("Continue Button - is Clicked").click();
        }

        // for ipads
        if (!testFlightApp.verify_apps_h1()) {
            if (testFlightApp.verify_apps_back_btn()) {
                testFlightApp.get_apps_back_btn("back button - is Clicked").click();
            }
        }

        testFlightApp.getPageSource();

        sleep(2);

//        Assertions.contains(testFlightApp.get_apps_h1().getText(), "Apps", true);


        if (testFlightApp.verify_all_btns()) {
            String buttonText = testFlightApp.get_all_btns().get(0).getText();
            testFlightApp.get_all_btns().get(0).click();
            sleep(5);
            testFlightApp.logmessage(Status.PASS, buttonText + " button clicked");

            if (!testFlightApp.verify_apps_h1()) {
                if (testFlightApp.verify_apps_back_btn()) {
                    testFlightApp.get_apps_back_btn("back button - is Clicked").click();
                }
            }
            sleep(5);
        } else {
            testFlightApp.logmessage(Status.WARNING, "No update buttons found");
        }

        testFlightApp.getPageSource();
    }
}
