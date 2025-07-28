package com.example.nepaltourism;

//package com.nepaltourism.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.nepaltourism.DataManager;
import com.example.nepaltourism.User;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> userTypeCombo;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    @FXML private Hyperlink signUpLink;

    private Stage primaryStage;
    private DataManager dataManager;

    public void initialize() {
        dataManager = DataManager.getInstance();

        // Initialize user type combo box
        userTypeCombo.getItems().addAll("Tourist", "Guide", "Admin");
        userTypeCombo.setValue("Tourist");

        // Set up event handlers
        loginButton.setOnAction(e -> handleLogin());
        signUpLink.setOnAction(e -> handleSignUp());

        // Clear error label initially
        errorLabel.setText("");

        // Add enter key support
        passwordField.setOnAction(e -> handleLogin());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String userType = userTypeCombo.getValue();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        // Authenticate user
        User user = dataManager.authenticateUser(email, password, userType);

        if (user != null) {
            // Login successful - navigate to appropriate dashboard
            navigateToDashboard(user);
        } else {
            showError("Invalid credentials. Please try again.");
        }
    }

    @FXML
    private void handleSignUp() {
        showInfo("Sign up functionality would be implemented here.");
    }

    private void navigateToDashboard(User user) {
        try {
            String fxmlFile = "";
            String title = "";

            switch (user.getUserType()) {
                case "Tourist":
                    fxmlFile = "tourist-dashboard.fxml";
                    title = "Tourist Dashboard - Nepal Tourism";
                    break;
                case "Guide":
                    fxmlFile = "guide-dashboard.fxml";
                    title = "Guide Dashboard - Nepal Tourism";
                    break;
                case "Admin":
                    fxmlFile = "admin-dashboard.fxml";
                    title = "Admin Dashboard - Nepal Tourism";
                    break;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            // Set user in the dashboard controller
            if (user.getUserType().equals("Tourist")) {
                TouristDashboardController controller = loader.getController();
                controller.setCurrentUser(user);
                controller.setPrimaryStage(primaryStage);
            } else if (user.getUserType().equals("Guide")) {
                GuideDashboardController controller = loader.getController();
                controller.setCurrentUser(user);
                controller.setPrimaryStage(primaryStage);
            } else if (user.getUserType().equals("Admin")) {
                AdminDashboardController controller = loader.getController();
                controller.setCurrentUser(user);
                controller.setPrimaryStage(primaryStage);
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading dashboard");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #ef4444;");
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}