package com.example.nepaltourism;

//package com.nepaltourism.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class EmergencyReport {
    private StringProperty id;
    private StringProperty bookingId;
    private StringProperty touristId;
    private StringProperty guideId;
    private StringProperty location;
    private StringProperty description;
    private ObjectProperty<Priority> priority;
    private ObjectProperty<Status> status;
    private ObjectProperty<LocalDateTime> reportedAt;
    private ObjectProperty<LocalDateTime> resolvedAt;
    private StringProperty resolution;

    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Status {
        REPORTED, IN_PROGRESS, RESOLVED, CLOSED
    }

    public EmergencyReport(String id, String bookingId, String touristId, String guideId,
                           String location, String description, Priority priority) {
        this.id = new SimpleStringProperty(id);
        this.bookingId = new SimpleStringProperty(bookingId);
        this.touristId = new SimpleStringProperty(touristId);
        this.guideId = new SimpleStringProperty(guideId);
        this.location = new SimpleStringProperty(location);
        this.description = new SimpleStringProperty(description);
        this.priority = new SimpleObjectProperty<>(priority);
        this.status = new SimpleObjectProperty<>(Status.REPORTED);
        this.reportedAt = new SimpleObjectProperty<>(LocalDateTime.now());
        this.resolution = new SimpleStringProperty("");
    }

    // Property getters
    public StringProperty idProperty() { return id; }
    public StringProperty bookingIdProperty() { return bookingId; }
    public StringProperty touristIdProperty() { return touristId; }
    public StringProperty guideIdProperty() { return guideId; }
    public StringProperty locationProperty() { return location; }
    public StringProperty descriptionProperty() { return description; }
    public ObjectProperty<Priority> priorityProperty() { return priority; }
    public ObjectProperty<Status> statusProperty() { return status; }
    public StringProperty resolutionProperty() { return resolution; }

    // Value getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getBookingId() { return bookingId.get(); }
    public void setBookingId(String bookingId) { this.bookingId.set(bookingId); }

    public String getTouristId() { return touristId.get(); }
    public void setTouristId(String touristId) { this.touristId.set(touristId); }

    public String getGuideId() { return guideId.get(); }
    public void setGuideId(String guideId) { this.guideId.set(guideId); }

    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public Priority getPriority() { return priority.get(); }
    public void setPriority(Priority priority) { this.priority.set(priority); }

    public Status getStatus() { return status.get(); }
    public void setStatus(Status status) {
        this.status.set(status);
        if (status == Status.RESOLVED || status == Status.CLOSED) {
            this.resolvedAt = new SimpleObjectProperty<>(LocalDateTime.now());
        }
    }

    public LocalDateTime getReportedAt() { return reportedAt.get(); }
    public LocalDateTime getResolvedAt() { return resolvedAt != null ? resolvedAt.get() : null; }

    public String getResolution() { return resolution.get(); }
    public void setResolution(String resolution) { this.resolution.set(resolution); }

    public String getPriorityText() {
        return priority.get().toString();
    }

    public String getStatusText() {
        return status.get().toString().replace("_", " ");
    }
}