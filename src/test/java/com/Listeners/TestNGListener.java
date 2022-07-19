package com.Listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestNGListener extends TestListenerAdapter implements ISuiteListener, ITestListener,
    IInvokedMethodListener {

  @Override
  public void onTestStart(ITestResult testResult) {

  }

  @Override
  public synchronized void onTestSuccess(ITestResult testResult) {

  }

  @Override
  public synchronized void onTestFailure(ITestResult testResult) {

  }

  @Override
  public synchronized void onTestSkipped(ITestResult testResult) {

  }

  @Override
  public void onStart(ISuite suite) {

  }

  @Override
  public void onFinish(ISuite suite) {

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
