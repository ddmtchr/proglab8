package client.controllers;

import client.ClientCollectionManager;
import client.ConnectionProvider;
import client.ScriptExecutor;
import client.Session;
import exceptions.ErrorInScriptException;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import stored.Difficulty;
import stored.LabWork;
import utility.Request;
import utility.RequestType;
import utility.Response;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// TODO no DBConnection ex catching
public class MainWindowController implements Initializable {
    @FXML
    private Button russianButton;
    @FXML
    private Button germanButton;
    @FXML
    private Button albanianButton;
    @FXML
    private Button indianEnglishButton;
    @FXML
    private AnchorPane userPane;
    @FXML
    private Label labStorageLabel;
    @FXML
    private Label userLabel;
    @FXML
    private TableView<LabWork> mainTable;
    @FXML
    private TableColumn<LabWork, Long> idColumn;
    @FXML
    private TableColumn<LabWork, String> nameColumn;
    @FXML
    private TableColumn<LabWork, Integer> XColumn;
    @FXML
    private TableColumn<LabWork, Float> YColumn;
    @FXML
    private TableColumn<LabWork, String> creationDateColumn;
    @FXML
    private TableColumn<LabWork, Integer> minimalPointColumn;
    @FXML
    private TableColumn<LabWork, Long> averagePointColumn;
    @FXML
    private TableColumn<LabWork, Difficulty> difficultyColumn;
    @FXML
    private TableColumn<LabWork, String> disciplineNameColumn;
    @FXML
    private TableColumn<LabWork, Long> lectureHoursColumn;
    @FXML
    private TableColumn<LabWork, Integer> practiceHoursColumn;
    @FXML
    private TableColumn<LabWork, Long> selfStudyHoursColumn;
    @FXML
    private TableColumn<LabWork, String> ownerColumn;
    @FXML
    private MenuButton commandMenu;
    @FXML
    private MenuItem addCommand;
    @FXML
    private MenuItem addIfMinCommand;
    @FXML
    private MenuItem updateByIdCommand;
    @FXML
    private MenuItem removeCommand;
    @FXML
    private MenuItem removeByIdCommand;
    @FXML
    private MenuItem removeGreaterCommand;
    @FXML
    private MenuItem clearCommand;
    @FXML
    private MenuItem averageOfMinimalPointCommand;
    @FXML
    private MenuItem minByIdCommand;
    @FXML
    private MenuItem printFieldAscendingMinimalPointCommand;
    @FXML
    private MenuItem executeScriptCommand;
    @FXML
    private MenuItem infoCommand;
    @FXML
    private MenuButton filterMenu;
    @FXML
    private MenuItem filterAveragePoint;
    @FXML
    private MenuItem filterCreationDate;
    @FXML
    private MenuItem filterDifficulty;
    @FXML
    private MenuItem filterDisciplineName;
    @FXML
    private MenuItem filterID;
    @FXML
    private MenuItem filterLectureHours;
    @FXML
    private MenuItem filterMinimalPoint;
    @FXML
    private MenuItem filterName;
    @FXML
    private MenuItem filterOwner;
    @FXML
    private MenuItem filterPracticeHours;
    @FXML
    private MenuItem filterSelfStudyHours;
    @FXML
    private MenuItem filterX;
    @FXML
    private MenuItem filterY;
    @FXML
    private MenuItem resetFilter;
    @FXML
    private Button sortMenu;
    @FXML
    private Button visualizationButton;
    private VisualizationController visualizationController;
    private Stage stage;
    private long selectedId = -1;
    private Session session;
    private ConnectionProvider connectionProvider;
    public ObservableList<LabWork> collection = FXCollections.observableList(new ArrayList<>());
    private ResourceBundle resourceBundle;
    private Button[] langButtons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connectionProvider = ConnectionProvider.getConnectionProvider();

