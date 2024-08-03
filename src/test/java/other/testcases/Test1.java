package other.testcases;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Test1 {


    public static void main(String[] args) throws MalformedURLException {


        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("browserName", "chrome");


        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--start-maximized");
//        options.addArguments("--ignore-certificate-errors");
//        options.addArguments("--disable-popup-blocking");
//        options.addArguments("--incognito");
//        options.addArguments("disable-infobars");
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--proxy-server=http://:80");

        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);


        WebDriver driver = new RemoteWebDriver(new URL("http://172.21.34.239:4444"), options);

        driver.navigate().to("https://www.google.com/");

        driver.quit();


    }

}
