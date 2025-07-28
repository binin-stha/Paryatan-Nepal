package com.example.nepaltourism;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.nepaltourism.*;

public class AttractionFormDialogController {
    @FXML private Label dialogTitleLabel;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> regionCombo;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ComboBox<String> difficultyCombo;
    @FXML private Spinner<Integer> durationSpinner;
    @FXML private TextField priceField;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox activeCheckBox;
    @FXML private Label errorLabel;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;

    private Stage dialogStage;
    private Attraction editingAttraction;
    private DataManager dataManager;
    private boolean confirmed = false;
    private boolean isEditing = false;

    public void initialize() {
        dataManager = DataManager.getInstance();
        setupEventHandlers();
        setupComboBoxes();
        setupSpinner();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAttraction(Attraction attraction) {
        this.editingAttraction = attraction;
        this.isEditing = (attraction != null);

        if (isEditing) {
            dialogTitleLabel.setText("Edit Attraction");
            loadAttractionData();
        } else {
            dialogTitleLabel.setText("Add Attraction");
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private void setupEventHandlers() {
        cancelButton.setOnAction(e -> handleCancel());
        saveButton.setOnAction(e -> handleSave());

        // Clear error when user starts typing
        nameField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        priceField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        descriptionArea.textProperty().addListener((obs, oldVal, newVal) -> clearError());
    }

    private void setupComboBoxes() {
        // Region combo
        regionCombo.getItems().addAll("Khumbu", "Annapurna", "Chitwan", "Langtang", "Mustang", "Manaslu");

        // Category combo
        categoryCombo.getItems().addAll("Trekking", "Wildlife", "Cultural", "Adventure", "Spiritual", "Mountaineering");

        // Difficulty combo
        difficultyCombo.getItems().addAll("Easy", "Medium", "Hard", "Expert");
    }

    private void setupSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 1);
        durationSpinner.setValueFactory(valueFactory);
    }

    private void loadAttractionData() {
        if (editingAttraction != null) {
            nameField.setText(editingAttraction.getName());
            regionCombo.setValue(editingAttraction.getRegion());
            categoryCombo.setValue(editingAttraction.getCategory());
            difficultyCombo.setValue(editingAttraction.getDifficulty());
            durationSpinner.getValueFactory().setValue(editingAttraction.getDuration());
            priceField.setText(String.valueOf(editingAttraction.getPrice()));
            descriptionArea.setText(editingAttraction.getDescription());
            activeCheckBox.setSelected(editingAttraction.isActive());
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
            saveAttraction();
            confirmed = true;
            dialogStage.close();
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errors.append("Attraction name is required.\n");
        }

        if (regionCombo.getValue() == null) {
            errors.append("Region is required.\n");
        }

        if (categoryCombo.getValue() == null) {
            errors.append("Category is required.\n");
        }

        if (difficultyCombo.getValue() == null) {
            errors.append("Difficulty is required.\n");
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            errors.append("Description is required.\n");
        }

        // Validate price
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price <= 0) {
                errors.append("Price must be greater than 0.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Please enter a valid price.\n");
        }

        // Check for duplicate name (except when editing the same attraction)
        if (!nameField.getText().trim().isEmpty()) {
            Attraction existingAttraction = dataManager.getAllAttractions().stream()
                    .filter(a -> a.getName().equalsIgnoreCase(nameField.getText().trim()))
                    .findFirst()
                    .orElse(null);

            if (existingAttraction != null &&
                    (editingAttraction == null || !existingAttraction.getId().equals(editingAttraction.getId()))) {
                errors.append("An attraction with this name already exists.\n");
            }
        }

        if (errors.length() > 0) {
            showError(errors.toString());
            return false;
        }

        return true;
    }

    private void saveAttraction() {
        if (isEditing) {
            // Update existing attraction
            editingAttraction.setName(nameField.getText().trim());
            editingAttraction.setRegion(regionCombo.getValue());
            editingAttraction.setCategory(categoryCombo.getValue());
            editingAttraction.setDifficulty(difficultyCombo.getValue());
            editingAttraction.setDuration(durationSpinner.getValue());
            editingAttraction.setPrice(Double.parseDouble(priceField.getText().trim()));
            editingAttraction.setDescription(descriptionArea.getText().trim());
            editingAttraction.setActive(activeCheckBox.isSelected());

            dataManager.updateAttraction(editingAttraction);

        } else {
            // Create new attraction
            String attractionId = dataManager.generateNextId("AT");

            Attraction newAttraction = new Attraction(
                    attractionId,
                    nameField.getText().trim(),
                    regionCombo.getValue(),
                    categoryCombo.getValue(),
                    descriptionArea.getText().trim(),
                    difficultyCombo.getValue(),
                    durationSpinner.getValue(),
                    Double.parseDouble(priceField.getText().trim())
            );

            newAttraction.setActive(activeCheckBox.isSelected());
            dataManager.addAttraction(newAttraction);
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #ef4444;");
    }
}