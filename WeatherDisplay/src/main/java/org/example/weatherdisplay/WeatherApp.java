package org.example.weatherdisplay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Create a main app that will lunch the FXML
public class WeatherApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/weatherdisplay/weather-view.fxml"));
        Parent root = loader.load();

        // Get the controller and load the appropriate to time frame background on launch
        WeatherController controller = loader.getController();
        controller.updateBackground();

        primaryStage.setTitle("Weather App");
        // Set the scene to have a window to disaplay an app
        Scene scene = new Scene(root, 800,600);
        scene.getStylesheets().add(getClass().getResource("/org/example/weatherdisplay/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
