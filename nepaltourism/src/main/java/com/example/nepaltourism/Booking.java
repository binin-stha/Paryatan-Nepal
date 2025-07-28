package com.example.nepaltourism;

//package com.nepaltourism.models;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
    private StringProperty id;
    private StringProperty touristId;
    private StringProperty guideId;
    private StringProperty attractionId;
    private ObjectProperty<LocalDate> bookingDate;
    private ObjectProperty<LocalDate> tourDate;
    private ObjectProperty<BookingStatus> status;
    private DoubleProperty totalPrice;
    private DoubleProperty discountApplied;
    private StringProperty specialRequests;
    private ObjectProperty<LocalDateTime> createdAt;
    private ObjectProperty<LocalDateTime> updatedAt;

    public enum BookingStatus {
        PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public Booking(String id, String touristId, String guideId, String attractionId,
                   LocalDate tourDate, double totalPrice) {
        this.id = new SimpleStringProperty(id);
        this.touristId = new SimpleStringProperty(touristId);
        this.guideId = new SimpleStringProperty(guideId);
        this.attractionId = new SimpleStringProperty(attractionId);
        this.bookingDate = new SimpleObjectProperty<>(LocalDate.now());
        this.tourDate = new SimpleObjectProperty<>(tourDate);
        this.status = new SimpleObjectProperty<>(BookingStatus.PENDING);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
        this.discountApplied = new SimpleDoubleProperty(0.0);
        this.specialRequests = new SimpleStringProperty("");
        this.createdAt = new SimpleObjectProperty<>(LocalDateTime.now());
        this.updatedAt = new SimpleObjectProperty<>(LocalDateTime.now());
    }

    // Property getters
    public StringProperty idProperty() { return id; }
    public StringProperty touristIdProperty() { return touristId; }
    public StringProperty guideIdProperty() { return guideId; }
    public StringProperty attractionIdProperty() { return attractionId; }
    public ObjectProperty<LocalDate> bookingDateProperty() { return bookingDate; }
    public ObjectProperty<LocalDate> tourDateProperty() { return tourDate; }
    public ObjectProperty<BookingStatus> statusProperty() { return status; }
    public DoubleProperty totalPriceProperty() { return totalPrice; }
    public DoubleProperty discountAppliedProperty() { return discountApplied; }
    public StringProperty specialRequestsProperty() { return specialRequests; }

    // Value getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getTouristId() { return touristId.get(); }
    public void setTouristId(String touristId) { this.touristId.set(touristId); }

    public String getGuideId() { return guideId.get(); }
    public void setGuideId(String guideId) { this.guideId.set(guideId); }

    public String getAttractionId() { return attractionId.get(); }
    public void setAttractionId(String attractionId) { this.attractionId.set(attractionId); }

    public LocalDate getBookingDate() { return bookingDate.get(); }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate.set(bookingDate); }

    public LocalDate getTourDate() { return tourDate.get(); }
    public void setTourDate(LocalDate tourDate) { this.tourDate.set(tourDate); }

    public BookingStatus getStatus() { return status.get(); }
    public void setStatus(BookingStatus status) {
        this.status.set(status);
        this.updatedAt.set(LocalDateTime.now());
    }

    public double getTotalPrice() { return totalPrice.get(); }
    public void setTotalPrice(double totalPrice) { this.totalPrice.set(totalPrice); }

    public double getDiscountApplied() { return discountApplied.get(); }
    public void setDiscountApplied(double discountApplied) { this.discountApplied.set(discountApplied); }

    public String getSpecialRequests() { return specialRequests.get(); }
    public void setSpecialRequests(String specialRequests) { this.specialRequests.set(specialRequests); }

    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public LocalDateTime getUpdatedAt() { return updatedAt.get(); }

    public double getFinalPrice() {
        return totalPrice.get() - discountApplied.get();
    }

    public String getStatusText() {
        return status.get().toString().replace("_", " ");
    }

    public String getFinalPriceText() {
        return String.format("$%.0f", getFinalPrice());
    }
}
