package com.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface ITestBase {

  /**
   * TRUE - If element is displayed
   */
  public default boolean isElementDisplayed(WebElement element) {
    try {
      return element.isDisplayed();
    } catch (Exception e) {
      // ignore
    }
    return false;
  }



  /**
   * TRUE - If elements are displayed
   */
  public default boolean isElementsDisplayed(List<WebElement> element) {
    Boolean isDisplayed = false;
    try {
      isDisplayed = element.listIterator().hasNext() ? true : false;
    } catch (Exception e) {
      // ignore
    }
    return isDisplayed;
  }

  /**
   * TRUE - If element is isEnabled
   */
  public default boolean isElementEnabled(WebElement element) {
    Boolean isEnabled = false;
    try {
      isEnabled = element.isEnabled() ? true : false;
    } catch (Exception e) {
      // ignore
    }
    return isEnabled;
  }

  /**
   * TRUE - If element is isSelected
   */
  public default boolean isElementSelected(WebElement element) {
    Boolean isSelected = false;
    try {
      isSelected = element.isSelected() ? true : false;
    } catch (Exception e) {
      // ignore
    }
    return isSelected;
  }

  /**
   * Thread sleep for Certain Seconds
   */
  public default void sleep(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      // ignore
    }
  }

  /*
   * Execute Runtime command
   */
  public default String runCommandThruProcess(String command) {
    BufferedReader br;
    String allLine = "";
    Process process = null;

    try {
      process = Runtime.getRuntime().exec(command);

      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);

      br = new BufferedReader(isr);

      String line;

      while ((line = br.readLine()) != null) {
        allLine = allLine + "" + line + "\n";
      }
    } catch (IOException e) {
      // ignore
    }
    return allLine;
  }

  /*
   * Jenkins Build number
   */
  public default String getBuildno() {

    String buildNumber = "";
    try {
      buildNumber = System.getenv("BUILD_NUMBER");
    } catch (Exception e) {
      // ignore
    }
    return buildNumber;
  }

  public default boolean isTextContains(String Actual, String Expected) {

    Boolean isMatching = false;

    String actual = Actual.replaceAll("\n", " ");
    String[] arrOfExpected = Expected.split("\\|");

    for (String expected : arrOfExpected) {
      if (StringUtils.containsIgnoreCase(actual.trim(), expected.trim())
          | StringUtils.containsIgnoreCase(expected, actual.trim()) && !actual.isEmpty()) {
        isMatching = true;
        break;
      }
    }

    return isMatching;
  }

}
