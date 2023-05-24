package com.ReportManager;

public class Result {

    String localDateTime;
    String testBrand;
    String buildNo;
    String testEnvironment;
    String testCase;
    String testDevice;
    String platForm;
    String status;
    String reason;

    // public Result(String testCase, String platForm, String status, String reason) {
    // this.testCase = testCase;
    // this.platForm = platForm;
    // this.status = status;
    // this.reason = reason;
    // }

    public Result(String localDateTime, String testBrand, String buildNo, String testEnvironment,
                  String testCase, String testDevice, String platForm, String status, String reason) {
        super();
        this.localDateTime = localDateTime;
        this.testBrand = testBrand;
        this.buildNo = buildNo;
        this.testEnvironment = testEnvironment;
        this.testCase = testCase;
        this.testDevice = testDevice;
        this.platForm = platForm;
        this.status = status;
        this.reason = reason;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getTestBrand() {
        return testBrand;
    }

    public void setTestBrand(String testBrand) {
        this.testBrand = testBrand;
    }

    public String getBuildNo() {
        return buildNo;
    }

    public void setBuildNo(String buildNo) {
        this.buildNo = buildNo;
    }

    public String getTestEnvironment() {
        return testEnvironment;
    }

    public void setTestEnvironment(String testEnvironment) {
        this.testEnvironment = testEnvironment;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public String getTestDevice() {
        return testDevice;
    }

    public void setTestDevice(String testDevice) {
        this.testDevice = testDevice;
    }

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
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
