package com.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.Reporter;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class AppiumManager {

	private static Logger log = Logger.getLogger(Class.class.getName());

	protected AppiumDriverLocalService server;

	public synchronized AppiumDriverLocalService AppiumService() {

		AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();

		ITestResult iTestResult = Reporter.getCurrentTestResult();
		Map<String, String> testParams = iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

		Boolean isAppiumLogsON = false;

		// APPIUM-PORT
		String p_port = testParams.get("PORT");
		int port = 0;

		try {
			port = Integer.parseInt(p_port);
			String appiumLog = testParams.get("APPIUM_LOG");
			if (appiumLog != null)
				isAppiumLogsON = appiumLog.contains("true");
		} catch (NumberFormatException e) {
			// ignore
		}

		serviceBuilder.withIPAddress(Constants.APPIUM_IP_ADDRESS);

		if (port != 0) {
			serviceBuilder.usingAnyFreePort();
		} else {
			serviceBuilder.usingPort(port);
		}

		// APPIUM_LOG
		if (isAppiumLogsON)
			serviceBuilder.withLogFile(new File(System.getProperty("user.dir") + "appium.log"));

		serviceBuilder.usingDriverExecutable(new File(Constants.NODE_PATH));
		serviceBuilder.withAppiumJS(new File(Constants.APPIUM_PATH));

		// important for ios
		HashMap<String, String> environment = new HashMap<String, String>();
		environment.put("PATH", "/usr/local/bin:" + System.getenv("PATH"));
		serviceBuilder.withEnvironment(environment);
		server = AppiumDriverLocalService.buildService(serviceBuilder);

		// APPIUM_LOG
		if (!isAppiumLogsON)
			server.clearOutPutStreams();

//		log.info("Appium server started!");
		return server;
	}

	public static void main(String[] args) {

		AppiumManager appiumManager = new AppiumManager();

		int port = 8302;
		if (appiumManager.isPortBusy(port)) {
			appiumManager.killPort(port);
		}

	}

//	public boolean isPortBusy(int port) {
//		boolean isPortBusy = !isTcpPortAvailable(port);
//		log.info(port + " - isPortBusy : " + isPortBusy);
//		return isPortBusy;
//	}

	public boolean isRemotePortInUse(String hostName, int portNumber) {
		try {
			// Socket try to open a REMOTE port
			new Socket(hostName, portNumber).close();
			return true;
		} catch (Exception e) {
			// remote port is closed, nothing is running on
			return false;
		}
	}

	public boolean isTcpPortAvailable(int port) {
		try (ServerSocket serverSocket = new ServerSocket()) {
			serverSocket.setReuseAddress(false);
			serverSocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port), 1);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean isPortBusy(int port) {
		String s = null;
		String pid = null;
		boolean isBusy = false;
		try {
			Process p = Runtime.getRuntime().exec("lsof -t -i:" + port);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while ((s = stdInput.readLine()) != null) {
				pid = s.trim();
			}
			// log.info("PID : " + pid);
			if (pid != null)
				isBusy = true;
		} catch (IOException e) {
			isBusy = false;
		}
		log.info(port + " - isPortBusy : " + isBusy);
		return isBusy;
	}

	public void killPort(int port) {
		List<String> list = new ArrayList<String>();
		list.add("lsof -ti:" + port + " | xargs kill");
		list.add("lsof -t -i:" + port);
		for (String each : list) {
			String s = null;
			String _pid = null;
			try {
				Process p = Runtime.getRuntime().exec(each);// lsof -ti:8302 | xargs kill

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while ((s = stdInput.readLine()) != null) {
					_pid = s.trim();
				}
				// log.info("PID : " + _pid);
				Runtime.getRuntime().exec("kill -9 " + _pid);
				Runtime.getRuntime().exec("fuser -k " + _pid + "/tcp");

				log.info(port + " - port kill success");
			} catch (IOException e) {
				log.error("failed to kill Port");
			}
		}
	}
}
