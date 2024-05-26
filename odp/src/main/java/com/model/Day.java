package com.model;

import java.time.LocalDate;

public class Day {
    LocalDate date;
    int workTime; //minutes    
    public Day(LocalDate date, int workTime) {
        this.date = date;
        this.workTime = workTime;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public int getWorkTime() {
        return workTime;
    }
    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }
    @Override
    public String toString() {
        return "Day [date=" + date + ", workTime=" + workTime + "]";
    }
}
