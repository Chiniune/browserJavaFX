package com.example.alsbrowser;

import com.example.alsbrowser.model.NewsModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class NewsController implements Initializable {

    @FXML
    private AnchorPane newsImage;
    @FXML
    private Label newsDate;
    @FXML
    private Label newsTitle;
    @FXML
    private Label newsContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setNewsData(NewsModel news) {
        String imgURL = "file:src/images/"+news.getImage();
        newsImage.setBackground(new Background(new BackgroundImage(new Image(imgURL, 1000, 1000, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false))));

        newsTitle.setText(news.getTitle());
        newsDate.setText(news.getDate());
        newsContent.setText(news.getContent());
    }

}
