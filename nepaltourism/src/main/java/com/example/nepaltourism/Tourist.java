package com.example.nepaltourism;

//package com.nepaltourism.models;

import javafx.beans.property.*;
import java.util.ArrayList;
import java.util.List;

public class Tourist extends User {
    private List<String> bookingIds;
    private DoubleProperty totalSpent;
    private StringProperty preferredLanguage;
    private IntegerProperty totalTrips;

    public Tourist(String id, String name, String email, String password, String phone) {
        super(id, name, email, password, phone);
        this.bookingIds = new ArrayList<>();
        this.totalSpent = new SimpleDoubleProperty(0.0);
        this.preferredLanguage = new SimpleStringProperty("English");
        this.totalTrips = new SimpleIntegerProperty(0);
    }

    @Override
    public String getUserType() { return "Tourist"; }

    // Property getters
    public DoubleProperty totalSpentProperty() { return totalSpent; }
    public StringProperty preferredLanguageProperty() { return preferredLanguage; }
    public IntegerProperty totalTripsProperty() { return totalTrips; }

    // Value getters and setters
    public List<String> getBookingIds() { return bookingIds; }
    public void addBooking(String bookingId) {
        this.bookingIds.add(bookingId);
        this.totalTrips.set(this.bookingIds.size());
    }

    public double getTotalSpent() { return totalSpent.get(); }
    public void setTotalSpent(double totalSpent) { this.totalSpent.set(totalSpent); }

    public String getPreferredLanguage() { return preferredLanguage.get(); }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage.set(preferredLanguage); }

    public int getTotalTrips() { return totalTrips.get(); }
    public void setTotalTrips(int totalTrips) { this.totalTrips.set(totalTrips); }
}