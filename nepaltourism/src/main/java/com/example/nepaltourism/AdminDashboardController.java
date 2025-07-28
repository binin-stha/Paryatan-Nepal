package com.example.nepaltourism;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.nepaltourism.*;

import java.util.Map;
import java.util.Optional;

public class AdminDashboardController {
    // Header
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;

    // KPI Labels
    @FXML private Label totalTouristsLabel;
    @FXML private Label activeGuidesLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label avgBookingValueLabel;
    @FXML private Label conversionRateLabel;
    @FXML private Label pendingBookingsLabel;
    @FXML private Label customerSatisfactionLabel;

    // Main tabs
    @FXML private TabPane mainTabPane;

    // Tourists Management
    @FXML private Button addTouristButton;
    @FXML private TableView<Tourist> touristsTable;
    @FXML private TableColumn<Tourist, String> touristIdColumn;
    @FXML private TableColumn<Tourist, String> touristNameColumn;
    @FXML private TableColumn<Tourist, String> touristEmailColumn;
    @FXML private TableColumn<Tourist, String> touristPhoneColumn;
    @FXML private TableColumn<Tourist, Integer> touristTripsColumn;
    @FXML private TableColumn<Tourist, Double> touristSpentColumn;
    @FXML private TableColumn<Tourist, String> touristStatusColumn;
    @FXML private TableColumn<Tourist, Void> touristActionsColumn;

    // Guides Management
    @FXML private Button addGuideButton;
    @FXML private TableView<Guide> guidesTable;
    @FXML private TableColumn<Guide, String> guideIdColumn;
    @FXML private TableColumn<Guide, String> guideNameColumn;
    @FXML private TableColumn<Guide, String> guideEmailColumn;
    @FXML private TableColumn<Guide, String> guideTourAreaColumn;
    @FXML private TableColumn<Guide, String> guideExperienceColumn;
    @FXML private TableColumn<Guide, Double> guideRatingColumn;
    @FXML private TableColumn<Guide, String> guideAvailabilityColumn;
    @FXML private TableColumn<Guide, Void> guideActionsColumn;

    // Attractions Management
    @FXML private Button addAttractionButton;
    @FXML private TableView<Attraction> attractionsTable;
    @FXML private TableColumn<Attraction, String> attractionIdColumn;
    @FXML private TableColumn<Attraction, String> attractionNameColumn;
    @FXML private TableColumn<Attraction, String> attractionRegionColumn;
    @FXML private TableColumn<Attraction, String> attractionCategoryColumn;
    @FXML private TableColumn<Attraction, String> attractionDifficultyColumn;
    @FXML private TableColumn<Attraction, Integer> attractionDurationColumn;
    @FXML private TableColumn<Attraction, Double> attractionPriceColumn;
    @FXML private TableColumn<Attraction, Integer> attractionVisitorsColumn;
    @FXML private TableColumn<Attraction, Void> attractionActionsColumn;

    // Bookings Management
    @FXML private ComboBox<String> bookingStatusFilter;
    @FXML private Button exportBookingsButton;
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> bookingTouristColumn;
    @FXML private TableColumn<Booking, String> bookingGuideColumn;
    @FXML private TableColumn<Booking, String> bookingAttractionColumn;
    @FXML private TableColumn<Booking, String> bookingDateColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;
    @FXML private TableColumn<Booking, Double> bookingPriceColumn;
    @FXML private TableColumn<Booking, Void> bookingActionsColumn;

    // Analytics Charts
    @FXML private LineChart<String, Number> revenueChart;
    @FXML private BarChart<String, Number> attractionsChart;
    @FXML private PieChart regionalChart;

    private Stage primaryStage;
    private User currentUser;
    private DataManager dataManager;

