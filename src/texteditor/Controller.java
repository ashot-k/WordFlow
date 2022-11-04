package texteditor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Controller {

    //TODO
    // -TAB MANAGEMENT DONE!
    // -ADD POPUP ON EXIT
    // -OPEN RECENT TAB
    // -SEARCH TAB
    // -SHORTCUTS
    // -FONT CONFIGURATION
    // -PRINT
    // -ZOOM SLIDER FUNCTIONALITY

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
    //REFERENCE TO CURRENT TAB
    Tab currentTab;

    public void refresh() {
        currentTab = tabs.getSelectionModel().getSelectedItem();
    }

    public TextArea getCurrentTextArea() {
        return (TextArea) (((HBox) currentTab.getContent()).getChildren().get(0));
    }

    public File getCurrentPath() {
        String check = currentTab.getId();
        if (check == null)
            return null;
        else
            return new File(check);
    }

    //create untitled tab
    public void newTab() {
        tabs.getTabs().add(createNewTab("Untitiled"));
        tabs.getSelectionModel().selectLast();
        currentTab = tabs.getSelectionModel().getSelectedItem();
        refresh();
    }

    public void openTab(String name, String filePath) {
        tabs.getTabs().add(createNewTab(name, filePath));
        tabs.getSelectionModel().selectLast();
        currentTab = tabs.getSelectionModel().getSelectedItem();
        refresh();
    }

    //create tab and open file

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


    public void menuManager(ActionEvent e){
        String menuName = ((MenuItem)e.getSource()).getId();
        switch (menuName){
            case "newMenu": newTab(); break;
            case "openMenu": break;
            case "openRecentMenu": break;
            case "saveMenu": break;
            case "saveAsMenu": break;
            case "closeMenu": break;
        }
    }


    //FILE TAB BUTTONS
    public void open() {
        Window stage = mainContainer.getScene().getWindow();

        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Document ", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);

        if (file == null) return;
        openTab(file.getName(), file.getAbsolutePath());
        getCurrentTextArea().setText(Utilities.readFile(file));
    }

    public void save() throws FileNotFoundException {
        if (tabs.getTabs().isEmpty()) return;

        refresh();
        System.out.println("called save()");
        // write on the same file if currently editing it
        File f = getCurrentPath();
        if (f != null)
            Utilities.writeFile(f, getCurrentTextArea());
        else
            saveAs();
    }

    public void saveAs() throws FileNotFoundException {
        Window stage = mainContainer.getScene().getWindow();
        refresh();

        //choose file destination // filerchooser setup
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Document ", "*.txt"));
        File f = getCurrentPath();
        if (f != null) fileChooser.setInitialFileName(f.getName());

        File file = fileChooser.showSaveDialog(stage);
        //try to create text file at destination
        Utilities.writeFile(file, getCurrentTextArea());

        //update current tab with file path
        if (file != null)
            currentTab.setId(file.getAbsolutePath());
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