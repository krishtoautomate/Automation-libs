package com.base;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Gridgraphql {

    public String getNodeUri(String sessionId) {
        // Set up the GraphQL query
        String query = "query SessionsInfo { sessionsInfo { sessions { id nodeUri } } }";

        // Set up the HTTP client and request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://bqatautomation.bell.corp.bce.ca:5555/graphql"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"query\": \"" + query + "\"}"))
                .build();

        // Execute the request and get the response
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response and extract the session information
            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray sessions = jsonObject.getJSONObject("data").getJSONObject("sessionsInfo").getJSONArray("sessions");

            for (int i = 0; i < sessions.length(); i++) {
                JSONObject session = sessions.getJSONObject(i);
                String _sessionId = session.getString("id");
//                String capabilities = session.getString("capabilities");
                String nodeUri = session.getString("nodeUri");

                // Do something with the session information
                if (_sessionId.equalsIgnoreCase(sessionId)) {
//                    System.out.println("Session ID: " + sessionId);
//                    System.out.println("Capabilities: " + capabilities);
//                    System.out.println("Node URI: " + nodeUri);
                    return nodeUri;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
