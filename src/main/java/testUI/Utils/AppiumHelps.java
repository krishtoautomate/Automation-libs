package testUI.Utils;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;


public class AppiumHelps {

  protected AppiumDriver driver;

  // public boolean exists(By element, String accessibilityId) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).isDisplayed();
  // return getMobileElement(accessibilityId).isDisplayed();
  // } catch (WebDriverException e) {
  // if (e.getMessage().contains("Unable to locate element")) {
  // return false;
  // } else {
  // throw new WebDriverException(e);
  // }
  // }
  // }

  public boolean exists(By element, String accessibilityId, int index) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).isDisplayed();
      return ((MobileElement) getMobileElementList(accessibilityId).get(index)).isDisplayed();
    } catch (WebDriverException e) {
      if (e.getMessage().contains("Unable to locate element")) {
        return false;
      } else {
        throw new WebDriverException(e);
      }
    }
  }

  // public boolean visible(By element, String accessibilityId) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).isDisplayed();
  // return getMobileElement(accessibilityId).isDisplayed();
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  public boolean visible(By element, String accessibilityId, int index) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).isDisplayed();
      return ((MobileElement) getMobileElementList(accessibilityId).get(index)).isDisplayed();
    } catch (Exception var4) {
      return false;
    }
  }

  // public boolean enable(By element, String accessibilityId) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).isEnabled();
  // return getMobileElement(accessibilityId).isEnabled();
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  public boolean enable(By element, String accessibilityId, int index) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).isEnabled();
      return ((MobileElement) getMobileElementList(accessibilityId).get(index)).isEnabled();
    } catch (Exception var4) {
      return false;
    }
  }

  // public boolean value(By element, String accessibilityId, String text) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).getAttribute("value").equals(text);
  // return getMobileElement(accessibilityId).getAttribute("value").equals(text);
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  public boolean value(By element, String accessibilityId, int index, String text) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).getAttribute("value")
            .equals(text);
      return ((MobileElement) getMobileElementList(accessibilityId).get(index))
          .getAttribute("value").equals(text);
    } catch (Exception var4) {
      return false;
    }
  }

  public boolean attribute(By element, String accessibilityId, int index, String Attribute,
      String text) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).getAttribute(Attribute)
            .equals(text);
      return ((MobileElement) getMobileElementList(accessibilityId).get(index))
          .getAttribute(Attribute).equals(text);
    } catch (Exception var4) {
      return false;
    }
  }

  public boolean attribute(By element, String accessibilityId, int index, String Attribute) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index))
            .getAttribute(Attribute) != null;
      return ((MobileElement) getMobileElementList(accessibilityId).get(index))
          .getAttribute(Attribute) != null;
    } catch (Exception var4) {
      return false;
    }
  }

  public boolean emptyAttribute(By element, String accessibilityId, int index, String Attribute) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index))
            .getAttribute(Attribute) == null
            || ((MobileElement) driver.findElements(element).get(index)).getAttribute(Attribute)
                .isEmpty();
      return ((MobileElement) getMobileElementList(accessibilityId).get(index))
          .getAttribute(Attribute) == null
          || ((MobileElement) getMobileElementList(accessibilityId).get(index))
              .getAttribute(Attribute).isEmpty();
    } catch (Exception var4) {
      return false;
    }
  }

  // public boolean attribute(By element, String accessibilityId, String Attribute, String text) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).getAttribute(Attribute).equals(text);
  // return getMobileElement(accessibilityId).getAttribute(Attribute).equals(text);
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  // public boolean attribute(By element, String accessibilityId, String Attribute) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).getAttribute(Attribute) != null;
  // return getMobileElement(accessibilityId).getAttribute(Attribute) != null;
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  // public boolean emptyAttribute(By element, String accessibilityId, String Attribute) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).getAttribute(Attribute) == null
  // || driver.findElement(element).getAttribute(Attribute).isEmpty();
  // return getMobileElement(accessibilityId).getAttribute(Attribute) == null
  // || getMobileElement(accessibilityId).getAttribute(Attribute).isEmpty();
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  public boolean equalsText(By element, String accessibilityId, int index, String text) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).getText().contains(text);
      return ((MobileElement) getMobileElementList(accessibilityId).get(index)).getText()
          .contains(text);
    } catch (Exception var4) {
      return false;
    }
  }

  public boolean emptyText(By element, String accessibilityId, int index) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).getText().isEmpty();
      return ((MobileElement) getMobileElementList(accessibilityId).get(index)).getText().isEmpty();
    } catch (Exception var4) {
      return false;
    }
  }

  // public boolean equalsText(By element, String accessibilityId, String text) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).getText().equals(text);
  // return getMobileElement(accessibilityId).getText().equals(text);
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  // public boolean emptyText(By element, String accessibilityId) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).getText().isEmpty();
  // return getMobileElement(accessibilityId).getText().isEmpty();
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  // public boolean containsText(By element, String accessibilityId, String text) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).getText().contains(text);
  // return getMobileElement(accessibilityId).getText().contains(text);
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  public boolean containsText(By element, String accessibilityId, int index, String text) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).getText().contains(text);
      return ((MobileElement) getMobileElementList(accessibilityId).get(index)).getText()
          .contains(text);
    } catch (Exception var4) {
      return false;
    }
  }

  public boolean containsAttribute(By element, String accessibilityId, int index, String attr,
      String text) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).getAttribute(attr)
            .contains(text);
      return ((MobileElement) getMobileElementList(accessibilityId).get(index)).getText()
          .contains(text);
    } catch (Exception var4) {
      return false;
    }
  }

  // public boolean containsTextNoCaseSensitive(By element, String accessibilityId, String text) {
  // try {
  // if (accessibilityId == null || accessibilityId.isEmpty())
  // return driver.findElement(element).getText().toLowerCase()
  // .contains(text.toLowerCase());
  // return getMobileElement(accessibilityId).getText().toLowerCase().contains(text.toLowerCase());
  // } catch (Exception var4) {
  // return false;
  // }
  // }

  public boolean containsTextNoCaseSensitive(By element, String accessibilityId, int index,
      String text) {
    try {
      if (accessibilityId == null || accessibilityId.isEmpty())
        return ((MobileElement) driver.findElements(element).get(index)).getText().toLowerCase()
            .contains(text.toLowerCase());
      return ((MobileElement) getMobileElementList(accessibilityId).get(index)).getText()
          .toLowerCase().contains(text.toLowerCase());
    } catch (Exception var4) {
      return false;
    }
  }

  public void sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException var3) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(var3);
    }
  }

  private List getMobileElementList(String locator) {
    switch (locator.split(": ")[0]) {
      // case "accessibilityId":
      // return driver.findElementsByAccessibilityId(locator.split(": ")[1]);
      // case "className":
      // return driver.findElementsByClassName(locator.split(": ")[1]);
      // case "androidUIAutomator":
      // return getAndroidTestUIDriver().findElementsByAndroidUIAutomator(locator.split(": ")[1]);
      // case "predicate":
      // return getIOSTestUIDriver().findElementsByIosNsPredicate(locator.split(": ")[1]);
      // case "classChain":
      // return getIOSTestUIDriver().findElementsByIosClassChain(locator.split(": ")[1]);
      // case "name":
      // return driver.findElementsByName(locator.split(": ")[1]);
      // case "xpath":
      // return driver.findElementsByXPath(locator.split(": ")[1]);
      // case "id":
      // return driver.findElementsById(locator.split(": ")[1]);
      // case "css":
      // return driver.findElementsByCssSelector(locator.split(": ")[1]);
      default:
        // UIAssert("The type of locator is not valid! " + locator.split(": ")[0], false);
        return new ArrayList();
    }
  }

  // private MobileElement getMobileElement(String locator) {
  // switch (locator.split(": ")[0]) {
  // case "accessibilityId":
  // return (MobileElement) driver.findElementByAccessibilityId(locator.split(": ")[1]);
  // case "className":
  // return (MobileElement) driver.findElementByClassName(locator.split(": ")[1]);
  // case "androidUIAutomator":
  // return (MobileElement) getAndroidTestUIDriver()
  // .findElementByAndroidUIAutomator(locator.split(": ")[1]);
  // case "predicate":
  // return (MobileElement) getIOSTestUIDriver()
  // .findElementByIosNsPredicate(locator.split(": ")[1]);
  // case "classChain":
  // return (MobileElement) getIOSTestUIDriver()
  // .findElementByIosClassChain(locator.split(": ")[1]);
  // case "name":
  // return (MobileElement) driver.findElementByName(locator.split(": ")[1]);
  // case "xpath":
  // return (MobileElement) getIOSTestUIDriver().findElementByXPath(locator.split(": ")[1]);
  // case "id":
  // return (MobileElement) driver.findElementById(locator.split(": ")[1]);
  // case "css":
  // return (MobileElement) driver.findElementByCssSelector(locator.split(": ")[1]);
  // default:
  // UIAssert("The type of locator is not valid! " + locator.split(": ")[0], false);
  // return (MobileElement) getAndroidTestUIDriver().findElement("", "");
  // }
  // }
}
