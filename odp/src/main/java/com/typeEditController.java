package com;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import com.model.AdminModel;
import com.model.TaskType;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;

public class typeEditController {

        AdminModel adminModel = AdminModel.getAdminModel();
        List<TaskType> depChoice;
        static Integer h[] = new Integer[] { 0, 152, 304, 456, 608, 760, 912, 1064 };
        static Integer v[] = new Integer[] { 100, 170, 240, 310, 380, 450 };
        List<TaskType> taskList = new ArrayList<>();
        static TaskType selectedTask = null;
        static Boolean selectMode;

        @FXML
        private Pane root;

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
        private Label dependenciesLabel;

        @FXML
        private TextField prepTime;

        @FXML
        private Button saveButton;

        @FXML
        private Button dropTaskButton;

        @FXML
        private ChoiceBox<TaskType> taskAddChoice;

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
                        String msg = "Imposible borrar " + selectedTask.toString() + " existen dependencias con ";
                        List<String> dependencies = new ArrayList<>();
                        for (TaskType tk : taskList) {
                                if (tk.getDependsOnIds().contains(selectedTask.getId()))
                                        clearTask = false;
                                dependencies.add(tk.toString());
                        }
                        if (clearTask) {
                                taskList.remove(selectedTask);
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
                                taskAddChoice.getItems().add(selectedTask);
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
                selectMode = true;
                selectedTask = null;
                refresh();

        }

        @FXML
        void addTask(ActionEvent event) {
                TaskType tsk = taskAddChoice.getValue();
                taskList.add(tsk);
                taskAddChoice.getItems().remove(tsk);
                if (!taskAddChoice.getItems().isEmpty()) {
                        taskAddChoice.setValue(taskAddChoice.getItems().get(taskAddChoice.getItems().size() - 1));
                }
                // selectedTask = tsk;
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
                taskListCp.addAll(taskList);
                System.out.println("Original>" + taskList);
                System.out.println("Inicial >" + taskListCp);

                while (!taskListCp.isEmpty()) {
                        System.out.println(taskListCp);
                        List<Integer> taskColumn = new ArrayList<>();
                        columns.add(0);
                        for (TaskType tk : taskListCp) {
                                System.out.println("tk?" + tk + " WITH DEPENDENCES " + tk.getDependsOnIds());
                                if (orderedIds.containsAll(tk.getDependsOnIds())) {
                                        tk.setX(lastColumn);
                                        Integer y = columns.get(lastColumn);
                                        System.out.println("asign x:" + lastColumn + " y:" + y + " to task "
                                                        + tk.getName());
                                        tk.setY(y);
                                        y++;
                                        maxY = (y > maxY) ? y : maxY;
                                        columns.set(lastColumn, y);
                                        System.out.println("Y = " + y);
                                        taskColumn.add(tk.getId());
                                } else {
                                        System.out.println("unnasigned: " + tk);
                                }
                        }
                        System.out.println("D1>" + taskListCp);
                        System.out.println("taskColumn: " + taskColumn);
                        Iterator<TaskType> iterator = taskListCp.iterator();
                        while (iterator.hasNext()) {
                                TaskType tk = iterator.next();
                                if (taskColumn.contains(tk.getId()))
                                        iterator.remove();
                        }

                        System.out.println("DD>" + taskListCp);

                        orderedIds.addAll(taskColumn);
                        lastColumn++;
                }

                // taskList.addAll(ordered);

                return new int[] { lastColumn, maxY };

        }

        @FXML
        void chooseTaskFile(ActionEvent event) {

        }

        @FXML
        void chooseTypeFolder(ActionEvent event) {

        }

        @FXML
        void exitButtonPressed(ActionEvent event) {

        }

        @FXML
        void refreshTimes(KeyEvent event) {
                refresh();

        }

        @FXML
        void removeDepend(ActionEvent event) {

        }

        @FXML
        void saveButtonPressed(ActionEvent event) {

        }

        private boolean errorOrCero(String str) {
                Boolean ok = false;
                try {
                        ok = (Integer.parseInt(str) == 0);
                } catch (Exception e) {
                        ok = true;
                }
                return ok;

        }

