package other.testcases;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.Utilities.ITestBase;
import com.Utilities.Utilities;
import com.aventstack.extentreports.Status;
import com.base.TestBase;
import com.mobileActions.MobileActions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import other.pages.TestFlightApp;


public class TestFlight extends TestBase implements ITestBase {
	

	@SuppressWarnings("unchecked")
	@Test
	public void TestFlightUpdateScript()  {
		
		Utilities utils = new Utilities(driver, log, test);
		TestFlightApp testFlightApp = new TestFlightApp(driver, log, test);
		MobileActions mobileActions = new MobileActions(driver, log, test);
		
		
		if(!((AppiumDriver<MobileElement>) driver).getCapabilities().getCapability("bundleId").toString().contains("com.apple.TestFlight"))
			((AppiumDriver<MobileElement>) driver).activateApp("com.apple.TestFlight");
		

//		if(isElementDisplayed(testFlightapp.get_password_inputField())) {
//			testFlightapp.get_password_inputField().sendKeys("Volt123456");
//			utils.logmessage(Status.INFO, "Password - provided");
//			
//			testFlightapp.get_signIn_btn().click();
//			utils.logmessage(Status.INFO, "Signin/OK button is Clicked");	
//		}
		
//		if(isElementDisplayed(testFlightApp.get_update_later_btn())) {
//			testFlightApp.get_update_later_btn().click();
//			utils.logmessage(Status.INFO, "'Update Later' Button - is Clicked");
//		}
//		
//		if(isElementDisplayed(testFlightApp.get_remindMeLater_btn())) {
//			testFlightApp.get_remindMeLater_btn().click();
//			utils.logmessage(Status.INFO, "'Remind Me Later' Button - is Clicked");
//		}
		
//		if(isElementDisplayed(testFlightApp.get_alert())) {
//			testFlightApp.get_notNow_btn().click();
//			utils.logmessage(Status.INFO, "'Not Now' Button - is Clicked");
//		}
		
		if(isElementDisplayed(testFlightApp.get_tryAgain_btn())) {
			testFlightApp.get_tryAgain_btn().click();
			utils.logmessage(Status.INFO, "'Try Again' Button - is Clicked");
		}
		
		if(isElementDisplayed(testFlightApp.get_continue_btn())) {
			testFlightApp.get_continue_btn().click();
			utils.logmessage(Status.INFO, "Continue Button - is Clicked");
		}
		
		//Verify title
		utils.AssertContains(testFlightApp.get_apps_h1().getText(), "Apps");
		
		List<WebElement> all_btns = testFlightApp.get_all_btns();
		
		try {
			log.info("Total 'UPDATE' or 'INSTALL' buttons : " + all_btns.size());
			
			for(int i=0;i<all_btns.size();i++) {
				String button = all_btns.get(i).getText();
				all_btns.get(i).click();
				utils.logmessage(Status.INFO,button + "- button clicked");
			}
			sleep(5);
		} catch (Exception e) {
			log.info("No 'UPDATE' or 'INSTALL' buttons found");
		}
		
		log.info("BUILD_NUMBER : "+getBuildno());
		
		
		
	}
}
