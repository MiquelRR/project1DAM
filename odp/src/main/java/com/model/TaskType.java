package com.model;

import java.util.ArrayList;
import java.util.List;

public class TaskType {
    private Integer id;
    private String name;
    private Integer taskRef;
    private List<TaskType> dependsOn;
    private Integer x;
    private Integer y;
    private Integer prepTime;
    private Integer pieceTime;
    private String  infoFilePath;
    private Integer typeRef;

    public TaskType() {
        this.dependsOn = new ArrayList<>();
    }

    public TaskType(int id, TaskSkill tk, Integer typeRef) {
        this.id = id;
        this.typeRef=typeRef;
        this.taskRef= tk.getId();
        this.name = tk.name;
        this.dependsOn = new ArrayList<>();
        this.pieceTime = 0;
        this.prepTime = 0;
        this.infoFilePath = "";
    }

    public TaskType(Integer id, Integer typeRef, Integer taskRef, String name, String infoFilePath, Integer prepTime, Integer pieceTime ){
        this.id = id;
        this.typeRef=typeRef;
        this.taskRef= taskRef;
        this.name = name;
        this.dependsOn = new ArrayList<>();
        this.pieceTime = pieceTime;
        this.prepTime = prepTime;
        this.infoFilePath = infoFilePath;
    }

    public TaskType(Integer id, Integer typeRef ,TaskType other) {
        this.id = id;
        this.typeRef = typeRef;
        this.taskRef = other.taskRef;
        this.name = other.name;
        this.dependsOn = new ArrayList<>();
        this.x = other.x;
        this.y = other.y;
        this.prepTime = other.prepTime;
        this.pieceTime = other.pieceTime;
        this.infoFilePath = other.infoFilePath;
    }

    public Integer getTypeRef() {
        return typeRef;
    }

    public void setTypeRef(Integer typeRef) {
        this.typeRef = typeRef;
    }

    public String getInfoFilePath() {
        return infoFilePath;
    }

    public void setInfoFilePath(String infoFilePath) {
        this.infoFilePath = infoFilePath;
    }


    public Integer getPieceTime() {
        return pieceTime;
    }

    public void setPieceTime(Integer pieceTime) {
        this.pieceTime = pieceTime;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public List<TaskType> getDependsOn() {
        return dependsOn;
    }

    public void removeDependences() {
        this.dependsOn = new ArrayList<>();
    }

    public List<Integer> getDependsOnIds() {
        List<Integer> list = new ArrayList<>();
        for (TaskType tk : dependsOn) {
            list.add(tk.getId());
        }
        return list;
    }

    public void addDependency(TaskType depends) {
        this.dependsOn.add(depends);
    };

    public void removeDependecy(TaskType dep) {
        this.dependsOn.remove(dep);
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
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

    public void setDependsOn(List<TaskType> dependsOn) {
        this.dependsOn = dependsOn;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getTaskRef() {
        return taskRef;
    }

}