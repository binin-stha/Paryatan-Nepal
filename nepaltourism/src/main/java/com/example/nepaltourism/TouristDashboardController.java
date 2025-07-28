package com.example.nepaltourism;

//package com.nepaltourism.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.nepaltourism.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TouristDashboardController {
    // Main navigation
    @FXML private TabPane mainTabPane;
    @FXML private Tab attractionsTab;
    @FXML private Tab bookingsTab;
    @FXML private Tab guidesTab;
    @FXML private Tab analyticsTab;

    // Header controls
    @FXML private Label welcomeLabel;
    @FXML private Button languageButton;
    @FXML private Button emergencyButton;
    @FXML private Button logoutButton;

    // Attractions tab
    @FXML private TextField searchField;
    @FXML private ComboBox<String> regionFilter;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private ListView<Attraction> attractionsList;

    // Bookings tab
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> attractionColumn;
    @FXML private TableColumn<Booking, String> guideColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, String> priceColumn;

    // Guides tab
    @FXML private ListView<Guide> guidesList;

    // Analytics tab
    @FXML private Label totalSpentLabel;
    @FXML private Label totalTripsLabel;
    @FXML private Label avgTripCostLabel;
    @FXML private Label completedTripsLabel;
    @FXML private LineChart<String, Number> spendingChart;
    @FXML private BarChart<String, Number> categoryChart;
    @FXML private VBox insightsBox;

    private Stage primaryStage;
    private User currentUser;
    private DataManager dataManager;
    private boolean isNepali = false;

    public void initialize() {
        dataManager = DataManager.getInstance();
        setupEventHandlers();
        setupTableColumns();
        setupFilters();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateWelcomeMessage();
        loadUserData();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void setupEventHandlers() {
        languageButton.setOnAction(e -> toggleLanguage());
        emergencyButton.setOnAction(e -> showEmergencyDialog());
        logoutButton.setOnAction(e -> handleLogout());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterAttractions());
        regionFilter.setOnAction(e -> filterAttractions());
        categoryFilter.setOnAction(e -> filterAttractions());

        // Double-click to book attraction
        attractionsList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Attraction selected = attractionsList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showBookingDialog(selected);
                }
            }
        });
    }

    private void setupTableColumns() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        attractionColumn.setCellValueFactory(cellData -> {
            String attractionId = cellData.getValue().getAttractionId();
            Attraction attraction = dataManager.getAttractionById(attractionId);
            return new javafx.beans.property.SimpleStringProperty(
                    attraction != null ? attraction.getName() : "Unknown");
        });
        guideColumn.setCellValueFactory(cellData -> {
            String guideId = cellData.getValue().getGuideId();
            User guide = dataManager.getUserById(guideId);
            return new javafx.beans.property.SimpleStringProperty(
                    guide != null ? guide.getName() : "Unknown");
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("tourDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusText"));
        priceColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        String.format("$%.0f", cellData.getValue().getFinalPrice())));
    }

    private void setupFilters() {
        // Setup region filter
        regionFilter.getItems().addAll("All Regions", "Khumbu", "Annapurna", "Chitwan", "Langtang");
        regionFilter.setValue("All Regions");

        // Setup category filter
        categoryFilter.getItems().addAll("All Categories", "Trekking", "Wildlife", "Cultural", "Adventure");
        categoryFilter.setValue("All Categories");
    }

    private void updateWelcomeMessage() {
        if (currentUser != null) {
            String message = isNepali ?
                    "स्वागतम्, " + currentUser.getName() + "!" :
                    "Welcome, " + currentUser.getName() + "!";
            welcomeLabel.setText(message);
        }
    }

    private void loadUserData() {
        loadAttractions();
        loadBookings();
        loadGuides();
        loadAnalytics();
    }

    private void loadAttractions() {
        ObservableList<Attraction> attractions = dataManager.getAttractionsObservableList();
        attractionsList.setItems(attractions);

        // Custom cell factory for attractions
        attractionsList.setCellFactory(listView -> new ListCell<Attraction>() {
            @Override
            protected void updateItem(Attraction attraction, boolean empty) {
                super.updateItem(attraction, empty);
                if (empty || attraction == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox content = new VBox(5);
                    content.getStyleClass().add("attraction-cell");

                    Label nameLabel = new Label(attraction.getName());
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

                    Label detailsLabel = new Label(String.format("%s • %s • %s • %s",
                            attraction.getRegion(), attraction.getCategory(),
                            attraction.getDurationText(), attraction.getPriceText()));
                    detailsLabel.setStyle("-fx-text-fill: #64748b;");

                    Label descLabel = new Label(attraction.getDescription());
                    descLabel.setWrapText(true);
                    descLabel.setStyle("-fx-font-size: 12px;");

                    Label ratingLabel = new Label(String.format("★ %.1f (%d visitors)",
                            attraction.getRating(), attraction.getTotalVisitors()));
                    ratingLabel.setStyle("-fx-text-fill: #f59e0b; -fx-font-weight: bold;");

                    content.getChildren().addAll(nameLabel, detailsLabel, descLabel, ratingLabel);
                    setGraphic(content);
                }
            }
        });
    }

    private void loadBookings() {
        if (currentUser != null) {
            List<Booking> userBookings = dataManager.getBookingsByTourist(currentUser.getId());
            ObservableList<Booking> bookingItems = FXCollections.observableArrayList(userBookings);
            bookingsTable.setItems(bookingItems);
        }
    }

    private void loadGuides() {
        ObservableList<Guide> guides = dataManager.getGuidesObservableList();
        guidesList.setItems(guides);

        // Custom cell factory for guides
        guidesList.setCellFactory(listView -> new ListCell<Guide>() {
            @Override
            protected void updateItem(Guide guide, boolean empty) {
                super.updateItem(guide, empty);
                if (empty || guide == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox content = new VBox(5);
                    content.getStyleClass().add("guide-cell");

                    Label nameLabel = new Label(guide.getName());
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

                    Label areaLabel = new Label("Tour Area: " + guide.getTourArea());
                    Label experienceLabel = new Label("Experience: " + guide.getExperience());
                    Label ratingLabel = new Label(String.format("Rating: ★ %.1f", guide.getRating()));

                    Label statusLabel = new Label(guide.getAvailabilityStatus());
                    statusLabel.setStyle(guide.isAvailable() ?
                            "-fx-text-fill: #10b981; -fx-font-weight: bold;" :
                            "-fx-text-fill: #ef4444; -fx-font-weight: bold;");

                    content.getChildren().addAll(nameLabel, areaLabel, experienceLabel, ratingLabel, statusLabel);
                    setGraphic(content);
                }
            }
        });
    }

    private void loadAnalytics() {
        if (currentUser != null) {
            Map<String, Object> analytics = dataManager.getTouristAnalytics(currentUser.getId());

            // Update KPI labels
            totalSpentLabel.setText(String.format("$%.0f", (Double) analytics.get("totalSpent")));
            totalTripsLabel.setText(analytics.get("totalTrips").toString());
            avgTripCostLabel.setText(String.format("$%.0f", (Double) analytics.get("averageTripCost")));
            completedTripsLabel.setText(analytics.get("completedTrips").toString());

            // Load charts
            loadSpendingChart();
            loadCategoryChart();
            loadInsights();
        }
    }

    private void loadSpendingChart() {
        spendingChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Spending");

        // Sample data - in real app, this would come from database
        series.getData().add(new XYChart.Data<>("Jan", 0));
        series.getData().add(new XYChart.Data<>("Feb", 450));
        series.getData().add(new XYChart.Data<>("Mar", 2500));
        series.getData().add(new XYChart.Data<>("Apr", 0));
        series.getData().add(new XYChart.Data<>("May", 1800));
        series.getData().add(new XYChart.Data<>("Jun", 0));

        spendingChart.getData().add(series);
    }

    private void loadCategoryChart() {
        categoryChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Spending by Category");

        // Sample data
        series.getData().add(new XYChart.Data<>("Trekking", 4300));
        series.getData().add(new XYChart.Data<>("Wildlife", 450));
        series.getData().add(new XYChart.Data<>("Cultural", 0));
        series.getData().add(new XYChart.Data<>("Adventure", 0));

        categoryChart.getData().add(series);
    }

    private void loadInsights() {
        insightsBox.getChildren().clear();

        // Create insight cards
        VBox insight1 = createInsightCard("🏔️",
                isNepali ? "तपाईंलाई ट्रेकिङ मन पर्छ! अर्को पटक मनास्लु सर्किट अन्वेषण गर्ने विचार गर्नुहोस्।" :
                        "You love trekking! Consider exploring the Manaslu Circuit next.");

        VBox insight2 = createInsightCard("💰",
                isNepali ? "तपाईंको औसत यात्रा लागत औसतभन्दा माथि छ। तपाईंले प्रिमियम अनुभवहरूको आनन्द लिन सक्नुहुन्छ।" :
                        "Your average trip cost is above average. You might enjoy premium experiences.");

        VBox insight3 = createInsightCard("📅",
                isNepali ? "तपाईंको अर्को यात्राको लागि उत्तम समय: स्पष्ट पहाडी दृश्यहरूको लागि मार्च-मे।" :
                        "Best time for your next trip: March-May for clear mountain views.");

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

    private void filterAttractions() {
        String searchText = searchField.getText().toLowerCase();
        String selectedRegion = regionFilter.getValue();
        String selectedCategory = categoryFilter.getValue();

        List<Attraction> allAttractions = dataManager.getAllAttractions();
        List<Attraction> filteredAttractions = allAttractions.stream()
                .filter(attr -> {
                    boolean matchesSearch = searchText.isEmpty() ||
                            attr.getName().toLowerCase().contains(searchText) ||
                            attr.getDescription().toLowerCase().contains(searchText);

                    boolean matchesRegion = selectedRegion.equals("All Regions") ||
                            attr.getRegion().equals(selectedRegion);

                    boolean matchesCategory = selectedCategory.equals("All Categories") ||
                            attr.getCategory().equals(selectedCategory);

                    return matchesSearch && matchesRegion && matchesCategory;
                })
                .collect(Collectors.toList());

        ObservableList<Attraction> filteredItems = FXCollections.observableArrayList(filteredAttractions);
        attractionsList.setItems(filteredItems);
    }

    private void showBookingDialog(Attraction attraction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/booking-dialog.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Book Tour - " + attraction.getName());
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            BookingDialogController controller = loader.getController();
            controller.setAttraction(attraction);
            controller.setCurrentUser(currentUser);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            // Refresh bookings after dialog closes
            loadBookings();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open booking dialog");
        }
    }

    private void showEmergencyDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/emergency-dialog.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Report Emergency");
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            EmergencyDialogController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open emergency dialog");
        }
    }

    private void toggleLanguage() {
        isNepali = !isNepali;
        languageButton.setText(isNepali ? "English" : "नेपाली");
        updateWelcomeMessage();

        // Update tab titles
        attractionsTab.setText(isNepali ? "आकर्षणहरू खोज्नुहोस्" : "Explore Attractions");
        bookingsTab.setText(isNepali ? "मेरो बुकिङ" : "My Bookings");
        guidesTab.setText(isNepali ? "उपलब्ध गाइडहरू" : "Available Guides");
        analyticsTab.setText(isNepali ? "मेरो विश्लेषण" : "My Analytics");

        // Update button texts
        emergencyButton.setText(isNepali ? "आपातकाल" : "Emergency");
        logoutButton.setText(isNepali ? "लगआउट" : "Logout");

        // Reload insights with new language
        loadInsights();
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
