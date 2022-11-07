package texteditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

    //TODO
    // -SEARCH MENU
    // -EDIT MENU
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
    MenuItem saveMenu;
    @FXML
    MenuItem saveAsMenu;
    @FXML
    MenuItem closeMenu;

    @FXML
    Menu openRecentMenu;
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
    @FXML
    Slider zoomSlider;
    @FXML
    Label zoomAmount;

    // FILE CHOOSER FOR OPENING AND SAVING FILES
    private FileChooser fileChooser = new FileChooser();
    //REFERENCE TO CURRENT TAB
    private Tab currentTab;
    private HashMap<MenuItem, Tab> recentlyOpened = new HashMap<>();

    public void setupEvents(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            this.exit();
        });

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
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                zoomAmount.textProperty().setValue(String.valueOf(newValue.intValue()));
            }
        });
    }

    public void menuManager(ActionEvent e) throws IOException{
        String menuName = ((MenuItem) e.getTarget()).getId();
        System.out.println(menuName);
        refresh();
        if (menuName != null)
            switch (menuName) {
                case "newMenu":
                    TabManagement.openTab(tabs);
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
                case "print":
                    print();
                    break;
                case "closeMenu":
                    close();
                    break;
                case "toolBarViewOption":
                    toggleToolBar();
                    break;
                case "utilitiesViewOption":
                    toggleUtilities();
                    break;
                case "exitMenu":
                    exit();
                    break;
            }
        refresh();
    }

    public void refresh() {

        currentTab = tabs.getSelectionModel().getSelectedItem();
        if (currentTab == null) {

            saveMenu.setDisable(true);
            saveAsMenu.setDisable(true);
            closeMenu.setDisable(true);
        } else {
            saveMenu.setDisable(false);
            saveAsMenu.setDisable(false);
            closeMenu.setDisable(false);
        }

    }

    //FILE TAB BUTTONS
    public void open() {
        Window stage = mainContainer.getScene().getWindow();

        File file = Utilities.openFile(fileChooser, (Stage) stage);
        if (file == null) return;

        Tab tab = TabManagement.createNewTab(file.getName(), file.getAbsolutePath());
        TabManagement.openTab(tabs, tab);

        addToRecentlyOpened(tab);
    }

    public void addToRecentlyOpened(Tab tab) {
        //create new menuitem under open_recent menu
        MenuItem newRecentlyOpened = new MenuItem(tab.getText());
        newRecentlyOpened.setOnAction(event -> TabManagement.openTab(tabs, tab));
        int index = findInRecentMenu(newRecentlyOpened);

        if (index != -1) {
            openRecentMenu.getItems().remove(index);
            openRecentMenu.getItems().add(0, newRecentlyOpened);
        } else if (openRecentMenu.getItems().size() < 5)
            openRecentMenu.getItems().add(0, newRecentlyOpened);
        else {
            openRecentMenu.getItems().remove(openRecentMenu.getItems().size() - 1);
            openRecentMenu.getItems().add(newRecentlyOpened);
        }
    }

    public int findInRecentMenu(MenuItem item) {
        for (int i = 0; i < openRecentMenu.getItems().size(); i++) {
            if (openRecentMenu.getItems().get(i).getText().equals(item.getText())) {
                return i;
            }
        }
        return -1;
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

        //choose file destination // filechooser setup
        File f = getCurrentPath();
        if (f != null) fileChooser.setInitialFileName(f.getName());
        File file = Utilities.saveFile(fileChooser, (Stage) stage);

        //try to create text file at destination
        Utilities.writeFile(file, getCurrentTextArea());

        //update current tab with file path
        if (file != null)
            currentTab.setId(file.getAbsolutePath());
    }

    public void close() throws IOException {
        tabs.getTabs().remove(tabs.getSelectionModel().getSelectedItem());
    }

    public void exit() {
        if (!tabs.getTabs().isEmpty()) {
            if (currentTab.getId() == null)
                AlertBox.exitSaveCheck(this, "Exit", "Do you want to save changes to ", currentTab.getText());
            else
                AlertBox.exitSaveCheck(this, "Exit", "Do you want to save changes to ", currentTab.getId());
        } else
            closeProgram();
    }


    public void closeProgram() {
        System.exit(0);
    }
    public void print() {
        Utilities.print(getCurrentTextArea());
    }
    //EDIT TAB BUTTONS

    //FORMAT TAB BUTTONS

    //VIEW TAB BUTTONS

    public void toggleToolBar() {
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

    // FILE DRAG & DROP EVENTS
    @FXML
    void handleFileOverEvent(DragEvent event) {
        Dragboard db = event.getDragboard();

        if (db.hasFiles()) {
            if (db.getFiles().get(0).getPath().endsWith("txt"))
                event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }

    @FXML
    void handleFileDroppedEvent(DragEvent event) {
        Dragboard db = event.getDragboard();
        File file = db.getFiles().get(0);

        handleSelectedFile(file);
    }

    public void handleSelectedFile(File file) {
        System.out.println("Dropped file: " + file.getAbsolutePath());
        Tab tab = TabManagement.createNewTab(file.getName(), file.getAbsolutePath());
        TabManagement.openTab(tabs, tab);
        addToRecentlyOpened(tab);
    }


    //GETTERS & SETTERS
    public Tab getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(Tab currentTab) {
        this.currentTab = currentTab;
    }

    public MenuBar getMenuBar() {
        return menuBar;
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

}