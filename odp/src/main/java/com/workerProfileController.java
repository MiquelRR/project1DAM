package com;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.model.AdminModel;
import com.model.Rank;
import com.model.Section;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class workerProfileController {
    AdminModel adminModel = AdminModel.getaAdminModel();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox activeChecbox;

    @FXML
    private TextField adressField;

    @FXML
    private Button applyButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField mailField;

    @FXML
    private TextField nameField;

    @FXML
    private Button nextButton;

    @FXML
    private TextField nikField;

    @FXML
    private TextField otherField;

    @FXML
    private TextField passField;

    @FXML
    private Button prevButton;

    @FXML
    private ChoiceBox<Rank> rankChoice;

    @FXML
    private Button returnButton;

    @FXML
    private ChoiceBox<Section> sectionChoice;

    @FXML
    private TextField ssDniField;

    @FXML
    private TextField ssField;

    @FXML
    private TextField telField;

    @FXML
    void applyButtonPressed(ActionEvent event) {

    }

    @FXML
    void chooseWorkerFolder(ActionEvent event) {

    }

    @FXML
    void next(ActionEvent event) {

    }

    @FXML
    void prev(ActionEvent event) {

    }

    @FXML
    void returnButtonPressed(ActionEvent event) {

    }

    @FXML
    void toAbilities(ActionEvent event) {

    }

    @FXML
    void toggleActive(ActionEvent event) {

    }

    @SuppressWarnings("exports")
    public void setDefaults(Rank rank, Section section){
        sectionChoice.setValue(section);
        rankChoice.setValue(rank);
    }

    @FXML
    void initialize() {
        sectionChoice.getItems().setAll(adminModel.getSections());
        rankChoice.getItems().setAll(adminModel.getRanks());
        sectionChoice.getSelectionModel().select(App.getDefaulSection());
        rankChoice.getSelectionModel().select(App.getDefaultRank());
        datePicker.setValue(LocalDate.now());
        prevButton.setDisable(true);
        nextButton.setDisable(true);
        nameField.requestFocus();
        assert activeChecbox != null : "fx:id=\"activeChecbox\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert adressField != null : "fx:id=\"adressField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert applyButton != null : "fx:id=\"applyButton\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert datePicker != null : "fx:id=\"datePicker\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert mailField != null : "fx:id=\"mailField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert nameField != null : "fx:id=\"nameField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert nextButton != null : "fx:id=\"nextButton\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert nikField != null : "fx:id=\"nikField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert otherField != null : "fx:id=\"otherField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert passField != null : "fx:id=\"passField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert prevButton != null : "fx:id=\"prevButton\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert rankChoice != null : "fx:id=\"rankChoice\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert returnButton != null : "fx:id=\"returnButton\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert sectionChoice != null : "fx:id=\"sectionChoice\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert ssDniField != null : "fx:id=\"ssDniField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert ssField != null : "fx:id=\"ssField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert telField != null : "fx:id=\"telField\" was not injected: check your FXML file 'workerProfile.fxml'.";

    }

}
