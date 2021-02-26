package com.base;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.Utilities.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Jira {

	JsonObject info = new JsonObject();
	JsonObject textExecution = new JsonObject();
	JsonArray tests = new JsonArray();
//	JsonObject test = new JsonObject();

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
		
		//start
		String buildNo = System.getenv("BUILD_NUMBER");
		String jobName = System.getenv("JOB_NAME");
		String buildUrl =  System.getenv("BUILD_URL");
		String testPlanKey = System.getenv("TEST_PLAN_KEY");
		
		String excutionSummary = jobName+"-"+buildNo;
		String excutionDescription = buildUrl;
		String startDate = dateANDtime;
//		String testExecKey = System.getenv("TEST_EXECUTION");
//		if(testExecKey != null)
//			jiraReporter.setTestExecutionInfo("testExecutionKey", testExecKey);
		jiraReporter.setTestExecutionInfo("summary", excutionSummary);
		jiraReporter.setTestExecutionInfo("testPlanKey", testPlanKey);//"MAEAUTO-320"
		jiraReporter.setTestExecutionInfo("description", excutionDescription);
		jiraReporter.setTestExecutionInfo("startDate", startDate);
		
        //finish
		String finishDate = dateANDtime;
		jiraReporter.setTestExecutionInfo("finishDate", finishDate);
		jiraReporter.addExecutionKey("testExecutionKey");
        jiraReporter.addInfo();
        jiraReporter.addTests();
//        jiraReporter.CreatejiraReport(Constants.JIRA_REPORT);
		
        
		System.out.println(jiraReporter.info.toString());
	}

	/*
	 * @param test execution details to set summary, description, revision,
	 * startDate, finishDate, testPlanKey, testEnvironments
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

}
