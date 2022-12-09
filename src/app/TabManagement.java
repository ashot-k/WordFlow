package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.File;

public class TabManagement {
    private static Controller controller;

    public static void onLoad(Controller c) {
        controller = c;
    }

    public static Tab createNewTab() {
        Tab newTab = new Tab("Untitled");
        HBox content = new HBox();
        TextArea txt = new TextArea();

        txt.setWrapText(controller.fontWrapSelection());
        txt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                controller.textCounters();
                newTab.setStyle("-fx-background-color: #91bbff");
            }
        });

        content.setAlignment(Pos.CENTER);
        content.prefHeight(200.0);
        content.prefWidth(200.0);
        HBox.setHgrow(txt, Priority.SOMETIMES);

        content.getChildren().add(txt);
        newTab.setContent(content);

        return newTab;
    }

    public static Tab createNewTab(String name, String filePath) {
        Tab newTab = new Tab(name);
        HBox content = new HBox();
        TextArea txt = new TextArea();
        txt.setWrapText(controller.fontWrapSelection());
        txt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                controller.textCounters();

                newTab.setStyle("-fx-background-color: #91bbff");
            }
        });

        content.setAlignment(Pos.CENTER);
        content.prefHeight(200.0);
        content.prefWidth(200.0);
        HBox.setHgrow(txt, Priority.SOMETIMES);
        newTab.setId(filePath);

        content.getChildren().add(txt);
        newTab.setContent(content);
        txt.setText(Utilities.readFile(new File(filePath)));

        return newTab;
    }

    public static void openTab(TabPane tabs) {
        Tab tab = createNewTab();

        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);
        controller.setCurrentTab(tabs.getSelectionModel().getSelectedItem());
    }

    public static void openTab(TabPane tabs, Tab tab) {
        if (!isTabOpen(tabs, tab))
            tabs.getTabs().add(tab);

        tabs.getSelectionModel().select(findTab(tabs, tab));
        controller.setCurrentTab(tabs.getSelectionModel().getSelectedItem());
    }

    public static boolean isTabOpen(TabPane tabs, Tab tab) {
        for (Tab t : tabs.getTabs()) {
            if (t.getId() != null)
                if (t.getId().equals(tab.getId()))
                    return true;
        }
        return false;
    }

    public static int findTab(TabPane tabs, Tab tab) {
        for (int i = 0; i < tabs.getTabs().size(); i++) {
            if (tabs.getTabs().get(i).getId() != null)
                if (tabs.getTabs().get(i).getId().equals(tab.getId()))
                    return i;
        }
        return -1;
    }

}
