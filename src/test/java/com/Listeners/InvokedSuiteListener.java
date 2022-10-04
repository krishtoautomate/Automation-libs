package com.Listeners;

import com.ReportManager.LoggerManager;
import com.base.Jira;
import com.base.Log;
import com.deviceinformation.DeviceInfo;
import com.deviceinformation.DeviceInfoImpl;
import com.deviceinformation.device.DeviceType;
import com.deviceinformation.model.Device;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class InvokedSuiteListener extends TestListenerAdapter implements ISuiteListener {

  private static Logger log = Logger.getLogger(InvokedSuiteListener.class.getName());

  @Override
  public void onFinish(ISuite suite) {

  }

  @Override
  public void onStart(ISuite suite) {

    DeviceInfo deviceInfo = new DeviceInfoImpl(DeviceType.ALL);

    System.out.println(
        "---------------------------------------------------------------------------------------------------------------------------------");
    System.out.printf("%2s %50s %30s %20s %10s", "S.NO", "UDID", "DEVICE NAME", "OS", "OS-VERSION");
    System.out.println();
    System.out.println(
        "---------------------------------------------------------------------------------------------------------------------------------");

    List<Device> deviceList = deviceInfo.getDevices();

    int i = 0;
    for (Device device : deviceList) {
      i++;
      System.out.format("%2d %50s %30s %20s %10s", i, device.getUniqueDeviceID(),
          device.getDeviceName(), device.getDeviceProductName(), device.getProductVersion());
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
