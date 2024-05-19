package com.model;

import java.util.List;

//-------------------------------- Singleton 
public class Staff {
    private static Staff staff; // ---- Singleton
    private List<Worker> staffList;

    private Staff() {

    }

    public static Staff getStaff() {
        if (staff == null)
            staff = new Staff();
        return staff;
    }

}
