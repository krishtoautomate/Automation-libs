package com.base;
/**
 * Created by Krish on 21.07.2018.
 **/

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by Krish on 21.01.2019.
 */
public class CapabilitiesManager {

  private DesiredCapabilities capabilities = new DesiredCapabilities();

  @SuppressWarnings("unchecked")
  public synchronized DesiredCapabilities loadJSONCapabilities(String capabilitiesJson,
      String capabilitiesName) {

    String buildNo = System.getenv("BUILD_NUMBER");

    if (buildNo != null)
      capabilitiesJson = Constants.CAPABILITIES;

    try {
      JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(capabilitiesJson));

      JSONArray jsonArray = (JSONArray) jsonObject.get(capabilitiesName);

      JSONObject innerObj = (JSONObject) jsonArray.get(0);

      innerObj.keySet().forEach(key -> {
        capabilities.setCapability(key.toString(), innerObj.get(key));
      });
    } catch (IOException | ParseException | NullPointerException ex) {
      ex.printStackTrace();
    }

    return capabilities;
  }
}
