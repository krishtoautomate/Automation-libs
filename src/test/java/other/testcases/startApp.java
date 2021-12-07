package other.testcases;

import com.Utilities.Constants;
import com.Utilities.ITestBase;

public class startApp implements ITestBase {

  public static void main(String[] args) {
    // TODO Auto-generated method stub

    startApp startApp = new startApp();

    startApp.run();

  }

  public void run() {


    runCommandThruProcess(Constants.ADB
        + " shell am start -a android.intent.action.SENDTO -d sms:+1- --es sms_body \"\" --ez exit_on_sent true");

  }

}
