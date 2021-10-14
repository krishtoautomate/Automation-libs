package com.deviceinformation.exception;

@SuppressWarnings("serial")
public class DeviceNotFoundException extends Exception {

  public DeviceNotFoundException() {}

  public DeviceNotFoundException(String message) {
    super(message);
  }

}
