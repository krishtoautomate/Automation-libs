package com.base;
/**
 * Created by Krish on 21.07.2018.
 **/

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
        String _platform = testParams.get("platForm");

        try {
        //capabilities from capabilities.json
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) new JSONParser().parse(new FileReader(Constants.CAPABILITIES));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject jObj = (JSONObject) jsonObject.get(platForm.toUpperCase());

        try {
            if (_platform.equalsIgnoreCase("Android") | _platform.equalsIgnoreCase("iOS")
                    | StringUtils.containsIgnoreCase("Android", platForm) | StringUtils.containsIgnoreCase("iOS", platForm)) {

                //UDID from TestNG parameter
                if (udid != null)
                    jObj.put("appium:udid", udid.trim());


                //capabilities from TestNG.xml
                String pCapabilities = testParams.get("capabilities");
                if (pCapabilities != null) {
                    pCapabilities = pCapabilities.replaceAll("'", "\"");
                    try {
                        JSONObject jObject = (JSONObject) new JSONParser().parse(pCapabilities);
                        for (Object keyStr : jObject.keySet()) {
                            try {
                                jObj.put(keyStr, jObject.get(keyStr));
                            } catch (Exception e) {
                                //ignore
                            }
                        }
                    } catch (ParseException e) {
                        //ignore
                    }
                }
            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
//            log.error("error in capabilities.json");
        }

        //capabilities from TestData
        try {
            String pTestData = testParams.get("p_Testdata");
            JSONObject jObject = (JSONObject) new JSONParser().parse(new FileReader(pTestData));
            String className = iTestResult.getInstanceName();
            JSONArray jsonArray = (JSONArray) jObject.get(className);
            JSONObject jObjt = (JSONObject) jsonArray.get(0);
            JSONObject jAObject = (JSONObject) jObjt.get("capabilities");

            jAObject.keySet().forEach(key -> {
                jObj.put(key,  jAObject.get(key));
            });
        } catch (Exception e) {
            //ignore
        }

        if (testName != null)
            jObj.put("auto:testName", testName);

        capabilitiesMap.put(Thread.currentThread().getId(), jObj);

        //add to capabilities
        System.out.println("capabilities : " + jObj.toJSONString());
        for (Object key : jObj.keySet()) {
            if (key.toString().equalsIgnoreCase("platformVersion")) {
                continue;
            }
            if (_platform.equalsIgnoreCase("iOS") | StringUtils.containsIgnoreCase("iOS", platForm)) {
                if (key.toString().equalsIgnoreCase("browserName")) {
                    capabilities.setCapability("bundleId", "");
                }
            }
            if (_platform.equalsIgnoreCase("Android") | StringUtils.containsIgnoreCase("Android", platForm)) {
                if (key.toString().equalsIgnoreCase("browserName")) {
                    capabilities.setCapability("appPackage", "");
                    capabilities.setCapability("appActivity", "");
                }
            }
            capabilities.setCapability(key.toString(), jObj.get(key));
        }

        } catch (Exception ex) {
//            log.error("failed to set capabilities");
            log.error("error in capabilities.json");
        }
        return capabilities;
    }

}
