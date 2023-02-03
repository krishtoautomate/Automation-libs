package com.Driver;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class CustomAppiumDriver extends AppiumDriver {

  public CustomAppiumDriver(URL appiumServerUrl, DesiredCapabilities capabilities) {
    super(appiumServerUrl, capabilities);
  }


  public void findElementAndClick(By by) {
    WebElement element = findElement(by);
    element.click();
  }

  public AppiumDriver getDriverInstance() {
    return this;
  }

  public void findElementAndTap(By by) {
    WebElement element = findElement(by);
    // Custom implementation for the tap() method
    actions().moveToElement(element).click().perform();
  }

  public Actions actions() {
    Actions a = new Actions(this);
    return a;
  }

  @Override
  public WebElement findElement(By by) {
    WebElement element = super.findElement(by);
    // Custom logic for finding the element
    return element;
  }

  public void sendKeys(CharSequence... keysToSend) {
    actions().sendKeys(keysToSend);
  }

  public WebElement findElement(By by, String... message) {
    WebElement element = super.findElement(by);
    // Custom logic for finding the element
    return element;
  }

  @Override
  public void get(String url) {
    // Custom logic for navigating to a URL
    super.get(url);
  }
}

