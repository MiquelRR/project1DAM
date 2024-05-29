package com;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class typeEditController {

        AdminModel adminModel = AdminModel.getAdminModel();
        List<TaskType> depChoice;
        TaskType boardSelectedTask;
        static Integer h[] = new Integer[] { 0, 152, 304, 456, 608, 760, 912, 1064 };
        static Integer v[] = new Integer[] { 100, 170, 240, 310, 380, 450 };
        List<TaskType> taskList = new ArrayList<>();

        @FXML
        private Pane root;

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private ToggleButton TOGGLE;

        @FXML
        private ChoiceBox<TaskType> addDepChoice;

        @FXML
        private Button addDependButton;

        @FXML
        private Button addTaskButton;

        @FXML
        private Label depAddLabel;

        @FXML
        private Label employeeName1;

        @FXML
        private Button exitButton;

        @FXML
        private TextField itemTime;

        @FXML
        private Label nameLabel;

        @FXML
        private TextField prepTime;

        @FXML
        private Button removeDependButton;

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
                taskAddChoice.setValue(taskAddChoice.getItems().get(taskAddChoice.getItems().size() - 1));
                root.getChildren().clear();
                generateButtons(order());
        }

        @FXML
        void generateButtons(int[] coord) {
                int x = (h.length - coord[0]) / 2;
                int y = (v.length - coord[1]) / 2;
                for (TaskType tk : taskList) {
                        ToggleButton tb = genToggleButton(tk, 0, 0);
                        root.getChildren().add(tb);
                }
        }

        int[] order() {
                int maxY = 0;
                List<Integer> columns = new ArrayList<>();
                Integer lastColumnIdx = 0;
                List<Integer> ordered = new ArrayList<>();
                List<TaskType> taskListCp = new ArrayList<>();
                taskListCp.addAll(taskList);
                // next columns
                while (!taskListCp.isEmpty()) {
                        System.out.println(taskListCp);
                        List<Integer> taskColumn = new ArrayList<>();
                        columns.add(0);
                        for (TaskType tk : taskListCp) {
                                System.out.println("tk>" + tk);
                                if (ordered.containsAll(tk.getDependsOnIds())) {
                                        taskColumn.add(tk.getId());
                                        tk.setX(lastColumnIdx);
                                        Integer y = columns.get(lastColumnIdx);
                                        tk.setY(y);
                                        y++;
                                        maxY = (y > maxY) ? y : maxY;
                                        columns.set(lastColumnIdx, y);
                                        System.out.println("Y = " + y);
                                        taskColumn.add(tk.getId());
                                }
                        }
                        System.out.println("DD>"+taskListCp);
                        for (int i = 0; i < taskListCp.size(); i++) {
                                if(taskColumn.contains(taskListCp.get(i).getId()))
                                taskListCp.remove(i);
                        }

                        /* for (TaskType tss : taskListCp) {
                                

                                if (tss.getId() != null && taskColumn.contains(tss.getId()))
                                        taskListCp.remove(tss);

                        } */
                        ordered.addAll(taskColumn);
                        lastColumnIdx++;
                }

                // taskList.addAll(ordered);

                return new int[] { lastColumnIdx, maxY };

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
                boolean noSel = boardSelectedTask != null;
                addTaskButton.setVisible(!taskAddChoice.getItems().isEmpty());
                addDependButton.setVisible(noSel);
                addDepChoice.setVisible(noSel);
                removeDependButton.setVisible(noSel);
                taskFileButton.setVisible(noSel);
                prepTime.setVisible(noSel);
                itemTime.setVisible(noSel);

        }

        @FXML
        private static ToggleButton genToggleButton(TaskType tsk, int x, int y) {
                ToggleButton toggleButton = new ToggleButton(tsk.toString());
                int xt = 0, yt = 0;
                if (tsk.getX() != null) {
                        xt = tsk.getX();
                        yt = tsk.getY();
                }
                toggleButton.setLayoutX(h[x + xt]);
                toggleButton.setLayoutY(v[y + yt]);
                toggleButton.setPrefWidth(135);
                toggleButton.setPrefHeight(52);
                toggleButton.setId(tsk.toString());
                toggleButton.setStyle("-fx-text-fill: #7c7c7c;");
                toggleButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                                pressed(tsk);
                        }
                });

                return toggleButton;

        }

        private static void pressed(TaskType tsk) {
                System.out.println(tsk);
        }

        @FXML
        void initialize() {
                taskAddChoice.getItems().setAll(adminModel.getTaskTypes());
                taskAddChoice.setValue(taskAddChoice.getItems().get(taskAddChoice.getItems().size() - 1));
                taskAddChoice.requestFocus();
                boardSelectedTask = null;
                nameLabel.setText(App.editedType.toString());
                depChoice = new ArrayList<>();
                refresh();

                assert TOGGLE != null : "fx:id=\"TOGGLE\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert addDepChoice != null
                                : "fx:id=\"addDepChoice\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert addDependButton != null
                                : "fx:id=\"addDependButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert addTaskButton != null
                                : "fx:id=\"addTaskButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert depAddLabel != null
                                : "fx:id=\"depAddLabel\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert employeeName1 != null
                                : "fx:id=\"employeeName1\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert exitButton != null
                                : "fx:id=\"exitButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert itemTime != null : "fx:id=\"itemTime\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert prepTime != null : "fx:id=\"prepTime\" was not injected: check your FXML file 'typeEdit.fxml'.";
                assert removeDependButton != null
                                : "fx:id=\"removeDependButton\" was not injected: check your FXML file 'typeEdit.fxml'.";
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
