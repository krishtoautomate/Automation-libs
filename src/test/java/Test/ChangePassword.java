package Test;


import static io.restassured.RestAssured.given;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONException;
import io.restassured.RestAssured;
import io.restassured.response.Response;


public class ChangePassword {

  public static void main(String[] args) throws IOException, URISyntaxException,
      NoSuchAlgorithmException, KeyManagementException, InterruptedException {

    Scanner s = new Scanner(new File("/Users/krish/Downloads/automationMBM.csv"));
    ArrayList<String> list = new ArrayList<String>();
    while (s.hasNext()) {
      list.add(s.next());

    }
    s.close();

    for (String bup : list) {

      String[] parts = bup.split(",");
      // String mdn = parts[0].trim();
      String user = parts[1].trim();
      String password = parts[2].trim();

      // System.out.println("user : "+ user);
      // System.out.println("password : "+ password);
      String newPassword = "Galaxy1$";


      // Specify the proxy address
      RestAssured.proxy("fastweb.int.bell.ca", 8083);
      RestAssured.proxy("fastweb.int.bell.ca", 8083, "http");

      // Specify the base URL to the RESTful web service
      // RestAssured.baseURI = Constants.BASE_URI;
      RestAssured.baseURI = "https://apigate.bell.ca/channelbellcaext";

      try {

        Thread.sleep(5000);
        // System.out.println("*************LOGIN******************");
        Response response = given().auth().basic(user, password)
            // .header("Host", Constants.API_HOST)
            .header("Connection", "keep-alive").header("Accept", "*/*")
            .header("Accept-Language", "en-ca").header("Cache-Control", "no-cache")
            .header("Accept-Encoding", "gzip, deflate, br")
            .get(RestAssured.baseURI + "/Authentication/BUP");


        String userId = response.path("userId").toString();

        Thread.sleep(5000);

        given().header("accept-language", "EN-CA").header("province", "ON").header("brand", "B")
            .header("Accept", "*/*").header("Cache-Control", "no-cache")
            .header("Host", "api.virginmobile.ca").header("Accept-Encoding", "gzip, deflate")
            .header("Connection", "keep-alive").cookies(response.cookies()).when()
            .get(RestAssured.baseURI + "/UXP.Services/eCare/CustomerProfile/CustomerAccounts/"
                + userId + "/CustomerProfile?privilegeRequired=All");



        Thread.sleep(5000);

        given().header("Accept-Language", "EN-CA").header("brand", "B")
            .header("Content-Type", "application/json")
            .body("{\r\n\"CurrentKey\" : \"" + password + "\",\r\n\"NewKey\" : \"" + newPassword
                + "\",\r\n\"Username\" : \"" + user + "\"\r\n}")
            .cookies(response.cookies()).when()
            .put(RestAssured.baseURI + "/UXP.Services/ecare/Profile/UserProfile/Me/Password");

      } catch (JSONException | InterruptedException | NullPointerException e) {
        // ignore
        System.out.println("Failed: " + user + ":" + password);
      }
    }


  }

}