    public void initialize() {
        dataManager = DataManager.getInstance();
        setupEventHandlers();
        setupTableColumns();
        setupFilters();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getName() + "!");
        loadDashboardData();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void setupEventHandlers() {
        logoutButton.setOnAction(e -> handleLogout());
        addTouristButton.setOnAction(e -> showAddTouristDialog());
        addGuideButton.setOnAction(e -> showAddGuideDialog());
        addAttractionButton.setOnAction(e -> showAddAttractionDialog());
        exportBookingsButton.setOnAction(e -> exportBookings());
        bookingStatusFilter.setOnAction(e -> filterBookings());
    }

    private void setupTableColumns() {
        // Tourist table columns
        touristIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        touristNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        touristEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        touristPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        touristTripsColumn.setCellValueFactory(new PropertyValueFactory<>("totalTrips"));
        touristSpentColumn.setCellValueFactory(new PropertyValueFactory<>("totalSpent"));
        touristStatusColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().isActive() ? "Active" : "Inactive"));

        // Add action buttons for tourists
        touristActionsColumn.setCellFactory(col -> {
            TableCell<Tourist, Void> cell = new TableCell<Tourist, Void>() {
                private final Button editBtn = new Button("Edit");
                private final Button deleteBtn = new Button("Delete");

                {
                    editBtn.getStyleClass().add("secondary-button");
                    deleteBtn.getStyleClass().add("error-button");
                    editBtn.setOnAction(e -> editTourist(getTableView().getItems().get(getIndex())));
                    deleteBtn.setOnAction(e -> deleteTourist(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5);
                        buttons.getChildren().addAll(editBtn, deleteBtn);
                        setGraphic(buttons);
                    }
                }
            };
            return cell;
        });

        // Guide table columns
        guideIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        guideNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        guideEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        guideTourAreaColumn.setCellValueFactory(new PropertyValueFactory<>("tourArea"));
        guideExperienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
        guideRatingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        guideAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));

        // Add action buttons for guides
        guideActionsColumn.setCellFactory(col -> {
            TableCell<Guide, Void> cell = new TableCell<Guide, Void>() {
                private final Button editBtn = new Button("Edit");
                private final Button deleteBtn = new Button("Delete");

                {
                    editBtn.getStyleClass().add("secondary-button");
                    deleteBtn.getStyleClass().add("error-button");
                    editBtn.setOnAction(e -> editGuide(getTableView().getItems().get(getIndex())));
                    deleteBtn.setOnAction(e -> deleteGuide(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5);
                        buttons.getChildren().addAll(editBtn, deleteBtn);
                        setGraphic(buttons);
                    }
                }
            };
            return cell;
        });

        // Attraction table columns
        attractionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        attractionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        attractionRegionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        attractionCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        attractionDifficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        attractionDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        attractionPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        attractionVisitorsColumn.setCellValueFactory(new PropertyValueFactory<>("totalVisitors"));

        // Add action buttons for attractions
        attractionActionsColumn.setCellFactory(col -> {
            TableCell<Attraction, Void> cell = new TableCell<Attraction, Void>() {
                private final Button editBtn = new Button("Edit");
                private final Button deleteBtn = new Button("Delete");

                {
                    editBtn.getStyleClass().add("secondary-button");
                    deleteBtn.getStyleClass().add("error-button");
                    editBtn.setOnAction(e -> editAttraction(getTableView().getItems().get(getIndex())));
                    deleteBtn.setOnAction(e -> deleteAttraction(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5);
                        buttons.getChildren().addAll(editBtn, deleteBtn);
                        setGraphic(buttons);
                    }
                }
            };
            return cell;
        });

        // Booking table columns
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookingTouristColumn.setCellValueFactory(cellData -> {
            String touristId = cellData.getValue().getTouristId();
            User tourist = dataManager.getUserById(touristId);
            return new javafx.beans.property.SimpleStringProperty(
                    tourist != null ? tourist.getName() : "Unknown");
        });
        bookingGuideColumn.setCellValueFactory(cellData -> {
            String guideId = cellData.getValue().getGuideId();
            User guide = dataManager.getUserById(guideId);
            return new javafx.beans.property.SimpleStringProperty(
                    guide != null ? guide.getName() : "Unknown");
        });
        bookingAttractionColumn.setCellValueFactory(cellData -> {
            String attractionId = cellData.getValue().getAttractionId();
            Attraction attraction = dataManager.getAttractionById(attractionId);
            return new javafx.beans.property.SimpleStringProperty(
                    attraction != null ? attraction.getName() : "Unknown");
        });
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("tourDate"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusText"));
        bookingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));

        // Add action buttons for bookings
        bookingActionsColumn.setCellFactory(col -> {
            TableCell<Booking, Void> cell = new TableCell<Booking, Void>() {
                private final Button updateBtn = new Button("Update");
                private final Button cancelBtn = new Button("Cancel");

                {
                    updateBtn.getStyleClass().add("secondary-button");
                    cancelBtn.getStyleClass().add("error-button");
                    updateBtn.setOnAction(e -> updateBookingStatus(getTableView().getItems().get(getIndex())));
                    cancelBtn.setOnAction(e -> cancelBooking(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5);
                        buttons.getChildren().addAll(updateBtn, cancelBtn);
                        setGraphic(buttons);
                    }
                }
            };
            return cell;
        });
    }

    private void setupFilters() {
        bookingStatusFilter.getItems().addAll("All Status", "PENDING", "CONFIRMED", "IN_PROGRESS", "COMPLETED", "CANCELLED");
        bookingStatusFilter.setValue("All Status");
    }

    private void loadDashboardData() {
        loadKPIs();
        loadTables();
        loadCharts();
    }

    private void loadKPIs() {
        Map<String, Object> analytics = dataManager.getAdminAnalytics();

        totalTouristsLabel.setText(analytics.get("totalTourists").toString());
        activeGuidesLabel.setText(analytics.get("activeGuides").toString());
        totalBookingsLabel.setText(analytics.get("totalBookings").toString());
        totalRevenueLabel.setText(String.format("$%.0f", (Double) analytics.get("totalRevenue")));
        avgBookingValueLabel.setText(String.format("$%.0f", (Double) analytics.get("averageBookingValue")));
        conversionRateLabel.setText(String.format("%.1f%%", (Double) analytics.get("conversionRate")));
        pendingBookingsLabel.setText(analytics.get("pendingBookings").toString());
        customerSatisfactionLabel.setText("4.7"); // Sample data
    }

    private void loadTables() {
        // Load tourists
        touristsTable.setItems(dataManager.getTouristsObservableList());

        // Load guides
        guidesTable.setItems(dataManager.getGuidesObservableList());

        // Load attractions
        attractionsTable.setItems(dataManager.getAttractionsObservableList());

        // Load bookings
        bookingsTable.setItems(dataManager.getBookingsObservableList());
    }

    private void loadCharts() {
        loadRevenueChart();
        loadAttractionsChart();
        loadRegionalChart();
    }

    private void loadRevenueChart() {
        revenueChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Revenue");

        // Sample data
        series.getData().add(new XYChart.Data<>("Jan", 12500));
        series.getData().add(new XYChart.Data<>("Feb", 18900));
        series.getData().add(new XYChart.Data<>("Mar", 25600));
        series.getData().add(new XYChart.Data<>("Apr", 22100));
        series.getData().add(new XYChart.Data<>("May", 31200));
        series.getData().add(new XYChart.Data<>("Jun", 28700));

        revenueChart.getData().add(series);
    }

    private void loadAttractionsChart() {
        attractionsChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bookings by Attraction");

        // Sample data
        series.getData().add(new XYChart.Data<>("Everest Base Camp", 45));
        series.getData().add(new XYChart.Data<>("Annapurna Circuit", 38));
        series.getData().add(new XYChart.Data<>("Chitwan Safari", 62));
        series.getData().add(new XYChart.Data<>("Langtang Trek", 28));

        attractionsChart.getData().add(series);
    }

    private void loadRegionalChart() {
        regionalChart.getData().clear();

        PieChart.Data khumbu = new PieChart.Data("Khumbu", 35);
        PieChart.Data annapurna = new PieChart.Data("Annapurna", 30);
        PieChart.Data chitwan = new PieChart.Data("Chitwan", 25);
        PieChart.Data langtang = new PieChart.Data("Langtang", 10);

        regionalChart.getData().addAll(khumbu, annapurna, chitwan, langtang);
    }

    // CRUD Operations
    private void showAddTouristDialog() {
        showUserFormDialog("Add Tourist", null, "Tourist");
    }

    private void showAddGuideDialog() {
        showUserFormDialog("Add Guide", null, "Guide");
    }

    private void showAddAttractionDialog() {
        showAttractionFormDialog("Add Attraction", null);
    }

    private void editTourist(Tourist tourist) {
        showUserFormDialog("Edit Tourist", tourist, "Tourist");
    }

    private void editGuide(Guide guide) {
        showUserFormDialog("Edit Guide", guide, "Guide");
    }

    private void editAttraction(Attraction attraction) {
        showAttractionFormDialog("Edit Attraction", attraction);
    }

    private void deleteTourist(Tourist tourist) {
        if (showConfirmationDialog("Delete Tourist", "Are you sure you want to delete " + tourist.getName() + "?")) {
            dataManager.deleteUser(tourist.getId());
            loadTables();
            loadKPIs();
        }
    }

    private void deleteGuide(Guide guide) {
        if (showConfirmationDialog("Delete Guide", "Are you sure you want to delete " + guide.getName() + "?")) {
            dataManager.deleteUser(guide.getId());
            loadTables();
            loadKPIs();
        }
    }

    private void deleteAttraction(Attraction attraction) {
        if (showConfirmationDialog("Delete Attraction", "Are you sure you want to delete " + attraction.getName() + "?")) {
            dataManager.deleteAttraction(attraction.getId());
            loadTables();
            loadKPIs();
        }
    }

    private void updateBookingStatus(Booking booking) {
        ChoiceDialog<Booking.BookingStatus> dialog = new ChoiceDialog<>(booking.getStatus(), Booking.BookingStatus.values());
        dialog.setTitle("Update Booking Status");
        dialog.setHeaderText("Update status for booking " + booking.getId());
        dialog.setContentText("Choose new status:");

        Optional<Booking.BookingStatus> result = dialog.showAndWait();
        result.ifPresent(status -> {
            booking.setStatus(status);
            dataManager.updateBooking(booking);
            loadTables();
            loadKPIs();
        });
    }

    private void cancelBooking(Booking booking) {
        if (showConfirmationDialog("Cancel Booking", "Are you sure you want to cancel booking " + booking.getId() + "?")) {
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            dataManager.updateBooking(booking);
            loadTables();
            loadKPIs();
        }
    }

    private void showUserFormDialog(String title, User user, String userType) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user-form-dialog.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            UserFormDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUser(user, userType);

            dialogStage.showAndWait();

            if (controller.isConfirmed()) {
                loadTables();
                loadKPIs();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open user form dialog");
        }
    }

    private void showAttractionFormDialog(String title, Attraction attraction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/attraction-form-dialog.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            AttractionFormDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAttraction(attraction);

            dialogStage.showAndWait();

            if (controller.isConfirmed()) {
                loadTables();
                loadKPIs();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open attraction form dialog");
        }
    }

    private void filterBookings() {
        String selectedStatus = bookingStatusFilter.getValue();
        if (selectedStatus.equals("All Status")) {
            bookingsTable.setItems(dataManager.getBookingsObservableList());
        } else {
            Booking.BookingStatus status = Booking.BookingStatus.valueOf(selectedStatus);
            ObservableList<Booking> filteredBookings = FXCollections.observableArrayList(
                    dataManager.getBookingsByStatus(status));
            bookingsTable.setItems(filteredBookings);
        }
    }

    private void exportBookings() {
        showAlert("Export", "Booking export functionality would be implemented here.");
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

    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}