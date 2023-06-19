package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InfoWindowController {
    @FXML
    private Label infoLabel;
    @FXML
    private Text infoText;
    @FXML
    private Button OKButton;

    public void setLabel(String text) {
        infoLabel.setText(text);
    }

    public void setText(String text) {
        infoText.setText(text);
    }

    @FXML
    public void closeWindow(ActionEvent event) {
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).close();
    }
}
