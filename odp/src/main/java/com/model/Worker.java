package com.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.App;

public class Worker {
    int idWorker;

    public int getIdWorker() {
        return idWorker;
    }

    public void setIdWorker(int idWorker) {
        this.idWorker = idWorker;
    }

    String userName;
    String fullName;
    String passwd;
    LocalDate since;
    String ssNum;
    String dni;
    Integer section;
    Integer rank;
    String address;
    String telNum;
    String mail;
    String contact;
    String docFolder;
    String type;

    Boolean active = true;
    List<Day> calendar = new ArrayList<>();
    List<TaskSkill> abilities = new ArrayList<>();
    WeekTemplate weekTemplate;
    String rol;

    public Worker() {
        this.docFolder=App.WORKERS_FOLDER;
        
    }

    public Worker(String idWorker, String userName, String fullName, String passwd, String since, String ssNum,
            String dni, String section, String rank, String address, String telNum, String mail, String contact,
            String docFolder, String active, String type, String rol) {
        this.idWorker = Integer.parseInt(idWorker);
        this.userName = userName;
        this.fullName = fullName;
        this.passwd = passwd;
        LocalDate date = (since != null) ? LocalDate.parse(since) : null;
        this.since = date;
        this.ssNum = ssNum;
        this.dni = dni;
        Integer sc = (section != null) ? Integer.parseInt(section) : null;
        this.section = sc;
        Integer rk = (rank != null) ? Integer.parseInt(rank) : null;
        this.rank = rk;
        this.address = address;
        this.telNum = telNum;
        this.mail = mail;
        this.contact = contact;
        this.docFolder = docFolder;
        this.active = active != null && active.equals("YES");
        this.type = type;
        this.rol = rol;
        
    }

    public Worker(int idWorker, String userName, String fullName, String passwd, String ssNum, String dni,
            Integer section,
            String address, String telNum, String mail, String contact, String docFolder,
            Boolean active, String rol) {
        this.idWorker = idWorker;
        this.userName = userName;
        this.fullName = fullName;
        this.passwd = passwd;
        this.ssNum = ssNum;
        this.dni = dni;
        this.section = section;
        this.address = address;
        this.telNum = telNum;
        this.mail = mail;
        this.contact = contact;
        this.docFolder = docFolder;
        this.active = active;
        this.rol = rol;
        
    }

    public String getUserName() {
        return userName;
    }

    public void addSkill(TaskSkill skill){
        this.abilities.add(skill);
    }
    public boolean removeSkill(TaskSkill skill){
        return this.abilities.remove(skill);
    }

    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public LocalDate getSince() {
        return since;
    }

    public void setSince(LocalDate since) {
        this.since = since;
    }

    public String getSsNum() {
        return ssNum;
    }

    public void setSsNum(String ssNum) {
        this.ssNum = ssNum;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDocFolder() {
        return docFolder;
    }

    public void setDocFolder(String docFolder) {
        this.docFolder = docFolder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Day> getCalendar() {
        return calendar;
    }

    public void setCalendar(List<Day> calendar) {
        this.calendar = calendar;
    }

    public List<TaskSkill> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<TaskSkill> abilities) {
        this.abilities = abilities;
    }

    public WeekTemplate getWeekTemplate() {
        return weekTemplate;
    }

    public void setWeekTemplate(WeekTemplate weekTemplate) {
        this.weekTemplate = weekTemplate;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
