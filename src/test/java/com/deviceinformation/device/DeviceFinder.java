package com.deviceinformation.device;

import java.io.IOException;
import java.util.Map;
import com.deviceinformation.exception.DeviceNotFoundException;
import com.deviceinformation.model.Device;
import com.deviceinformation.model.DeviceInfoModel;

public interface DeviceFinder<S extends Device> {

  default DeviceInfoModel<S> findDevices() {
    try {
      return findDevices("");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (DeviceNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  DeviceInfoModel<S> findDevices(String localPath) throws IOException, DeviceNotFoundException;

  default Map<String, Object> readDeviceInfo() throws IOException {
    return readDeviceInfo("");
  }

  Map<String, Object> readDeviceInfo(String localPath) throws IOException;
}
