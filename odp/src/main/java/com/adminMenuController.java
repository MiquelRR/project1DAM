package com;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.model.AdminModel;
import com.model.Rank;
import com.model.Section;
import com.model.TaskType;
import com.model.Worker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class adminMenuController {
    AdminModel adminModel = AdminModel.getaAdminModel();
    final String OK = "✔";
    final String MENU = "☰";
    private String message = "";
    @FXML
    private Label title;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addRankButton;

    @FXML
    private Button addSectionButton;

    @FXML
    private Button editOrNewModelButton1;

    @FXML
    private Button editOrNewOrderButton;

    @FXML
    private Button editOrNewTypeButton;

    @FXML
    private Button editWorkerButton;

    @FXML
    private Button removeTaskButton;

    @FXML
    private ChoiceBox<?> modelChoice;

    @FXML
    private ChoiceBox<TaskType> taskChoice;

    @FXML
    private ChoiceBox<?> orderChoice;

    @FXML
    private ChoiceBox<Rank> rankChooser;

    @FXML
    private TextField rankField;

    @FXML
    private TextField taskField;

    @FXML
    private Button removeSectionButton;

    @FXML
    private Button removeRankButton;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button returnButton;

    @FXML
    private ChoiceBox<Section> sectionChooser;

    @FXML
    private TextField sectionField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField clientField;

    @FXML
    private TextField unitsField;

    @FXML
    private ChoiceBox<?> typeChoice;

    @FXML
    void addRank(ActionEvent event) {
        if (rankField.getText().length() > 1) {
            adminModel.addRank(rankField.getText());
            rankField.setText("");
            addRankButton.setDisable(false);
            addRankButton.setText("+");
            refresh();
        } else {
            addRankButton.setDisable(true);
            rankChooser.setVisible(false);
            rankField.setVisible(true);
            rankField.requestFocus();
            removeRankButton.setVisible(false);
        }

    }

    private void showDialog(String st) {
        if (st == null)
            st = "null";
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Operación no posible");
        alert.setHeaderText("revise las siguientes condiciones");
        alert.setContentText(st);
        alert.showAndWait();
    }

    @FXML
    void addSection(ActionEvent event) {
        if (sectionField.getText().length() > 1) {
            adminModel.addSection(sectionField.getText());
            sectionField.setText("");
            addSectionButton.setDisable(false);
            addSectionButton.setText("+");
            refresh();
        } else {
            addSectionButton.setDisable(true);
            sectionChooser.setVisible(false);
            sectionField.setVisible(true);
            sectionField.requestFocus();
            removeSectionButton.setVisible(false);
        }

    }

    @FXML
    void editWorker(ActionEvent event) throws IOException {
        App.setDefaulSection(sectionChooser.getValue());
        App.setDefaultRank(rankChooser.getValue());
        App.setRoot("workerProfile");

    }

    @FXML
    void addWorker(ActionEvent event) {

    }

    @FXML
    void addTask(ActionEvent event) {

        if (taskField.getText().length() > 1) {
            adminModel.addTask(taskField.getText());
            taskField.setText("");
            addTaskButton.setDisable(false);
            addTaskButton.setText("+");
            refresh();
        } else {
            addTaskButton.setDisable(true);
            taskChoice.setVisible(false);
            taskField.setVisible(true);
            taskField.requestFocus();
            removeTaskButton.setVisible(false);
        }


    }

    @FXML
    void editOrNewModel(ActionEvent event) {

    }

    @FXML
    void editOrNewType(ActionEvent event) {

    }

    @FXML
    void editOrNewOrder(ActionEvent event) {

    }

    @FXML
    void removeTask(ActionEvent event) {
        Boolean result = false;
        if (taskChoice.getValue() != null) {
            result = adminModel.removeTaskType(taskChoice.getValue());

            if (result) {
                taskChoice.getItems().remove(taskChoice.getValue());
                refresh();
            } else
                showDialog("Intentas borrar una tarea en uso");
        }

    }

    @FXML
    void removeType(ActionEvent event) {

    }

    @FXML
    void removeModel(ActionEvent event) {

    }

    @FXML
    void removeRank(ActionEvent event) {
        Boolean result = false;
        if (rankChooser.getValue() != null) {
            result = adminModel.removeRank(rankChooser.getValue());

            if (result) {
                rankChooser.getItems().remove(rankChooser.getValue());
                refresh();
            } else
                showDialog("Intentas borrar una categoria con trabajadores");
        }

    }

    @FXML
    void removeSection(ActionEvent event) {
        Boolean result = false;
        if (sectionChooser.getValue() != null) {
            result = adminModel.removeSection(sectionChooser.getValue());

            if (result) {
                sectionChooser.getItems().remove(sectionChooser.getValue());
                refresh();
            } else
                showDialog("Intentas borrar una sección con trabajadores");
        }
    }

    @FXML
    void returnButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("login");
    }

    @FXML
    void toCalendars(ActionEvent event) {

    }

    @FXML
    void showAddSectionButton(KeyEvent event) {
        addSectionButton.setVisible(true);
        addSectionButton.setDisable(false);
        addSectionButton.setText(OK);
    }

    @FXML
    void showAddRankButton(KeyEvent event) {
        addRankButton.setVisible(true);
        addRankButton.setDisable(false);
        addRankButton.setText(OK);
    }
    @FXML
    void showAddTaskButton(KeyEvent event) {
        addTaskButton.setVisible(true);
        addTaskButton.setDisable(false);
        addTaskButton.setText(OK);
    }

    @FXML
    void refresh() {
        boolean posibleWorkers = true;
        boolean posibleTypes= true;
        if (adminModel.getSections().isEmpty()) {
            removeSectionButton.setVisible(false);
            addSectionButton.setVisible(false);
            sectionField.setVisible(true);
            sectionChooser.setVisible(false);
        } else {
            removeSectionButton.setVisible(true);
            sectionChooser.getItems().setAll(adminModel.getSections());
            sectionChooser.setVisible(true);
            sectionChooser.getSelectionModel().select(sectionChooser.getItems().size() - 1);
            addSectionButton.setVisible(true);
        }

        if (adminModel.getRanks().isEmpty()) {
            posibleWorkers = false;
            removeRankButton.setVisible(false);
            addRankButton.setVisible(false);
            rankField.setVisible(true);
            rankChooser.setVisible(false);
        } else {
            removeRankButton.setVisible(true);
            rankChooser.getItems().setAll(adminModel.getRanks());
            rankChooser.setVisible(true);
            rankChooser.getSelectionModel().select(rankChooser.getItems().size() - 1);
            addRankButton.setVisible(true);
        }

        if (adminModel.getTaskTypes().isEmpty()) {
            posibleTypes = false;
            removeTaskButton.setVisible(false);
            addTaskButton.setVisible(false);
            taskField.setVisible(true);
            taskChoice.setVisible(false);
        } else {
            removeTaskButton.setVisible(true);
            taskChoice.getItems().setAll(adminModel.getTaskTypes());
            taskChoice.setVisible(true);
            taskChoice.getSelectionModel().select(taskChoice.getItems().size() - 1);
            addTaskButton.setVisible(true);
        }


        if (adminModel.getFilteredStaff().isEmpty()) {
            //
        }
        editWorkerButton.setVisible(posibleWorkers);

    }

    @FXML
    void initialize() {
        adminModel.filterStaff(null, null);
        refresh();

       
        assert editOrNewModelButton1 != null
                : "fx:id=\"editOrNewModelButton1\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert editOrNewOrderButton != null
                : "fx:id=\"editOrNewOrderButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert editOrNewTypeButton != null
                : "fx:id=\"editOrNewTypeButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert editWorkerButton != null
                : "fx:id=\"editWorkerButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert modelChoice != null : "fx:id=\"modelChoice\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert orderChoice != null : "fx:id=\"orderChoice\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert rankChooser != null : "fx:id=\"rankChooser\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert rankField != null : "fx:id=\"rankField\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert removeSectionButton != null
                : "fx:id=\"removeSectionButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert removeRankButton != null
                : "fx:id=\"removeSectionButton1\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert returnButton != null : "fx:id=\"returnButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert sectionChooser != null
                : "fx:id=\"sectionChooser\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert sectionField != null : "fx:id=\"sectionField\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert typeChoice != null : "fx:id=\"typeChoice\" was not injected: check your FXML file 'adminMenu.fxml'.";
        

    }

}
