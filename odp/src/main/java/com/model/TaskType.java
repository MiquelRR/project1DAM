package com.model;

import java.util.ArrayList;
import java.util.List;


public class TaskType {
    Integer id;
    String name;
    List<TaskType> dependsOn;
    Integer x;
    Integer y;
 

 
    public List<TaskType> getDependsOn() {
        return dependsOn;
    }

    public List<Integer> getDependsOnIds(){
        List<Integer> list= new ArrayList<>();
        for (TaskType tk : dependsOn) {
            list.add(tk.getId());
        }
        return list;
    }
    
    public void addDependency(TaskType depends) {
        this.dependsOn.add(depends);
    }
    public Integer getId() {
        return id;
    }
    public TaskType(int id , String name) {
        this.id = id;
        this.name = name;
        this.dependsOn =  new ArrayList<>();
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
/*     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskType task = (TaskType) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
 */
}
