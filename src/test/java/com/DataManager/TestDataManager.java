package com.DataManager;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import com.base.Log;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;


public class TestDataManager {

  private static Logger log = Logger.getLogger(TestDataManager.class.getName());



  private String filePath;
  private String className;
  private String platformName;

  public TestDataManager(String filePath) {
    ITestResult iTestResult = Reporter.getCurrentTestResult();
    this.className = iTestResult.getInstanceName();
    this.filePath = filePath;
    Map<String, String> testParams =
            iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();
    this.platformName = testParams.get("platForm");
  }

  public TestDataManager(String filePath, String platformName) {
    ITestResult iTestResult = Reporter.getCurrentTestResult();
    this.className = iTestResult.getInstanceName();
    this.filePath = filePath;
    this.platformName = platformName;
  }

  public TestDataManager(String filePath, String platForm, String className) {
    this.className = className;
    this.filePath = filePath;
    this.platformName = platForm;
  }

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

  public synchronized String get(int index, String key) {

    try {
      // read the json file
      JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(filePath));

      // get an array from the JSON object
      JSONArray jsonArray = (JSONArray) jsonObject.get(className);

      JSONObject innerObj = (JSONObject) jsonArray.get(index);

      return innerObj.get(key).toString();

    } catch (IOException | ParseException | NullPointerException ex) {
      log.error("Data file error to get : "+className + ":"+key);
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

      ITestResult iTestResult = Reporter.getCurrentTestResult();
      Map<String, String> testParams =
              iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();
      this.platformName = testParams.get("platForm");
      int index = "iOS".equalsIgnoreCase(platformName) ? 1 : 0;
      JSONObject innerObj = (JSONObject) jsonArray.get(index);

      return innerObj.get(key).toString();

    } catch (IOException | ParseException | NullPointerException ex) {
      log.error("Data file error to get : "+className + ":"+key);
    }
    return null;
  }

  public synchronized String get(String className, String key) {

    try {
      // read the json file
      JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(filePath));

      // get an array from the JSON object
      JSONArray jsonArray = (JSONArray) jsonObject.get(className);

      JSONObject innerObj = (JSONObject) jsonArray.get(0);

      return innerObj.get(key).toString();

    } catch (IOException | ParseException | NullPointerException ex) {
      log.error("Data file error to get : "+className + ":"+key);
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
      log.error("Data file error to get global : "+key);
    }
    return null;
  }

}
