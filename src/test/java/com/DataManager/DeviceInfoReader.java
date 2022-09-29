package com.DataManager;

import com.ReportManager.LoggerManager;
import com.ReportManager.ReportBuilder;
import com.Utilities.Constants;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class DeviceInfoReader {


  private static Logger log = Logger.getLogger(DeviceInfoReader.class.getName());

  int index = 0;
  String udid = "";

  public DeviceInfoReader(String udid) {
    this.udid = udid;
    this.index = getIndex("udid", udid);
  }

  public static void main(String[] args) {
    DeviceInfoReader deviceInfoReader = new DeviceInfoReader(
        "e0857ea6c266c485198cf77589ac858a2526dc01");
    System.out.println(deviceInfoReader.getString("udid"));
    System.out.println(deviceInfoReader.getInt("devicePort"));
  }

  public synchronized String getString(String key) {
    try {
      ArrayList jArray = (JSONArray) new JSONParser().parse(new FileReader(Constants.DEVICE_INFO));
      return ((JSONObject) jArray.get(index)).get(key).toString();
    } catch (IOException | ParseException e) {
      log.error("Device-info file error..");
    }
    return null;
  }

  public synchronized int getInt(String key) {
    try {
      ArrayList jArray = (JSONArray) new JSONParser().parse(new FileReader(Constants.DEVICE_INFO));
      return Integer.valueOf(((JSONObject) jArray.get(index)).get(key).toString());
    } catch (IOException | ParseException e) {
      log.error("Device-info file error..");
    }
    return 8301;
  }

  private synchronized int getIndex(String key, String value) {

    try {
      ArrayList jArray = (JSONArray) new JSONParser().parse(new FileReader(Constants.DEVICE_INFO));

      for (int i = 0; i < jArray.size(); i++) {
        if (((JSONObject) jArray.get(i)).get(key).toString().equals(value)) {
          return i;
        }
      }
    } catch (Exception e) {
      // ignore
    }
    return 0;
  }


}
