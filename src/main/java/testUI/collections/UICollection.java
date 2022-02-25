package testUI.collections;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import testUI.elements.UIElement;

public interface UICollection {

  UICollection setSelenideCollection(By SelenideElement);

  UICollection setIOSCollection(By iOSElement);

  UICollection setIOSCollection(String accesibilityId);

  UICollection setAndroidCollection(By element);

  UICollection setAndroidCollection(String element);

  // ElementsCollection getSelenideCollection();

  UIElement get(int i);

  UIElement first();

  int size();

  Dimension getSize();

  UIElement findByVisible();

  UICollection waitUntilAllVisible(int seconds);

  UIElement findByText(String text);

  int getLastCommandTime();

  int getTimeErrorBar();

  UIElement findByValue(String value);

  UIElement findByEnabled();

  String asString();
}
