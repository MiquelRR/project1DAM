package com.model;

public class RolAndId {
    int idWorker;
    String rol;
    public RolAndId(int idWorker, String rol) {
        this.idWorker = idWorker;
        this.rol = rol;
    }
    public int getIdWorker() {
        return idWorker;
    }
    public void setIdWorker(int idWorker) {
        this.idWorker = idWorker;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }    
}
