package com;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import com.model.AdminModel;
import com.model.Section;
import com.model.Worker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class calendarsController {
    static LocalDate selectedDate;
    AdminModel adminModel = AdminModel.getAdminModel();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button applyButton;

    @FXML
    private ChoiceBox<String> dayWeekAllChoice;

    @FXML
    private Label fridayDate;

    @FXML
    private TextField fridayField;

    @FXML
    private Label mondayDate;

    @FXML
    private TextField mondayField;

    @FXML
    private Button nextWeekButton;

    @FXML
    private Button pervWeekbuton;

    @FXML
    private ChoiceBox<String> reduceChoice;

    @FXML
    private Label saturdayDate;

    @FXML
    private TextField saturdayField;

    @FXML
    private ChoiceBox<Section> sectionChoice;

    @FXML
    private DatePicker dateSelector;

    @FXML
    private Label thursdayDate;

    @FXML
    private TextField thursdayField;

    @FXML
    private TextField tuesdayField;

    @FXML
    private Label tuesdayDate;

    @FXML
    private Label wednesdayDate;

    @FXML
    private TextField wednesdayField;

    @FXML
    private ChoiceBox<Worker> workerChoice;

    @FXML
    static TextField[] dayField;

    @FXML
    static Label[] dayLabel;

    @FXML
    void mondayEdited(ActionEvent event) {
        edited(0);
    }

    @FXML
    void tuesdayEdited(ActionEvent event) {
        edited(1);
    }

    @FXML
    void wednesdayEdited(ActionEvent event) {
        edited(2);
    }

    @FXML
    void thursdayEdited(ActionEvent event) {
        edited(3);
    }

    @FXML
    void fridayEdited(ActionEvent event) {
        edited(4);
    }

    @FXML
    void saturdayEdited(ActionEvent event) {
        edited(4);
    }

    void edited(int weekDay) {

    }

    @FXML
    void applyMask(ActionEvent event) throws IOException {
        App.setRoot("adminMenu");

    }

    @FXML
    void changedDates(ActionEvent event) {
        refresh();

    }

    @FXML
    void checkChoices(ActionEvent event) {
        refresh();

    }

    @FXML
    void nextWeek(ActionEvent event) {
        selectedDate = dateSelector.getValue();
        selectedDate = selectedDate.plusDays(7);
        dateSelector.setValue(selectedDate);
        refresh();
    }

    @FXML
    void prevWeek(ActionEvent event) {
        selectedDate = dateSelector.getValue();
        selectedDate = selectedDate.minusDays(7);
        dateSelector.setValue(selectedDate);
        refresh();

    }

    public static String formatLocalDate(LocalDate date) {
        int dayOfMonth = date.getDayOfMonth();
        String month = date.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
        return String.format("%d-%s", dayOfMonth, month);
    }

    @SuppressWarnings("null")
    void refresh() {

        selectedDate = dateSelector.getValue();
        if (selectedDate == null)
            selectedDate = LocalDate.now();
        Integer weekDate = selectedDate.getDayOfWeek().getValue() + 1;
        LocalDate monday = selectedDate.minusDays(weekDate);
        for (int i = 0; i < dayField.length; i++) {
            dayLabel[i].setText(formatLocalDate(monday.plusDays(i)));
        }
        int enabled = 8;
        if (dayWeekAllChoice.getValue() == null || dayWeekAllChoice.getValue().equals("SOLO ESTE DIA"))
            enabled = weekDate;
        for (int i = 0; i < dayField.length; i++) {
            dayField[i].setDisable(!(enabled == 8 || enabled == i));
        }
        if (sectionChoice.getValue() == null || sectionChoice.getValue().equals(solo))
            workerChoice.setDisable(false);
        else
            workerChoice.setDisable(true);

    }

    static Boolean initzialitzed = false;
    static Section solo;

    @FXML
    void initialize() {

        initzialitzed = true;

        dayField = new TextField[] { mondayField, tuesdayField, wednesdayField, thursdayField, fridayField,
                saturdayField };

        dayLabel = new Label[] { mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate, saturdayDate };

        Section todos = new Section(-100, "TODA LA PLANTILLA");
        solo = new Section(-101, "SOLO UN TRABAJADOR --->");
        sectionChoice.getItems().setAll(adminModel.getSections());
        sectionChoice.getItems().addAll(solo, todos);
        sectionChoice.getSelectionModel().selectLast();
        workerChoice.getItems().setAll(adminModel.getActiveWorkers());
        workerChoice.getSelectionModel().selectLast();
        workerChoice.setDisable(true);
        reduceChoice.getItems().setAll("SOLO REDUCE HORARIO", "REDUCE y/o AMPLIA");
        reduceChoice.getSelectionModel().selectLast();
        dayWeekAllChoice.getItems().setAll("SOLO ESTE DIA", "LA SEMANA MOSTRADA", "PARA TODAS LAS SEMANAS");
        dayWeekAllChoice.getSelectionModel().selectLast();
        dateSelector.setValue(LocalDate.now());
        for (int i = 0; i < dayField.length; i++) {
            dayField[i].setDisable(true);
        }

        refresh();

        assert applyButton != null
                : "fx:id=\"applyButton\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert dayWeekAllChoice != null
                : "fx:id=\"dayWeekAllChoice\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert fridayDate != null
                : "fx:id=\"fridayDate\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert fridayField != null
                : "fx:id=\"fridayField\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert mondayDate != null
                : "fx:id=\"mondayDate\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert mondayField != null
                : "fx:id=\"mondayField\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert nextWeekButton != null
                : "fx:id=\"nextWeekButton\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert pervWeekbuton != null
                : "fx:id=\"pervWeekbuton\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert reduceChoice != null
                : "fx:id=\"reduceChoice\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert saturdayDate != null
                : "fx:id=\"saturdayDate\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert saturdayField != null
                : "fx:id=\"saturdayField\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert sectionChoice != null
                : "fx:id=\"sectionChoice\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert dateSelector != null
                : "fx:id=\"dateSelector1\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert thursdayDate != null
                : "fx:id=\"thursdayDate\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert thursdayField != null
                : "fx:id=\"thursdayField\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert tuesdayField != null
                : "fx:id=\"tuesdayField\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert tuesdayDate != null
                : "fx:id=\"tuesdayDate\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert wednesdayDate != null
                : "fx:id=\"wednesdayDate\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert wednesdayField != null
                : "fx:id=\"wednesdayField\" was not injected: check your FXML file 'calendarsEdit.fxml'.";
        assert workerChoice != null
                : "fx:id=\"workerChoice\" was not injected: check your FXML file 'calendarsEdit.fxml'.";

    }

}
