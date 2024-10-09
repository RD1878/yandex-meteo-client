package org.example;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    final double latitude = 55.7887;
    final double longitude = 49.1221;
    final String yandexUrl = "https://api.weather.yandex.ru/v2/forecast";

    int daysLimit;

    HttpClient httpClient = HttpClient.newHttpClient();


    public void setDaysLimit(int daysLimit) {
        this.daysLimit = daysLimit;
    }

    private String getCoordinatesString() {
        return "lat=" + latitude + "&" + "lon=" + longitude;
    }

    public HttpRequest createRequest(String accessKey) {
        URI uri = URI.create(yandexUrl + "?" + getCoordinatesString() + "&limit=" + daysLimit);

        return HttpRequest.newBuilder().uri(uri).header("X-Yandex-Weather-Key", accessKey).GET().build();
    }

    public void fetchData(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject fact = jsonObject.getJSONObject("fact");
                int currentTemperature = fact.getInt("temp");

                System.out.println("All weather data: " + jsonResponse);
                System.out.println("Current temperature: " + currentTemperature);
            }
        } catch (Exception e) {
            System.err.println("Error making HTTP request: " + e.getMessage());
        }
    }
}
