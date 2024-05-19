package com.model;

public class WeekTemplate {
    public WeekTemplate(int id, String name, int monday, int tuesday, int wednesday, int thursday, int friday,
            int saturday, int sunday) {
        this.id = id;
        this.name = name;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }
    int id;
    String name;
    int monday;
    int tuesday;
    int wednesday;
    int thursday;
    int friday;
    int saturday;
    int sunday;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getMonday() {
        return monday;
    }
    public void setMonday(int monday) {
        this.monday = monday;
    }
    public int getTuesday() {
        return tuesday;
    }
    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }
    public int getWednesday() {
        return wednesday;
    }
    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }
    public int getThursday() {
        return thursday;
    }
    public void setThursday(int thursday) {
        this.thursday = thursday;
    }
    public int getFriday() {
        return friday;
    }
    public void setFriday(int friday) {
        this.friday = friday;
    }
    public int getSaturday() {
        return saturday;
    }
    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }
    public int getSunday() {
        return sunday;
    }
    public void setSunday(int sunday) {
        this.sunday = sunday;
    }
}
