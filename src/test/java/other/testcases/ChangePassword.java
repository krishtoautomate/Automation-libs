package other.testcases;


import static io.restassured.RestAssured.given;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import io.restassured.RestAssured;
import io.restassured.response.Response;


public class ChangePassword {

  public static void main(String[] args) throws IOException, URISyntaxException,
      NoSuchAlgorithmException, KeyManagementException, InterruptedException {

    Scanner s = new Scanner(new File("/Users/krish/Downloads/MLM_Auto_March.csv"));
    ArrayList<String> list = new ArrayList<String>();
    while (s.hasNext()) {
      list.add(s.next());

    }
    s.close();

    for (String bup : list) {

      String[] parts = bup.split(",");
      // String mdn = parts[0].trim();
      String user = parts[0].trim();
      String password = "Fibe1234$";// "Lucky1234$";// parts[1].trim();

      System.out.println("user : " + user);
      System.out.println("password : " + password);
      // Specify the proxy address
      RestAssured.useRelaxedHTTPSValidation();
      // RestAssured.proxy("fastweb.int.bell.ca", 8083);
      // RestAssured.proxy("fastweb.int.bell.ca", 8083, "http");

      // Specify the base URL to the RESTful web service
      // RestAssured.baseURI = Constants.BASE_URI;
      RestAssured.baseURI = "https://api.luckymobile.ca/channelluckyext";
      // "https://api.virginplus.ca/channelvirginext";
      // "https://apigate.bell.ca/channelbellcaext";

      String Brand = "L";// "B" "V";
      String applicationid = "MLM_IOS";// MVM_IOS

      try {

        Thread.sleep(5000);
        Response response = given().relaxedHTTPSValidation().auth().basic(user, password)
            .header("accept-language", "en-ca").header("brand", Brand)
            .header("applicationid", applicationid)
            .get(RestAssured.baseURI + "/Authentication/BUP");

        System.out.println(response.getBody().path("status").toString()); // .asPrettyString().);

        System.out.println("----------");

        // String userId = response.path("userId").toString();
        // Thread.sleep(5000);
        //
        // given().header("accept-language", "EN-CA").header("province", "ON").header("brand",
        // Brand)
        // .header("Accept", "*/*").header("Cache-Control", "no-cache")
        // .header("Host", "api.virginmobile.ca").header("Accept-Encoding", "gzip, deflate")
        // .header("Connection", "keep-alive").cookies(response.cookies()).when()
        // .get(RestAssured.baseURI + "/UXP.Services/eCare/CustomerProfile/CustomerAccounts/"
        // + userId + "/CustomerProfile?privilegeRequired=All");



        Thread.sleep(2000);

        // String newPassword = "Fibe1234$";
        // given().header("Accept-Language", "EN-CA").header("brand", Brand)
        // .header("Content-Type", "application/json").header("applicationid", applicationid)
        // .body("{\r\n'CurrentKey' : '" + password + "',\r\n'NewKey' : '" + newPassword
        // + "',\r\n'Username' : '" + user + "'\r\n}")
        // .cookies(response.cookies()).when()
        // .put(RestAssured.baseURI + "/UXP.Services/ecare/Profile/UserProfile/Me/Password");


      } catch (Exception e) {
        // ignore
        System.out.println("Failed: " + user + ":" + password);

        System.out.println("----------");
      }
    }


  }

}

