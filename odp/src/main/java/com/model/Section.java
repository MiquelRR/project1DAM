package com.model;

import java.util.List;

public class Section {
    int id;
    String name;
    WeekTemplate weekTemplate; 
    List<Day> calendar;
    
    public List<Day> getCalendar() {
        return calendar;
    }
    public void setCalendar(List<Day> calendar) {
        this.calendar = calendar;
    }
    public Section(int id, String name) {
        this.id = id;
        this.name = name;
        
    }
    @Override
    public String toString(){
        return name;
    }

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

  public WeekTemplate getWeekTemplate() {
        return weekTemplate;
    }

    public void setWeekTemplate(WeekTemplate weekTemplate) {
        this.weekTemplate = weekTemplate;
    } 
    
}
