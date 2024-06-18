package com.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class WeekTemplate {

    public class InvalidDayException extends RuntimeException {
        public InvalidDayException(String message) {
            super(message);
        }
    }

    int[] workingMinutes;

    public int[] getWorkingMinutes() {
        return workingMinutes;
    }

    public static final Map<String, Integer> DAYS_OF_WEEK = new HashMap<>() {
        {
            put("monday", 0);
            put("tuesday", 1);
            put("wednesday", 2);
            put("thursday", 3);
            put("friday", 4);
            put("saturday", 5);
            put("sunday", 6);
        }
    };

    public WeekTemplate(int monday, int tuesday, int wednesday, int thursday, int friday,
            int saturday) {
        this.workingMinutes = new int[] { monday, tuesday, wednesday, thursday, friday, saturday, 0 };
    }

    public WeekTemplate() {
        this.workingMinutes = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    }

    public boolean setTime(String day, Integer time) {
        day = day.toLowerCase();
        if (DAYS_OF_WEEK.containsKey(day) && time > 0 && time < 900) {
            this.workingMinutes[DAYS_OF_WEEK.get(day)] = time;
            return true;
        } else
            return false;
    }

    public boolean setTime(Integer day, Integer time) {
        if (day >= 0 && day <= 6 && time > 0 && time < 900) {
            this.workingMinutes[day] = time;
            return true;
        } else
            return false;
    }

    public boolean setTime(LocalDate date, Integer time) {
        int day = date.getDayOfWeek().getValue() - 1;
        if (day >= 0 && day <= 6 && time > 0 && time < 900) {
            this.workingMinutes[day] = time;
            return true;
        } else
            return false;
    }

    @Override
    public String toString() {
        String st = "";
        for (String dayname : DAYS_OF_WEEK.keySet()) {
            st += " " + dayname + " : " + this.workingMinutes[DAYS_OF_WEEK.get(dayname)];
        }
        return st;

    }

    public int getTotalTime(int weekday) {
        return this.workingMinutes[weekday];
    }

    public int getTotalTime(String dayName) {
        dayName = dayName.toLowerCase();
        if (DAYS_OF_WEEK.keySet().contains(dayName)) {
            return this.workingMinutes[DAYS_OF_WEEK.get(dayName)];
        } else {
            throw new InvalidDayException("Invalid dayName :" + dayName);
        }

    }

}
