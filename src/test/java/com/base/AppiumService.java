package com.base;

import com.Listeners.TestListenerRT;
import com.ReportManager.LoggerManager;
import com.Utilities.Constants;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import java.io.File;
import org.apache.log4j.Logger;

public class AppiumService {

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
      Log.error("failed to start appium server : " + e.getLocalizedMessage());
    }

    Log.info("session : " + server.getUrl());

    return server;

  }

}
