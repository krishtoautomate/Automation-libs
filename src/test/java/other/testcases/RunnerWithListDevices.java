package other.testcases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class RunnerWithListDevices {

  // private static Logger log = Logger.getLogger(Class.class.getName());

  @Test
  public void testrunner() {
    // TODO Auto-generated method stub

    /*
     * Common
     */

    ITestResult iTestResult = Reporter.getCurrentTestResult();
    Map<String, String> testParams =
        iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

    String remoteHost =
        testParams.get("REMOTE_HOST") != null ? testParams.get("REMOTE_HOST") : "localhost";

    // Create an instance of XML Suite and assign a name for it.
    XmlSuite mySuite = new XmlSuite();
    mySuite.setName("MySuite");
    mySuite.setParallel(XmlSuite.ParallelMode.TESTS);
    // mySuite.addListener("com.Listeners.InvokedSuiteListener");

    List<XmlTest> myTests = new ArrayList<XmlTest>();

    /*
     * Test1
     */

    String update = System.getenv("UPDATE");

    if (update == null || update.length() == 0) {
      update = "All";
    }

    String udids = System.getenv("UDID");
    String[] iosDeviceList = udids.split("\n");

    if (update.equalsIgnoreCase("iOS") || update.equalsIgnoreCase("All")) {

      // for (int i = 0; i < iosDeviceList.size(); i++) {
      int i = 0;
      for (String deviceId : iosDeviceList) {
        i++;

        // Create an instance of XmlTest and assign a name for it.
        XmlTest iosTest = new XmlTest(mySuite);

        // Test name
        iosTest.setName("TestFlight_" + i);

        // Test parameters
        iosTest.addParameter("platForm", "IOS");
        iosTest.addParameter("udid", deviceId.trim());
        iosTest.addParameter("REMOTE_HOST", remoteHost);// "localhost"

        // Create classes
        List<XmlClass> myClasses = new ArrayList<XmlClass>();
        myClasses.add(new XmlClass("other.testcases.TestFlight"));

        // Add Classes
        iosTest.setXmlClasses(myClasses);

        // Add Test
        myTests.add(iosTest);


      }
    }

//    /*
//     * Test2
//     */
//
//    // get connected device list
//
//    DeviceInfo androidDeviceInfo = new DeviceInfoImpl(DeviceType.ANDROID);
//
//    if (androidDeviceInfo.anyDeviceConnected()) {
//
//      List<Device> androidDeviceList = androidDeviceInfo.getDevices();
//
//      if (update.equalsIgnoreCase("Android") || update.equalsIgnoreCase("All")) {
//
//        // for (int i = 0; i < androidDeviceList.size(); i++) {
//        int i = 0;
//        for (Device device : androidDeviceList) {
//          i++;
//
//          if (device.getUniqueDeviceID() != null) {
//            // Create Test
//            XmlTest androidTests = new XmlTest(mySuite);
//
//            // Name test
//            androidTests.setName("PlayStore_" + i);
//
//            // Add parameters
//            androidTests.addParameter("platForm", "Android");
//            androidTests.addParameter("udid", device.getUniqueDeviceID());
//            androidTests.addParameter("REMOTE_HOST", remoteHost);
//
//            // Create classes
//            List<XmlClass> myClasses2 = new ArrayList<XmlClass>();
//            myClasses2.add(new XmlClass("other.testcases.PlayStore"));
//
//            // add classes
//            androidTests.setXmlClasses(myClasses2);
//
//            // add test to tests
//            myTests.add(androidTests);
//          }
//
//        }
//
//      }
//    }

    /*
     * Common
     */
    // add the list of tests to your Suite.
    mySuite.setTests(myTests);
    mySuite.setThreadCount(15);

    // Add the suite to the list of suites.
    List<XmlSuite> mySuites = new ArrayList<XmlSuite>();

    TestNG myTestNG = new TestNG();

    mySuite.setFileName("myTemp.xml");
//    mySuite.addListener("com.Listeners.InvokedSuiteListener");
    mySuite.addListener("com.Listeners.TestListener");

    mySuites.add(mySuite);

    myTestNG.setXmlSuites(mySuites);

    System.out.println(mySuite.toXml());

    myTestNG.run();

  }

}
