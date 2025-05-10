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
import tn.esprit.connecthr.models.Paiement;
import tn.esprit.connecthr.services.CommandeService;
import tn.esprit.connecthr.services.PaiementService;

/**
 * Controller for the add payment dialog
 */
public class AddPaiementController implements Initializable {
    @FXML
    private TextField txtCommande;
    
    @FXML
    private TextField txtNumeroPaiement;
    
    @FXML
    private DatePicker dateDatePaiement;
    
    @FXML
    private TextField txtMontant;
    
    @FXML
    private ComboBox<String> comboMethodePaiement;
    
    @FXML
    private ComboBox<String> comboStatut;
    
    @FXML
    private TextField txtReference;
    
    @FXML
    private TextArea txtDescription;
    
    @FXML
    private Button btnSave;
    
    @FXML
    private Button btnCancel;
    
    @FXML
    private Label lblErrorMessage;
    
    private PaiementService paiementService;
    private CommandeService commandeService;
    private CommandeController parentController;
    private Commande commande;
    private double resteAPayer = 0;
    
    public AddPaiementController() {
        paiementService = new PaiementService();
        commandeService = new CommandeService();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the payment method combobox
        List<String> methodesPaiement = Arrays.asList(
                "Virement bancaire", 
                "Chèque", 
                "Espèces", 
                "Carte bancaire",
                "PayPal",
                "Autre");
        
        comboMethodePaiement.setItems(FXCollections.observableArrayList(methodesPaiement));
        
        // Initialize the payment status combobox
        List<String> statutsPaiement = Arrays.asList(
                "En attente", 
                "Complété", 
                "Échoué", 
                "Annulé");
        
        comboStatut.setItems(FXCollections.observableArrayList(statutsPaiement));
        
        // Set default values
        dateDatePaiement.setValue(LocalDate.now());
        comboStatut.setValue("En attente");
        
        // Set up button actions
        btnSave.setOnAction(this::handleSave);
        btnCancel.setOnAction(this::handleCancel);
        
        // Clear any error messages
        lblErrorMessage.setText("");
        
        // Try to generate a new payment number
        try {
            String newPaymentNumber = paiementService.generatePaymentNumber();
            txtNumeroPaiement.setText(newPaymentNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            lblErrorMessage.setText("Erreur lors de la génération du numéro de paiement.");
        }
    }
    
    /**
     * Set the order for payment
     * @param commande The order to pay
     */
    public void setCommande(Commande commande) {
        this.commande = commande;
        
        // Set the order info
        if (commande != null) {
            txtCommande.setText(commande.getNumeroCommande() + " - " + 
                               (commande.getFournisseur() != null ? commande.getFournisseur().getNom() : ""));
            
            try {
                // Calculate remaining amount to pay
                double totalPaid = paiementService.getTotalPaidForCommande(commande.getId());
                resteAPayer = commande.getMontantTotal() - totalPaid;
                
                // Set the default amount to the remaining amount
                txtMontant.setText(String.format("%.2f", resteAPayer));
                
                // Set description with payment details
                txtDescription.setText("Paiement pour la commande " + commande.getNumeroCommande() + 
                                     "\nFournisseur: " + (commande.getFournisseur() != null ? commande.getFournisseur().getNom() : "N/A") +
                                     "\nMontant total de la commande: " + String.format("%,.2f TND", commande.getMontantTotal()) +
                                     "\nMontant déjà payé: " + String.format("%,.2f TND", totalPaid) +
                                     "\nReste à payer: " + String.format("%,.2f TND", resteAPayer));
                
            } catch (SQLException e) {
                e.printStackTrace();
                lblErrorMessage.setText("Erreur lors du calcul du montant restant à payer.");
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
                // Create new payment
                Paiement newPaiement = new Paiement();
                newPaiement.setCommandeId(commande.getId());
                newPaiement.setNumeroPaiement(txtNumeroPaiement.getText().trim());
                newPaiement.setDatePaiement(dateDatePaiement.getValue());
                newPaiement.setMontant(Double.parseDouble(txtMontant.getText().trim().replace(",", ".")));
                newPaiement.setMethodePaiement(comboMethodePaiement.getValue());
                newPaiement.setStatut(comboStatut.getValue());
                newPaiement.setReference(txtReference.getText().trim());
                newPaiement.setDescription(txtDescription.getText().trim());
                
                Paiement addedPaiement = paiementService.addPaiement(newPaiement);
                
                if (addedPaiement != null && addedPaiement.getId() > 0) {
                    // Check if order status should be updated
                    if (comboStatut.getValue().equals("Complété")) {
                        try {
                            double totalPaid = paiementService.getTotalPaidForCommande(commande.getId());
                            
                            // If fully paid and order is not yet marked as delivered, update it
                            if (Math.abs(totalPaid - commande.getMontantTotal()) < 0.01 && 
                                !commande.getStatut().equals("Livrée")) {
                                
                                // Ask the user if they want to update the order status
                                Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
                                confirmDialog.setTitle("Mise à jour du statut de la commande");
                                confirmDialog.setHeaderText("Commande entièrement payée");
                                confirmDialog.setContentText("La commande est maintenant entièrement payée. Voulez-vous mettre à jour son statut à \"Livrée\"?");
                                
                                confirmDialog.showAndWait().ifPresent(response -> {
                                    if (response == javafx.scene.control.ButtonType.OK) {
                                        try {
                                            commande.setStatut("Livrée");
                                            commandeService.updateCommande(commande);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    
                    showAlert(AlertType.INFORMATION, "Succès", "Paiement ajouté", 
                            "Le paiement a été ajouté avec succès.");
                    closeDialog();
                } else {
                    lblErrorMessage.setText("Erreur lors de l'ajout du paiement.");
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
        
        if (txtNumeroPaiement.getText().trim().isEmpty()) {
            errorMsg.append("Le numéro de paiement est obligatoire.\n");
        }
        
        if (dateDatePaiement.getValue() == null) {
            errorMsg.append("La date de paiement est obligatoire.\n");
        }
        
        if (txtMontant.getText().trim().isEmpty()) {
            errorMsg.append("Le montant est obligatoire.\n");
        } else {
            try {
                double montant = Double.parseDouble(txtMontant.getText().trim().replace(",", "."));
                if (montant <= 0) {
                    errorMsg.append("Le montant doit être supérieur à zéro.\n");
                } else if (montant > resteAPayer) {
                    errorMsg.append("Le montant ne peut pas dépasser le reste à payer (" + 
                                    String.format("%.2f", resteAPayer) + " TND).\n");
                }
            } catch (NumberFormatException e) {
                errorMsg.append("Format de montant invalide. Utilisez des nombres (ex: 1234.56).\n");
            }
        }
        
        if (comboMethodePaiement.getValue() == null) {
            errorMsg.append("La méthode de paiement est obligatoire.\n");
        }
        
        if (comboStatut.getValue() == null) {
            errorMsg.append("Le statut est obligatoire.\n");
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
