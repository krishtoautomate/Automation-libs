package other.testcases;

import com.Utilities.ITestBase;
import com.opencsv.CSVReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;


public class DataproviderPasswordReset implements ITestBase {

    @Test(dataProvider = "DeepLinksDataProvider", groups = {"DeepLinks"})
    public void Password_change(Map<String, String> data) {

        sleep(5);

        String brand = "Bell";//data.get("﻿BRAND");
        String bup = data.get("\uFEFFBUP").trim();
//        System.out.println(brand);
//        System.out.println(bup);

        String password = "June1234$";//"Leo1234$";
        String newPassword = "June1234$";

        // Specify the proxy address
        RestAssured.useRelaxedHTTPSValidation();
//       RestAssured.proxy("fastweb.int.bell.ca", 8083);
//       RestAssured.proxy("fastweb.int.bell.ca", 8083, "http");

        // Specify the base URL to the RESTful web service
        RestAssured.baseURI = "https://apigate.bell.ca/channelbellcaext";
        String Brand = "B";// "B" "V";
        String applicationid = "MBM_IOS";// MVM_IOS

        if (brand.equalsIgnoreCase("Bell")) {
            //Bell
            Brand = "B";
            applicationid = "MBM_IOS";
            RestAssured.baseURI = "https://apigate.bell.ca/channelbellcaext";
        } else {
            //Virgin
            Brand = "V";
            applicationid = "MVM_IOS";
            RestAssured.baseURI = "https://api.virginplus.ca/channelvirginext";
        }

        try {

            Response response = given().relaxedHTTPSValidation().auth().basic(bup, password)
                    .header("accept-language", "en-ca")
                    .header("brand", Brand)
                    .header("applicationid", applicationid)
                    .get(RestAssured.baseURI + "/Authentication/BUP");

//                System.out.println("status : " + response.getBody().path("status").toString()); // .asPrettyString().);

            if (response.getBody().path("status").toString().equalsIgnoreCase("fail")) {
                System.out.println(bup);
                return;
//                Assert.fail();
            }
//            else {
//                System.out.println(response.getBody().asString());
//            }


            sleep(5);

            Response response1 = given().header("Accept-Language", "EN-CA")
                    .header("brand", Brand)
                    .header("Content-Type", "application/json")
                    .header("applicationid", applicationid)
                    .body("{\r\n'CurrentKey' : '" + password + "',\r\n'NewKey' : '" + newPassword
                            + "',\r\n'Username' : '" + bup + "'\r\n}")
                    .cookies(response.cookies()).when()
                    .put(RestAssured.baseURI + "/UXP.Services/ecare/Profile/UserProfile/Me/Password");

//            if (response1.statusCode() != 200) {
//                System.out.println(bup);
//            }

        } catch (Exception e) {
            // ignore
            System.out.println(bup);
//            System.out.println("Failed: " + bup + ":" + password);

//            System.out.println("----------" + e.getMessage());
        }
    }


    // ****Deep Link Data Provider *****
    @DataProvider(parallel = true, name = "DeepLinksDataProvider")
    public synchronized Iterator<Object[]> DeepLinksDataProvider(ITestContext context) {
        List<Object[]> list = new ArrayList<Object[]>();
        String pathname = context.getCurrentXmlTest().getParameter("p_Testdata");

        File file = new File(pathname);

        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] keys = reader.readNext();
            if (keys != null) {
                String[] dataParts;
                while ((dataParts = reader.readNext()) != null) {
                    Map<String, String> testData = new HashMap<String, String>();
                    for (int i = 0; i < keys.length; i++) {
                        if (keys[i].equalsIgnoreCase("AndroidTestKey") || keys[i].equalsIgnoreCase("IOSTestKey")) {
                            context.setAttribute("testKey", dataParts[i]);
//                            log.info("Jira-TestKey : "+ keys[i] + ":"+dataParts[i]);
                        }

                        testData.put(keys[i], dataParts[i]);
                    }
                    list.add(new Object[]{testData});
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(
                    "File :" + pathname + " was not found. \n" + e.getStackTrace().toString());
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not read" + pathname + "file.\n" + e.getStackTrace().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list.iterator();
    }
}
