package other.testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.IOException;
import java.nio.file.Paths;

public class JsonReport {

    public static void main(String[] args) throws IOException {

        ExtentSparkReporter spark = new ExtentSparkReporter(
                Paths.get("/Users/home/Downloads/21.35.06.818/"+ "AUTOMATION_FULL_REPORT.html").toFile());

        ExtentReports extent = new ExtentReports();
        try {
            extent.createDomainFromJsonArchive("/Users/home/Downloads/21.35.06.818/AUTOMATION_REPORT.json");
        } catch (IOException e) {
//            throw new CombinerException("Exception in creating merged JSON report.", e);
            System.out.println("Exception in creating merged JSON report."+e);
        }

        extent.attachReporter(spark);
        extent.flush();
    }

}
