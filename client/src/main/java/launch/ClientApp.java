package launch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/client.controllers/fxml/loginButtons.fxml"));
        Scene scene = new Scene(root, 600, 400);
        Image icon = new Image(getClass().getResourceAsStream("/client.controllers/images/rubberduck1.png"));

        stage.getIcons().add(icon);
        stage.setTitle("Program 42");

        String resource = getClass().getResource("/client.controllers/styles/app.css").toExternalForm();
        scene.getStylesheets().add(resource);
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(440);

        stage.show();

    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause().printStackTrace();
        }
    }
}

