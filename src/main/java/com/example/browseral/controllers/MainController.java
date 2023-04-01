package com.example.browseral.controllers;

import com.example.browseral.MainApplication;
import com.example.browseral.api.GoogleAPIFetching;
import com.example.browseral.dao.impl.MacAddressDAOImpl;
import com.example.browseral.dao.impl.UserDAOImpl;
import com.example.browseral.models.AccountModel;
import com.example.browseral.models.Bookmark;
import com.example.browseral.models.NewsModel;
import com.example.browseral.models.User;
import com.example.browseral.services.BookmarkService;
import com.example.browseral.services.impl.BookmarkServiceImpl;
import com.example.browseral.services.impl.UserServiceImpl;
import com.example.browseral.utils.IPNetworkUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;


public class MainController implements Initializable {

    @FXML
    protected TabPane tabPane;
    @FXML
    private Circle accountIcon;
    @FXML
    private Circle accountIcon2;
    @FXML
    AnchorPane chooseAccountPane;
    @FXML
    VBox vBoxAccountList;
    Boolean accountBoolean = true;
    @FXML
    Label fullname;
    @FXML
    Label email;
    int countAccIconClick = 0;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    MenuItem homePageBackgroundImg;
    @FXML
    protected Menu historyMenu = new Menu(); //no need to make final
    Boolean countHisIconClick = true;

    // shortcut
    @FXML
    private VBox vBoxShortcut;
    private Button addUrlShortcut;


    public class AutoCompleteTextField extends TextField {
        /**
         * The existing auto complete entries.
         */
        private final SortedSet<String> entries;
        /**
         * The pop up used to select an entry.
         */
        private ContextMenu entriesPopup;

        /**
         * Construct a new AutoCompleteTextField.
         */
        public AutoCompleteTextField() {
            super();
            entries = new TreeSet<>();
            entriesPopup = new ContextMenu();
            textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                    if (getText().length() == 0) {
                        entriesPopup.hide();
                    } else {
                        LinkedList<String> searchResult = new LinkedList<>();
                        final List<String> filteredEntries = entries.stream().filter(e -> e.toLowerCase().contains(getText().toLowerCase())).collect(Collectors.toList());
                        entries.add("apple.com"); //entries.add("a"); entries.add("aa"); entries.add("aaa"); entries.add("aab"); entries.add("aac"); entries.add("BBC");
                        entries.add("bing.com");
                        entries.add("google.com");
                        entries.add("microsoft.com");
                        entries.add("yahoo.com");
                        entries.add("facebook.com");
                        searchResult.addAll(entries);
                        if (entries.size() > 0) {
                            populatePopup(searchResult);
                            if (!entriesPopup.isShowing()) {
                                entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                            }
                        } else {
                            entriesPopup.hide();
                        }
                    }
                }
            });

            focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                    entriesPopup.hide();
                }
            });

        }

        /**
         * Get the existing set of auto complete entries.
         *
         * @return The existing auto complete entries.
         */
        public SortedSet<String> getEntries() {
            return entries;
        }

        /**
         * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
         *
         * @param searchResult The set of matching strings.
         */
        private void populatePopup(List<String> searchResult) {
            List<CustomMenuItem> menuItems = new LinkedList<>();
            // If you'd like more entries, modify this line.
            int maxEntries = 10;
            int count = Math.min(searchResult.size(), maxEntries);
            for (int i = 0; i < count; i++) {
                final String result = searchResult.get(i);
                Label entryLabel = new Label(result);
                CustomMenuItem item = new CustomMenuItem(entryLabel, true);
                item.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        setText(result);
                        entriesPopup.hide();
                    }
                });
                menuItems.add(item);
            }
            entriesPopup.getItems().clear();
            entriesPopup.getItems().addAll(menuItems);
        }
    }


    public static SearchEngine srcEng = new SearchEngine("google", "http://www.google.com");

    @FXML
    private CheckMenuItem googleMenuItm;
    @FXML
    private CheckMenuItem bingMenuItm;

    ObservableList<String> histItems = FXCollections.observableArrayList();


    int EtG = 1, EtB = 0;

    @FXML
    private void setEngine() {
        if (googleMenuItm.isSelected() && bingMenuItm.isSelected()) {
            if (EtG > EtB) {
                googleMenuItm.setSelected(false);
                EtG = 0;
                EtB = 1;
                srcEng.setEngine("bing");
                System.out.println("Bing is the eninge and Google is disabled.");
            } else {
                bingMenuItm.setSelected(false);
                EtG = 1;
                EtB = 0;
                srcEng.setEngine("google");
                System.out.println("Google is the eninge and Bing is disabled.");
            }
        } else if (googleMenuItm.isSelected()) {
            System.out.println("Inside google");
            srcEng.setEngine("google");
            System.out.println("Google is the eninge and Bing is disabled.");
            EtG = 1;
        } else if (bingMenuItm.isSelected()) {
            System.out.println("Inside Bing.");
            srcEng.setEngine("bing");
            System.out.println("Bing is the eninge and Google is disabled.");
            EtB = 1;
        }

    }


    private double newTabLeftPadding = 142.0;

    @FXML
    private Button newTabBtn;


    @FXML
    private void newTabFunction(ActionEvent event) {
        NewTab aTab = new NewTab();
        aTab.setTabBackground("file:src/images/background_main.png");
        Tab tab = aTab.createTab();
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab); //take this tab to front
        newTabBtnPosRight();
    }

    private void newTabBtnPosRight() {
        newTabLeftPadding += 131.0;
        AnchorPane.setLeftAnchor(newTabBtn, newTabLeftPadding++);
    }

    public void newTabBtnPosLeft() {
        newTabLeftPadding -= 131.0;
        AnchorPane.setLeftAnchor(newTabBtn, newTabLeftPadding--);
        if (newTabLeftPadding < 142.0) {
            System.out.println("All tabs closed.");
            Platform.exit(); //exits application if all tabs are closed
        }
    }

    private NewTab aTab = new NewTab();

    //history menu options -------------------------------------------------------------------------------
    HistoryObject histObj;
    @FXML
    ListView historyList;
    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;
    @FXML
    DatePicker delStartDatePicker;
    @FXML
    DatePicker delEndDatePicker;
    @FXML
    Label delHistLabel;
    @FXML
    Button delHistButton;
    @FXML
    ListView prevHistoryListView;

    @FXML
    AnchorPane historyAnchorPane;

    @FXML
    private void deleteHistoryFunction() {
        if (delEndDatePicker.getValue() == null) {
            delHistLabel.setText("Please enter end date.");
        }

        if (delStartDatePicker.getValue() == null) {
            delHistLabel.setText("Please enter start date.");
        }

        if (delStartDatePicker.getValue() != null && delEndDatePicker.getValue() != null) {
            delHistLabel.setText("History has been deleted.");
            histObj.deleteHistByDate(delStartDatePicker.getValue(), delEndDatePicker.getValue(), "hist.txt");
            System.out.println("Deleted history from " + delStartDatePicker.getValue() + " to " + delEndDatePicker.getValue());
        }
    }

    @FXML
    private void historyBtnFunction() {
        delHistLabel.setText("Permanently delete history");
        if (countHisIconClick) {
            historyAnchorPane.setVisible(true);
            countHisIconClick = false;
        } else {
            historyAnchorPane.setVisible(false);
            countHisIconClick = true;
            System.out.println("Showing history options");
        }
    }
