package com.base;
/**
 * Created by Krish on 21.07.2018.
 **/

import com.Utilities.Constants;
import org.apache.log4j.Logger;
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

        try {
            //capabilities from capabilities.json
            JSONObject jsonObject =
                    (JSONObject) new JSONParser().parse(new FileReader(Constants.CAPABILITIES));
//            JSONArray jsonArray = (JSONArray) jsonObject.get(platForm.toUpperCase());
//            JSONObject jObj = (JSONObject) jsonArray.get(0);
            JSONObject jObj = (JSONObject) jsonObject.get(platForm.toUpperCase());

            if (platForm.equalsIgnoreCase("Android") || platForm.equalsIgnoreCase("iOS")) {

                //UDID from TestNG parameter
                if (udid != null) {
                    jObj.put("appium:udid", udid.trim());
                }

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

            //capabilities from TestData
//        try {
//            String pTestData = testParams.get("p_Testdata");
//            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(pTestData));
//            String className = iTestResult.getInstanceName();
//            JSONArray jsonArray = (JSONArray) jsonObject.get(className);
//            JSONObject jObj = (JSONObject) jsonArray.get(0);
//            JSONObject jAObject = (JSONObject) jObj.get("capabilities");
//
//            jAObject.keySet().forEach(key -> {
//                capabilities.setCapability(key.toString(), jAObject.get(key));
//            });
//        } catch (Exception e) {
//            //ignore
//        }

            if (testName != null) {
                jObj.put("auto:testName", testName);
            }

            capabilitiesMap.put(Thread.currentThread().getId(), jObj);

            //add to capabilities
            System.out.println("capabilities : " + jObj.toJSONString());
            for (Object key : jObj.keySet()) {
                if (key.toString().equalsIgnoreCase("platformVersion")) {
                    continue;
                }
                capabilities.setCapability(key.toString(), jObj.get(key));
            }

        } catch (IOException | ParseException | NullPointerException ex) {
            log.error("failed to set capabilities");
        }
        return capabilities;
    }

}
