package com.example.browseral.controllers;

import com.example.browseral.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class sceneController {
    public static void switchScene(ActionEvent e, String file) {
        try {
            Parent root = FXMLLoader.load(MainApplication.class.getResource(file));
//            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(MainApplication.class.getResource("style.css").toExternalForm());
//            Screen screen = Screen.getPrimary();
            stage.setScene(scene);
            stage.show();
            if (file.equals("FXMLDocument.fxml")) {
                stage.setMaximized(true);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static void switchScene(MouseEvent e, String file) {
        try {
            Parent root = FXMLLoader.load(MainApplication.class.getResource(file));
//            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(MainApplication.class.getResource("style.css").toExternalForm());
//            Screen screen = Screen.getPrimary();
            stage.setScene(scene);
            stage.show();
            if (file.equals("FXMLDocument.fxml")) {
                stage.setMaximized(true);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
