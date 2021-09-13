package com.deviceinformation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import com.deviceinformation.exception.DeviceNotFoundException;
import com.deviceinformation.model.Android;
import com.deviceinformation.model.Device;
import com.deviceinformation.model.Ios;
import com.deviceinformation.model.IosSimulator;

public interface DeviceInfo {

  default boolean anyDeviceConnected() {

    try {
      return getDevices().size() > 0;
    } catch (Exception e) {
      // ignore
    }
    return false;
  }

  default Device getFirstDevice() throws IOException, DeviceNotFoundException {
    return getDevices().stream().findFirst().orElse(null);
  }


  default Device getUdid(String udid) {
    return

    getDevices().stream().filter(device -> udid.equalsIgnoreCase(device.getUniqueDeviceID()))
        .findAny().orElse(null);
  }

  default Android getFirstAndroid() throws IOException, DeviceNotFoundException {
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

  default Ios getFirstIos() throws IOException, DeviceNotFoundException {
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

  default List<IosSimulator> getIosSimulatorDevices() throws IOException, DeviceNotFoundException {
    return getIosSimulatorDevices();
  }

  // default Device searchDeviceByName(String name) throws IOException, DeviceNotFoundException {
  // return getDevices().stream().filter(device ->
  // device.getDeviceProductName().equals(name)).findFirst().orElse(null);
  // }

  List<Device> getDevices();

}
