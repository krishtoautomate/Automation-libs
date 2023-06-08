package com.Deviceinformation;

import com.Deviceinformation.exception.DeviceNotFoundException;
import com.Deviceinformation.model.Android;
import com.Deviceinformation.model.Device;
import com.Deviceinformation.model.Ios;
import com.Deviceinformation.model.IosSimulator;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public interface DeviceInfo {

  default boolean anyDeviceConnected() {

    try {
      return getDevices().size() > 0;
    } catch (Exception e) {
      // ignore
    }
    return false;
  }

  default Device getFirstDevice() {
    return getDevices().stream().findFirst().orElse(null);
  }


  default Device getUdid(String udid) {
    if (udid.equalsIgnoreCase("Auto")) {
      return getDevices().stream()
          .filter(device -> udid.equalsIgnoreCase(device.getUniqueDeviceID())).findFirst()
          .orElse(null);
    }
    return getDevices().stream().filter(device -> udid.equalsIgnoreCase(device.getUniqueDeviceID()))
        .findAny().orElse(null);
  }

  default Android getFirstAndroid() {
    return (Android) getDevices().stream()
        .filter(device -> device.getDeviceProductName().contains("Android"))
        .filter(device -> device.getSerialNumber() != null)
        .filter(device -> !device.getModelNumber().contains("SDK")).findFirst().orElse(null);
  }

  default Android getFirstAndroidEmulator() throws IOException, DeviceNotFoundException {
    return (Android) getDevices().stream()
        .filter(device -> device.getDeviceProductName().contains("Android"))
        .filter(device -> device.getSerialNumber() == null)
        .filter(device -> device.getModelNumber().contains("SDK")).findFirst().orElse(null);
  }

  default Ios getFirstIos() {
    return (Ios) getDevices().stream()
        .filter(device -> device.getDeviceProductName().contains("Ios")).findFirst().orElse(null);
  }

  default IosSimulator getFirstIosSimulator() throws IOException, DeviceNotFoundException {
    return (IosSimulator) getDevices().stream()
        .filter(device -> device.getDeviceProductName().contains("Ios Simulator")).findFirst()
        .orElse(null);
  }

  default Device getFirstAndroidDevice() throws IOException, DeviceNotFoundException {
    return getDevices().stream().filter(device -> device.getDeviceProductName().contains("Android"))
        .filter(device -> device.getSerialNumber() != null)
        .filter(device -> !device.getModelNumber().contains("SDK")).findFirst().orElse(null);
  }

  default Device getFirstAndroidEmulatorDevice() throws IOException, DeviceNotFoundException {
    return getDevices().stream().filter(device -> device.getDeviceProductName().contains("Android"))
        .filter(device -> device.getSerialNumber() == null)
        .filter(device -> device.getModelNumber().contains("SDK")).findFirst().orElse(null);
  }

  default Device getFirstIosDevice() throws IOException, DeviceNotFoundException {
    return getDevices().stream().filter(device -> device.getDeviceProductName().contains("Ios"))
        .findFirst().orElse(null);
  }

  default Device getFirstIosSimulatorDevice() throws IOException, DeviceNotFoundException {
    return getDevices().stream()
        .filter(device -> device.getDeviceProductName().contains("Ios Simulator")).findFirst()
        .orElse(null);
  }

  default List<Android> getAndroidDevices() throws IOException, DeviceNotFoundException {
    return getAndroidDevices();
  }

  default List<Android> getAndroidEmulatorDevices() throws IOException, DeviceNotFoundException {
    return getAndroidDevices().stream()
        .filter(device -> device.getDeviceProductName().contains("Android"))
        .filter(device -> device.getSerialNumber() == null)
        .filter(device -> device.getModelNumber().contains("SDK")).collect(Collectors.toList());
  }

  default List<Ios> getIosDevices() throws IOException, DeviceNotFoundException {
    return getIosDevices();
  }

  default List<IosSimulator> getIosSimulatorDevices() {
    return getIosSimulatorDevices();
  }

  // default Device searchDeviceByName(String name) throws IOException, DeviceNotFoundException {
  // return getDevices().stream().filter(device ->
  // device.getDeviceProductName().equals(name)).findFirst().orElse(null);
  // }

  List<Device> getDevices();

}
