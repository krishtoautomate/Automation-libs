package com.Deviceinformation.device;

import com.Deviceinformation.exception.DeviceNotFoundException;
import com.Deviceinformation.model.Android;
import com.Deviceinformation.model.Device;
import com.Deviceinformation.model.DeviceInfoModel;
import com.Deviceinformation.model.Ios;
import com.Deviceinformation.model.IosSimulator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllDeviceFinder implements DeviceFinder<Device> {

  private DeviceType deviceType;

  public AllDeviceFinder(DeviceType deviceType) {
    this.deviceType = deviceType;
  }

  public AllDeviceFinder() {
  }

  @Override
  public DeviceInfoModel<Device> findDevices(String localPath) throws IOException {
    DeviceInfoModel<Device> deviceDeviceInfoModel = new DeviceInfoModel<>();
    List<Device> devices = new ArrayList<>();

    List<Android> deviceAndroid = new AndroidDeviceFinder().findDevices(localPath).getDevices();
    List<Ios> deviceIos = new IosDeviceFinder().findDevices(localPath).getDevices();
    List<IosSimulator> deviceIosSimulator = null;

    if (deviceType == DeviceType.ALLANDIOSSIMULATOR) {
      deviceIosSimulator = new IosSimulatorDeviceFinder().findDevices(localPath).getDevices();
    }

    if (deviceAndroid != null) {
      devices.addAll(deviceAndroid);
    }
    if (deviceIos != null) {
      devices.addAll(deviceIos);
    }
    if (deviceIosSimulator != null) {
      devices.addAll(deviceIosSimulator);
    }
    if (deviceAndroid == null && deviceIos == null && deviceType == null
        || deviceAndroid == null && deviceIos == null && deviceIosSimulator.size() == 0) {
      try {
        throw new DeviceNotFoundException("No device is plugged in !!!");
      } catch (DeviceNotFoundException e) {
        // System.err.println(e.toString());
      }
    }
    deviceDeviceInfoModel.setDevices(devices);
    return deviceDeviceInfoModel;
  }

  @Override
  public Map<String, Object> readDeviceInfo(String localPath) {
    return null;
  }
}
