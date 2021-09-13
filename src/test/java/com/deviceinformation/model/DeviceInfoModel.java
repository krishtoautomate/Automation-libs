package com.deviceinformation.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceInfoModel<T extends Device> {

  @SerializedName(value = "device", alternate = {"ios", "iosSimulator", "android"})
  @Expose
  private List<T> devices;

  public List<T> getDevices() {
    return devices;
  }

  @SuppressWarnings("unchecked")
  public List<Android> getAndroidDevices() {
    return (List<Android>) devices;
  }

  @SuppressWarnings("unchecked")
  public List<Ios> getIosDevices() {
    return (List<Ios>) devices;
  }

  @SuppressWarnings("unchecked")
  public List<IosSimulator> getIosSimulatorDevices() {
    return (List<IosSimulator>) devices;
  }

  public void setDevices(List<T> devices) {
    this.devices = devices;
  }
}
