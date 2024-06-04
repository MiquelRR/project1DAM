package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Bidimap {
    
    private Map<TaskType, TaskType> map = new HashMap<>();

    public Bidimap (){
        this.map= new HashMap<>();
    }

    public void put(TaskType k, TaskType v){
        this.map.put(k,v);
    }

    public TaskType getValue(TaskType o){
        return this.map.get(o);
    }

    public TaskType getKeyOf(TaskType o){
        
        for (TaskType o2: this.map.keySet()) {
            if(this.map.get(o2).equals(o)) return o2;
        }
        return null;
    }

    public Set<TaskType> keySet(){
        return this.map.keySet();
    }

    public List<TaskType> values(){
        return new ArrayList<>(this.map.values());
    }

}
