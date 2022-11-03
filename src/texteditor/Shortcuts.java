package texteditor;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;

public class Shortcuts {
    @FXML
    static
    Button saveButton;



    public static void shortcutSetup(Parent scene, Stage primaryStage){

        KeyCombination combination = new KeyCodeCombination(KeyCode.W,KeyCodeCombination.SHIFT_DOWN);

        scene.setOnKeyPressed(event -> {
            if (combination.match(event)){






            }

        });


    }

}
