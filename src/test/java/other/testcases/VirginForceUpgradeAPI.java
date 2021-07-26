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

public class VirginForceUpgradeAPI extends TestBaseAPI implements ITestBase {

  String className = this.getClass().getName();

  String p_userid = "";
  String p_password = "";

  @Test
  @Parameters({"p_Testdata"})
  public void Virgin_ForceUpgrade_API(@Optional String p_Testdata) {

    test.getModel().setName("Virgin Force-upgrade API");

    test.info(MarkupHelper.createLabel("MVM", ExtentColor.RED));

    RestAssured.proxy("fastweb.int.bell.ca", 8083);
    RestAssured.useRelaxedHTTPSValidation();

    RestAssured.baseURI = "https://api.virginmobile.ca/channelvirginext";

    LoginObjects loginObjects = new LoginObjects(log, test);

    Response andriod_response = loginObjects.getForceUpgradeAPI("V", "android");

    Response ios_response = loginObjects.getForceUpgradeAPI("V", "ios");

    test.pass(MarkupHelper
        .createCodeBlocks(new String[] {"<>ANDROID<>\n" + andriod_response.getBody().prettyPrint(),
            "<>iOS<>\n" + ios_response.getBody().prettyPrint()}));// (ios_response.getBody().prettyPrint()));

    RestAssured.reset();

  }

}
