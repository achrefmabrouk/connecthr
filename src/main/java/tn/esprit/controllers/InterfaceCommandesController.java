package tn.esprit.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import tn.esprit.models.*;
import tn.esprit.services.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InterfaceCommandesController {

    @FXML
    private Button addCommandeBtn;

    @FXML
    private Button addProduitBtn;

    @FXML
    private ComboBox<String> categorieComboBox;

    @FXML
    private TextField clientPhoneInput;

    @FXML
    private Button crmBtn;

    @FXML
    private Label currentNomClient;

    @FXML
    private Label currentPrenomClient;

    @FXML
    private Label dateCommandeLabel;

    @FXML
    private Button invoiceBtn;

    @FXML
    private Button searchClientBtn;

    @FXML
    private TextField searchProduitTextField;

    @FXML
    private GridPane produitsGrid;

    private Clientservices clientService = new Clientservices();
    private ProduitService produitService = new ProduitService();

    public void initialize() {
        // Charger les catégories de produits dans la ComboBox
        List<String> categories = produitService.getCategories();
        categorieComboBox.getItems().setAll(categories);

        // Ajouter un écouteur d'événements sur la sélection de catégorie
        categorieComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    afficherProduitsParCategorie(newValue);
                }
            }
        });
    }

    private void afficherProduitsParCategorie(String categorie) {
        produitsGrid.getChildren().clear();  // Vider le GridPane avant d'ajouter les nouveaux produits

        List<Produit> produits = produitService.getProductsByCategory(categorie);

        if (produits.isEmpty()) {
            showCategoryAlert(Alert.AlertType.INFORMATION, "Aucun produit", "Aucun produit trouvé dans cette catégorie.");
        } else {
            for (Produit produit : produits) {
                AfficherProduit(produit);  // Méthode pour charger l'FXML de la carte produit
            }
        }
    }

    private int colonne = 0;
    private int ligne = 0;

    private void AfficherProduit(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Produitcard.fxml"));
            AnchorPane card = loader.load();

            ProduitCardController controller = loader.getController();
            controller.setProduit(produit);

            produitsGrid.add(card, colonne, ligne);

            colonne++;
            if (colonne == 3) { // Par exemple, 3 colonnes par ligne
                colonne = 0;
                ligne++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void rechercherProduits() {
        String searchText = searchProduitTextField.getText().toLowerCase();
        String selectedCategorie = categorieComboBox.getSelectionModel().getSelectedItem();

        produitsGrid.getChildren().clear();  // Vider le GridPane avant d'ajouter les produits recherchés

        List<Produit> produits = produitService.getProductsByCategory(selectedCategorie);

        // Si la recherche est vide, on affiche tous les produits de la catégorie
        if (searchText.isEmpty()) {
            afficherProduitsParCategorie(selectedCategorie);
        } else {
            // Recherche des produits par nom
            for (Produit produit : produits) {
                // Si le produit correspond à la recherche
                if (produit.getNom_prod().toLowerCase().contains(searchText)) {
                    // Créer une carte de produit en utilisant un AnchorPane ou VBox
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Produitcard.fxml")); // Chemin relatif
                        AnchorPane productCard = loader.load();

                        // Récupérer le contrôleur de la carte produit
                        ProduitCardController productCardController = loader.getController();
                        productCardController.setProduit(produit); // Passer l'objet produit à la carte pour l'affichage

                        // Ajouter la carte produit dans le GridPane
                        produitsGrid.add(productCard, 0, produitsGrid.getChildren().size());
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la carte produit.");
                    }
                }
            }
        }
    }

    @FXML
    void loadProduits(ActionEvent event) {
        // Récupérer la catégorie sélectionnée dans le ComboBox
        String selectedCategory = categorieComboBox.getSelectionModel().getSelectedItem();

        // Vérifier si une catégorie est sélectionnée
        if (selectedCategory != null) {
            afficherProduitsParCategorie(selectedCategory);  // Afficher les produits de la catégorie sélectionnée
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune catégorie sélectionnée", "Veuillez sélectionner une catégorie.");
        }
    }

    private void showCategoryAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void addCommande(ActionEvent event) {
        // Logique pour ajouter une commande
    }

    @FXML
    void addProduitToCommande(ActionEvent event) {
        // Logique pour ajouter un produit à la commande
    }

    @FXML
    void searchClient(ActionEvent event) {
        String phone = clientPhoneInput.getText().trim();
        if (phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un numéro de téléphone.");
        }

        try {
            int phoneNumber = Integer.parseInt(phone);
            boolean exists = clientService.chercherClientParTelephone(phoneNumber);

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentDate.format(formatter);

            dateCommandeLabel.setText(formattedDate);

            if (exists) {
                Clients client = clientService.getClientParTelephone(phoneNumber);
                if (client != null) {
                    currentNomClient.setText(client.getNom_client() != null ? client.getNom_client() : "");
                    currentPrenomClient.setText(client.getPrenom_client() != null ? client.getPrenom_client() : "");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des données du client.");
                    clearClientLabels();
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Information", "Aucun client trouvé avec ce numéro de téléphone.");
                clearClientLabels();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit être un nombre valide.");
        }
    }

    private void clearClientLabels() {
        currentNomClient.setText("");
        currentPrenomClient.setText("");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void showCRM(ActionEvent event) {
        // Logique pour afficher le CRM
    }
}
