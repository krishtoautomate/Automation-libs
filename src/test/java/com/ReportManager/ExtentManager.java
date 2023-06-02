package com.ReportManager;

import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.io.IOException;
import java.util.Locale;

public class ExtentManager {

    public static final ExtentReports extentReports = new ExtentReports();

    public synchronized static ExtentReports createExtentReports() {

        Locale.setDefault(Locale.ENGLISH);
        JsonFormatter jsonReport = new JsonFormatter(Constants.EXTENT_JSON_REPORT);
        ExtentSparkReporter report = new ExtentSparkReporter(Constants.EXTENT_HTML_REPORT)
                .viewConfigurer()
                .viewOrder().as(new ViewName[]{ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
                        ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
                .apply();

        report.config()
                .setReportName("Automation Report");

//        ExtentPDFReporter pdfReport = new ExtentPDFReporter(Constants.EXTENT_PDF_REPORT);

        extentReports.attachReporter(report, jsonReport);

        extentReports.setSystemInfo("OS", Constants.HOST_OS);
        extentReports.setSystemInfo("HostIPAddress", Constants.HOST_IP_ADDRESS());
        extentReports.setSystemInfo("Host Name", Constants.HOST_NAME());

        return extentReports;
    }

    public static synchronized void createReportFromJson(String jsonReport, String htmlReport) {

        ExtentSparkReporter spark = new ExtentSparkReporter(htmlReport)
                .viewConfigurer()
                .viewOrder().as(new ViewName[]{ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
                        ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
                .apply();

        spark.config()
                .setReportName("Automation Report");

        ExtentReports extent = new ExtentReports();

        try {
            extent.createDomainFromJsonArchive(jsonReport);

            extent.attachReporter(spark);
            extent.flush();
        } catch (IOException e) {
//            throw new CombinerException("Exception in creating merged JSON report.", e);
            System.out.println("Exception in creating merged JSON report." + e);
        } finally {
            System.out.println(Constants.EXTENT_HTML_REPORT);
        }


    }


}
