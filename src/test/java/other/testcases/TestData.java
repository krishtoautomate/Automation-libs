package other.testcases;

import com.Utilities.ITestBase;
import com.base.TestBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestData extends TestBase implements ITestBase {

  @Test
  @Parameters({"udid"})
  public void Test(@Optional String udid) {

  }

}
