package com;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.model.AdminModel;
import com.model.TaskType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class typeEditController3 {
    AdminModel adminModel = AdminModel.getAdminModel();
    List<TaskType> depChoice;
    TaskType boardSelectedTask;

    @FXML
    private Pane root;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ToggleButton TOGGLE;

    @FXML
    private ChoiceBox<TaskType> addDepChoice;

    @FXML
    private Button addDependButton;

    @FXML
    private Button addTaskButton;

    @FXML
    private Label depAddLabel;

    @FXML
    private Label employeeName1;

    @FXML
    private Button exitButton;

    @FXML
    private TextField itemTime;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField prepTime;

    @FXML
    private Button removeDependButton;

    @FXML
    private Button saveButton;

    @FXML
    private ChoiceBox<TaskType> taskAddChoice;

    @FXML
    private Button taskFileButton;

    @FXML
    private Button typeFolderButton;

    @FXML
    void checkSelectedTask(ActionEvent event) {

    }

    @FXML
    void addDepend(ActionEvent event) {

    }

    @FXML
    void addTask(ActionEvent event) {
        TaskType tt=taskAddChoice.getValue();
        taskAddChoice.getItems().remove(tt);

        ToggleButton t1 = getButton(tt);

        t1.setVisible(true);
        t1.setViewOrder(1);
        System.out.println("KASE");
        root.getChildren().add(t1);

    }

    @FXML
    void chooseTaskFile(ActionEvent event) {

    }

    @FXML
    void chooseTypeFolder(ActionEvent event) {

    }

    @FXML
    void exitButtonPressed(ActionEvent event) {

    }

    @FXML
    void refreshTimes(KeyEvent event) {

    }

    @FXML
    void removeDepend(ActionEvent event) {

    }

    @FXML
    void saveButtonPressed(ActionEvent event) {

    }

    private void refresh() {
        boolean noSel = boardSelectedTask != null;
        addTaskButton.setVisible(!taskAddChoice.getItems().isEmpty());
        addDependButton.setVisible(noSel);
        addDepChoice.setVisible(noSel);
        removeDependButton.setVisible(noSel);
        taskFileButton.setVisible(noSel);
        prepTime.setVisible(noSel);
        itemTime.setVisible(noSel);

    }

    @FXML
    private ToggleButton getButton(TaskType tsk) {
        ToggleButton toggleButton = new ToggleButton(tsk.toString());
        toggleButton.setLayoutX(100);
        toggleButton.setLayoutY(100);
        toggleButton.setPrefWidth(135);
        toggleButton.setPrefHeight(52);
        toggleButton.setId("A0");
        toggleButton.setStyle("-fx-text-fill: #7c7c7c;");
        return toggleButton;
    }

    @FXML
    void initialize() {
        taskAddChoice.getItems().setAll(adminModel.getTaskTypes());
        taskAddChoice.setValue(taskAddChoice.getItems().get(taskAddChoice.getItems().size()-1));
        taskAddChoice.requestFocus();
        boardSelectedTask = null;
        nameLabel.setText(App.editedType.getName());
        depChoice = new ArrayList<>();
        refresh();

        assert TOGGLE != null : "fx:id=\"TOGGLE\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert addDepChoice != null : "fx:id=\"addDepChoice\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert addDependButton != null
                : "fx:id=\"addDependButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert addTaskButton != null
                : "fx:id=\"addTaskButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert depAddLabel != null : "fx:id=\"depAddLabel\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert employeeName1 != null
                : "fx:id=\"employeeName1\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert itemTime != null : "fx:id=\"itemTime\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert nameLabel != null : "fx:id=\"nameLabel\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert prepTime != null : "fx:id=\"prepTime\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert removeDependButton != null
                : "fx:id=\"removeDependButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert taskAddChoice != null
                : "fx:id=\"taskAddChoice\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert taskFileButton != null
                : "fx:id=\"taskFileButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert typeFolderButton != null
                : "fx:id=\"typeFolderButton\" was not injected: check your FXML file 'typeEdit.fxml'.";

    }

}
