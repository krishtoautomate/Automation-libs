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
        String pCapabilities = testParams.get("capabilities");

        String deviceName = "Android".equalsIgnoreCase(platForm) ? "Android Device" : "iPhone";

        if (udid != null) {
            capabilities.setCapability(MobileCapabilityType.UDID, udid);


            DeviceInfoReader deviceInfoReader = new DeviceInfoReader(udid);
            deviceName = deviceInfoReader.getString("name");
            devicePort = deviceInfoReader.getInt("devicePort");
            if ("Android".equalsIgnoreCase(platForm)) {
                capabilities.setCapability("systemPort", devicePort);
            }
            if ("iOS".equalsIgnoreCase(platForm)) {
                capabilities.setCapability("wdaLocalPort", devicePort);
            }
        } else {

            if ("Android".equalsIgnoreCase(platForm)) {
                capabilities.setCapability("systemPort", devicePort);
            }
            if ("iOS".equalsIgnoreCase(platForm)) {
                capabilities.setCapability("wdaLocalPort", devicePort);
            }

        }
        capabilities.setCapability("deviceName", deviceName);

        String capabilitiesName = "Android".equalsIgnoreCase(platForm) ? "ANDROID" : "IOS";

        try {
            JSONObject jsonObject =
                    (JSONObject) new JSONParser().parse(new FileReader(Constants.CAPABILITIES));
            JSONArray jsonArray = (JSONArray) jsonObject.get(capabilitiesName);
            JSONObject jObj = (JSONObject) jsonArray.get(0);

            jObj.keySet().forEach(key -> {
                capabilities.setCapability(key.toString(), jObj.get(key));
            });

        } catch (IOException | ParseException | NullPointerException ex) {
            log.error(("failed to set global capabilities"));
        }

        try {
            String pTestData = testParams.get("p_Testdata");
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(pTestData));
            String className = iTestResult.getInstanceName();
            JSONArray jsonArray = (JSONArray) jsonObject.get(className);
            JSONObject jObj = (JSONObject) jsonArray.get(0);
            JSONObject jAObject = (JSONObject) jObj.get("capabilities");

            for (Object keyStr : jAObject.keySet()) {
                capabilities.setCapability(keyStr.toString(), jAObject.get(keyStr).toString());
            }
        } catch (Exception e) {
            log.error(("No test capabilities found!"));
        }


        if (pCapabilities != null) {

            pCapabilities = pCapabilities.replaceAll("'", "\"");

            try {
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(pCapabilities);

                for (Object keyStr : jsonObject.keySet()) {
                    capabilities.setCapability(keyStr.toString(), jsonObject.get(keyStr).toString());
                }
            } catch (ParseException e) {
                log.error(("No test parameter capabilities found!"));
            }


        }

        return capabilities;
    }

}
