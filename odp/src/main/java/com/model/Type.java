package com.model;

import java.util.ArrayList;
import java.util.List;

public class Type {
    Integer idType;
    String name;
    String docFolder;
    Integer modelOf;
    List<TaskType> taskList;

    public List<TaskType> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskType> taskList) {
        this.taskList = taskList;
    }

    final String DEFAULT_FOLDER=".";
    
    public void setDefaultFlder() {
        this.docFolder=DEFAULT_FOLDER;
    }

    @Override
    public String toString() {
        return name;
    }

    

    public Type(Integer idType, String name, String docFolder) {
        this.idType = idType;
        this.name = name;
        this.docFolder = docFolder;
        this.taskList=new ArrayList<>();
    }



    public Type(Integer idType, String name){
        this.idType = idType;
        this.name = name;
        this.docFolder=DEFAULT_FOLDER;
        this.taskList=new ArrayList<>();
    }
    public Type(Integer idType, String name, Integer modelOf) {
        this.idType = idType;
        this.name = name;
        this.modelOf = modelOf;
        this.docFolder=DEFAULT_FOLDER;
        this.taskList=new ArrayList<>();
    }


    public Type(Integer idType, String name, String docFolder, Integer modelOf) {
        this.idType = idType;
        this.name = name;
        this.docFolder = docFolder;
        this.modelOf = modelOf;
        this.taskList=new ArrayList<>();
    }

    public Type() {
        this.docFolder=DEFAULT_FOLDER;
        this.taskList=new ArrayList<>();
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocFolder() {
        return (this.docFolder==null)?DEFAULT_FOLDER:docFolder;
    }

    public void setDocFolder(String docFolder) {
        this.docFolder = docFolder;
    }

    public Integer getModelOf() {
        return modelOf;
    }

    public void setModelOf(Integer modelOf) {
        this.modelOf = modelOf;
    }
        
}
