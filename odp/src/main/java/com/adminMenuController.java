package com;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.model.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class adminMenuController {
    AdminModel adminModel = AdminModel.getAdminModel();
    final String OK = "✔";
    final String TRASH = "\uD83D\uDDD1️";
    final String MENU = "☰";
    final int SECS = 2;
    private String message = "";

    @FXML
    private Pane root;

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
    void removeModel(ActionEvent event) {
    }

    @FXML
    void removeType(ActionEvent event) {
        Type val=typeChoice.getValue();
        adminModel.removeType(val);
        typeChoice.getItems().remove(val);
        selectLast(typeChoice);
    }

    private void selectLast(ChoiceBox<?> choiceBox) {
        int itemCount = choiceBox.getItems().size();
        if (itemCount > 0) {
            choiceBox.getSelectionModel().select(itemCount - 1);
        }
    }

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
            sectionField.setCursor(Cursor.WAIT);

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    adminModel.addSection(sectionField.getText());
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    Platform.runLater(() -> {
                        sectionField.setText("");
                        addSectionButton.setText("+");
                        refresh();
                        sectionField.setCursor(Cursor.DEFAULT);
                    });
                }

                @Override
                protected void failed() {
                    super.failed();
                    Platform.runLater(() -> {
                        App.showDialog("Error inserción sección "+sectionField.getText());
                        sectionField.setText("");
                        addSectionButton.setText("+");
                        refresh();
                        sectionField.setCursor(Cursor.DEFAULT);
                        
                    });
                }
            };
            new Thread(task).start();
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
            //refresh();
        }

    }

    @FXML
    void addWorker(ActionEvent event) {

    }


    @FXML
    void editType(ActionEvent event) throws IOException {
        if(!editTypeButton.getText().equals(TRASH)) {
            App.editedModel = null;
            App.editedType = typeChoice.getValue();
            if (!adminModel.getModelsOf(typeChoice.getValue()).isEmpty())
                App.showDialog("Al editar el tipo sólo afectará a modelos nuevos", "Advertencia",
                        "Los cambios no afectan a modelos ya existentes", new Alert(AlertType.WARNING));
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
        } else removeType(null);

    }

    @FXML
    void editWorker(ActionEvent event) throws IOException {
        App.setRoot("workerProfile");

    }

    @FXML
    void addNewModel(ActionEvent event) {
        App.editedType = typeChoice.getValue();
        if (modelField.getText().length() > 1) {
            adminModel.addModel(modelField.getText(), App.editedType);
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
        Integer units = Integer.parseInt(unitsField.getText());
        App.units = units;
        App.reference = referenceField.getText();

        App.editedOrder = adminModel.addOrder(referenceField.getText(), modelChoice.getValue(), units);
        System.out.println("@".repeat(199) + App.editedOrder.getName());
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
        App.editedType = typeChoice.getValue();
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
            System.out.println(sectionChooser.getValue());
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
        App.setDefaultSection(sectionChooser.getValue());
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



    private static boolean possibleModels;

    @FXML
    void refresh() {

        boolean possibleWorkers = true;
        boolean possibleTypes;
        boolean possibleOrder = !adminModel.getModels().isEmpty();

        if (adminModel.getSections().isEmpty()) {
            possibleWorkers = false;
            removeSectionButton.setVisible(false);
            addSectionButton.setVisible(false);
            sectionField.setVisible(true);
            sectionChooser.setVisible(false);
        } else {
            removeSectionButton.setVisible(true);
            sectionChooser.getItems().clear();
            sectionChooser.getItems().addAll(adminModel.getSections());
            sectionChooser.setVisible(!addSectionButton.isDisabled());
            selectLast(sectionChooser);
            addSectionButton.setVisible(true);
        }
        if (sectionChooser.getValue().getId() == -1) // general section
            removeSectionButton.setVisible(false);

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
            selectLast(rankChooser);
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
            selectLast(taskChoice);
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
            if(typeChoice.getItems().size()!=adminModel.getTypes().size()) {
                typeChoice.getItems().clear();
                typeChoice.getItems().addAll(adminModel.getTypes());
            }
            typeChoice.setVisible(true);
            if(typeChoice.getValue()==null){
                selectLast(typeChoice);
            }
            addTypeButton.setVisible(true);
        }
        refreshModels();


        referenceField.setVisible(possibleOrder);
        unitsField.setVisible(possibleOrder);
        addOrderButton.setVisible(possibleOrder);
        arrow2.setVisible(possibleOrder);

        typeField.setVisible(possibleTypes);
        modelField.setVisible(possibleModels);
        editWorkerButton.setVisible(possibleWorkers);
    }

    private void refreshModels() {
        possibleModels = typeChoice.getValue()!=null && !typeChoice.getValue().getTaskList().isEmpty();
        arrow.setVisible(possibleModels);

        List<Type> modelsOfType = adminModel.getModelsOf(typeChoice.getValue());
        System.out.println("98-"+modelsOfType);
        if (modelsOfType.isEmpty() ) {
            editModelButton.setVisible(false);
            addModelButton.setVisible(false);
            modelField.setVisible(possibleModels); // here
            modelChoice.setVisible(false);
        } else {
            System.out.println("99-"+modelsOfType+"---"+modelsOfType.size());
            editModelButton.setVisible(true);
            modelChoice.getItems().clear();
            modelChoice.getItems().addAll(modelsOfType);
            modelChoice.setVisible(true);
            selectLast(modelChoice);
            addModelButton.setVisible(true);
        }
    }

    @FXML
    void refreshBorders() {
        Boolean readyToPlan = true;
        if (referenceField.getText() != null && referenceField.getText().length() > 1) {
            referenceField.setBorder(null);
        } else {
            readyToPlan = false;
            referenceField.setBorder(App.ORANGE_BORDER);
        }

        if (validate(unitsField.getText(), "^\\d+$") && Integer.parseInt(unitsField.getText()) > 0) {
            unitsField.setBorder(null);
        } else {
            readyToPlan = false;
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
        typeField.setText("");

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(SECS), e -> {
            if( typeChoice.getValue()!=null && adminModel.getModelsOf(typeChoice.getValue()).isEmpty()) {
                editTypeButton.setText(TRASH);
                editTypeButton.setStyle("-fx-font-size: 20");
            }
        }));
        timeline.setCycleCount(1);

        editTypeButton.setOnMouseEntered(event -> {
            timeline.playFromStart();
        });
        editTypeButton.setOnMouseExited(event -> {
            timeline.stop();
            editTypeButton.setText(MENU);
            editTypeButton.setStyle("-fx-font-size: 24");
        });

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
        typeChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Type>() {
            @Override
            public void changed(ObservableValue<? extends Type> observable, Type oldValue, Type newValue) {
                refreshModels();
            }
        });

    }

}
