package Messenger;

import GlobalVariables.Variables;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MessengerController implements Initializable {
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label nameInfoLabel;
    @FXML
    private Button dehaze_button;
    @FXML
    private Button infoMenuButton;
    @FXML
    private Button settingMenuButton;
    @FXML
    private Button contactsMenuButton;
    @FXML
    private Button logoutMenuButton;
    @FXML
    public VBox vbSidebarMain;
    @FXML
    private VBox menuSidebar;
    @FXML
    private VBox shadowBox;
    @FXML
    private VBox chatLogs;
    @FXML
    private HBox avaCircle;
    @FXML
    public AnchorPane pnlRoot;
    @FXML
    private TextField messageField;
    public static ListView<CustomMessage> listView;
    public ObservableList<CustomMessage> messages;
    private FadeTransition ft;
    private TranslateTransition openNav;
    private TranslateTransition closeNav;
    private TranslateTransition closeFastNav;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        openNav = new TranslateTransition(Duration.millis(100), menuSidebar);
        openNav.setToX(menuSidebar.getTranslateX()-menuSidebar.getWidth());

        closeNav = new TranslateTransition(Duration.millis(100), menuSidebar);
        closeFastNav = new TranslateTransition(Duration.millis(.1), menuSidebar);

        shadowBox.setDisable(true);

        ft = new FadeTransition(Duration.millis(100), shadowBox);
        ft.setFromValue(0.6);
        ft.setToValue(0.0);
        ft.play();

        dehaze_button.getStyleClass().add("dehaze");
        dehaze_button.setPickOnBounds(true);
        Region icon = new Region();
        icon.getStyleClass().add("icon");
        dehaze_button.setGraphic(icon);

        dehaze_button.setOnAction((ActionEvent evt) -> {
            sideBarShow();
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                closeFastNav.setToX(-(menuSidebar.getWidth()));
                closeFastNav.play();
                menuSidebar.setVisible(false);
            }
        });

        avaCircle.getChildren().add(circleAva("/resources/images/large.jpg"));
        avaCircle.setPadding(new Insets(10,0,0,25));

        nicknameLabel.setText(Variables.user);
        nicknameLabel.setTranslateX(25);
        nicknameLabel.setTranslateY(20);

        nameInfoLabel.setText(Variables.email);
        nameInfoLabel.setTranslateX(25);
        nameInfoLabel.setTranslateY(24);

        setMenuButton("Settings","settings-icon", settingMenuButton);
        setMenuButton("Info","info-icon", infoMenuButton);
        setMenuButton("Contacts", "edit-icon", contactsMenuButton);
        setMenuButton("Logout", "logout-icon", logoutMenuButton);

        messages = FXCollections.observableArrayList();

        listView = new ListView<CustomMessage>(messages);

        listView.setCellFactory(new Callback<ListView<CustomMessage>, ListCell<CustomMessage>>() {
            @Override
            public ListCell<CustomMessage> call(ListView<CustomMessage> listView) {
                return new MessageListCell();
            }
        });

        listView.getStyleClass().add("chat-list");

        chatLogs.getChildren().addAll(listView);

        VBox.setVgrow(listView, Priority.ALWAYS);

        messageField = new TextField();
        messageField.setPromptText("Write something...");
        messageField.getStyleClass().add("write-message-field");

        Button sendButton = new Button();
        sendButton.getStyleClass().add("write-icon-button");
        sendButton.setPickOnBounds(true);
        Region icon_region = new Region();
        icon_region.getStyleClass().add("send-icon");
        sendButton.setGraphic(icon_region);

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!messageField.getText().isEmpty()) {
                    messages.add(new CustomMessage(Variables.user, messageField.getText()));
                    messageField.setText("");
                }
            }
        });

        HBox writeFieldBox = new HBox(messageField, sendButton);
        HBox.setHgrow(messageField,Priority.ALWAYS);
        writeFieldBox.getStyleClass().add("write-box");
        writeFieldBox.prefHeight(50);

        writeFieldBox.setAlignment(Pos.CENTER_LEFT);

        chatLogs.getChildren().add(writeFieldBox);

        closeNav.statusProperty().addListener(new ChangeListener<Animation.Status>() {
            @Override
            public void changed(ObservableValue<? extends Animation.Status> observableValue,
                                Animation.Status oldValue, Animation.Status newValue) {
                if(newValue == Animation.Status.STOPPED){
                    if(closeNav.getToX() == -(menuSidebar.getWidth())) {
                        menuSidebar.setVisible(false);
                        shadowBox.setDisable(true);
                    }
                }
            }
        });


        logoutMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();
                loadLogin();
            }
        });
    }

    private void loadLogin() {
        try {
            Stage stage = new Stage();
            Pane pane = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
            stage.setScene(new Scene(pane));
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class CustomMessage {
        private String name;
        private String message;

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }

        public CustomMessage(String name, String message) {
            super();
            this.name = name;
            this.message = message;
        }
    }

    private class MessageListCell extends ListCell<CustomMessage> {
        private HBox content;
        private Text name;
        private Text message;
        private Label messageLabel;

        public MessageListCell() {
            super();

            name = new Text();
            message = new Text();

            messageLabel = new Label();
            messageLabel.setWrapText(true);
            messageLabel.setTextAlignment(TextAlignment.JUSTIFY);
            messageLabel.maxWidthProperty().bind(listView.widthProperty().subtract(130));

            VBox textBox = new VBox(name, messageLabel);
            textBox.getStyleClass().add("message-box");
            textBox.setPadding(new Insets(10,10,10,10));
            content = new HBox(circleAva("/resources/images/large.jpg"), textBox);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(CustomMessage item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null && !empty) {
                name.setText(item.getName());
                message.setText(item.getMessage());
                messageLabel.setText(item.getMessage());
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }

    }

    private Circle circleAva(String url) {
        Circle ava = new Circle(50,50,25);
        ava.setStroke(Color.WHITE);
        try {
            Image im = new Image(url, false);
            ava.setFill(new ImagePattern(im));
        } catch (Exception e){
            e.printStackTrace();
            Image na = new Image("/resources/images/noavatar.png", false);
            ava.setFill(new ImagePattern(na));
        }
        return ava;
    }

    private void setMenuButton(String text, String icon, Button button) {
        button.getStyleClass().add("sidebar-icon-button");
        button.setPickOnBounds(true);
        Region icon_region = new Region();
        icon_region.getStyleClass().add(icon);
        button.setGraphic(icon_region);
        button.setText(text);
    }

    @FXML
    public void sideBarHide() {
        ft.setFromValue(0.6);
        ft.setToValue(0.0);
        ft.play();

        dehaze_button.getStyleClass().remove("sidebar-button-active");
        dehaze_button.getStyleClass().add("sidebar-button");

        closeNav.setToX(-(menuSidebar.getWidth()));
        closeNav.play();
    }

    @FXML
    public void sideBarShow() {
        if ((menuSidebar.getTranslateX()) == -(menuSidebar.getWidth()) ) {
            shadowBox.setDisable(false);
            menuSidebar.setVisible(true);

            ft.setFromValue(0.0);
            ft.setToValue(0.6);
            ft.play();

            dehaze_button.getStyleClass().remove("sidebar-button");
            dehaze_button.getStyleClass().add("sidebar-button-active");

            openNav.play();
        } else {
            sideBarHide();
        }
    }

}
