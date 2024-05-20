package com.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//-------------------------------- Singleton 
public class AdminModel {
    private static AdminModel adminModel; // ---- Singleton
    private List<Worker> staffList;
    private Map<Integer, WeekTemplate> weekTemplates;
    private Map<Integer, String> ranks;
    private Map<Integer,TaskType> taskTypes;

    private LocalDate getExpected(LocalDate today) {
        int nextYear = today.getYear() + 1;
        return YearMonth.of(nextYear, 2).atEndOfMonth();

    }

    private AdminModel() {
        weekTemplates = Accesdb.readWeekTemplates();
        staffList = Accesdb.readWorkers();
        ranks = Accesdb.readRanks();
        taskTypes = Accesdb.readTaskTypes();
        
        LocalDate lastDate = Accesdb.readLastProcessedDate();
        LocalDate today=LocalDate.now();
        LocalDate expected=getExpected(today);
        if (lastDate.isBefore(expected)) {
            for (Worker worker : staffList) {
                List<Day> calendar = new ArrayList<>();
                Integer key = 0;
                if (weekTemplates.keySet().contains(worker.getIdWorker()))
                    key = worker.getIdWorker();
                else if (weekTemplates.keySet().contains(worker.getSection()))
                    key = worker.getSection();
                WeekTemplate wt=weekTemplates.get(key);
                for( LocalDate date= today; !date.isAfter(expected); date = date.plusDays(1)){
                    Day day = new Day(date,wt.getTotalTime(date.getDayOfWeek().getValue()-1));
                    calendar.add(day);
                }
                worker.setCalendar(calendar);
                Accesdb.writeCalendar(worker.getIdWorker(),calendar);
                Accesdb.writeLastProcessedDate(expected);
            }
        } else {
            for (Worker worker : staffList) {
                List<Day> calendar = Accesdb.readCalendarOf(worker.getIdWorker());
                List<Integer> list = Accesdb.readTaskTypeIndexesOf(worker.idWorker);
                List<TaskType> abilities= new ArrayList<>();
                for (Integer key : list) {
                    abilities.add(taskTypes.get(key));
                }
                worker.setAbilities(abilities);
                worker.setCalendar(calendar);
            }
        }

    }

    public static AdminModel getaAdminModel() {
        if (adminModel == null)
        adminModel = new AdminModel();
        return adminModel;
    }

}
