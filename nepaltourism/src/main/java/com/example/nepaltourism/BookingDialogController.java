package com.example.nepaltourism;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.nepaltourism.*;

import java.time.LocalDate;
import java.util.List;

public class BookingDialogController {
    @FXML private Label attractionNameLabel;
    @FXML private Label attractionDetailsLabel;
    @FXML private ComboBox<Guide> guideComboBox;
    @FXML private DatePicker tourDatePicker;
    @FXML private Spinner<Integer> peopleSpinner;
    @FXML private Label totalPriceLabel;
    @FXML private TextArea specialRequestsArea;
    @FXML private VBox discountBox;
    @FXML private Label discountLabel;
    @FXML private VBox safetyBox;
    @FXML private Label safetyLabel;
    @FXML private Button cancelButton;
    @FXML private Button bookButton;

    private Stage dialogStage;
    private Attraction attraction;
    private User currentUser;
    private DataManager dataManager;
    private boolean confirmed = false;

    public void initialize() {
        dataManager = DataManager.getInstance();
        setupEventHandlers();
        setupSpinner();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
        loadAttractionDetails();
        loadAvailableGuides();
        calculatePrice();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private void setupEventHandlers() {
        cancelButton.setOnAction(e -> handleCancel());
        bookButton.setOnAction(e -> handleBook());

        // Update price when people count changes
        peopleSpinner.valueProperty().addListener((obs, oldVal, newVal) -> calculatePrice());

        // Set minimum date to today
        tourDatePicker.setValue(LocalDate.now().plusDays(1));
        tourDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }

    private void setupSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        peopleSpinner.setValueFactory(valueFactory);
    }

    private void loadAttractionDetails() {
        if (attraction != null) {
            attractionNameLabel.setText(attraction.getName());
            attractionDetailsLabel.setText(String.format("%s • %s • %s • Difficulty: %s",
                    attraction.getRegion(),
                    attraction.getCategory(),
                    attraction.getDurationText(),
                    attraction.getDifficulty()));

            // Show safety information based on difficulty
            if (attraction.getDifficulty().equals("Hard")) {
                safetyLabel.setText("High altitude trek - Proper acclimatization and fitness required. Travel insurance mandatory.");
                safetyBox.setVisible(true);
            } else if (attraction.getDifficulty().equals("Medium")) {
                safetyLabel.setText("Moderate trek - Good physical fitness recommended. Basic trekking gear required.");
                safetyBox.setVisible(true);
            } else {
                safetyBox.setVisible(false);
            }

            // Show discount if applicable (sample logic)
            if (attraction.getPrice() > 2000) {
                discountLabel.setText("10% early bird discount for bookings made 30 days in advance!");
                discountBox.setVisible(true);
            } else {
                discountBox.setVisible(false);
            }
        }
    }

    private void loadAvailableGuides() {
        if (attraction != null) {
            List<Guide> availableGuides = dataManager.getAvailableGuides().stream()
                    .filter(guide -> guide.getTourArea().equals(attraction.getRegion()))
                    .toList();

            guideComboBox.setItems(FXCollections.observableArrayList(availableGuides));

            // Custom cell factory to display guide information
            guideComboBox.setCellFactory(listView -> new ListCell<Guide>() {
                @Override
                protected void updateItem(Guide guide, boolean empty) {
                    super.updateItem(guide, empty);
                    if (empty || guide == null) {
                        setText(null);
                    } else {
                        setText(String.format("%s (★%.1f, %s)",
                                guide.getName(), guide.getRating(), guide.getExperience()));
                    }
                }
            });

            guideComboBox.setButtonCell(new ListCell<Guide>() {
                @Override
                protected void updateItem(Guide guide, boolean empty) {
                    super.updateItem(guide, empty);
                    if (empty || guide == null) {
                        setText("Select a guide...");
                    } else {
                        setText(String.format("%s (★%.1f)", guide.getName(), guide.getRating()));
                    }
                }
            });

            // Select first available guide by default
            if (!availableGuides.isEmpty()) {
                guideComboBox.setValue(availableGuides.get(0));
            }
        }
    }

    private void calculatePrice() {
        if (attraction != null) {
            int people = peopleSpinner.getValue();
            double basePrice = attraction.getPrice() * people;
            double discount = 0.0;

            // Apply discount if booking is made in advance
            LocalDate tourDate = tourDatePicker.getValue();
            if (tourDate != null && tourDate.isAfter(LocalDate.now().plusDays(30))) {
                discount = basePrice * 0.10; // 10% discount
            }

            double finalPrice = basePrice - discount;

            if (discount > 0) {
                totalPriceLabel.setText(String.format("$%.0f (Save $%.0f)", finalPrice, discount));
                totalPriceLabel.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold; -fx-font-size: 18px;");
            } else {
                totalPriceLabel.setText(String.format("$%.0f", finalPrice));
                totalPriceLabel.setStyle("-fx-text-fill: #2563eb; -fx-font-weight: bold; -fx-font-size: 18px;");
            }
        }
    }

    @FXML
    private void handleCancel() {
        confirmed = false;
        dialogStage.close();
    }

    @FXML
    private void handleBook() {
        if (validateInput()) {
            createBooking();
            confirmed = true;
            dialogStage.close();
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (guideComboBox.getValue() == null) {
            errors.append("Please select a guide.\n");
        }

        if (tourDatePicker.getValue() == null) {
            errors.append("Please select a tour date.\n");
        } else if (tourDatePicker.getValue().isBefore(LocalDate.now().plusDays(1))) {
            errors.append("Tour date must be at least tomorrow.\n");
        }

        if (errors.length() > 0) {
            showAlert("Validation Error", errors.toString());
            return false;
        }

        return true;
    }

    private void createBooking() {
        Guide selectedGuide = guideComboBox.getValue();
        LocalDate tourDate = tourDatePicker.getValue();
        int people = peopleSpinner.getValue();

        double basePrice = attraction.getPrice() * people;
        double discount = 0.0;

        // Apply discount if applicable
        if (tourDate.isAfter(LocalDate.now().plusDays(30))) {
            discount = basePrice * 0.10;
        }

        double finalPrice = basePrice - discount;

        // Create booking
        String bookingId = dataManager.createBooking(
                currentUser.getId(),
                selectedGuide.getId(),
                attraction.getId(),
                tourDate,
                finalPrice
        );

        // Update booking with special requests and discount
        Booking booking = dataManager.getBookingById(bookingId);
        if (booking != null) {
            booking.setSpecialRequests(specialRequestsArea.getText());
            booking.setDiscountApplied(discount);
            dataManager.updateBooking(booking);
        }

        // Update tourist's total spent
        if (currentUser instanceof Tourist) {
            Tourist tourist = (Tourist) currentUser;
            tourist.setTotalSpent(tourist.getTotalSpent() + finalPrice);
            dataManager.updateUser(tourist);
        }

        // Update attraction visitor count
        attraction.incrementVisitors();
        dataManager.updateAttraction(attraction);

        showAlert("Booking Confirmed",
                String.format("Your booking has been confirmed!\n\n" +
                                "Booking ID: %s\n" +
                                "Guide: %s\n" +
                                "Tour Date: %s\n" +
                                "Total Price: $%.0f\n\n" +
                                "You will receive a confirmation email shortly.",
                        bookingId, selectedGuide.getName(), tourDate, finalPrice));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
