package com.Listeners;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
/**
 * Created by Krish on 21.07.2018.
 */
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.DeviceManager.DeviceDAO;

public class InvokedSuiteListener extends TestListenerAdapter implements ISuiteListener {

  private static Logger log = Logger.getLogger(Class.class.getName());

  @Override
  public void onFinish(ISuite suite) {

  }

  @Override
  public void onStart(ISuite suite) {

    DeviceDAO deviceDAO = new DeviceDAO();

    System.out.println(
        "---------------------------------------------------------------------------------------------------------------------------------");
    System.out.printf("%2s %50s %30s %20s %10s %10s", "S.NO", "UDID", "DEVICE NAME", "OS",
        "OS-VERSION", "BRAND");
    System.out.println();
    System.out.println(
        "---------------------------------------------------------------------------------------------------------------------------------");

    ArrayList<String> deviceList = deviceDAO.getIdevices(); // iOS devices

    deviceList.addAll(deviceDAO.getADBdevices()); // Android devices
    int i = 0;
    for (String udid : deviceList) {
      i++;
      DeviceDAO iosDevicesList = new DeviceDAO(udid);
      System.out.format("%2d %50s %30s %20s %10s %10s", i, udid, iosDevicesList.getDeviceName(),
          iosDevicesList.getOs(), iosDevicesList.getosVersion(), iosDevicesList.getBrand());
      System.out.println();
      System.out.println(
          "--------------------------------------------------------------------------------------------------------------------------------");
    }

  }


  @Override
  public void onFinish(ITestContext context) {
    Iterator<ITestResult> skippedTestCases = context.getSkippedTests().getAllResults().iterator();
    while (skippedTestCases.hasNext()) {
      ITestResult skippedTestCase = skippedTestCases.next();
      ITestNGMethod method = skippedTestCase.getMethod();
      if (context.getSkippedTests().getResults(method).size() > 0) {
        log.info("Removing:" + skippedTestCase.getTestClass().toString());
        skippedTestCases.remove();
      }
    }
  }

}
