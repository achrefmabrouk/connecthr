package tn.esprit.connecthr.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Main controller for the application
 */
public class MainController implements Initializable {
    
    @FXML
    private BorderPane mainLayout;
    
    @FXML
    private Button btnFournisseurs;
    
    @FXML
    private Button btnCommandes;
    
    @FXML
    private Button btnPaiements;
    
    @FXML
    private Button btnAnalyses;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up event handlers for navigation buttons
        btnFournisseurs.setOnAction(e -> loadView("/tn/esprit/connecthr/views/fournisseur-view.fxml"));
        btnCommandes.setOnAction(e -> loadView("/tn/esprit/connecthr/views/commande-view.fxml"));
        btnPaiements.setOnAction(e -> loadView("/tn/esprit/connecthr/views/paiement-view.fxml"));
        
        // Configurer le bouton d'analyses s'il existe
        if (btnAnalyses != null) {
            btnAnalyses.setOnAction(e -> loadView("/tn/esprit/connecthr/views/rapport-fournisseur-view.fxml"));
        }
        
        // Load the default view (Fournisseurs)
        loadView("/tn/esprit/connecthr/views/fournisseur-view.fxml");
    }
    
    /**
     * Load a view into the main content area
     * @param fxmlPath The path to the FXML file
     */
    private void loadView(String fxmlPath) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            
            // Set the view in the center of the BorderPane
            mainLayout.setCenter(view);
            
            // Make sure the view fills the available space
            AnchorPane.setTopAnchor((Node) view, 0.0);
            AnchorPane.setRightAnchor((Node) view, 0.0);
            AnchorPane.setBottomAnchor((Node) view, 0.0);
            AnchorPane.setLeftAnchor((Node) view, 0.0);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}