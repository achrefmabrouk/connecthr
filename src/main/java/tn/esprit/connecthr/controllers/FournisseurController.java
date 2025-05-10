package tn.esprit.connecthr.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.services.FournisseurService;

/**
 * Controller for the supplier management view
 */
public class FournisseurController implements Initializable {
    
    /**
     * Ouvrir le rapport de performance du fournisseur sélectionné
     * @param fournisseur Le fournisseur pour lequel générer le rapport
     */
    public void showFournisseurPerformance(Fournisseur fournisseur) {
        try {
            // Charger la vue de rapport
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/connecthr/views/rapport-fournisseur-view.fxml"));
            Parent root = loader.load();
            
            // Configurer la fenêtre
            Stage stage = new Stage();
            stage.setTitle("Rapport de Performance - " + fournisseur.getNom());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(javafx.stage.StageStyle.DECORATED);
            
            // Récupérer le contrôleur et initialiser le fournisseur
            RapportFournisseurController controller = loader.getController();
            
            // Définir la scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            
            // Afficher la fenêtre
            stage.show();
            
            // Pré-sélectionner le fournisseur dans le ComboBox du rapport
            if (controller.getComboFournisseur() != null) {
                controller.getComboFournisseur().getSelectionModel().select(fournisseur);
                controller.genererRapportFournisseur(fournisseur.getId());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de chargement", 
                    "Impossible de charger le rapport de performance: " + e.getMessage());
        }
    }
    @FXML
    private ScrollPane scrollPaneFournisseurs;
    
    @FXML
    private FlowPane flowPaneFournisseurs;
    
    @FXML
    private Button btnAddFournisseur;
    
    @FXML
    private TextField txtSearchFournisseur;
    
    @FXML
    private Button btnSearchFournisseur;
    
    private FournisseurService fournisseurService;
    
    public FournisseurController() {
        fournisseurService = new FournisseurService();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configure FlowPane
        flowPaneFournisseurs.setHgap(20);
        flowPaneFournisseurs.setVgap(20);
        flowPaneFournisseurs.setPrefWidth(scrollPaneFournisseurs.getPrefWidth() - 20);
        
        // Set up event handlers
        btnAddFournisseur.setOnAction(this::handleAddFournisseur);
        btnSearchFournisseur.setOnAction(this::handleSearchFournisseur);
        
        // Load suppliers when view is initialized
        refreshFournisseursList();
    }
    
    /**
     * Refresh the list of suppliers
     */
    public void refreshFournisseursList() {
        try {
            // Clear existing items
            flowPaneFournisseurs.getChildren().clear();
            
            // Get all suppliers
            List<Fournisseur> fournisseurs = fournisseurService.getAllFournisseurs();
            
            if (fournisseurs.isEmpty()) {
                // Show empty state message
                VBox emptyState = new VBox();
                emptyState.setSpacing(10);
                emptyState.setAlignment(javafx.geometry.Pos.CENTER);
                emptyState.setPrefWidth(flowPaneFournisseurs.getPrefWidth());
                emptyState.setPrefHeight(200);
                
                javafx.scene.control.Label emptyLabel = new javafx.scene.control.Label("Aucun fournisseur trouvé");
                emptyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #555;");
                
                Button addButton = new Button("Ajouter un fournisseur");
                addButton.setOnAction(this::handleAddFournisseur);
                
                emptyState.getChildren().addAll(emptyLabel, addButton);
                flowPaneFournisseurs.getChildren().add(emptyState);
            } else {
                // Create and add supplier cards
                for (Fournisseur fournisseur : fournisseurs) {
                    CardFournisseurController cardController = CardFournisseurController.createCard(fournisseur, this);
                    flowPaneFournisseurs.getChildren().add(cardController.getRoot());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible de charger les fournisseurs", 
                    "Une erreur est survenue lors du chargement des fournisseurs: " + e.getMessage());
        }
    }
    
    /**
     * Handle add supplier button click
     * @param event The action event
     */
    @FXML
    private void handleAddFournisseur(ActionEvent event) {
        try {
            // Load the add supplier dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/connecthr/views/add-fournisseur.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un fournisseur");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            // Set the scene
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            
            // Get the controller and set callback for refreshing the list
            Object controller = loader.getController();
            if (controller instanceof javafx.fxml.Initializable) {
                if (controller.getClass().getSimpleName().equals("AddFournisseurController")) {
                    java.lang.reflect.Method method = controller.getClass().getMethod("setParentController", FournisseurController.class);
                    method.invoke(controller, this);
                }
            }
            
            // Show the dialog
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire", 
                    "Une erreur est survenue lors de l'ouverture du formulaire d'ajout: " + e.getMessage());
        }
    }
    
    /**
     * Open edit supplier dialog
     * @param fournisseur The supplier to edit
     */
    public void openEditFournisseurDialog(Fournisseur fournisseur) {
        try {
            // Load the edit supplier dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/connecthr/views/add-fournisseur.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier le fournisseur");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            // Set the scene
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            
            // Get the controller and set the supplier data
            Object controller = loader.getController();
            if (controller instanceof javafx.fxml.Initializable) {
                if (controller.getClass().getSimpleName().equals("AddFournisseurController")) {
                    java.lang.reflect.Method setFournisseurMethod = controller.getClass().getMethod("setFournisseur", Fournisseur.class);
                    setFournisseurMethod.invoke(controller, fournisseur);
                    
                    java.lang.reflect.Method setParentControllerMethod = controller.getClass().getMethod("setParentController", FournisseurController.class);
                    setParentControllerMethod.invoke(controller, this);
                }
            }
            
            // Show the dialog
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire", 
                    "Une erreur est survenue lors de l'ouverture du formulaire de modification: " + e.getMessage());
        }
    }
    
    /**
     * Handle search supplier button click
     * @param event The action event
     */
    @FXML
    private void handleSearchFournisseur(ActionEvent event) {
        String searchTerm = txtSearchFournisseur.getText().trim();
        
        if (searchTerm.isEmpty()) {
            refreshFournisseursList();
            return;
        }
        
        try {
            // Clear existing items
            flowPaneFournisseurs.getChildren().clear();
            
            // Search suppliers
            List<Fournisseur> fournisseurs = fournisseurService.searchFournisseurs(searchTerm);
            
            if (fournisseurs.isEmpty()) {
                // Show empty search results message
                VBox emptyState = new VBox();
                emptyState.setSpacing(10);
                emptyState.setAlignment(javafx.geometry.Pos.CENTER);
                emptyState.setPrefWidth(flowPaneFournisseurs.getPrefWidth());
                emptyState.setPrefHeight(200);
                
                javafx.scene.control.Label emptyLabel = new javafx.scene.control.Label("Aucun résultat pour \"" + searchTerm + "\"");
                emptyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #555;");
                
                Button clearButton = new Button("Effacer la recherche");
                clearButton.setOnAction(e -> {
                    txtSearchFournisseur.clear();
                    refreshFournisseursList();
                });
                
                emptyState.getChildren().addAll(emptyLabel, clearButton);
                flowPaneFournisseurs.getChildren().add(emptyState);
            } else {
                // Create and add supplier cards
                for (Fournisseur fournisseur : fournisseurs) {
                    CardFournisseurController cardController = CardFournisseurController.createCard(fournisseur, this);
                    flowPaneFournisseurs.getChildren().add(cardController.getRoot());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de recherche", 
                    "Une erreur est survenue lors de la recherche: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur d'affichage", 
                    "Une erreur est survenue lors de l'affichage des résultats: " + e.getMessage());
        }
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
