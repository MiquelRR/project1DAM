package com;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.model.AdminModel;
import com.model.TaskSkill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class abilitiesController {
    AdminModel adminModel = AdminModel.getAdminModel();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<TaskSkill> abilities;

    @FXML
    private Button addButton;

    @FXML
    private ListView<TaskSkill> allAbilities;

    @FXML
    private Button applyButton;

    @FXML
    private Button removeButton;

    @FXML
    private Label workerLabel;

    @FXML
    void add(ActionEvent event) {
        TaskSkill skill = allAbilities.getSelectionModel().getSelectedItem();
        if (skill == null)
            skill = allAbilities.getItems().get(0);
        App.editedWorker.addSkill(skill);
        allAbilities.getItems().remove(skill);
        abilities.getItems().add(skill);
        abilities.getSelectionModel().select(skill);
        refresh();

    }

    @FXML
    void applyAndClose(ActionEvent event) throws IOException {
        adminModel.modifyWorkerSkills(App.editedWorker);
        App.setRoot("workerProfile");
    }

    @FXML
    void remove(ActionEvent event) {
        TaskSkill skill = abilities.getSelectionModel().getSelectedItem();
        if (skill == null)
            skill = abilities.getItems().get(0);
        App.editedWorker.removeSkill(skill);
        allAbilities.getItems().add(skill);
        abilities.getItems().remove(skill);
        refresh();
    }

    void refresh() {
        addButton.setVisible(!allAbilities.getItems().isEmpty());
        removeButton.setVisible(!abilities.getItems().isEmpty());

    }

    @FXML
    void initialize() {
        workerLabel.setText(App.editedWorker.getFullName() + " - " +
                adminModel.getSectionById(App.editedWorker.getSection()));
        allAbilities.getItems().setAll(adminModel.getTaskTypes());

        // allAbilities.setEditable(false);
        if (App.editedWorker.getAbilities() != null) {
            abilities.getItems().setAll(App.editedWorker.getAbilities());
            for (TaskSkill skill : App.editedWorker.getAbilities()) {
                allAbilities.getItems().remove(skill);
            }
            abilities.getSelectionModel().select(0);
        }
        if (!allAbilities.getItems().isEmpty())
                allAbilities.getSelectionModel().select(0);

        // abilities.setEditable(false);
        refresh();

        assert abilities != null : "fx:id=\"abilities\" was not injected: check your FXML file 'workerAbilities.fxml'.";
        assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'workerAbilities.fxml'.";
        assert allAbilities != null
                : "fx:id=\"allAbilities\" was not injected: check your FXML file 'workerAbilities.fxml'.";
        assert applyButton != null
                : "fx:id=\"applyButton\" was not injected: check your FXML file 'workerAbilities.fxml'.";
        assert removeButton != null
                : "fx:id=\"removeButton\" was not injected: check your FXML file 'workerAbilities.fxml'.";
        assert workerLabel != null
                : "fx:id=\"workerLabel\" was not injected: check your FXML file 'workerAbilities.fxml'.";

    }

}
