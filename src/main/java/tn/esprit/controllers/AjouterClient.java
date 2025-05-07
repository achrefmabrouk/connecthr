package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Clients;
import tn.esprit.services.Clientservices;

import java.io.IOException;

public class AjouterClient {

    @FXML
    private TextField phoneInput;
    @FXML
    private TextField lastNameInput;
    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField clientPhoneInput;
    @FXML
    private Label clientResult;
    @FXML
    private Button orderBtn;
    @FXML
    private Button searchClientBtn;
    @FXML
    private Button viewOrdersBtn;
    @FXML
    private Button addClientBtn;
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

    private Clientservices clientServices;

    public AjouterClient() {
        this.clientServices = new Clientservices();
    }

    @FXML
    private void initialize() {
        // Initialize any UI components if needed
    }

    @FXML
    private void placeOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PasserCommande.fxml"));
            Scene scene = new Scene(loader.load(), 930, 650);
            Stage stage = (Stage) orderBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement de l'interface Passer Commande: " + e.getMessage());
        }
    }

    @FXML
    private void searchClient() {
        String phone = phoneInput.getText().trim();
        if (phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un numéro de téléphone");
            return;
        }

        try {
            String phone1 = phone;
            Clients client = clientServices.getTelephone_client((phone));
            if (client != null) {
                clientResult.setText("Client trouvé: " + client.getPrenom() + " " + client.getNom() + ", Téléphone: " + client.getTelephone());
            } else {
                clientResult.setText("Aucun client trouvé avec le numéro " + phone);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recherche du client: " + e.getMessage());
        }
    }

    @FXML
    private void viewOrders() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Consulter les commandes sélectionné");
        // TODO: Implement navigation to view orders interface if needed
    }

    @FXML
    private void addClient() {
        String nom = lastNameInput.getText().trim();
        String prenom = firstNameInput.getText().trim();
        String telephone = clientPhoneInput.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs");
            return;
        }

        try {
            Clients client = new Clients();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setTelephone(telephone);

            boolean success = clientServices.add(client);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Client ajouté avec succès");
                clearClientForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du client");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du client: " + e.getMessage());
        }
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

    private void clearClientForm() {
        lastNameInput.clear();
        firstNameInput.clear();
        clientPhoneInput.clear();
        phoneInput.clear();
        clientResult.setText("");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}