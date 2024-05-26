package com.model;

import java.util.HashMap;
import java.util.Map;

public class WeekTemplate {

    public class InvalidDayException extends RuntimeException {
        public InvalidDayException(String message) {
            super(message);
        }
    }

    int[] workingMinutes;

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
        this.workingMinutes = new int[] { monday, tuesday, wednesday, thursday, friday, saturday ,0};
    }
@Override
    public String toString(){
    String st = "";
    for (String dayname : DAYS_OF_WEEK.keySet()) {
        st += " "+dayname+" : "+this.workingMinutes[DAYS_OF_WEEK.get(dayname)];
    }
    return st;

    }

    public int getTotalTime(int weekday) {
        return this.workingMinutes[weekday];
    }

    public int getTotalTime(String dayName){
        dayName=dayName.toLowerCase();
        if(DAYS_OF_WEEK.keySet().contains(dayName)){
            return this.workingMinutes[DAYS_OF_WEEK.get(dayName)];
        } else {
            throw new InvalidDayException( "Invalid dayName :"+dayName);
        }

    }

}
