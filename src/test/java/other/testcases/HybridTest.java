package other.testcases;

import com.Utilities.ITestBase;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseHybrid;
import com.base.TestBaseWeb;
import org.testng.annotations.Test;

public class HybridTest extends TestBaseHybrid implements ITestBase {

    @Test
    public void Hybrid_Test(){
        webDriver.get("http://bqatautomation:4444/grid/console");

        sleep(10);

        String errorXML = webDriver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));

        errorXML = driver.getPageSource();
        test.info(MarkupHelper.createCodeBlock(errorXML));

//        utils.logmessage(Status.PASS, "Web page launched  : "+driver.getCurrentUrl());
//        utils.getPageSource();
    }
}
