package com.Driver;

import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import java.net.URL;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import com.base.TestBase;

public class BaseDriver extends TestBase implements IBaseDriver {


  //  protected ExtentTest test;
//
//
//  public BaseDriver(AppiumDriver driver){
//    driver = driver;
//  }

//  @Override
  public AppiumDriver getDriver() {
    return driver;
  }

//  @Override
  public boolean isAndroid() {
    return driver instanceof AndroidDriver;
  }

//  @Override
  public boolean isiOS() {
    return !(driver instanceof AndroidDriver);
  }

//  @Override
  public void click(By by, String elementDescription) {
    try {
      driver.findElement(by).click();
    } catch (Exception e) {
      String message = elementDescription + " is not found";
//      test.fail(message);
      Assert.fail(message);
    }
//    test.pass(elementDescription + " : isClicked");
  }

//  @Override
  public void sendKeys(By by, String keys, String elementDescription) {
    try {
      driver.findElement(by).sendKeys(keys);
    } catch (Exception e) {
      String message = elementDescription + " is not found";
//      test.fail(message);
      Assert.fail(message);
    }
//    test.pass(elementDescription);
  }

//  @Override
  public boolean isElementDisplayed(By by) {
    try {
      return driver.findElement(by).isDisplayed();
    } catch (Exception ign) {
      //ignore
    }
    return false;
  }

//  @Override
  public boolean isElementsDisplayed(By by) {
    try {
      return driver.findElement(by).isDisplayed();
    } catch (Exception ign) {
      //ignore
    }
    return false;
  }

//  @Override
  public void tapByElement(By by) {

  }
//
//  @Override
//  public void get(String url) {
//    navigate().to(url);
//  }

//  @Override
//  public String getCurrentUrl() {
//    return driver.getCurrentUrl();
//  }
//
//  @Override
//  public String getTitle() {
//    return driver.getTitle();
//  }
//
//  @Override
//  public <T extends WebElement> List<T> findElements(By by) {
//    return driver.findElements(by);
//  }
//
//  @Override
//  public <T extends WebElement> T findElement(By by) {
//    return (T) driver.findElements(by).get(0);
//  }

//  @Override
//  public String getPageSource() {
//    return null;
//  }
//
//  @Override
//  public void close() {
//    driver.close();
//  }
//
//  @Override
//  public void quit() {
//    driver.quit();
//  }
//
//  @Override
//  public Set<String> getWindowHandles() {
//    return null;
//  }
//
//  @Override
//  public String getWindowHandle() {
//    return null;
//  }
//
//  @Override
//  public TargetLocator switchTo() {
//    return driver.switchTo();
//  }
//
//  @Override
//  public Navigation navigate() {
//    return driver.navigate();
//  }
//
//  @Override
//  public Options manage() {
//    return driver.manage();
//  }
}
