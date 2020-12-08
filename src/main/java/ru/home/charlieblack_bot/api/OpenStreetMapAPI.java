package ru.home.charlieblack_bot.api;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import ru.home.charlieblack_bot.model.UserProfileData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenStreetMapAPI {
    private URIBuilder builder;
    private long totalDistance;

    public OpenStreetMapAPI(UserProfileData userProfileData){
//        this.builder = new URIBuilder()
//                .setScheme("http")
//                .setHost("routes.maps.sputnik.ru")
//                .setPath("/osrm/router/viaroute")
//                .addParameter("loc", userProfileData.getDeparturePoint())
//                .addParameter("loc", userProfileData.getDestinationPoint());
//        this.totalDistance = 0;
    }

    public long getTotalDistance(){
        //long totalDistance = 0;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(builder.toString()))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonObject = new JSONObject(response.body());
            JSONObject routeSummary = jsonObject.getJSONObject("route_summary");
            totalDistance = routeSummary.getInt("total_distance");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return totalDistance;
    }

}
