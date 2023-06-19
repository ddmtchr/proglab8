package client.controllers;

import client.ClientCollectionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import stored.Difficulty;
import stored.Discipline;
import stored.LabWork;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;

public class SortFilterWindowController implements Initializable {
    @FXML
    private Label sortFilterLabel;
    @FXML
    private TextField valueField;
    @FXML
    private ChoiceBox<String> conditionMenu;
    @FXML
    private ChoiceBox<String> sortField;
    @FXML
    private Button button;
    private List<String> conditions = List.of("=", "!=", ">", ">=", "<", "<=");
    private List<String> sortDirections;
    private List<String> sortFields;
    private Map<String, Predicate<LabWork>> predicateMap = new HashMap<>();
    private Map<String, Comparator<LabWork>> comparatorMap = new HashMap<>();
    private Map<String, String> fieldMap = new HashMap<>();
    private Map<String, String> sortingMap = new HashMap<>();
    private String filterField;
    private String comparingValue;
    private ResourceBundle resourceBundle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        sortFields = List.of("ID",
                resourceBundle.getString("Name"),
                "X",
                "Y",
                resourceBundle.getString("Creation Date"),
                resourceBundle.getString("Minimum point"),
                resourceBundle.getString("Average point"),
                resourceBundle.getString("Difficulty"),
                resourceBundle.getString("Discipline Name"),
                resourceBundle.getString("Lecture Hours"),
                resourceBundle.getString("Practice Hours"),
                resourceBundle.getString("Self-study Hours"),
                resourceBundle.getString("Owner"));
        sortDirections = List.of(resourceBundle.getString("Ascending"),
                resourceBundle.getString("Descending"));
//        fieldMap.put("id")
        fillPredicateMap();
        fillComparatorMap();
    }

    @FXML
    public void filter(ActionEvent event) {
        comparingValue = valueField.getText();
        String operator = conditionMenu.getValue();
        String operation = operator + filterField;
        Predicate<LabWork> predicate = predicateMap.get(operation);
        ClientCollectionManager.setFilter(predicate);
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).close();
    }

    @FXML
    public void sort(ActionEvent event) {
        String sorting = (sortField.getSelectionModel().getSelectedIndex() + 1) + "_" + (conditionMenu.getSelectionModel().getSelectedIndex() + 1);
        Comparator<LabWork> comparator = comparatorMap.get(sorting);
        ClientCollectionManager.setComparator(comparator);
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).close();
    }

    public void setValueFieldFormatter(TextFormatter<String> formatter) {
        valueField.setTextFormatter(formatter);
    }

    public void setFilterField(String fieldNumber) {
        filterField = fieldNumber;
    }

    public void setFilterNodesVisible() {
        sortFilterLabel.setText(resourceBundle.getString("Enter a value for the filter"));
        conditionMenu.setItems(FXCollections.observableList(conditions));
        conditionMenu.setValue("=");
        valueField.setVisible(true);
    }

    public void setSortNodesVisible() {
        sortFilterLabel.setText(resourceBundle.getString("Select a field for sorting"));
        conditionMenu.setItems(FXCollections.observableList(sortDirections));
        sortField.setItems(FXCollections.observableList(sortFields));
        conditionMenu.setValue(resourceBundle.getString("Ascending"));
        sortField.setValue("ID");
        sortField.setVisible(true);
    }

    public void setButtonAction(EventHandler<ActionEvent> eh) {
        button.setOnAction(eh);
    }

    public void setButtonText(String text) {
        button.setText(text);
    }


    // DON'T LOOK AT THIS. IT JUST WORKS
    private void fillPredicateMap() {
        predicateMap.put("=1", lw -> lw.getId() == Long.parseLong(comparingValue)); predicateMap.put("!=1", lw -> lw.getId() != Long.parseLong(comparingValue));
        predicateMap.put(">1", lw -> lw.getId() > Long.parseLong(comparingValue)); predicateMap.put(">=1", lw -> lw.getId() >= Long.parseLong(comparingValue));
        predicateMap.put("<1", lw -> lw.getId() < Long.parseLong(comparingValue)); predicateMap.put("<=1", lw -> lw.getId() <= Long.parseLong(comparingValue));
        predicateMap.put("=2", lw -> lw.getName().equals(comparingValue)); predicateMap.put("!=2", lw -> !lw.getName().equals(comparingValue));
        predicateMap.put(">2", lw -> lw.getName().compareTo(comparingValue) > 0); predicateMap.put(">=2", lw -> lw.getName().compareTo(comparingValue) >= 0);
        predicateMap.put("<2", lw -> lw.getName().compareTo(comparingValue) < 0); predicateMap.put("<=2", lw -> lw.getName().compareTo(comparingValue) <= 0);
        predicateMap.put("=3", lw -> lw.getCoordinates().getX() == Integer.parseInt(comparingValue)); predicateMap.put("!=3", lw -> lw.getCoordinates().getX() != Integer.parseInt(comparingValue));
        predicateMap.put(">3", lw -> lw.getCoordinates().getX() > Integer.parseInt(comparingValue)); predicateMap.put(">=3", lw -> lw.getCoordinates().getX() >= Integer.parseInt(comparingValue));
        predicateMap.put("<3", lw -> lw.getCoordinates().getX() < Integer.parseInt(comparingValue)); predicateMap.put("<=3", lw -> lw.getCoordinates().getX() <= Integer.parseInt(comparingValue));
        predicateMap.put("=4", lw -> lw.getCoordinates().getY() == Float.parseFloat(comparingValue)); predicateMap.put("!=4", lw -> lw.getCoordinates().getY() != Float.parseFloat(comparingValue));
        predicateMap.put(">4", lw -> lw.getCoordinates().getY() > Float.parseFloat(comparingValue)); predicateMap.put(">=4", lw -> lw.getCoordinates().getY() >= Float.parseFloat(comparingValue));
        predicateMap.put("<4", lw -> lw.getCoordinates().getY() < Float.parseFloat(comparingValue)); predicateMap.put("<=4", lw -> lw.getCoordinates().getY() <= Float.parseFloat(comparingValue));
        predicateMap.put("=5", lw -> lw.getCreationDate().compareTo(ZonedDateTime.parse(comparingValue)) == 0); predicateMap.put("!=5", lw -> lw.getCreationDate().compareTo(ZonedDateTime.parse(comparingValue)) != 0);
        predicateMap.put(">5", lw -> lw.getCreationDate().compareTo(ZonedDateTime.parse(comparingValue)) > 0); predicateMap.put(">=5", lw -> lw.getCreationDate().compareTo(ZonedDateTime.parse(comparingValue)) >= 0);
        predicateMap.put("<5", lw -> lw.getCreationDate().compareTo(ZonedDateTime.parse(comparingValue)) < 0); predicateMap.put("<=5", lw -> lw.getCreationDate().compareTo(ZonedDateTime.parse(comparingValue)) <= 0);
        predicateMap.put("=6", lw -> lw.getMinimalPoint() == Integer.parseInt(comparingValue)); predicateMap.put("!=6", lw -> lw.getMinimalPoint() != Integer.parseInt(comparingValue));
        predicateMap.put(">6", lw -> lw.getMinimalPoint() > Integer.parseInt(comparingValue)); predicateMap.put(">=6", lw -> lw.getMinimalPoint() >= Integer.parseInt(comparingValue));
        predicateMap.put("<6", lw -> lw.getMinimalPoint() < Integer.parseInt(comparingValue)); predicateMap.put("<=6", lw -> lw.getMinimalPoint() <= Integer.parseInt(comparingValue));
        predicateMap.put("=7", lw -> lw.getAveragePoint() == Long.parseLong(comparingValue)); predicateMap.put("!=7", lw -> lw.getAveragePoint() != Long.parseLong(comparingValue));
        predicateMap.put(">7", lw -> lw.getAveragePoint() < Long.parseLong(comparingValue)); predicateMap.put(">=7", lw -> lw.getAveragePoint() >= Long.parseLong(comparingValue));
        predicateMap.put("<7", lw -> lw.getAveragePoint() > Long.parseLong(comparingValue)); predicateMap.put("<=7", lw -> lw.getAveragePoint() <= Long.parseLong(comparingValue));
        predicateMap.put("=8", lw -> lw.getDifficulty().ordinal() == Difficulty.valueOf(comparingValue).ordinal()); predicateMap.put("!=8", lw -> lw.getDifficulty().ordinal() != Difficulty.valueOf(comparingValue).ordinal());
        predicateMap.put(">8", lw -> lw.getDifficulty().ordinal() > Difficulty.valueOf(comparingValue).ordinal()); predicateMap.put(">=8", lw -> lw.getDifficulty().ordinal() >= Difficulty.valueOf(comparingValue).ordinal());
        predicateMap.put("<8", lw -> lw.getDifficulty().ordinal() < Difficulty.valueOf(comparingValue).ordinal()); predicateMap.put("<=8", lw -> lw.getDifficulty().ordinal() <= Difficulty.valueOf(comparingValue).ordinal());
        predicateMap.put("=13", lw -> lw.getUsername().equals(comparingValue)); predicateMap.put("!=13", lw -> !lw.getUsername().equals(comparingValue));
        predicateMap.put(">13", lw -> lw.getUsername().compareTo(comparingValue) > 0); predicateMap.put(">=13", lw -> lw.getUsername().compareTo(comparingValue) >= 0);
        predicateMap.put("<13", lw -> lw.getUsername().compareTo(comparingValue) < 0); predicateMap.put("<=13", lw -> lw.getUsername().compareTo(comparingValue) <= 0);
        predicateMap.put("=9", lw -> (lw.getDiscipline() == null && comparingValue.isBlank()) || (lw.getDiscipline() != null && comparingValue.equals(lw.getDiscipline().getName())));
        predicateMap.put("!=9", lw -> !((lw.getDiscipline() == null && comparingValue.isBlank()) || (lw.getDiscipline() != null && comparingValue.equals(lw.getDiscipline().getName()))));
        predicateMap.put(">9", lw -> (comparingValue.isBlank() && lw.getDiscipline() != null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getName().compareTo(comparingValue) > 0));
        predicateMap.put(">=9", lw -> !((!comparingValue.isBlank() && lw.getDiscipline() == null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getName().compareTo(comparingValue) < 0)));
        predicateMap.put("<9", lw -> (!comparingValue.isBlank() && lw.getDiscipline() == null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getName().compareTo(comparingValue) < 0));
        predicateMap.put("<=9", lw -> !((comparingValue.isBlank() && lw.getDiscipline() != null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getName().compareTo(comparingValue) > 0)));
        predicateMap.put("=10", lw -> (lw.getDiscipline() == null && comparingValue.isBlank()) || (lw.getDiscipline() != null && lw.getDiscipline().getLectureHours() == Long.parseLong(comparingValue)));
        predicateMap.put("!=10", lw -> !((lw.getDiscipline() == null && comparingValue.isBlank()) || (lw.getDiscipline() != null && lw.getDiscipline().getLectureHours() == Long.parseLong(comparingValue))));
        predicateMap.put(">10", lw -> (comparingValue.isBlank() && lw.getDiscipline() != null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getLectureHours() > Long.parseLong(comparingValue)));
        predicateMap.put(">=10", lw -> !((!comparingValue.isBlank() && lw.getDiscipline() == null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getLectureHours() < Long.parseLong(comparingValue))));
        predicateMap.put("<10", lw -> (!comparingValue.isBlank() && lw.getDiscipline() == null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getLectureHours() < Long.parseLong(comparingValue)));
        predicateMap.put("<=10", lw -> !((comparingValue.isBlank() && lw.getDiscipline() != null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getLectureHours() > Long.parseLong(comparingValue))));
        predicateMap.put("=11", lw -> (lw.getDiscipline() == null && comparingValue.isBlank()) || (lw.getDiscipline() != null && lw.getDiscipline().getPracticeHours() == Integer.parseInt(comparingValue)));
        predicateMap.put("!=11", lw -> !((lw.getDiscipline() == null && comparingValue.isBlank()) || (lw.getDiscipline() != null && lw.getDiscipline().getPracticeHours() == Integer.parseInt(comparingValue))));
        predicateMap.put(">11", lw -> (comparingValue.isBlank() && lw.getDiscipline() != null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getPracticeHours() > Integer.parseInt(comparingValue)));
        predicateMap.put(">=11", lw -> !((!comparingValue.isBlank() && lw.getDiscipline() == null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getPracticeHours() < Integer.parseInt(comparingValue))));
        predicateMap.put("<11", lw -> (!comparingValue.isBlank() && lw.getDiscipline() == null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getPracticeHours() < Integer.parseInt(comparingValue)));
        predicateMap.put("<=11", lw -> !((comparingValue.isBlank() && lw.getDiscipline() != null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getPracticeHours() > Integer.parseInt(comparingValue))));
        predicateMap.put("=12", lw -> (lw.getDiscipline() == null && comparingValue.isBlank()) || (lw.getDiscipline() != null && lw.getDiscipline().getSelfStudyHours() == Long.parseLong(comparingValue)));
        predicateMap.put("!=12", lw -> !((lw.getDiscipline() == null && comparingValue.isBlank()) || (lw.getDiscipline() != null && lw.getDiscipline().getSelfStudyHours() == Long.parseLong(comparingValue))));
        predicateMap.put(">12", lw -> (comparingValue.isBlank() && lw.getDiscipline() != null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getSelfStudyHours() > Long.parseLong(comparingValue)));
        predicateMap.put(">=12", lw -> !((!comparingValue.isBlank() && lw.getDiscipline() == null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getSelfStudyHours() < Long.parseLong(comparingValue))));
        predicateMap.put("<12", lw -> (!comparingValue.isBlank() && lw.getDiscipline() == null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getSelfStudyHours() < Long.parseLong(comparingValue)));
        predicateMap.put("<=12", lw -> !((comparingValue.isBlank() && lw.getDiscipline() != null) || (!comparingValue.isBlank() && lw.getDiscipline() != null && lw.getDiscipline().getSelfStudyHours() > Long.parseLong(comparingValue))));
    }

    private void fillComparatorMap() {
        comparatorMap.put("1_1", Comparator.comparingLong(LabWork::getId)); comparatorMap.put("1_2", Comparator.comparingLong(LabWork::getId).reversed());
        comparatorMap.put("2_1", Comparator.comparing(LabWork::getName)); comparatorMap.put("2_2", Comparator.comparing(LabWork::getName).reversed());
        comparatorMap.put("3_1", Comparator.comparingInt(lw -> lw.getCoordinates().getX())); comparatorMap.put("3_2", Comparator.comparingInt((LabWork lw) -> lw.getCoordinates().getX()).reversed());
        comparatorMap.put("4_1", Comparator.comparingDouble(lw -> lw.getCoordinates().getY())); comparatorMap.put("4_2", Comparator.comparingDouble((LabWork lw) -> lw.getCoordinates().getY()).reversed());
        comparatorMap.put("5_1", Comparator.comparing(LabWork::getCreationDate)); comparatorMap.put("5_2", Comparator.comparing(LabWork::getCreationDate).reversed());
        comparatorMap.put("6_1", Comparator.comparingInt(LabWork::getMinimalPoint)); comparatorMap.put("6_2", Comparator.comparingInt(LabWork::getMinimalPoint).reversed());
        comparatorMap.put("7_1", Comparator.comparingLong(LabWork::getAveragePoint)); comparatorMap.put("7_2", Comparator.comparingLong(LabWork::getAveragePoint).reversed());
        comparatorMap.put("8_1", Comparator.comparing(LabWork::getDifficulty)); comparatorMap.put("8_2", Comparator.comparing(LabWork::getDifficulty).reversed());
        comparatorMap.put("13_1", Comparator.comparing(LabWork::getUsername));
        comparatorMap.put("13_2", Comparator.comparing(LabWork::getUsername).reversed());
        comparatorMap.put("9_1", Comparator.nullsFirst(Comparator.comparing(LabWork::getDiscipline,
                Comparator.nullsFirst(Comparator.comparing(Discipline::getName)))));
        comparatorMap.put("9_2", Comparator.nullsFirst(Comparator.comparing(LabWork::getDiscipline,
                Comparator.nullsFirst(Comparator.comparing(Discipline::getName)))).reversed());
        comparatorMap.put("10_1", Comparator.nullsFirst(Comparator.comparing(LabWork::getDiscipline,
                Comparator.nullsFirst(Comparator.comparing(Discipline::getLectureHours)))));
        comparatorMap.put("10_2", Comparator.nullsFirst(Comparator.comparing(LabWork::getDiscipline,
                Comparator.nullsFirst(Comparator.comparing(Discipline::getLectureHours)))).reversed());
        comparatorMap.put("11_1", Comparator.nullsFirst(Comparator.comparing(LabWork::getDiscipline,
                Comparator.nullsFirst(Comparator.comparing(Discipline::getPracticeHours)))));
        comparatorMap.put("11_2", Comparator.nullsFirst(Comparator.comparing(LabWork::getDiscipline,
                Comparator.nullsFirst(Comparator.comparing(Discipline::getPracticeHours)))).reversed());
        comparatorMap.put("12_1", Comparator.nullsFirst(Comparator.comparing(LabWork::getDiscipline,
                Comparator.nullsFirst(Comparator.comparing(Discipline::getSelfStudyHours)))));
        comparatorMap.put("12_2", Comparator.nullsFirst(Comparator.comparing(LabWork::getDiscipline,
                Comparator.nullsFirst(Comparator.comparing(Discipline::getSelfStudyHours)))).reversed());
    }
}
