package com.selenium.grid.matcher;

import org.openqa.grid.internal.utils.DefaultCapabilityMatcher;

import java.util.Map;

public class CapabilityMatcher extends DefaultCapabilityMatcher {
    @Override
    public boolean matches(Map<String, Object> nodeCapability, Map<String, Object> requestedCapability) {

        // Check if the node has the appium udid property
        if (nodeCapability.containsKey("appium:udid")) {
            // Check if the requested capability has the same appium version
            if (requestedCapability.containsKey("appium:udid") &&
                    requestedCapability.get("appium:udid").equals(nodeCapability.get("appium:udid"))) {
                return true;
            }
        }

        if (nodeCapability.containsKey("udid")) {
            // Check if the requested capability has the same appium version
            if (requestedCapability.containsKey("appium:udid") &&
                    requestedCapability.get("appium:udid").equals(nodeCapability.get("udid"))) {
                return true;
            }
        }

        if (nodeCapability.containsKey("udid")) {
            // Check if the requested capability has the same appium version
            if (requestedCapability.containsKey("udid") &&
                    requestedCapability.get("udid").equals(nodeCapability.get("udid"))) {
                return true;
            }
        }

        // Check if the node has the appium version property
        if (nodeCapability.containsKey("appium:platformVersion")) {
            // Check if the requested capability has the same appium version
            if (requestedCapability.containsKey("appium:platformVersion") &&
                    requestedCapability.get("appium:platformVersion").equals(nodeCapability.get("appium:platformVersion"))) {
                return true;
            }
        }

        if (nodeCapability.containsKey("platformVersion")) {
            // Check if the requested capability has the same appium version
            if (requestedCapability.containsKey("appium:platformVersion") &&
                    requestedCapability.get("appium:platformVersion").equals(nodeCapability.get("platformVersion"))) {
                return true;
            }
        }


        // Check if the node has the platform name property
        if (nodeCapability.containsKey("platformName")) {
            // Check if the requested capability has the same platform name
            if (requestedCapability.containsKey("platformName") &&
                    requestedCapability.get("platformName").equals(nodeCapability.get("platformName"))) {
                return true;
            }
        }
        // Check if the node has the device name property
        if (nodeCapability.containsKey("deviceName")) {
            // Check if the requested capability has the same device name
            if (requestedCapability.containsKey("deviceName") &&
                    requestedCapability.get("deviceName").equals(nodeCapability.get("deviceName"))) {
                return true;
            }
        }
        // If no properties match, call the super.matches() method to continue matching
        return super.matches(nodeCapability, requestedCapability);
    }
}

