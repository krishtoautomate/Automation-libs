package other.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.Utilities.BaseObjs;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class PlayStoreApp extends BaseObjs<PlayStoreApp>{
	
	By navigate_btn = By.xpath("//android.view.ViewGroup/android.widget.FrameLayout/android.widget.ImageView[contains(@resource-id,'id/navigation_button')  or contains(@resource-id,'id/main_nav_item') or contains(@resource-id,'id/0_resource_name_obfuscated')]");
	By myApps_and_games_btn = By.xpath("//*[contains(@text,'My apps & games')]");
	
	By updates_refresh_btn = By.xpath("//android.widget.ImageView[contains(@resource-id, 'updates_refresh_button')]");
	By update_btn = By.xpath("//android.widget.Button[contains(@text,'UPDATE')]");
	By updateAll_btn = By.xpath("//android.widget.Button[contains(@text,'UPDATE ALL') or contains(@text,'Update all')]"); 
	
	
	public PlayStoreApp(WebDriver driver, Logger log, ExtentTest test) {
		super(driver, log, test);		
	}
	
	public WebElement get_navigate_btn() {
		WebElement ele = null;
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(navigate_btn));
			ele = driver.findElement(navigate_btn);
		} catch (Exception e) {
			logmessage(Status.FAIL, "'navigate' button -  Not Found in 'PlayStore' app");
			Assert.fail("'navigate' button -  Not Found in 'PlayStore' app");
		}
		return ele;
	}
	
	public WebElement get_myApps_and_games_btn() {
		WebElement ele = null;
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(myApps_and_games_btn));
			ele = driver.findElement(myApps_and_games_btn);
		} catch (Exception e) {
			logmessage(Status.FAIL, "'My apps & games' button -  Not Found in 'PlayStore' app");
			Assert.fail("'My apps & games' button -  Not Found in 'PlayStore' app");
		}
		return ele;
	}
	
	public WebElement get_updates_refresh_btn() {
		WebElement ele = null;
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(updates_refresh_btn));
			ele = driver.findElement(updates_refresh_btn);
		} catch (Exception e) {
			//ignore
		}
		return ele;
	}
	
	public WebElement get_update_btn() {
		WebElement ele = null;
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(update_btn));
			ele = driver.findElement(update_btn);
		} catch (Exception e) {
			//ignore
		}
		return ele;
	}
	
	public WebElement get_updateAll_btn() {
		WebElement ele = null;
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(updateAll_btn));
			ele = driver.findElement(updateAll_btn);
		} catch (Exception e) {
			//ignore
		}
		return ele;
	}
	
}
