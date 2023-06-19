package client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EnterIdController implements Initializable {
    @FXML
    private Label enterIDLabel;
    @FXML
    private Button enterIdButton;
    @FXML
    private TextField idField;
    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        enterIDLabel.setText(resourceBundle.getString("Enter ID"));
        enterIdButton.setText(resourceBundle.getString("Ok"));
    }

    public Button getButton() {
        return enterIdButton;
    }

    public TextField getIdField() {
        return idField;
    }

}
