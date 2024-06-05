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
import com.model.TaskType;
import com.model.Type;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class planController {
    AdminModel adminModel = AdminModel.getAdminModel();

    protected Type edited;

    static Integer h[] = new Integer[] { 0, 152, 304, 456, 608, 760, 912, 1064 };
    static Integer v[] = new Integer[] { 100, 170, 240, 310, 380, 450 };
    static TaskType selectedTask = null;
    static Boolean selectMode;
    static Boolean modelMode;
    static int[] modelSize = new int[] { 0, 0 };
    static Set<Integer> deps;

    @FXML
    private Pane root;

    @FXML
    private Pane lineRoot;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label dateLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Label modelLabel;

    @FXML
    private Button nextDay;

    @FXML
    private Label orderLabel;

    @FXML
    private ProgressIndicator percentLabel;

    @FXML
    private Button prevDay;

    @FXML
    private Label taskDateLabel;

    @FXML
    private Label totalTime;

    @FXML
    private Label typeLabel;

    @FXML
    private ChoiceBox<?> workerChoice;

    @FXML
    void exitButtonPressed(ActionEvent event) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Operación no recuperable");
        alert.setHeaderText("Realmente deseas guardar esta planificacion?");
        alert.setContentText("¿Estás seguro?");
        ButtonType buttonTypeOne = new ButtonType("SI, Guardar y salir");
        ButtonType buttonTypeTwo = new ButtonType("NO, seguir editando");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOne) {
            edited.setDefaultFlder();
            edited.setTaskList(new ArrayList<>());
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }
        // App.editedModel = null;
    }

    @FXML
    void nextDayPressed(ActionEvent event) {

    }

    @FXML
    void prevDayPressed(ActionEvent event) {

    }

    @FXML
    public static Border normalBorder;

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
            toggleButton.setStyle("-fx-font-size: 12px;-fx-text-fill: #7c7c7c;");

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
    private void pressed(TaskType tsk) {

        for (var node : root.getChildren()) {
            ToggleButton tgb = (ToggleButton) node;
            if (tgb.getId().equals(Integer.toString(tsk.getId()))){
                tgb.setSelected(true);
            }
                
            else
                tgb.setSelected(false);
        }
        selectedTask = tsk;
        System.out.println(tsk.getName());
        System.out.println();
    }

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

    private void refresh() {

    }

    @FXML
    void initialize() {
        edited = App.editedOrder;
        
        adminModel.plan(edited.getTaskList());
        orderLabel.setText(edited.getName());
        Type modelOf = adminModel.getModelById(edited.getModelOf());
        modelLabel.setText(modelOf.getName());
        Type typeOf = adminModel.getTypeById(modelOf.getModelOf());
        typeLabel.setText(typeOf.getName());
        deps = new HashSet<>();
        selectedTask = edited.getTaskList().get(0);
        generateButtons(order());
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

        refresh();

        assert dateLabel != null : "fx:id=\"dateLabel\" was not injected: check your FXML file 'plan.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'plan.fxml'.";
        assert modelLabel != null : "fx:id=\"modelLabel\" was not injected: check your FXML file 'plan.fxml'.";
        assert nextDay != null : "fx:id=\"nextDay\" was not injected: check your FXML file 'plan.fxml'.";
        assert orderLabel != null : "fx:id=\"orderLabel\" was not injected: check your FXML file 'plan.fxml'.";
        assert percentLabel != null : "fx:id=\"percentLabel\" was not injected: check your FXML file 'plan.fxml'.";
        assert prevDay != null : "fx:id=\"prevDay\" was not injected: check your FXML file 'plan.fxml'.";
        assert taskDateLabel != null : "fx:id=\"taskDateLabel\" was not injected: check your FXML file 'plan.fxml'.";
        assert totalTime != null : "fx:id=\"totalTime\" was not injected: check your FXML file 'plan.fxml'.";
        assert typeLabel != null : "fx:id=\"typeLabel\" was not injected: check your FXML file 'plan.fxml'.";
        assert workerChoice != null : "fx:id=\"workerChoice\" was not injected: check your FXML file 'plan.fxml'.";

    }

}
