package testUI.elements;

import io.appium.java_client.MobileBy;

public class TheElement {

  public static TheElement theElement() {
    return new TheElement();
  }

  public UIElement withText(String text) {
    return TestUI.E(MobileBy.linkText(text));
  }

  public UIElement withId(String id) {
    return TestUI.E(MobileBy.id(id));
  }

  // public UIElement withValue(String value) {
  // return TestUI.E(byValue(value));
  // }

  public UIElement withXpath(String xpath) {
    return TestUI.E(MobileBy.xpath(xpath));
  }
}
