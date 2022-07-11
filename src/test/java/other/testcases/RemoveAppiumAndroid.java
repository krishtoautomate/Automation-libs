package other.testcases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RemoveAppiumAndroid {

  public static void main(String[] args) {
    // TODO Auto-generated method stub

    String udid = "R58N70GGVZH";

    runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb -s " + udid
        + " uninstall io.appium.uiautomator2.server");
    runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb -s " + udid
        + " uninstall io.appium.uiautomator2.server.test");
    runCommandThruProcess("/usr/local/share/android-sdk/platform-tools/adb -s " + udid
        + " uninstall io.appium.settings");

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
      // ignore
    }
    return allLine;
  }

  private static BufferedReader getBufferedReader(String command) throws IOException {

    final Process process = execForProcessToExecute(command);

    InputStream is = process.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);
    return new BufferedReader(isr);
  }

  private static Process execForProcessToExecute(String cmd) throws IOException {

    Process process = Runtime.getRuntime().exec(cmd);
    return process;
  }

}
