package com.base;

import com.Utilities.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;


/**
 * Created by Krish on 21.05.2018.
 */

public class TLDriverFactoryWeb {

  private static final Logger log = LoggerFactory.getLogger(Class.class.getName());
  public static Properties prop = new Properties();
  public static InputStream input = null;
  private static OptionsManager optionsManager = new OptionsManager();
  private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
  public Map<Long, WebDriver> driverMap = new ConcurrentHashMap<Long, WebDriver>();

  protected synchronized void setDriver() {
    try {
      input = new FileInputStream(
          System.getProperty("user.dir") + "/src/main/resources/driver.properties");
      prop.load(input);
    } catch (IOException e) {
      log.info("Driver properties file not found - " + e.toString());
    }

    ITestResult iTestResult = Reporter.getCurrentTestResult();
    Map<String, String> testParams =
        iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

    String browser = testParams.get("browser");

    if (browser.equals("chrome")) {
      DesiredCapabilities capabilities = new DesiredCapabilities();
      // identify System O

      // ChromeDriverManager.chromedriver().setup();
      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        System.setProperty("webdriver.chrome.driver",
            Constants.USER_DIR + prop.getProperty("chrome_driver_windows"));
      } else {
        System.setProperty("webdriver.chrome.driver",
            Constants.USER_DIR + prop.getProperty("chrome_driver_mac"));
      }

      // WebDriverManager.chromedriver().setup();

      capabilities.setCapability(ChromeOptions.CAPABILITY, optionsManager.getChromeOptions());

      capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
      capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

      capabilities.setCapability("chrome.switches",
          Arrays.asList("--ignore-certificate-errors" + "," + "--web-security=false" + ","
              + "--ssl-protocol=any" + "," + "--ignore-ssl-errors=true"));

      // tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
      // tlDriver.set(new ChromeDriver(capabilities));

      ChromeOptions options = new ChromeOptions();
      options.merge(capabilities);
      tlDriver.set(new ChromeDriver(options));

      log.info("Chrome started!");
    } else if (browser.equals("ie")) {

      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        System.setProperty("webdriver.ie.driver",
            System.getProperty("user.dir") + prop.getProperty("ieedge_driver_windows"));
      } else {
        System.setProperty("webdriver.ie.driver",
            System.getProperty("user.dir") + prop.getProperty("ieedge_driver_mac"));
      }
      WebDriverManager.iedriver().setup();

      // EdgeOptions options = new EdgeOptions();
      // Capabilities cap = new DesiredCapabilities();

      // options.setCapability("user-agent", "GomezAgent");

      // EdgeProfile
      // For Local Usage
      tlDriver.set(new InternetExplorerDriver());


    }
    driverMap.put(Thread.currentThread().getId(), tlDriver.get());

    this.getDriverInstance().manage().timeouts().implicitlyWait(Constants.IMPLICITLYWAIT, TimeUnit.SECONDS);

  }

  public synchronized WebDriver getDriverInstance() {
    return driverMap.get(Long.valueOf(Thread.currentThread().getId()));
  }

}
