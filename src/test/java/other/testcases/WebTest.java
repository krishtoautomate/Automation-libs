package other.testcases;

import com.Utilities.ITestBase;
import com.base.TestBaseWeb;
import org.testng.annotations.Test;

public class WebTest extends TestBaseWeb implements ITestBase {

    @Test
    public void Web_Test(){
        driver.get("http://172.21.34.239:4444/grid/console");

        System.out.println("session info : "+ driver.getPageSource());
        sleep(20);
    }
}
