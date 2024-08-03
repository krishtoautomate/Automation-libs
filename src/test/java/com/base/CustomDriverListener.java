package com.base;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class CustomDriverListener implements WebDriverListener {

    public void beforeClick(WebElement element) {
        System.out.println("Before clicking on element.");
    }
}
