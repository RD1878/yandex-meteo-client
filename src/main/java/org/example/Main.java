package org.example;

import java.net.http.HttpRequest;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please input yandex weather access key: ");
        String accessKey = scanner.nextLine();

        System.out.print("Please input days limit (1-11) for forecast: ");

        if (scanner.hasNextInt()) {
            int daysLimit = scanner.nextInt();

            if (daysLimit > 0 && daysLimit < 12) {
                Client weatherClient = new Client();

                weatherClient.setDaysLimit(daysLimit);
                HttpRequest request = weatherClient.createRequest(accessKey);
                weatherClient.fetchData(request);
            } else {
                System.out.println("Wrong max days limit. Please enter a number between 1 and 11");
            }
        } else {
            System.out.println("Invalid input! Please enter a valid number");
        }
    }
}