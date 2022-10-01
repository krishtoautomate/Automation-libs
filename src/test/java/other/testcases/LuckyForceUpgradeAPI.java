package other.testcases;

import com.Utilities.Constants;
import com.Utilities.ITestBase;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.base.TestBaseAPI;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import other.pages.LoginObjects;

public class LuckyForceUpgradeAPI extends TestBaseAPI implements ITestBase {

  String className = this.getClass().getName();

  String p_userid = "";
  String p_password = "";

  @Test
  @Parameters({"p_Testdata"})
  public void Force_Upgrade_API(@Optional String p_Testdata) {

    test.getModel().setName("Lucky Mobile Force-upgrade API");

    test.info(MarkupHelper.createLabel("MLM", ExtentColor.GREEN));

    RestAssured.proxy("fastweb.int.bell.ca", 8083);
    RestAssured.useRelaxedHTTPSValidation();

    RestAssured.baseURI = Constants.BASE_URI;

    LoginObjects loginObjects = new LoginObjects( test);

    Response andriod_response = loginObjects.getForceUpgradeAPI("L", "android");

    Response ios_response = loginObjects.getForceUpgradeAPI("L", "ios");

    test.pass(MarkupHelper
        .createCodeBlocks(new String[]{"<>ANDROID<>\n" + andriod_response.getBody().prettyPrint(),
            "<>iOS<>\n" + ios_response.getBody()
                .prettyPrint()}));// (ios_response.getBody().prettyPrint()));

    RestAssured.reset();

  }

}
