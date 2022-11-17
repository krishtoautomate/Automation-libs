package com.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import com.Utilities.Constants;


/**
 * Created by Krish on 21.05.2018.
 */

public class WebBrowserDriverManager {

  private static OptionsManager optionsManager = new OptionsManager();
  private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
  static Map<Long, WebDriver> driverMap = new ConcurrentHashMap<Long, WebDriver>();

  protected synchronized void setDriver() throws MalformedURLException {

    ITestResult iTestResult = Reporter.getCurrentTestResult();
    Map<String, String> testParams =
        iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

    String browser = testParams.get("browser");

    String REMOTE_HOST =
        testParams.get("REMOTE_HOST") == null ? "localhost" : testParams.get("REMOTE_HOST");

    if (browser.equalsIgnoreCase("chrome")) {
      DesiredCapabilities capabilities = new DesiredCapabilities();

      if ("localhost".equalsIgnoreCase(REMOTE_HOST)) {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
          System.setProperty("webdriver.chrome.driver",
              Constants.USER_DIR + "/src/main/resources/chromedriver.exe");
        } else {
          System.setProperty("webdriver.chrome.driver",
              Constants.USER_DIR + "/src/main/resources/chromedriver");
        }
      }

      capabilities.setCapability(ChromeOptions.CAPABILITY, optionsManager.getChromeOptions());

      capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
      capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

      capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
      capabilities.setCapability(CapabilityType.VERSION, "chrome");

      capabilities.setCapability("chrome.switches",
          Arrays.asList("--ignore-certificate-errors" + "," + "--web-security=false" + ","
              + "--ssl-protocol=any" + "," + "--ignore-ssl-errors=true"));

      ChromeOptions options = new ChromeOptions();
      options.merge(capabilities);

      if ("localhost".equalsIgnoreCase(REMOTE_HOST)) {
        tlDriver.set(new ChromeDriver(options));
      } else {
        tlDriver.set(new RemoteWebDriver(new URL(REMOTE_HOST), options));
      }
    } else if (browser.equalsIgnoreCase("ie")) {


      // EdgeOptions options = new EdgeOptions();
      // Capabilities cap = new DesiredCapabilities();

      // options.setCapability("user-agent", "GomezAgent");

      // EdgeProfile
      // For Local Usage
      tlDriver.set(new InternetExplorerDriver());


    }
    driverMap.put(Thread.currentThread().getId(), tlDriver.get());

    getDriverInstance().manage().timeouts().implicitlyWait(Constants.IMPLICITLYWAIT,
        TimeUnit.SECONDS);

  }

  public static synchronized WebDriver getDriverInstance() {
    return driverMap.get(Long.valueOf(Thread.currentThread().getId()));
  }

  public static synchronized void quit() {
    getDriverInstance().quit();
  }

}
