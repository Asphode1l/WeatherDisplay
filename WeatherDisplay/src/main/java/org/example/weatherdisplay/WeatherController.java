package org.example.weatherdisplay;

import com.google.gson.JsonArray;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherController {

    @FXML
    private TextField cityTextField;
    @FXML
    private Label weatherLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label windLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label loadingLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Button switchUnitButton;
    @FXML
    private ImageView weatherIcon;
    @FXML
    private VBox hourlyForecastContainer;
    @FXML
    private AnchorPane root;
    @FXML
    private VBox dailyForecastContainer;

    private boolean isCelsius = true;
    private String apiKey = "1890187ff59f5a8d167198cc1c0a20cf";

    @FXML
    protected void onCityEntered() {
        String city = cityTextField.getText();
        fetchWeather(city);

    }

    @FXML
    protected void onSwitchUnit() {
        isCelsius = !isCelsius;
        switchUnitButton.setText(isCelsius ? "Switch to Fahrenheit" : "Switch to Celsius");
        onCityEntered();
    }

    @FXML
    protected void onHourlyForecast() {
        loadingLabel.setVisible(true);
        hourlyForecastContainer.setVisible(false);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    String city = cityTextField.getText();
                    String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());

                    String geoUrl = String.format(
                            "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
                            encodedCity, apiKey);
                    String response = makeApiCall(geoUrl);

                    JsonObject geoData = JsonParser.parseString(response).getAsJsonArray().get(0).getAsJsonObject();
                    double lat = geoData.get("lat").getAsDouble();
                    double lon = geoData.get("lon").getAsDouble();

                    String unit = isCelsius ? "metric" : "imperial";
                    String forecastUrl = String.format(
                            "https://pro.openweathermap.org/data/2.5/forecast/hourly?lat=%f&lon=%f&units=%s&appid=%s",
                            lat, lon, unit, apiKey);
                    String forecastResponse = makeApiCall(forecastUrl);

                    JsonObject forecastJson = JsonParser.parseString(forecastResponse).getAsJsonObject();
                    JsonArray hourly = forecastJson.getAsJsonArray("list");

                    updateHourlyForecast(hourly);

                } catch (Exception e) {
                    e.printStackTrace();
                    updateErrorLabel("Error fetching hourly forecast: " + e.getMessage());
                }
                return null;
            }
        };

        new Thread(task).start();
    }
    @FXML
    protected void onDailyForecast() {
        loadingLabel.setVisible(true);
        dailyForecastContainer.setVisible(false);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    String city = cityTextField.getText();
                    String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());

                    String geoUrl = String.format(
                            "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
                            encodedCity, apiKey);
                    String response = makeApiCall(geoUrl);

                    JsonObject geoData = JsonParser.parseString(response).getAsJsonArray().get(0).getAsJsonObject();
                    double lat = geoData.get("lat").getAsDouble();
                    double lon = geoData.get("lon").getAsDouble();

                    String unit = isCelsius ? "metric" : "imperial";
                    String dailyForecastUrl = String.format(
                            "https://api.openweathermap.org/data/2.5/forecast/daily?lat=%f&lon=%f&units=%s&appid=%s",
                            lat, lon, unit, apiKey);
                    String forecastResponse = makeApiCall(dailyForecastUrl);

                    JsonObject forecastJson = JsonParser.parseString(forecastResponse).getAsJsonObject();
                    JsonArray daily = forecastJson.getAsJsonArray("list");

                    updateDailyForecast(daily);
                } catch (Exception e) {
                    e.printStackTrace();
                    updateErrorLabel("Error fetching daily forecast: " + e.getMessage());
                }
                return null;
            }
        };

        new Thread(task).start();
    }
    private void fetchWeather(String city) {
        loadingLabel.setVisible(true);
        errorLabel.setVisible(false);


        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());

                    String geoUrl = String.format(
                            "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
                            encodedCity, apiKey);
                    String response = makeApiCall(geoUrl);

                    JsonObject jsonObject = JsonParser.parseString(response).getAsJsonArray().get(0).getAsJsonObject();
                    double lat = jsonObject.get("lat").getAsDouble();
                    double lon = jsonObject.get("lon").getAsDouble();

                    String unit = isCelsius ? "metric" : "imperial";
                    String weatherUrl = String.format(
                            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                            lat, lon, unit, apiKey);
                    String weatherResponse = makeApiCall(weatherUrl);

                    JsonObject weatherJson = JsonParser.parseString(weatherResponse).getAsJsonObject();
                    JsonObject main = weatherJson.getAsJsonObject("main");
                    JsonObject wind = weatherJson.getAsJsonObject("wind");
                    JsonObject weather = weatherJson.getAsJsonArray("weather").get(0).getAsJsonObject();

                    double temp = main.get("temp").getAsDouble();
                    double windSpeed = wind.get("speed").getAsDouble();
                    double humidity = main.get("humidity").getAsDouble();
                    String description = weather.get("description").getAsString();
                    String iconCode = weather.get("icon").getAsString();

                    updateWeatherLabel(weatherResponse, temp, windSpeed, humidity, description, iconCode);
                    onHourlyForecast();
                    onDailyForecast();
                } catch (Exception e) {
                    e.printStackTrace();
                    updateErrorLabel("An error occurred: " + e.getMessage());
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    private void updateWeatherLabel(String weatherInfo, double temp, double windSpeed, double humidity, String description, String iconCode) {
        Platform.runLater(() -> {
            loadingLabel.setVisible(false);
            weatherLabel.setVisible(false);
            temperatureLabel.setText(String.format("Temperature: %.2f %s", temp, isCelsius ? "°C" : "°F"));
            windLabel.setText(String.format("Wind Speed: %.2f m/s", windSpeed));
            humidityLabel.setText(String.format("Humidity: %.0f%%", humidity));
            descriptionLabel.setText(String.format("Conditions: %s", description));

            String iconUrl = String.format("http://openweathermap.org/img/wn/%s.png", iconCode);
            Image image = new Image(iconUrl);
            weatherIcon.setImage(image);
        });
    }

    private void updateHourlyForecast(JsonArray hourly) {
        if (hourly == null) {
            Platform.runLater(() -> updateErrorLabel("No hourly forecast data available."));
            return;
        }

        Platform.runLater(() -> {
            loadingLabel.setVisible(false);
            hourlyForecastContainer.setVisible(true);
            hourlyForecastContainer.getChildren().clear();

            // Create the header row (hour labels)
            GridPane grid = new GridPane();
            grid.setHgap(50);
            grid.setVgap(50);
            grid.setAlignment(Pos.CENTER);

            // Create hour headers (00:00, 01:00, ..., 23:00)
            for (int i = 0; i < Math.min(8, hourly.size()); i++) {
                String time = LocalDateTime.ofEpochSecond(hourly.get(i).getAsJsonObject().get("dt").getAsLong(), 0, ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("HH:mm"));
                Label hourLabel = new Label(time);
                hourLabel.setStyle("-fx-font-weight: bold;");
                grid.add(hourLabel, i, 0); // Add hour labels in the first row
            }

            // Create rows for temperature, wind speed, and description
            for (int i = 0; i < Math.min(8, hourly.size()); i++) {
                JsonObject hourData = hourly.get(i).getAsJsonObject();
                JsonObject main = hourData.getAsJsonObject("main");
                double temp = main.get("temp").getAsDouble();
                double windSpeed = hourData.getAsJsonObject("wind").get("speed").getAsDouble();
                String description = hourData.getAsJsonArray("weather")
                        .get(0).getAsJsonObject().get("description").getAsString();
                String iconCode = hourData.getAsJsonArray("weather")
                        .get(0).getAsJsonObject().get("icon").getAsString();

                // Temperature row
                Label tempLabel = new Label(String.format("%.1f %s", temp, isCelsius ? "°C" : "°F"));
                grid.add(tempLabel, i, 1); // Add temperature labels in the second row

                // Wind speed row
                Label windLabel = new Label(String.format("%.1f m/s", windSpeed));
                grid.add(windLabel, i, 2); // Add wind speed labels in the third row

                // Icon row (icon images)
                ImageView icon = new ImageView(new Image("http://openweathermap.org/img/wn/" + iconCode + "@2x.png"));
                icon.setFitHeight(30);
                icon.setFitWidth(30);
                grid.add(icon, i, 3); // Add icons in the fifth row

//                Label descriptionLabel = new Label(description);
//                grid.add(descriptionLabel, i, 3);
            }

            // Update the container with the grid
            hourlyForecastContainer.getChildren().add(grid);

        });
    }
    private void updateDailyForecast(JsonArray daily) {
        if (daily == null) {
            Platform.runLater(() -> updateErrorLabel("No daily forecast data available."));
            return;
        }

        Platform.runLater(() -> {
            loadingLabel.setVisible(false);
            dailyForecastContainer.setVisible(true);
            dailyForecastContainer.getChildren().clear();

            // Create the header row (day labels)
            GridPane grid = new GridPane();
            grid.setHgap(20);
            grid.setVgap(20);
            grid.setAlignment(Pos.CENTER);

            // Create day headers (e.g., Monday, Tuesday, ...)
            for (int i = 0; i < Math.min(7, daily.size()); i++) {
                String day = LocalDateTime.ofEpochSecond(daily.get(i).getAsJsonObject().get("dt").getAsLong(), 0, ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("EEE"));
                Label dayLabel = new Label(day);
                dayLabel.setStyle("-fx-font-weight: bold;");
                grid.add(dayLabel, i, 0); // Add day labels in the first row
            }

            // Create rows for temperature, wind speed, and description
            for (int i = 0; i < Math.min(7, daily.size()); i++) {
                JsonObject dayData = daily.get(i).getAsJsonObject();
                JsonObject temp = dayData.getAsJsonObject("temp");
                double dayTemp = temp.get("day").getAsDouble();
               double nightTemp = temp.get("night").getAsDouble();
                double windSpeed = dayData.get("speed").getAsDouble();
                String description = dayData.getAsJsonArray("weather")
                        .get(0).getAsJsonObject().get("description").getAsString();
                String iconCode = dayData.getAsJsonArray("weather")
                        .get(0).getAsJsonObject().get("icon").getAsString();

               // Day temperature row
               Label dayTempLabel = new Label(String.format("Day: %.1f %s", dayTemp, isCelsius ? "°C" : "°F"));
                grid.add(dayTempLabel, i, 1); // Add day temperature labels in the second row

                // Night temperature row
               Label nightTempLabel = new Label(String.format("Night: %.1f %s", nightTemp, isCelsius ? "°C" : "°F"));
               grid.add(nightTempLabel, i, 2); // Add night temperature labels in the third row

                // Wind speed row
                Label windLabel = new Label(String.format("%.1f m/s", windSpeed));
                grid.add(windLabel, i, 3); // Add wind speed labels in the fourth row

                // Icon row (icon images)
                ImageView icon = new ImageView(new Image("http://openweathermap.org/img/wn/" + iconCode + "@2x.png"));
                icon.setFitHeight(30);
                icon.setFitWidth(30);
                grid.add(icon, i, 4); // Add icons in the fifth row

                // Description row
                Label descriptionLabel = new Label(description);
                grid.add(descriptionLabel, i, 5); // Add description labels in the sixth row
            }

            // Update the container with the grid
            dailyForecastContainer.getChildren().add(grid);

        });
    }

    private void updateErrorLabel(String errorMessage) {
        Platform.runLater(() -> {
            loadingLabel.setVisible(false);
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
        });
    }

    private String makeApiCall(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    @FXML
    public void updateBackground() {
        LocalTime now = LocalTime.now();

        if (now.isAfter(LocalTime.of(5, 0)) && now.isBefore(LocalTime.of(19, 0))) {
            // Змінюємо на денний фон
            root.getStyleClass().removeAll("night-background");
            root.getStyleClass().add("day-background");
        } else {
            // Змінюємо на нічний фон
            root.getStyleClass().removeAll("day-background");
            root.getStyleClass().add("night-background");
        }
    }

}
