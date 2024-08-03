package other.testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AIWebTest {


    public static void main(String[] args) throws MalformedURLException, InterruptedException {

        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--no-sandbox");
//        chromeOptions.addArguments("--disable-dev-shm-usage");
        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://bautomation:8085"), chromeOptions);
//        SelfHealingDriver driver = SelfHealingDriver.create(delegate);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://www.google.com");

        driver.findElement(By.xpath("//*[@id='APjFqb']")).isDisplayed();

        Thread.sleep(5000);


        driver.quit();
//        delegate.quit();
    }

}
