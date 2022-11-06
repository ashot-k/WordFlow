package texteditor;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class AlertBox {
    public static void exitSaveCheck(Controller controller, String title, String message, String file) {
        Stage alertWindow = new Stage();

        //block other windows until popup is closed
        alertWindow.initModality(Modality.APPLICATION_MODAL);

        alertWindow.setTitle(title);
        alertWindow.setResizable(false);

        HBox labels = new HBox(10);
        labels.setAlignment(Pos.CENTER);

        Label popupText = new Label(message);
        Label fileName = new Label(file);
        labels.getChildren().addAll(popupText, fileName);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> alertWindow.close());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            try {
                controller.save();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            alertWindow.close();
        });

        Button noButton = new Button("No");
        noButton.setOnAction(event -> {
            controller.closeProgram();
        });

        VBox layout = new VBox(15);
        HBox buttons = new HBox(15);
        buttons.setStyle("-fx-background-color:  #E0E0E0;");
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(saveButton, noButton, cancelButton);

        layout.getChildren().addAll(labels, buttons);
        layout.setAlignment(Pos.CENTER);

        VBox.setVgrow(buttons, Priority.ALWAYS);
        VBox.setVgrow(labels, Priority.ALWAYS);


        Scene scene = new Scene(layout, 350,100);

        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }

}
