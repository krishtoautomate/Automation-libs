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


public class BellLogin {


    @Test(dataProvider = "DeepLinksDataProvider", groups = {"DeepLinks"})
    public void Bell_Login(Map<String, String> data) {
//        System.out.println(data.get("username"));

            String user = data.get("username");
            String password = "Leo1234$";

            // Specify the proxy address
            RestAssured.useRelaxedHTTPSValidation();

            RestAssured.baseURI = "https://apigate.bell.ca/channelbellcaext";

            String Brand = "B";// "B" "V";
            String applicationid = "MBM_IOS";// MVM_IOS

            try {
                Thread.sleep(5000);

                Response response = given().relaxedHTTPSValidation().auth().basic(user, password)
                        .header("accept-language", "en-ca")
                        .header("brand", Brand)
                        .header("applicationid", applicationid)
                        .get(RestAssured.baseURI + "/Authentication/BUP");

                if(response.getBody().path("status").toString().equalsIgnoreCase("fail"))
                    System.out.println(user);
//                    System.out.println("status : " + response.getBody().path("status").toString()); // .asPrettyString().);

//                System.out.println("----------");

            } catch (Exception e) {
                // ignore
                System.out.println("Failed: " + user + ":" + password);

                System.out.println("----------" + e.getMessage());
            }
    }



    // ****Deep Link Data Provider *****
    @DataProvider(parallel = true)
    public synchronized Iterator<Object[]> DeepLinksDataProvider() {
        List<Object[]> list = new ArrayList<Object[]>();
//        String pathname = context.getCurrentXmlTest().getParameter("p_Testdata");

//        File file = new File(pathname);
        File file = new File(Constants.USER_DIR+"/BellLogin.csv");

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
