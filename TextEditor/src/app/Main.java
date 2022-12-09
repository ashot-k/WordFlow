package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/fxml/MainInterface.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setup(primaryStage);
        TabManagement.onLoad(controller);

        primaryStage.getIcons().add(new Image("/Resources/icons/app_icon.png"));
        primaryStage.setTitle("TextEditor");
        primaryStage.setScene(new Scene(root, 750, 500));
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
