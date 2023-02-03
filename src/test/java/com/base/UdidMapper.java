package com.base;

import com.ReportManager.ExtentManager;
import com.aventstack.extentreports.ExtentReports;

import java.util.HashMap;
import java.util.Map;

public class UdidMapper {

  static Map<Long, String> udidMap = new HashMap<>();

  public static synchronized String getUdid() {
//    for(Long udid : udidMap.keySet()){
//      if(udid == Thread.currentThread().getId()) {
//        return udidMap.get(udid);
//      }
//    }
    return udidMap.get(Long.valueOf(Thread.currentThread().getId()));
  }

  public static synchronized String setUdid(String udid) {
    udidMap.put(Thread.currentThread().getId(), udid);
    return udid;
  }


}
