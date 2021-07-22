package other.testcases;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.Utilities.ITestBase;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseAPI;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import other.pages.LoginObjects;

public class BellForceUpgradeAPI extends TestBaseAPI implements ITestBase {

  String className = this.getClass().getName();

  String p_userid = "";
  String p_password = "";

  @Test
  @Parameters({"p_Testdata"})
  public void Force_Upgrade_API(@Optional String p_Testdata) {

    test.getModel().setName("Bell Force-upgrade API");

    test.info(MarkupHelper.createLabel("MBM", ExtentColor.BLUE));

    RestAssured.proxy("fastweb.int.bell.ca", 8083);
    RestAssured.useRelaxedHTTPSValidation();

    RestAssured.baseURI = "https://apigate.bell.ca/channelbellcaext";

    LoginObjects loginObjects = new LoginObjects(log, test);

    Response andriod_response = loginObjects.getForceUpgradeAPI("B", "android");

    Response ios_response = loginObjects.getForceUpgradeAPI("B", "ios");

    test.pass(MarkupHelper
        .createCodeBlocks(new String[] {"<>ANDROID<>\n" + andriod_response.getBody().prettyPrint(),
            "<>iOS<>\n" + ios_response.getBody().prettyPrint()}));// (ios_response.getBody().prettyPrint()));

    RestAssured.reset();

  }

}
