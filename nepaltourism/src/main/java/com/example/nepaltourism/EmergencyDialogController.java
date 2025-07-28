package com.example.nepaltourism;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.nepaltourism.*;

public class EmergencyDialogController {
    @FXML private TextField locationField;
    @FXML private ComboBox<String> emergencyTypeCombo;
    @FXML private ComboBox<String> priorityCombo;
    @FXML private TextArea descriptionArea;
    @FXML private TextField contactField;
    @FXML private Button cancelButton;
    @FXML private Button submitButton;

    private Stage dialogStage;
    private User currentUser;
    private DataManager dataManager;
    private boolean confirmed = false;

    public void initialize() {
        dataManager = DataManager.getInstance();
        setupEventHandlers();
        setupComboBoxes();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        if (currentUser != null) {
            contactField.setText(currentUser.getPhone());
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private void setupEventHandlers() {
        cancelButton.setOnAction(e -> handleCancel());
        submitButton.setOnAction(e -> handleSubmit());

        // Auto-fill location based on emergency type
        emergencyTypeCombo.setOnAction(e -> {
            String selectedType = emergencyTypeCombo.getValue();
            if (selectedType != null && locationField.getText().isEmpty()) {
                switch (selectedType) {
                    case "Medical Emergency":
                        priorityCombo.setValue("CRITICAL");
                        break;
                    case "Accident":
                        priorityCombo.setValue("HIGH");
                        break;
                    case "Equipment Malfunction":
                        priorityCombo.setValue("MEDIUM");
                        break;
                    default:
                        priorityCombo.setValue("HIGH");
                        break;
                }
            }
        });
    }

    private void setupComboBoxes() {
        // Emergency type combo
        emergencyTypeCombo.getItems().addAll(
                "Medical Emergency",
                "Equipment Malfunction",
                "Weather Related",
                "Lost/Stranded",
                "Accident",
                "Other"
        );

        // Priority combo
        priorityCombo.getItems().addAll("CRITICAL", "HIGH", "MEDIUM", "LOW");
        priorityCombo.setValue("HIGH"); // Default to HIGH priority
    }

    @FXML
    private void handleCancel() {
        confirmed = false;
        dialogStage.close();
    }

    @FXML
    private void handleSubmit() {
        if (validateInput()) {
            createEmergencyReport();
            confirmed = true;

            // Show confirmation with emergency contact info
            showEmergencyConfirmation();
            dialogStage.close();
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (locationField.getText().trim().isEmpty()) {
            errors.append("Please enter your current location.\n");
        }

        if (emergencyTypeCombo.getValue() == null) {
            errors.append("Please select emergency type.\n");
        }

        if (priorityCombo.getValue() == null) {
            errors.append("Please select priority level.\n");
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            errors.append("Please describe the emergency situation.\n");
        }

        if (contactField.getText().trim().isEmpty()) {
            errors.append("Please provide a contact number.\n");
        }

        if (errors.length() > 0) {
            showAlert("Validation Error", errors.toString());
            return false;
        }

        return true;
    }

    private void createEmergencyReport() {
        // Find current booking for the user (if any)
        String bookingId = findCurrentBooking();
        String guideId = findAssignedGuide(bookingId);

        EmergencyReport.Priority priority = EmergencyReport.Priority.valueOf(priorityCombo.getValue());

        String reportId = dataManager.createEmergencyReport(
                bookingId != null ? bookingId : "",
                currentUser.getId(),
                guideId != null ? guideId : "",
                locationField.getText().trim(),
                descriptionArea.getText().trim()
        );

        // Update the priority
        EmergencyReport report = dataManager.getAllEmergencyReports().stream()
                .filter(r -> r.getId().equals(reportId))
                .findFirst()
                .orElse(null);

        if (report != null) {
            report.setPriority(priority);
        }
    }

    private String findCurrentBooking() {
        // Find the most recent confirmed or in-progress booking for the user
        return dataManager.getBookingsByTourist(currentUser.getId()).stream()
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.CONFIRMED ||
                        booking.getStatus() == Booking.BookingStatus.IN_PROGRESS)
                .map(Booking::getId)
                .findFirst()
                .orElse(null);
    }

    private String findAssignedGuide(String bookingId) {
        if (bookingId != null) {
            Booking booking = dataManager.getBookingById(bookingId);
            return booking != null ? booking.getGuideId() : null;
        }
        return null;
    }

    private void showEmergencyConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Emergency Report Submitted");
        alert.setHeaderText("Your emergency report has been submitted successfully!");

        String message = String.format(
                "Emergency services and your assigned guide have been notified.\n\n" +
                        "Emergency Hotline: +977-1-4411188\n" +
                        "Tourist Police: +977-1-4247041\n" +
                        "Rescue Coordination: +977-1-4411188\n\n" +
                        "Priority: %s\n" +
                        "Location: %s\n\n" +
                        "Please stay in a safe location and keep your phone accessible.\n" +
                        "Help is on the way!",
                priorityCombo.getValue(),
                locationField.getText()
        );

        alert.setContentText(message);

        // Make the alert more prominent for emergencies
        if (priorityCombo.getValue().equals("CRITICAL")) {
            alert.getDialogPane().setStyle("-fx-background-color: #fee2e2; -fx-border-color: #ef4444;");
        }

        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}