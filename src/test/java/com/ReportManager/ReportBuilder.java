package com.ReportManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportBuilder {

  private static final Logger log = LoggerFactory.getLogger(Class.class.getName());
  public static List<Result> details;
  public final String resultPlaceholder = "<!-- INSERT_RESULTS -->";
  public final String templatePath = System.getProperty("user.dir") + "/" + "src/main/resources"
      + "/" + "ReportTemplate" + ".html";

  public ReportBuilder() {
  }

  public void initialize() {
    details = new ArrayList<Result>();
  }

  public void report(String localDateTime, String testBrand, String buildNo, String testEnvironment,
      String testCase, String testDevice, String platForm, String status, String reason) {
    Result r = new Result(localDateTime, testBrand, buildNo, testEnvironment, testCase, testDevice,
        platForm, status, reason);
    details.add(r);
  }

  public void writeResults(String Path) {
    Boolean dbConnected = false;
    Statement stmt = null;
    Connection con = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      String sbURL = "jdbc:oracle:thin:@//mtrlpqdc2ae-034.bell.corp.bce.ca:1521/MONITOR";
      con = DriverManager.getConnection(sbURL, "AUTOMATION_FEED", "iLUVr0b0tS");

      stmt = con.createStatement();

      dbConnected = true;
    } catch (ClassNotFoundException | SQLException e) {
      log.error("DB connection error..");
    }

    try {
      String reportIn = new String(Files.readAllBytes(Paths.get(templatePath)));
      for (int i = 0; i < details.size(); i++) {

        String htmlStyle = "<td>";
        if (details.get(i).getStatus().contains("FAIL")) {
          htmlStyle = "<td style=\"color:#FF0000\";>";

          reportIn = reportIn.replaceFirst(resultPlaceholder,

              "<tr><td>" + Integer.toString(i + 1) + "</td><td>" + details.get(i).getTestCase()
                  + "</td><td>" + details.get(i).getPlatForm() + "</td>" + htmlStyle
                  + details.get(i).getStatus() + "</td></tr>" + resultPlaceholder);

        }

        if (dbConnected) {

          stmt.executeQuery(
              "INSERT INTO BELLCA.AUTO_TBL_DATA(TEST_TIME ,TEST_BRAND ,TEST_BUILD , TEST_ENVIRONMENT ,TEST_CASE ,TEST_STATUS,TEST_PLATFORM, TEST_DEVICE) "
                  + "VALUES " + "(" + "TO_TIMESTAMP('" + details.get(i).getLocalDateTime()
                  + "','YYYY-MM-DD HH24:MI:SS')," + "'" + details.get(i).getTestBrand() + "'," + "'"
                  + details.get(i).getBuildNo() + "'," + "'" + details.get(i).getTestEnvironment()
                  + "'," + "'" + details.get(i).getTestCase() + "'," + "'"
                  + details.get(i).getStatus() + "'," + "'" + details.get(i).getPlatForm() + "',"
                  + "'" + details.get(i).getTestDevice() + "'" + ")");
        }
      }

      con.close();

      Files.write(Paths.get(Path), reportIn.getBytes(), StandardOpenOption.CREATE);

    } catch (Exception e) {
      log.info("Error when writing report file..");
    }
  }
}
