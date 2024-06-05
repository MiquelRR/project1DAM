package com;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import com.model.AdminModel;
import com.model.TaskSkill;
import com.model.TaskType;
import com.model.Type;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class typeEditController {

        AdminModel adminModel = AdminModel.getAdminModel();

        static Integer h[] = new Integer[] { 0, 152, 304, 456, 608, 760, 912, 1064 };
        static Integer v[] = new Integer[] { 100, 170, 240, 310, 380, 450 };

        static TaskType selectedTask = null;
        static Boolean selectMode;
        static Boolean modelMode;
        static int[] modelSize = new int[] { 0, 0 };
        static Set<Integer> deps;
        static Type edited;

        @FXML
        private Pane root;

        @FXML
        private Pane lineRoot;

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private Button toSelectModeButton;

        @FXML
        private Button addTaskButton;

        @FXML
        private Button exitButton;

        @FXML
        private TextField itemTime;

        @FXML
        private Label nameLabel;

        @FXML
        private Label minuteItemLabel;

        @FXML
        private Label taskFileLabel;

        @FXML
        private Label minutePrepLabel;

        @FXML
        private Label dependenciesLabel;

        @FXML
        private TextField prepTime;

        @FXML
        private Button saveButton;

        @FXML
        private Button dropTaskButton;

        @FXML
        private ChoiceBox<TaskSkill> taskAddChoice;

        @FXML
        private Button taskFileButton;

        @FXML
        private Button typeFolderButton;

        @FXML
        void checkSelectedTask(ActionEvent event) {

        }

        @FXML
        void dropTask(ActionEvent event) {
                if (selectedTask != null) {
                        Boolean clearTask = true;
                        String msg = "Imposible borrar '" + selectedTask.toString() + "' existen dependencias con : ";
                        List<String> dependencies = new ArrayList<>();
                        for (TaskType tk : edited.getTaskList()) {
                                if (tk.getDependsOnIds().contains(selectedTask.getId())) {
                                        clearTask = false;
                                        dependencies.add(tk.toString());
                                }
                        }
                        if (clearTask) {
                                edited.getTaskList().remove(selectedTask);
                                if (!root.getChildren().isEmpty())
                                        for (var node : root.getChildren()) {
                                                ToggleButton tskButton = (ToggleButton) node;
                                                String idBt = tskButton.getId();
                                                if (idBt.equals(tskButton.getId())) {
                                                        root.getChildren().remove(tskButton);
                                                        break;
                                                }
                                        }
                                selectedTask.removeDependences();
                                taskAddChoice.getItems().add(adminModel.getTaskByName(selectedTask.getName()));
                                selectedTask = null;
                                selectMode = true;
                                refresh();
                        } else {
                                App.showDialog(msg + String.join(", ", dependencies));
                        }
                }
        }

        @FXML
        void toSelectMode(ActionEvent event) {
                deps = new HashSet<>();
                selectMode = true;
                refreshTimes(null);
                selectedTask = null;
                refresh();

        }

        @FXML
        void addTask(ActionEvent event) {
                TaskSkill skill = taskAddChoice.getValue();
                TaskType tsk = new TaskType(adminModel.getNextIdTask(), skill, edited.getIdType());
                edited.getTaskList().add(tsk);
                taskAddChoice.getItems().remove(skill);

                if (!taskAddChoice.getItems().isEmpty()) {
                        taskAddChoice.setValue(taskAddChoice.getItems().get(taskAddChoice.getItems().size() - 1));
                }
                refresh();
        }

        @FXML
        public static Border normalBorder;

        private int[] order() {
                int maxY = 0;
                List<Integer> columns = new ArrayList<>();
                Integer lastColumn = 0;
                List<Integer> orderedIds = new ArrayList<>();
                List<TaskType> taskListCp = new ArrayList<>();
                taskListCp.addAll(edited.getTaskList());

                while (!taskListCp.isEmpty()) {
                        List<Integer> taskColumn = new ArrayList<>();
                        columns.add(0);
                        for (TaskType tk : taskListCp) {
                                if (orderedIds.containsAll(tk.getDependsOnIds())) {
                                        tk.setX(lastColumn);
                                        Integer y = columns.get(lastColumn);
                                        tk.setY(y);
                                        y++;
                                        maxY = (y > maxY) ? y : maxY;
                                        columns.set(lastColumn, y);
                                        taskColumn.add(tk.getId());
                                } else {
                                        // "unnasigned: ";
                                }
                        }

                        Iterator<TaskType> iterator = taskListCp.iterator();
                        while (iterator.hasNext()) {
                                TaskType tk = iterator.next();
                                if (taskColumn.contains(tk.getId()))
                                        iterator.remove();
                        }

                        orderedIds.addAll(taskColumn);
                        lastColumn++;
                }

                modelSize = new int[] { lastColumn, maxY };
                return modelSize;

        }

        @FXML
        void chooseTaskFile(ActionEvent event) {
                Node source = (Node) event.getSource();
                Scene scene = source.getScene();
                Stage stage = (Stage) scene.getWindow();
                String pth = App.chooseFile(edited.getDocFolder(), stage);
                selectedTask.setInfoFilePath((pth == null) ? "" : pth);
                taskFileButton.setBorder((selectedTask != null && selectedTask.getInfoFilePath().length() < 1)
                                ? App.ORANGE_BORDER
                                : null);

        }

        @FXML
        void chooseTypeFolder(ActionEvent event) {
                Node source = (Node) event.getSource();
                Scene scene = source.getScene();
                Stage stage = (Stage) scene.getWindow();
                edited.setDocFolder(App.chooseFolder(edited.getDocFolder(), stage));
                typeFolderButton.setBorder((edited.getDocFolder().equals(".")) ? App.ORANGE_BORDER : null);
                // baseFolder = App.chooseFolder(baseFolder,stage);
        }

        @FXML
        void exitButtonPressed(ActionEvent event) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Operación no recuperable");
                alert.setHeaderText("Vas a descartar la edición del modelo");
                alert.setContentText("¿Estás seguro?");
                ButtonType buttonTypeOne = new ButtonType("SI, perder edición");
                ButtonType buttonTypeTwo = new ButtonType("NO, seguir editando");
                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == buttonTypeOne) {
                        edited.setDefaultFlder();
                        edited.setTaskList(new ArrayList<>());
                        Stage stage = (Stage) exitButton.getScene().getWindow();
                        stage.close();
                }
                App.editedModel = null;
        }

        @FXML
        void refreshTimes(ActionEvent event) {

                if (selectedTask != null) {

                        if (tovalidInteger(prepTime.getText()) != null) {
                                selectedTask.setPrepTime(tovalidInteger(prepTime.getText()));
                        }

                        if (tovalidInteger(itemTime.getText()) != null) {
                                selectedTask.setPieceTime(tovalidInteger(itemTime.getText()));
                        }
                }
                refresh();

        }

        @FXML
        void removeDepend(ActionEvent event) {

        }

        @FXML
        void saveButtonPressed(ActionEvent event) {
                adminModel.writeType(edited);
                Stage stage = (Stage) saveButton.getScene().getWindow();
                String message = (modelMode) ? "Modelo" : "Tipo";
                message += " guardado con éxito";
                App.showConfirmation(message);
                App.editedModel = null;
                stage.close();

        }

        private Integer tovalidInteger(String str) {
                Integer value;
                try {
                        value = Integer.parseInt(str);
                } catch (Exception e) {
                        return null;
                }
                value = (value > 0) ? value : null;
                return value;
        }

        @FXML
        void generateButtons(int[] coord) {
                root.getChildren().clear();
                int x = (h.length - coord[0]) / 2;
                int y = (v.length - coord[1]) / 2;
                normalBorder = (coord[1] > v.length || coord[0] > h.length) ? App.RED_BORDER : null;
                for (TaskType tk : edited.getTaskList()) {
                        genToggleButton(tk, x, y);
                }
        }

        @SuppressWarnings("exports")
        @FXML // RECURSIVE
        public Set<Integer> depsOf(TaskType task, Set<Integer> set) {
                for (TaskType tk : edited.getTaskList()) {
                        if (tk.getDependsOnIds().contains(task.getId())) {
                                set.add(tk.getId());
                                set.addAll(depsOf(tk, set));
                        }
                }
                return set;
        }

        @FXML
        private void genToggleButton(TaskType tsk, int x, int y) {

                ToggleButton toggleButton = null;
                if (tsk.getX() != null) {
                        x += tsk.getX();
                        y += tsk.getY();
                }
                if (x >= 0 && x < h.length && y >= 0 && y < v.length) {
                        toggleButton = new ToggleButton(tsk.toString());
                        toggleButton.setLayoutX(h[x]);
                        toggleButton.setLayoutY(v[y]);
                        toggleButton.setPrefWidth(135);
                        toggleButton.setPrefHeight(52);
                        String idString = "" + tsk.getId();
                        toggleButton.setId(idString);
                        toggleButton.setSelected(selectedTask != null && tsk.getId() == selectedTask.getId());
                        toggleButton.setDisable((deps.contains(tsk.getId())));

                        toggleButton.setStyle("-fx-font-size: 12px;-fx-text-fill: #7c7c7c;");
                        String ttText;
                        if (selectMode) {
                                ttText = "Click para edición de tarea";
                        } else {
                                if (selectedTask != null && tsk.getId() == selectedTask.getId())
                                        ttText = "Click para finalizar la edición";
                                else {
                                        ttText = "Seleccionar/deseleccionar como\n tarea previa";
                                        if (selectedTask != null)
                                                ttText += " de " + selectedTask.getName();
                                }
                        }
                        Tooltip customTooltip = new Tooltip(ttText);
                        customTooltip.setStyle("-fx-font-size: 10px; ");
                        Tooltip.install(toggleButton, customTooltip);

                        toggleButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                        pressed(tsk);
                                }
                        });
                        root.getChildren().add(toggleButton);
                } else
                        normalBorder = App.RED_BORDER;

        }

        private void refresh() {

                generateButtons(order());
                if (selectedTask != null) {
                        itemTime.setText(Integer.toString(selectedTask.getPieceTime()));
                        prepTime.setText(Integer.toString(selectedTask.getPrepTime()));
                        taskFileButton.setBorder(
                                        (selectedTask.getInfoFilePath().length() < 1) ? App.ORANGE_BORDER : null);
                }
                typeFolderButton.setBorder((edited.getDocFolder().equals(".")) ? App.ORANGE_BORDER : null);
                if (selectMode) {
                        // root.setCursor(null);

                        if (!root.getChildren().isEmpty())
                                for (var node : root.getChildren()) {
                                        ToggleButton tskButton = (ToggleButton) node;
                                        String idBt = tskButton.getId();
                                        String taskString = (selectedTask == null) ? "xx" : ("" + selectedTask.getId());
                                        tskButton.setSelected(idBt.equals(taskString));
                                }
                } else {

                        // root.setCursor(Cursor.HAND);

                }
                dependenciesLabel.setVisible(!selectMode && edited.getTaskList().size() > 1 || modelMode);

                // Iterator<Node> iterator = root.getChildren().iterator();
                for (var node : root.getChildren()) {
                        // while (iterator.hasNext()) {
                        ToggleButton bt = (ToggleButton) node;

                        if (selectedTask != null && edited.getTaskList().size() > 1
                                        && stringDepsListoOf(selectedTask).contains(bt.getId())) {
                                bt.setBorder(App.ORANGE_BORDER);
                                bt.setCursor(null);
                        } else {
                                bt.setBorder(normalBorder);
                        }
                        if (selectedTask != null && bt.getId().equals(Integer.toString(selectedTask.getId()))) {
                                bt.setBorder(App.ORANGE_BORDER_B);
                                bt.setCursor(Cursor.CROSSHAIR);
                                bt.setTextFill(Color.ORANGE);
                        } else {
                                bt.setCursor((selectMode) ? null : Cursor.HAND);
                        }

                }
                lineRoot.getChildren().clear();
                int x = (h.length - modelSize[0]) / 2;
                int y = (v.length - modelSize[1]) / 2;
                for (TaskType tk : edited.getTaskList()) {
                        int valX = tk.getX() + x;
                        if (valX < 0)
                                valX = 0;
                        if (valX >= h.length)
                                valX = h.length - 1;
                        int valY = tk.getY() + y;
                        if (valY < 0)
                                valY = 0;
                        if (valY >= v.length)
                                valY = v.length - 1;

                        int x1 = h[valX], y1 = v[valY] + 26;
                        for (TaskType t : tk.getDependsOn()) {
                                int x2 = h[t.getX() + x] + 135, y2 = v[t.getY() + y] + 26;
                                Line line = new Line(x1, y1, x2, y2);
                                line.setStroke((selectedTask != null && selectedTask.getId() == tk.getId())
                                                ? Color.ORANGE
                                                : Color.GRAY);
                                line.setStrokeWidth(2);
                                lineRoot.getChildren().add(line);
                        }

                }

                setTimesBorders();

                // toSelectModeButton.setBorder((edited.getTaskList().size() == 1) ?
                // null : App.ORANGE_BORDER);

                addTaskButton.setDisable(!selectMode);
                taskAddChoice.setDisable(!selectMode);
                typeFolderButton.setDisable(!selectMode);
                saveButton.setDisable(!selectMode);

                boolean noSel = selectedTask != null;
                addTaskButton.setVisible(!taskAddChoice.getItems().isEmpty() && !modelMode);
                taskAddChoice.setVisible(!modelMode);
                toSelectModeButton.setVisible(noSel);
                taskFileButton.setVisible(noSel);
                minutePrepLabel.setVisible(noSel);
                minuteItemLabel.setVisible(noSel);
                // taskFileLabel.setVisible(noSel);
                prepTime.setVisible(noSel);
                itemTime.setVisible(noSel);
                dropTaskButton.setVisible(noSel && !modelMode);

        }

        private void setTimesBorders() {
                prepTime.setBorder((tovalidInteger(prepTime.getText()) == null) ? App.ORANGE_BORDER : null);
                itemTime.setBorder((tovalidInteger(itemTime.getText()) == null) ? App.ORANGE_BORDER : null);
        }

        private List<String> stringDepsListoOf(TaskType ts) {
                List<String> ls = new ArrayList<>();
                ls.add(Integer.toString(ts.getId()));
                for (TaskType t : ts.getDependsOn()) {
                        ls.add(Integer.toString(t.getId()));
                }
                return ls;
        }

        private void pressed(TaskType tsk) {

                if (selectMode || modelMode) {
                        deps = new HashSet<>();
                        selectMode = false;
                        selectedTask = tsk;
                        deps = depsOf(tsk, deps);
                        generateButtons(order());
                        refresh();
                } else {

                        if (tsk.equals(selectedTask)) {
                                toSelectMode(null);
                        } else {
                                if (tsk.getDependsOnIds().contains(selectedTask.getId())) {
                                        System.out.println("circular dependency");
                                } else {
                                        if (selectedTask.getDependsOnIds().contains(tsk.getId())) {
                                                selectedTask.removeDependecy(tsk);
                                        } else {
                                                selectedTask.addDependency(tsk);

                                        }
                                        for (var node : root.getChildren()) {
                                                ToggleButton tgb = (ToggleButton) node;
                                                if (tgb.getId().equals(Integer.toString(tsk.getId())))
                                                        tgb.setSelected(false);
                                        }
                                        generateButtons(order());

                                        refresh();
                                }
                        }

                }
        }

        @FXML
        void initialize() {
                modelMode = App.editedModel != null;
                edited=(modelMode)?App.editedModel:App.editedType;
                String toLabel = App.editedType.toString();
                if (modelMode){
                        toLabel += " : " + App.editedModel.toString();
                        //generateButtons(order());
                }
                nameLabel.setText(toLabel);

                selectMode = true;
                deps = new HashSet<>();
                dependenciesLabel.setBorder(App.ORANGE_BORDER);
                if (modelMode)
                        dependenciesLabel.setText("Edita el tiempo y documentos de cada tarea del modelo");
                Tooltip toSelecModeButtonTooltip = new Tooltip("Finaliza la seleccion de dependencias");
                toSelecModeButtonTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(toSelectModeButton, toSelecModeButtonTooltip);
                Tooltip taskChoiceTooltip = new Tooltip("Selecciona una tarea para añadir");
                taskChoiceTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(taskAddChoice, taskChoiceTooltip);
                Tooltip addTaskTooltip = new Tooltip("Añade tarea al modelo");
                addTaskTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(taskAddChoice, addTaskTooltip);
                Tooltip dropTaskButtonTooltip = new Tooltip(
                                "Borra la tarea seleccionada\n(si ninguna depende de ella)");
                dropTaskButtonTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(dropTaskButton, dropTaskButtonTooltip);
                Tooltip prepTimeTooltip = new Tooltip(
                                "tiempo estimado en preparar+finalizar para esta tarea\n(independiente de la cantidad de piezas a manipular)");
                prepTimeTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(prepTime, prepTimeTooltip);
                Tooltip.install(dropTaskButton, dropTaskButtonTooltip);
                Tooltip itemTimeTooltip = new Tooltip("tiempo estimado en manipular/procesar una pieza en esta tarea");
                itemTimeTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(itemTime, itemTimeTooltip);
                Tooltip.install(dropTaskButton, dropTaskButtonTooltip);
                Tooltip typeFolderTooltip = new Tooltip(
                                "seleccione la carpeta base de las instrucciones patra el modelo");
                typeFolderTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(typeFolderButton, typeFolderTooltip);
                Tooltip saveButtonTooltip = new Tooltip("Guarda el estado del modelo");
                saveButtonTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(saveButton, saveButtonTooltip);
                Tooltip taskFileButtonTooltip = new Tooltip("Selecciona el fichero de\ninstrucciones de la tares");
                taskFileButtonTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(taskFileButton, taskFileButtonTooltip);

                taskAddChoice.getItems().setAll(adminModel.getTaskTypes());
                taskAddChoice.setValue(taskAddChoice.getItems().get(taskAddChoice.getItems().size() - 1));
                taskAddChoice.requestFocus();
                
                

                prepTime.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue,
                                        String newValue) {
                                setTimesBorders();
                        }
                });
                itemTime.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue,
                                        String newValue) {
                                setTimesBorders();
                        }
                });

                refresh();

        }

}
