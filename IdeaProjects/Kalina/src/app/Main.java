package app;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.KindLogger;
import utils.MonitoringUtils;

import java.io.IOException;

public class Main extends Application {
    private final static String PATH = "/views/main.fxml";

    @Override
    public void start(Stage primaryStage) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource(PATH));

        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            MonitoringUtils.logger(
                    KindLogger.ERROR,
                    "Exception has occured during loader.load()!",
                    e);
        }

        if (anchorPane == null) {
            MonitoringUtils.logger(
                    KindLogger.ERROR,
                    "Main.start(): AnchorPane is null!");
            return;
        }

        StackPane root = new StackPane();

        root.getChildren().addAll(anchorPane);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Kalina");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
