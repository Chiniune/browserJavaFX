package com.example.alsbrowser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.example.alsbrowser.model.AccountModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker.State;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class MainController implements Initializable {

    @FXML
    protected TabPane tabPane;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    MenuItem homePageBackgroundImg;
    @FXML
    protected Menu historyMenu = new Menu(); //no need to make final

    @FXML
    private Circle accountIcon;
    @FXML
    private Circle accountIcon2;
    @FXML
    AnchorPane chooseAccountPane;
    @FXML
    VBox vBoxAccountList;
    Boolean accountBoolean = true ;

    //url shortcut list
    ArrayList<String> urlList = new ArrayList<String>();
    // shortcut list
    @FXML
    private VBox vBoxShortcut;
    @FXML
    private Button searchBtn;

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
        aTab.setTabBackground("file:src/images/background_main.jpg");
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


//    download ----------------------------------------------------------------------------------------------

    @FXML
    public static Label downloadStatusLabel;
    @FXML
    private AnchorPane downloadAnchorPane;

    //create content for downloads
    private Parent createContent() {
        VBox root = new VBox();
        root.setPrefSize(300, 400);

        TextField fieldURL = new TextField();
        fieldURL.setPromptText("⚭ enter download link here");
        root.getChildren().addAll(fieldURL);

        fieldURL.setOnAction(event -> {
            downloadStatusLabel.setText("Downloading...");
            Task<Void> task = new DownloadTask(fieldURL.getText());
            ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefWidth(350);
            progressBar.progressProperty().bind(task.progressProperty());
            root.getChildren().add(progressBar);

            fieldURL.clear();

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });

        return root;
    }

    @FXML
    private Button downloadButton;
    @FXML
    private Label downloadLabel;

    @FXML
    private void downloadBtnHover() {
        downloadLabel.setText("Downloads");
    }

    @FXML
    private void downloadBtnHoverExit() {
        downloadLabel.setText("");
    }

    //download menu options
    int k = 0;

    @FXML
    private void downloadButtonFunction() {
        k++;
        if (k % 2 == 0) {
            System.out.println("Download menu is now hidden.");
            downloadAnchorPane.setVisible(false);
            downloadStatusLabel.setText("");
        } else {
            downloadAnchorPane.setVisible(true);
            System.out.println("Download menu is now visible.");
            downloadAnchorPane.setStyle("-fx-background-color: gray;");
        }
    }
//    end download ----------------------------------------------------------------------------------------------

    //    bookmark ----------------------------------------------------------------------------------------------
    @FXML
    private Button bookmarkButton;
    @FXML
    private Label bookmarkLabel;

    @FXML
    private void bookmarkBtnHover() {
        bookmarkLabel.setText("Bookmarks");
    }

    @FXML
    private void bookmarkBtnHoverExit() {
        bookmarkLabel.setText("");
    }

    @FXML
    private void bookmarkButtonFunction() {
        System.out.println("Bookmark button pressed.");
    }

//  end bookmark  ----------------------------------------------------------------------------------------------

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

    Boolean countHisIconClick = true;

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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aTab.setTabBackground("file:src/images/background_main.jpg");
        Tab tab = aTab.createTab();
        tab.setText("New tab");
        tabPane.getTabs().add(tab);


//        ImageView iv2 = new ImageView();
//        Image img2 = new Image("file:src/images/downloadIcon.png");
//        iv2.setImage(img2);
//        iv2.setFitHeight(21);
//        iv2.setFitWidth(20);
//        downloadButton.setGraphic(iv2);


        // new button "+"
        ImageView iv3 = new ImageView();
        Image img3 = new Image("file:src/images/plus.png");
        iv3.setImage(img3);
        iv3.setFitHeight(12);
        iv3.setFitWidth(12);
        newTabBtn.setGraphic(iv3);

        // button search on anchor
        ImageView ivBtnSearch = new ImageView();
        Image imgBtnSearch= new Image("file:src/images/search.png");
        ivBtnSearch.setImage(imgBtnSearch);
        ivBtnSearch.setFitHeight(16);
        ivBtnSearch.setFitWidth(16);
        searchBtn.setGraphic(ivBtnSearch);

        //start account modify
        Image img = new Image("file:src/images/Jimin.png", false);
        accountIcon.setFill(new ImagePattern(img));
        accountIcon.focusedProperty().addListener(observable -> {
            System.out.println(observable);
        });
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
        urlList.add("facebook.com");
        urlList.add("youtube.com");
        urlList.add("messenger.com");
        urlList.add("zalo.me");
        addUrlShortcut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onClickAddShortcutUrl();
            }
        });

        vBoxShortcut.getChildren().addAll(addUrlShortcut);
        showListShortUrl();


