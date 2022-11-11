package texteditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Controller {

    //TODO
    // -SHORTCUTS

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
    MenuItem printMenu;
    @FXML
    MenuItem closeMenu;
    @FXML
    Menu openRecentMenu;
    @FXML
    MenuItem fontMenu;
    @FXML
    ToolBar toolBar;
    @FXML
    CheckMenuItem toolBarViewOption;
    @FXML
    ToolBar utilBar;
    @FXML
    CheckMenuItem utilitiesViewOption;
    @FXML
    Label wordCounter;
    @FXML
    Label lineCounter;
    @FXML
    CheckMenuItem fontWrapMenu;
    @FXML
    TabPane tabs;

    @FXML
    TextField newWord;

    @FXML
    TextField oldWord;

    @FXML
    Button replaceButton;


    @FXML
    public  void replaceWindow()throws  IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReplaceWindow.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Replace");
        stage.setScene(new Scene(root,600,200));


       /* replaceButton.setOnAction(event -> {
             replace(textArea);
            stage.close();
        });*/

        stage.show();

    }

    public void  replace(){
        String oldW = oldWord.getText();
        String newW = newWord.getText();
        String replacedWordString = getCurrentTextArea().getText().replace(oldW,newW);

        getCurrentTextArea().setText(replacedWordString);

    }

   /* public void  replace(TextArea currentTextArea){
        String oldW = oldWord.getText();
        String newW = newWord.getText();
        String replacedWordString = currentTextArea.getText().replace(oldW,newW);
        System.out.println(replacedWordString);
        currentTextArea.setText(replacedWordString);

    }
*/
    // FILE CHOOSER FOR OPENING AND SAVING FILES
    private FileChooser fileChooser = new FileChooser();
    //REFERENCE TO CURRENT TAB
    private static Tab currentTab;

    public void setup(Stage primaryStage) {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Document ", "*.txt"));
        //exit check event
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            this.exit();
        });
        //current tab refresh event
        tabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                refresh();
            }
        });
        refresh();
    }

    public void refresh() {
        currentTab = tabs.getSelectionModel().getSelectedItem();
        if(currentTab != null) textCounters();
        toggleMenus();
    }

    public void toggleMenus() {
        if (currentTab == null) {
            saveMenu.setDisable(true);
            saveAsMenu.setDisable(true);
            closeMenu.setDisable(true);
            printMenu.setDisable(true);
            fontMenu.setDisable(true);
        } else {
            saveMenu.setDisable(false);
            saveAsMenu.setDisable(false);
            closeMenu.setDisable(false);
            printMenu.setDisable(false);
            fontMenu.setDisable(false);
        }
    }

    //FILE MENU CALLS
    public void newTab() {
        TabManagement.openTab(tabs);
    }

    public void open() {
        Window stage = mainContainer.getScene().getWindow();

        File file = Utilities.openFile(fileChooser, (Stage) stage);
        if (file == null) return;

        Tab tab = TabManagement.createNewTab(file.getName(), file.getAbsolutePath());
        tab.setStyle("-fx-base: #EAEAEA");
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
        if (currentTab == null) return;
        // write on the same file if currently editing it
        File f = getCurrentPath();
        if (f != null) {
            Utilities.writeFile(f, getCurrentTextArea());
            currentTab.setStyle("-fx-base: #EAEAEA");
        } else
            saveAs();
    }

    public void saveAs() throws FileNotFoundException {
        if (currentTab == null) return;
        Window stage = mainContainer.getScene().getWindow();

        //choose file destination // filechooser setup
        File f = getCurrentPath();
        if (f != null) {
            fileChooser.setInitialFileName(f.getName());
            fileChooser.setInitialDirectory(f.getParentFile());
        }
        File file = Utilities.saveFile(fileChooser, (Stage) stage);

        //try to create text file at destination
        Utilities.writeFile(file, getCurrentTextArea());

        //update current tab with file path
        if (file != null)
            currentTab.setId(file.getAbsolutePath());
        currentTab.setStyle("-fx-base: #EAEAEA");
    }

    public void close() throws IOException {
        tabs.getTabs().remove(tabs.getSelectionModel().getSelectedItem());
    }

    public void print() {
        Utilities.print(getCurrentTextArea());
    }

    public void exit() {
        if (!tabs.getTabs().isEmpty()) {
            if (tabs.getTabs().size() > 1)
                AlertBox.closeTabsCheck(this, "Exit", "You may have unsaved changes in your tabs.\nAre you sure you want to exit?", null);
            else if (currentTab.getId() == null)
                AlertBox.exitSaveCheck(this, "Exit", "Do you want to save changes to ", currentTab.getText());
            else
                AlertBox.exitSaveCheck(this, "Exit", "Do you want to save changes to ", currentTab.getId());
        } else
            Main.closeProgram();
    }

    //EDIT MENU CALLS
    public void undo() {
        getCurrentTextArea().undo();
    }

    public void redo() {
        getCurrentTextArea().redo();
    }

    public void copy() {
        getCurrentTextArea().copy();
    }

    public void cut() {
        getCurrentTextArea().cut();
    }

    public void paste() {
        getCurrentTextArea().paste();
    }

    public void selectAll() {
        getCurrentTextArea().selectAll();
    }

    public void delete() {
        getCurrentTextArea().deleteText(getCurrentTextArea().getSelection());
    }

    //FORMAT MENU CALLS
    public void fontWrap() {
        if (tabs.getTabs().isEmpty()) return;

        if (fontWrapMenu.isSelected())
            for (Tab t : tabs.getTabs())
                ((TextArea) ((HBox) t.getContent()).getChildren().get(0)).setWrapText(true);
        else
            for (Tab t : tabs.getTabs())
                ((TextArea) ((HBox) t.getContent()).getChildren().get(0)).setWrapText(false);
    }

    public boolean fontWrapSelection() {
        return (fontWrapMenu.isSelected());
    }

    public void fontMenu() {
        Stage stage = new Stage();
        stage.setTitle("Font");
        stage.setResizable(false);

        List<String> families = Font.getFamilies();
        ObservableList<String> fontList = FXCollections.observableList(families);

        ComboBox<String> fontSelector = new ComboBox<>();
        fontSelector.getItems().addAll(fontList);
        fontSelector.getSelectionModel().select("Arial");

        ComboBox<String> sizeSelector = new ComboBox<>();
        sizeSelector.getItems().addAll("8", "9", "12", "14", "16", "18", "24", "28", "32", "36", "42", "56", "64", "72");
        sizeSelector.setEditable(true);
        sizeSelector.getSelectionModel().select(2);

        Button confirmButton = new Button("Ok");
        confirmButton.setOnAction(event -> {
            getFontChoice(fontSelector, sizeSelector);
            stage.close();
        });

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(fontSelector, sizeSelector, confirmButton);

        stage.setScene(new Scene(vbox, 300, 150));
        stage.showAndWait();
    }

    private void getFontChoice(ComboBox<String> fontBox, ComboBox<String> sizeBox) {
        String fontChoice = fontBox.getValue();
        String sizeChoice = sizeBox.getValue();

        Font selectedFont = Font.font(fontChoice, FontWeight.NORMAL, Double.parseDouble(sizeChoice));
        getCurrentTextArea().setFont(selectedFont);
    }



    private  void replace(String findtext,String currentText,String replacetext){



    }

    //VIEW MENU CALLS
    public void toggleToolBar() {
        if (!toolBarViewOption.isSelected())
            mainContainer.getChildren().remove(toolBar);
        else
            mainContainer.getChildren().add(1, toolBar);
    }

    public void toggleUtilities() {
        if (!utilitiesViewOption.isSelected())
            mainContainer.getChildren().remove(utilBar);
        else
            mainContainer.getChildren().add(mainContainer.getChildren().size() - 1, utilBar);
    }

    //UTILITIES BAR CALLS
    public void textCounters() {
        if (currentTab == null)
            return;
        int words = Utilities.countWords(getCurrentTextArea().getText());
        int lines = Utilities.countLines(getCurrentTextArea().getText());
        wordCounter.setText(String.valueOf(words));
        lineCounter.setText(String.valueOf(lines));
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

        currentTab.setStyle("-fx-base: #EAEAEA");
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