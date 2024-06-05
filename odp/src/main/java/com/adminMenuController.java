package com;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.model.AdminModel;
import com.model.Rank;
import com.model.Section;
import com.model.TaskSkill;
import com.model.Type;
import com.model.Worker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

public class adminMenuController {
    AdminModel adminModel = AdminModel.getAdminModel();
    final String OK = "✔";
    final String MENU = "☰";
    private String message = "";
    @FXML
    private Label title;

    @FXML
    private Label arrow;

    @FXML
    private Label arrow2;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addRankButton;

    @FXML
    private Button addSectionButton;

    @FXML
    private Button addModelButton;

    @FXML
    private Button addOrderButton;

    @FXML
    private Button addTypeButton;

    @FXML
    private Button editWorkerButton;

    @FXML
    private Button removeTaskButton;

    @FXML
    private Button editTypeButton;

    @FXML
    private Button editModelButton;

    @FXML
    private ChoiceBox<Type> modelChoice;

    @FXML
    private ChoiceBox<TaskSkill> taskChoice;

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
    private TextField referenceField;

    @FXML
    private TextField unitsField;

    @FXML
    private ChoiceBox<Type> typeChoice;

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
        if (rankField.getText().length() == 1)
            rankField.setText("");
        ;
        // refresh();

    }

    @FXML
    void addSection(ActionEvent event) {
        if (sectionField.getText().length() > 1) {
            adminModel.addSection(sectionField.getText());
            sectionField.setText("");
            ;
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
    void addNewType(ActionEvent event) {
        if (typeField.getText().length() > 1) {
            adminModel.addType(typeField.getText());
            typeField.setText("");
            addTypeButton.setDisable(false);
            addTypeButton.setText("+");
            refresh();
        } else {
            addTypeButton.setDisable(true);
            typeChoice.setVisible(false);
            typeField.setVisible(true);
            typeField.requestFocus();
            editTypeButton.setVisible(false);
        }

    }

    @FXML
    void addWorker(ActionEvent event) {

    }

    @FXML
    void editType(ActionEvent event) throws IOException {
        App.editedModel = null;
        App.editedType = typeChoice.getValue();
        if (!modelChoice.getItems().isEmpty())
            App.showDialog("Al editar el tipo sólo afectará a modelos nuevos", "Advertencia",
                    "Los cambios no afectan a modelos ya exitentes", new Alert(AlertType.WARNING));
        Window parentWindow = ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("typeEdit.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("OpendPlan: Edición de tipos");
        stage.setX(parentWindow.getX() - 300);
        stage.setY(parentWindow.getY() + 150);
        stage.show();

    }

    @FXML
    void editWorker(ActionEvent event) throws IOException {
        App.setRoot("workerProfile");

    }

    @FXML
    void addNewModel(ActionEvent event) {
        App.editedType = typeChoice.getValue();
        if (modelField.getText().length() > 1) {
            adminModel.addModel(modelField.getText(), typeChoice.getValue());
            modelField.setText("");
            addModelButton.setDisable(false);
            addModelButton.setText("+");
            refresh();
        } else {
            addModelButton.setDisable(true);
            modelChoice.setVisible(false);
            modelField.setVisible(true);
            modelField.requestFocus();
            editModelButton.setVisible(false);
        }
    }

    @FXML
    void addOrder(ActionEvent event) throws IOException {
        Integer units=Integer.parseInt(unitsField.getText());
        App.units= units;
        App.reference = referenceField.getText();
        
        App.editedOrder=adminModel.addOrder(referenceField.getText(), modelChoice.getValue(), units );
        System.out.println("@".repeat(199)+App.editedOrder.getName());
        Window parentWindow = ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("plan.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("OndPlan: Planificar pedido");
        stage.setX(parentWindow.getX() - 300);
        stage.setY(parentWindow.getY() + 150);
        stage.show();

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
                App.showDialog("Intentas borrar una tarea en uso");
        }

    }

    @FXML
    void editModel(ActionEvent event) throws IOException {
        App.editedModel = modelChoice.getValue();
        Window parentWindow = ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("typeEdit.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("OndPlan: Edición de tipos");
        stage.setX(parentWindow.getX() - 300);
        stage.setY(parentWindow.getY() + 150);
        stage.show();

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
                App.showDialog("Intentas borrar una categoria con trabajadores");
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
                App.showDialog("Intentas borrar una sección con trabajadores");
        }
    }

    @FXML
    void returnButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("login");
    }

    @FXML
    void toCalendars(ActionEvent event) throws IOException {
        App.setRoot("calendarsEdit");

    }

    @FXML
    void showAddSectionButton(KeyEvent event) {
        addSectionButton.setVisible(true);
        addSectionButton.setDisable(false);
        addSectionButton.setText(OK);
    }

    @FXML
    void showAddTypeButton(KeyEvent event) {
        addTypeButton.setVisible(true);
        addTypeButton.setDisable(false);
        addTypeButton.setText(OK);
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
    void showAddModelButton(KeyEvent event) {
        addModelButton.setVisible(true);
        addModelButton.setDisable(false);
        addModelButton.setText(OK);
    }

    @FXML
    void setEditedType() {
        List<Type> modelsOfType = adminModel.getModelsOf(typeChoice.getValue());
        if (!possibleModels || modelsOfType.isEmpty()) {
            editModelButton.setVisible(false);
            addModelButton.setVisible(false);
            modelField.setVisible(true);
            modelChoice.setVisible(false);
        } else {
            editModelButton.setVisible(true);
            modelChoice.getItems().clear();
            modelChoice.getItems().addAll(modelsOfType);
            modelChoice.setVisible(true);
            modelChoice.getSelectionModel().select(modelChoice.getItems().size() - 1);
            addModelButton.setVisible(true);
        }
    }

    private static boolean possibleModels;

    @FXML
    void refresh() {

        boolean possibleWorkers = true;
        boolean possibleTypes = true;
        possibleModels = true;
        boolean possibleOrder = adminModel.getModels().size() > 0;

        if (adminModel.getSections().isEmpty()) {
            possibleWorkers = false;
            removeSectionButton.setVisible(false);
            addSectionButton.setVisible(false);
            sectionField.setVisible(true);
            sectionChooser.setVisible(false);
        } else {
            removeSectionButton.setVisible(true);
            sectionChooser.getItems().clear();
            // sectionChooser.getItems().add(new Section(-1, "TODAS"));
            sectionChooser.getItems().addAll(adminModel.getSections());
            sectionChooser.setVisible(!addSectionButton.isDisabled());
            sectionChooser.getSelectionModel().select(sectionChooser.getItems().size() - 1);
            addSectionButton.setVisible(true);
        }

        if (adminModel.getRanks().isEmpty()) {
            possibleWorkers = false;
            removeRankButton.setVisible(false);
            addRankButton.setVisible(false);
            rankField.setVisible(true);
            rankChooser.setVisible(false);
        } else {
            removeRankButton.setVisible(true);
            rankChooser.getItems().clear();
            // rankChooser.getItems().add(new Rank(-1, "TODAS"));
            rankChooser.getItems().addAll(adminModel.getRanks());
            rankChooser.setVisible(true);
            rankChooser.getSelectionModel().select(rankChooser.getItems().size() - 1);
            addRankButton.setVisible(true);
        }

        if (adminModel.getTaskTypes().isEmpty()) {
            possibleTypes = false;
            removeTaskButton.setVisible(false);
            addTaskButton.setVisible(false);
            taskField.setVisible(true);
            taskChoice.setVisible(false);
        } else {
            removeTaskButton.setVisible(true);
            taskChoice.getItems().clear();
            possibleTypes = true;
            taskChoice.getItems().addAll(adminModel.getTaskTypes());
            taskChoice.setVisible(true);
            taskChoice.getSelectionModel().select(taskChoice.getItems().size() - 1);
            addTaskButton.setVisible(true);
        }

        // TYPES
        if (adminModel.getTypes().isEmpty()) {
            possibleModels = false;
            editTypeButton.setVisible(false);
            addTypeButton.setVisible(false);
            typeField.setVisible(true);
            typeChoice.setVisible(false);
        } else {
            editTypeButton.setVisible(true);
            possibleModels = true;
            typeChoice.getItems().clear();
            typeChoice.getItems().addAll(adminModel.getTypes());
            typeChoice.setVisible(true);
            typeChoice.getSelectionModel().select(typeChoice.getItems().size() - 1);
            addTypeButton.setVisible(true);
        }
        setEditedType();
        /*
         * List<Type> modelsOfType = adminModel.getModelsOf(typeChoice.getValue());
         * System.out.println("~".repeat(100)+modelsOfType);
         * 
         * if (!possibleModels || modelsOfType.isEmpty()) {
         * editModelButton.setVisible(false);
         * addModelButton.setVisible(false);
         * modelField.setVisible(true);
         * modelChoice.setVisible(false);
         * } else {
         * editModelButton.setVisible(true);
         * modelChoice.getItems().clear();
         * modelChoice.getItems().addAll(modelsOfType);
         * modelChoice.setVisible(true);
         * modelChoice.getSelectionModel().select(modelChoice.getItems().size() - 1);
         * addModelButton.setVisible(true);
         * }
         */

        referenceField.setVisible(possibleOrder);
        unitsField.setVisible(possibleOrder);
        addOrderButton.setVisible(possibleOrder);
        arrow2.setVisible(possibleOrder);

        typeField.setVisible(possibleTypes);
        modelField.setVisible(possibleModels);
        arrow.setVisible(possibleModels);
        editWorkerButton.setVisible(possibleWorkers);
    }
    @FXML
    void refreshBorders(){
        Boolean readyToPlan = true;
        if (referenceField.getText() != null && referenceField.getText().length()>1){
            referenceField.setBorder(null);
        } else {
            readyToPlan=false;
            referenceField.setBorder(App.ORANGE_BORDER);
        }
        
        if (validate(unitsField.getText(), "^\\d+$") && Integer.parseInt(unitsField.getText())>0){
            unitsField.setBorder(null);
        } else{
            readyToPlan=false;
            unitsField.setBorder(App.ORANGE_BORDER);
        }

        addOrderButton.setDisable(!readyToPlan);




    }
    
    public static boolean validate(String string, String patt) {
        if (string == null || string.isEmpty()) {
            return false; // Retorna false si la cadena es nula o vacía
        }
        Pattern pattern = Pattern.compile(patt);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    @FXML
    void initialize() {
        addOrderButton.setDisable(true);
        refreshBorders();

        unitsField.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                refreshBorders();
            }
        });
        referenceField.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                refreshBorders();
            }
        });

        refresh();

    }

}