//        accountIcon.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN ));


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
                            aTab.setTabBackground("file:src/images/background_main.jpg");
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

        Parent parent = createContent();
        downloadAnchorPane.getChildren().add(parent);
        downloadAnchorPane.setVisible(false);
        historyAnchorPane.setVisible(false);  //set history Anchor pane invisible
        chooseAccountPane.setVisible(false);  //set choose account Anchor pane invisible

        // check mouse event outside listview
//        while (true) {
//            searchSuggestList.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//                if (!isNowFocused) {
//                    searchAnchor.setVisible(false);
//                }
//            });
//        }

    }

    private void showSearchSuggestList() {
        ObservableList list = FXCollections.observableArrayList();
        searchSuggestList.getItems().clear();
        list.removeAll(list);

        String s1 = "he luuu";
        String s2 = "chao mung kt";
        String s3 = "den voi";
        String s4 = "listview cua vt";
        list.addAll(s1,s2,s3,s4);

        list.forEach(item->{
            HBox hBox = new HBox();

            ImageView iv = new ImageView();
            Image img = new Image("file:src/images/search.png");
            iv.setImage(img);
            iv.setFitHeight(15);
            iv.setFitWidth(15);
            Label lblIcon = new Label();
            lblIcon.setGraphic(iv);

            Label lblText = new Label(item.toString());
            hBox.getChildren().addAll(lblIcon, lblText);
            hBox.setSpacing(15);
            searchSuggestList.getItems().add(hBox);
        });
    }

    public void showListShortUrl() {

        urlList.forEach((url) -> {
            setShortcutIconInList(url);
        });
    }

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
            if (dialogButton == addButtonType){
                return new String(txtUrl.getText());
            }
            return null;
        } );

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(url -> {
            System.out.println("url added: " + url);
            urlList.add(url);
            setShortcutIconInList(url);
        });
    }

    public TextField urlShortcut = new TextField();
    private void setShortcutIconInList(String url) {
        Button btn = new Button();
        btn.getStyleClass().add("background-hover");
        btn.setId(String.valueOf(urlList.indexOf(url)));
        btn.getStyleClass().add("background-transparent");
        ImageView iv = loadFavicon(url);
//        Image img;
//        if (url.startsWith("https://") || url.startsWith("http://") || url.contains(".")) {
//            img = new Image("https://www.google.com/s2/favicons?sz=64&domain_url=" + url);
//        } else {
//            img = new Image("https://www.google.com/s2/favicons?sz=64&domain_url=google.com");
//        }
//        iv.setImage(img);
        iv.setFitHeight(21);
        iv.setFitWidth(21);
        btn.setGraphic(iv);
        vBoxShortcut.getChildren().add(btn);
        btn.setOnMouseClicked(e -> {
            urlShortcut.setText(url);
        });
    }
    public void loadAccountList(){
        AccountModel acc1 = new AccountModel(1, "Jimin","smile.png" );
//        AccountModel acc2 = new AccountModel(2, "Jin ne","smile.png" );
        AccountModel acc3 = new AccountModel(2, "Vo Thi Trinh","icon256.png" );
        ObservableList<AccountModel> list = FXCollections.observableArrayList();
        list.removeAll(list);
        list.addAll(acc1,acc3);

        AtomicInteger count = new AtomicInteger(0);
        list.forEach((acc) ->{
            Circle avt = new Circle();
            Image iv = new Image("file:src/images/"+acc.getAvt());
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
        Label textAdd = new Label("Add Account");
        hBox.getChildren().addAll(addAccount, textAdd);
        vBoxAccountList.getChildren().add(hBox);

    }

    @FXML public void accountIconFunc(){
        if (accountBoolean) {
            chooseAccountPane.setVisible(true);
            accountBoolean = false;
        } else {
            chooseAccountPane.setVisible(false);
            accountBoolean = true;
        }
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


    public boolean isValidUrl(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    public ImageView loadFavicon(String location) {
        try {
            ImageView iv = new ImageView();
            Image img;
            String url = "https://www.google.com/s2/favicons?sz=64&domain_url=";
//            System.out.println();
            if(!location.startsWith("http") && location.contains(".")) {
                location = "http://" + location;
            }
            url += location;
            if(isValidUrl(location)) {
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

    @FXML
    private Label homeLabel;
    @FXML
    private Label reloadLabel;
    @FXML
    private Label backLabel;
    @FXML
    private Label forwardLabel;
    @FXML
    private Button settingIcon;
    @FXML
    Button historyBtn;
    @FXML
    private AnchorPane searchAnchor;
    @FXML
    private ListView searchSuggestList;
    @FXML
    private TextField txtTypeUrlOnAnchor;


    class NewTab {
        //properties

        private final Tab newTab;
        private final AnchorPane smallAnchor;
//        private final VBox vBox;
        private final ToolBar toolBar;
        private final Label label;
        private final MenuBar menuBar;
        private final Menu bookmarksMenu, settingsMenu, helpMenu;
        private final HBox hBox;
        private final TextField urlBox;
        private final Button goButton;
        private final Button backButton;
        private final Button forwardButton;
        private final Button reloadButton;
        private final Button homeBtn;

        private final BorderPane borderPane;
        private MyBrowser myBrowser;

        //methods
        public NewTab() {
            newTab = new Tab();
            smallAnchor = new AnchorPane();
            toolBar = new ToolBar();
            label = new Label();
            menuBar = new MenuBar();
            bookmarksMenu = new Menu();
            settingsMenu = new Menu();
            helpMenu = new Menu();
            hBox = new HBox();
            urlBox = new TextField();
            goButton = new Button();
            backButton = new Button();
            forwardButton = new Button();
            reloadButton = new Button();
            homeBtn = new Button();
            borderPane = new BorderPane();
//            vBox = new VBox();
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
            historyBtn.getStyleClass().add("background-transparent");
            historyBtn.getStyleClass().add("background-hover");
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
            Image img8 = new Image("file:src/images/search.png");
            iv8.setImage(img8);
            iv8.setFitHeight(20);
            iv8.setFitWidth(20);
            goButton.setGraphic(iv8);
            goButton.getStyleClass().addAll("background-transparent","background-hover-circle");
//            goButton.setPrefHeight(30);
//            goButton.setPrefWidth(32);

            hBox.getStyleClass().add("hBox-search");
            hBox.getChildren().addAll(urlBox, goButton);
            hBox.setSpacing(5.0);
            AnchorPane.setTopAnchor(hBox, 4.0);
            AnchorPane.setLeftAnchor(hBox, 155.0);
            smallAnchor.getChildren().add(hBox);

            //label.setText("Developed by Arif Mahmud");
            AnchorPane.setTopAnchor(label, 10.0);
            AnchorPane.setLeftAnchor(label, 520.0);
            smallAnchor.getChildren().add(label);


//            vBox.getChildren().add(addUrlShortcut);
//            showListShortUrl();
//            borderPane.setLeft(vBox);
//            vBox.getStyleClass().add("vBox");

            AnchorPane.setTopAnchor(borderPane, 40.0);
            AnchorPane.setBottomAnchor(borderPane, 0.0);
            AnchorPane.setLeftAnchor(borderPane, 48.0);
            AnchorPane.setRightAnchor(borderPane, 0.0);
            smallAnchor.getChildren().add(borderPane);

            newTab.setContent(smallAnchor);
            newTab.setOnClosed((Event arg) -> {
                System.out.println("A tab closed.");
                newTabBtnPosLeft();
                if(myBrowser != null) {
                    myBrowser.closeWindow();
                }
            });

            backButton.setOnMouseClicked((MouseEvent me) -> {
                System.out.println("Back button has been pressed.");
                myBrowser.goBack();
                label.setText("");
            });
            backButton.setOnMouseEntered(e->{
                backLabel.setVisible(true);
            });
            backButton.setOnMouseExited(e->{
                backLabel.setVisible(false);
            });

            forwardButton.setOnMouseClicked((MouseEvent me) -> {
                System.out.println("Forward button has been pressed.");
                myBrowser.goForward();
                label.setText("");
            });
            forwardButton.setOnMouseEntered(e->{
                forwardLabel.setVisible(true);
            });
            forwardButton.setOnMouseExited(e->{
                forwardLabel.setVisible(false);
            });

            AnchorPane.setTopAnchor(tabPane, 0.0);
            AnchorPane.setBottomAnchor(tabPane, 0.0);
            AnchorPane.setLeftAnchor(tabPane, 0.0);
            AnchorPane.setRightAnchor(tabPane, 0.0);


            goButton.setOnAction((ActionEvent e) -> {
                goButtonPressed();
            });
            searchBtn.setOnAction((ActionEvent e) ->{
                urlBox.setText(txtTypeUrlOnAnchor.getText());
                goButtonPressed();
                searchAnchor.setVisible(true);
            });

            reloadButton.setOnAction((ActionEvent e) -> {
                myBrowser.reloadWebPage();
            });
            reloadButton.setOnMouseEntered(e->{
                reloadLabel.setVisible(true);
            });
            reloadButton.setOnMouseExited(e->{
                reloadLabel.setVisible(false);
            });

            urlBox.setOnAction((ActionEvent e) -> {
                goButtonPressed();
            });
            txtTypeUrlOnAnchor.setOnAction(e->{
                urlBox.setText(txtTypeUrlOnAnchor.getText());
                goButtonPressed();
                searchAnchor.setVisible(false);
            });

            urlBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
                txtTypeUrlOnAnchor.setText(urlBox.getText());
                searchAnchor.setVisible(true);
                txtTypeUrlOnAnchor.requestFocus();
//                txtTypeUrlOnAnchor.setText(urlBox.getText());
            });

            txtTypeUrlOnAnchor.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
//                searchAnchor.setVisible(false);
                txtTypeUrlOnAnchor.textProperty().addListener((observable, oldValue, newValue) -> {
                    searchAnchor.setVisible(!txtTypeUrlOnAnchor.getText().trim().isEmpty());
                    showSearchSuggestList();
                });
                if(aBoolean) {
                    searchAnchor.setVisible(false);
                    urlBox.setText(txtTypeUrlOnAnchor.getText());
                }
            });

            urlShortcut.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newTab.isSelected()){
                    urlBox.setText(urlShortcut.getText());
                    goButtonPressed();
                }

            });

            homeBtn.setOnAction(e->{
                backHome();
            });
            homeBtn.setOnMouseEntered(e->{
                homeLabel.setVisible(true);
            });
            homeBtn.setOnMouseExited(e->{
                homeLabel.setVisible(false);
            });

            return newTab;
        }

//        url shortcut apply for all tab

        private void backHome() {
            System.out.println("Home button pressed.");

            borderPane.setCenter(null);
            urlBox.setText("");
            newTab.setText("New Tab");
            newTab.setGraphic(null);
//        aTab = new NewTab();
//        aTab.setTabBackground("file:Resources/background_main.jpg");
//        Tab tab = aTab.createTab();
//        tab.setText("Home Tab");
//        tabPane.getTabs().add(tab);
//        tabPane.getSelectionModel().select(tab);
//        newTabBtnPosRight();
        }
        public void goToURL(String urlStr) {
            myBrowser = new MyBrowser(urlStr);
            borderPane.setCenter(myBrowser);
        }

        public void goButtonPressed() {
            label.setText("");
            String urlStr;
            if (urlBox.getText() != null && !urlBox.getText().isEmpty()) {
                if (!urlBox.getText().contains(".")) {
                    //formating user input url to search engine sepecific url
                    srcEng.setUrlStr(urlBox.getText());
                    urlStr = srcEng.getEngineSpecificUrl();
                } else if (!urlBox.getText().startsWith("http://")) {
                    urlStr = "http://" + urlBox.getText();
                } else if (!urlBox.getText().startsWith("http://www.")) {
                    urlStr = "http://www." + urlBox.getText();
                } else {
                    urlStr = urlBox.getText();
                }

                myBrowser = new MyBrowser(urlStr);
                borderPane.setCenter(myBrowser);
            } else {
                label.setText("You didn't enter anything : (");
            }
        }

        public void setTabBackground(String imgURL) {
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
                            label.setText("You have aborted loading the page.");
                            newTab.setGraphic(null);
                        });

                        if (newState == State.SUCCEEDED) {
                            label.setText("");
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
                    public void onChanged(ListChangeListener.Change<? extends Entry> c) {
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
                            menuItem.setOnAction((ActionEvent ev) -> {
                                NewTab aTab = new NewTab();
                                aTab.setTabBackground("file:src/images/background_main.jpg");
                                aTab.goToURL(e.getUrl());
                                Tab tab = aTab.createTab();
                                tab.setStyle("-fx-background-color: #d9afac;");
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



}