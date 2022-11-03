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

public class Main extends Application {
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInterface.fxml"));


        Parent root = loader.load();
        controller = loader.getController();

        primaryStage.setTitle("TextEditor");
        primaryStage.setScene(new Scene(root, 650, 500));
        primaryStage.show();

        Shortcuts shortcutStart = new Shortcuts();
        shortcutStart.shortcutSetup(primaryStage.getScene().getRoot(), primaryStage);
    }


    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        launch(args);
    }


    public class Shortcuts {


        public void shortcutSetup(Parent scene, Stage primaryStage) {

            KeyCombination combination = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
            scene.setOnKeyPressed(event -> {
                if (combination.match(event)) {
                    try {
                        controller.save();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }


                }
            });


        }
    }


}
