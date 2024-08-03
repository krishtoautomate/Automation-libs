package com.Others;

import com.Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MergeReports {

    public static void main(String[] args) {

        ExtentSparkReporter spark = new ExtentSparkReporter(Constants.EXTENT_HTML_REPORT)
                .viewConfigurer()
                .viewOrder().as(new ViewName[]{ViewName.TEST, ViewName.DEVICE, ViewName.AUTHOR,
                        ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG, ViewName.DASHBOARD})
                .apply();

        spark.config()
                .setReportName("Automation Report");

        ExtentReports extent = new ExtentReports();
        List<String> jsonFiles = new ArrayList<>();
        try {
            Files.walk(Paths.get(Constants.EXTENT_REPORT_DIR))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains("_REPORT"))
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> jsonFiles.add(path.toString()));

            if(jsonFiles.size() > 1) {
                for(String jsonReport : jsonFiles) {
                    extent.createDomainFromJsonArchive(jsonReport);
                }
                extent.attachReporter(spark);
                extent.flush();
            }
        } catch (IOException e) {
            System.out.println("Exception in creating merged JSON report." + e);
        }
    }
}
