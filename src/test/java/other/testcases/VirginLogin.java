package other.testcases;

import com.Utilities.Constants;
import com.opencsv.CSVReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.util.*;

import static io.restassured.RestAssured.given;


public class VirginLogin {


    @Test(dataProvider = "DeepLinksDataProvider", groups = {"DeepLinks"})
    public void Virgin_Login(Map<String, String> data) {
//        System.out.println(data.get("username"));

            String user = data.get("username");
            String password = "Leo1234$";

            // Specify the proxy address
            RestAssured.useRelaxedHTTPSValidation();

            RestAssured.baseURI = "https://api.virginplus.ca/channelvirginext";

            String Brand = "V";
            String applicationId = "MVM_IOS";

            try {
                Thread.sleep(5000);

                Response response = given().relaxedHTTPSValidation().auth().basic(user, password)
                        .header("accept-language", "en-ca")
                        .header("brand", Brand)
                        .header("applicationid", applicationId)
                        .get(RestAssured.baseURI + "/Authentication/BUP");

                if(response.getBody().path("status").toString().equalsIgnoreCase("fail"))
                    System.out.println(user);


            } catch (Exception e) {
                // ignore
//                System.out.println("Failed: " + user + ":" + password);

//                System.out.println("----------" + e.getMessage());

                System.out.println("----------");
            }
    }



    // ****Deep Link Data Provider *****
    @DataProvider(parallel = true)
    public synchronized Iterator<Object[]> DeepLinksDataProvider() {
        List<Object[]> list = new ArrayList<Object[]>();
//        String pathname = context.getCurrentXmlTest().getParameter("p_Testdata");

//        File file = new File(pathname);
        File file = new File(Constants.USER_DIR+"/VirginLogin.csv");

        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] keys = reader.readNext();
            if (keys != null) {
                String[] dataParts;
                while ((dataParts = reader.readNext()) != null) {
                    Map<String, String> testData = new HashMap<String, String>();
                    for (int i = 0; i < keys.length; i++) {
                        testData.put(keys[i], dataParts[i]);
                    }
                    list.add(new Object[]{testData});
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list.iterator();
    }
}
