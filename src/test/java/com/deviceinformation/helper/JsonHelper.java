package com.deviceinformation.helper;

import java.util.Map;
import com.deviceinformation.model.Device;
import com.deviceinformation.model.DeviceInfoModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonHelper {

  private JsonHelper() {

  }

  public static <S extends Device> DeviceInfoModel<S> convertJsonToDeviceInfo(
      Map<String, Object> deviceList, TypeToken<?> typeToken) {
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(String.valueOf(gson.toJson(deviceList)), typeToken.getType());
  }


}
