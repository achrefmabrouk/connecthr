package tn.esprit.connecthr.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.services.FournisseurService;

/**
 * Controller for the supplier card FXML
 */
public class CardFournisseurController implements Initializable {
    
    @FXML
    private HBox cardFournisseur;
    
    @FXML
    private ImageView imgFournisseur;
    
    @FXML
    private Label nomFournisseurLabel;
    
    @FXML
    private Label typeFournisseurLabel;
    
    @FXML
    private Label contactLabel;
    
    @FXML
    private Label adresseFournisseurLabel;
    
    @FXML
    private Label telephoneFournisseurLabel;
    
    @FXML
    private Label emailFournisseurLabel;
    
    @FXML
    private Button modifierFournisseurButton;
    
    @FXML
    private Button supprimerFournisseurButton;
    
    @FXML
    private Button analyseButton;
    
    @FXML
    private ImageView modifierIcon;
    
    @FXML
    private ImageView supprimerIcon;
    
    private Fournisseur fournisseur;
    private FournisseurService fournisseurService;
    private FournisseurController parentController;
    
    public CardFournisseurController() {
        this.fournisseurService = new FournisseurService();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set button actions
        setupButtons();
    }
    
    /**
     * Set up the card with supplier data
     * @param fournisseur The supplier data to display
     */
    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        
        // Populate the labels with supplier data
        nomFournisseurLabel.setText(fournisseur.getNom());
        typeFournisseurLabel.setText(fournisseur.getTypeFournisseur());
        contactLabel.setText(fournisseur.getContactInfo());
        adresseFournisseurLabel.setText(fournisseur.getAdresse());
        telephoneFournisseurLabel.setText("Téléphone: " + fournisseur.getTelephone());
        emailFournisseurLabel.setText("Email: " + fournisseur.getEmail());
    }
    
    /**
     * Set the parent controller for callbacks
     * @param controller The parent FournisseurController
     */
    public void setParentController(FournisseurController controller) {
        this.parentController = controller;
    }
    
    /**
     * Set up the edit and delete buttons with event handlers
     */
    private void setupButtons() {
        // Edit button action
        modifierFournisseurButton.setOnAction(event -> {
            if (parentController != null) {
                parentController.openEditFournisseurDialog(fournisseur);
            }
        });
        
        // Delete button action
        supprimerFournisseurButton.setOnAction(event -> {
            try {
                // Show confirmation dialog
                Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirmation de suppression");
                confirmDialog.setHeaderText("Supprimer le fournisseur");
                confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer le fournisseur " + fournisseur.getNom() + "?");
                
                confirmDialog.showAndWait().ifPresent(response -> {
                    if (response == javafx.scene.control.ButtonType.OK) {
                        try {
                            // Delete the supplier
                            boolean success = fournisseurService.deleteFournisseur(fournisseur.getId());
                            
                            if (success) {
                                // Notify parent controller to refresh the list
                                if (parentController != null) {
                                    parentController.refreshFournisseursList();
                                }
                                
                                // Show success message
                                Alert successAlert = new Alert(AlertType.INFORMATION);
                                successAlert.setTitle("Succès");
                                successAlert.setHeaderText("Fournisseur supprimé");
                                successAlert.setContentText("Le fournisseur a été supprimé avec succès.");
                                successAlert.show();
                            } else {
                                // Show error message
                                Alert errorAlert = new Alert(AlertType.ERROR);
                                errorAlert.setTitle("Erreur");
                                errorAlert.setHeaderText("Erreur de suppression");
                                errorAlert.setContentText("Impossible de supprimer le fournisseur.");
                                errorAlert.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            
                            // Show error message
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Erreur");
                            errorAlert.setHeaderText("Erreur de suppression");
                            errorAlert.setContentText("Une erreur est survenue lors de la suppression du fournisseur: " + e.getMessage());
                            errorAlert.show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        // Analyse performance button (if exists)
        if (analyseButton != null) {
            analyseButton.setOnAction(event -> {
                if (parentController != null) {
                    parentController.showFournisseurPerformance(fournisseur);
                }
            });
        }
    }
    
    /**
     * Create a new supplier card instance from FXML
     * @param fournisseur The supplier data to display
     * @param parentController The parent controller for callbacks
     * @return The created CardFournisseurController instance
     * @throws IOException If the FXML loading fails
     */
    public static CardFournisseurController createCard(Fournisseur fournisseur, FournisseurController parentController) throws IOException {
        FXMLLoader loader = new FXMLLoader(CardFournisseurController.class.getResource("/tn/esprit/connecthr/views/card-fournisseur.fxml"));
        loader.load();
        
        CardFournisseurController controller = loader.getController();
        controller.setFournisseur(fournisseur);
        controller.setParentController(parentController);
        
        return controller;
    }
    
    /**
     * Get the root HBox node of the card
     * @return The card's root HBox
     */
    public HBox getRoot() {
        return cardFournisseur;
    }
}
