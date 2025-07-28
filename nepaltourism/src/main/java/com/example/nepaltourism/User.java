package com.example.nepaltourism;

//package com.nepaltourism.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected StringProperty id;
    protected StringProperty name;
    protected StringProperty email;
    protected StringProperty password;
    protected StringProperty phone;
    protected ObjectProperty<LocalDateTime> createdAt;
    protected BooleanProperty isActive;

    public User(String id, String name, String email, String password, String phone) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.phone = new SimpleStringProperty(phone);
        this.createdAt = new SimpleObjectProperty<>(LocalDateTime.now());
        this.isActive = new SimpleBooleanProperty(true);
    }

    // Property getters
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public BooleanProperty isActiveProperty() { return isActive; }

    // Value getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }

    public String getPassword() { return password.get(); }
    public void setPassword(String password) { this.password.set(password); }

    public String getPhone() { return phone.get(); }
    public void setPhone(String phone) { this.phone.set(phone); }

    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }

    public boolean isActive() { return isActive.get(); }
    public void setActive(boolean active) { isActive.set(active); }

    public abstract String getUserType();
}
