package com.model;

import java.time.LocalDate;
import java.util.List;

public class Worker {
    String userName;
    String fullName;
    String passwd;
    LocalDate since;
    String ssNum;
    String dni;
    Section section;
    Rank rank;
    String address;
    String telNum;
    String mail;
    String contact;
    String docFolder;
    Boolean active;
    List<Day> calendar;
    List<TaskType> abilities;
    WeekTemplate weekTemplate;
    String rol;
    public Worker(String userName, String fullName, String passwd, String ssNum, String dni,
             String address, String telNum, String mail, String contact, String docFolder,
            Boolean active, String rol) {
        this.userName = userName;
        this.fullName = fullName;
        this.passwd = passwd;
        this.ssNum = ssNum;
        this.dni = dni;
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
    public Section getSection() {
        return section;
    }
    public void setSection(Section section) {
        this.section = section;
    }
    public Rank getRank() {
        return rank;
    }
    public void setRank(Rank rank) {
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
    public List<TaskType> getAbilities() {
        return abilities;
    }
    public void setAbilities(List<TaskType> abilities) {
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

}
