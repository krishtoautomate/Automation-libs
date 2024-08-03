package other.pages;

import com.Utilities.BaseObjs;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;

public class LoginObjects extends BaseObjs<LoginObjects> {

    public LoginObjects(ExtentTest test) {
        super(test);
        // TODO Auto-generated constructor stub
    }

    public Response getForceUpgradeAPI(String Brand, String platForm) {

        RestAssured.basePath = "/UXP.Services/Tools/Utilities/forceupgrade?platform=" + platForm;

        String[][] data = {{"<b>Base url : </b>", RestAssured.baseURI},
                {"<b>Request url : </b>", RestAssured.basePath}, {"<b>Brand : </b>", Brand}};

        test.info(MarkupHelper.createTable(data));

        Response response = null;
        try {
            response = given().with().header("accept-language", "en-ca").header("brand", Brand).and()
                    .get(RestAssured.baseURI + RestAssured.basePath);
        } catch (Exception e) {
            String errorMessage = "API call failed : " + e.getLocalizedMessage();
            log.error(errorMessage);
            test.log(Status.FAIL, errorMessage);
            Assert.fail(errorMessage);
        }

        return response;
    }

    public Response getBupLogin(String userName, String password) {

        RestAssured.basePath = "/Authentication/BUP";

        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(userName);
        authScheme.setPassword(password);
        // RestAssured.authentication = authScheme;

        // String[][] data = {{"<b>Request : </b>", "BUP Login"}, {"<b>User : </b>", userName},
        // {"<b>Password : </b>", password}, {"<b>Request url : </b>", RestAssured.basePath},
        // {"<b>Request TYPE : </b>", "<i>GET</i>"}};
        //
        // test.info(MarkupHelper.createTable(data));

        Response response = null;
        try {
            response = given().when().auth().basic(userName, password).with()
                    .header("basic", RestAssured.authentication).header("accept-language", "en-ca").and()
                    .get(RestAssured.baseURI + RestAssured.basePath);
        } catch (Exception e) {
            String errorMessage = "API call failed : " + e.getLocalizedMessage();
            log.error(errorMessage);
            test.log(Status.FAIL, errorMessage);
            Assert.fail(errorMessage);
        }

        return response;
    }

    public Response getNSILogin(String subId) {

        String basePath = "/Authentication/JWT";

        String[][] data = {{"<b>Request : </b>", "NSI Login"}, {"<b>subId : </b>", subId},
                {"<b>Request url : </b>", basePath}, {"<b>Request TYPE : </b>", "<i>POST</i>"}};

        test.info(MarkupHelper.createTable(data));

        Response response = null;

        try {
            response = given().header("Content-Type", "application/json").header("Accept", "*/*")
                    // .header("Cache-Control", "no-cache").header("Host", "api.ca")
                    .header("Accept-Encoding", "gzip, deflate").header("Connection", "keep-alive")
                    .header("cache-control", "no-cache").body("{\"SubId2\":" + subId + ",\"Brand\":\"L\"}")
                    .when().post(basePath);
        } catch (Exception e) {
            test.log(Status.FAIL, "API call failed : " + e.getLocalizedMessage());
            Assert.fail("API call failed : " + e.getLocalizedMessage());
        }

        return response;
    }

}
