package com.base;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.Utilities.Constants;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class AppiumService {

  private static final Logger log = LoggerFactory.getLogger(AppiumService.class);

  public synchronized AppiumDriverLocalService AppiumServer() {

    AppiumDriverLocalService server = AppiumDriverLocalService.buildService(
        new AppiumServiceBuilder().usingDriverExecutable(new File(Constants.NODE_PATH))
            .withAppiumJS(new File(Constants.APPIUM_PATH)).usingAnyFreePort()
            .withIPAddress(Constants.APPIUM_IP_ADDRESS)
            .withArgument(() -> "--base-path", "/wd/hub"));

    server.clearOutPutStreams();

    try {
      if (server.isRunning())
        server.stop();

      server.start();

    } catch (Exception e) {
      log.error("failed to start appium server : " + e.getLocalizedMessage());
    }

    log.info("session : " + server.getUrl());
    log.info("session isRunning : " + server.isRunning());

    return server;

  }

  public static void main(String[] args) {

    AppiumService appiumService = new AppiumService();



    appiumService.AppiumServer();


  }

}
