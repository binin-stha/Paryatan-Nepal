package com.example.nepaltourism;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.nepaltourism.*;

public class UserFormDialogController {
    @FXML private Label dialogTitleLabel;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> userTypeCombo;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private VBox tourAreaBox;
    @FXML private TextField tourAreaField;
    @FXML private Label errorLabel;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;

    private Stage dialogStage;
    private User editingUser;
    private String userType;
    private DataManager dataManager;
    private boolean confirmed = false;
    private boolean isEditing = false;

    public void initialize() {
        dataManager = DataManager.getInstance();
        setupEventHandlers();
        setupUserTypeCombo();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUser(User user, String userType) {
        this.editingUser = user;
        this.userType = userType;
        this.isEditing = (user != null);

        if (isEditing) {
            dialogTitleLabel.setText("Edit " + userType);
            loadUserData();
            // Disable user type change when editing
            userTypeCombo.setDisable(true);
            // Make password fields optional when editing
            passwordField.setPromptText("Leave blank to keep current password");
            confirmPasswordField.setPromptText("Leave blank to keep current password");
        } else {
            dialogTitleLabel.setText("Add " + userType);
            userTypeCombo.setValue(userType);
        }

        updateTourAreaVisibility();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private void setupEventHandlers() {
        cancelButton.setOnAction(e -> handleCancel());
        saveButton.setOnAction(e -> handleSave());

        userTypeCombo.setOnAction(e -> updateTourAreaVisibility());

        // Clear error when user starts typing
        nameField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
    }

    private void setupUserTypeCombo() {
        userTypeCombo.getItems().addAll("Tourist", "Guide", "Admin");
    }

    private void updateTourAreaVisibility() {
        String selectedType = userTypeCombo.getValue();
        boolean isGuide = "Guide".equals(selectedType);
        tourAreaBox.setVisible(isGuide);
        tourAreaBox.setManaged(isGuide);
    }

    private void loadUserData() {
        if (editingUser != null) {
            nameField.setText(editingUser.getName());
            emailField.setText(editingUser.getEmail());
            phoneField.setText(editingUser.getPhone());
            userTypeCombo.setValue(editingUser.getUserType());

            if (editingUser instanceof Guide) {
                Guide guide = (Guide) editingUser;
                tourAreaField.setText(guide.getTourArea());
            }
        }
    }

    private void clearError() {
        errorLabel.setText("");
    }

    @FXML
    private void handleCancel() {
        confirmed = false;
        dialogStage.close();
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            saveUser();
            confirmed = true;
            dialogStage.close();
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errors.append("Name is required.\n");
        }

        if (emailField.getText().trim().isEmpty()) {
            errors.append("Email is required.\n");
        } else if (!isValidEmail(emailField.getText().trim())) {
            errors.append("Please enter a valid email address.\n");
        }

        if (phoneField.getText().trim().isEmpty()) {
            errors.append("Phone is required.\n");
        }

        if (userTypeCombo.getValue() == null) {
            errors.append("User type is required.\n");
        }

        // Password validation (only required for new users)
        if (!isEditing) {
            if (passwordField.getText().isEmpty()) {
                errors.append("Password is required.\n");
            } else if (passwordField.getText().length() < 6) {
                errors.append("Password must be at least 6 characters long.\n");
            }

            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                errors.append("Passwords do not match.\n");
            }
        } else {
            // For editing, only validate if password is provided
            if (!passwordField.getText().isEmpty()) {
                if (passwordField.getText().length() < 6) {
                    errors.append("Password must be at least 6 characters long.\n");
                }

                if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                    errors.append("Passwords do not match.\n");
                }
            }
        }

        // Guide-specific validation
        if ("Guide".equals(userTypeCombo.getValue()) && tourAreaField.getText().trim().isEmpty()) {
            errors.append("Tour area is required for guides.\n");
        }

        // Check for duplicate email (except when editing the same user)
        Tourist existingTourist = dataManager.getAllTourists().stream()
                .filter(u -> u.getEmail().equals(emailField.getText().trim()))
                .findFirst()
                .orElse(null);

        Guide existingGuide = null;
        if (existingTourist == null) {
            existingGuide = dataManager.getAllGuides().stream()
                    .filter(u -> u.getEmail().equals(emailField.getText().trim()))
                    .findFirst()
                    .orElse(null);
        }

//        if ((existingTourist != null && (editingUser == null || !existingTourist.getId().equals(editingUser.getId())))
//                || (existingGuide != null && (editingUser == null || !existingGuide.getId().equals(editingUser.getId())))) {
//            errors.append("Email address is already in use.\n");
//        }
//
//        if (!emailField.getText().trim().isEmpty()) {
//            User existingUser = dataManager.getAllTourists().stream()
//                    .filter(u -> u.getEmail().equals(emailField.getText().trim()))
//                    .findFirst()
//                    .orElse(dataManager.getAllGuides().stream().filter(u -> u.getEmail().equals(emailField.getText().trim())).findFirst().orElse(null));
//
//            if (existingUser != null && (editingUser == null || !existingUser.getId().equals(editingUser.getId()))) {
//                errors.append("Email address is already in use.\n");
//            }
//        }

        if (errors.length() > 0) {
            showError(errors.toString());
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void saveUser() {
        String selectedUserType = userTypeCombo.getValue();

        if (isEditing) {
            // Update existing user
            editingUser.setName(nameField.getText().trim());
            editingUser.setEmail(emailField.getText().trim());
            editingUser.setPhone(phoneField.getText().trim());

            // Update password if provided
            if (!passwordField.getText().isEmpty()) {
                editingUser.setPassword(passwordField.getText());
            }

            // Update guide-specific fields
            if (editingUser instanceof Guide) {
                Guide guide = (Guide) editingUser;
                guide.setTourArea(tourAreaField.getText().trim());
            }

            dataManager.updateUser(editingUser);

        } else {
            // Create new user
            String userId = dataManager.generateNextId(getIdPrefix(selectedUserType));
            User newUser = null;

            switch (selectedUserType) {
                case "Tourist":
                    newUser = new Tourist(userId, nameField.getText().trim(),
                            emailField.getText().trim(), passwordField.getText(),
                            phoneField.getText().trim());
                    break;

                case "Guide":
                    newUser = new Guide(userId, nameField.getText().trim(),
                            emailField.getText().trim(), passwordField.getText(),
                            phoneField.getText().trim(), tourAreaField.getText().trim());
                    break;

                case "Admin":
                    newUser = new Admin(userId, nameField.getText().trim(),
                            emailField.getText().trim(), passwordField.getText(),
                            phoneField.getText().trim());
                    break;
            }

            if (newUser != null) {
                dataManager.addUser(newUser);
            }
        }
    }

    private String getIdPrefix(String userType) {
        switch (userType) {
            case "Tourist": return "T";
            case "Guide": return "G";
            case "Admin": return "A";
            default: return "U";
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #ef4444;");
    }
}