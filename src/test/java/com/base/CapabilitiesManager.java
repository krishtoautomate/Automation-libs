package com.base;
/**
 * Created by Krish on 21.07.2018.
 **/

import com.DataManager.DeviceInfoReader;
import com.Utilities.Constants;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Krish on 21.01.2019.
 */
public class CapabilitiesManager {

    static int devicePort = 8100;
    private static Logger log = Logger.getLogger(CapabilitiesManager.class.getName());

    @SuppressWarnings("unchecked")
    public synchronized DesiredCapabilities setCapabilities() {

        devicePort++;

        DesiredCapabilities capabilities = new DesiredCapabilities();

        ITestResult iTestResult = Reporter.getCurrentTestResult();
        Map<String, String> testParams =
                iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

        String platForm = testParams.get("platForm");
        String udid = testParams.get("udid");

        String deviceName = "Android".equalsIgnoreCase(platForm) ? "Android Device" : "iPhone";

        try {
            //capabilities from capabilities.json
            JSONObject jsonObject =
                    (JSONObject) new JSONParser().parse(new FileReader(Constants.CAPABILITIES));
            JSONArray jsonArray = (JSONArray) jsonObject.get(platForm.toUpperCase());
            JSONObject jObj = (JSONObject) jsonArray.get(0);

            if (udid != null) {
                //UDID from TestNG parameter
                jObj.put(MobileCapabilityType.UDID, udid);

                DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
                deviceName = deviceInfoReader.getString("name");
                devicePort = deviceInfoReader.getInt("devicePort");
            }
            jObj.put("Android".equalsIgnoreCase(platForm) ? "systemPort" : "wdaLocalPort", devicePort);
            jObj.put("deviceName", deviceName);

            //capabilities from TestNG.xml
            String pCapabilities = testParams.get("capabilities");
            if (pCapabilities != null) {
                pCapabilities = pCapabilities.replaceAll("'", "\"");
                try {
                    JSONObject jObject = (JSONObject) new JSONParser().parse(pCapabilities);
                    for (Object keyStr : jObject.keySet()) {
                        try {
                            jObj.put(keyStr.toString(), jObject.get(keyStr).toString());
                        } catch (Exception e) {
                            //ignore
                        }
                    }
                } catch (ParseException e) {
                    //ignore
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

            //add to capabilities
            System.out.println("capabilities : " + jObj.toJSONString());
            jObj.keySet().forEach(key -> {
                capabilities.setCapability(key.toString(), jObj.get(key));
            });

        } catch (IOException | ParseException | NullPointerException ex) {
            log.error("failed to set global capabilities");
        }


        return capabilities;
    }

}
