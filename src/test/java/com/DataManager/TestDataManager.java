package com.DataManager;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;


public class TestDataManager {

  private static final Logger log = LoggerFactory.getLogger(Class.class.getName());

  private String filePath;
  private String className;
  private String platformName;

  public TestDataManager(String filePath) {
    ITestResult iTestResult = Reporter.getCurrentTestResult();
    this.className = iTestResult.getInstanceName();
    this.filePath = filePath;
  }

  public TestDataManager(String filePath, String platformName) {
    ITestResult iTestResult = Reporter.getCurrentTestResult();
    this.className = iTestResult.getInstanceName();
    this.filePath = filePath;
    this.platformName = platformName;
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

//    JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader("Database.txt"));
//
//    JSONArray jsonArray = (JSONArray) jsonObject.get("other.testcases.TestFlight");
//
//    JSONObject jObj = (JSONObject) jsonArray.get(0);
//
//    JSONObject jAObject = (JSONObject) jObj.get("capabilities");
//
//    for (Object keyStr : jAObject.keySet()) {
//      System.out
//          .println("key: " + keyStr.toString() + " value: " + jAObject.get(keyStr).toString());
//    }
//
//    TestDataManager testDataManager = new TestDataManager("/Users/krish/Automation/Automation-libs/Database.txt");
//
//    System.out.println(testDataManager.getGlobal("language"));

  }

//  public synchronized String getValue(String key) {
//
//    JSONParser jsonParser = new JSONParser();
//    String value = null;
//    try {
//      JSONObject jsonTestData = (JSONObject) jsonParser.parse(new FileReader(filePath));
//      JSONObject currentTestData = (JSONObject) jsonTestData.get(className);
//      JSONObject userPassData = (JSONObject) currentTestData.get(platformName);
//      value = userPassData.get(key).toString();
//    } catch (IOException | ParseException e) {
//      log.error("Data file error..");
//    }
//    return value;
//  }


  /*
   * get key by unique key
   */
//  public synchronized String getJsonValue(String key) {
//    JsonFileReader JsonFileReader = new JsonFileReader(filePath);
//    int index = 0;
//    index = JsonFileReader.getObjIndex("className", className);
//    try {
//      return JsonFileReader.getJsonValue(index, key);
//    } catch (Exception e) {
//      log.error("Data file error..");
//    }
//    return null;
//  }

  public synchronized String getJsonValue(int index, String key) {

    try {
      // read the json file
      JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(filePath));

      // get an array from the JSON object
      JSONArray jsonArray = (JSONArray) jsonObject.get(className);

      JSONObject innerObj = (JSONObject) jsonArray.get(index);

      return innerObj.get(key).toString();

    } catch (IOException | ParseException | NullPointerException ex) {
      log.error("Data file error..");
    }

    return null;
  }

  /*
  index is set by default based on platform
   */
  public synchronized String get(String key) {

    try {
      // read the json file
      JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(filePath));

      // get an array from the JSON object
      JSONArray jsonArray = (JSONArray) jsonObject.get(className);

      int index = "Android".equalsIgnoreCase(platformName) ? 0 : 1;
      JSONObject innerObj = (JSONObject) jsonArray.get(index);

      return innerObj.get(key).toString();

    } catch (IOException | ParseException | NullPointerException ex) {
      log.error("Data file error..");
    }
    return null;
  }

  /*
  get global value without className/testName
 */
  public synchronized String getGlobal(String key) {

    try {
      // read the json file
      JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(filePath));

      return jsonObject.get(key).toString();

    } catch (IOException | ParseException | NullPointerException ex) {
      log.error("Data file error..");
    }
    return null;
  }

}
