package other.testcases;

import com.Utilities.ITestBase;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseWeb;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WebTest extends TestBaseWeb implements ITestBase {

    @Test
    public void Web_Test(){

        String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
        test.getModel().setName(String.format("%s", browserName));

        byte[] credDecoded = Base64.getDecoder().decode("a3Jpc2gucGF2dWx1cjpOZW9sb2FkZXIyODI3JA==");
        String auth = new String(credDecoded, StandardCharsets.UTF_8);
//        driver.get("https://"+auth+"@bca-csr-ui.int.bell.ca/eCareBellCa/BAT");
//
//        sleep(5);

//        driver = new Augmenter().augment(driver);

        //chrome dev tools
        driver.get("https://www.google.com");


//        DevTools devTools = ((HasDevTools) driver).getDevTools();
//        devTools.createSession();

        //file upload POC
//        driver.navigate().to("http://bqatautomation.bell.corp.bce.ca:9000/");
//
        sleep(5);
//
//        driver.findElement(By.tagName("input")).sendKeys("//files//Bell_Shop_NewCommerFlow_UnverifiedFormE2Evalidation.png");
//
//        sleep(5);
//
//        test.pass("web-page loaded : "+driver.getCurrentUrl(), MediaEntityBuilder.createScreenCaptureFromPath(new ScreenShotManager(driver).getScreenshot()).build());

        String errorXML = driver.getPageSource();

//        System.out.println("SessionId : " + ((RemoteWebDriver) driver).getSessionId());

        test.info(MarkupHelper.createCodeBlock(errorXML));

    }
}
