package other.testcases;

import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import com.DeviceManager.DeviceDAO;

public class RunnerWithAllConnectedDevices {

  // private static Logger log = Logger.getLogger(Class.class.getName());

  @Test
  public void testrunner() {
    // TODO Auto-generated method stub

    DeviceDAO deviceDAO = new DeviceDAO();

    /*
     * Common
     */

    // Create an instance of XML Suite and assign a name for it.
    XmlSuite mySuite = new XmlSuite();
    mySuite.setName("MySuite");
    mySuite.setParallel(XmlSuite.ParallelMode.TESTS);
    mySuite.addListener("com.Listeners.InvokedSuiteListener");
    mySuite.setThreadCount(1);

    List<XmlTest> myTests = new ArrayList<XmlTest>();

    /*
     * Test1
     */
    ArrayList<String> iosDeviceList = deviceDAO.getIdevices();


    String update = System.getenv("UPDATE");

    if (update == null || update.length() == 0) {
      update = "All";
    }

    if (update.equalsIgnoreCase("iOS") || update.equalsIgnoreCase("All")) {

      for (int i = 0; i < iosDeviceList.size(); i++) {
        // Create an instance of XmlTest and assign a name for it.
        XmlTest iosTest = new XmlTest(mySuite);

        // Test name
        iosTest.setName("TestFlight_" + i);

        // Test parameters
        iosTest.addParameter("platForm", "IOS");
        iosTest.addParameter("udid", iosDeviceList.get(i));

        // Create classes
        List<XmlClass> myClasses = new ArrayList<XmlClass>();
        myClasses.add(new XmlClass("other.testcases.TestFlight"));

        // Add Classes
        iosTest.setXmlClasses(myClasses);

        // Add Test
        myTests.add(iosTest);
      }
    }

    /*
     * Test2
     */

    // get connected device list
    ArrayList<String> androidDeviceList = deviceDAO.getADBdevices();

    if (update.equalsIgnoreCase("Android") || update.equalsIgnoreCase("All")) {
      for (int i = 0; i < androidDeviceList.size(); i++) {

        // Create Test
        XmlTest androidTests = new XmlTest(mySuite);

        // Name test
        androidTests.setName("PlayStore_" + i);

        // Add parameters
        androidTests.addParameter("platForm", "Android");
        androidTests.addParameter("udid", androidDeviceList.get(i));

        // Create classes
        List<XmlClass> myClasses2 = new ArrayList<XmlClass>();
        myClasses2.add(new XmlClass("other.testcases.PlayStore"));

        // add classes
        androidTests.setXmlClasses(myClasses2);

        // add test to tests
        myTests.add(androidTests);

      }
    }

    /*
     * Common
     */
    // add the list of tests to your Suite.
    mySuite.setTests(myTests);

    // Add the suite to the list of suites.
    List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
    mySuites.add(mySuite);

    TestNG myTestNG = new TestNG();
    myTestNG.setXmlSuites(mySuites);
    mySuite.setFileName("myTemp.xml");
    mySuite.addListener("com.Listeners.InvokedSuiteListener");
    mySuite.addListener("com.Listeners.TestListener");

    System.out.println(mySuite.toXml());

    myTestNG.run();

  }

}
