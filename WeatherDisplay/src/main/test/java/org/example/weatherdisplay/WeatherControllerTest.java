package org.example.weatherdisplay;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class WeatherControllerTest {

    private WeatherController testController;

    @BeforeAll
    public static void setupClass() {
        Platform.startup(() -> {
            // No need to implement anything inside; it just initializes the toolkit
        });
    }

    @BeforeEach
    public void setUp() throws Exception {
        URL fxmlUrl = getClass().getResource("/org/example/weatherdisplay/weather-view.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML file not found at the specified location!");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        Parent root = loader.load();
        testController = loader.getController();

        assertNotNull(root, "FXML file did not load properly!");
        assertNotNull(testController, "Controller was not initialized!");
    }

    @Test
    void testFetchWeather() throws Exception {
        String testCity = "Kyiv";

        testController.fetchWeather(testCity);

        assertTrue(testController.temperatureLabel.isVisible(), "FXML file not found at the specified location!");
    }

    @Test
    void testFetchWeatherLabels() throws Exception {
        String testCity1 = "Kyiv";

        testController.fetchWeather(testCity1);

        Thread.sleep(5000);

        assertFalse(testController.temperatureLabel.getText().isEmpty(), "Temperature label is empty");
        assertFalse(testController.windLabel.getText().isEmpty(), "Wind label is empty");
        assertFalse(testController.humidityLabel.getText().isEmpty(), "Humidity label is empty");
        assertFalse(testController.descriptionLabel.getText().isEmpty(), "Description label is empty");
    }

    @Test
    void testHourlyForecastDisplays() throws Exception {
        String testCity = "Kyiv";

        testController.fetchWeather(testCity);

        // Simulate some delay to allow asynchronous data loading
        Thread.sleep(10000);

        Platform.runLater(() -> {
            assertFalse(testController.hourlyForecastContainer.getChildren().isEmpty(), "Hourly forecast grid should have data");

            Object firstChild = testController.hourlyForecastContainer.getChildren().get(0);
            assertTrue(firstChild instanceof GridPane, "The first child should be a GridPane");
        });
    }

    @Test
    void testFetchWeatherWithInvalidCity() throws Exception {
        String invalidCity = "";

        testController.fetchWeather(invalidCity);

        Platform.runLater(() -> {
            try {
                Thread.sleep(500);

                assertTrue(testController.errorLabel.isVisible(), "Error label should be visible when city is invalid");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(1000);
    }
}
