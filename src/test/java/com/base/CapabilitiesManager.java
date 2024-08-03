package com.base;
/**
 * Created by Krish on 21.07.2018.
 **/

import com.DataManager.TestDataManager;
import com.Utilities.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Krish on 21.01.2019.
 */
public class CapabilitiesManager {

    static Map<Long, JSONObject> capabilitiesMap = new HashMap<Long, JSONObject>();
    private static Logger log = Logger.getLogger(CapabilitiesManager.class.getName());



    public static synchronized DesiredCapabilities getCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        JSONObject jObj = capabilitiesMap.get(Thread.currentThread().getId());

        System.out.println("capabilities : " + jObj.toJSONString());
        for (Object key : jObj.keySet()) {
            if (key.toString().equalsIgnoreCase("platformVersion")) {
                continue;
            }
            capabilities.setCapability(key.toString(), jObj.get(key));
        }
        return capabilities;
    }

    @SuppressWarnings("unchecked")
    public synchronized DesiredCapabilities setCapabilities(String platForm) {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        ITestResult iTestResult = Reporter.getCurrentTestResult();
        Map<String, String> testParams =
                iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();
        String udid = GlobalMapper.getUdid();
        String testName = GlobalMapper.getTestName();

//        try {
        //** capabilities from capabilities.json
        JSONObject jObj;
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(Constants.CAPABILITIES));
            jObj = (JSONObject) jsonObject.get(platForm.toUpperCase());
        } catch (Exception e) {
            log.error("capabilities.json file is missing!");
            throw new RuntimeException(e);
        }

        if (testName != null)
            jObj.put("auto:testName", testName);
        if (udid != null)
            jObj.put("appium:udid", udid.trim());

        try {
//            if (StringUtils.containsIgnoreCase(platForm, "Android") | StringUtils.containsIgnoreCase(platForm,"iOS")) {
                //*** capabilities from TestNG.xml
                String pCapabilities = testParams.get("capabilities");
                if (pCapabilities != null) {
                    pCapabilities = pCapabilities.replaceAll("'", "\"");
                    JSONObject jObject = (JSONObject) new JSONParser().parse(pCapabilities);
                    for (Object keyStr : jObject.keySet()) {
                        try {
                            jObj.put(keyStr, jObject.get(keyStr));
                        } catch (Exception e) {
                            //ignore
                        }
                    }
                }
//            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.warn("no test capabilities found");
        }

        //*** capabilities from TestData
        try {
            String pTestData = testParams.get("p_Testdata");
            JSONObject jsObject = (JSONObject)(new JSONParser()).parse(new FileReader(pTestData));
            String className = iTestResult.getInstanceName();
            org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray)jsObject.get(className);
            int index = "iOS".equalsIgnoreCase(platForm) ? 1 : 0;
            JSONObject innerObj = (JSONObject)jsonArray.get(index);
            JSONObject jAObject = (JSONObject) innerObj.get("capabilities");

            jAObject.keySet().forEach(key -> {
                jObj.put(key,  jAObject.get(key));
            });
        } catch (Exception e) {
            //ignore
            log.warn("no test data capabilities found");
        }

        capabilitiesMap.put(Thread.currentThread().getId(), jObj);

        try {
            //add to capabilities
            System.out.println("capabilities : " + jObj.toJSONString());
            for (Object key : jObj.keySet()) {
                if(key.toString().equalsIgnoreCase("goog:chromeOptions")){
                    continue;
                }

                if (key.toString().equalsIgnoreCase("platformVersion")) {
                    continue;
                }
                //remove app capabilities for ios
                try {
                    if (StringUtils.containsIgnoreCase("iOS", platForm)) {
                        if (key.toString().equalsIgnoreCase("browserName")) {
                            capabilities.setCapability("bundleId", "");
                        }
                    }
                } catch (Exception e) {
                    //ignore
                }
                //remove app capabilities for Android
                try {
                    if (StringUtils.containsIgnoreCase("Android", platForm)) {
                        if (key.toString().equalsIgnoreCase("browserName") | key.toString().equalsIgnoreCase("appium:browserName")) {
                            capabilities.setCapability("appPackage", "");
                            capabilities.setCapability("appActivity", "");
                        }
                    }
                } catch (Exception e) {
                    //ignore
                }
                capabilities.setCapability(key.toString(), jObj.get(key));
            }
        } catch (Exception e) {
            log.error("error in capabilities.json");
        }

        return capabilities;
    }

}