        @FXML
        void generateButtons(int[] coord) {
                root.getChildren().clear();
                int x = (h.length - coord[0]) / 2;
                int y = (v.length - coord[1]) / 2;
                normalBorder = (coord[1] > v.length || coord[0] > h.length) ? App.RED_BORDER : null;
                System.out.println("Y------------------------------------ > " + y);
                for (TaskType tk : taskList) {
                        genToggleButton(tk, x, y);
                }
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
                        toggleButton.setDisable(
                                        selectedTask != null && tsk.getDependsOnIds().contains(selectedTask.getId()));

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
                if (selectMode) {
                        root.setCursor(null);

                        if (!root.getChildren().isEmpty())
                                for (var node : root.getChildren()) {
                                        ToggleButton tskButton = (ToggleButton) node;
                                        String idBt = tskButton.getId();
                                        String taskString = (selectedTask == null) ? "xx" : ("" + selectedTask.getId());
                                        tskButton.setSelected(idBt.equals(taskString));
                                }
                } else {
                        root.setCursor(Cursor.HAND);

                }
                dependenciesLabel.setVisible(!selectMode && taskList.size() > 1);

                // Iterator<Node> iterator = root.getChildren().iterator();
                for (var node : root.getChildren()) {
                        // while (iterator.hasNext()) {
                        ToggleButton bt = (ToggleButton) node;
                        if (selectedTask != null && taskList.size() > 1
                                        && stringDepsListoOf(selectedTask).contains(bt.getId()))
                                bt.setBorder(App.ORANGE_BORDER);
                        else
                                bt.setBorder(normalBorder);
                        if (selectedTask != null && bt.getId().equals(Integer.toString(selectedTask.getId())))
                                bt.setBorder(App.ORANGE_BORDER_B);

                }

                prepTime.setBorder((errorOrCero(prepTime.getText())) ? App.ORANGE_BORDER : null);
                itemTime.setBorder((errorOrCero(itemTime.getText())) ? App.ORANGE_BORDER : null);

                toSelectModeButton.setBorder((taskList.size() == 1) ? null : App.ORANGE_BORDER);
                // toSelectModeButton.setDisable(taskList.size() == 1);

                addTaskButton.setDisable(!selectMode);
                taskAddChoice.setDisable(!selectMode);
                typeFolderButton.setDisable(!selectMode);
                saveButton.setDisable(!selectMode);

                boolean noSel = selectedTask != null;
                addTaskButton.setVisible(!taskAddChoice.getItems().isEmpty());
                toSelectModeButton.setVisible(noSel);
                taskFileButton.setVisible(noSel);
                prepTime.setVisible(noSel);
                itemTime.setVisible(noSel);
                dropTaskButton.setVisible(noSel);

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
                if (selectMode) {
                        selectMode = false;
                        selectedTask = tsk;
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
                                        System.out.println("*".repeat(100));
                                        generateButtons(order());

                                        refresh();
                                }
                        }
                }
        }

        @FXML
        void initialize() {
                selectMode = true;
                dependenciesLabel.setBorder(App.ORANGE_BORDER);
                Tooltip toSelecModeButtonTooltip = new Tooltip("Finaliza la seleccion de dependencias");
                toSelecModeButtonTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(toSelectModeButton, toSelecModeButtonTooltip);
                Tooltip taskChoiceTooltip = new Tooltip("Selecciona una tarea para añadir");
                taskChoiceTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(taskAddChoice, taskChoiceTooltip);
                Tooltip addTaskTooltip = new Tooltip("Añade tarea al modelo");
                addTaskTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(taskAddChoice, addTaskTooltip);
                Tooltip dropTaskButtonTooltip = new Tooltip("Borra la tarea seleccionada (si ninguna depende de ella)");
                dropTaskButtonTooltip.setStyle("-fx-font-size: 10px; ");
                Tooltip.install(dropTaskButton, dropTaskButtonTooltip);
                Tooltip prepTimeTooltip = new Tooltip(
                                "tiempo estimado en preparar+finalizar y recoger para esta tarea (independiente de la cantidad de piezas a manipular)");
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
                taskAddChoice.getItems().setAll(adminModel.getTaskTypes());
                taskAddChoice.setValue(taskAddChoice.getItems().get(taskAddChoice.getItems().size() - 1));
                taskAddChoice.requestFocus();
                nameLabel.setText(App.editedType.toString());
                depChoice = new ArrayList<>();
                refresh();

                assert exitButton != null
                                : "fx:id=\"exitButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert itemTime != null : "fx:id=\"itemTime\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert prepTime != null : "fx:id=\"prepTime\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert saveButton != null
                                : "fx:id=\"saveButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert taskAddChoice != null
                                : "fx:id=\"taskAddChoice\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert taskFileButton != null
                                : "fx:id=\"taskFileButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert typeFolderButton != null
                                : "fx:id=\"typeFolderButton\" was not injected: check your FXML file 'typeEdit.fxml'.";

        }

}
