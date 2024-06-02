package com.model;

public class TaskSkill {

    Integer id;
    String name;
    public TaskSkill(Integer id, String name) {
        //System.out.println("nooooooooooooooooooooooooooooooooooom   "+name);
        this.id = id;
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
