package texteditor;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AlertBox {


    public static void closeTabsCheck(Controller controller, String title, String message, String file){
        Stage alertWindow = new Stage();
        //block other windows until popup is closed
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setResizable(false);

        VBox labels = new VBox(10);
        labels.setAlignment(Pos.CENTER);

        Label popupText = new Label(message);
        popupText.setAlignment(Pos.CENTER);
        labels.getChildren().addAll(popupText);


        Button closeButton = new Button("Yes");
        closeButton.setOnAction(event ->
                Main.closeProgram()
        );

        Button noButton = new Button("No");
        noButton.setOnAction(event -> {
            alertWindow.close();
        });

        VBox layout = new VBox(5);
        HBox buttons = new HBox(10);
        buttons.setStyle("-fx-background-color:  #E0E0E0;");
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(closeButton, noButton);

        layout.getChildren().addAll(labels, buttons);
        layout.setAlignment(Pos.CENTER);

        VBox.setVgrow(buttons, Priority.ALWAYS);
        VBox.setVgrow(labels, Priority.ALWAYS);


        Scene scene = new Scene(layout, 350,100);

        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }
    public static void exitSaveCheck(Controller controller, String title, String message, String file) {
        Stage alertWindow = new Stage();
        //block other windows until popup is closed
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setResizable(false);

        VBox labels = new VBox(10);
        labels.setAlignment(Pos.CENTER);

        Label popupText = new Label(message);
        popupText.setAlignment(Pos.CENTER);
        Label fileName = new Label(file);
        fileName.setTextAlignment(TextAlignment.CENTER);
        fileName.setWrapText(true);
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
            Main.closeProgram();
        });

        VBox layout = new VBox(5);
        HBox buttons = new HBox(10);
        buttons.setStyle("-fx-background-color:  #E0E0E0;");
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(saveButton, noButton, cancelButton);

        layout.getChildren().addAll(labels, buttons);
        layout.setAlignment(Pos.CENTER);

        VBox.setVgrow(buttons, Priority.ALWAYS);
        VBox.setVgrow(labels, Priority.ALWAYS);


        Scene scene = new Scene(layout, 350,120);

        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }
    public static void noPrinterFound(String title, String message){
        Stage alertWindow = new Stage();
        //block other windows until popup is closed
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setResizable(false);

        VBox v = new VBox();
        Label alertmsg = new Label(message);
        alertmsg.setTextAlignment(TextAlignment.CENTER);
        VBox.setVgrow(alertmsg, Priority.ALWAYS);

        v.setAlignment(Pos.CENTER);
        v.getChildren().add(alertmsg);

        alertWindow.setScene(new Scene(v, 400, 120));
        alertWindow.showAndWait();
    }

}
