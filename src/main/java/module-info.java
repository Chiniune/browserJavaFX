module com.example.browseral {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires ipinfo.api;
    requires json.simple;
    requires com.google.gson;
    requires jjwt;
//    requires news.api.java;


    opens com.example.browseral to javafx.fxml;
    exports com.example.browseral;
    exports com.example.browseral.controllers;
    opens com.example.browseral.controllers to javafx.fxml;
}