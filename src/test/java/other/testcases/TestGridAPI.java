package other.testcases;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestGridAPI {

    public static void main(String[] args) throws IOException {

        String hubUrl = "http://172.21.34.239:4444/grid/api/hub";

        // Create a new HTTP client
        CloseableHttpClient client = HttpClients.createDefault();

        // Make a GET request to the hub API endpoint
        HttpGet request = new HttpGet(hubUrl);
        CloseableHttpResponse response = client.execute(request);

        try {
            // Get the response entity as a string
            HttpEntity entity = response.getEntity();
            String jsonString = EntityUtils.toString(entity);

            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(jsonString);

            // Get the list of nodes
//            JSONArray nodesArray = jsonObject.getJSONArray("nodes");

            System.out.println(jsonObject);

            // Loop through the nodes and print their capabilities
//            for (int i = 0; i < nodesArray.length(); i++) {
//                JSONObject nodeObject = nodesArray.getJSONObject(i);
//                JSONObject capabilitiesObject = nodeObject.getJSONObject("capabilities");
//                System.out.println("Node " + (i+1) + " capabilities:");
//                System.out.println(capabilitiesObject.toString());
//            }

        } finally {
            // Close the response and client objects
            response.close();
            client.close();
        }
    }
}
