package com.Listeners;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class EventListener implements WebDriverListener {


  @Override
  public void beforeClick(WebElement element) {
      System.out.println(""+element.getText());
  }


}
