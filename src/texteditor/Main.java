package texteditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
   private static Stage mainStage;
   public static Stage getMainStage(){
       return mainStage;
   }
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainInterface.fxml"));

        primaryStage.setTitle("TextEditor");
        primaryStage.setScene(new Scene(root,650,500));
        primaryStage.show();

        mainStage = primaryStage;
    }
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        launch(args);
    }
}
