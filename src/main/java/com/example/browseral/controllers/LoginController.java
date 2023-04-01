package com.example.browseral.controllers;

import com.example.browseral.MainApplication;
import com.example.browseral.dao.impl.MacAddressDAOImpl;
import com.example.browseral.dao.impl.UserDAOImpl;
import com.example.browseral.models.AccountModel;
import com.example.browseral.models.MacAddress;
import com.example.browseral.models.User;
import com.example.browseral.services.impl.UserServiceImpl;
import com.example.browseral.utils.IPNetworkUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane loginPane;
    @FXML
    private GridPane accountGridPane;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    private List accountList;
    @FXML
    private ImageView sayhiIv;
    @FXML
    private ImageView emailIv;
    @FXML
    private ImageView passIv;
    @FXML
    private Button loginBtn;
    int jj = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loginPane.setBackground(new Background(new BackgroundImage(new Image("file:src/images/login.png", 1000, 1000, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false))));
        showAccountList();

        // icon login
        Image emailImg = new Image("file:src/images/person.png");
        emailIv.setImage(emailImg);
        Image passImg = new Image("file:src/images/password.png");
        passIv.setImage(passImg);
        Image hiImg = new Image("file:src/images/sayhi.png");
        sayhiIv.setImage(hiImg);

        //set action login
        loginBtn.setOnAction(e -> {
            System.out.println(email.getText());
            System.out.println(password.getText());

            if (new UserDAOImpl().checkUser(email.getText())){
                new UserDAOImpl().updateUser2(1,email.getText());
                sceneController.switchScene(e,"FXMLDocument.fxml");}
            else{

                System.out.println("ch có email");
                User userx = new User();
                userx.setEmail(email.getText());
                userx.setPassword(password.getText());
                userx.setFullName("Người dùng");
                new UserServiceImpl().createUser(userx);
                new UserDAOImpl().updateUser2(1,email.getText());
                IPNetworkUtils ipNetworkUtils = new IPNetworkUtils();

                MacAddress a = new MacAddress();
                a.setMacAddress(ipNetworkUtils.getMacAddress());
                new MacAddressDAOImpl().createMacAddress(a);
                System.out.println(a);
                sceneController.switchScene(e,"FXMLDocument.fxml");

                new MacAddressDAOImpl().get2(email.getText(), ipNetworkUtils.getMacAddress());


            }

        });

        email.setFocusTraversable(false);
        password.setFocusTraversable(false);
    }

    public void showAccountList(){
        IPNetworkUtils ipNetworkUtils = new IPNetworkUtils();
        List<String> listUser = new MacAddressDAOImpl().findAllUserByAddress(ipNetworkUtils.getMacAddress());
        List<AccountModel> acc = new ArrayList<>();
        int index = 0;
        for (String i: listUser) {
            acc.add(new AccountModel(index,i,"Jimin.png"));
            index++;
        }

        ObservableList<AccountModel> list = FXCollections.observableArrayList();
        list.removeAll(list);
        list.addAll(acc);

        int column = 0;
        int row = 0;

        for (int i = 0; i < list.size(); i++){
            Circle avt = new Circle();
            Image iv = new Image("file:src/images/"+list.get(i).getAvt());
            avt.setFill(new ImagePattern(iv));
            avt.setRadius(35);
            Label name = new Label(list.get(i).getEmailName());
            VBox box = new VBox();
            box.getChildren().addAll(avt, name);
            box.setSpacing(5);
            box.setPrefSize(30, 40);
            box.getStyleClass().add("grid-cell");

            box.setOnMouseClicked(e->{
                String s = box.getChildren().get(1).toString().substring(33,box.getChildren().get(1).toString().length()-1);
                new UserDAOImpl().updateUser(1,s);
                sceneController.switchScene(e,"FXMLDocument.fxml");
            });



            if (column == 3) {
                column = 0;
                row++;
            }
            accountGridPane.add(box, column++, row);
            GridPane.setMargin(box, new Insets(10));

        }


    }

}
