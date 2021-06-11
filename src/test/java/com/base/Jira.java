package com.base;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Jira {

  JsonObject info = new JsonObject();
  JsonObject textExecution = new JsonObject();
  JsonArray tests = new JsonArray();
  // JsonObject test = new JsonObject();

  String summary;
  String description;
  String revision;
  String startDate;
  String finishDate;
  String testPlanKey;
  String testEnvironments;

  String testKey;
  String start;
  String finish;
  String comment;
  String status;

  public static void main(String[] args) {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    sdf.setTimeZone(TimeZone.getTimeZone("EST"));
    String dateANDtime = sdf.format(date.getTime());

    Jira jiraReporter = new Jira();

    // start
    String buildNo = System.getenv("BUILD_NUMBER");
    String jobName = System.getenv("JOB_NAME");
    String buildUrl = System.getenv("BUILD_URL");
    String testPlanKey = System.getenv("TEST_PLAN_KEY");

    String excutionSummary = jobName + "-" + buildNo;
    String excutionDescription = buildUrl;
    String startDate = dateANDtime;
    // String testExecKey = System.getenv("TEST_EXECUTION");
    // if(testExecKey != null)
    // jiraReporter.setTestExecutionInfo("testExecutionKey", testExecKey);
    jiraReporter.setTestExecutionInfo("summary", excutionSummary);
    jiraReporter.setTestExecutionInfo("testPlanKey", testPlanKey);// "MAEAUTO-320"
    jiraReporter.setTestExecutionInfo("description", excutionDescription);
    jiraReporter.setTestExecutionInfo("startDate", startDate);

    // finish
    String finishDate = dateANDtime;
    jiraReporter.setTestExecutionInfo("finishDate", finishDate);
    jiraReporter.addExecutionKey("testExecutionKey");
    jiraReporter.addInfo();
    jiraReporter.addTests();
    // jiraReporter.CreatejiraReport(Constants.JIRA_REPORT);


    System.out.println(jiraReporter.info.toString());
  }

  /*
   * @param test execution details to set summary, description, revision, startDate, finishDate,
   * testPlanKey, testEnvironments
   */
  public void setTestExecutionInfo(String key, String value) {
    textExecution.addProperty(key, value);
  }

  /*
   * Add textExecution to Execution info object
   */
  public void addExecutionKey(String testExecutionKey) {
    info.addProperty("testExecutionKey", testExecutionKey);
  }

  /*
   * Add textExecution to Execution info object
   */
  public void addInfo() {
    info.add("info", textExecution);
  }

  /*
   * add Test to Tests
   */
  public void addTest(String testKey, String start, String finish, String status, String comments) {

    JsonObject test = new JsonObject();
    test.addProperty("testKey", testKey);
    test.addProperty("start", start);
    test.addProperty("finish", finish);
    test.addProperty("status", status);
    test.addProperty("comment", comments);

    tests.add(test);
  }

  /*
   * add Tests to Execution info
   */
  public void addTests() {
    info.add("tests", tests);

  }

  /*
   * Create jira report - default "test-output/jira-result.json"
   */
  public void CreatejiraReport(String reportPath) {

    try (FileWriter file = new FileWriter(reportPath)) {

      file.write(info.toString());
      file.flush();

    } catch (IOException e) {
      // ignore
    }
  }

  /*
   * Updates test execution for individual test case
   */
  public void update_Test_Exec(String testExecutionKey, String testKey, String status, String start,
      String finish) {
    // https://docs.getxray.app/display/XRAY/Import+Execution+Results+-+REST
    try {
      String testPlanKey = System.getenv("TEST_PLAN_KEY");
      String jiraAuth = System.getenv("JIRA_AUTH");

      JsonObject _mainObj = new JsonObject();
      JsonObject _testPlanKey = new JsonObject();
      JsonObject _testKey_Status = new JsonObject();
      JsonArray _tests = new JsonArray();

      _testPlanKey.addProperty("testPlanKey", testPlanKey);
      _testKey_Status.addProperty("testKey", testKey);
      _testKey_Status.addProperty("status", status);
      _testKey_Status.addProperty("start", start);
      _testKey_Status.addProperty("finish", finish);
      _tests.add(_testKey_Status);
      _mainObj.addProperty("testExecutionKey", testExecutionKey);
      _mainObj.add("tests", _tests);
      _mainObj.add("info", _testPlanKey);

      String jsonBody = _mainObj.toString();

      RestAssured.baseURI = "https://jira.bell.corp.bce.ca/rest/raven/1.0/import/execution";
      RequestSpecification req = RestAssured.given();
      req.header("Content-Type", "application/json");
      req.header("Authorization", "Basic " + jiraAuth);
      req.body(jsonBody);
      req.post();
    } catch (Exception e) {
      // TODO: handle exception
    }

  }

  /*
   * Creates test execution and return the key as string
   */
  public String create_Test_Exec(String summary, String description, String project_key, String testPlanKey) {
    // https://developer.atlassian.com/server/jira/platform/jira-rest-api-examples/
    String exec = "";
    try {
      String issue_type = "Test Execution";
      String jiraAuth = System.getenv("JIRA_AUTH");
      JsonObject _mainObj = new JsonObject();
      JsonObject _key = new JsonObject();
      JsonObject _name = new JsonObject();
      JsonObject _fields = new JsonObject();
      JsonArray _testPlanKey = new JsonArray();

      _key.addProperty("key", project_key);
      _name.addProperty("name", issue_type);
      _testPlanKey.add(testPlanKey);
      _fields.addProperty("summary", summary);
      _fields.add("issuetype", _name);
      _fields.add("project", _key);
      _fields.addProperty("description", description);
      _fields.add("customfield_11368", _testPlanKey);
      _mainObj.add("fields", _fields);

      String jsonBody = _mainObj.toString();

      RestAssured.baseURI = "https://jira.bell.corp.bce.ca/rest/api/2/issue";
      RequestSpecification req = RestAssured.given();
      req.header("Content-Type", "application/json");
      req.header("Authorization", "Basic " + jiraAuth);
      req.body(jsonBody);
      Response res = req.post();
      exec = res.getBody().jsonPath().getString("key");
      System.out.println(exec);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return exec;
  }

}
