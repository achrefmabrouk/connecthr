package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tn.esprit.models.Commandes_clients;
import tn.esprit.services.Commandeservices;

import java.time.LocalDate;
import java.util.List;

public class ModifierCommandeController {

    @FXML
    private VBox contentArea;

    @FXML
    private Button crmBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button invoiceBtn;

    @FXML
    private GridPane productsGrid;

    @FXML
    private Button retourBtn;

    @FXML
    private Button searchDateBtn;

    @FXML
    private DatePicker searchDateInput;

    @FXML
    private Button searchOrderBtn;

    @FXML
    private TextField searchOrderIdInput;

    @FXML
    private Button staffBtn;

    @FXML
    private Button stockBtn;

    @FXML
    private Button suppliersBtn;

    private Commandeservices commandeService=new Commandeservices();

    @FXML
    void retourMain(ActionEvent event) {

    }


    @FXML
    private void searchByDate(ActionEvent event) {
        LocalDate selectedDate = searchDateInput.getValue();
        if (selectedDate == null) {
            showAlert("Erreur", "Veuillez sélectionner une date.");
            return;
        }

        List<Commandes_clients> commandes = commandeService.afficherCommandesParDate(selectedDate);
        displayOrderCards(commandes);
    }

    private void displayOrderCards(List<Commandes_clients> commandes) {
        productsGrid.getChildren().clear();

        if (commandes.isEmpty()) {
            showAlert("Information", "Aucune commande trouvée pour cette date.");
            return;
        }

        int row = 0;
        int col = 0;
        for (Commandes_clients cmd : commandes) {
            VBox card = new VBox(8);
            card.getStyleClass().add("order-card");
            card.setPrefWidth(300);
            card.setPrefHeight(150);

            Label idLabel = new Label("ID Commande: " + cmd.getId_commande());
            idLabel.getStyleClass().add("card-label");
            Label produitLabel = new Label("ID Produit: " + cmd.getId_produit());
            produitLabel.getStyleClass().add("card-label");
            Label quantiteLabel = new Label("Quantité: " + cmd.getQuantite());
            quantiteLabel.getStyleClass().add("card-label");
            Label prixLabel = new Label("Prix: " + String.format("%.2f", cmd.getPrix()));
            prixLabel.getStyleClass().add("card-label");
            Label etatLabel = new Label("État: " + cmd.getEtat_commande());
            etatLabel.getStyleClass().add("card-label");
            Label clientLabel = new Label("ID Client: " + cmd.getId_client());
            clientLabel.getStyleClass().add("card-label");

            card.getChildren().addAll(idLabel, produitLabel, quantiteLabel, prixLabel, etatLabel, clientLabel);
            productsGrid.add(card, col, row);

            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void searchOrder(ActionEvent event) {

    }

    @FXML
    void showCRM(ActionEvent event) {

    }

    @FXML
    void showDashboard(ActionEvent event) {

    }

    @FXML
    void showInvoices(ActionEvent event) {

    }

    @FXML
    void showStaff(ActionEvent event) {

    }

    @FXML
    void showStock(ActionEvent event) {

    }

    @FXML
    void showSuppliers(ActionEvent event) {

    }

}
