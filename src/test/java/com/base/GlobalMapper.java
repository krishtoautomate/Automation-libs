package com.base;

import java.util.HashMap;
import java.util.Map;

public class GlobalMapper {

    static Map<Long, String> udidMap = new HashMap<>();
    static Map<Long, String> testMap = new HashMap<>();

    public static synchronized String getUdid() {
        return udidMap.get(Long.valueOf(Thread.currentThread().getId()));
    }

    public static synchronized String setUdid(String udid) {
        udidMap.put(Thread.currentThread().getId(), udid);
        return udid;
    }

    public static synchronized String getTestName() {
        return testMap.get(Long.valueOf(Thread.currentThread().getId()));
    }

    public static synchronized String setTestName(String testName) {
        testMap.put(Thread.currentThread().getId(), testName);
        return testName;
    }

//    "automation:testName": "testName"
}
