package other.testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class HtmlReport {

    public static void main(String[] args) {


        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("/Users/home/Automation/bqatautomation-mbm/test-output/06012023/AUTOMATION_REPORT.html");
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter htmlReporter2 = new ExtentSparkReporter("/Users/home/Automation/bqatautomation-mbm/test-output/05312023/AUTOMATION_REPORT.html");


        extent.attachReporter(htmlReporter, htmlReporter2);

        extent.flush();



    }
}
