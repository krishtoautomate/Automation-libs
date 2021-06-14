package com.DeviceManager;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.Utilities.ITestBase;

public class ConnectedDevices implements ITestBase {

  public static void main(String[] args) throws Exception {

    DeviceDAO deviceDAO = new DeviceDAO();
    // DeviceinfoProviderOld deviceInfo = new DeviceinfoProviderOld();

    // deviceInfo.setDevices(devices.getAllIOSDevicesInfo());
    // deviceInfo.setDevices(devices.getAllAndroidDevicesInfo());

    System.out.println(
        "---------------------------------------------------------------------------------------------------------------------------------");
    System.out.printf("%2s %50s %30s %20s %10s %10s", "S.NO", "UDID", "DEVICE NAME", "OS",
        "OS-VERSION", "BRAND");
    System.out.println();
    System.out.println(
        "---------------------------------------------------------------------------------------------------------------------------------");

    ArrayList<String> deviceList = deviceDAO.getIdevices();

    deviceList.addAll(deviceDAO.getADBdevices());
    int i = 1;
    for (String udid : deviceList) {
      i++;
      DeviceDAO iosDevicesList = new DeviceDAO(udid);
      System.out.format("%2d %50s %30s %20s %10s %10s", i, udid, iosDevicesList.getDeviceName(),
          iosDevicesList.getOs(), iosDevicesList.getosVersion(), iosDevicesList.getBrand());
      System.out.println();
      System.out.println(
          "--------------------------------------------------------------------------------------------------------------------------------");
    }


  }

  public JSONArray getAllIOSDevicesInfo() {
    DeviceDAO deviceDAO = new DeviceDAO();
    JSONObject deviceInfo = new JSONObject();
    JSONArray devices = new JSONArray();

    ArrayList<String> deviceList = deviceDAO.getIdevices();

    for (String device : deviceList) {
      deviceInfo = getAndroidDeviceInfo(device);
      devices.put(deviceInfo);
    }
    return devices;
  }

  public JSONArray getAllAndroidDevicesInfo() {
    DeviceDAO deviceDAO = new DeviceDAO();
    JSONObject deviceInfo = new JSONObject();
    JSONArray devices = new JSONArray();

    ArrayList<String> deviceList = deviceDAO.getADBdevices();

    for (String device : deviceList) {
      deviceInfo = getAndroidDeviceInfo(device);
      devices.put(deviceInfo);
    }

    return devices;
  }

  public JSONObject getDeviceIosInfo(String udid) {

    DeviceDAO deviceinfoProvider = new DeviceDAO(udid);

    String brand = deviceinfoProvider.getBrand();
    String os = deviceinfoProvider.getOs();
    String osVersion = deviceinfoProvider.getosVersion();
    String deviceModel = deviceinfoProvider.getDeviceModel();
    String deviceName = deviceinfoProvider.getDeviceName();

    JSONObject adbDevices = new JSONObject();
    adbDevices.put("udid", udid);
    adbDevices.put("name", deviceName);
    adbDevices.put("os", os);
    adbDevices.put("osVersion", osVersion);
    adbDevices.put("brand", brand);
    adbDevices.put("deviceModel", deviceModel);

    return adbDevices;
  }

  private JSONObject getAndroidDeviceInfo(String deviceID) {

    DeviceDAO deviceinfoProvider = new DeviceDAO(deviceID);

    String deviceModel = deviceinfoProvider.getDeviceModel();
    String brand = deviceinfoProvider.getBrand();
    String osVersion = deviceinfoProvider.getosVersion();
    String deviceName = brand + " " + deviceModel;
    String os = deviceinfoProvider.getOs();

    JSONObject adbDevices = new JSONObject();
    adbDevices.put("udid", deviceID);
    adbDevices.put("name", deviceName);
    adbDevices.put("os", os);
    adbDevices.put("osVersion", osVersion);
    adbDevices.put("brand", brand);
    adbDevices.put("deviceModel", deviceModel);

    return adbDevices;
  }

}

