package com.model;

public class Type {
    Integer idType;
    String name;
    String docFolder;
    Integer modelOf;
    
    @Override
    public String toString() {
        return name;
    }

    public Type(Integer idType, String name, String docFolder) {
        this.idType = idType;
        this.name = name;
        this.docFolder = docFolder;
    }


    public Type(Integer idType, String name){
        this.idType = idType;
        this.name = name;
    }
    public Type(Integer idType, String name, Integer modelOf) {
        this.idType = idType;
        this.name = name;
        this.modelOf = modelOf;
    }


    public Type(Integer idType, String name, String docFolder, Integer modelOf) {
        this.idType = idType;
        this.name = name;
        this.docFolder = docFolder;
        this.modelOf = modelOf;
    }

    public Type() {

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
        return docFolder;
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
