package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tn.esprit.models.Personne;
import tn.esprit.services.ServicePersonne;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffFormController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField addressField;
    @FXML private TextField telephoneField;
    @FXML private ComboBox<String> sexComboBox;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private TextField dailyRateField;
    @FXML private TextField leaveDaysField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private Label errorLabel;

    private ServicePersonne servicePersonne;
    private StaffManagementController staffManagementController;
    private Personne currentPersonne = null;
    private boolean isEditMode = false;

    private static final String ERROR_STYLE_CLASS = "error-field";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sexComboBox.setItems(FXCollections.observableArrayList("Homme", "Femme"));
        roleComboBox.setItems(FXCollections.observableArrayList("Responsable", "Admin", "Employe"));
        departmentComboBox.setItems(FXCollections.observableArrayList("Relations Fournisseurs","Finance","Stocks","Facture","CRM"));
        addNumericListener(dailyRateField, true);
        addNumericListener(leaveDaysField, false);
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        deleteButton.setVisible(false);
        deleteButton.setManaged(false);
    }

    public void setServicePersonne(ServicePersonne service) { this.servicePersonne = service; }
    public void setStaffManagementController(StaffManagementController controller) { this.staffManagementController = controller; }

    public void loadPersonneData(Personne personne) {
        if (personne == null) return;
        this.currentPersonne = personne;
        this.isEditMode = true;
        titleLabel.setText("Modifier Personnel");
        nomField.setText(personne.getNom());
        prenomField.setText(personne.getPrenom());
        addressField.setText(personne.getAddress());
        telephoneField.setText(personne.getTel());
        sexComboBox.setValue(personne.getSex());
        roleComboBox.setValue(personne.getRole());
        departmentComboBox.setValue(personne.getDepartment());
        dailyRateField.setText(String.format("%.2f", personne.getDailyRate()).replace(',', '.'));
        leaveDaysField.setText(String.valueOf(personne.getRemainingLeaveDays()));
        deleteButton.setVisible(true);
        deleteButton.setManaged(true);
    }
    @FXML
    void handleSave(ActionEvent event) {
        if (!validateInput()) {
            showError("Veuillez corriger les erreurs indiquées.");
            return;
        }
        hideError();
        try {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String address = addressField.getText().trim();
            String telephone = telephoneField.getText().trim();
            String sex = sexComboBox.getValue();
            String role = roleComboBox.getValue();
            String department = departmentComboBox.getValue();
            double dailyRate = dailyRateField.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(dailyRateField.getText().trim().replace(',', '.'));
            int leaveDays = leaveDaysField.getText().trim().isEmpty() ? 0 : Integer.parseInt(leaveDaysField.getText().trim());

            if (isEditMode && currentPersonne != null) {
                currentPersonne.setNom(nom);
                currentPersonne.setPrenom(prenom);
                currentPersonne.setAddress(address);
                currentPersonne.setTel(telephone);
                currentPersonne.setSex(sex);
                currentPersonne.setRole(role);
                currentPersonne.setDepartment(department);
                currentPersonne.setDailyRate(dailyRate);
                currentPersonne.setRemainingLeaveDays(leaveDays);
                servicePersonne.update(currentPersonne);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Membre du personnel mis à jour avec succès.");
            } else {
                Personne newPersonne = new Personne(nom, prenom, sex, telephone, role, address, department, dailyRate, leaveDays);
                servicePersonne.add(newPersonne);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Nouveau membre du personnel ajouté avec succès.");
            }
            if (staffManagementController != null) {
                staffManagementController.showListViewContent();
                staffManagementController.refreshData();
            }
        } catch (NumberFormatException e) {
            showError("Format numérique invalide pour Taux Journalier ou Jours Congé.");
            showAlert(Alert.AlertType.ERROR, "Erreur de Saisie", "Veuillez entrer des nombres valides pour le Taux Journalier et les Jours Congé.");
        } catch (Exception e) {
            showError("Une erreur s'est produite : " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur Inattendue", "Une erreur inattendue s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML void handleCancel(ActionEvent event) {
        if (staffManagementController != null) {
            staffManagementController.showListViewContent();
        }
    }
    @FXML
    void handleFormDelete(ActionEvent event) {
        if (!isEditMode || currentPersonne == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun personnel sélectionné pour la suppression.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de Suppression");
        confirmation.setHeaderText("Supprimer : " + currentPersonne.getPrenom() + " " + currentPersonne.getNom());
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce membre du personnel ? Cette action est irréversible.");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                servicePersonne.delete(currentPersonne);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Membre du personnel supprimé avec succès.");
                if (staffManagementController != null) {
                    staffManagementController.showListViewContent();
                    staffManagementController.refreshData();
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de Suppression", "Impossible de supprimer le membre du personnel : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean validateInput() {
        boolean isValid = true;
        resetStyle(nomField);
        resetStyle(prenomField);
        resetStyle(sexComboBox);
        resetStyle(roleComboBox);
        resetStyle(departmentComboBox);
        resetStyle(dailyRateField);
        resetStyle(leaveDaysField);
        hideError();
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            setErrorStyle(nomField); isValid = false;
        }
        if (prenomField.getText() == null || prenomField.getText().trim().isEmpty()) {
            setErrorStyle(prenomField); isValid = false;
        }
        if (sexComboBox.getValue() == null) {
            setErrorStyle(sexComboBox); isValid = false;
        }
        if (roleComboBox.getValue() == null) {
            setErrorStyle(roleComboBox); isValid = false;
        }
        if (departmentComboBox.getValue() == null) {
            setErrorStyle(departmentComboBox); isValid = false;
        }
        String rateText = dailyRateField.getText().trim();
        if (!rateText.isEmpty()) {
            try { Double.parseDouble(rateText.replace(',', '.')); }
            catch (NumberFormatException e) { setErrorStyle(dailyRateField); isValid = false; }
        }
        String leaveText = leaveDaysField.getText().trim();
        if (!leaveText.isEmpty()) {
            try { Integer.parseInt(leaveText); }
            catch (NumberFormatException e) { setErrorStyle(leaveDaysField); isValid = false; }
        }
        if (!isValid) {
            showError("Veuillez corriger les champs indiqués en rouge.");
        }
        return isValid;
    }

    private void setErrorStyle(Control control) {
        if (!control.getStyleClass().contains(ERROR_STYLE_CLASS)) {
            control.getStyleClass().add(ERROR_STYLE_CLASS);
        }
    }

    private void resetStyle(Control control) {
        control.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void addNumericListener(TextField textField, boolean allowDecimal) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;
            String regex = allowDecimal ? "[0-9]*\\.?[0-9]*" : "\\d*";
            if (!newValue.matches(regex)) {
                textField.setText(oldValue);
            }
            if (allowDecimal && newValue.chars().filter(ch -> ch == '.').count() > 1) {
                textField.setText(oldValue);
            }
            if (allowDecimal && newValue.contains(",")) {
                textField.setText(newValue.replace(',', '.'));
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}