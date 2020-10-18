package other.testcases;

import org.openqa.selenium.By;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.Utilities.ITestBase;
import com.base.TestBase;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;


public class DataON extends TestBase implements ITestBase {
	

	@SuppressWarnings("unchecked")
	@Test
	@Parameters({"platForm"})
	public void Data_ON(@Optional String platForm)  {
		
		 if("ios".equalsIgnoreCase(platForm)) {
			   try {
				//Turn-OFF wifi
				   ((AppiumDriver<MobileElement>) driver).activateApp("com.apple.shortcuts");
					
				   driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Data ON']")).click();
				   log.info("Data ON"); 
				   
				 //Restart app
				   ((AppiumDriver<MobileElement>) driver).resetApp();
				   
				   log.info("App Restarted");
				} catch (Exception e) {
					// ignore
				}
		   }
		
		
	}
}
