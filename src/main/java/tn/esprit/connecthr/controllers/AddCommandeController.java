package tn.esprit.connecthr.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.connecthr.models.Commande;
import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.services.CommandeService;
import tn.esprit.connecthr.services.FournisseurService;

/**
 * Controller for the add/edit order dialog
 */
public class AddCommandeController implements Initializable {
    @FXML
    private ComboBox<Fournisseur> comboFournisseur;
    
    @FXML
    private TextField txtNumeroCommande;
    
    @FXML
    private DatePicker dateDateCommande;
    
    @FXML
    private DatePicker dateDateLivraison;
    
    @FXML
    private ComboBox<String> comboStatut;
    
    @FXML
    private TextField txtMontantTotal;
    
    @FXML
    private TextArea txtDescription;
    
    @FXML
    private Button btnSave;
    
    @FXML
    private Button btnCancel;
    
    @FXML
    private Label lblErrorMessage;
    
    private CommandeService commandeService;
    private FournisseurService fournisseurService;
    private CommandeController parentController;
    private Commande commande;
    private boolean isEditMode = false;
    
    public AddCommandeController() {
        commandeService = new CommandeService();
        fournisseurService = new FournisseurService();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the order status combobox
        List<String> statutsCommande = Arrays.asList(
                "En attente", 
                "En cours", 
                "Livrée", 
                "Annulée");
        
        comboStatut.setItems(FXCollections.observableArrayList(statutsCommande));
        
        // Load suppliers into combobox
        loadFournisseurs();
        
        // Set default values
        dateDateCommande.setValue(LocalDate.now());
        
        // Set up button actions
        btnSave.setOnAction(this::handleSave);
        btnCancel.setOnAction(this::handleCancel);
        
        // Clear any error messages
        lblErrorMessage.setText("");
        
        // Try to generate a new order number
        try {
            String newOrderNumber = commandeService.generateOrderNumber();
            txtNumeroCommande.setText(newOrderNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            lblErrorMessage.setText("Erreur lors de la génération du numéro de commande.");
        }
    }
    
    /**
     * Load suppliers into the combobox
     */
    private void loadFournisseurs() {
        try {
            List<Fournisseur> fournisseurs = fournisseurService.getAllFournisseurs();
            comboFournisseur.setItems(FXCollections.observableArrayList(fournisseurs));
            
            // Configure display text
            comboFournisseur.setCellFactory(param -> new javafx.scene.control.ListCell<Fournisseur>() {
                @Override
                protected void updateItem(Fournisseur item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNom());
                    }
                }
            });
            
            comboFournisseur.setButtonCell(new javafx.scene.control.ListCell<Fournisseur>() {
                @Override
                protected void updateItem(Fournisseur item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNom());
                    }
                }
            });
            
        } catch (SQLException e) {
            e.printStackTrace();
            lblErrorMessage.setText("Erreur lors du chargement des fournisseurs.");
        }
    }
    
    /**
     * Set the order for editing
     * @param commande The order to edit
     */
    public void setCommande(Commande commande) {
        this.commande = commande;
        this.isEditMode = true;
        
        // Update UI for edit mode
        txtNumeroCommande.setText(commande.getNumeroCommande());
        dateDateCommande.setValue(commande.getDateCommande());
        dateDateLivraison.setValue(commande.getDateLivraison());
        comboStatut.setValue(commande.getStatut());
        txtMontantTotal.setText(String.valueOf(commande.getMontantTotal()));
        txtDescription.setText(commande.getDescription());
        
        // Select the correct fournisseur in the combobox
        for (Fournisseur f : comboFournisseur.getItems()) {
            if (f.getId() == commande.getFournisseurId()) {
                comboFournisseur.setValue(f);
                break;
            }
        }
    }
    
    /**
     * Set the parent controller for callbacks
     * @param controller The parent controller
     */
    public void setParentController(CommandeController controller) {
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
                Fournisseur selectedFournisseur = comboFournisseur.getValue();
                
                if (isEditMode) {
                    // Update existing order
                    commande.setFournisseurId(selectedFournisseur.getId());
                    commande.setNumeroCommande(txtNumeroCommande.getText().trim());
                    commande.setDateCommande(dateDateCommande.getValue());
                    commande.setDateLivraison(dateDateLivraison.getValue());
                    commande.setStatut(comboStatut.getValue());
                    commande.setMontantTotal(Double.parseDouble(txtMontantTotal.getText().trim().replace(",", ".")));
                    commande.setDescription(txtDescription.getText().trim());
                    
                    boolean success = commandeService.updateCommande(commande);
                    
                    if (success) {
                        showAlert(AlertType.INFORMATION, "Succès", "Commande modifiée", 
                                "La commande a été modifiée avec succès.");
                        closeDialog();
                    } else {
                        lblErrorMessage.setText("Erreur lors de la modification de la commande.");
                    }
                } else {
                    // Create new order
                    Commande newCommande = new Commande();
                    newCommande.setFournisseurId(selectedFournisseur.getId());
                    newCommande.setNumeroCommande(txtNumeroCommande.getText().trim());
                    newCommande.setDateCommande(dateDateCommande.getValue());
                    newCommande.setDateLivraison(dateDateLivraison.getValue());
                    newCommande.setStatut(comboStatut.getValue());
                    newCommande.setMontantTotal(Double.parseDouble(txtMontantTotal.getText().trim().replace(",", ".")));
                    newCommande.setDescription(txtDescription.getText().trim());
                    
                    Commande addedCommande = commandeService.addCommande(newCommande);
                    
                    if (addedCommande != null && addedCommande.getId() > 0) {
                        showAlert(AlertType.INFORMATION, "Succès", "Commande ajoutée", 
                                "La commande a été ajoutée avec succès.");
                        closeDialog();
                    } else {
                        lblErrorMessage.setText("Erreur lors de l'ajout de la commande.");
                    }
                }
                
                // Refresh the orders list in the parent controller
                if (parentController != null) {
                    parentController.refreshCommandesList();
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                lblErrorMessage.setText("Erreur de base de données: " + e.getMessage());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                lblErrorMessage.setText("Format de montant invalide. Utilisez des nombres (ex: 1234.56)");
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
        
        if (comboFournisseur.getValue() == null) {
            errorMsg.append("Le fournisseur est obligatoire.\n");
        }
        
        if (txtNumeroCommande.getText().trim().isEmpty()) {
            errorMsg.append("Le numéro de commande est obligatoire.\n");
        }
        
        if (dateDateCommande.getValue() == null) {
            errorMsg.append("La date de commande est obligatoire.\n");
        }
        
        if (comboStatut.getValue() == null) {
            errorMsg.append("Le statut est obligatoire.\n");
        }
        
        if (txtMontantTotal.getText().trim().isEmpty()) {
            errorMsg.append("Le montant total est obligatoire.\n");
        } else {
            try {
                double montant = Double.parseDouble(txtMontantTotal.getText().trim().replace(",", "."));
                if (montant <= 0) {
                    errorMsg.append("Le montant total doit être supérieur à zéro.\n");
                }
            } catch (NumberFormatException e) {
                errorMsg.append("Format de montant invalide. Utilisez des nombres (ex: 1234.56).\n");
            }
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
