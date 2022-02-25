package testUI.collections;

import static testUI.collections.TestUI.EE;
import io.appium.java_client.MobileBy;

public class TheCollection {

  public static class Given {
    public static Given given() {
      return new Given();
    }

    public TheCollection aCollection() {
      return new TheCollection();
    }
  }

  public UICollection withText(String text) {
    return EE(MobileBy.linkText(text));
  }

  public UICollection withId(String id) {
    return EE(MobileBy.id(id));
  }

  // public UICollection withValue(String value) {
  // return EE(byValue(value));
  // }

  public UICollection withXpath(String xpath) {
    return EE(MobileBy.xpath(xpath));
  }
}
