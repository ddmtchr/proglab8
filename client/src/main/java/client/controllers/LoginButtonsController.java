package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginButtonsController implements Initializable {
    @FXML
    private Button russianButton;
    @FXML
    private Button germanButton;
    @FXML
    private Button albanianButton;
    @FXML
    private Button indianEnglishButton;
    @FXML
    private Label labStorageLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    private ResourceBundle resourceBundle;
    private Button[] langButtons;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setResourceBundle();
        changeLanguage();
        langButtons = new Button[]{russianButton, germanButton, albanianButton, indianEnglishButton};
    }

    @FXML
    public void signUp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.controllers/fxml/loginFields.fxml"), resourceBundle);
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        LoginFieldsController controller = loader.getController();
        controller.setRegLogButton(event);
        controller.setActiveLangButton(getActiveLangButton());
        controller.changeLanguage();
        scene.getStylesheets().add(getClass().getResource("/client.controllers/styles/app.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void setRussian(ActionEvent event) {
        russianButton.getStyleClass().add("activeLangButton");
        germanButton.getStyleClass().remove("activeLangButton");
        albanianButton.getStyleClass().remove("activeLangButton");
        indianEnglishButton.getStyleClass().remove("activeLangButton");
        changeLanguage();
    }

    @FXML
    public void setGerman(ActionEvent event) {
        russianButton.getStyleClass().remove("activeLangButton");
        germanButton.getStyleClass().add("activeLangButton");
        albanianButton.getStyleClass().remove("activeLangButton");
        indianEnglishButton.getStyleClass().remove("activeLangButton");
        changeLanguage();
    }

    @FXML
    public void setAlbanian(ActionEvent event) {
        russianButton.getStyleClass().remove("activeLangButton");
        germanButton.getStyleClass().remove("activeLangButton");
        albanianButton.getStyleClass().add("activeLangButton");
        indianEnglishButton.getStyleClass().remove("activeLangButton");
        changeLanguage();
    }

    @FXML
    public void setIndianEnglish(ActionEvent event) {
        russianButton.getStyleClass().remove("activeLangButton");
        germanButton.getStyleClass().remove("activeLangButton");
        albanianButton.getStyleClass().remove("activeLangButton");
        indianEnglishButton.getStyleClass().add("activeLangButton");
        changeLanguage();
    }

    private void changeLanguage() {
        setResourceBundle();
        labStorageLabel.setText(resourceBundle.getString("Lab repository"));
        loginButton.setText(resourceBundle.getString("Log in"));
        registerButton.setText(resourceBundle.getString("Register"));
    }

    public void setResourceBundle() {
        if (russianButton.getStyleClass().contains("activeLangButton")) {
            this.resourceBundle = ResourceBundle.getBundle("client.localization.Locale", new Locale("ru", "RU"));
        }
        if (germanButton.getStyleClass().contains("activeLangButton")) {
            this.resourceBundle = ResourceBundle.getBundle("client.localization.Locale", new Locale("de", "DE"));
        }
        if (albanianButton.getStyleClass().contains("activeLangButton")) {
            this.resourceBundle = ResourceBundle.getBundle("client.localization.Locale", new Locale("sq", "AL"));
        }
        if (indianEnglishButton.getStyleClass().contains("activeLangButton")) {
            this.resourceBundle = ResourceBundle.getBundle("client.localization.Locale", new Locale("en", "IN"));
        }
    }

    public Button getActiveLangButton() {
        for (Button button: langButtons) {
            if (button.getStyleClass().contains("activeLangButton")) return button;
        }
        return null;
    }
}