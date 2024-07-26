package com.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

public class Jira {

    static String JIRA_URL = "https://jira.bell.corp.bce.ca";
    private static Logger log = Logger.getLogger(Jira.class.getName());
    JsonObject info = new JsonObject();
    JsonObject textExecution = new JsonObject();
    //    String summary;
//    String description;
//    String revision;
//    String startDate;
//    String finishDate;
//    String testPlanKey;
//    String testEnvironments;
//
//    String testKey;
//    String start;
//    String finish;
//    String comment;
//    String status;
    JsonArray tests = new JsonArray();

//    public static void main(String[] args) {
//
//        // update status
//        Jira jira = new Jira();
//
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
//        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
//        String dateANDtime = sdf.format(date.getTime());
//
//        jira.update_Test_Exec("MAEAUTO-16746", "MAEAUTO-6930", "PASS", dateANDtime, dateANDtime);
//    }

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
    public void createJiraReport(String reportPath) {

        try (FileWriter file = new FileWriter(reportPath)) {
            file.write(info.toString());
            file.flush();
        } catch (IOException e) {
            // ignore
        }
    }

    /*
    @type : add/remove
     */
    public void updateTestLabel(String testKey, String type, String label) {
        try {
            String jiraAuth = System.getenv("JIRA_AUTH");
            RestAssured.useRelaxedHTTPSValidation();
            RestAssured.baseURI = "https://jira.bell.corp.bce.ca";
            RestAssured.basePath = "/rest/api/2/issue/" + testKey;
//            RestAssured.baseURI = "https://jira.bell.corp.bce.ca/rest/api/2/issue/" + testKey;
            RequestSpecification req = RestAssured.given();
            req.header("Content-Type", "application/json");
            req.header("Authorization", "Basic " + jiraAuth);
            req.body("{\"update\" : {\"labels\" : [{\"" + type + "\" : \"" + label + "\"}]}}");
            req.put();
        } catch (Exception e) {
            log.info("JIRA test update of labels failed for " + testKey + " due to: "
                    + e.getLocalizedMessage());
        }
    }

    public String getTestLabels(String testKey) {
        try {
            String jiraAuth = System.getenv("JIRA_AUTH");
            RestAssured.useRelaxedHTTPSValidation();
            RestAssured.baseURI = JIRA_URL;
            RestAssured.basePath = "/rest/api/2/issue/" + testKey;
//            RestAssured.baseURI = "https://jira.bell.corp.bce.ca/rest/api/2/issue/" + testKey;
            RequestSpecification req = RestAssured.given();
            req.header("Content-Type", "application/json");
            req.header("Authorization", "Basic " + jiraAuth);
            Response res = req.get();
            String labels = res.getBody().jsonPath().getString("labels");
            return labels;
        } catch (Exception e) {
            log.info("JIRA test get labels failed for " + testKey + " due to: "
                    + e.getLocalizedMessage());
        }
        return "";
    }

    /*
     * Updates test execution for individual test case
     */
    public void update_Test_Exec(String testExecutionKey, String testKey, String status, String start,
                                 String finish) {
        try {

//            String testPlanKey = "MAERT-25397";
//            String jiraAuth = "a3Jpc2gucGF2dWx1cjpBdXRvbWF0aW9uMjAyMiQ=";


            String testPlanKey = System.getenv("TEST_PLAN_KEY");
            String jiraAuth = System.getenv("JIRA_AUTH");

            JsonObject _testExecution = new JsonObject();
            JsonObject _testPlan = new JsonObject();
            JsonObject _test = new JsonObject();
            JsonArray _tests = new JsonArray();

            _testPlan.addProperty("testPlanKey", testPlanKey);
            _test.addProperty("testKey", testKey);
            _test.addProperty("status", status);
            _test.addProperty("start", start);
            _test.addProperty("finish", finish);
            _tests.add(_test);
            _testExecution.addProperty("testExecutionKey", testExecutionKey);
            _testExecution.add("tests", _tests);
            _testExecution.add("info", _testPlan);

            RestAssured.useRelaxedHTTPSValidation();

            RestAssured.baseURI = JIRA_URL;
            RestAssured.basePath = "/rest/raven/1.0/import/execution";
//            RestAssured.baseURI = "https://jira.bell.corp.bce.ca/rest/raven/1.0/import/execution";
            Response response = RestAssured.given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + jiraAuth)
                    .body(_testExecution.toString())
                    .post();
            if (response.statusCode() != 200)
                log.error("Jira error : " + response.getBody().asString());
        } catch (Exception e) {
            log.info("JIRA test case execution update failed for " + testKey + " due to: "
                    + e.getLocalizedMessage());
        }
    }

    /*
     * Creates test execution and return the key as string
     */
    public String create_Test_Exec(String summary, String description, String testPlanKey) {
        // https://developer.atlassian.com/server/jira/platform/jira-rest-api-examples/
        String exec = "";
//    Response res = null;
        try {
            String issue_type = "Test Execution";
            String jiraAuth = System.getenv("JIRA_AUTH");
            String project_key = testPlanKey.substring(0, testPlanKey.indexOf("-"));
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

            RestAssured.useRelaxedHTTPSValidation();

            RestAssured.baseURI = JIRA_URL;
            RestAssured.basePath = "/rest/api/2/issue";

//            RestAssured.baseURI = "https://jira.bell.corp.bce.ca/rest/api/2/issue";
            RequestSpecification req = RestAssured.given();
            req.header("Content-Type", "application/json");
            req.header("Authorization", "Basic " + jiraAuth);
            req.body(jsonBody);
            Response res = req.post();
            exec = res.getBody().jsonPath().getString("key");
        } catch (Exception e) {
            log.info("JIRA execution creation failed due to: " + e.getLocalizedMessage());
        }
        return exec;
    }

//    public static void setEnv(String key, String value) {
//        try {
//            Map<String, String> env = System.getenv();
//            Class<?> cl = env.getClass();
//            Field field = cl.getDeclaredField("m");
//            field.setAccessible(true);
//            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
//            writableEnv.put(key, value);
//        } catch (Exception e) {
//            throw new IllegalStateException("Failed to set environment variable", e);
//        }
//    }

}
