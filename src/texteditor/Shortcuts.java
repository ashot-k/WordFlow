package texteditor;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Shortcuts {
    public static void shortcutLoad(Parent scene, Stage primaryStage, Controller controller) {

        KeyCombination saveShortcut = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        KeyCombination newTabShortcut = new KeyCodeCombination(KeyCode.T, KeyCodeCombination.CONTROL_DOWN);
        KeyCombination closeShortcut = new KeyCodeCombination(KeyCode.W, KeyCodeCombination.CONTROL_DOWN);
        KeyCombination openShortcut = new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN);

        HashMap<KeyCombination, String> hotkeyFunctions = new HashMap<>();
        hotkeyFunctions.put(saveShortcut, "save");
        hotkeyFunctions.put(newTabShortcut, "newTab");
        hotkeyFunctions.put(closeShortcut, "close");
        hotkeyFunctions.put(openShortcut, "open");

        ArrayList<KeyCombination> shortcutList = new ArrayList<>();
        shortcutList.add(saveShortcut);
        shortcutList.add(newTabShortcut);
        shortcutList.add(closeShortcut);
        shortcutList.add(openShortcut);

        scene.setOnKeyPressed(event -> {

            for (KeyCombination combination : shortcutList) {
                if (combination.match(event)) {
                    String function = hotkeyFunctions.get(combination);
                    try {
                        switch (function) {
                            case "newTab":
                                controller.openTab();
                                break;
                            case "open":
                                controller.open();
                                break;
                            case "save":
                                controller.save();
                                break;
                            case "saveAs":
                                controller.saveAs();
                                break;
                            case "close":
                                controller.close();
                                break;
                        }

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }finally {
                        controller.refresh();
                    }

                }
            }

                /*
                if (saveShortcut.match(event)) {
                    try {
                        controller.save();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } else if (newTabShortcut.match(event)) {
                    controller.newTab();
                } else if (closeShortcut.match(event)) {

                    try {
                        controller.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (openShortcut.match(event)) {
                    controller.open();
                }

                 */
        });
    }
}
