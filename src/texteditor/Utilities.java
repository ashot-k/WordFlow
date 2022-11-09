package texteditor;

import javafx.print.PrinterJob;
import javafx.scene.control.Label;
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
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }

    public static File saveFile(FileChooser fileChooser, Stage stage) {
        fileChooser.setTitle("Save");
        File file = fileChooser.showSaveDialog(stage);
        return file;
    }

    public static void print(TextArea txtArea) {
        printFile(txtArea, Main.mainStage);
    }

    private static void printFile(TextArea txtArea, Stage primaryStage) {
        // Create the PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            AlertBox.noPrinterFound("No printer", "No printer was found on your system");
            return;
        }
        // Show the print setup dialog
        boolean proceed = job.showPrintDialog(primaryStage);
        if (proceed) {
            boolean printed = job.printPage(new Label(txtArea.getText()));
            if (printed) {
                job.endJob();
                System.out.println("printed");
            }
        }
    }

    public static int countWords(String txt) {
        if (txt == null || txt.isEmpty())
            return 0;
        String[] words = txt.split("\\s+");
        return words.length;
    }

    public static void closeProgram() {
        System.exit(0);
    }


}
