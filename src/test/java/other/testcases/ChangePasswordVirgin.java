package other.testcases;


import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import static io.restassured.RestAssured.given;


public class ChangePasswordVirgin {

    public static void main(String[] args) throws IOException, URISyntaxException,
            NoSuchAlgorithmException, KeyManagementException, InterruptedException {

        Scanner s = new Scanner(new File("/Users/home/Downloads/VirginPasswords.csv"));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNext()) {
            list.add(s.next());

        }
        s.close();

        for (String bup : list) {

//            String[] parts = bup.split(",");
            // String mdn = parts[0].trim();
            String user = bup.trim();// parts[0].trim();
            String password = "Bqat123456";//"Fibe1234$";// "Lucky1234$";// parts[1].trim();
            String newPassword = "Leo1234$";//"Ssqa1234$";//"Autobude1234$";

            System.out.println("user : " + user);
            System.out.println("password : " + password);
            // Specify the proxy address
            RestAssured.useRelaxedHTTPSValidation();

//          RestAssured.proxy("fastweb.int.bell.ca", 8083, "https");
//          RestAssured.proxy("fastweb.int.bell.ca", 8083, "http");

            // Specify the base URL to the RESTful web service
            // RestAssured.baseURI = Constants.BASE_URI;
            RestAssured.baseURI =
//           "https://apigate.bell.ca/channelbellcaext";
             "https://api.virginplus.ca/channelvirginext";
            // "https://apigate.bell.ca/channelbellcaext";

            String Brand = "V";
            String applicationId = "MVM_IOS";

            try {

                Thread.sleep(5000);
                Response response = given().relaxedHTTPSValidation().auth().basic(user, password)
                        .header("accept-language", "en-ca")
                        .header("brand", Brand)
                        .header("applicationid", applicationId)
                        .get(RestAssured.baseURI + "/Authentication/BUP");

                System.out.println("status : " + response.getBody().path("status").toString()); // .asPrettyString().);

                System.out.println("----------");


                Thread.sleep(2000);

                given().header("Accept-Language", "EN-CA")
                        .header("brand", Brand)
                        .header("Content-Type", "application/json")
                        .header("applicationid", applicationId)
                        .body("{\r\n'CurrentKey' : '" + password + "',\r\n'NewKey' : '" + newPassword
                                + "',\r\n'Username' : '" + user + "'\r\n}")
                        .cookies(response.cookies()).when()
                        .put(RestAssured.baseURI + "/UXP.Services/ecare/Profile/UserProfile/Me/Password");

            } catch (Exception e) {
                // ignore
                System.out.println("Failed: " + user + ":" + password);

                System.out.println("----------"+e.getMessage());
            }
        }


    }

}

