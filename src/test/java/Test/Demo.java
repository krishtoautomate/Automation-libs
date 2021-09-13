package Test;

import java.io.IOException;
import java.util.List;
import com.deviceinformation.DeviceInfo;
import com.deviceinformation.DeviceInfoImpl;
import com.deviceinformation.device.DeviceType;
import com.deviceinformation.exception.DeviceNotFoundException;
import com.deviceinformation.model.Device;

public class Demo {

  public static void main(String[] args) throws IOException, DeviceNotFoundException {
    // TODO Auto-generated method stub

    DeviceInfo deviceInfo = new DeviceInfoImpl(DeviceType.ANDROID);
    if (deviceInfo.anyDeviceConnected()) {


      List<Device> deviceList = deviceInfo.getDevices();

      for (Device device : deviceList) {

        System.out.println("Device id - " + device.getUniqueDeviceID());

      }

      // deviceInfo.getFirstDevice();

      // System.out.println("Device Name - " + device.getDeviceProductName());
      // System.out.println("Device id - " + device.getUniqueDeviceID());
      // System.out.println("Model no - " + device.getModelNumber());
      // System.out.println("build no - " + device.getBuildVersion());
      // System.out.println("serial no - " + device.getSerialNumber());
      // System.out.println("product version - " + device.getProductVersion());
      //
      // System.out.println(device.getDeviceName());
      //
      // System.out
      // .println(deviceInfo.getDevices().get(0).getUniqueDeviceID().contains("15141JEC201224"));


    }

  }

}
