package com.Listeners;

import com.ReportManager.ExtentManager;
import com.Utilities.Constants;
import org.apache.log4j.Logger;
import org.testng.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class InvokedSuiteListener extends TestListenerAdapter implements ISuiteListener {

    private static Logger log = Logger.getLogger(InvokedSuiteListener.class.getName());

    @Override
    public void onFinish(ISuite suite) {
        String folderPath = Constants.EXTENT_REPORT_DIR; // Replace with the actual folder path
        List<String> jsonFiles = new ArrayList<>();
        try {
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> jsonFiles.add(path.toString()));
            ExtentManager.createHTMLReportFromJsonReports(jsonFiles, Constants.EXTENT_HTML_REPORT);
        } catch (Exception e) {
            //ignore
        }
    }

    @Override
    public void onStart(ISuite suite) {

    }

    @Override
    public void onFinish(ITestContext context) {
        Iterator<ITestResult> skippedTestCases = context.getSkippedTests().getAllResults().iterator();
        while (((Iterator<?>) skippedTestCases).hasNext()) {
            ITestResult skippedTestCase = skippedTestCases.next();
            ITestNGMethod method = skippedTestCase.getMethod();
            if (context.getSkippedTests().getResults(method).size() > 0) {
                log.info("Removing:" + skippedTestCase.getTestClass().toString());
                skippedTestCases.remove();
            }
        }
    }


}
