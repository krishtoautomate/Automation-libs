package other.testcases;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
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

public class BellLoginAPI extends TestBaseAPI implements ITestBase {

  String className = this.getClass().getName();

  String p_mdn = "";
  String p_userid = "";
  String p_password = "";

  @Test
  @Parameters({"p_Testdata"})
  public void Force_Upgrade_API(@Optional String p_Testdata) throws FileNotFoundException {

    test.getModel().setName("MBM");

    test.info(MarkupHelper.createLabel("MBM", ExtentColor.BLUE));

    RestAssured.proxy("fastweb.int.bell.ca", 8083);
    RestAssured.useRelaxedHTTPSValidation();

    RestAssured.baseURI = "https://apigate.bell.ca/channelbellcaext";

    LoginObjects loginObjects = new LoginObjects(log, test);

    Scanner s = new Scanner(new File("/Users/krish/Downloads/Buplist.csv"));
    ArrayList<String> list = new ArrayList<String>();
    while (s.hasNext()) {
      list.add(s.next());

    }
    s.close();

    for (String bup : list) {

      String[] parts = bup.split(",");
      p_mdn = parts[0].trim();
      p_userid = parts[1].trim();
      p_password = parts[2].trim();

      // test.getModel().setName("MBM"); // : " + p_mdn + " : {" + p_userid + ":" + p_password +
      // "}");

      Response response = loginObjects.getBupLogin(p_userid, p_password);

      log.info("\n" + response.getBody().asString());

      loginObjects.VERIFY_API_STATUS(response);
      loginObjects.VERIFY_API_CONTAINS(response, "success");

      test.pass(MarkupHelper.createCodeBlock(response.getBody().asString()));

      // break;
      sleep(5);

    }
    RestAssured.reset();

  }

}
