package com.ReportManager;

import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Locale;

public class ExtentManager {

    public static ExtentReports extentReports = new ExtentReports();

    public synchronized static ExtentReports createExtentReports() {

        Locale.setDefault(Locale.ENGLISH);
//        JsonFormatter jsonReport = new JsonFormatter(Constants.EXTENT_JSON_REPORT);
        String htmlReport = Constants.EXTENT_HTML_REPORT;
        ExtentSparkReporter report = new ExtentSparkReporter(htmlReport)
                .viewConfigurer()
                .viewOrder().as(new ViewName[]{ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
                        ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
                .apply();

        report.config()
                .setReportName("Automation Report");

//        ExtentPDFReporter pdfReport = new ExtentPDFReporter(Constants.EXTENT_PDF_REPORT);

        extentReports.attachReporter(report);

        extentReports.setSystemInfo("OS", Constants.HOST_OS);
        extentReports.setSystemInfo("HostIPAddress", Constants.HOST_IP_ADDRESS());
        extentReports.setSystemInfo("Host Name", Constants.HOST_NAME());

        return extentReports;
    }

    public synchronized static ExtentReports createExtentReports(String htmlReport, String jsonReport) {

        ExtentReports extentReport = new ExtentReports();

        Locale.setDefault(Locale.ENGLISH);
        JsonFormatter jsonFormatter = new JsonFormatter(jsonReport);
        ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(htmlReport)
                .viewConfigurer()
                .viewOrder().as(new ViewName[]{ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
                        ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
                .apply();

        extentSparkReporter.config().setTimelineEnabled(false);
        extentSparkReporter.config()
                .setReportName("Automation Report");

//        ExtentPDFReporter pdfReport = new ExtentPDFReporter(Constants.EXTENT_PDF_REPORT);

        extentReport.attachReporter(extentSparkReporter,jsonFormatter);

        extentReport.setSystemInfo("OS", Constants.HOST_OS);
        extentReport.setSystemInfo("HostIPAddress", Constants.HOST_IP_ADDRESS());
        extentReport.setSystemInfo("Host Name", Constants.HOST_NAME());

        return extentReport;
    }

    public static synchronized void createHTMLReportFromJsonReports(List<String> jsonReports, String htmlReport) {

        ExtentSparkReporter spark = new ExtentSparkReporter(htmlReport)
                .viewConfigurer()
                .viewOrder().as(new ViewName[]{ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
                        ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
                .apply();

        spark.config()
                .setReportName("Automation Report");

        ExtentReports extent = new ExtentReports();

        try {
            for(String jsonReport : jsonReports) {
                extent.createDomainFromJsonArchive(jsonReport);
            }

            extent.attachReporter(spark);
            extent.flush();
        } catch (IOException e) {
//            throw new CombinerException("Exception in creating merged JSON report.", e);
            System.out.println("Exception in creating merged JSON report." + e);
        } finally {
            System.out.println("Report : "+Constants.EXTENT_HTML_REPORT);
        }
    }


}
