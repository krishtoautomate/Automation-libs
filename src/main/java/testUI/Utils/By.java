package testUI.Utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

public class By {

  String automationType = "";
  String DESKTOP_PLATFORM = "";

  protected AppiumDriver driver;


  public org.openqa.selenium.By byText(String text) {
    if (!automationType.equals(DESKTOP_PLATFORM))
      // if (driver.size() != 0 && !getDriver().isBrowser())
      return org.openqa.selenium.By.xpath("//*[contains(@text,'" + text + "')]");
    return MobileBy.linkText(text);
  }

  public org.openqa.selenium.By byId(String id) {
    return MobileBy.id(id);
  }

  public org.openqa.selenium.By byXpath(String xpath) {
    return MobileBy.xpath(xpath);
  }

  public org.openqa.selenium.By byCssSelector(String cssSelector) {
    return MobileBy.cssSelector(cssSelector);
  }

  public org.openqa.selenium.By byName(String name) {
    return MobileBy.name(name);
  }

  // public org.openqa.selenium.By byAttribute(String attribute, String value) {
  // return Selectors.byAttribute(attribute, value);
  // }

  // public org.openqa.selenium.By byValue(String value) {
  // return Selectors.byValue(value);
  // }

  public String byAccesibilityId(String value) {
    return "accessibilityId: " + value;
  }

  public String byiOSPredicate(String value) {
    return "predicate: " + value;
  }

  public String byAndroidUIAutomator(String value) {
    return "androidUIAutomator: " + value;
  }

  public String byClassChain(String value) {
    return "classChain: " + value;
  }

  public String byMobileClassName(String value) {
    return "className: " + value;
  }

  public String byMobileXpath(String value) {
    return "xpath: " + value;
  }

  public String byMobileId(String value) {
    return "id: " + value;
  }

  public String byMobileCss(String value) {
    return "css: " + value;
  }

  public String byMobileName(String value) {
    return "name: " + value;
  }
}
