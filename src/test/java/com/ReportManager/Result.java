package com.ReportManager;

public class Result {

	
	String testCase; 
	String browser; 
	String status;
	String reason;
	
	
	public Result(String testCase, String browser, String status, String reason) {
		this.testCase = testCase;
		this.browser = browser;
		this.status = status;
		this.reason = reason;
	}
	public String getTestCase() {
		return testCase;
	}
	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

}