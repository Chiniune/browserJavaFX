package com.example.alsbrowser;

import com.example.alsbrowser.model.AccountModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.List;
import java.util.ResourceBundle;

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
            switchScene("FXMLDocument.fxml");
        });

        email.setFocusTraversable(false);
        password.setFocusTraversable(false);
    }


    public void switchScene(String file) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(file));
//            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            Stage stage = (Stage) accountGridPane.getParent().getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            Screen screen = Screen.getPrimary();
            Rectangle2D bound = screen.getVisualBounds();
            stage.setWidth(bound.getWidth());
            stage.setHeight(bound.getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void showAccountList(){
        AccountModel acc1 = new AccountModel(1, "Jimin","smile.png" );
        AccountModel acc2 = new AccountModel(2, "Jin ne","news.png" );
        AccountModel acc3 = new AccountModel(3, "Vo Thi Trinh","icon256.png" );
        ObservableList<AccountModel> list = FXCollections.observableArrayList();
        list.removeAll(list);
        list.addAll(acc1,acc2,acc3);

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
                switchScene("FXMLDocument.fxml");
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
