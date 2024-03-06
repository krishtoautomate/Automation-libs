package com.base;


import com.Utilities.Constants;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by Krish on 21.05.2018.
 */
public class OptionsManager {

    private static Logger log = Logger.getLogger(CapabilitiesManager.class.getName());

    // Get Chrome Options
    public ChromeOptions getChromeOptions() {

        ChromeOptions chromeOptions = new ChromeOptions();

        try {
            String content = new String(Files.readAllBytes(Paths.get(Constants.CAPABILITIES)));
            // Create a JSONObject from the file content
            JSONObject obj = (JSONObject) new JSONParser().parse(content);

            //get CHROME capabilities
            JSONObject chromeCapabilities = (JSONObject) obj.get("CHROME");

            // Access the nested "goog:chromeOptions" JSONObject
            JSONObject chromeOptionsObj = (JSONObject) chromeCapabilities.get("goog:chromeOptions");
            // Get the "args" JSONArray
            JSONArray argsArray = (JSONArray) chromeOptionsObj.get("args");

            // Print each Chrome Option Arg
            System.out.println("Chrome Options Args: "+ argsArray);
            for (int i = 0; i < argsArray.size(); i++) {
                chromeOptions.addArguments(argsArray.get(i).toString());
            }
        } catch (Exception e) {
            log.error("failed to add Chrome Options Args");
        }


//        options.addArguments("--start-maximized");
//        options.addArguments("--ignore-certificate-errors");
////        options.addArguments("--disable-popup-blocking");
//        options.addArguments("--incognito");
//        options.addArguments("user-agent=GomezAgent");
//        options.addArguments("disable-infobars");
//        options.addArguments("--remote-allow-origins=*");

//        options.addArguments("--host-rules=MAP *.google.com 127.0.0.1, MAP *.amazon.com 127.0.0.1, MAP *.analytics.tiktok.com 127.0.0.1");

//        options.addArguments("--host-resolver-rules=MAP analytics.tiktok.com 127.0.0.1");
//        options.addArguments("--host-resolver-rules=Exclude analytics.tiktok.com 127.0.0.1");

//        String proxyadd = "fastweb.int.bell.ca";
//        Proxy proxy = new Proxy();
//        proxy.setSocksProxy("socks5h://fastweb.int.bell.ca:1080");

//        proxy.setHttpProxy(proxyadd);
//        proxy.setSslProxy(proxyadd);
//        options.setCapability("proxy", proxy);

//        chromeOptions.addArguments("--proxy-server=http://fastweb.int.bell.ca:8083");//working
//        options.addArguments("--proxy-server=http://fastweb.int.bell.ca:8083");

//        options.setAcceptInsecureCerts(true);//it is a capability
//        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

//        options.merge(capabilities);//not working


        return chromeOptions;
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
