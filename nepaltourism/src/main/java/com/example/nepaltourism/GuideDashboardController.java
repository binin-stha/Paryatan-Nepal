package com.example.nepaltourism;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.nepaltourism.*;

import java.util.List;
import java.util.Map;

public class GuideDashboardController {
    // Header
    @FXML private Label welcomeLabel;
    @FXML private CheckBox availabilityCheckBox;
    @FXML private Button logoutButton;

    // Status Alert
    @FXML private HBox statusAlert;
    @FXML private Label statusAlertLabel;

    // KPI Labels
    @FXML private Label activeBookingsLabel;
    @FXML private Label totalEarningsLabel;
    @FXML private Label averageRatingLabel;
    @FXML private Label totalToursLabel;
    @FXML private Label monthlyEarningsLabel;
    @FXML private Label completedToursLabel;
    @FXML private Label repeatCustomersLabel;
    @FXML private Label responseTimeLabel;

    // Main tabs
    @FXML private TabPane mainTabPane;

    // Bookings table
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> touristColumn;
    @FXML private TableColumn<Booking, String> attractionColumn;
    @FXML private TableColumn<Booking, String> tourDateColumn;
    @FXML private TableColumn<Booking, String> durationColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, Void> contactColumn;

    // Emergency Reports table
    @FXML private TableView<EmergencyReport> emergencyTable;
    @FXML private TableColumn<EmergencyReport, String> reportIdColumn;
    @FXML private TableColumn<EmergencyReport, String> reportTouristColumn;
    @FXML private TableColumn<EmergencyReport, String> locationColumn;
    @FXML private TableColumn<EmergencyReport, String> issueColumn;
    @FXML private TableColumn<EmergencyReport, String> priorityColumn;
    @FXML private TableColumn<EmergencyReport, String> reportStatusColumn;
    @FXML private TableColumn<EmergencyReport, Void> reportActionsColumn;

    // Profile fields
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField tourAreaField;
    @FXML private TextField experienceField;
    @FXML private TextField languagesField;
    @FXML private Button updateProfileButton;
    @FXML private Button resetProfileButton;

    // Analytics charts
    @FXML private LineChart<String, Number> earningsChart;
    @FXML private BarChart<String, Number> tourChart;
    @FXML private PieChart ratingsChart;
    @FXML private VBox insightsBox;

    private Stage primaryStage;
    private Guide currentGuide;
    private DataManager dataManager;

    public void initialize() {
        dataManager = DataManager.getInstance();
        setupEventHandlers();
        setupTableColumns();
    }

