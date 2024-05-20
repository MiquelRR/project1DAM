package com.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//-------------------------------- Singleton 
public class Staff {
    private static Staff staff; // ---- Singleton
    private List<Worker> staffList;
    private Map<Integer, WeekTemplate> weekTemplates;
    private Map<Integer, String> ranks;

    private LocalDate getExpected(LocalDate today) {
        int nextYear = today.getYear() + 1;
        return YearMonth.of(nextYear, 2).atEndOfMonth();

    }

    private Staff() {
        weekTemplates = Accesdb.readWeekTemplates();
        staffList = Accesdb.readWorkers();
        ranks = Accesdb.readRanks();
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
            }
        } ///to do READ EXIST CALENDARS

    }

    public static Staff getStaff() {
        if (staff == null)
            staff = new Staff();
        return staff;
    }

}
