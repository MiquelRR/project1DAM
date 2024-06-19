package com;
import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import com.model.AdminModel;
import com.model.Section;
import com.model.WeekTemplate;
import com.model.Worker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class calendarsController {
        static boolean canApply;
        static LocalDate selectedDate;
        static WeekTemplate wt;
        static LocalDate monday;
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
        private Button closeButton;

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

        @FXML
        void close(ActionEvent event) throws IOException {

                App.setRoot("adminMenu");

        }

        void edited(int weekDay) {
                refresh();

        }

        @FXML
        void applyMask(ActionEvent event) throws IOException {
                for (int i = 0; i < dayField.length; i++) {
                        wt.getWorkingMinutes()[i] = strToMinutes(dayField[i].getText());
                }
                boolean reduce = !reduceChoice.getValue().equals("REDUCE y/o AMPLIA");
                LocalDate date = dateSelector.getValue();
                Integer mins = wt.getWorkingMinutes()[date.getDayOfWeek().getValue() - 1];
                if (sectionChoice.getValue().equals(solo)) {
                        Worker worker = workerChoice.getValue();
                        if (dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS"))
                                adminModel.modifyAllCalendarWorker(worker, wt, reduce);
                        else if (dayWeekAllChoice.getValue().equals("LA SEMANA MOSTRADA"))
                                adminModel.modifyWeekCalendarWorker(worker, wt, monday, reduce);
                        else if (dayWeekAllChoice.getValue().equals("SOLO ESTE DIA"))
                                adminModel.modifyDayCalendarWorker(worker, date, mins);

                } else {
                        Section section = sectionChoice.getValue();
                        if (dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS"))
                                adminModel.modifyAllCalendarSection(section, wt, reduce);
                        else if (dayWeekAllChoice.getValue().equals("LA SEMANA MOSTRADA"))
                                adminModel.modifyWeekCalendarSection(section, wt, monday, reduce);
                        else if (dayWeekAllChoice.getValue().equals("SOLO ESTE DIA"))
                                adminModel.modifyDayCalendarSection(section, date, mins, reduce);

                }
                App.showDialog("CAMBIOS REALIZADOS");

        }

        @FXML
        void changedDates(ActionEvent event) {
                refresh();

        }

        @FXML
        void checkChoices(Event event) {
                refresh();

        }

        @FXML
        private Pane root;

        @FXML
        void next(ActionEvent event) {
                int step = (dayWeekAllChoice.getValue() == null || dayWeekAllChoice.getValue().equals("SOLO ESTE DIA"))
                                ? 1
                                : 7;
                selectedDate = dateSelector.getValue();
                selectedDate = selectedDate.plusDays(step);
                if (selectedDate.getDayOfWeek().getValue() == 7)
                        selectedDate = selectedDate.plusDays(1);
                if(selectedDate.isAfter(lastDate))
                        selectedDate = lastDate;
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
                if (selectedDate.getDayOfWeek().getValue() == 7)
                        selectedDate = selectedDate.minusDays(1);
                if (selectedDate.isBefore(LocalDate.now()))
                        selectedDate =  LocalDate.now();
                dateSelector.setValue(selectedDate);
                refresh();

        }

        public static String formatLocalDate(LocalDate date) {
                int dayOfMonth = date.getDayOfMonth();
                String month = date.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
                return String.format("%d-%s", dayOfMonth, month);
        }

       
        void refresh() {

                Boolean weekTemplatesEdit = (dayWeekAllChoice.getValue() == null
                                || dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS"));

                dateSelector.setDisable(weekTemplatesEdit);
                selectedDate = dateSelector.getValue();
                prevButon.setDisable(weekTemplatesEdit || selectedDate.isBefore(LocalDate.now().plusDays(1)));
                nextButton.setDisable(weekTemplatesEdit || selectedDate.isAfter(lastDate.minusDays(1))) ;

                
                if (selectedDate == null)
                        selectedDate = LocalDate.now();
                Integer weekDate = selectedDate.getDayOfWeek().getValue() - 1;
                monday = selectedDate.minusDays(weekDate);

                int enabled = 8;
                if (dayWeekAllChoice.getValue() == null || dayWeekAllChoice.getValue().equals("SOLO ESTE DIA"))
                        enabled = weekDate;

                for (int i = 0; i < dayField.length; i++) {
                        String tx = (weekTemplatesEdit || !(enabled == 8 || enabled == i)) ? ""
                                        : formatLocalDate(monday.plusDays(i));
                        dayLabel[i].setText(tx);

                }
                for (int i = 0; i < dayField.length; i++) {
                        dayField[i].setDisable(!(enabled == 8 || enabled == i));
                }
                if (sectionChoice.getValue() == null || sectionChoice.getValue().equals(solo))
                        workerChoice.setDisable(false);
                else
                        workerChoice.setDisable(true);

                // show weektemplate
                Section sec;
                if (sectionChoice.getValue() != null && dayWeekAllChoice.getValue() != null) {
                        if (workerChoice.getValue() != null && sectionChoice.getValue() == solo) {
                                sec = adminModel.getSectionById(workerChoice.getValue().getSection());
                                System.out.println(workerChoice.getValue()+ " belongs to  "+sec);
                        } else
                                sec = sectionChoice.getValue();
                        if (weekTemplatesEdit) {
                                wt = sec.getWeekTemplate();
                                System.out.println(sec+" has this template : "+wt);
                        } else {
                                if (sectionChoice.getValue() == solo) {
                                        wt = adminModel.getWorkerWeek(workerChoice.getValue(), monday);
                                } else {
                                        wt = adminModel.getSectionWeek(sectionChoice.getValue(), monday);
                                }
                        }

                        for (int i = 0; i < dayField.length; i++) {
                                dayField[i].setText(minsToStrHours(wt.getWorkingMinutes()[i]));
                        }

                        if (sectionChoice.getValue() == solo // no sense
                                        && !dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS")) {
                                info.setText("Modifica el horario de " + workerChoice.getValue());
                                info.setTextFill(Color.DARKGRAY);
                                reduceChoice.getSelectionModel().selectLast();
                                reduceChoice.setDisable(true);
                        } else
                                reduceChoice.setDisable(false);

                        if (sectionChoice.getValue() == adminModel.getGeneralSection()
                                        && dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS")
                                        && reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("OJO: aplica en TODOS los empleados. perderás todas las modificaciones");
                                info.setTextFill(Color.RED);
                        }

                        if (sectionChoice.getValue() != adminModel.getGeneralSection()
                                        && sectionChoice.getValue() != solo
                                        && dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS")
                                        && reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("OJO: aplica en empleados de " + sectionChoice.getValue()
                                                + ", perderás modificaciones");
                                info.setTextFill(Color.RED);
                        }

                        if (sectionChoice.getValue() == solo
                                        && dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS")
                                        && reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("OJO: aplica en " + workerChoice.getValue()
                                                + ", perderás modificaciones");
                                info.setTextFill(Color.ORANGE);
                        }
                        if (sectionChoice.getValue() == adminModel.getGeneralSection()
                                        && dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS")
                                        && !reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Reduce el horario en todos los empleados");
                                info.setTextFill(Color.ORANGE);
                        }

                        if (sectionChoice.getValue() != adminModel.getGeneralSection()
                                        && sectionChoice.getValue() != solo
                                        && dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS")
                                        && !reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Reduce el horario de los empleados de " + sectionChoice.getValue());
                                info.setTextFill(Color.DARKGRAY);
                        }

                        if (sectionChoice.getValue() == solo
                                        && dayWeekAllChoice.getValue().equals("PARA TODAS LAS SEMANAS")
                                        && !reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Reduce el horario de :" + workerChoice.getValue());
                                info.setTextFill(Color.DARKGRAY);
                        }
                        if (sectionChoice.getValue() == adminModel.getGeneralSection()
                                        && dayWeekAllChoice.getValue().equals("LA SEMANA MOSTRADA")
                                        && reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Modifica todos los horarios en esta semana");
                                info.setTextFill(Color.ORANGE);
                        }
                        if (sectionChoice.getValue() != adminModel.getGeneralSection()
                                        && sectionChoice.getValue() != solo
                                        && dayWeekAllChoice.getValue().equals("LA SEMANA MOSTRADA")
                                        && reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Modifica los horarios de esta semana de empleados de "
                                                + sectionChoice.getValue());
                                info.setTextFill(Color.ORANGE);
                        }
                        if (sectionChoice.getValue() == solo
                                        && dayWeekAllChoice.getValue().equals("LA SEMANA MOSTRADA")
                                        && reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Modifica el horario de esta semana para " + workerChoice.getValue());
                                info.setTextFill(Color.DARKGRAY);
                        }

                        if (sectionChoice.getValue() == adminModel.getGeneralSection()
                                        && dayWeekAllChoice.getValue().equals("LA SEMANA MOSTRADA")
                                        && !reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Reduce todos los horarios para esta semana");
                                info.setTextFill(Color.ORANGE);
                        }
                        if (sectionChoice.getValue() != adminModel.getGeneralSection()
                                        && sectionChoice.getValue() != solo
                                        && dayWeekAllChoice.getValue().equals("LA SEMANA MOSTRADA")
                                        && !reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Reduce los horarios de esta semana de empleados de "
                                                + sectionChoice.getValue());
                                info.setTextFill(Color.ORANGE);
                        }

                        if (sectionChoice.getValue() == adminModel.getGeneralSection()
                                        && dayWeekAllChoice.getValue().equals("SOLO ESTE DIA")
                                        && reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Modifica todos los horarios de este día");
                                info.setTextFill(Color.DARKGRAY);
                        }
                        if (sectionChoice.getValue() != adminModel.getGeneralSection()
                                        && sectionChoice.getValue() != solo
                                        && dayWeekAllChoice.getValue().equals("SOLO ESTE DIA")
                                        && reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Modifica los horarios en este día de empleados de "
                                                + sectionChoice.getValue());
                                info.setTextFill(Color.DARKGRAY);
                        }
                        if (sectionChoice.getValue() == solo
                                        && dayWeekAllChoice.getValue().equals("SOLO ESTE DIA")
                                        && reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Modifica el horario de " + workerChoice.getValue() + " en este día");
                                info.setTextFill(Color.DARKGRAY);
                        }

                        if (sectionChoice.getValue() == adminModel.getGeneralSection()
                                        && dayWeekAllChoice.getValue().equals("SOLO ESTE DIA")
                                        && !reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Reduce todos los horarios de este día");
                                info.setTextFill(Color.DARKGRAY);
                        }
                        if (sectionChoice.getValue() != adminModel.getGeneralSection()
                                        && sectionChoice.getValue() != solo
                                        && dayWeekAllChoice.getValue().equals("SOLO ESTE DIA")
                                        && !reduceChoice.getValue().equals("REDUCE y/o AMPLIA")) {
                                info.setText("Reduce los horarios en este día de empleados de "
                                                + sectionChoice.getValue());
                                info.setTextFill(Color.DARKGRAY);
                        }
                }
                refreshBorders();

        }

        void refreshBorders() {
                canApply = true;
                for (TextField field : dayField) {
                        if (strToMinutes(field.getText()) == null) {
                                canApply = false;
                                field.setBorder(App.ORANGE_BORDER);
                        } else
                                field.setBorder(null);

                }
                applyButton.setDisable(!canApply);
        }

        protected String minsToStrHours(int mins) {
                double hours = (double) mins / 60;
                return String.format(Locale.US, "%.2f", hours);
        }

        protected Integer strToMinutes(String str) {
                try {
                        double value = Double.parseDouble(str);
                        if (value >= 0.00 && value <= 15.00) {
                                return (int) (value * 60);
                        }
                } catch (NumberFormatException e) {
                        // ignore exception
                }
                return null;
        }

        static Boolean initialized = false;
        static Section solo;
        static LocalDate lastDate;
        @FXML
        void initialize() {
                lastDate=adminModel.getLastGeneratedDate();
                Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(LocalDate.now()) || item.isAfter(lastDate)) {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;"); 
                                }

                        }
                };
                dateSelector.setDayCellFactory(dayCellFactory);
                canApply = false;
                for (javafx.scene.Node node : root.getChildren()) {
                        if (node instanceof TextField) {
                                TextField textField = (TextField) node;
                                textField.textProperty().addListener(
                                                (ChangeListener<? super String>) new ChangeListener<String>() {
                                                        @Override
                                                        public void changed(
                                                                        ObservableValue<? extends String> observable,
                                                                        String oldValue, String newValue) {
                                                                refreshBorders();
                                                        }
                                                });
                        }
                }

                initialized = true;

                dayField = new TextField[] { mondayField, tuesdayField, wednesdayField, thursdayField, fridayField,
                                saturdayField };

                dayLabel = new Label[] { mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate,
                                saturdayDate };

                solo = new Section(-101, "SOLO UN TRABAJADOR --->");
                sectionChoice.getItems().setAll(adminModel.getSections());
                if(adminModel.getActiveWorkers().isEmpty()) {
                        workerChoice.setVisible(false);
                } else {
                        sectionChoice.getItems().add(solo);
                        workerChoice.getItems().setAll(adminModel.getActiveWorkers());
                        workerChoice.getSelectionModel().selectLast();
                        workerChoice.setDisable(true);
                }
                sectionChoice.getSelectionModel().select(App.getDefaultSection());
                System.out.println("0->" + workerChoice.getValue());
                reduceChoice.getItems().setAll("SOLO REDUCE HORARIO", "REDUCE y/o AMPLIA");
                reduceChoice.getSelectionModel().selectLast();
                dayWeekAllChoice.getItems().setAll("SOLO ESTE DIA", "LA SEMANA MOSTRADA", "PARA TODAS LAS SEMANAS");
                dayWeekAllChoice.getSelectionModel().selectLast();
                dateSelector.setValue(LocalDate.now());
                for (TextField textField : dayField) {
                        textField.setDisable(true);
                }

                refresh();

        }

}