    public void setCurrentUser(User user) {
        if (user instanceof Guide) {
            this.currentGuide = (Guide) user;
            welcomeLabel.setText("Welcome, " + user.getName() + "!");
            loadGuideData();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void setupEventHandlers() {
        logoutButton.setOnAction(e -> handleLogout());
        availabilityCheckBox.setOnAction(e -> updateAvailability());
        updateProfileButton.setOnAction(e -> updateProfile());
        resetProfileButton.setOnAction(e -> resetProfile());
    }

    private void setupTableColumns() {
        // Bookings table
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        touristColumn.setCellValueFactory(cellData -> {
            String touristId = cellData.getValue().getTouristId();
            User tourist = dataManager.getUserById(touristId);
            return new javafx.beans.property.SimpleStringProperty(
                    tourist != null ? tourist.getName() : "Unknown");
        });
        attractionColumn.setCellValueFactory(cellData -> {
            String attractionId = cellData.getValue().getAttractionId();
            Attraction attraction = dataManager.getAttractionById(attractionId);
            return new javafx.beans.property.SimpleStringProperty(
                    attraction != null ? attraction.getName() : "Unknown");
        });
        tourDateColumn.setCellValueFactory(new PropertyValueFactory<>("tourDate"));
        durationColumn.setCellValueFactory(cellData -> {
            String attractionId = cellData.getValue().getAttractionId();
            Attraction attraction = dataManager.getAttractionById(attractionId);
            return new javafx.beans.property.SimpleStringProperty(
                    attraction != null ? attraction.getDurationText() : "Unknown");
        });
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusText"));

        // Contact column with button
        contactColumn.setCellFactory(col -> {
            TableCell<Booking, Void> cell = new TableCell<Booking, Void>() {
                private final Button contactBtn = new Button("Contact");

                {
                    contactBtn.getStyleClass().add("secondary-button");
                    contactBtn.setOnAction(e -> contactTourist(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(contactBtn);
                    }
                }
            };
            return cell;
        });

        // Emergency Reports table
        reportIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        reportTouristColumn.setCellValueFactory(cellData -> {
            String touristId = cellData.getValue().getTouristId();
            User tourist = dataManager.getUserById(touristId);
            return new javafx.beans.property.SimpleStringProperty(
                    tourist != null ? tourist.getName() : "Unknown");
        });
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        issueColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priorityText"));
        reportStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusText"));

        // Report actions column
        reportActionsColumn.setCellFactory(col -> {
            TableCell<EmergencyReport, Void> cell = new TableCell<EmergencyReport, Void>() {
                private final Button resolveBtn = new Button("Resolve");

                {
                    resolveBtn.getStyleClass().add("primary-button");
                    resolveBtn.setOnAction(e -> resolveEmergency(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        EmergencyReport report = getTableView().getItems().get(getIndex());
                        if (report.getStatus() == EmergencyReport.Status.RESOLVED ||
                                report.getStatus() == EmergencyReport.Status.CLOSED) {
                            setGraphic(null);
                        } else {
                            setGraphic(resolveBtn);
                        }
                    }
                }
            };
            return cell;
        });
    }

    private void loadGuideData() {
        if (currentGuide != null) {
            // Update availability checkbox
            availabilityCheckBox.setSelected(currentGuide.isAvailable());
            updateStatusAlert();

            // Load KPIs
            loadKPIs();

            // Load tables
            loadBookings();
            loadEmergencyReports();

            // Load profile
            loadProfile();

            // Load analytics
            loadAnalytics();
        }
    }

    private void updateStatusAlert() {
        if (currentGuide.isAvailable()) {
            statusAlertLabel.setText("You are currently available for new bookings.");
            statusAlert.getStyleClass().removeAll("alert-warning");
            statusAlert.getStyleClass().add("alert-info");
        } else {
            statusAlertLabel.setText("You are currently marked as busy. Update your availability to receive new bookings.");
            statusAlert.getStyleClass().removeAll("alert-info");
            statusAlert.getStyleClass().add("alert-warning");
        }
    }

    private void loadKPIs() {
        Map<String, Object> analytics = dataManager.getGuideAnalytics(currentGuide.getId());

        activeBookingsLabel.setText(analytics.get("activeBookings").toString());
        totalEarningsLabel.setText(String.format("$%.0f", (Double) analytics.get("totalEarnings")));
        averageRatingLabel.setText(String.format("%.1f", (Double) analytics.get("averageRating")));
        totalToursLabel.setText(analytics.get("totalTours").toString());

        // Additional KPIs
        monthlyEarningsLabel.setText(String.format("$%.0f", (Double) analytics.get("totalEarnings") / 12));
        completedToursLabel.setText(analytics.get("completedBookings").toString());
        repeatCustomersLabel.setText("12"); // Sample data
        responseTimeLabel.setText("2h"); // Sample data
    }

    private void loadBookings() {
        List<Booking> guideBookings = dataManager.getBookingsByGuide(currentGuide.getId());
        ObservableList<Booking> bookingItems = FXCollections.observableArrayList(guideBookings);
        bookingsTable.setItems(bookingItems);
    }

    private void loadEmergencyReports() {
        List<EmergencyReport> reports = dataManager.getEmergencyReportsByGuide(currentGuide.getId());
        ObservableList<EmergencyReport> reportItems = FXCollections.observableArrayList(reports);
        emergencyTable.setItems(reportItems);
    }

    private void loadProfile() {
        nameField.setText(currentGuide.getName());
        emailField.setText(currentGuide.getEmail());
        phoneField.setText(currentGuide.getPhone());
        tourAreaField.setText(currentGuide.getTourArea());
        experienceField.setText(currentGuide.getExperience());
        languagesField.setText(String.join(", ", currentGuide.getLanguages()));
    }

    private void loadAnalytics() {
        loadEarningsChart();
        loadTourChart();
        loadRatingsChart();
        loadInsights();
    }

    private void loadEarningsChart() {
        earningsChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Earnings");

        // Sample data
        series.getData().add(new XYChart.Data<>("Jan", 2100));
        series.getData().add(new XYChart.Data<>("Feb", 2800));
        series.getData().add(new XYChart.Data<>("Mar", 3200));
        series.getData().add(new XYChart.Data<>("Apr", 2900));
        series.getData().add(new XYChart.Data<>("May", 3800));
        series.getData().add(new XYChart.Data<>("Jun", 3500));

        earningsChart.getData().add(series);
    }

    private void loadTourChart() {
        tourChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tours Completed");

        // Sample data
        series.getData().add(new XYChart.Data<>("Jan", 3));
        series.getData().add(new XYChart.Data<>("Feb", 4));
        series.getData().add(new XYChart.Data<>("Mar", 5));
        series.getData().add(new XYChart.Data<>("Apr", 4));
        series.getData().add(new XYChart.Data<>("May", 6));
        series.getData().add(new XYChart.Data<>("Jun", 5));

        tourChart.getData().add(series);
    }

    private void loadRatingsChart() {
        ratingsChart.getData().clear();

        PieChart.Data excellent = new PieChart.Data("5 Stars", 65);
        PieChart.Data good = new PieChart.Data("4 Stars", 25);
        PieChart.Data average = new PieChart.Data("3 Stars", 8);
        PieChart.Data poor = new PieChart.Data("2 Stars", 2);

        ratingsChart.getData().addAll(excellent, good, average, poor);
    }

    private void loadInsights() {
        insightsBox.getChildren().clear();

        VBox insight1 = createInsightCard("📈",
                "Your earnings have increased by 15% this month compared to last month.");

        VBox insight2 = createInsightCard("⭐",
                "Your average rating is 4.8/5. Keep up the excellent service!");

        VBox insight3 = createInsightCard("🎯",
                "You have 3 upcoming tours this week. Check your schedule for details.");

        insightsBox.getChildren().addAll(insight1, insight2, insight3);
    }

    private VBox createInsightCard(String icon, String text) {
        VBox card = new VBox(5);
        card.getStyleClass().add("insight-card");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 20px;");

        Label textLabel = new Label(text);
        textLabel.setWrapText(true);
        textLabel.setStyle("-fx-font-size: 12px;");

        card.getChildren().addAll(iconLabel, textLabel);
        return card;
    }

    private void updateAvailability() {
        if (currentGuide != null) {
            currentGuide.setAvailable(availabilityCheckBox.isSelected());
            dataManager.updateUser(currentGuide);
            updateStatusAlert();
            showAlert("Success", "Availability updated successfully!");
        }
    }

    private void updateProfile() {
        if (currentGuide != null) {
            currentGuide.setName(nameField.getText());
            currentGuide.setEmail(emailField.getText());
            currentGuide.setPhone(phoneField.getText());
            currentGuide.setTourArea(tourAreaField.getText());
            currentGuide.setExperience(experienceField.getText());

            dataManager.updateUser(currentGuide);
            showAlert("Success", "Profile updated successfully!");
        }
    }

    private void resetProfile() {
        loadProfile();
    }

    private void contactTourist(Booking booking) {
        User tourist = dataManager.getUserById(booking.getTouristId());
        if (tourist != null) {
            showAlert("Contact Tourist",
                    "Tourist: " + tourist.getName() + "\n" +
                            "Email: " + tourist.getEmail() + "\n" +
                            "Phone: " + tourist.getPhone());
        }
    }

    private void resolveEmergency(EmergencyReport report) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Resolve Emergency");
        dialog.setHeaderText("Resolve emergency report: " + report.getId());
        dialog.setContentText("Resolution details:");

        dialog.showAndWait().ifPresent(resolution -> {
            report.setStatus(EmergencyReport.Status.RESOLVED);
            report.setResolution(resolution);
            loadEmergencyReports();
            showAlert("Success", "Emergency report resolved successfully!");
        });
    }

    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            LoginController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Nepal Tourism Management System");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not logout");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}