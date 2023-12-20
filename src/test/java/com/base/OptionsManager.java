package com.base;


import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
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
//        options.addArguments("--proxy-server=http://fastweb-on.bell.corp.bce.ca:80");//working
//        options.addArguments("--proxy-server=http://fastweb.int.bell.ca:8083");

        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        options.merge(capabilities);

        return options;
    }

    public FirefoxOptions getFirefoxOptions(DesiredCapabilities capabilities) {

        FirefoxOptions options = new FirefoxOptions();
//    options.addArguments("--start-maximized");
//    options.addArguments("--ignore-certificate-errors");
//    options.addArguments("--disable-popup-blocking");
//    options.addArguments("--incognito");
//    options.addArguments("user-agent=GomezAgent");
//    options.addArguments("disable-infobars");
//    options.addArguments("--remote-allow-origins=*");

        options.addArguments("-private");

        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        options.merge(capabilities);

        return options;
    }

}
