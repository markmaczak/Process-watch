package com.codecool.processwatch.gui;

import com.codecool.processwatch.queries.SelectUser;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static javafx.collections.FXCollections.observableArrayList;


public class FxMain extends Application {
    private static final String TITLE = "Process Watch";

    private App app;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws FileNotFoundException {
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image("https://findicons.com/files/icons/770/token_dark/128/task_manager.png"));

        ObservableList<ProcessView> displayList = observableArrayList();
        app = new App(displayList);
        // TODO: Factor out the repetitive code
        var tableView = new TableView<ProcessView>(displayList);


        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        var pidColumn = new TableColumn<ProcessView, Long>("Process ID");
        pidColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, Long>("pid"));

        var parentPidColumn = new TableColumn<ProcessView, Long>("Parent Process ID");
        parentPidColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, Long>("parentPid"));

        var userNameColumn = new TableColumn<ProcessView, String>("Owner");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("userName"));

        var processNameColumn = new TableColumn<ProcessView, String>("Name");
        processNameColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("processName"));

        var argsColumn = new TableColumn<ProcessView, String>("Arguments");
        argsColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("args"));

        tableView.getColumns().add(pidColumn);
        tableView.getColumns().add(parentPidColumn);
        tableView.getColumns().add(userNameColumn);
        tableView.getColumns().add(processNameColumn);
        tableView.getColumns().add(argsColumn);


        var refreshButton = new Button("REFRESH");
        refreshButton.setOnAction(ignoreEvent -> System.out.println("Button pressed"));
        refreshButton.setOnAction(ignoreEvent -> app = new App(displayList));
        refreshButton.setTooltip(new Tooltip("Refresh processes"));

        Button aboutButton = new Button("ABOUT");
        aboutButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        final Stage dialog = new Stage();
                        dialog.setTitle("About");
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(primaryStage);

                        VBox dialogVbox = new VBox();

                        String about = "";
                        try {
                            File myObj = new File("src/main/java/com/codecool/processwatch/gui/about.txt");
                            Scanner reader = new Scanner(myObj);

                            while (reader.hasNextLine()) {
                                String data = reader.nextLine();
                                about += data + " ";
                            }

                            reader.close();
                        }
                        catch (FileNotFoundException e) {
                            System.out.println(e);
                        }

                        TextArea text = new TextArea(about);
                        text.setWrapText(true);
                        text.setEditable(false);
                        dialogVbox.getChildren().add(text);

                        Scene dialogScene = new Scene(dialogVbox, 500, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                });
        aboutButton.setTooltip(new Tooltip("Get information about the application"));

        Label filterLabel = new Label("Search User:");
        Button submitButton = new Button("SEARCH");
        submitButton.setTooltip(new Tooltip("Search by username"));
        TextField filterTextField = new TextField();
        filterTextField.setPromptText("Type username here...");

        Button deleteButton = new Button("DELETE");
        deleteButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Stream<ProcessHandle> handle = ProcessHandle.allProcesses();
                        ObservableList<ProcessView> rows = tableView.getSelectionModel().getSelectedItems();
                        List<Long> processesToDestroy = new ArrayList<>();
                        rows.forEach(r -> processesToDestroy.add(r.getPid()));

                        handle.forEach(h -> {
                            for (int d = 0; d < processesToDestroy.size(); d++) {
                                if (h.pid() == processesToDestroy.get(d)) {
                                    h.destroy();
                                }
                            }
                        });

                        app.refresh();
                    }
                }
        );
        deleteButton.setTooltip(new Tooltip("Kill selected processes"));



        CheckBox cb = new CheckBox("Select All");
        cb.setStyle("-fx-text-fill : #cacaca; -fx-font-family: Arial");
        cb.setIndeterminate(false);

        cb.selectedProperty().addListener(new ChangeListener<Boolean>() {

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                tableView.requestFocus();
                tableView.getFocusModel().focus(0);
                tableView.getSelectionModel().selectAll();
                // var test = tableView.getSelectionModel().getSelectedItems();
                // test.forEach(t -> System.out.println(t));
            }else{
                tableView.getSelectionModel().clearSelection();
            }
        }
        });


        var topMenu = new HBox(10);
        Image image = new Image("https://findicons.com/files/icons/770/token_dark/128/task_manager.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        topMenu.setStyle("-fx-background-color: #4f4f4f; -fx-min-height: 50px; -fx-alignment: center");
        aboutButton.setStyle("-fx-background-color: #66cd60; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: #2a531b");
        refreshButton.setStyle("-fx-background-color: #66cd60; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: #2a531b");
        deleteButton.setStyle("-fx-background-color: #66cd60; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: #2a531b");
        filterTextField.setStyle("-fx-background-color: #989898; -fx-text-fill: #7DFF77");
        submitButton.setStyle("-fx-background-color: #66cd60; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: #2a531b");
        filterLabel.setStyle("-fx-text-fill : #cacaca; -fx-font-family: Arial");

        topMenu.getChildren().addAll(imageView, aboutButton, refreshButton, deleteButton, filterLabel, filterTextField, submitButton);
        submitButton.setOnAction(e ->
                {
                    String result = filterTextField.getText();
                    SelectUser filtered = new SelectUser();
                    filtered.setFilterUser(result);
                    app.setQuery(filtered);
                }
        );
        topMenu.getChildren().addAll(cb);

        var center = new VBox();
        center.getChildren().addAll(tableView);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setCenter(center);

        var scene = new Scene(borderPane, 720, 576);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
