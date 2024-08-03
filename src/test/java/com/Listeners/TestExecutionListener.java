package com.Listeners;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.ITestContext;
import org.testng.ITestListener;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class TestExecutionListener implements ITestListener {

    private String baseUrl = "https://<git-url>/api/v4";
    private String personalAccessToken = "mxKmoR48CJhr6XiXBgEH";

    @Override
    public void onStart(ITestContext context) {

        RestAssured.baseURI = baseUrl;
        RestAssured.basePath = "/projects";

        Response response = given()
                .header("Private-Token", personalAccessToken)
                .get();

        assertEquals(response.getStatusCode(), 200, "Failed to authenticate with GitLab");
        assertEquals(response.getContentType(), "application/json", "Unexpected response content type");


    }

    @Override
    public void onFinish(ITestContext context) {
        int totalTests = context.getAllTestMethods().length;
        int passedTests = context.getPassedTests().size();
        int failedTests = context.getFailedTests().size();
        int skippedTests = context.getSkippedTests().size();

        System.out.println("Total tests executed: " + totalTests);
        System.out.println("Tests passed: " + passedTests);
        System.out.println("Tests failed: " + failedTests);
        System.out.println("Tests skipped: " + skippedTests);
    }

    // Other overridden methods
    // ...

}
