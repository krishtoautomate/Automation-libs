package other.testcases;

import org.testng.annotations.Test;

import com.Utilities.Utilities;
import com.aventstack.extentreports.Status;
import com.base.TestBase;

import other.pages.PlayStoreApp;


public class PlayStore extends TestBase{
	

	@Test
	public void PlaystoreUpdateScript() {
		
		Utilities utils = new Utilities(driver, log, test);
		PlayStoreApp playstoreapp = new PlayStoreApp(driver, log, test);
		
		utils.sleep(2);
		
		//1.0 - 
		if(utils.isElementDisplayed(playstoreapp.get_updates_refresh_btn())) {
			playstoreapp.get_updates_refresh_btn().click();
			utils.logmessage(Status.PASS, "'updates refresh Button' is clicked");
		}
		
		//2.0 - Click navigate button
		playstoreapp.get_navigate_btn().click();
		playstoreapp.get_navigate_btn().click();
		utils.logmessage(Status.PASS, "Left Navigate Button clicked");
		
		//3.0 - Click 'My apps & games' link
		playstoreapp.get_myApps_and_games_btn().click();
		utils.logmessage(Status.PASS, "'My apps & games' link clicked");
		
		//4.0 - Click Update All button
		if(utils.isElementDisplayed(playstoreapp.get_updateAll_btn())) {
			playstoreapp.get_updateAll_btn().click();
			utils.sleep(2);
			utils.logmessage(Status.PASS, "'UPDATE ALL' button clicked");
		}else {
			utils.logmessage(Status.PASS, "'UPDATE ALL' button is NOT FOUND");
		}
		
		
	}
}
