package other.testcases;

import com.ReportManager.ExtentManager;
import com.Utilities.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeJsonReport {

    public static void main(String[] args) throws IOException {

//        ExtentManager extentManager = new ExtentManager();

        String folderPath = System.getenv("REPORT_PATH");
//                Constants.USER_DIR;
//                System.getenv("REPORT_PATH"); // Replace with the actual folder path
        List<String> jsonFiles = new ArrayList<>();
        try {

                        Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                                .filter(path -> path.toString().contains("_REPORT"))
                                .filter(path -> path.toString().endsWith(".json"))
                    .forEach(System.out::println);
            System.out.println(Arrays.deepToString(jsonFiles.toArray()));
            ExtentManager.createHTMLReportFromJsonReports(jsonFiles, Constants.EXTENT_HTML_REPORT);


            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains("_REPORT"))
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> jsonFiles.add(path.toString()));

            ExtentManager.createHTMLReportFromJsonReports(jsonFiles,Constants.EXTENT_HTML_REPORT );

            System.out.println("merge Reports");

        } catch (Exception e) {
            //ignore
            System.out.println("merge Reports : "+e.getMessage());
        }

//
//        extentManager.createHTMLReportFromJsonReports(extentManager)
//
//        ExtentSparkReporter spark = new ExtentSparkReporter(
//                Paths.get("/Users/home/Downloads/21.35.06.818/"+ "AUTOMATION_FULL_REPORT.html").toFile());
//
//        ExtentReports extent = new ExtentReports();
//        try {
//            extent.createDomainFromJsonArchive("/Users/home/Downloads/21.35.06.818/AUTOMATION_REPORT.json");
//        } catch (IOException e) {
////            throw new CombinerException("Exception in creating merged JSON report.", e);
//            System.out.println("Exception in creating merged JSON report."+e);
//        }
//
//        extent.attachReporter(spark);
//        extent.flush();

//        System.out.println("merge Reports");
    }


}
