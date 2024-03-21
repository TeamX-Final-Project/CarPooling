package org.example.carpooling.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class DistanceTravelImpl {

    private static final String bingMapsApiKey = "AiBFhZ1cxdAmuWT8wXlxv3VbYasqPgbjqmM1P1O69JAvPPPxv_xJm9Q3Twu1GoU0";

    public static int[] getTravelDetails(String startPoint, String endPoint) {
        try {
            String encodedStart = URLEncoder.encode(startPoint, StandardCharsets.UTF_8);
            String encodedEnd = URLEncoder.encode(endPoint, StandardCharsets.UTF_8);
            String url = "http://dev.virtualearth.net/REST/V1/Routes/Driving?" + "wp.0=" + encodedStart + "&wp.1=" +
                    encodedEnd + "&key=" + bingMapsApiKey;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject resourceSets = jsonResponse.getJSONArray("resourceSets").getJSONObject(0);
            JSONObject resources = resourceSets.getJSONArray("resources").getJSONObject(0);
            int travelDistance = resources.getInt("travelDistance");
            int travelDuration = resources.getInt("travelDuration");

            int[] result = new int[2];
            result[0] = travelDistance;
            result[1] = travelDuration / 60;
            return result;
        } catch (IOException | InterruptedException | JSONException e) {
            throw new RuntimeException("Error retrieving travel details.");
        }
    }
}

