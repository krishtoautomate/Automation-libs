package com.base;

import com.Utilities.Constants;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.log4j.Logger;

import java.io.File;

public class AppiumService {

    private static Logger log = Logger.getLogger(AppiumService.class.getName());

    public synchronized AppiumDriverLocalService AppiumServer() {

        AppiumDriverLocalService server = AppiumDriverLocalService.buildService(
                new AppiumServiceBuilder().usingDriverExecutable(new File(Constants.NODE_PATH))
                        .withAppiumJS(new File(Constants.APPIUM_PATH)).usingAnyFreePort()
                        .withIPAddress(Constants.APPIUM_IP_ADDRESS)
                        .withArgument(() -> "--base-path", "/wd/hub"));

        server.clearOutPutStreams();

        try {
            if (server.isRunning()) {
                server.stop();
            }

        } catch (Exception e) {
            log.error("failed to start appium server : " + e.getLocalizedMessage());
        }

        log.info("session : " + server.getUrl());

        return server;

    }

}
