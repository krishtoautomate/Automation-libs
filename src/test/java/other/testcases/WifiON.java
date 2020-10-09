package other.testcases;

import org.openqa.selenium.By;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.base.TestBase;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;


public class WifiON extends TestBase{
	

	@SuppressWarnings("unchecked")
	@Test
	@Parameters({"platForm"})
	public void Wifi_ON(@Optional String platForm)  {
		
		 if("ios".equalsIgnoreCase(platForm)) {
			   try {
				   //Turn-OFF WIFI
				   ((AppiumDriver<MobileElement>) driver).activateApp("com.apple.shortcuts");
					
				   driver.findElement(By.xpath("//XCUIElementTypeCell[@name='Wifi ON']")).click();
				   log.info("WIFI ON"); 
				   
				   //Restart app
				   ((AppiumDriver<MobileElement>) driver).resetApp();
				   
				   log.info("App Restarted");
				} catch (Exception e) {
					// ignore
				}
		   }
		
		
	}
}
