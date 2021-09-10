package com.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

public class ExtentManager {

  protected static ExtentReports extent = new ExtentReports();

  public synchronized static ExtentReports createExtentReports(String reportName,
      String htmlReport) {

    // extent report
    extent = new ExtentReports();
    ExtentSparkReporter htmlReporter = new ExtentSparkReporter(htmlReport).viewConfigurer()
        .viewOrder().as(new ViewName[] {ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
            ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
        .apply();

    extent.attachReporter(htmlReporter);

    htmlReporter.config().setDocumentTitle("AUTOMATION REPORT");
    htmlReporter.config().setReportName(reportName);

    // HOST INFO
    extent.setSystemInfo("OS", Constants.HOST_OS);
    extent.setSystemInfo("HostIPAddress", Constants.HOST_IP_ADDRESS());
    extent.setSystemInfo("Host Name", Constants.HOST_NAME());

    return extent;
  }
}
