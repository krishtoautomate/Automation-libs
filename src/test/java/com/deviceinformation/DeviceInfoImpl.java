package com.deviceinformation;

import com.deviceinformation.device.DeviceFinder;
import com.deviceinformation.device.DeviceFinderFactory;
import com.deviceinformation.device.DeviceType;
import com.deviceinformation.exception.DeviceNotFoundException;
import com.deviceinformation.model.Android;
import com.deviceinformation.model.Device;
import com.deviceinformation.model.Ios;
import com.deviceinformation.model.IosSimulator;
import java.io.IOException;
import java.util.List;
import org.apache.commons.exec.OS;
import org.apache.commons.lang3.StringUtils;

public class DeviceInfoImpl implements DeviceInfo {

  private DeviceFinder<Device> deviceFinder;

  @SuppressWarnings("unchecked")
  public DeviceInfoImpl(DeviceType deviceType) throws UnsupportedOperationException {
    if (!StringUtils.isEmpty(System.getProperty("key"))) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " - "
          + System.getProperty("key") + " - will not work if you have any key value.");
    }

    if (deviceType == DeviceType.IOS && !isOperationSystemMacOs()) {
      throw new UnsupportedOperationException(
          "Operation System Is Not Valid! Ios device info only run macos operation system.");
    }

    if (deviceType == DeviceType.IOSSIMULATOR && !isOperationSystemMacOs()) {
      throw new UnsupportedOperationException(
          "Operation System Is Not Valid! Ios Simulator device info only run macos operation system.");
    }

    deviceFinder = DeviceFinderFactory.createDeviceFinder(deviceType);
  }

  private boolean isOperationSystemMacOs() {
    return OS.isFamilyMac();
  }

  @Override
  public List<Device> getDevices() {
    return deviceFinder.findDevices().getDevices();

  }

  @Override
  public List<Android> getAndroidDevices() throws IOException, DeviceNotFoundException {
    return deviceFinder.findDevices().getAndroidDevices();
  }

  @Override
  public List<Ios> getIosDevices() throws IOException, DeviceNotFoundException {
    return deviceFinder.findDevices().getIosDevices();
  }

  @Override
  public List<IosSimulator> getIosSimulatorDevices() {
    return deviceFinder.findDevices().getIosSimulatorDevices();
  }

}
