package Tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dbTest {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    Class.forName("oracle.jdbc.driver.OracleDriver");
    // Class.forName("oracle.jdbc.OracleDriver");

    // JDBC Connection String: jdbc:oracle:thin:@//servername.com:port#/sid
    String sbURL = "jdbc:oracle:thin:@//mtrlpqdc2ae-034.bell.corp.bce.ca:1521/MONITOR";
    Connection con = DriverManager.getConnection(sbURL, "AUTOMATION_FEED", "iLUVr0b0tS");

    Statement stmt = con.createStatement();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    LocalDateTime now = LocalDateTime.now();

    String date = dtf.format(now);
    System.out.println(date);

    // ResultSet insertQuery = stmt.executeQuery("INSERT INTO BELLCA.AUTO_TBL_DATA(TEST_TIME
    // ,TEST_BRAND ,TEST_BUILD , TEST_ENVIRONMENT ,TEST_CASE ,TEST_STATUS,TEST_PLATFORM,
    // TEST_DEVICE) "
    // + "VALUES "+
    // "(" +
    // "TO_TIMESTAMP('"+date+"','YYYY-MM-DD HH24:MI:SS')," +
    // "'MBM'," +
    // "'2'," +
    // "'PREPROD'," +
    // "'Login_Test'," +
    // "'FAIL'," +
    // "'Ios'," +
    // "'Iphone xs'" +
    // ")");

    ResultSet rs = stmt.executeQuery("SELECT * FROM BELLCA.AUTO_TBL_DATA");



    System.out.println(rs.getMetaData().getColumnName(9));
    System.out.println("TEST_TIME" + "          |" + "TEST_BRAND" + "|" + "TEST_BUILD" + "|"
        + "TEST_ENVIRONMENT" + "|" + "TEST_CASE" + "             |" + "TEST_STATUS" + "    |"
        + "TEST_PLATFORM" + "|" + "TEST_DEVICE" + "|" + "DB_INSERT_TIME");
    while (rs.next())
      System.out.println(rs.getString("TEST_TIME") + "|" + rs.getString("TEST_BRAND") + "       |"
          + rs.getString("TEST_BUILD") + "        |" + rs.getString("TEST_ENVIRONMENT")
          + "            |" + rs.getString("TEST_CASE") + "            |"
          + rs.getString("TEST_STATUS") + "   |" + rs.getString("TEST_PLATFORM") + "  |"
          + rs.getString("TEST_DEVICE") + "  |" + rs.getString("DB_INSERT_TIME"));

    // step5 close the connection object
    con.close();

    String buildNo = System.getenv("BUILD_NUMBER");
    if (buildNo != null)
      System.out.println("Build Number:" + buildNo);

  }


}
