package com.Driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.ElementOption;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

@SuppressWarnings("unchecked")
public class MobiDriver extends AppiumDriver{

  private AppiumDriver driver;
  private final int timeout = 10;

  public MobiDriver(URL remoteAddress,
      Capabilities desiredCapabilities) {
    super(remoteAddress, desiredCapabilities);
    this.driver = driver;
  }

  public boolean isAndroid() {
    return this.driver instanceof AndroidDriver;
  }

  public Boolean isiOS() {
    return !(this.driver instanceof AndroidDriver);
  }

  public boolean isDisplayed(By by) {
    try {
      return findElement(by).isDisplayed();
    } catch (Exception e) {
      // ignore
    }
    return false;
  }

//  public void tapByElement(By by){
//    new TouchAction(this.driver)
//        .tap(ElementOption.element(super.findElement(by))).perform();
//  }
//
//  public void swipeByCoordinates(int startX, int startY, int endX, int endY){
//    new TouchAction(this.driver)
//        .press(ElementOption.point(startX,startY))
//        .moveTo(ElementOption.point(endX, endY))
//        .release().perform();
//  }

  /*
  @param seconds = time in seconds
   */
  public void sleep(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      // ignore
    }
  }

}
