module org.example.weatherdisplay {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.google.gson;

    opens org.example.weatherdisplay to javafx.fxml;
    exports org.example.weatherdisplay;
}