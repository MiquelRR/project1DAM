package com.model;

public class TaskType {
    Integer id;
    public Integer getId() {
        return id;
    }

    String name;
    public TaskType(int id , String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
