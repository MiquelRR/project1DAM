package com;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.model.Accesdb;
import com.model.AdminModel;
import com.model.Rank;
import com.model.Section;
import com.model.Worker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class workerProfileController {
    private static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String SS = "^\\d{8}$";
    private static final String DNI = "^\\d{8}[A-Za-z]$";
    private static final String NMTOKEN = "^[a-zA-Z0-9._\\-:]+$";

    public static boolean validate(String string, String patt) {
        if (string == null || string.isEmpty()) {
            return false; // Retorna false si la cadena es nula o vacía
        }
        Pattern pattern = Pattern.compile(patt);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }


    AdminModel adminModel = AdminModel.getAdminModel();
    // Worker worker;

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
    private TextField nickField;

    @FXML
    private TextField otherField;

    @FXML
    private TextField passField;

    @FXML
    private Button prevButton;

    @FXML
    private Button abilitiesButton;

    @FXML
    private ChoiceBox<Rank> rankChoice;

    @FXML
    private Pane root;

    @FXML
    private Button returnButton;

    @FXML
    private Button folderButton;

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
        nickField.setText((worker.getUserName() == null) ? "" : worker.getUserName());
        nickField.setText((worker.getUserName() == null) ? "" : worker.getUserName());
        nameField.setText((worker.getFullName() == null) ? "" : worker.getFullName());
        passField.setText((worker.getPasswd() == null) ? "" : worker.getPasswd());
        if (worker.getSince() == null) {
            datePicker.setValue(LocalDate.now());
            worker.setSince(LocalDate.now());
        } else
            datePicker.setValue(worker.getSince());
        ssField.setText((worker.getSsNum() == null) ? "" : worker.getSsNum());
        ssDniField.setText((worker.getDni() == null) ? "" : worker.getDni());

        if (worker.getSection() == null) {
            App.setWorkerProfModeAdd(true);
            sectionChoice.setValue(App.getDefaultSection());
            App.editedWorker.setSection(App.getDefaultSection().getId());
        } else
            sectionChoice.setValue(adminModel.getSectionById(worker.getSection()));

        if (worker.getRank() == null) {
            rankChoice.setValue(App.getDefaultRank());
            App.editedWorker.setRank(App.getDefaultRank().getId());
        } else
            rankChoice.setValue(adminModel.getRankById(worker.getRank()));
        adressField.setText((worker.getAddress() == null) ? "" : worker.getAddress());
        telField.setText((worker.getTelNum() == null) ? "" : worker.getTelNum());
        mailField.setText((worker.getMail() == null) ? "" : worker.getMail());
        otherField.setText((worker.getContact() == null) ? "" : worker.getContact());
        activeCheckbox.setSelected(worker.getActive() == null || !worker.getActive());
        refresh();
    }

    @FXML
    Boolean applyButtonPressed(ActionEvent event) {
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
        if (!validate(nickField.getText(), NMTOKEN)) {
            alert += "El nick no puede tener espacios\n";
            required = false;
        }
        ;
        if (!validate(passField.getText(), NMTOKEN)) {
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
            App.editedWorker.setUserName(nickField.getText().strip());
            App.editedWorker.setPasswd(passField.getText().strip());
            App.editedWorker.setActive(!activeCheckbox.isSelected());
            if (App.workerProfModeAdd) {
                adminModel.addNewWorker(App.editedWorker);
                App.setWorkerProfModeAdd(false);
            } else {
                adminModel.updateWorker(App.editedWorker);
            }
            refresh();
        } else {
            App.showDialog(alert);
        }
        return required;
    }

    @FXML
    void chooseWorkerFolder(ActionEvent event) throws IOException {
        if (App.editedWorker.getDocFolder() == null)
            App.editedWorker.setDocFolder(App.WORKERS_FOLDER);
        String selectedDirectory = App.chooseFolder(App.editedWorker.getDocFolder(), App.st);
        if (selectedDirectory != null) {
            App.editedWorker.setDocFolder(selectedDirectory);
            adminModel.updateWorker(App.editedWorker);
        }
        refresh();
    }

    @FXML
    void next(ActionEvent event) {
        if (nextButton.getText().equals(">"))
            nextWorker();
        else
            addNewWorker();
    }

    private void addNewWorker() {
        App.editedWorker = new Worker(adminModel.getNewWorkerIndex()); 
        showWorker(App.editedWorker);
    }

    private void nextWorker() {
        App.editedWorker = adminModel.getWorkerById(App.editedWorker.getIdWorker() + 1);
        showWorker(App.editedWorker);
    }

    @FXML
    void prev(ActionEvent event) {
        if (!App.workerProfModeAdd)
            App.editedWorker = adminModel.getWorkerById(App.editedWorker.getIdWorker() - 1);
        else {
            App.workerProfModeAdd = false;
            App.editedWorker = adminModel.getLastWorker();
        }
        showWorker(App.editedWorker);
    }

    @FXML
    void returnButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("adminMenu");

    }

    @FXML
    void toAbilities(ActionEvent event) throws IOException {
        if (applyButtonPressed(event))
            App.setRoot("workerAbilities");

    }

    @FXML
    void toggleActive(ActionEvent event) {
        App.editedWorker.setActive(!activeCheckbox.isSelected());
        refresh();
        if (activeCheckbox.isSelected())
            adminModel.updateWorker(App.editedWorker);
        else
            applyButtonPressed(event);
    }

    @SuppressWarnings("exports")
    public void setDefaults(Rank rank, Section section) {
        sectionChoice.setValue(section);
        rankChoice.setValue(rank);
    }

    @FXML
    void refresh() {
        String color;
        if (App.editedWorker.getDocFolder().equals(App.WORKERS_FOLDER))
            color = "-fx-text-fill: orange;";
        else
            color = "#c7c7c7";
        folderButton.setStyle(color);
        if (App.editedWorker.getAbilities() == null)
            color = "-fx-text-fill: orange;";
        else
            color = "#c7c7c7";
        abilitiesButton.setStyle(color);
        applyButton.setText((App.workerProfModeAdd) ? "Añadir" : "Actualiza");
        if (App.isWorkerProfModeAdd()) {
            activeCheckbox.setDisable(true);
            datePicker.setValue(LocalDate.now());
            prevButton.setVisible(true);
            nextButton.setVisible(false);
            nameField.requestFocus();

        } else {
            activeCheckbox.setDisable(false);
            prevButton.setVisible(true);
            nextButton.setVisible(true);
            App.editedWorker = adminModel.getWorkerById(App.editedWorker.getIdWorker());
            int idx = App.editedWorker.getIdWorker();
            int lastIdx = adminModel.getWorkerStafSize() - 1;
            prevButton.setDisable(idx <= 2);
            nextButton.setText((idx >= lastIdx) ? "+" : ">");
        }
        Boolean is = activeCheckbox.isSelected();
        nameField.setDisable(is);
        datePicker.setDisable(is);
        ssField.setDisable(is);
        ssDniField.setDisable(is);
        sectionChoice.setDisable(is);
        rankChoice.setDisable(is);
        adressField.setDisable(is);
        telField.setDisable(is);
        mailField.setDisable(is);
        otherField.setDisable(is);
        folderButton.setDisable(is);
        nickField.setDisable(is);
        passField.setDisable(is);
        abilitiesButton.setDisable(is);
        refreshBorders();
    }
    @FXML
    void refreshBorders(){
        nameField.setBorder(!nameField.getText().isBlank() ? null : App.ORANGE_BORDER);
        ssField.setBorder(validate(ssField.getText(), SS) ? null : App.ORANGE_BORDER);
        ssDniField.setBorder(validate(ssDniField.getText(), DNI) ? null : App.ORANGE_BORDER);
        mailField.setBorder(validate(mailField.getText(), EMAIL) ? null : App.ORANGE_BORDER);
        nickField.setBorder(validate(nickField.getText(), NMTOKEN) ? null : App.ORANGE_BORDER);
        passField.setBorder(validate(passField.getText(), NMTOKEN) ? null : App.ORANGE_BORDER);

    }

    @FXML
    void initialize() {
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                textField.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        refreshBorders();
                    }
                });
            }
        }
        

        sectionChoice.getItems().setAll(adminModel.getSections());
        rankChoice.getItems().setAll(adminModel.getRanks());
        sectionChoice.getSelectionModel().select(App.getDefaultSection());
        rankChoice.getSelectionModel().select(App.getDefaultRank());
        App.editedWorker=adminModel.getLastWorker();
        showWorker(App.editedWorker);
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
        assert nickField != null : "fx:id=\"nikField\" was not injected: check your FXML file 'workerProfile.fxml'.";
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
