package other.testcases;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.DeviceManager.ConnectedDevices;

public class RunnerWithAllConnectedDevices {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ConnectedDevices devices = new ConnectedDevices();
		
		System.out.println(devices.getAllAndroidDevicesInfo());
		System.out.println(devices.getAllIOSDevicesInfo());
		System.out.println(devices.getIdevices().get(0));
		System.out.println(devices.getADBdevices().get(0));
		
		
		/*
	     * Common
	     */
		
		//Create an instance of XML Suite and assign a name for it. 
	      XmlSuite mySuite = new XmlSuite(); 
	      mySuite.setName("MySuite"); 
	      mySuite.setParallel(XmlSuite.ParallelMode.TESTS);  
	      mySuite.setThreadCount(1);
	      
	      
	      
	      
	      
	      //*************************************************
	      
	      
	      /*
	       * Test1
	       */
	      //Create an instance of XmlTest and assign a name for it.  
	      XmlTest myTest = new XmlTest(mySuite); 
	      myTest.setName("MyTest");  
	      
	      myTest.addParameter("platForm", "IOS");
	      myTest.addParameter("udid", "xxxx");
	      myTest.addParameter("env", "PROD");
//	      myTest.addParameter("p_Testdata", "src/test/resources/AndroidTestData.txt");
	      
	    //Create a list which can contain the classes that you want to run.
	      List<XmlClass> myClasses = new ArrayList<XmlClass>();
//	      myClasses.add(new XmlClass("other.testcases.PlayStore"));  
	      myClasses.add(new XmlClass("other.testcases.TestFlight"));  

	      //Assign that to the XmlTest Object created earlier. 
	      myTest.setXmlClasses(myClasses);
	      
	      
	      
	      /*
	       * Test2
	       */
	      XmlTest myTest2 = new XmlTest(mySuite); 
	      myTest2.setName("MyTest2");
	      
	      myTest2.addParameter("platForm", "Android");
	      myTest2.addParameter("udid", "xxxx");
	      myTest2.addParameter("env", "PROD");
//	      myTest2.addParameter("p_Testdata", "src/test/resources/AndroidTestData.txt");
	      
	      List<XmlClass> myClasses2 = new ArrayList<XmlClass>();
	      myClasses2.add(new XmlClass("other.testcases.PlayStore")); 
//	      myClasses.add(new XmlClass("other.testcases.TestFlight"));  
	      
	      myTest2.setXmlClasses(myClasses2);
	      
	      
	      //********************Add all created tests***************************** 
	      
	    //Create a list of XmlTests and add the Xmltest you created earlier to it.
	      List<XmlTest> myTests = new ArrayList<XmlTest>(); //add all tests to this in a loop
	      
	      myTests.add(myTest);   
	      myTests.add(myTest2);  
	      
	      
	      
	    //*************************************************
	    /*
	     * Common
	     */
	    //add the list of tests to your Suite. 
	      mySuite.setTests(myTests);  
	      
	    //Add the suite to the list of suites. 
	      List<XmlSuite> mySuites = new ArrayList<XmlSuite>(); 
	      mySuites.add(mySuite); 
	      
	      TestNG myTestNG = new TestNG();   
	      myTestNG.setXmlSuites(mySuites);
	      mySuite.setFileName("myTemp.xml"); 
//	      mySuite.setThreadCount(1); 
	      
	      System.out.println(mySuite.toXml());
	      
//	      myTestNG.run();

	}

}
