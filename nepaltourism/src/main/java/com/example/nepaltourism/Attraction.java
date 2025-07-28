package com.example.nepaltourism;

//package com.nepaltourism.models;

import javafx.beans.property.*;
import java.util.ArrayList;
import java.util.List;

public class Attraction {
    private StringProperty id;
    private StringProperty name;
    private StringProperty region;
    private StringProperty category;
    private StringProperty description;
    private StringProperty difficulty;
    private IntegerProperty duration;
    private DoubleProperty price;
    private DoubleProperty rating;
    private IntegerProperty totalVisitors;
    private List<String> images;
    private BooleanProperty isActive;

    public Attraction(String id, String name, String region, String category,
                      String description, String difficulty, int duration, double price) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.region = new SimpleStringProperty(region);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.difficulty = new SimpleStringProperty(difficulty);
        this.duration = new SimpleIntegerProperty(duration);
        this.price = new SimpleDoubleProperty(price);
        this.rating = new SimpleDoubleProperty(4.5);
        this.totalVisitors = new SimpleIntegerProperty(0);
        this.images = new ArrayList<>();
        this.isActive = new SimpleBooleanProperty(true);
    }

    // Property getters
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty regionProperty() { return region; }
    public StringProperty categoryProperty() { return category; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty difficultyProperty() { return difficulty; }
    public IntegerProperty durationProperty() { return duration; }
    public DoubleProperty priceProperty() { return price; }
    public DoubleProperty ratingProperty() { return rating; }
    public IntegerProperty totalVisitorsProperty() { return totalVisitors; }
    public BooleanProperty isActiveProperty() { return isActive; }

    // Value getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getRegion() { return region.get(); }
    public void setRegion(String region) { this.region.set(region); }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public String getDifficulty() { return difficulty.get(); }
    public void setDifficulty(String difficulty) { this.difficulty.set(difficulty); }

    public int getDuration() { return duration.get(); }
    public void setDuration(int duration) { this.duration.set(duration); }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }

    public double getRating() { return rating.get(); }
    public void setRating(double rating) { this.rating.set(rating); }

    public int getTotalVisitors() { return totalVisitors.get(); }
    public void setTotalVisitors(int totalVisitors) { this.totalVisitors.set(totalVisitors); }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public boolean isActive() { return isActive.get(); }
    public void setActive(boolean active) { isActive.set(active); }

    public void incrementVisitors() { this.totalVisitors.set(this.totalVisitors.get() + 1); }

    public String getDurationText() {
        int days = duration.get();
        return days + (days == 1 ? " day" : " days");
    }

    public String getPriceText() {
        return String.format("$%.0f", price.get());
    }

    public String getStatusText() {
        return isActive.get() ? "Active" : "Inactive";
    }
}
