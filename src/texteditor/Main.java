package texteditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
        TabManagement.onLoad(controller);

        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        launch(args);
    }

}
