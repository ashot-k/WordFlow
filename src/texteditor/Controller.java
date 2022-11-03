package texteditor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import sun.security.util.ArrayUtil;

import javax.xml.soap.Text;
import java.io.*;
import java.util.ArrayList;

public class Controller {

    //TODO
    // -TAB MANAGEMENT
    // -PRINT
    // -ZOOM SLIDER FUNCTIONALITY
    // -ADD POPUP ON EXIT
    // -OPEN RECENT TAB
    // -SEARCH TAB
    // -SHORTCUTS
    // -FONT CONFIGURATION


    //REFERENCES TO FXML ELEMENTS


    @FXML
    VBox mainContainer;
    @FXML
    ToolBar toolBar;
    @FXML
    CheckMenuItem toolBarOption;
    @FXML
    GridPane utilitiesBar;
    @FXML
    CheckMenuItem utilitiesBarOption;
    @FXML
    TabPane tabs;

    // FILE CHOOSER FOR OPENING AND SAVING FILES
    FileChooser fileChooser = new FileChooser();
    //REFERENCES TO CURRENT TAB NODES
    Tab currentTab;
    TextArea currentTextArea;
    File currentFile;
    ArrayList<String> paths = new ArrayList<>();

    public void refresh() {
        currentTab = tabs.getSelectionModel().getSelectedItem();
        //get current tab text
        currentTextArea = (TextArea) (((HBox) currentTab.getContent()).getChildren().get(0));

        //get current tab file path
        String path = (((Label) (((HBox) currentTab.getContent()).getChildren().get(1))).getText());
        //check if file path exists
        if(!path.isEmpty())
            currentFile = new File(((Label) (((HBox) currentTab.getContent()).getChildren().get(1))).getText());
        System.out.println(currentFile);
    }



    //untitled tab
    public void newTab() {
        tabs.getTabs().add(createNewTab("Untitiled"));
        tabs.getSelectionModel().selectLast();
        currentTab = tabs.getSelectionModel().getSelectedItem();
    }
    //titled tab opened through file
    public void newTab(String name, String filePath) {
        tabs.getTabs().add(createNewTab(name,filePath));
        tabs.getSelectionModel().selectLast();
        currentTab = tabs.getSelectionModel().getSelectedItem();
    }

    public Tab createNewTab(String name) {
        Tab newTab = new Tab(name);

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER);
        content.prefHeight(200.0);
        content.prefWidth(200.0);

        TextArea txt = new TextArea();
        HBox.setHgrow(txt, Priority.SOMETIMES);
        Label path = new Label();

        content.getChildren().add(txt);
        content.getChildren().add(path);


        newTab.setContent(content);

        return newTab;
    }
    public Tab createNewTab(String name, String filePath) {
        Tab newTab = new Tab(name);

       HBox content = new HBox();
        content.setAlignment(Pos.CENTER);
        content.prefHeight(200.0);
        content.prefWidth(200.0);

        TextArea txt = new TextArea();
        HBox.setHgrow(txt, Priority.SOMETIMES);

        Label path = new Label();

        path.setVisible(false);
        path.setText(filePath);

        path.minWidth(0);
        path.prefWidth(0);
        path.maxWidth(0);


        content.getChildren().add(txt);
        content.getChildren().add(path);

        newTab.setContent(content);

        return newTab;
    }
    public TextArea getCurrentTabText() {
        return ((TextArea) ((HBox) currentTab.getContent()).getChildren());
    }
    public File checkForPath(){
        String check = ((Label) (((HBox) currentTab.getContent()).getChildren().get(1))).getText();
        if(check.isEmpty())
            return null;
        else
            return new File(check);

    }


    //FILE TAB BUTTONS
    public void open() {
        Window stage = mainContainer.getScene().getWindow();

        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Document ", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);

        if (file == null) return;
        //put text from file to textarea
        newTab(file.getName(), file.getAbsolutePath());
        refresh();


        currentTextArea.setText(Utilities.readFile(file));
    }

    public void save() throws FileNotFoundException {
        System.out.println("called");
        // write on the same file if currently editing it
        File f = checkForPath();
        if (f != null)
            Utilities.writeFile(f, currentTextArea);
    }

    public void saveAs() throws FileNotFoundException {
        Window stage = getCurrentTabText().getScene().getWindow();
        //choose file destination
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Document ", "*.txt"));

        File f = checkForPath();
        if (checkForPath() != null) fileChooser.setInitialFileName(f.getName());
        File file = fileChooser.showSaveDialog(stage);

        //try to create text file at destination
        Utilities.writeFile(file, currentTextArea);
    }

    public void close() throws IOException {
        tabs.getTabs().remove(tabs.getSelectionModel().getSelectedItem());
        refresh();
    }

    //EDIT TAB BUTTONS

    //FORMAT TAB BUTTONS

    //VIEW TAB BUTTONS
    public void toggleToolBar(ActionEvent e) {
        if (!toolBarOption.isSelected())
            mainContainer.getChildren().remove(toolBar);
        else
            mainContainer.getChildren().add(1, toolBar);
    }

    public void toggleUtilities() {
        if (!utilitiesBarOption.isSelected())
            mainContainer.getChildren().remove(utilitiesBar);
        else
            mainContainer.getChildren().add(3, utilitiesBar);
    }



}