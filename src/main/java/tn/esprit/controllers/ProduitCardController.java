package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.models.Produit;
import tn.esprit.services.ProduitService;

public class ProduitCardController {

    @FXML
    private Label nomLabel;

    @FXML
    private Label categorieLabel;

    @FXML
    private Label prixLabel;

    @FXML
    private Label etatLabel;

    @FXML
    private TextField quantiteInput;

    @FXML
    private Button increaseQuantiteBtn;

    @FXML
    private Button decreaseQuantiteBtn;

    private ProduitService produitService = new ProduitService();
    private Produit produit = new Produit();

    // Cette méthode initialise les données de la carte avec les informations du produit
    public void setProduit(Produit produit) {
        this.produit = produit; // Sauvegarder l'objet produit pour l'utiliser dans d'autres méthodes
        nomLabel.setText(produit.getNom_prod());
        categorieLabel.setText("Catégorie: " + produitService.getCategorieByNom(produit.getNom_prod()));
        prixLabel.setText("Prix: " + produit.getPrix_prod());
        etatLabel.setText(produitService.verifierQuantiteDisponible(produit.getId_prod(), 1) ? "Disponible" : "Indisponible"); // Vérification initiale
    }

    @FXML
    private void updateEtatLabel() {
        try {
            String inputText = quantiteInput.getText().trim(); // Récupérer l'entrée utilisateur
            float quantiteDemandee;

            // Vérifier si l'entrée est un entier ou un float
            if (inputText.contains(".")) {
                quantiteDemandee = Float.parseFloat(inputText); // Traiter comme float
            } else {
                quantiteDemandee = Integer.parseInt(inputText); // Traiter comme int
            }

            // Vérifier que la quantité demandée est valide (positive)
            if (quantiteDemandee <= 0) {
                etatLabel.setText("Quantité invalide");
                etatLabel.setStyle("-fx-text-fill: orange;");
                return;
            }

            int idProduit = produit.getId_prod(); // Assure-toi d'avoir l'ID du produit
            boolean isAvailable = produitService.verifierQuantiteDisponible(idProduit, (int) quantiteDemandee);

            // Mettre à jour l'état en fonction de la disponibilité
            if (isAvailable) {
                etatLabel.setText("En stock");
                etatLabel.setStyle("-fx-text-fill: green;");
            } else {
                etatLabel.setText("Hors stock");
                etatLabel.setStyle("-fx-text-fill: red;");
            }

        } catch (NumberFormatException e) {
            etatLabel.setText("Quantité invalide");
            etatLabel.setStyle("-fx-text-fill: orange;");
        }
    }
    @FXML
    private void increaseQuantite() {
        try {
            int quantiteDemandee = Integer.parseInt(quantiteInput.getText());
            quantiteInput.setText(String.valueOf(quantiteDemandee + 1)); // Incrémenter la quantité
            updateEtatLabel(); // Mettre à jour l'état après modification
        } catch (NumberFormatException e) {
            quantiteInput.setText("1"); // Initialiser à 1 si l'entrée n'est pas valide
            updateEtatLabel();
        }
    }

    @FXML
    private void decreaseQuantite() {
        try {
            int quantiteDemandee = Integer.parseInt(quantiteInput.getText());
            if (quantiteDemandee > 1) {
                quantiteInput.setText(String.valueOf(quantiteDemandee - 1)); // Décrémenter la quantité
                updateEtatLabel(); // Mettre à jour l'état après modification
            }
        } catch (NumberFormatException e) {
            quantiteInput.setText("1"); // Initialiser à 1 si l'entrée n'est pas valide
            updateEtatLabel();
        }
    }
}
