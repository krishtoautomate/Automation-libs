package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class webdriverAvailable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String installedApps = runCommandThruProcess("/usr/local/bin/ideviceinstaller -u 00008030-001A550A2643802E -l");
		
		System.out.println(installedApps);
		
		Boolean deviceBusy = false;
		//check available
		if(installedApps.contains("com.facebook.WebDriverAgentRunner.xctrunner")) {
			deviceBusy = true;
		}
		
		if(!deviceBusy)
			runCommandThruProcess("/usr/local/bin/ideviceinstaller -u 00008030-001A550A2643802E -U com.facebook.WebDriverAgentRunner.xctrunner");
		
		
			

	}
	
	private static String runCommandThruProcess(String command) {
	    BufferedReader br;
	    String allLine = "";
		try {
			br = getBufferedReader(command);
		
	     String line;
	     
	     while ((line = br.readLine()) != null) {
	         allLine = allLine + "" + line + "\n";
	     }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return allLine;
	 }
	
	 private static BufferedReader getBufferedReader(String command) throws IOException {
	     
	     final Process process =  execForProcessToExecute(command);
	     
	     InputStream is = process.getInputStream();
	     InputStreamReader isr = new InputStreamReader(is);
	     return new BufferedReader(isr);
	 }
	
	 private static Process execForProcessToExecute(String cmd) throws IOException {
		 
		 Process process = Runtime.getRuntime()
	    	      .exec(cmd);
	     return process;
	 }

}
