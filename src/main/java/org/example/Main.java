package org.example;

import java.net.http.HttpRequest;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please input yandex weather access key: ");
        String accessKey = scanner.nextLine();

        System.out.print("Please input days limit for forecast: ");
        int daysLimit = scanner.nextInt();

        Client weatherClient = new Client();

        weatherClient.setDaysLimit(daysLimit);
        HttpRequest request = weatherClient.createRequest(accessKey);
        weatherClient.fetchData(request);
    }
}