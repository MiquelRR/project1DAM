package com;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.security.auth.RefreshFailedException;

import com.model.AdminModel;
import com.model.TaskType;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
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
        private Button addDependButton;

        @FXML
        private Button addTaskButton;

        @FXML
        private Button exitButton;

        @FXML
        private TextField itemTime;

        @FXML
        private Label nameLabel;

        @FXML
        private TextField prepTime;

        @FXML
        private Button saveButton;

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
        void addDepend(ActionEvent event) {

        }

        @FXML
        void addTask(ActionEvent event) {
                TaskType tsk = taskAddChoice.getValue();
                taskList.add(tsk);
                taskAddChoice.getItems().remove(tsk);
                if (!taskAddChoice.getItems().isEmpty()) {
                        taskAddChoice.setValue(taskAddChoice.getItems().get(taskAddChoice.getItems().size() - 1));
                }
                selectedTask = tsk;
                refresh();
        }

        @FXML
        void generateButtons(int[] coord) {
                root.getChildren().clear();
                int x = (h.length - coord[0]) / 2;
                int y = (v.length - coord[1]) / 2;
                for (TaskType tk : taskList) {
                        genToggleButton(tk, x, y);
                }
        }

        int[] order() {
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

        }

        @FXML
        void removeDepend(ActionEvent event) {

        }

        @FXML
        void saveButtonPressed(ActionEvent event) {

        }

        private void refresh() {
                if (selectMode) {
                        generateButtons(order());
                        if (!root.getChildren().isEmpty())
                                for (var node : root.getChildren()) {

                                        ToggleButton tskButton = (ToggleButton) node;
                                        String idBt = tskButton.getId();
                                        System.out.println(idBt + "==" + "b:" + selectedTask.getId() + " - "
                                                        + selectedTask.getName());
                                        tskButton.setSelected(idBt.equals("b:" + selectedTask.getId()));
                                }
                }
                boolean noSel = selectedTask != null;
                addTaskButton.setVisible(!taskAddChoice.getItems().isEmpty());
                addDependButton.setVisible(noSel);
                taskFileButton.setVisible(noSel);
                prepTime.setVisible(noSel);
                itemTime.setVisible(noSel);

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
                        toggleButton.setId("b:" + tsk.getId());
                        toggleButton.setStyle("-fx-text-fill: #7c7c7c;");
                        toggleButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                        pressed(tsk);
                                }
                        });
                        root.getChildren().add(toggleButton);
                }

        }

        private void pressed(TaskType tsk) {
                if (selectMode) {
                        selectedTask = tsk;
                        refresh();
                }
        }

        @FXML
        void initialize() {
                selectMode = true;
                Tooltip addDependButtonTooltip = new Tooltip("Activa selección tareas previas a esta");
                addDependButtonTooltip.setStyle("-fx-font-size: 16px; ");
                Tooltip.install(addDependButton, addDependButtonTooltip);
                Tooltip taskChoiceTooltip = new Tooltip("Selecciona una tarea para añadir");
                taskChoiceTooltip.setStyle("-fx-font-size: 16px; ");
                Tooltip.install(taskAddChoice, taskChoiceTooltip);
                Tooltip addTaskTooltip = new Tooltip("Añade tarea al modelo");
                addTaskTooltip.setStyle("-fx-font-size: 16px; ");
                Tooltip.install(taskAddChoice, addTaskTooltip);
                taskAddChoice.getItems().setAll(adminModel.getTaskTypes());
                taskAddChoice.setValue(taskAddChoice.getItems().get(taskAddChoice.getItems().size() - 1));
                taskAddChoice.requestFocus();
                nameLabel.setText(App.editedType.toString());
                depChoice = new ArrayList<>();
                refresh();

                assert addDependButton != null
                                : "fx:id=\"addDependButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
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