            this.resourceBundle = resourceBundle;
            langButtons = new Button[]{russianButton, germanButton, albanianButton, indianEnglishButton};
            mainTable.setRowFactory(tableView -> {
                TableRow<LabWork> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    try {
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            LabWork labWork = row.getItem();
                            openEditWindow(labWork, session);
                        } else if (event.getClickCount() == 1 && (!row.isEmpty())) {
                            selectedId = row.getItem().getId();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                return row;
            });

            idColumn.setCellValueFactory(new PropertyValueFactory<LabWork, Long>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<LabWork, String>("name"));
            XColumn.setCellValueFactory(cellValue -> new SimpleIntegerProperty(cellValue.getValue().getCoordinates().getX()).asObject());
            YColumn.setCellValueFactory(cellValue -> new SimpleFloatProperty(cellValue.getValue().getCoordinates().getY()).asObject());
            creationDateColumn.setCellValueFactory(cellValue -> new SimpleStringProperty(cellValue.getValue().creationDateToString()));
            minimalPointColumn.setCellValueFactory(new PropertyValueFactory<LabWork, Integer>("minimalPoint"));
            averagePointColumn.setCellValueFactory(new PropertyValueFactory<LabWork, Long>("averagePoint"));
            difficultyColumn.setCellValueFactory(new PropertyValueFactory<LabWork, Difficulty>("difficulty"));
            disciplineNameColumn.setCellValueFactory(cellValue -> {
                if (cellValue.getValue().getDiscipline() != null)
                    return new SimpleStringProperty(cellValue.getValue().getDiscipline().getName());
                else return null;
            });
            lectureHoursColumn.setCellValueFactory(cellValue -> {
                if (cellValue.getValue().getDiscipline() != null)
                    return new SimpleLongProperty(cellValue.getValue().getDiscipline().getLectureHours()).asObject();
                else return null;
            });
            practiceHoursColumn.setCellValueFactory(cellValue -> {
                if (cellValue.getValue().getDiscipline() != null)
                    return new SimpleIntegerProperty(cellValue.getValue().getDiscipline().getPracticeHours()).asObject();
                else return null;
            });
            selfStudyHoursColumn.setCellValueFactory(cellValue -> {
                if (cellValue.getValue().getDiscipline() != null)
                    return new SimpleLongProperty(cellValue.getValue().getDiscipline().getSelfStudyHours()).asObject();
                else return null;
            });
            ownerColumn.setCellValueFactory(new PropertyValueFactory<LabWork, String>("username"));

            mainTable.setPlaceholder(new Label("Collection is empty"));
            mainTable.setItems(FXCollections.observableList(collection));

        } catch (IOException e) {
            System.out.println("Ошибка установления соединения с сервером");
        }
    }

    private EditWindowController loadFilledEditWindow(LabWork labWork) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.controllers/fxml/editWindow.fxml"), resourceBundle);
        Parent root = loader.load();
        EditWindowController controller = loader.getController();
        controller.setEditableId(labWork.getId());
        controller.fillFields(labWork);

        initEditor(root);
        return loader.getController();
    }


    private EditWindowController loadEditWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.controllers/fxml/editWindow.fxml"), resourceBundle);
        Parent root = loader.load();

        initEditor(root);
        return loader.getController();
    }


    public void openEditWindow(LabWork lw, Session session) throws IOException {
        EditWindowController controller = loadFilledEditWindow(lw);
        controller.setSession(session);
        controller.setMainLabel(resourceBundle.getString("Object editing"));
        controller.setSaveAction(controller::saveUpdate);
    }

    public void setAddWindow(Session session) throws IOException {
        EditWindowController controller = loadEditWindow();
        controller.setSession(session);
        controller.setMainLabel(resourceBundle.getString("Object addition"));
        controller.setSaveAction(controller::saveAdd);
    }

    public void setAddIfMinWindow(Session session) throws IOException {
        EditWindowController controller = loadEditWindow();
        controller.setSession(session);
        controller.setMainLabel(resourceBundle.getString("Object addition"));
        controller.setSaveAction(controller::saveAddIfMin);
    }

    public void setRemoveGreaterWindow(Session session) throws IOException {
        EditWindowController controller = loadEditWindow();
        controller.setSession(session);
        controller.setMainLabel(resourceBundle.getString("Object removal"));
        controller.setSaveAction(controller::saveRemoveGreater);
    }

    public void openVisualization() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.controllers/fxml/visualization.fxml"));
            Parent root = loader.load();
            visualizationController = loader.getController();
            visualizationController.setSession(session);
            visualizationController.setParentController(this);

            Scene scene = new Scene(root, 800, 800);
            Image icon = new Image(getClass().getResourceAsStream("/client.controllers/images/rubberduck1.png"));

            Stage stage = new Stage();
            stage.getIcons().add(icon);
            stage.setTitle(resourceBundle.getString("Visualization"));

            String resource = getClass().getResource("/client.controllers/styles/app.css").toExternalForm();
            scene.getStylesheets().add(resource);
            stage.setScene(scene);
            stage.setResizable(false);
            scene.setOnKeyPressed((e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.setFullScreen(false);
                } else if (e.getCode() == KeyCode.F11) {
                    stage.setFullScreen(true);
                }
            }));
            stage.setOnCloseRequest((e) -> visualizationController = null);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyVisualizer() {
        visualizationController.updateVisual(ClientCollectionManager.getClientCollection());
    }

    @FXML
    public void add(ActionEvent event) {
        try {
            setAddWindow(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addIfMin(ActionEvent event) {
        try {
            setAddIfMinWindow(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateById(ActionEvent event) {
        try {
            EnterIdController controller = showEnterIdWindow();
            controller.getButton().setOnAction((e -> {
                try {
                    Set<Long> currentIDs = ClientCollectionManager.getClientCollection().stream().map(LabWork::getId).collect(Collectors.toSet());
                    long id = Long.parseLong(controller.getIdField().getText());
                    if (currentIDs.contains(id)) {
                        openEditWindow(ClientCollectionManager.getById(id), session);
                        ((Stage) (((Node) (e.getSource())).getScene().getWindow())).close();
                    } else {
                        showAlert(resourceBundle.getString("No element in the collection with this ID"));
                    }
                } catch (SocketTimeoutException ex) {
                    showAlert(resourceBundle.getString("The server is unavailable, please try again later"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void remove(ActionEvent event) {
        try {
            if (selectedId != -1) {
                connectionProvider.send(new Request("remove_by_id", String.valueOf(selectedId), null,
                        session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
                Response response = connectionProvider.receive();
                showAlert(response.getBody().getText());
            } else {
                showAlert(resourceBundle.getString("No element selected for removal"));
            }
        } catch (SocketTimeoutException e) {
            showAlert(resourceBundle.getString("The server is unavailable, please try again later"));
        }
    }

    @FXML
    public void removeById() {
        try {
            EnterIdController controller = showEnterIdWindow();

            controller.getButton().setOnAction((event -> {
                try {
                    connectionProvider.send(new Request("remove_by_id", controller.getIdField().getText(), null,
                            session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
                    Response response = connectionProvider.receive();
                    ((Stage) (((Node) (event.getSource())).getScene().getWindow())).close();
                    showAlert(response.getBody().getText());
                } catch (SocketTimeoutException e) {
                    showAlert(resourceBundle.getString("The server is unavailable, please try again later"));
                }
            }));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeGreater() {
        try {
            setRemoveGreaterWindow(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clear() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, resourceBundle.getString("Are you sure you want to clear your collection?"));
        alert.showAndWait().ifPresent(pressed -> {
            try {
                if (pressed == ButtonType.OK) {
                    connectionProvider.send(new Request("clear", " ", null,
                            session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
                    Response response = connectionProvider.receive();
                    showAlert(response.getBody().getText());
                }
            } catch (SocketTimeoutException e) {
                showAlert(resourceBundle.getString("The server is unavailable, please try again later"));
            }
        });
    }

    @FXML
    public void averageOfMinimalPoint() {
        try {
            connectionProvider.send(new Request("average_of_minimal_point", " ", null,
                    session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
            Response response = connectionProvider.receive();
            showAlert(response.getBody().getText());
        } catch (SocketTimeoutException e) {
            showAlert(resourceBundle.getString("The server is unavailable, please try again later"));
        }
    }

    @FXML
    public void minById() {
        try {
            connectionProvider.send(new Request("min_by_id", " ", null,
                    session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
            Response response = connectionProvider.receive();
            if (response.getExecCode() == 0) {
                InfoWindowController controller = showInfoWindow();
                controller.setLabel(resourceBundle.getString("Object with the minimum ID"));
                controller.setText(response.getBody().getText());
            } else {
                showAlert(response.getBody().getText());
            }
        } catch (SocketTimeoutException e) {
            showAlert(resourceBundle.getString("The server is unavailable, please try again later"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void printFieldAscendingMinimalPoint() {
        try {
            connectionProvider.send(new Request("print_field_ascending_minimal_point", " ", null,
                    session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
            Response response = connectionProvider.receive();
            if (response.getExecCode() == 0) {
                InfoWindowController controller = showInfoWindow();
                controller.setLabel(resourceBundle.getString("Minimum point in ascending order:"));
                controller.setText(response.getBody().getText());
            } else {
                showAlert(response.getBody().getText());
            }
        } catch (SocketTimeoutException e) {
            showAlert(resourceBundle.getString("The server is unavailable, please try again later"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void executeScript() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("Open script file"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        stage = (Stage) (commandMenu.getScene().getWindow());
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            ScriptExecutor scriptExecutor = new ScriptExecutor();
            new Thread(() -> {
                try {
                    Platform.runLater(() -> showAlert(resourceBundle.getString("Executing script ") + selectedFile.getName() + "..."));
                    scriptExecutor.executeScript(selectedFile, connectionProvider, session);
                    Platform.runLater(() -> showAlert(scriptExecutor.getAndClear()));
                } catch (ErrorInScriptException ignored) {
                }
            }).start();
        }
    }

    @FXML
    public void info() {
        try {
            connectionProvider.send(new Request("info", " ", null,
                    session.getLogin(), session.getPassword(), null, RequestType.COMMAND));
            Response response = connectionProvider.receive();
            showAlert(response.getBody().getText());
        } catch (SocketTimeoutException e) {
            showAlert(resourceBundle.getString("The server is unavailable, please try again later"));
        }
    }

    @FXML
    public void sort() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("sort");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterID() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setValueFieldFormatter(Util.getWholeNumbersFormatter());
            controller.setFilterField("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterName() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setFilterField("2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterX() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setValueFieldFormatter(Util.getWholeNumbersFormatter());
            controller.setFilterField("3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterY() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setValueFieldFormatter(Util.getFracNumbersFormatter());
            controller.setFilterField("4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterCreationDate() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setFilterField("5");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterMinimalPoint() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setValueFieldFormatter(Util.getWholeNumbersFormatter());
            controller.setFilterField("6");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterAveragePoint() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setValueFieldFormatter(Util.getWholeNumbersFormatter());
            controller.setFilterField("7");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterDifficulty() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setFilterField("8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterDisciplineName() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setFilterField("9");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterLectureHours() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setValueFieldFormatter(Util.getWholeNumbersFormatter());
            controller.setFilterField("10");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterPracticeHours() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setValueFieldFormatter(Util.getWholeNumbersFormatter());
            controller.setFilterField("11");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterSelfStudyHours() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setValueFieldFormatter(Util.getWholeNumbersFormatter());
            controller.setFilterField("12");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterOwner() {
        try {
            SortFilterWindowController controller = showSortFilterWindow("filter");
            controller.setFilterField("13");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void resetFilter() {
        ClientCollectionManager.setFilter(lw -> true);
    }

    private void setUserLabel(String username) {
        userLabel.setText(username);
    }

    public void setSession(Session session) {
        this.session = session;
        setUserLabel(resourceBundle.getString("User:") + " " + session.getLogin());
    }

    public void loadCollection(Session session) {
        try {
            Request request = new Request(null, null, null,
                    session.getLogin(), session.getPassword(), null, RequestType.GET_COLLECTION);
            connectionProvider.send(request);
            Response response = connectionProvider.receive();
            ClientCollectionManager.setClientCollection(response.getBody().getCollection());
            setCollection(ClientCollectionManager.getClientCollection());
        } catch (SocketTimeoutException e) {
            System.out.println("Обновление коллекции не пришло");
        }
    }

    public void setCollection(Vector<LabWork> collection) {

        this.collection = FXCollections.observableList(collection);
        mainTable.setItems(this.collection);
    }

    public void runUpdatingThread(Session session) {
        Thread updater = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    loadCollection(session);
                    if (visualizationController != null) notifyVisualizer();
                });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    System.out.println("Поток обновления коллекции был прерван");
                }
            }
        });
        updater.start();
    }

    private void initEditor(Parent root) {
        Scene scene = new Scene(root, 462, 800);
        Image icon = new Image(getClass().getResourceAsStream("/client.controllers/images/rubberduck1.png"));

        Stage stage = new Stage();
        stage.getIcons().add(icon);
        stage.setTitle(resourceBundle.getString("Collection Editor"));

        String resource = getClass().getResource("/client.controllers/styles/app.css").toExternalForm();
        scene.getStylesheets().add(resource);
        stage.setScene(scene);
        stage.setMinWidth(475);
        stage.setMinHeight(813);
        scene.setOnKeyPressed((e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.setFullScreen(false);
            } else if (e.getCode() == KeyCode.F11) {
                stage.setFullScreen(true);
            }
        }));

        stage.show();
    }

    private EnterIdController showEnterIdWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.controllers/fxml/enterId.fxml"), resourceBundle);
        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 200);
        Image icon = new Image(getClass().getResourceAsStream("/client.controllers/images/rubberduck1.png"));

        Stage stage = new Stage();
        stage.getIcons().add(icon);
        stage.setTitle(resourceBundle.getString("Enter ID"));

        String resource = getClass().getResource("/client.controllers/styles/app.css").toExternalForm();
        scene.getStylesheets().add(resource);
        stage.setScene(scene);
        stage.setResizable(false);

        EnterIdController controller = loader.getController();
        controller.getIdField().setTextFormatter(new TextFormatter<>(change -> {
            if (Pattern.compile("^(\\d*)$").matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null;
            }
        }));
        stage.show();
        return controller;

    }

    private InfoWindowController showInfoWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.controllers/fxml/infoWindow.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        Image icon = new Image(getClass().getResourceAsStream("/client.controllers/images/rubberduck1.png"));

        Stage stage = new Stage();
        stage.getIcons().add(icon);
        stage.setTitle("Info");

        String resource = getClass().getResource("/client.controllers/styles/app.css").toExternalForm();
        scene.getStylesheets().add(resource);
        stage.setScene(scene);
        stage.setResizable(false);

        InfoWindowController controller = loader.getController();

        stage.show();
        return controller;
    }

    private SortFilterWindowController showSortFilterWindow(String action) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.controllers/fxml/sortFilterWindow.fxml"), resourceBundle);
        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 200);
        Image icon = new Image(getClass().getResourceAsStream("/client.controllers/images/rubberduck1.png"));

        Stage stage = new Stage();
        stage.getIcons().add(icon);
        stage.setTitle(resourceBundle.getString("Sorting and Filtering"));

        String resource = getClass().getResource("/client.controllers/styles/app.css").toExternalForm();
        scene.getStylesheets().add(resource);
        stage.setScene(scene);
        stage.setResizable(false);

        SortFilterWindowController controller = loader.getController();
        if (action.equals("filter")) {
            controller.setFilterNodesVisible();
            controller.setButtonAction(controller::filter);
            controller.setButtonText("Filter");

        }
        else if (action.equals("sort")) {
            controller.setSortNodesVisible();
            controller.setButtonAction(controller::sort);
            controller.setButtonText("Sort");
        }

        stage.show();
        return controller;
    }

    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.setTitle(resourceBundle.getString("Information"));
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
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

    public void changeLanguage() {
        setResourceBundle();
        labStorageLabel.setText(resourceBundle.getString("Lab repository"));
        setUserLabel(resourceBundle.getString("User:") + " " + session.getLogin());
        nameColumn.setText(resourceBundle.getString("Name"));
        creationDateColumn.setText(resourceBundle.getString("Creation Date"));
        minimalPointColumn.setText(resourceBundle.getString("Minimum point"));
        averagePointColumn.setText(resourceBundle.getString("Average point"));
        difficultyColumn.setText(resourceBundle.getString("Difficulty"));
        disciplineNameColumn.setText(resourceBundle.getString("Discipline Name"));
        lectureHoursColumn.setText(resourceBundle.getString("Lecture Hours"));
        practiceHoursColumn.setText(resourceBundle.getString("Practice Hours"));
        selfStudyHoursColumn.setText(resourceBundle.getString("Self-study Hours"));
        ownerColumn.setText(resourceBundle.getString("Owner"));

        commandMenu.setText(resourceBundle.getString("Select an action..."));
        addCommand.setText(resourceBundle.getString("Add"));
        addIfMinCommand.setText(resourceBundle.getString("Add if Min"));
        updateByIdCommand.setText(resourceBundle.getString("Update by ID"));
        removeCommand.setText(resourceBundle.getString("Remove"));
        removeByIdCommand.setText(resourceBundle.getString("Remove by ID"));
        removeGreaterCommand.setText(resourceBundle.getString("Remove Greater"));
        clearCommand.setText(resourceBundle.getString("Clear"));
        averageOfMinimalPointCommand.setText(resourceBundle.getString("Average Minimum point"));
        minByIdCommand.setText(resourceBundle.getString("Minimum by ID"));
        printFieldAscendingMinimalPointCommand.setText(resourceBundle.getString("Minimum point Ascending"));
        executeScriptCommand.setText(resourceBundle.getString("Execute Script"));
        infoCommand.setText(resourceBundle.getString("Information"));

        filterMenu.setText(resourceBundle.getString("Filtering"));
        sortMenu.setText(resourceBundle.getString("Sorting"));
        visualizationButton.setText(resourceBundle.getString("Visualization"));

        filterName.setText(resourceBundle.getString("Name"));
        filterCreationDate.setText(resourceBundle.getString("Creation Date"));
        filterMinimalPoint.setText(resourceBundle.getString("Minimum point"));
        filterAveragePoint.setText(resourceBundle.getString("Average point"));
        filterDifficulty.setText(resourceBundle.getString("Difficulty"));
        filterDisciplineName.setText(resourceBundle.getString("Discipline Name"));
        filterLectureHours.setText(resourceBundle.getString("Lecture Hours"));
        filterPracticeHours.setText(resourceBundle.getString("Practice Hours"));
        filterSelfStudyHours.setText(resourceBundle.getString("Self-study Hours"));
        filterOwner.setText(resourceBundle.getString("Owner"));
        resetFilter.setText(resourceBundle.getString("Reset"));

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
}
