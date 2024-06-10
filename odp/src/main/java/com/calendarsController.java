package com;

import java.io.IOException;
import java.net.URL;
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
        private Label info;

        @FXML
        private Label mondayDate;

        @FXML
        private TextField mondayField;

        @FXML
        private Button nextButton;

        @FXML
        private Button prevButon;

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
        void next(ActionEvent event) {
                int step = (dayWeekAllChoice.getValue() == null || dayWeekAllChoice.getValue().equals("SOLO ESTE DIA"))
                                ? 1
                                : 7;
                selectedDate = dateSelector.getValue();
                selectedDate = selectedDate.plusDays(step);
                dateSelector.setValue(selectedDate);
                refresh();
        }

        @FXML
        void prev(ActionEvent event) {
                int step = (dayWeekAllChoice.getValue() == null || dayWeekAllChoice.getValue().equals("SOLO ESTE DIA"))
                                ? 1
                                : 7;
                selectedDate = dateSelector.getValue();
                selectedDate = selectedDate.minusDays(step);
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
                Boolean hideSels = (dayWeekAllChoice.getValue() == null
                                || dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS"));

                dateSelector.setDisable(hideSels);
                prevButon.setDisable(hideSels);
                nextButton.setDisable(hideSels);

                selectedDate = dateSelector.getValue();
                if (selectedDate == null)
                        selectedDate = LocalDate.now();
                Integer weekDate = selectedDate.getDayOfWeek().getValue() - 1;
                LocalDate monday = selectedDate.minusDays(weekDate);

                int enabled = 8;
                if (dayWeekAllChoice.getValue() == null || dayWeekAllChoice.getValue().equals("SOLO ESTE DIA"))
                        enabled = weekDate;
                for (int i = 0; i < dayField.length; i++) {
                        String tx = (hideSels || !(enabled == 8 || enabled == i)) ? "" : formatLocalDate(monday.plusDays(i));
                        dayLabel[i].setText(tx);
                        
                }
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

                dayLabel = new Label[] { mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate,
                                saturdayDate };

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

        }

}
