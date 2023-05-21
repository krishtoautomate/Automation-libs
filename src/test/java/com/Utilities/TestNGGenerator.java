package com.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestNGGenerator {

  public static void main(String[] args) throws IOException {
    // TODO Auto-generated method stub

    // Create an instance of XML Suite and assign a name for it.
    XmlSuite mySuite = new XmlSuite();
    mySuite.setName("MySuite");
    mySuite.setParallel(XmlSuite.ParallelMode.TESTS);
    mySuite.setThreadCount(50);

    // Create an instance of XmlTest and assign a name for it.
    XmlTest myTest = new XmlTest(mySuite);
    myTest.setName("MyTest");

    myTest.addParameter("platForm", "NONE");
    myTest.addParameter("p_Testdata", "LM_Database.txt");

    // Create a list which can contain the classes that you want to run.
    List<XmlClass> myClasses = new ArrayList<XmlClass>();

    Path start = Paths.get("src/test/java/Tests");
    try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
      List<String> collect = stream.map(String::valueOf).sorted().collect(Collectors.toList());

      // collect.forEach(System.out::println);

      for (String each : collect) {
        if (each.contains(".java")) {
          // System.out.println(each);
          String className = each.replaceAll("src/test/java/", "");
          className = className.replaceAll(".java", "");
          className = className.replaceAll("/", ".");
          // System.out.println(className);
          if (!isFound(className)) {
            myClasses.add(new XmlClass(className));
          }
        }
      }
    }

    // Assign that to the XmlTest Object created earlier.
    myTest.setXmlClasses(myClasses);

    // Create a list of XmlTests and add the Xmltest you created earlier to it.
    List<XmlTest> myTests = new ArrayList<XmlTest>();

    myTests.add(myTest);

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

  public static boolean isFound(String className) {

    File file = new File("src/test/resources/TestNG.xml");
    try {
      @SuppressWarnings("resource")
      Scanner scanner = new Scanner(file);
      // now read the file line by line...
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.contains(className)) {
          return true;
        }
      }
    } catch (FileNotFoundException e) {
      // handle this
    }
    return false;
  }

}
