/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.browseral;
import com.example.browseral.models.User;
import com.example.browseral.services.impl.UserServiceImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApplication extends Application {
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
//        Screen screen = Screen.getPrimary();
        Parent root = FXMLLoader.load(getClass().getResource("loginFXML.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Browser");
        stage.show();   
    }
    
    


    public static void main(String[] args) {
//        System.out.println(new UserServiceImpl().getUserById(12).toString());
//        User user = new User();
//        user.setEmail("kietcon@gmail.com");
//        user.setPassword("kietcon@gmail");
//        user.setFullName("Hoang Kiet");
//        new UserServiceImpl().createUser(user);
        launch(args);
    }
    
}
