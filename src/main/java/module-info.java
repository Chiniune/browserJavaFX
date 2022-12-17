module com.example.alsbrowser {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;


    opens com.example.alsbrowser to javafx.fxml;
    exports com.example.alsbrowser;
}