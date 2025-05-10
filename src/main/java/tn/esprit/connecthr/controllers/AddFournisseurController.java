package tn.esprit.connecthr.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.services.FournisseurService;

/**
 * Controller for the add/edit supplier dialog
 */
public class AddFournisseurController implements Initializable {
    @FXML
    private Label titleLabel;
    
    @FXML
    private TextField txtNom;
    
    @FXML
    private ComboBox<String> comboType;
    
    @FXML
    private TextField txtContactName;
    
    @FXML
    private TextField txtContactPosition;
    
    @FXML
    private TextField txtAdresse;
    
    @FXML
    private TextField txtTelephone;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private TextArea txtNotes;
    
    @FXML
    private Button btnSave;
    
    @FXML
    private Button btnCancel;
    
    @FXML
    private Label lblErrorMessage;
    
    private FournisseurService fournisseurService;
    private FournisseurController parentController;
    private Fournisseur fournisseur;
    private boolean isEditMode = false;
    
    public AddFournisseurController() {
        fournisseurService = new FournisseurService();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the supplier type combobox
        List<String> typesFournisseur = Arrays.asList(
                "Matériel informatique", 
                "Fournitures de bureau", 
                "Mobilier", 
                "Services de nettoyage", 
                "Services de maintenance", 
                "Services informatiques", 
                "Formation",
                "Consultation",
                "Ressources humaines",
                "Marketing",
                "Autre");
        
        comboType.setItems(FXCollections.observableArrayList(typesFournisseur));
        
        // Set up button actions
        btnSave.setOnAction(this::handleSave);
        btnCancel.setOnAction(this::handleCancel);
        
        // Clear any error messages
        lblErrorMessage.setText("");
    }
    
    /**
     * Set the supplier for editing
     * @param fournisseur The supplier to edit
     */
    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        this.isEditMode = true;
        
        // Update UI for edit mode
        titleLabel.setText("Modifier le fournisseur");
        
        // Populate fields with supplier data
        txtNom.setText(fournisseur.getNom());
        comboType.setValue(fournisseur.getTypeFournisseur());
        txtContactName.setText(fournisseur.getContactName());
        txtContactPosition.setText(fournisseur.getContactPosition());
        txtAdresse.setText(fournisseur.getAdresse());
        txtTelephone.setText(fournisseur.getTelephone());
        txtEmail.setText(fournisseur.getEmail());
        txtNotes.setText(fournisseur.getNotes());
    }
    
    /**
     * Set the parent controller for callbacks
     * @param controller The parent controller
     */
    public void setParentController(FournisseurController controller) {
        this.parentController = controller;
    }
    
    /**
     * Handle save button click
     * @param event The action event
     */
    @FXML
    private void handleSave(ActionEvent event) {
        if (validateForm()) {
            try {
                if (isEditMode) {
                    // Update existing supplier
                    fournisseur.setNom(txtNom.getText().trim());
                    fournisseur.setTypeFournisseur(comboType.getValue());
                    fournisseur.setContactName(txtContactName.getText().trim());
                    fournisseur.setContactPosition(txtContactPosition.getText().trim());
                    fournisseur.setAdresse(txtAdresse.getText().trim());
                    fournisseur.setTelephone(txtTelephone.getText().trim());
                    fournisseur.setEmail(txtEmail.getText().trim());
                    fournisseur.setNotes(txtNotes.getText().trim());
                    
                    boolean success = fournisseurService.updateFournisseur(fournisseur);
                    
                    if (success) {
                        showAlert(AlertType.INFORMATION, "Succès", "Fournisseur modifié", 
                                "Le fournisseur a été modifié avec succès.");
                        closeDialog();
                    } else {
                        lblErrorMessage.setText("Erreur lors de la modification du fournisseur.");
                    }
                } else {
                    // Create new supplier
                    Fournisseur newFournisseur = new Fournisseur();
                    newFournisseur.setNom(txtNom.getText().trim());
                    newFournisseur.setTypeFournisseur(comboType.getValue());
                    newFournisseur.setContactName(txtContactName.getText().trim());
                    newFournisseur.setContactPosition(txtContactPosition.getText().trim());
                    newFournisseur.setAdresse(txtAdresse.getText().trim());
                    newFournisseur.setTelephone(txtTelephone.getText().trim());
                    newFournisseur.setEmail(txtEmail.getText().trim());
                    newFournisseur.setNotes(txtNotes.getText().trim());
                    
                    Fournisseur addedFournisseur = fournisseurService.addFournisseur(newFournisseur);
                    
                    if (addedFournisseur != null && addedFournisseur.getId() > 0) {
                        showAlert(AlertType.INFORMATION, "Succès", "Fournisseur ajouté", 
                                "Le fournisseur a été ajouté avec succès.");
                        closeDialog();
                    } else {
                        lblErrorMessage.setText("Erreur lors de l'ajout du fournisseur.");
                    }
                }
                
                // Refresh the suppliers list in the parent controller
                if (parentController != null) {
                    parentController.refreshFournisseursList();
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                lblErrorMessage.setText("Erreur de base de données: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handle cancel button click
     * @param event The action event
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        closeDialog();
    }
    
    /**
     * Validate the form fields
     * @return true if form is valid, false otherwise
     */
    private boolean validateForm() {
        StringBuilder errorMsg = new StringBuilder();
        
        if (txtNom.getText().trim().isEmpty()) {
            errorMsg.append("Le nom du fournisseur est obligatoire.\n");
        }
        
        if (comboType.getValue() == null) {
            errorMsg.append("Le type de fournisseur est obligatoire.\n");
        }
        
        if (txtContactName.getText().trim().isEmpty()) {
            errorMsg.append("Le nom du contact est obligatoire.\n");
        }
        
        if (txtAdresse.getText().trim().isEmpty()) {
            errorMsg.append("L'adresse est obligatoire.\n");
        }
        
        if (txtTelephone.getText().trim().isEmpty()) {
            errorMsg.append("Le téléphone est obligatoire.\n");
        } else if (!txtTelephone.getText().trim().matches("\\d+")) {
            errorMsg.append("Le téléphone doit contenir uniquement des chiffres.\n");
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            errorMsg.append("L'email est obligatoire.\n");
        } else if (!txtEmail.getText().trim().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            errorMsg.append("Format d'email invalide.\n");
        }
        
        if (errorMsg.length() > 0) {
            lblErrorMessage.setText(errorMsg.toString());
            return false;
        }
        
        return true;
    }
    
    /**
     * Close the dialog
     */
    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Show an alert dialog
     * @param type Alert type
     * @param title Alert title
     * @param header Alert header
     * @param content Alert content
     */
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
