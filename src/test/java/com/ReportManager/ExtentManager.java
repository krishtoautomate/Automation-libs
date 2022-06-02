package com.ReportManager;

import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

public class ExtentManager {
  public static final ExtentReports extentReports = new ExtentReports();
  public synchronized static ExtentReports createExtentReports() {
    JsonFormatter jsonReport = new JsonFormatter(Constants.EXTENT_JSON_REPORT);
    ExtentSparkReporter reporter = new ExtentSparkReporter(Constants.EXTENT_HTML_REPORT).viewConfigurer()
        .viewOrder().as(new ViewName[] {ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
            ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
        .apply();
    reporter.config().setReportName("Automation Report");
    extentReports.attachReporter(reporter,jsonReport);
    extentReports.setSystemInfo("OS", Constants.HOST_OS);
    extentReports.setSystemInfo("HostIPAddress", Constants.HOST_IP_ADDRESS());
    extentReports.setSystemInfo("Host Name", Constants.HOST_NAME());
    return extentReports;
  }

}
