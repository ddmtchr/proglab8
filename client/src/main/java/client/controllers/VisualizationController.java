package client.controllers;

import client.ClientCollectionManager;
import client.Session;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import stored.LabWork;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VisualizationController implements Initializable {
    @FXML
    private AnchorPane visualizationPane;
    private Vector<LabWork> oldCollection;
    private Vector<LabWork> newCollection;
    private ConcurrentHashMap<LabWork, Canvas> positions;
    private HashMap<String, Color> colorMap;
    private Session session;
    private MainWindowController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        oldCollection = new Vector<>();
        newCollection = new Vector<>();
        positions = new ConcurrentHashMap<>();
        colorMap = new HashMap<>();
    }

    public void updateVisual(Vector<LabWork> receivedCollection) {
        oldCollection = new Vector<>(newCollection);
        newCollection = receivedCollection;
        Map<Long, LabWork> oldMap = oldCollection.stream().collect(Collectors.toMap(LabWork::getId, Function.identity()));
        Map<Long, LabWork> newMap = newCollection.stream().collect(Collectors.toMap(LabWork::getId, Function.identity()));
        List<LabWork> elementsToAdd = getElementsToAdd(oldMap, newCollection);
        List<LabWork> elementsToUpdate = getElementsToUpdate(oldMap, newCollection);
        List<LabWork> elementsToRemove = getElementsToRemove(newMap, oldCollection);
        for (LabWork lw: elementsToRemove) {
            long id = lw.getId();
            positions.keySet().stream().filter(l -> l.getId() == id).forEach(this::removeFromVisualization);
        }
        elementsToAdd.forEach(this::addToVisualization);
        for (LabWork lw: elementsToUpdate) {
            long id = lw.getId();
            positions.keySet().stream().filter(l -> l.getId() == id).forEach(this::removeFromVisualization);
            addToVisualization(lw);
        }
    }

    private List<LabWork> getElementsToAdd(Map<Long, LabWork> oldMap, Vector<LabWork> newCollection) {
        return newCollection.stream().filter(lw -> !oldMap.containsKey(lw.getId())).collect(Collectors.toList());
    }

    private List<LabWork> getElementsToUpdate(Map<Long, LabWork> oldMap, Vector<LabWork> newCollection) {
        ArrayList<LabWork> elementsToUpdate = new ArrayList<>();
        for (LabWork newLW : newCollection) {
            if (oldMap.containsKey(newLW.getId()) && oldMap.get(newLW.getId()).hashCode() != newLW.hashCode()) {
                elementsToUpdate.add(newLW);
            }
        }
        return elementsToUpdate;
    }

    private List<LabWork> getElementsToRemove(Map<Long, LabWork> newMap, Vector<LabWork> oldCollection) {
        return oldCollection.stream().filter(lw -> !newMap.containsKey(lw.getId())).collect(Collectors.toList());
    }

    private void addToVisualization(LabWork lw) {
        Canvas canvas = generateCanvas(lw);
        FadeTransition transition = new FadeTransition(Duration.seconds(2), canvas);
        transition.setFromValue(0);
        transition.setToValue(1);
        positions.put(lw, canvas);
        transition.play();
        visualizationPane.getChildren().add(canvas);
    }

    private void removeFromVisualization(LabWork lw) {

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), positions.get(lw));
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> visualizationPane.getChildren().remove(positions.get(lw)));
        fadeOut.play();
        positions.remove(lw);
    }

    private Canvas generateCanvas(LabWork lw) {
        int size = (lw.getDifficulty().ordinal() + 1) * 15;
        Canvas canvas = new Canvas(size, size);
        CustomTooltip info = new CustomTooltip(lw.toString());
//        Tooltip info = new Tooltip("text");
//        Tooltip.install(canvas, info);
//        info.setShowDelay(Duration.millis(300));
//        info.setShowDuration(Duration.seconds(60));
        canvas.setOnMouseEntered(event -> {
            canvas.setScaleX(1.1);
            canvas.setScaleY(1.1);
        });
        canvas.setOnMouseExited(event -> {
            canvas.setScaleX(1);
            canvas.setScaleY(1);
        });
        canvas.setOnMouseClicked(mouseEvent -> {
            try {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    parentController.openEditWindow(lw, session);
                } else {
                    if (info.isShowing()) info.hide();
                    else info.show(canvas);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        canvas.setLayoutX(400 + lw.getCoordinates().getX() - size / 2.0);
        canvas.setLayoutY(400 - lw.getCoordinates().getY() + size / 2.0);
        drawLabWork(lw, size, canvas);
        return canvas;
    }

    private void drawLabWork(LabWork lw, int size, Canvas canvas) {
        String username = lw.getUsername();
        if (!colorMap.containsKey(username)) {
            colorMap.put(username, Color.color(Math.random(), Math.random(), Math.random()));
        }
        Color color = colorMap.get(username);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(color);
        context.fillRoundRect(0, 0, size, size, size / 5.0, size / 5.0);
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setParentController(MainWindowController controller) {
        parentController = controller;
    }

    private class CustomTooltip extends Pane {
        private TextFlow text;
        private boolean isShowing;

        public CustomTooltip(String t) {
            setPrefWidth(100);
            setMinHeight(0);
            setMaxHeight(1000);
            text = new TextFlow(new Text(t));
            text.setStyle("-fx-background-color: lightgray; -fx-padding: 4px; -fx-border-radius: 5px;");
            getChildren().add(text);
            setOpacity(0);
            isShowing = false;
            setMouseTransparent(true);
            visualizationPane.getChildren().add(this);
        }

        public void show(Canvas parentCanvas) {
            setOpacity(1);
            setTranslateX(parentCanvas.getLayoutX() + parentCanvas.getWidth());
            setTranslateY(parentCanvas.getLayoutY() - parentCanvas.getHeight());
            isShowing = true;
        }

        public void hide() {
            setOpacity(0);
            isShowing = false;
        }

        public boolean isShowing() {
            return isShowing;
        }
    }
}
