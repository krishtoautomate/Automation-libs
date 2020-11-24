package Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.DataManager.TestDataManager;

public class TestData {

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub
		
//		TestDataManager testDataManager = new TestDataManager("src/test/resources/MBM_Database.txt", "MBMobility_TestCases.Login", "ios");
		
		TestDataManager testDataManager = new TestDataManager("src/test/resources/MBM_Database.txt", "MBMobility_TestCases.InValidLogin", "ios");
		
//		System.out.println(testDataManager.getValue("name"));
		System.out.println(testDataManager.getJsonValue(0, "name"));
		
	}

}
