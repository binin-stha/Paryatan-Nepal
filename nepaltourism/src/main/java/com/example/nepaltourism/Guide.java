package com.example.nepaltourism;

//package com.nepaltourism.models;

import javafx.beans.property.*;
import java.util.ArrayList;
import java.util.List;

public class Guide extends User {
    private StringProperty tourArea;
    private StringProperty experience;
    private List<String> languages;
    private BooleanProperty isAvailable;
    private DoubleProperty rating;
    private IntegerProperty totalTours;
    private DoubleProperty totalEarnings;

    public Guide(String id, String name, String email, String password, String phone, String tourArea) {
        super(id, name, email, password, phone);
        this.tourArea = new SimpleStringProperty(tourArea);
        this.experience = new SimpleStringProperty("0 years");
        this.languages = new ArrayList<>();
        this.languages.add("English");
        this.languages.add("Nepali");
        this.isAvailable = new SimpleBooleanProperty(true);
        this.rating = new SimpleDoubleProperty(5.0);
        this.totalTours = new SimpleIntegerProperty(0);
        this.totalEarnings = new SimpleDoubleProperty(0.0);
    }

    @Override
    public String getUserType() { return "Guide"; }

    // Property getters
    public StringProperty tourAreaProperty() { return tourArea; }
    public StringProperty experienceProperty() { return experience; }
    public BooleanProperty isAvailableProperty() { return isAvailable; }
    public DoubleProperty ratingProperty() { return rating; }
    public IntegerProperty totalToursProperty() { return totalTours; }
    public DoubleProperty totalEarningsProperty() { return totalEarnings; }

    // Value getters and setters
    public String getTourArea() { return tourArea.get(); }
    public void setTourArea(String tourArea) { this.tourArea.set(tourArea); }

    public String getExperience() { return experience.get(); }
    public void setExperience(String experience) { this.experience.set(experience); }

    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }

    public boolean isAvailable() { return isAvailable.get(); }
    public void setAvailable(boolean available) { isAvailable.set(available); }

    public double getRating() { return rating.get(); }
    public void setRating(double rating) { this.rating.set(rating); }

    public int getTotalTours() { return totalTours.get(); }
    public void setTotalTours(int totalTours) { this.totalTours.set(totalTours); }

    public double getTotalEarnings() { return totalEarnings.get(); }
    public void setTotalEarnings(double totalEarnings) { this.totalEarnings.set(totalEarnings); }

    public String getAvailabilityStatus() {
        return isAvailable.get() ? "Available" : "Busy";
    }
}
