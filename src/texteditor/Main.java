package texteditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Controller controller;
    public static Stage mainStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInterface.fxml"));
        Parent root = loader.load();

        controller = loader.getController();
        controller.setup(primaryStage);

        primaryStage.setTitle("TextEditor");
        primaryStage.setScene(new Scene(root, 650, 500));

      //  Shortcuts.onLoad(primaryStage.getScene().getRoot(), primaryStage, controller);
        TabManagement.onLoad(controller);
        primaryStage.show();
        mainStage = primaryStage;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        launch(args);
    }
    public static void closeProgram() {
        System.exit(0);
    }
}
