package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherApp {

    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";
    private static final String BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=";

    public static JSONObject getWeatherData(String city) throws IOException, JSONException {
        URL url = new URL(BASE_URL + city + "&appid=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {

            response.append(line);
        }
        reader.close();

        return new JSONObject(response.toString());
    }

    public static double getTemperature(String date, JSONObject weatherData) throws JSONException {
        JSONArray list = weatherData.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject forecast = list.getJSONObject(i);
            String dt_txt = forecast.getString("dt_txt");
            if (dt_txt.contains(date)) {
                JSONObject main = forecast.getJSONObject("main");
                return main.getDouble("temp");
            }
        }
        return Double.NaN;
    }

    public static double getWindSpeed(String date, JSONObject weatherData) throws JSONException {
        JSONArray list = weatherData.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject forecast = list.getJSONObject(i);
            String dt_txt = forecast.getString("dt_txt");
            if (dt_txt.contains(date)) {
                JSONObject wind = forecast.getJSONObject("wind");
                return wind.getDouble("speed");
            }
        }
        return Double.NaN;
    }

    public static double getPressure(String date, JSONObject weatherData) throws JSONException {
        JSONArray list = weatherData.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject forecast = list.getJSONObject(i);
            String dt_txt = forecast.getString("dt_txt");
            if (dt_txt.contains(date)) {
                JSONObject main = forecast.getJSONObject("main");
                return main.getDouble("pressure");
            }
        }
        return Double.NaN;
    }

    public static void main(String[] args) {
        try {
            String city = "London,us";
            JSONObject weatherData = getWeatherData(city);

            java.util.Scanner scanner = new java.util.Scanner(System.in);

            while (true) {
                System.out.println("\nOptions:");
                System.out.println("1. Get weather");
                System.out.println("2. Get Wind Speed");
                System.out.println("3. Get Pressure");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    if (choice == 1) {
                        System.out.print("Enter the date (YYYY-MM-DD HH:MM:SS): ");
                        String date = scanner.nextLine();
                        double temperature = getTemperature(date, weatherData);
                        if (!Double.isNaN(temperature)) {
                            System.out.println("Temperature on " + date + ": " + temperature + " Kelvin");
                        } else {
                            System.out.println("Weather data not available for the provided date.");
                        }
                    } else if (choice == 2) {
                        System.out.print("Enter the date (YYYY-MM-DD HH:MM:SS): ");
                        String date = scanner.nextLine();
                        double windSpeed = getWindSpeed(date, weatherData);
                        if (!Double.isNaN(windSpeed)) {
                            System.out.println("Wind Speed on " + date + ": " + windSpeed + " m/s");
                        } else {
                            System.out.println("Weather data not available for the provided date.");
                        }
                    } else if (choice == 3) {
                        System.out.print("Enter the date (YYYY-MM-DD HH:MM:SS): ");
                        String date = scanner.nextLine();
                        double pressure = getPressure(date, weatherData);
                        if (!Double.isNaN(pressure)) {
                            System.out.println("Pressure on " + date + ": " + pressure + " hPa");
                        } else {
                            System.out.println("Weather data not available for the provided date.");
                        }
                    } else if (choice == 0) {
                        System.out.println("Exiting the program.");
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } catch (java.util.InputMismatchException e) {
                    // If the input is not an integer, prompt the user to enter again.
                    System.out.println("Invalid input. Please enter a valid integer choice.");
                    scanner.nextLine(); // Consume the invalid input to avoid an infinite loop.
                }
            }

            scanner.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}
