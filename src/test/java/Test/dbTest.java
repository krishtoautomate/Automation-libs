package Test;

import java.sql.*;

public class dbTest {

	public static void main(String[] args) throws SQLException, ClassNotFoundException  {
		// TODO Auto-generated method stub
		
		Class.forName("com.mysql.jdbc.Driver");
		String sbURL = "jdbc:mysql://mtrlpqdc2ae-034.bell.corp.bce.ca:1521";
		Connection con = DriverManager.getConnection(sbURL,"AUTOMATION_FEED","iLUVr0b0tS");
		
		Statement stmt = con.createStatement();

	}

}
