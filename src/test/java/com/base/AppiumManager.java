package com.base;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.apache.log4j.Logger;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class AppiumManager{
	
	private static Logger log = Logger.getLogger(Class.class.getName());
	
	protected AppiumDriverLocalService server;

	public synchronized AppiumDriverLocalService AppiumService() {
		
		AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
		
		serviceBuilder.withIPAddress(Constants.APPIUM_IP_ADDRESS);
		serviceBuilder.usingAnyFreePort();
	//	serviceBuilder.withLogFile(new File(System.getProperty("user.dir") +"appium.log"));
		serviceBuilder.usingDriverExecutable(new File(Constants.NODE_PATH));
		serviceBuilder.withAppiumJS(new File(Constants.APPIUM_PATH));
		   
		//important for ios
		HashMap<String, String> environment = new HashMap<String, String>();
		environment.put("PATH", "/usr/local/bin:" + System.getenv("PATH"));
		serviceBuilder.withEnvironment(environment);
		server = AppiumDriverLocalService.buildService(serviceBuilder);
		server.clearOutPutStreams();
		
		return server;
	}
	
	public static void main(String[] args) {
		
		AppiumManager appiumManager = new AppiumManager();
		
		appiumManager.killPort(8302);
		
	}
	
	public boolean isPortBusy(int port) {
		
		boolean isPortBusy = true;

		isPortBusy = (this.isRemotePortInUse("127.0.0.1", port) == false || this.isTcpPortAvailable(port) == true)?false:true;

		return isPortBusy;
	}

	public boolean isRemotePortInUse(String hostName, int portNumber) {
	    try {
	        // Socket try to open a REMOTE port
	        new Socket(hostName, portNumber).close();
	        return true;
	    } catch(Exception e) {
	        // remote port is closed, nothing is running on
	        return false;
	    }
	}
	
	public boolean isTcpPortAvailable(int port) {
	    try (ServerSocket serverSocket = new ServerSocket()) { 
	        serverSocket.setReuseAddress(false);
	        serverSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), port), 1);
	        return true;
	    } catch (Exception ex) {
	        return false;
	    }
	} 
	
	public void killPort(int port) {
		String s = null;
    	String _pid = null;
    	try {
			Process p = Runtime.getRuntime().exec("lsof -t -i:"+port); 
			
			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(p.getInputStream()));
			
			 while ((s = stdInput.readLine()) != null ) {
            	_pid = s.trim();
			 }

			Process p1 = Runtime.getRuntime().exec("kill -9 "+_pid); 
			stdInput = new BufferedReader(new 
			        InputStreamReader(p1.getInputStream()));
			
			log.info("port kill success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("failed to kill Port");
		} 
	}
}
