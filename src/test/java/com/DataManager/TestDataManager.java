package com.DataManager;

import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestResult;
import org.testng.Reporter;


public class TestDataManager {

  private static final Logger log = LogManager.getLogger(TestDataManager.class);

  private String filePath;
  private String className;
  private String platformName;

  public TestDataManager(String filePath) {
    ITestResult iTestResult = Reporter.getCurrentTestResult();
    this.className = iTestResult.getInstanceName();
    this.filePath = filePath;
  }

  // public TestDataManager(String filePath, String platformName) {
  // ITestResult iTestResult = Reporter.getCurrentTestResult();
  // this.filePath = filePath;
  // this.className = iTestResult.getInstanceName();
  // this.platformName = platformName;
  // }
  //
  // public TestDataManager(String filePath, boolean isAndroid) {
  // ITestResult iTestResult = Reporter.getCurrentTestResult();
  // this.filePath = filePath;
  // this.className = iTestResult.getInstanceName();
  // this.platformName = isAndroid ? "Android" : "iOS";
  // }


  public static void main(String[] args) throws IOException, ParseException {

    // TestDataManager testDataManager = new TestDataManager("src/test/resources/deviceInfo.json");

    JsonFileReader JsonFileReader = new JsonFileReader("src/test/resources/deviceInfo.json");

    System.out
        .println(JsonFileReader.getObjIndex("udid", "e0857ea6c266c485198cf77589ac858a2526dc01"));


    System.out.println(JsonFileReader.getJsonValue(2, "name"));
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
  public synchronized String getJsonValue(String key) {

    JsonFileReader JsonFileReader = new JsonFileReader(filePath);

    int index = 0;

    index = JsonFileReader.getObjIndex("className", className);

    String value = null;
    try {
      value = JsonFileReader.getJsonValue(index, key);
    } catch (Exception e) {
      log.error("Data file error..");
    }

    return value;
  }

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
