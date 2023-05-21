package other.testcases;

import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class RunnerDemo {

  public static void main(String[] args) {
    // TODO Auto-generated method stub

    // Create an instance of XML Suite and assign a name for it.
    XmlSuite mySuite = new XmlSuite();
    mySuite.setName("MySuite");
    mySuite.setParallel(XmlSuite.ParallelMode.TESTS);
    mySuite.setThreadCount(50);

    /*
     * Test1
     */
    // Create an instance of XmlTest and assign a name for it.
    XmlTest myTest = new XmlTest(mySuite);
    myTest.setName("MyTest");

    myTest.addParameter("platForm", "IOS");
    myTest.addParameter("udid", "xxxx");
    myTest.addParameter("env", "PROD");
    // myTest.addParameter("p_Testdata", "src/test/resources/AndroidTestData.txt");

    // Create a list which can contain the classes that you want to run.
    List<XmlClass> myClasses = new ArrayList<XmlClass>();
    // myClasses.add(new XmlClass("other.testcases.PlayStore"));
    myClasses.add(new XmlClass("other.testcases.TestFlight"));

    // Assign that to the XmlTest Object created earlier.
    myTest.setXmlClasses(myClasses);

    /*
     * Test2
     */
    XmlTest myTest2 = new XmlTest(mySuite);
    myTest2.setName("MyTest2");

    myTest2.addParameter("platForm", "Android");
    myTest2.addParameter("udid", "xxxx");
    myTest2.addParameter("env", "PROD");
    // myTest2.addParameter("p_Testdata", "src/test/resources/AndroidTestData.txt");

    List<XmlClass> myClasses1 = new ArrayList<XmlClass>();
    myClasses1.add(new XmlClass("other.testcases.PlayStore"));
    // myClasses.add(new XmlClass("other.testcases.TestFlight"));

    myTest2.setXmlClasses(myClasses1);

    // Create a list of XmlTests and add the Xmltest you created earlier to it.
    List<XmlTest> myTests = new ArrayList<XmlTest>();

    myTests.add(myTest);
    myTests.add(myTest2);

    // add the list of tests to your Suite.
    mySuite.setTests(myTests);

    // Add the suite to the list of suites.
    List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
    mySuites.add(mySuite);

    TestNG myTestNG = new TestNG();
    myTestNG.setXmlSuites(mySuites);
    mySuite.setFileName("myTemp.xml");
    // mySuite.setThreadCount(1);

    System.out.println(mySuite.toXml());

    // myTestNG.run();

  }

}
