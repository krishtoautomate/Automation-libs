package com.base;

import java.io.FileWriter;
import java.io.IOException;

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
	
	/*
	 * @param test execution details to set
	 * summary, description, revision, startDate, finishDate, testPlanKey, testEnvironments
	 */
	public void setTestExecutionInfo(String key, String value) {
		textExecution.addProperty(key, value);
	}
	
	/*
	 * Add textExecution to Execution info object
	 */
	public void addInfo() {
		info.add("info", textExecution);
	}
	
	/*
	 * @param Set Test info parameters
	 */
//	public void setTestInfo(String key, String value) {
//		test.addProperty(key, value);
//	}
	
	/*
	 * add Test to Tests
	 */
	public void addTest(String testKey, String start, String finish, String status, String comments) {
		
		JsonObject test = new JsonObject();
		test.addProperty("testKey", testKey);
		test.addProperty("start", start);
		test.addProperty("finish", finish);
		test.addProperty("status", "PASS");
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
	        e.printStackTrace();
	    }
	}
	
}
