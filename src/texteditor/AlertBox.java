package texteditor;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class AlertBox {
    public static void exitSaveCheck(Controller controller, String title, String message) {
        Stage window = new Stage();

        //block other windows until popup is closed
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle(title);
        window.setResizable(false);

        Label popupText = new Label();
        popupText.setText(message);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> window.close());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            try {
                controller.save();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            window.close();
        });

        Button noButton = new Button("No");
        noButton.setOnAction(event -> {
            controller.closeProgram();
        });

        VBox layout = new VBox(15);

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(saveButton, noButton, cancelButton);

        layout.getChildren().addAll(popupText, buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 350,80);
        window.setScene(scene);

        window.showAndWait();
    }

}
