package ru.kpfu.itis.sergeev.oristest;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ChatBotController {
    private boolean botStarted = false;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    void handleUserInput(ActionEvent event) {
        String userInput = inputField.getText().toLowerCase();
        String response = "";

        if (userInput.equals("/start")) {
            displayResponse("Вы: " + userInput);
            botStarted = true;
            response = "Бот запущен. Вводите команды.";
        } else if (userInput.equals("/end") && botStarted) {
            displayResponse("Вы: " + userInput);

            response = "Бот остановлен. До свидания!";
            botStarted = false;
            System.exit(0);
        } else if (userInput.equals("/help") && botStarted) {
            displayResponse("Вы: " + userInput);
            displayResponse("Вот список команд бота:");
            displayResponse("/start - запуск бота");
            displayResponse("/end - закрывает бота");
            displayResponse("/weather - после этого сообщения, через пробел нужно указать название города на английском");
            displayResponse("/exchange - необходимо указать две валюты, например `/exchange USD RUB`");
        } else if (userInput.contains("/weather") && botStarted) {
            String city = userInput.trim().split(" ")[1];
            response = getWeather(city);
        } else if (userInput.contains("/exchange") && botStarted) {
            displayResponse("Вы: " + userInput);
            String value1 = userInput.trim().split(" ")[1];
            String value2 = userInput.trim().split(" ")[2];
            response = getCurrency(value1, value2);
        } else {
            response = "Неизвестная команда. Введите 'help' для получения списка команд.";
        }

        displayResponse(response);
        inputField.clear();
    }

    private void displayResponse(String response) {
        if (botStarted) {
            chatArea.appendText("Бот: " + response + "\n");
        }
        else {
            chatArea.appendText("Вам нужно запустить бота для выполнения команд" + "\n");
        }
    }

    private String getWeather(String city) {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=143c9d8999112b2f489a1e3a44de6ade";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            JSONArray weatherArray = jsonResponse.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);
            String description = weatherObject.getString("description");

            JSONObject mainObject = jsonResponse.getJSONObject("main");
            double temperatureKelvin = mainObject.getDouble("temp");

            double temperatureCelsius = temperatureKelvin - 273.15;

            return "Weather in " + city + ": " + description + "\nTemperature: " + temperatureCelsius + "°C";
        } catch (Exception e) {
            return "Error fetching weather information.";
        }
    }


    private static String getCurrency(String baseCurrency, String targetCurrency) {


        String apiUrl = "https://v6.exchangerate-api.com/v6/71eac03047ea793f43fda7e8/latest/" + baseCurrency.toUpperCase();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");

            double exchangeRate = conversionRates.getDouble(targetCurrency.toUpperCase());

            return String.format("1 %s = %.4f %s", baseCurrency, exchangeRate, targetCurrency);
        } catch (Exception e) {
            return "Error fetching currency exchange rate.";
        }
    }
}