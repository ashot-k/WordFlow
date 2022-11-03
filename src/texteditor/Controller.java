package texteditor;

import com.sun.prism.shader.AlphaOne_Color_Loader;
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
        String path = currentTab.getId();
        //check if file path exists
        if(path != null)
            currentFile = new File(currentTab.getId());
        System.out.println(currentFile);
    }



    //untitled tab
    public void newTab() {
        tabs.getTabs().add(createNewTab("Untitiled"));
        tabs.getSelectionModel().selectLast();
        currentTab = tabs.getSelectionModel().getSelectedItem();
        refresh();
    }
    //titled tab opened through file
    public void newTab(String name, String filePath) {
        tabs.getTabs().add(createNewTab(name,filePath));
        tabs.getSelectionModel().selectLast();
        currentTab = tabs.getSelectionModel().getSelectedItem();
        refresh();
    }

    public Tab createNewTab(String name) {
        Tab newTab = new Tab(name);
        HBox content = new HBox();
        TextArea txt = new TextArea();
        content.setAlignment(Pos.CENTER);
        content.prefHeight(200.0);
        content.prefWidth(200.0);
        HBox.setHgrow(txt, Priority.SOMETIMES);

        content.getChildren().add(txt);
        newTab.setContent(content);

        return newTab;
    }
    public Tab createNewTab(String name, String filePath) {
        Tab newTab = new Tab(name);
        HBox content = new HBox();
        TextArea txt = new TextArea();
        content.setAlignment(Pos.CENTER);
        content.prefHeight(200.0);
        content.prefWidth(200.0);
        HBox.setHgrow(txt, Priority.SOMETIMES);

        newTab.setId(filePath);

        content.getChildren().add(txt);
        newTab.setContent(content);

        return newTab;
    }
    public TextArea getCurrentTabText() {
        return ((TextArea) ((HBox) currentTab.getContent()).getChildren().get(0));
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
        refresh();
        System.out.println("called");
        // write on the same file if currently editing it
        File f = checkForPath();
        if (f != null)
            Utilities.writeFile(f, currentTextArea);
        else
            saveAs();
    }

    public void saveAs() throws FileNotFoundException {

        Window stage = getCurrentTabText().getScene().getWindow();
        //choose file destination
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Document ", "*.txt"));


        File f = checkForPath();
        if (f != null) fileChooser.setInitialFileName(f.getName());

        File file = fileChooser.showSaveDialog(stage);
        //try to create text file at destination
        Utilities.writeFile(file, currentTextArea);

        currentTab.setId(file.getAbsolutePath());
        refresh();
    }
    public File checkForPath(){
        String check = currentTab.getId();
        if(check == null)
            return null;
        else
            return new File(check);
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