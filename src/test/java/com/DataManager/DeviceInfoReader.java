package com.DataManager;

import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import com.Utilities.Constants;


public class DeviceInfoReader {

  private static final Logger log = LogManager.getLogger(DeviceInfoReader.class);


  int index = 0;

  public DeviceInfoReader(String udid) {
    JsonFileReader JsonFileReader = new JsonFileReader(Constants.DEVICE_INFO);

    this.index = JsonFileReader.getObjIndex("udid", udid);
  }

  // public static void main(String[] args) throws IOException, ParseException {
  //
  // // TestDataManager testDataManager = new TestDataManager("src/test/resources/deviceInfo.json");
  //
  // JsonFileReader JsonFileReader = new JsonFileReader(Constants.DEVICE_INFO);
  //
  // int index = JsonFileReader.getObjIndex("udid", "e0857ea6c266c485198cf77589ac858a2526dc01");
  //
  // System.out.println(JsonFileReader.getJsonValue(index, "name"));
  // }

  public synchronized String getString(String key) {

    JsonFileReader jsonFileReader = new JsonFileReader(Constants.DEVICE_INFO);
    try {
      return jsonFileReader.getJsonValue(index, key);
    } catch (IOException | ParseException e) {
      log.error("Data file error..");
    }
    return null;
  }

  public synchronized int getInt(String key) {

    JsonFileReader jsonFileReader = new JsonFileReader(Constants.DEVICE_INFO);
    try {
      return Integer.valueOf(jsonFileReader.getJsonValue(index, key));
    } catch (IOException | ParseException e) {
      log.error("Data file error..");
    }
    return 8301;
  }


}
