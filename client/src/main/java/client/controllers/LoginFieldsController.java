package client.controllers;

import client.ConnectionProvider;
import client.CredentialsInputHandler;
import client.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import utility.Request;
import utility.RequestType;
import utility.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginFieldsController implements Initializable {
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
    private Button regLogButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private Button pressedButton;
    private ConnectionProvider connectionProvider;
    private CredentialsInputHandler credentialsInputHandler;
    private ResourceBundle resourceBundle;
    private Button[] langButtons;
    private String errorText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        langButtons = new Button[]{russianButton, germanButton, albanianButton, indianEnglishButton};
    }

    public void setRegLogButton(ActionEvent event) {
        pressedButton = (Button) event.getSource();
        if (pressedButton.getId().equals("loginButton")) {
            regLogButton.setOnAction(this::login);
            regLogButton.setText(resourceBundle.getString("Log in"));
        } else if (pressedButton.getId().equals("registerButton")) {
            regLogButton.setOnAction(this::register);
            regLogButton.setText(resourceBundle.getString("Register"));
        }
    }

    @FXML
    public void register(ActionEvent event) {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (!checkCredentialsLength(username, password)) return;

            connectionProvider = ConnectionProvider.getConnectionProvider();
            credentialsInputHandler = new CredentialsInputHandler();

            Session session;

            String userSalt = credentialsInputHandler.generateSalt();
            String encryptedPassword = credentialsInputHandler.encryptPassword(password, userSalt);

            connectionProvider.send(new Request(null, null, null,
                    username, encryptedPassword, userSalt, RequestType.REGISTER));
            Response registerResponse = connectionProvider.receive();

            if (registerResponse == null)
                throw new SocketTimeoutException();
            if (registerResponse.getExecCode() == 0) {
                session = new Session(username, encryptedPassword);

                setMainWindow(event, session);
            } else {
                errorText = "This user is already registered";
                setErrorLabel(resourceBundle.getString(errorText));
            }


        } catch (SocketTimeoutException e) {
            errorText = "The server is temporarily unavailable. Please try again later.";
            setErrorLabel(resourceBundle.getString(errorText));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void login(ActionEvent event) {
        try {

            String username = usernameField.getText();
            String password = passwordField.getText();

            if (!checkCredentialsLength(username, password)) return;

            connectionProvider = ConnectionProvider.getConnectionProvider();
            credentialsInputHandler = new CredentialsInputHandler();

            Session session;

            connectionProvider.send(new Request(null, null, null,
                    username, null, null, RequestType.GET_SALT));
            Response saltResponse = connectionProvider.receive();

            if (saltResponse == null)
                throw new SocketTimeoutException();
            if (saltResponse.getExecCode() == 1) {
                errorText = "Invalid username or password";
                setErrorLabel(resourceBundle.getString(errorText));
                return;
            }
            String userSalt = saltResponse.getBody().getText();
            String encryptedPassword = credentialsInputHandler.encryptPassword(password, userSalt);

            connectionProvider.send(new Request(null, null, null,
                    username, encryptedPassword, null, RequestType.LOGIN));
            Response loginResponse = connectionProvider.receive();

            if (loginResponse == null)
                throw new SocketTimeoutException();
            if (loginResponse.getExecCode() == 0) {
                session = new Session(username, encryptedPassword);

                setMainWindow(event, session);
            } else {
                errorText = "Invalid username or password";
                setErrorLabel(resourceBundle.getString(errorText));
            }

        } catch (SocketTimeoutException e) {
            errorText = "The server is temporarily unavailable. Please try again later.";
            setErrorLabel(resourceBundle.getString(errorText));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setErrorLabel(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private MainWindowController loadMainWindow(ActionEvent event) throws IOException {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.controllers/fxml/mainWindow.fxml"), resourceBundle);
        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);
        Image icon = new Image(getClass().getResourceAsStream("/client.controllers/images/rubberduck1.png"));

        Stage stage = new Stage();
        stage.getIcons().add(icon);
        stage.setTitle("Program 42");

        String resource = getClass().getResource("/client.controllers/styles/app.css").toExternalForm();
        scene.getStylesheets().add(resource);
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        scene.setOnKeyPressed((e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.setFullScreen(false);
            } else if (e.getCode() == KeyCode.F11) {
                stage.setFullScreen(true);
            }
        }));
        stage.setOnCloseRequest(e -> {
            stage.close();
            System.exit(0);
        });

        stage.show();

        return loader.getController();

    }

    private void setMainWindow(ActionEvent event, Session session) throws IOException {
        MainWindowController controller = loadMainWindow(event);
        controller.setSession(session);
        controller.setActiveLangButton(getActiveLangButton());
        controller.changeLanguage();
        controller.runUpdatingThread(session);
    }

    private boolean checkCredentialsLength(String username, String password) {
        if (username.length() < 3) {
            errorText = "The username is too short";
            setErrorLabel(resourceBundle.getString("The username is too short"));
            return false;
        } else if (password.length() < 6) {
            errorText = "The password is too short";
            setErrorLabel(resourceBundle.getString(errorText));
            return false;
        }
        return true;
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

    public void setActiveLangButton(Button button) {
        for (Button b: langButtons) {
            if (b.getId().equals(button.getId())) b.getStyleClass().add("activeLangButton");
        }
    }

    public Button getActiveLangButton() {
        for (Button button: langButtons) {
            if (button.getStyleClass().contains("activeLangButton")) return button;
        }
        return null;
    }

    public void changeLanguage() {
        setResourceBundle();
        labStorageLabel.setText(resourceBundle.getString("Lab repository"));
        usernameField.setPromptText(resourceBundle.getString("Username"));
        passwordField.setPromptText(resourceBundle.getString("Password"));
        if (pressedButton.getId().equals("loginButton")) regLogButton.setText(resourceBundle.getString("Log in"));
        else regLogButton.setText(resourceBundle.getString("Register"));
        if (errorLabel.isVisible()) errorLabel.setText(resourceBundle.getString(errorText));
    }

    private void setResourceBundle() {
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

}
