package com.model;

public class Rank {
    int id;
    String name;
    
    public Rank(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    
}
