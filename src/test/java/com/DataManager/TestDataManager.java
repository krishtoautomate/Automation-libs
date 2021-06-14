package com.DataManager;

import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TestDataManager {

  private static final Logger log = LogManager.getLogger(TestDataManager.class);

  private String filePath;
  private String className;
  private String platformName;


  public TestDataManager(String PathtoJSONfile) {
    this.filePath = PathtoJSONfile;
  }

  public TestDataManager(String filePath, String className) {
    this.filePath = filePath;
    this.className = className;
  }

  public TestDataManager(String filePath, String className, String platformName) {
    this.filePath = filePath;
    this.className = className;
    this.platformName = platformName;
  }

  public synchronized String getValue(String key) {

    JSONParser jsonParser = new JSONParser();
    String value = null;
    try {
      JSONObject jsonTestData = (JSONObject) jsonParser.parse(new FileReader(filePath));
      JSONObject currentTestData = (JSONObject) jsonTestData.get(className);
      JSONObject userPassData = (JSONObject) currentTestData.get(platformName);
      value = userPassData.get(key).toString();
    } catch (IOException | ParseException e) {
      log.error("Data file error..");
    }

    return value;

  }


  /*
   * get key by unique key
   */
  public synchronized String getJsonValue(String className, String key) {

    JsonFileReader JsonFileReader = new JsonFileReader(filePath);

    int index = 0;
    try {
      index = JsonFileReader.getObjIndex("className", className);
    } catch (IOException | ParseException | NullPointerException e) {
      log.error("test data not found with className : " + className);
    }

    String value = null;
    try {
      value = JsonFileReader.getJsonValue(index, key);
    } catch (Exception e) {
      log.error("Data file error..");
    }

    return value;
  }

  /*
   * get key by index
   */
  // public synchronized String getJsonValue(int index, String key) {
  //
  // String jsonValue = null;
  //
  // try {
  // Object obj = new JSONParser().parse(new FileReader(new File(filePath)));
  // JSONArray jsonArray = (JSONArray) obj;
  //
  // jsonValue = ((JSONObject)jsonArray.get(index)).get(key).toString();
  // } catch (Exception e) {
  // log.error("Data file error..");
  // }
  //
  // return jsonValue;
  // }


  public synchronized String getJsonValue(int index, String key) {

    String value = null;

    try {
      // read the json file
      JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(filePath));

      // get an array from the JSON object
      JSONArray jsonArray = (JSONArray) jsonObject.get(className);

      JSONObject innerObj = (JSONObject) jsonArray.get(index);

      value = (String) innerObj.get(key);

    } catch (IOException | ParseException | NullPointerException ex) {
      log.error("Data file error..");
    }

    return value;
  }

}
