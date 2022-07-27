package com.Listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestNGListener extends TestListenerAdapter implements ISuiteListener, ITestListener,
    IInvokedMethodListener {

  XmlSuite mySuite = new XmlSuite();
  List<XmlSuite> mySuites = new ArrayList<XmlSuite>();//suites
  List<XmlTest> myTests = new ArrayList<XmlTest>();//tests
  //  XmlTest xmlTest = new XmlTest(mySuite); //test
//  List<XmlClass> myClasses = new ArrayList<XmlClass>();//classes
  TestNG myTestNG = new TestNG();

  @Override
  public void onTestStart(ITestResult testResult) {
//    Map<String, String> testParams = testResult.getTestContext().getCurrentXmlTest()
//        .getAllParameters();
    System.out.println("TestName on start : " + testResult.getTestContext().getName());

  }



  @Override
  public synchronized void onTestSuccess(ITestResult testResult) {
    System.out.println("TestName success : " + testResult.getTestContext().getClass().getName());
  }

  @Override
  public synchronized void onTestFailure(ITestResult testResult) {
    System.out.println("TestName failure : " + testResult.getTestContext().getClass().getName());
  }

  @Override
  public synchronized void onTestSkipped(ITestResult testResult) {

    System.out.println("TestName on skipped : " + testResult.getTestContext().getName());
    System.out.println("Skipped class : " + testResult.getTestClass().getName());

//    // Test name
//    XmlTest xmlTest = new XmlTest(mySuite); //test
//    xmlTest.setName(testResult.getTestContext().getName());
//
//    // Test parameters
//    xmlTest.addParameter("platForm", testResult.getTestContext().getCurrentXmlTest().getAllParameters().get("platForm"));
//    xmlTest.addParameter("udid", testResult.getTestContext().getCurrentXmlTest().getAllParameters().get("udid"));
//    xmlTest.addParameter("REMOTE_HOST", testResult.getTestContext().getCurrentXmlTest().getAllParameters().get("REMOTE_HOST"));
//
//    // Add Classes
//    List<XmlClass> myClasses = new ArrayList<XmlClass>();
//
//    myClasses.add(new XmlClass(testResult.getTestClass().getName()));
//
//    xmlTest.setXmlClasses(myClasses);
//
//    myTests.add(xmlTest);

  }

  @Override
  public void onStart(ISuite suite) {
    System.out.println("suite start : " + suite.getName());

    mySuite.setName(suite.getName());
    mySuite.setParallel(XmlSuite.ParallelMode.TESTS);
    mySuite.setThreadCount(45);


  }

  @Override
  public void onFinish(ISuite suite) {
    System.out.println("suite end : " + suite.getName());

//    // add the list of tests to your Suite.
//    mySuite.setTests(myTests);
//
//    mySuites.add(mySuite);
//
//    myTestNG.setXmlSuites(mySuites);
//    mySuite.setFileName("myTemp.xml");
//    mySuite.addListener("com.Listeners.TestListener");
//
//    System.out.println(mySuite.toXml());


    suite.getResults().values().forEach(each ->{
      ITestContext testObj = each.getTestContext();
      System.out.println(testObj.getSkippedTests());
    });






  }

  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    // TODO Auto-generated method stub

  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    // TODO Auto-generated method stub
  }

}
