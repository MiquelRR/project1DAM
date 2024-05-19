package com;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class adminMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addSctionButton;

    @FXML
    private Button addSctionButton1;

    @FXML
    private Button editOrNewModelButton1;

    @FXML
    private Button editOrNewOrderButton;

    @FXML
    private Button editOrNewTypeButton;

    @FXML
    private Button editOrNewWorkerButton;

    @FXML
    private ChoiceBox<?> modelChoice;

    @FXML
    private ChoiceBox<?> orderChoice;

    @FXML
    private ChoiceBox<?> rankChooser;

    @FXML
    private TextField rankField;

    @FXML
    private Button removeSectionButton;

    @FXML
    private Button removeSectionButton1;

    @FXML
    private Button returnButton;

    @FXML
    private ChoiceBox<?> sectionChooser;

    @FXML
    private TextField sectionField;

    @FXML
    private ChoiceBox<?> typeChoice;

    @FXML
    private ChoiceBox<?> workerChoice;

    @FXML
    void addRank(ActionEvent event) {

    }

    @FXML
    void addSection(ActionEvent event) {

    }

    @FXML
    void editOrNew(ActionEvent event) {

    }

    @FXML
    void editOrNewModel(ActionEvent event) {

    }

    @FXML
    void editOrNewType(ActionEvent event) {

    }

    @FXML
    void removeRank(ActionEvent event) {

    }

    @FXML
    void removeSection(ActionEvent event) {

    }

    @FXML
    void returnButtonPressed(ActionEvent event) {

    }

    @FXML
    void toAbilities(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert addSctionButton != null : "fx:id=\"addSctionButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert addSctionButton1 != null : "fx:id=\"addSctionButton1\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert editOrNewModelButton1 != null : "fx:id=\"editOrNewModelButton1\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert editOrNewOrderButton != null : "fx:id=\"editOrNewOrderButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert editOrNewTypeButton != null : "fx:id=\"editOrNewTypeButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert editOrNewWorkerButton != null : "fx:id=\"editOrNewWorkerButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert modelChoice != null : "fx:id=\"modelChoice\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert orderChoice != null : "fx:id=\"orderChoice\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert rankChooser != null : "fx:id=\"rankChooser\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert rankField != null : "fx:id=\"rankField\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert removeSectionButton != null : "fx:id=\"removeSectionButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert removeSectionButton1 != null : "fx:id=\"removeSectionButton1\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert returnButton != null : "fx:id=\"returnButton\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert sectionChooser != null : "fx:id=\"sectionChooser\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert sectionField != null : "fx:id=\"sectionField\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert typeChoice != null : "fx:id=\"typeChoice\" was not injected: check your FXML file 'adminMenu.fxml'.";
        assert workerChoice != null : "fx:id=\"workerChoice\" was not injected: check your FXML file 'adminMenu.fxml'.";

    }

}
