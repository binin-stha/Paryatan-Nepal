package com.example.nepaltourism;

import javafx.beans.property.*;

public class Admin extends User {
    private StringProperty department;
    private StringProperty accessLevel;

    public Admin(String id, String name, String email, String password, String phone) {
        super(id, name, email, password, phone);
        this.department = new SimpleStringProperty("Tourism Management");
        this.accessLevel = new SimpleStringProperty("FULL");
    }

    @Override
    public String getUserType() { return "Admin"; }

    // Property getters
    public StringProperty departmentProperty() { return department; }
    public StringProperty accessLevelProperty() { return accessLevel; }

    // Value getters and setters
    public String getDepartment() { return department.get(); }
    public void setDepartment(String department) { this.department.set(department); }

    public String getAccessLevel() { return accessLevel.get(); }
    public void setAccessLevel(String accessLevel) { this.accessLevel.set(accessLevel); }
}