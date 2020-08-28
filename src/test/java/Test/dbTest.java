package Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbTest {

	public static void main(String[] args) throws SQLException, ClassNotFoundException  {
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
//		Class.forName("oracle.jdbc.OracleDriver");
		
//		JDBC Connection String: jdbc:oracle:thin:@//servername.com:port#/sid
		String sbURL = "jdbc:oracle:thin:@//mtrlpqdc2ae-034.bell.corp.bce.ca:1521/MONITOR";
		Connection con = DriverManager.getConnection(sbURL,"AUTOMATION_FEED","iLUVr0b0tS");
		
		Statement stmt = con.createStatement();
		
//		ResultSet insertQuery = stmt.executeQuery("INSERT INTO BELLCA.AUTO_TBL_DATA(TEST_TIME ,TEST_BRAND ,TEST_BUILD , TEST_ENVIRONMENT ,TEST_CASE ,TEST_STATUS) "
//				+ "VALUES "+
//				"(" + 
//				"TO_TIMESTAMP('2020-08-20 12:42:02','YYYY-MM-DD HH24:MI:SS')," + 
//				"'Lucky'," + 
//				"'1'," + 
//				"'PROD'," + 
//				"'Login_Test'," + 
//				"'PASS'" + 
//				")");
		ResultSet rs = stmt.executeQuery("SELECT * FROM BELLCA.AUTO_TBL_DATA");

//		System.out.println(rs.getMetaData().getColumnName(1));
		System.out.println("TEST_TIME"+"          |"+"TEST_BRAND"+"|"+"TEST_BUILD"+"|"
				+"TEST_ENVIRONMENT"+"|"+"TEST_CASE"+"   |"+"TEST_STATUS"+"|"+"DB_INSERT_TIME");
		while(rs.next())
			System.out.println(rs.getString("TEST_TIME")+"|"+rs.getString("TEST_BRAND")+"  |"+rs.getString("TEST_BUILD")+"  |"
		+rs.getString("TEST_ENVIRONMENT")+"  |"+rs.getString("TEST_CASE")+"|"+rs.getString("TEST_STATUS")+"  |"+rs.getString("DB_INSERT_TIME"));  
		  
		//step5 close the connection object  
		con.close(); 
		
		
		System.out.println("Build Number:"+System.getenv("BUILD_NUMBER"));
			  
		

	}

}
