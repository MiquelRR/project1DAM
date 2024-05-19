package com;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class editTypeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<?> addDepChoice;

    @FXML
    private Button addRemoveButton;

    @FXML
    private Label depAddLabel;

    @FXML
    private Label depRemoveLabel;

    @FXML
    private Label employeeName;

    @FXML
    private Label employeeName1;

    @FXML
    private Button exitButton;

    @FXML
    private Label prova;

    @FXML
    private ChoiceBox<?> remDepChoice;

    @FXML
    private Button saveButton;

    @FXML
    private Button taskFileBUtton;

    @FXML
    private Label taskNameField;

    @FXML
    private Button typeFolderButton;

    @FXML
    private Label typeNameField;

    @FXML
    void chooseTypeFolder(ActionEvent event) {

    }

    @FXML
    void exitButtonPressed(ActionEvent event) {

    }

    @FXML
    void mouseClick(MouseEvent event) {
        System.out.println(event.getSource().toString());

    }

    @FXML
    void saveButtonPressed(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert addDepChoice != null : "fx:id=\"addDepChoice\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert addRemoveButton != null : "fx:id=\"addRemoveButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert depAddLabel != null : "fx:id=\"depAddLabel\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert depRemoveLabel != null : "fx:id=\"depRemoveLabel\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert employeeName != null : "fx:id=\"employeeName\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert employeeName1 != null : "fx:id=\"employeeName1\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert prova != null : "fx:id=\"prova\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert remDepChoice != null : "fx:id=\"remDepChoice\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert taskFileBUtton != null : "fx:id=\"taskFileBUtton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert taskNameField != null : "fx:id=\"taskNameField\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert typeFolderButton != null : "fx:id=\"typeFolderButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
        assert typeNameField != null : "fx:id=\"typeNameField\" was not injected: check your FXML file 'typeEdit.fxml'.";

    }

}
