package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Commandes_clients;
import tn.esprit.services.Commandeservices;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

public class PasserCommande {

    @FXML
    private TextField productIdInput;
    @FXML
    private TextField quantityInput;
    @FXML
    private TextField priceInput;
    @FXML
    private Label dateCommandeLabel;
    @FXML
    private ComboBox<String> statusInput;
    @FXML
    private TextField clientIdInput;
    @FXML
    private DatePicker dateSearchInput;
    @FXML
    private Label commandeResult;

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button staffBtn;
    @FXML
    private Button suppliersBtn;
    @FXML
    private Button invoiceBtn;
    @FXML
    private Button stockBtn;
    @FXML
    private Button crmBtn;
    @FXML
    private Button searchCommandeBtn;
    @FXML
    private Button addCommandeBtn;

    private Commandeservices commandeServices;

    public PasserCommande() {
        this.commandeServices = new Commandeservices();
    }

    @FXML
    private void initialize() {
        // Initialize ComboBox with status options
        statusInput.setItems(FXCollections.observableArrayList("En attente", "Confirmé", "Livré", "Annulé"));
        // Set current date in label
        dateCommandeLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @FXML
    private void showDashboard() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Dashboard selected");
    }

    @FXML
    private void showStaff() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Staff section selected");
    }

    @FXML
    private void showSuppliers() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Fournisseurs section selected");
    }

    @FXML
    private void showInvoices() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Factures section selected");
    }

    @FXML
    private void showStock() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Stock section selected");
    }

    @FXML
    private void showCRM() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "CRM section selected");
    }

    @FXML
    private void addCommande() {
        String productIdStr = productIdInput.getText().trim();
        String quantityStr = quantityInput.getText().trim();
        String priceStr = priceInput.getText().trim();
        String status = statusInput.getValue();
        String clientIdStr = clientIdInput.getText().trim();

        // Validation
        if (productIdStr.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty() || status == null || clientIdStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            float price = Float.parseFloat(priceStr);
            int clientId = Integer.parseInt(clientIdStr);

            if (quantity <= 0 || price <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La quantité et le prix doivent être positifs");
                return;
            }

            Commandes_clients commande = new Commandes_clients();
            commande.setId_produit(productId);
            commande.setQuantite(quantity);
            commande.setPrix(price);
            commande.setDate_commande(LocalDate.now());
            commande.setEtat_commande(status);
            commande.setId_client(clientId);

            boolean success = commandeServices.add(commande);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Commande ajoutée avec succès");
                clearCommandeForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de la commande");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les champs ID Produit, Quantité, Prix et ID Client doivent être des nombres valides");
        }
    }

    @FXML
    private void searchCommande() {
        LocalDate date = dateSearchInput.getValue();
        if (date == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommandeParDate.fxml"));
            Scene scene = new Scene(loader.load(), 930, 650);
            CommandeParDate controller = loader.getController();
            controller.setSelectedDate(date);
            Stage stage = (Stage) searchCommandeBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement de l'interface de recherche: " + e.getMessage());
        }
    }

    private void clearCommandeForm() {
        productIdInput.clear();
        quantityInput.clear();
        priceInput.clear();
        statusInput.setValue(null);
        clientIdInput.clear();
        dateCommandeLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}