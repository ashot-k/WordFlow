package texteditor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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
    MenuBar menuBar;
    @FXML
    ToolBar toolBar;
    @FXML
    CheckMenuItem toolBarViewOption;
    @FXML
    GridPane utilitiesBar;
    @FXML
    CheckMenuItem utilitiesViewOption;
    @FXML
    TabPane tabs;


    // FILE CHOOSER FOR OPENING AND SAVING FILES
    private FileChooser fileChooser = new FileChooser();
    //REFERENCE TO CURRENT TAB
    private Tab currentTab;
    private Stage primaryStage;



    @FXML
    public void initialize(URL location, ResourceBundle resources) {
          setupEvents();
    }
    private void setupEvents() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();
        //insert all menu items to arraylist
        for (Object node : menuBar.getMenus()) {
            if (node instanceof MenuItem)
                menuItems.add((MenuItem) node);
            else if (node instanceof Menu) {
                for (MenuItem m : ((Menu) node).getItems())
                    menuItems.add(m);
            }
        }
        //add events to each menuItem
        for (MenuItem menuItem : menuItems) {
            menuItem.setOnAction(event -> {
                try {
                    menuManager(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

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


    public void menuManager(ActionEvent e) throws IOException {
        refresh();
        String menuName = ((MenuItem)e.getTarget()).getId();
        System.out.println(menuName);

        if(menuName!= null)
        switch (menuName) {
            case "newMenu":
                newTab();
                break;
            case "openMenu":
                open();
                break;
            case "openRecentMenu":
                break;
            case "saveMenu":
                save();
                break;
            case "saveAsMenu":
                saveAs();
                break;
            case "closeMenu":
                close();
                break;
            case"toolBarViewOption":
                toggleToolBar(e);
                break;
            case "utilitiesViewOption":
                toggleUtilities();
                break;
        }

        refresh();
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
        // write on the same file if currently editing it
        File f = getCurrentPath();
        if (f != null)
            Utilities.writeFile(f, getCurrentTextArea());
        else
            saveAs();
    }

    public void saveAs() throws FileNotFoundException {
        if (tabs.getTabs().isEmpty()) return;
        Window stage = mainContainer.getScene().getWindow();
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
    }

    //EDIT TAB BUTTONS

    //FORMAT TAB BUTTONS

    //VIEW TAB BUTTONS
    public void toggleToolBar(ActionEvent e) {
        if (!toolBarViewOption.isSelected())
            mainContainer.getChildren().remove(toolBar);
        else
            mainContainer.getChildren().add(1, toolBar);
    }

    public void toggleUtilities() {
        if (!utilitiesViewOption.isSelected())
            mainContainer.getChildren().remove(utilitiesBar);
        else
            mainContainer.getChildren().add(3, utilitiesBar);
    }



}