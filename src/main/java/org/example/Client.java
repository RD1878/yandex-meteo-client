package org.example;

import org.json.JSONArray;
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
        if (accessKey == null || accessKey.isEmpty()) {
            throw new IllegalArgumentException("Access key cannot be empty");
        }

        URI uri = URI.create(yandexUrl + "?" + getCoordinatesString() + "&limit=" + daysLimit);

        return HttpRequest.newBuilder().uri(uri).header("X-Yandex-Weather-Key", accessKey).GET().build();
    }

    private String calculateAverageTemperature(JSONObject jsonObject) {
        JSONArray forecasts = jsonObject.getJSONArray("forecasts");

        double sum = 0;
        int count = 0;

        for (int i = 0; i < forecasts.length(); i++) {
            JSONObject forecast = forecasts.getJSONObject(i);

            if (forecast.has("parts") && forecast.getJSONObject("parts").has("day")) {
                JSONObject dayPart = forecast.getJSONObject("parts").getJSONObject("day");

                if (dayPart.has("temp_avg")) {
                    double tempAvg = dayPart.getInt("temp_avg");
                    sum += tempAvg;
                    count++;
                }
            }
        }

        if (count > 0) {
            return String.format("%.2f", sum / count);
        } else {
            throw new RuntimeException("No temperature data found");
        }
    }


    public void fetchData(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject fact = jsonObject.getJSONObject("fact");
                int currentTemperature = fact.getInt("temp");
                String averageTemperature = calculateAverageTemperature(jsonObject);

                System.out.println("All weather data: " + jsonResponse);
                System.out.println("Current temperature: " + currentTemperature + "°C");
                System.out.println("Average temperature: " + averageTemperature + "°C");
            }

            if (response.statusCode() == 403) {
                System.out.println("Incorrect access key");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Error making HTTP request: " + e.getMessage());
        }
    }
}
