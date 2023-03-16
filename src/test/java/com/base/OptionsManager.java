package com.base;


import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by Krish on 21.05.2018.
 */
public class OptionsManager {

  // Get Chrome Options
  public ChromeOptions getChromeOptions(DesiredCapabilities capabilities) {

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized");
    options.addArguments("--ignore-certificate-errors");
    options.addArguments("--disable-popup-blocking");
    options.addArguments("--incognito");
    options.addArguments("user-agent=GomezAgent");
    options.addArguments("disable-infobars");
    options.addArguments("--remote-allow-origins=*");

    options.setAcceptInsecureCerts(true);
    options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
    options.merge(capabilities);

    return options;
  }

}
