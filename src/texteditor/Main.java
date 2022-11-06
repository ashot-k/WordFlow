package texteditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInterface.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setupEvents(primaryStage);

        primaryStage.setTitle("TextEditor");
        primaryStage.setScene(new Scene(root, 650, 500));

        Shortcuts.shortcutLoad(primaryStage.getScene().getRoot(), primaryStage, controller);

        primaryStage.show();
    }


    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        launch(args);
    }


}
