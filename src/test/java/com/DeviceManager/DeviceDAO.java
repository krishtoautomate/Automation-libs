package com.DeviceManager;

import java.util.ArrayList;

import com.Utilities.ITestBase;
import com.base.Constants;

public class DeviceDAO implements ITestBase {

  ArrayList<String> adbDeviceList = new ArrayList<String>();
  ArrayList<String> iDeviceList = new ArrayList<String>();

  public String udid;
  public String brand;
  public String os;
  public String deviceName;
  public String platformName;
  public String osVersion;
  public String deviceModel;
  public String deviceColour;

  public DeviceDAO(String udid) {
    super();
    this.udid = udid;
  }

  public DeviceDAO() {
    super();
  }

  /*
   * get all Android device list
   */
  public ArrayList<String> getADBdevices() {

    runCommandThruProcess(Constants.ADB + " start-server"); // start adb service

    String output = runCommandThruProcess(Constants.ADB + " devices");

    String[] lines = output.split("\n");

    for (int i = 1; i < lines.length; i++) {
      lines[i] = lines[i].split("\\s+")[0];

      this.adbDeviceList.add(lines[i]);
    }

    return this.adbDeviceList;
  }

  /*
   * List all ios devices
   */
  public synchronized ArrayList<String> getIdevices() {

    String output = runCommandThruProcess(Constants.IDEVICE_ID + " -l");

    if (output.length() > 0) {
      String[] lines = output.split("\n");

      for (String device : lines) {
        device.replace("\n", "");
        this.iDeviceList.add(device);
      }
    }
    return this.iDeviceList;
  }

  public synchronized String getBrand() {
    if (this.getOs().equalsIgnoreCase("iPhone OS"))
      this.brand = "Apple";
    else
      this.brand = runCommandThruProcess(
          Constants.ADB + " -s " + this.udid + " shell getprop ro.product.brand").replaceAll("\n",
              "");
    return brand;
  }

  // net.bt.name
  public synchronized String getOs() {
    if (runCommandThruProcess(Constants.ADB + " -s " + this.udid + " shell getprop net.bt.name")
        .replaceAll("\n", "").contains("Android"))
      this.os = "Android";
    else
      this.os = "iPhone OS";

    return os;
  }

  public synchronized String getDeviceModel() {
    if (this.getOs().equalsIgnoreCase("iPhone OS"))
      this.deviceModel =
          runCommandThruProcess(Constants.IDEVICEINFO + " -u" + this.udid + " -k ProductType");
    else
      this.deviceModel = runCommandThruProcess(
          Constants.ADB + " -s " + this.udid + " shell getprop ro.product.model");

    return deviceModel.replaceAll("\\s+", "");
  }

  public static void main(String[] args) {
    DeviceDAO deviceDAO = new DeviceDAO("00008101-001C15A20AD2001E");
    System.out.println(deviceDAO.getDeviceName());
  }

  public synchronized String getDeviceName() {

    if (this.getOs().equalsIgnoreCase("iPhone OS")) {

      String deviceModel = getDeviceModel();

      switch (deviceModel) {
        case "iPhone6,1":
          return "iPhone 5s";
        case "iPhone6,2":
          return "iPhone 5s";
        case "iPhone7,1":
          return "iPhone 6 Plus";
        case "iPhone7,2":
          return "iPhone 6";
        case "iPhone8,1":
          return "iPhone 6s";
        case "iPhone8,2":
          return "iPhone 6s Plus";
        case "iPhone8,4":
          return "iPhone SE";
        case "iPhone9,1":
          return "iPhone 7";
        case "iPhone9,2":
          return "iPhone 7 Plus";
        case "iPhone9,3":
          return "iPhone 7";
        case "iPhone9,4":
          return "iPhone 7 Plus";
        case "iPhone10,1":
          return "iPhone 8";
        case "iPhone10,2":
          return "iPhone 8 Plus";
        case "iPhone10,3":
          return "iPhone X";
        case "iPhone10,4":
          return "iPhone 8";
        case "iPhone10,5":
          return "iPhone 8 Plus";
        case "iPhone10,6":
          return "iPhone X";
        case "iPhone11,2":
          return "iPhone XS";
        case "iPhone11,4":
          return "iPhone XS Max";
        case "iPhone11,6":
          return "iPhone XS Max";
        case "iPhone11,8":
          return "iPhone XR";
        case "iPhone12,1":
          return "iPhone 11";
        case "iPhone12,3":
          return "iPhone 11 Pro";
        case "iPhone12,5":
          return "iPhone 11 Pro Max";
        case "iPhone12,8":
          return "iPhone SE 2";
        case "iPhone13,1":
          return "iPhone 12 Mini";
        case "iPhone13,2":
          return "iPhone 12";
        case "iPhone13,3":
          return "iPhone 12 Pro";
        case "iPhone13,4":
          return "iPhone 12 Pro Max";
        default:
          return deviceModel;
      }
    } else {
      String device = getBrand() + " "
          + runCommandThruProcess(
              Constants.ADB + " -s " + this.udid + " shell getprop ro.product.model")
                  .replaceAll("\n", "");
      this.deviceName = device;
    }
    return deviceName;
  }

  public synchronized String getPlatformName() {
    if (this.getOs().equalsIgnoreCase("iPhone OS"))
      this.platformName = "iOS";
    else
      this.platformName = "Android";

    return platformName;
  }

  public synchronized String getosVersion() {
    if (this.getOs().equalsIgnoreCase("iPhone OS"))
      this.osVersion =
          runCommandThruProcess(Constants.IDEVICEINFO + " -u" + this.udid + " -k ProductVersion");

    else
      this.osVersion = runCommandThruProcess(
          Constants.ADB + " -s " + this.udid + " shell getprop ro.build.version.release");

    return osVersion.replaceAll("\\s+", "");
  }

  public synchronized void uninstall_WDA() {
    if (this.getOs().equalsIgnoreCase("iPhone OS"))
      runCommandThruProcess("/usr/local/bin/ideviceinstaller -u " + this.udid
          + " -U com.facebook.WebDriverAgentRunner.xctrunner");
    else {
      runCommandThruProcess(
          Constants.ADB + " -s " + this.udid + " uninstall io.appium.uiautomator2.server");
      runCommandThruProcess(
          Constants.ADB + " -s " + this.udid + " uninstall io.appium.uiautomator2.server.test");
      runCommandThruProcess(Constants.ADB + " -s " + this.udid + " uninstall io.appium.settings");
    }
  }

}
