package com;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.model.AdminModel;
import com.model.Rank;
import com.model.Section;
import com.model.Worker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class workerProfileController {
    private static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String SS = "^\\d{8}$";
    private static final String DNI = "^\\d{8}[A-Za-z]$";
    private static final String NO_SPACES = "^\\S*$";

    public static boolean validate(String string, String patt) {
        Pattern pattern = Pattern.compile(patt);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    private static int workerIdx;

    AdminModel adminModel = AdminModel.getaAdminModel();
    Worker worker;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox activeCheckbox;

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
    private void showWorker(Worker worker) {
        nikField.setText((worker.getUserName() == null) ? "" : worker.getUserName());
        nikField.setText((worker.getUserName() == null) ? "" : worker.getUserName());
        nameField.setText((worker.getFullName() == null) ? "" : worker.getFullName());
        passField.setText((worker.getPasswd() == null) ? "" : worker.getPasswd());
        if (worker.getSince() == null)
            datePicker.setValue(LocalDate.now());
        else
            datePicker.setValue(worker.getSince());
        ssField.setText((worker.getSsNum() == null) ? "" : worker.getSsNum());
        ssDniField.setText((worker.getDni() == null) ? "" : worker.getDni());

        if (worker.getSection() == null) {
            App.setWorkerProfModeAdd(true);
            sectionChoice.setValue(App.getDefaulSection());
        } else
            sectionChoice.setValue(adminModel.getSectionById(worker.getSection()));

        if (worker.getRank() == null)
            rankChoice.setValue(App.getDefaultRank());
        else
            rankChoice.setValue(adminModel.getRankById(worker.getRank()));
        adressField.setText((worker.getAddress() == null) ? "" : worker.getAddress());
        telField.setText((worker.getTelNum() == null) ? "" : worker.getTelNum());
        mailField.setText((worker.getMail() == null) ? "" : worker.getMail());
        otherField.setText((worker.getContact() == null) ? "" : worker.getContact());
        refresh();
    }

    @FXML
    void applyButtonPressed(ActionEvent event) {
        String alert = "";
        Boolean required = true;
        if (nameField.getText().isBlank()) {
            alert += "Rellena el Nombre completo\n";
            required = false;
        }
        ;
        if (!validate(ssField.getText(), SS)) {
            alert += "El numero de la ss son 8 dígitos\n";
            required = false;
        }
        if (!validate(ssDniField.getText(), DNI)) {
            alert += "El DNI no es válido\n";
            required = false;
        }

        if (!validate(mailField.getText(), EMAIL)) {
            alert += "El email es incorrecto\n";
            required = false;
        }
        if (!validate(nikField.getText(), NO_SPACES)) {
            alert += "El nick no puede tener espacios\n";
            required = false;
        }
        ;
        if (!validate(passField.getText(), NO_SPACES)) {
            alert += "La password no puede tener espacios\n";
            required = false;
        }
        ;
        if (required) {
            App.editedWorker.setFullName(nameField.getText());
            App.editedWorker.setSince(datePicker.getValue());
            App.editedWorker.setSsNum(ssField.getText());
            App.editedWorker.setDni(ssDniField.getText());
            App.editedWorker.setSection(sectionChoice.getValue().getId());
            App.editedWorker.setRank(rankChoice.getValue().getId());
            App.editedWorker.setAddress(adressField.getText());
            App.editedWorker.setTelNum(telField.getText());
            App.editedWorker.setMail(mailField.getText());
            App.editedWorker.setContact(otherField.getText());
            App.editedWorker.setUserName(nikField.getText().strip());
            App.editedWorker.setPasswd(passField.getText().strip());
            App.editedWorker.setActive(activeCheckbox.isSelected());
            adminModel.addNewWorker(App.editedWorker);
            App.setWorkerProfModeAdd(false);
            refresh();
        } else {
            App.showDialog(alert);
        }
    }

    @FXML
    void chooseWorkerFolder(ActionEvent event) {

    }

    @FXML
    void next(ActionEvent event) {
        if (nextButton.getText().equals(">"))
            nextWorker();
        else
            addNewWorker();
    }

    private void addNewWorker() {
        App.editedWorker = new Worker();
        showWorker(App.editedWorker);
    }

    private void nextWorker() {
        App.editedWorker = adminModel.getWorkerById(App.editedWorker.getIdWorker() + 1);
        showWorker(App.editedWorker);
    }

    @FXML
    void prev(ActionEvent event) {
        App.editedWorker = adminModel.getWorkerById(App.editedWorker.getIdWorker() - 1);
        showWorker(App.editedWorker);
    }

    @FXML
    void returnButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("adminMenu");

    }

    @FXML
    void toAbilities(ActionEvent event) {

    }

    @FXML
    void toggleActive(ActionEvent event) {

    }

    @SuppressWarnings("exports")
    public void setDefaults(Rank rank, Section section) {
        sectionChoice.setValue(section);
        rankChoice.setValue(rank);
    }

    @FXML
    void refresh() {
        if (App.isWorkerProfModeAdd()) {
            activeCheckbox.setDisable(true);
            datePicker.setValue(LocalDate.now());
            prevButton.setVisible(false);
            nextButton.setVisible(false);
            nameField.requestFocus();

        } else {
            activeCheckbox.setDisable(false);
            prevButton.setVisible(true);
            nextButton.setVisible(true);
            App.editedWorker = adminModel.getWorkerById(App.editedWorker.getIdWorker());
            int idx = App.editedWorker.getIdWorker();
            int lastIdx = adminModel.getWorkerStafSize() - 1;
            prevButton.setDisable(idx <= 3);
            nextButton.setText((idx >= lastIdx) ? "+" : ">");
        }

    }

    @FXML
    void initialize() {

        sectionChoice.getItems().setAll(adminModel.getSections());
        rankChoice.getItems().setAll(adminModel.getRanks());
        sectionChoice.getSelectionModel().select(App.getDefaulSection());
        rankChoice.getSelectionModel().select(App.getDefaultRank());
        workerIdx = App.editedWorker.getIdWorker();
        refresh();

        assert activeCheckbox != null
                : "fx:id=\"activeCheckbox\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert adressField != null
                : "fx:id=\"adressField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert applyButton != null
                : "fx:id=\"applyButton\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert datePicker != null : "fx:id=\"datePicker\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert mailField != null : "fx:id=\"mailField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert nameField != null : "fx:id=\"nameField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert nextButton != null : "fx:id=\"nextButton\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert nikField != null : "fx:id=\"nikField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert otherField != null : "fx:id=\"otherField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert passField != null : "fx:id=\"passField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert prevButton != null : "fx:id=\"prevButton\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert rankChoice != null : "fx:id=\"rankChoice\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert returnButton != null
                : "fx:id=\"returnButton\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert sectionChoice != null
                : "fx:id=\"sectionChoice\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert ssDniField != null : "fx:id=\"ssDniField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert ssField != null : "fx:id=\"ssField\" was not injected: check your FXML file 'workerProfile.fxml'.";
        assert telField != null : "fx:id=\"telField\" was not injected: check your FXML file 'workerProfile.fxml'.";

    }

}
