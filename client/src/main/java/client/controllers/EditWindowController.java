package client.controllers;

import client.ConnectionProvider;
import client.Session;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import stored.Coordinates;
import stored.Difficulty;
import stored.Discipline;
import stored.LabWork;
import utility.LabWorkStatic;
import utility.Request;
import utility.RequestType;
import utility.Response;
import validation.LabWorkValidator;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class EditWindowController implements Initializable {
    @FXML
    private Label mainLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField XField;
    @FXML
    private TextField YField;
    @FXML
    private TextField minimalPointField;
    @FXML
    private TextField averagePointField;
    @FXML
    private ChoiceBox<String> difficultyMenu;
    @FXML
    private TextField disciplineNameField;
    @FXML
    private TextField lectureHoursField;
    @FXML
    private TextField practiceHoursField;
    @FXML
    private TextField selfStudyHoursField;
    @FXML
    private Label labelAveragePoint;
    @FXML
    private Label labelDifficulty;
    @FXML
    private Label labelDisciplineName;
    @FXML
    private Label labelLectureHours;
    @FXML
    private Label labelMinPoint;
    @FXML
    private Label labelName;
    @FXML
    private Label labelPracticeHours;
    @FXML
    private Label labelSelfStudyHours;
    @FXML
    private Label labelX;
    @FXML
    private Label labelY;
    @FXML
    private Label messageLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    private long editableId;
    private ConnectionProvider connectionProvider;
    private Session session;
    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.resourceBundle = resourceBundle;
            difficultyMenu.getItems().addAll("VERY_EASY", "NORMAL", "IMPOSSIBLE", "TERRIBLE");
            initializeFieldFormatters();
            connectionProvider = ConnectionProvider.getConnectionProvider();

            labelName.setText(resourceBundle.getString("Name (not empty):"));
            labelX.setText(resourceBundle.getString("X Coordinate (greater than -934):"));
            labelY.setText(resourceBundle.getString("Y Coordinate (not greater than 946):"));
            labelMinPoint.setText(resourceBundle.getString("Minimum point (greater than 0):"));
            labelAveragePoint.setText(resourceBundle.getString("Average point (greater than 0):"));
            labelDifficulty.setText(resourceBundle.getString("Difficulty:"));
            labelDisciplineName.setText(resourceBundle.getString("Discipline Name (empty if not filled):"));
            labelLectureHours.setText(resourceBundle.getString("Lecture Hours:"));
            labelPracticeHours.setText(resourceBundle.getString("Practice Hours:"));
            labelSelfStudyHours.setText(resourceBundle.getString("Self-study Hours:"));

            saveButton.setText(resourceBundle.getString("Save"));
            cancelButton.setText(resourceBundle.getString("Cancel"));


        } catch (IOException e) {
            System.out.println("Ошибка установления соединения с сервером");
        }
    }

    private void initializeFieldFormatters() {
        Queue<TextFormatter<String>> wholeNumberFormatters = new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            TextFormatter<String> wholeNumbersFormatter = Util.getWholeNumbersFormatter();
            wholeNumberFormatters.add(wholeNumbersFormatter);
        }
        TextFormatter<String> fracNumbersFormatter = Util.getFracNumbersFormatter();

        XField.setTextFormatter(wholeNumberFormatters.poll());
        YField.setTextFormatter(fracNumbersFormatter);
        minimalPointField.setTextFormatter(wholeNumberFormatters.poll());
        averagePointField.setTextFormatter(wholeNumberFormatters.poll());
        lectureHoursField.setTextFormatter(wholeNumberFormatters.poll());
        practiceHoursField.setTextFormatter(wholeNumberFormatters.poll());
        selfStudyHoursField.setTextFormatter(wholeNumberFormatters.poll());
    }

    public void fillFields(LabWork lw) {
        nameField.setText(lw.getName());
        XField.setText(String.valueOf(lw.getCoordinates().getX()));
        YField.setText(String.valueOf(lw.getCoordinates().getY()));
        minimalPointField.setText(String.valueOf(lw.getMinimalPoint()));
        averagePointField.setText(String.valueOf(lw.getAveragePoint()));
        difficultyMenu.setValue(lw.getDifficulty().name());
        if (lw.getDiscipline() != null) {
            disciplineNameField.setText(lw.getDiscipline().getName());
            lectureHoursField.setText(String.valueOf(lw.getDiscipline().getLectureHours()));
            practiceHoursField.setText(String.valueOf(lw.getDiscipline().getPracticeHours()));
            selfStudyHoursField.setText(String.valueOf(lw.getDiscipline().getSelfStudyHours()));
        }
    }

    public void saveAdd(ActionEvent event) {

        try {
            LabWorkStatic lws = getLabWorkFromFields();
            if (LabWorkValidator.isValid(lws)) {
                connectionProvider.send(new Request("add", " ", lws,
                        session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
                Response response = connectionProvider.receive();
                if (response.getExecCode() == 0) showSuccessMessage(response.getBody().getText());
                else showErrorMessage(response.getBody().getText());
            } else {
                showErrorMessage(resourceBundle.getString("Invalid values entered"));
            }
        } catch (SocketTimeoutException e) {
            processServerUnavailability();
        } catch (NumberFormatException e) {
            showErrorMessage(resourceBundle.getString("Not all required fields are filled"));
        }
    }

    public void saveAddIfMin(ActionEvent event) {
        try {
            LabWorkStatic lws = getLabWorkFromFields();
            if (LabWorkValidator.isValid(lws)) {
                connectionProvider.send(new Request("add_if_min", " ", lws,
                        session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
                Response response = connectionProvider.receive();
                if (response.getExecCode() == 0) showSuccessMessage(response.getBody().getText());
                else showErrorMessage(response.getBody().getText());
            } else {
                showErrorMessage(resourceBundle.getString("Invalid values entered"));
            }
        } catch (SocketTimeoutException e) {
            processServerUnavailability();
        } catch (NumberFormatException e) {
            showErrorMessage(resourceBundle.getString("Not all required fields are filled"));
        }
    }

    public void saveUpdate(ActionEvent event) {

        try {
            LabWorkStatic lws = getLabWorkFromFields();
            if (LabWorkValidator.isValid(lws)) {
                connectionProvider.send(new Request("update", String.valueOf(editableId), lws,
                        session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
                Response response = connectionProvider.receive();
                if (response.getExecCode() == 0) showSuccessMessage(response.getBody().getText());
                else showErrorMessage(response.getBody().getText());
            } else {
                showErrorMessage(resourceBundle.getString("Invalid values entered"));
            }
        } catch (SocketTimeoutException e) {
            processServerUnavailability();
        } catch (NumberFormatException e) {
            showErrorMessage(resourceBundle.getString("Not all required fields are filled"));
        }
    }

    public void saveRemoveGreater(ActionEvent event) {
        try {
            LabWorkStatic lws = getLabWorkFromFields();
            if (LabWorkValidator.isValid(lws)) {
                connectionProvider.send(new Request("remove_greater", " ", lws,
                        session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
                Response response = connectionProvider.receive();
                if (response.getExecCode() == 0) showSuccessMessage(response.getBody().getText());
                else showErrorMessage(response.getBody().getText());
            } else {
                showErrorMessage(resourceBundle.getString("Invalid values entered"));
            }
        } catch (SocketTimeoutException e) {
            processServerUnavailability();
        } catch (NumberFormatException e) {
            showErrorMessage(resourceBundle.getString("Not all required fields are filled"));
        }
    }

    public void cancel(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    public void setEditableId(long id) {
        editableId = id;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setMainLabel(String text) {
        mainLabel.setText(text);
    }

    public void setSaveAction(EventHandler<ActionEvent> handler) {
        saveButton.setOnAction(handler);
    }

    private void showSuccessMessage(String text) {
        messageLabel.setTextFill(Color.GREEN);
        messageLabel.setText(text);
        messageLabel.setVisible(true);
    }

    private void showErrorMessage(String text) {
        messageLabel.setTextFill(Color.RED);
        messageLabel.setText(text);
        messageLabel.setVisible(true);
    }

    private LabWorkStatic getLabWorkFromFields() {

        Discipline discipline = (disciplineNameField.getText().isEmpty() || disciplineNameField.getText().isBlank()) ?
                null: new Discipline(disciplineNameField.getText(), Long.parseLong(lectureHoursField.getText()),
                Integer.parseInt(practiceHoursField.getText()), Long.parseLong(selfStudyHoursField.getText()));

        return new LabWorkStatic(nameField.getText(),
                new Coordinates(Integer.parseInt(XField.getText()),
                        Float.parseFloat(YField.getText().replace(',', '.'))),
                Integer.parseInt(minimalPointField.getText()), Long.parseLong(averagePointField.getText()),
                Difficulty.valueOf(difficultyMenu.getValue()), discipline);
    }

    private void processServerUnavailability() {
        showErrorMessage(resourceBundle.getString("The server is unavailable, please try again later"));
    }
}