//    en history --------------------------------------------------------------------------------------

    @FXML
    public void accountIconFunc() {
        if (accountBoolean) {
            chooseAccountPane.setVisible(true);
            accountBoolean = false;
        } else {
            chooseAccountPane.setVisible(false);
            accountBoolean = true;
        }
    }

    User hi = new UserDAOImpl().findByActive();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aTab.setTabBackground("file:src/images/background_main.png");
        Tab tab = aTab.createTab();
        tab.setText("New tab");
        tabPane.getTabs().add(tab);

        //start account modify
//        System.out.println(hi.toString());
        fullname.setText(hi.getFullName());
        email.setText(hi.getEmail());
        Image img = new Image("file:src/images/Jimin.png", false);
        accountIcon.setFill(new ImagePattern(img));
        accountIcon2.setFill(new ImagePattern(img));
        loadAccountList();


        // shortcut list
        addUrlShortcut = new Button();
        ImageView iv5 = new ImageView();
        Image img5 = new Image("file:src/images/plus.png");
        iv5.setImage(img5);
        iv5.setFitHeight(21);
        iv5.setFitWidth(21);
        addUrlShortcut.setGraphic(iv5);
        addUrlShortcut.getStyleClass().add("addUrlShortcut");
        addUrlShortcut.setOnAction(e -> {
            onClickAddShortcutUrl();
        });
        urlList = (ArrayList) hi.getBookmarks();
        vBoxShortcut.getChildren().addAll(addUrlShortcut);
        showListShortUrl();


        //get value from color picker and set that as home page theme
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                //text.setFill(colorPicker.getValue());
                System.out.println("Color choosed: " + colorPicker.getValue());
            }
        });

        //Instantiating history object
        histObj = new HistoryObject();
        startDatePicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                ObservableList<String> prevHistItems = FXCollections.observableArrayList();
                LocalDate date = startDatePicker.getValue();
                System.err.println("Selected date: " + date);
                ArrayList<HistoryObject> ar = new ArrayList();
                if (endDatePicker.getValue() == null) {
                    ar = histObj.getHistByDate(startDatePicker.getValue(), startDatePicker.getValue(), "hist.txt");
                    for (int i = 0; i < ar.size(); i++) {
                        prevHistItems.add(ar.get(i).url);
                    }
                    prevHistoryListView.setItems(prevHistItems);
                    prevHistoryListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("clicked on " + prevHistoryListView.getSelectionModel().getSelectedItem());
                            NewTab aTab = new NewTab();
                            aTab.setTabBackground("file:src/images/background_main.png");
                            aTab.goToURL(prevHistoryListView.getSelectionModel().getSelectedItem().toString());
                            Tab tab = aTab.createTab();
                            tabPane.getTabs().add(tab);
                            tabPane.getSelectionModel().select(tab); //take this tab to front
                            newTabBtnPosRight();
                        }
                    });
                } else {
                    ar = histObj.getHistByDate(startDatePicker.getValue(), endDatePicker.getValue(), "hist.txt");
                    for (int i = 0; i < ar.size(); i++) {
                        prevHistItems.add(ar.get(i).url);
                    }
                    prevHistoryListView.setItems(prevHistItems);
                }
            }
        });

        endDatePicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                ObservableList<String> prevHistItems = FXCollections.observableArrayList();
                LocalDate date = endDatePicker.getValue();
                System.err.println("Selected date: " + date);
                ArrayList<HistoryObject> ar = new ArrayList();
                if (startDatePicker.getValue() == null) {
                    ar = histObj.getHistByDate(endDatePicker.getValue(), endDatePicker.getValue(), "hist.txt");
                    for (int i = 0; i < ar.size(); i++) {
                        prevHistItems.add(ar.get(i).url);
                    }
                    prevHistoryListView.setItems(prevHistItems);
                } else {
                    ar = histObj.getHistByDate(startDatePicker.getValue(), endDatePicker.getValue(), "hist.txt");
                    for (int i = 0; i < ar.size(); i++) {
                        prevHistItems.add(ar.get(i).url);
                    }
                    prevHistoryListView.setItems(prevHistItems);
                }
            }
        });

    }

    public void loadAccountList() {
        IPNetworkUtils ipNetworkUtils = new IPNetworkUtils();
        List<String> listUser = new MacAddressDAOImpl().findAllUserByAddress(ipNetworkUtils.getMacAddress());
        List<AccountModel> acc1 = new ArrayList<>();
        int index = 0;
        for (String i : listUser) {
            acc1.add(new AccountModel(index, i, "Jimin.png"));
            index++;
        }

        ObservableList<AccountModel> list = FXCollections.observableArrayList();
        list.removeAll(list);
        list.addAll(acc1);

        AtomicInteger count = new AtomicInteger(0);
        list.forEach((acc) -> {
            Circle avt = new Circle();
            Image iv = new Image("file:src/images/" + acc.getAvt());
            avt.setFill(new ImagePattern(iv));
            avt.setRadius(12);

            Label name = new Label(acc.getEmailName());

            HBox hBox = new HBox();
            hBox.getStyleClass().add("hBox");
            hBox.getChildren().addAll(avt, name);
            count.getAndIncrement();
            vBoxAccountList.getChildren().add(hBox);
        });
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hBox");
        Label addAccount = new Label("+");
        addAccount.setAlignment(Pos.CENTER);
        addAccount.getStyleClass().add("addAccountIcon");
        Label textAdd = new Label("Login another account");
        hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new UserDAOImpl().updateUserall();
                System.out.println("logout");
                sceneController.switchScene(mouseEvent, "loginFXML.fxml");
            }
        });

        hBox.getChildren().addAll(addAccount, textAdd);
        vBoxAccountList.getChildren().add(hBox);

    }

    //
    public void onClickAddShortcutUrl() {
        Dialog<String> dialog = new Dialog<>();
        dialog.getDialogPane().setMaxWidth(200);
        dialog.getDialogPane().setPadding(new Insets(5, 8, 5, 8));
        dialog.setTitle("Add shortcut");
        dialog.setHeaderText("Please Enter URL");

        ButtonType addButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField txtUrl = new TextField();
        txtUrl.setPromptText("Enter your url here");
        txtUrl.requestFocus();

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // set add button disable = check textField
        txtUrl.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(txtUrl);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new String(txtUrl.getText());
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(url -> {
            Bookmark bookmark = formatNewBookmark(url);
//            System.out.println("url added: " + url);
            urlList.add(bookmark);
            setShortcutIconInList(bookmark);
        });
    }

    public Bookmark formatNewBookmark(String url) {
        BookmarkService bookmarkService = new BookmarkServiceImpl();
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(url);
        bookmarkService.createBookmark(hi, bookmark);
        return bookmark;
    }

    ArrayList<Bookmark> urlList;

    public void showListShortUrl() {
        urlList.forEach((bookmark) -> {
            setShortcutIconInList(bookmark);
        });
    }

    public TextField urlShortcut = new TextField();

    private void setShortcutIconInList(Bookmark bookmark) {
        Button btn = new Button();
        btn.getStyleClass().add("background-hover");
        btn.setId(String.valueOf(urlList.indexOf(bookmark)));
        btn.getStyleClass().add("background-transparent");
        ImageView iv = loadFavicon(bookmark.getUrl());
        iv.setFitHeight(21);
        iv.setFitWidth(21);
        btn.setGraphic(iv);
        vBoxShortcut.getChildren().add(btn);
        btn.setOnMouseClicked(e -> {
            urlShortcut.setText(bookmark.getUrl());
        });
    }

    @FXML
    private void backgroundImgFunction() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(stage);
        System.out.println("You chose this file: " + file.getAbsolutePath());
        aTab.setTabBackground("file:" + file.getAbsolutePath());
    }

    //    button home ----------------------------------------------------------------------------------------------


    @FXML
    private Label homeLabel, reloadLabel, backLabel, forwardLabel;
    @FXML
    private Button settingIcon;
    @FXML
    Button historyBtn;
    @FXML
    private ListView<String> searchSuggestList;

    class NewTab {
        //properties
        private final Tab newTab;
        private final AnchorPane smallAnchor;
        private final ToolBar toolBar;
        private final MenuBar menuBar;
        private final Menu bookmarksMenu, settingsMenu, helpMenu;
        private final HBox hBox;
        private final TextField urlBox;
        private final Button favoriteButton;
        private final Button backButton;
        private final Button forwardButton;
        private final Button reloadButton;
        private final Button homeBtn;

        // anchor news and search
        private VBox homePane;
        private GridPane newsGridPane;
        private ScrollPane newsScrollPane;
        private HBox searchBox2;
        private TextField urlBox2;
        private Button searchBtn2;
        private List<NewsModel> newsModels;

        private final BorderPane borderPane;
        private MyBrowser myBrowser;

        //methods
        public NewTab() {
            newTab = new Tab();
            smallAnchor = new AnchorPane();
            toolBar = new ToolBar();
            menuBar = new MenuBar();
            bookmarksMenu = new Menu();
            settingsMenu = new Menu();
            helpMenu = new Menu();
            hBox = new HBox();
            urlBox = new TextField();
            favoriteButton = new Button();
            backButton = new Button();
            forwardButton = new Button();
            reloadButton = new Button();
            homeBtn = new Button();
            borderPane = new BorderPane();
            homePane = new VBox();
            newsGridPane = new GridPane();
            newsScrollPane = new ScrollPane();
            searchBox2 = new HBox();
            urlBox2 = new TextField();
            searchBtn2 = new Button();
        }

        public Tab createTab() {
            newTab.setText("New Tab");
            newTab.getStyleClass().add("aTab");

            ImageView iv1 = new ImageView();
            Image img1 = new Image("file:src/images/arrow-back.png");
            iv1.setImage(img1);
            iv1.setFitHeight(18);
            iv1.setFitWidth(17);
            backButton.setGraphic(iv1);
            backButton.getStyleClass().add("background-transparent");
            backButton.getStyleClass().add("background-hover");

            ImageView iv2 = new ImageView();
            Image img2 = new Image("file:src/images/arrow-next.png");
            iv2.setImage(img2);
            iv2.setFitHeight(18);
            iv2.setFitWidth(17);
            forwardButton.setGraphic(iv2);
            forwardButton.getStyleClass().add("background-transparent");
            forwardButton.getStyleClass().add("background-hover");


            ImageView iv3 = new ImageView();
            Image img3 = new Image("file:src/images/reload.png");
            iv3.setImage(img3);
            iv3.setFitHeight(20);
            iv3.setFitWidth(20);
            reloadButton.setGraphic(iv3);
            reloadButton.getStyleClass().add("background-transparent");
            reloadButton.getStyleClass().add("background-hover");

            ImageView iv7 = new ImageView();
            Image img7 = new Image("file:src/images/home.png");
            iv7.setImage(img7);
            iv7.setFitHeight(20);
            iv7.setFitWidth(20);
            homeBtn.setGraphic(iv7);
            homeBtn.getStyleClass().add("background-transparent");
            homeBtn.getStyleClass().add("background-hover");

            toolBar.getItems().addAll(backButton, forwardButton, reloadButton, homeBtn);
            toolBar.setPrefHeight(40);
            toolBar.setPrefWidth(549);
            toolBar.getStyleClass().add("-fx-background-color: #da45ef");
            AnchorPane.setTopAnchor(toolBar, 0.0);
            AnchorPane.setLeftAnchor(toolBar, 0.0);
            AnchorPane.setRightAnchor(toolBar, 0.0);
            smallAnchor.getChildren().add(toolBar);

            bookmarksMenu.setText("Bookmarks");
//            settingsMenu.setText("Settings");
            ImageView iv4 = new ImageView();
            Image img4 = new Image("file:src/images/settings.png");
            iv4.setImage(img4);
            iv4.setFitHeight(19);
            iv4.setFitWidth(18);
            settingIcon.setGraphic(iv4);

            ImageView iv6 = new ImageView();
            Image img6 = new Image("file:src/images/history.png");
            iv6.setImage(img6);
            iv6.setFitHeight(18);
            iv6.setFitWidth(17);
            historyBtn.setGraphic(iv6);
            historyBtn.getStyleClass().addAll("background-transparent", "background-hover");
            helpMenu.setText("Help");

            menuBar.getMenus().addAll(bookmarksMenu, settingsMenu, helpMenu);
            menuBar.setPrefWidth(190);
            menuBar.setPrefHeight(40);
            menuBar.setPadding(new Insets(6, 0, 0, 0));
            AnchorPane.setRightAnchor(menuBar, 0.0);


            urlBox.setPromptText("🔎 enter your url here or search something");
            urlBox.setPrefHeight(30);
            urlBox.setPrefWidth(700);
            urlBox.getStyleClass().add("urlBox");
            ImageView iv8 = new ImageView();
            Image img8 = new Image("file:src/images/favorites.png");
            iv8.setImage(img8);
            iv8.setFitHeight(15);
            iv8.setFitWidth(15);
            favoriteButton.setGraphic(iv8);
            favoriteButton.getStyleClass().addAll("background-transparent", "background-hover-circle");

            hBox.getChildren().addAll(urlBox, favoriteButton);
            hBox.getStyleClass().add("hBox-search");
            AnchorPane.setTopAnchor(hBox, 5.0);
            AnchorPane.setLeftAnchor(hBox, 155.0);
            smallAnchor.getChildren().add(hBox);

            AnchorPane.setTopAnchor(borderPane, 40.0);
            AnchorPane.setBottomAnchor(borderPane, 0.0);
            AnchorPane.setLeftAnchor(borderPane, 48.0);
            AnchorPane.setRightAnchor(borderPane, 0.0);
            smallAnchor.getChildren().add(borderPane);

            newTab.setContent(smallAnchor);
            newTab.setOnClosed((Event arg) -> {
                System.out.println("A tab closed.");
                newTabBtnPosLeft();
                if (myBrowser != null) {
                    myBrowser.closeWindow();
                }
            });

            backButton.setOnMouseClicked((MouseEvent me) -> {
                System.out.println("Back button has been pressed.");
                myBrowser.goBack();
            });
            backButton.setOnMouseEntered(e -> {
                backLabel.setVisible(true);
            });
            backButton.setOnMouseExited(e -> {
                backLabel.setVisible(false);
            });

            forwardButton.setOnMouseClicked((MouseEvent me) -> {
                System.out.println("Forward button has been pressed.");
                myBrowser.goForward();
            });
            forwardButton.setOnMouseEntered(e -> {
                forwardLabel.setVisible(true);
            });
            forwardButton.setOnMouseExited(e -> {
                forwardLabel.setVisible(false);
            });

            favoriteButton.setOnAction((ActionEvent e) -> {
                System.out.println("url added: " + urlBox.getText());
                Bookmark bookmark = formatNewBookmark(urlBox.getText());
                urlList.add(bookmark);
                setShortcutIconInList(bookmark);
            });

            reloadButton.setOnAction((ActionEvent e) -> {
                myBrowser.reloadWebPage();
            });
            reloadButton.setOnMouseEntered(e -> {
                reloadLabel.setVisible(true);
            });
            reloadButton.setOnMouseExited(e -> {
                reloadLabel.setVisible(false);
            });

            searchBtn2.setOnAction(e -> {
                urlBox.setText(urlBox2.getText());
                goButtonPressed();
            });
            urlBox2.setOnAction(e -> {
                urlBox.setText(urlBox2.getText());
                goButtonPressed();
            });
            urlBox.setOnAction((ActionEvent e) -> {
                goButtonPressed();
                searchSuggestList.setVisible(false);
            });
            urlBox.setOnKeyTyped((KeyEvent e) -> {
                searchSuggestion(e);
            });

            urlBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
                searchSuggestList.setVisible(true);
            });

            searchSuggestList.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    searchSuggestList.setVisible(false);
                }
            });

            urlShortcut.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newTab.isSelected()) {
                    urlBox.setText(urlShortcut.getText());
                    goButtonPressed();
                    urlShortcut.setText("");
                }
            });

            homeBtn.setOnAction(e -> {
                backHome();
            });
            homeBtn.setOnMouseEntered(e -> {
                homeBtnHover();
            });
            homeBtn.setOnMouseExited(e -> {
                homeBtnHoverExit();
            });

            searchSuggestList.setOnMousePressed(mouseEvent -> {
                selectSuggestion(mouseEvent);
            });


            // Anchor news and search
            searchBtn2.getStyleClass().addAll("background-transparent", "background-hover-circle");
            ImageView ivSearch = new ImageView();
            Image imgSearch = new Image("file:src/images/search.png");
            ivSearch.setImage(imgSearch);
            ivSearch.setFitWidth(20);
            ivSearch.setFitHeight(20);
            searchBtn2.setGraphic(ivSearch);
            searchBox2.getStyleClass().add("searchBox2");
            urlBox2.getStyleClass().add("urlBox2");
            urlBox2.setPromptText("🔎 enter your url here or search something");
            searchBox2.getChildren().addAll(urlBox2, searchBtn2);
            homePane.getStyleClass().add("homePane");


            showNewsList(newsGridPane);
            newsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            newsScrollPane.setContent(newsGridPane);
            newsGridPane.getStyleClass().add("background-transparent");
            newsScrollPane.getStyleClass().add("background-transparent");

            homePane.getChildren().addAll(searchBox2, newsScrollPane);
            borderPane.setCenter(homePane);

            return newTab;
        }

        private List<NewsModel> setNewsList() {
            List<NewsModel> list = new ArrayList<>();
            NewsModel news1 = new NewsModel();
            news1.setImage("news2.png");
            news1.setDate("15/12/2002");
            news1.setTitle("TP HCM bắn pháo hoa 6 điểm mừng Tết Nguyên đán 2023");
            news1.setContent("Sau hai năm dừng do Covid-19, TP HCM sẽ bắn pháo hoa giao thừa Tết Nguyên đán ở 6 điểm, trong đó một vị trí mới tại huyện Bình Chánh.\n" +
                    "\n" +
                    "Thông tin được Phó giám đốc Sở Văn hoá và Thể thao TP HCM Võ Trọng Nam nói tại chương trình dân hỏi - chính quyền trả lời, sáng 8/1");

            NewsModel news2 = new NewsModel();
            news2.setImage("news.png");
            news2.setDate("15/12/2002");
            news2.setTitle("Kpop 2022: Dấu ấn BTS, Blackpink và làn sóng các nhóm nhạc nữ Gen 4");
            news2.setContent("Trong năm 2022, BTS và Blackpink tiếp tục chứng minh đẳng cấp của hai nhóm nhạc Kpop hàng đầu. " +
                    "Trong khi đó, hàng loạt nhóm nữ Gen 4 như IVE, NewJeans, " +
                    "LE SSERAFIM gặt hái thành tích nhạc số ấn tượng, tạo nên “làn sóng mới. " +
                    "Trong năm 2022, BTS và Blackpink tiếp tục chứng minh đẳng cấp của hai nhóm nhạc Kpop hàng đầu. " +
                    "Trong khi đó, hàng loạt nhóm nữ Gen 4 như IVE, NewJeans, LE SSERAFIM gặt hái thành tích nhạc số ấn tượng, " +
                    "tạo nên “làn sóng mới.");

            NewsModel news3 = new NewsModel();
            news3.setImage("news5.jpg");
            news3.setDate("15/12/2002");
            news3.setTitle("Khôi phục xuất nhập cảnh ở cửa khẩu Việt Nam - Trung Quốc");
            news3.setContent("Cửa khẩu quốc tế Hữu Nghị (Lạng Sơn) hôm nay đón khoảng 5.000 công dân Trung Quốc đến làm thủ tục về nước sau khi nước này thông báo mở cửa. Nhiều người Trung Quốc có mặt tại cửa khẩu trước 6h30 - thời điểm lực lượng chức năng bắt đầu làm việc.");

            NewsModel news4 = new NewsModel();
            news4.setImage("news3.jpg");
            news4.setDate("15/12/2002");
            news4.setTitle("Hoài Linh tạo tiếng cười qua vai diễn nông dân");
            news4.setContent("Đụng vô là phỏng tay (kịch bản: Trường Giang, đạo diễn: Huỳnh Tiến Khoa) là vở kịch đầu tiên ra mắt khán giả TP HCM tại sân khấu mới của Minh Nhí. Sau buổi diễn hôm 7/1, vở sẽ phục vụ khán giả dịp Tết Quý Mão các ngày: mùng 1 (22/1), mùng 2 (23/1), mùng 4 (25/1), mùng 6 (27/1).");

            NewsModel news5 = new NewsModel();
            news5.setImage("news5.jpg");
            news5.setDate("15/12/2002");
            news5.setTitle("Táo quân 2023 tái hiện các cuộc thi sắc đẹp");
            news5.setContent("Nghệ sĩ Công Lý (giữa) trở lại sau một năm vắng mặt vì sức khỏe yếu. Anh là ban giám khảo trong một phần thi của các Táo, nói một số câu ngắn. Dù thoại ít, nghệ sĩ chăm chút hình ảnh, trang phục. Khi anh xuất hiện trên sân khấu, khán giả đồng loạt vỗ tay cổ vũ.");


            list.add(news1);
            list.add(news2);
            list.add(news3);
            list.add(news4);
            list.add(news5);

            return list;
        }

        private void showNewsList(GridPane newsGridPane) {
            newsModels = new ArrayList<>(setNewsList());
            int column = 0;
            int row = 0;
            try {
                for (int i = 0; i <= newsModels.size() - 1; i++) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(MainApplication.class.getResource("newsFXML.fxml"));

                    AnchorPane pane = fxmlLoader.load();
                    NewsController newsController = fxmlLoader.getController();
                    newsController.setNewsData(newsModels.get(i));

                    if (column == 3) {
                        column = 0;
                        row++;
                    }
                    newsGridPane.add(pane, column++, row);
                    GridPane.setMargin(pane, new Insets(10, 20, 10, 20));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void goButtonPressed() {
//            newsTabPane.setVisible(false);
//            searchSuggestList.setVisible(false);
            String urlStr;
            String address = urlBox.getText();
            if (urlBox.getText() != null && !urlBox.getText().isEmpty()) {
                if (!urlBox.getText().contains(".")) {
                    urlStr = "https://www.bing.com/search?q=" + urlBox.getText().replace(" ", "+") + "&sourceid=chrome&ie=UTF-8&aq=" + urlBox.getText().replace(" ", "+");
                } else {
                    if (urlBox.getText().startsWith("https://www") || urlBox.getText().startsWith("http://www")) {
                        urlStr = urlBox.getText();
                    } else if (urlBox.getText().startsWith("https://") || urlBox.getText().startsWith("http://")) {
                        urlStr = urlBox.getText();
                    } else {
                        urlStr = "https://" + urlBox.getText();
                    }
                }

                myBrowser = new MyBrowser(urlStr);
                borderPane.setCenter(myBrowser);
            }
        }

        public void searchSuggestion(KeyEvent e) {
            searchSuggestList.getItems().clear();
            GoogleAPIFetching googleAPIFetching = new GoogleAPIFetching();
            String searchKey = urlBox.getText();
            if (searchKey.equals("")) {
                searchSuggestList.setVisible(false);
            } else {
                searchSuggestList.toFront();

                searchSuggestList.setVisible(true);
                for (String s : Objects.requireNonNull(googleAPIFetching.suggestionSearching(searchKey))) {
                    searchSuggestList.getItems().add(s);
                }
            }
        }

        public void selectSuggestion(MouseEvent mouseEvent) {
            String searchKey = searchSuggestList.getSelectionModel().getSelectedItem();
            urlBox.setText(searchKey);
            goButtonPressed();
        }

        private void backHome() {
            System.out.println("Home button pressed.");
            urlBox.setText("");
            urlBox2.setText("");
            borderPane.setCenter(homePane);
            newTab.setText("New Tab");
            newTab.setGraphic(null);
            newsScrollPane.setVisible(true);
        }

        private void homeBtnHover() {
            homeLabel.setText("Home");
        }

        private void homeBtnHoverExit() {
            homeLabel.setText("");
        }

        public void goToURL(String urlStr) {
            myBrowser = new MyBrowser(urlStr);
            borderPane.setCenter(myBrowser);
        }


        public void setTabBackground(String imgURL) {
//            ImageView iv = new ImageView();
//            Image img = new Image(imageFileLocation);
//            iv.setImage(img);
            borderPane.setBackground(new Background(new BackgroundImage(new Image(imgURL, 1000, 1000, false, true),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(1.0, 1.0, true, true, false, false))));
        }

        public void setTabContent(MyBrowser passedBroser) {
            borderPane.setClip(passedBroser);
        }


        class MyBrowser extends Region {
            WebView browser = new WebView();
            final WebEngine webEngine = browser.getEngine();
            WebHistory history = webEngine.getHistory();

            public MyBrowser(String url) {
                //tell when page loading is complete
                webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {   //no need to use lambda expression
                    public void changed(ObservableValue ov, State oldState, State newState) {
                        ProgressIndicator progInd = new ProgressIndicator(-1.0);
                        progInd.setPrefHeight(17);
                        progInd.setPrefWidth(25);
                        newTab.setGraphic(progInd);

                        //make reload button -> stop loading button
                        ImageView iv = new ImageView();
                        Image img = new Image("file:src/images/close.png");
                        iv.setImage(img);
                        iv.setFitHeight(15);
                        iv.setFitWidth(15);
                        reloadButton.setGraphic(iv);
                        reloadButton.setOnAction((ActionEvent e) -> {
                            ImageView iv1 = new ImageView();
                            Image img1 = new Image("file:src/images/reload.png");
                            iv1.setImage(img1);
                            iv1.setFitHeight(18);
                            iv1.setFitWidth(18);
                            reloadButton.setGraphic(iv1);
                            myBrowser.closeWindow();
                            newTab.setText("Aborted!");
                            newTab.setGraphic(null);
                        });

                        if (newState == State.SUCCEEDED) {
                            newTab.setText(webEngine.getTitle());
                            urlBox.setText(webEngine.getLocation());
                            newTab.setGraphic(loadFavicon(url));
                            ImageView iv1 = new ImageView();
                            Image img1 = new Image("file:src/images/reload.png");
                            iv1.setImage(img1);
                            iv1.setFitHeight(18);
                            iv1.setFitWidth(18);
                            reloadButton.setGraphic(iv1);
                            reloadButton.setOnAction((ActionEvent e) -> {
                                myBrowser.reloadWebPage();
                            });

                            //String html = (String) webEngine.executeScript("document.documentElement.outerHTML");
                            //System.out.println(html);

                            //DOM access
                            EventListener listener = new EventListener() {
                                public void handleEvent(Event ev) {
                                    //Platform.exit();
                                    System.out.println("You pressed on a link");
                                }
                            };

                            Document doc = webEngine.getDocument();
                            NodeList el = doc.getElementsByTagName("a");
                            for (int i = 0; i < el.getLength(); i++) {
                                //((EventTarget) el.item(i)).addEventListener("click", (org.w3c.dom.events.EventListener) listener, true);
                                //System.out.println(el.item(i).getTextContent());
                            }
                        }
                    }
                });

                history.getEntries().addListener(new ListChangeListener<WebHistory.Entry>() {
                    @Override
                    public void onChanged(Change<? extends Entry> c) {
                        //System.out.println("Inside history code.");
                        c.next();
                        for (Entry e : c.getRemoved()) {
                            //System.out.println("Removing: " + e.getUrl());
                            historyMenu.getItems().remove(e.getUrl());
                        }
                        for (Entry e : c.getAddedSubList()) {
                            //System.out.println("Adding: " + e.getUrl());
                            MenuItem menuItem = new MenuItem(e.getUrl().replace(e.getUrl().substring(24), ""));
                            histObj.addHist(e.getUrl(), "hist.txt"); //save to local file
                            histItems.add(e.getUrl());
                            //historyList.setItems(histItems);
                            menuItem.setGraphic(loadFavicon(e.getUrl()));
                            //action if this item is clicked on
                            // day ne
                            menuItem.setOnAction((ActionEvent ev) -> {
                                NewTab aTab = new NewTab();
                                aTab.setTabBackground("file:src/images/background_main.png");
                                Tab tab = aTab.createTab();
                                aTab.goToURL(e.getUrl());
                                tabPane.getTabs().add(tab);
                                tabPane.getSelectionModel().select(tab); //take this tab to front
                                newTabBtnPosRight();
                            });
                            historyMenu.setText(LocalDate.now().toString());
                            historyMenu.getItems().add(menuItem);
                        }
                    }
                });
                //handle popup windows
                webEngine.setCreatePopupHandler((PopupFeatures config) -> {
                    browser.setFontScale(0.8);
                    if (!getChildren().contains(browser)) {
                        getChildren().add(browser);
                    }
                    return browser.getEngine();
                });

                final WebView smallView = new WebView();

                //disable default popup
                //browser.setContextMenuEnabled(false);
                //createContextMenu(browser);
                webEngine.load(url); // load the web page
                getChildren().add(browser); //add the web view to the scene
            }

            //pop up control
            private void createContextMenu(WebView webView) {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem reload = new MenuItem("Reload");
                reload.setOnAction(e -> webView.getEngine().reload());
                MenuItem savePage = new MenuItem("Save Page");
                savePage.setOnAction(e -> System.out.println("Save page..."));
                MenuItem openInNewWindow = new MenuItem("Open in New Window");
                openInNewWindow.setOnAction(e -> System.out.println("Open in New Window"));
                MenuItem openInNewTab = new MenuItem("Open in New Tab");
                openInNewTab.setOnAction(e -> System.out.println("Open in New Tab"));
                contextMenu.getItems().addAll(reload, savePage, openInNewWindow, openInNewTab);

                webView.setOnMousePressed(e -> {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        contextMenu.show(webView, e.getScreenX(), e.getScreenY());
                    } else {
                        contextMenu.hide();
                    }
                });
            }

            public void goBack() {
                final WebHistory history = webEngine.getHistory();
                ObservableList<WebHistory.Entry> entryList = history.getEntries();
                int currentIndex = history.getCurrentIndex();

                Platform.runLater(() -> {
                    history.go(entryList.size() > 1 && currentIndex > 0 ? -1 : 0);
                });
            }

            public void goForward() {
                final WebHistory history = webEngine.getHistory();
                ObservableList<WebHistory.Entry> entryList = history.getEntries();
                int currentIndex = history.getCurrentIndex();

                Platform.runLater(() -> {
                    history.go(entryList.size() > 1 && currentIndex < entryList.size() - 1 ? 1 : 0);
                });
            }

            public void closeWindow() {
                browser.getEngine().load(null);
                browser = null; //making the object available for garbage collection
            }

            public void reloadWebPage() {
                webEngine.reload();
            }

            @Override
            protected void layoutChildren() {
                double w = getWidth();
                double h = getHeight();
                layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
            }

            @Override
            protected double computePrefWidth(double height) {
                return 750;
            }

            @Override
            protected double computePrefHeight(double width) {
                return 500;
            }
        }
    }

    public boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ImageView loadFavicon(String location) {
        try {
            ImageView iv = new ImageView();
            Image img;
            String url = "https://www.google.com/s2/favicons?sz=64&domain_url=";
            if (!location.startsWith("http") && location.contains(".")) {
                location = "http://" + location;
            }
            url += location;
            if (isValidUrl(location)) {
                img = new Image(url);
            } else {
                img = new Image("https://www.google.com/s2/favicons?sz=64&domain_url=google.com");
            }
            iv.setImage(img);
            iv.setFitHeight(15);
            iv.setFitWidth(15);
            return iv;
        } catch (Exception ex) {
            throw new RuntimeException(ex); // not expected
        }
    }

}