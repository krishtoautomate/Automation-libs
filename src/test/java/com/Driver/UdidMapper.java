package com.Driver;

import com.ReportManager.ExtentManager;
import com.aventstack.extentreports.ExtentReports;

import java.util.HashMap;
import java.util.Map;

public class UdidMapper {

  static Map<Integer, String> udidMap = new HashMap<>();

  public static synchronized String getUdid() {
    for(Integer udid : udidMap.keySet()){
      if(udid == Thread.currentThread().getId()) {
        return udidMap.get(udid);
      }
    }
    return udidMap.get(Thread.currentThread().getId());
  }

  public static synchronized String setUdid(String udid) {
    udidMap.put((int) Thread.currentThread().getId(), udid);
    return udid;
  }


}
