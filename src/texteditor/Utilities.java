package texteditor;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class Utilities {

    public static void writeFile(File file, TextArea txt) throws FileNotFoundException {
        if (file != null)
            try (PrintWriter output = new PrintWriter(file)) {
                output.println(txt.getText());
            }
    }

    public static String readFile(File file) {
        StringBuffer textFromFile = new StringBuffer("");

        if (file != null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();

                while (line != null) {
                    textFromFile.append(line);
                    textFromFile.append(System.lineSeparator());
                    line = br.readLine();
                }
            } catch (IOException err) {
                System.out.println(err);
            }
        }
        return textFromFile.toString();
    }

    public static File openFile(FileChooser fileChooser, Stage stage) {
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Document ", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }
    public static File saveFile(FileChooser fileChooser, Stage stage){
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Document ", "*.txt"));

        File file = fileChooser.showSaveDialog(stage);
        return file;
    }

}
