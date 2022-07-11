package com.Driver;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public interface IBaseDriver {


  AppiumDriver getDriver();

  boolean isAndroid();

  boolean isiOS();

  void click(By by, String elementDescription);

  void sendKeys(By by, String keys, String elementDescription);

  boolean isElementDisplayed(By by);

  boolean isElementsDisplayed(By by);

  void tapByElement(By by);

}
