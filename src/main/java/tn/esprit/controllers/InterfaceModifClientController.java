package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import tn.esprit.models.*;
import tn.esprit.services.Clientservices;

public class InterfaceModifClientController {

    @FXML
    private VBox contentArea;

    @FXML
    private Button crmBtn;

    @FXML
    private Label currentNomClient;

    @FXML
    private Label currentPrenomClient;

    @FXML
    private Label currentTelephoneClient;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button invoiceBtn;

    @FXML
    private Button modifierBtn;

    @FXML
    private TextField newNomClientInput;

    @FXML
    private TextField newPrenomClientInput;

    @FXML
    private TextField newTelephoneClientInput;

    @FXML
    private Button retourBtn;

    @FXML
    private Button searchClientBtn;

    @FXML
    private TextField searchPhoneInput;

    @FXML
    private Button staffBtn;

    @FXML
    private Button stockBtn;

    @FXML
    private Button suppliersBtn;

    @FXML
    private Button supprimerBtn;

    private Clientservices clientService = new Clientservices();


    @FXML
    void modifierClient(ActionEvent event) {

        String phone = searchPhoneInput.getText().trim();
        String newNom = newNomClientInput.getText().trim();
        String newPrenom = newPrenomClientInput.getText().trim();
        String newTelephone = newTelephoneClientInput.getText().trim();

        if (phone.isEmpty() || newNom.isEmpty() || newPrenom.isEmpty() || newTelephone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int oldPhoneNumber = Integer.parseInt(phone);
            Clients existingClient = clientService.getClientParTelephone(oldPhoneNumber);
            if (existingClient == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des données du client.");
                return;
            }
            int newPhoneNumber = Integer.parseInt(newTelephone);
            Clients updatedClient = new Clients(newNom, newPrenom, newPhoneNumber);
            updatedClient.setId_client(existingClient.getId_client());
            boolean success = clientService.update(updatedClient);
            if (success) {
                currentNomClient.setText(newNom);
                currentPrenomClient.setText(newPrenom);
                currentTelephoneClient.setText(newTelephone);
                clearInputFields();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Client mis à jour avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour du client.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les numéros de téléphone doivent être des nombres valides.");
        }
    }

    private void clearInputFields() {newNomClientInput.clear();
        newPrenomClientInput.clear();
        newTelephoneClientInput.clear();
    }

    @FXML
    void retourMain(ActionEvent event) {

    }

    @FXML
    void searchClient(ActionEvent event) {

        String phone = searchPhoneInput.getText().trim();
        if (phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un numéro de téléphone.");
            return;}

        try {int phoneNumber = Integer.parseInt(phone);
            boolean exists = clientService.chercherClientParTelephone(phoneNumber);
            if (exists) {
                Clients client = clientService.getClientParTelephone(phoneNumber);
                if (client != null) {
                    currentNomClient.setText(client.getNom_client() != null ? client.getNom_client() : "");
                    currentPrenomClient.setText(client.getPrenom_client() != null ? client.getPrenom_client() : "");
                    currentTelephoneClient.setText(client.getTelephone_client() != 0 ? String.valueOf(client.getTelephone_client()) : "");
                } else {

                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des données du client.");
                    clearLabels();}
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Information", "Aucun client trouvé avec ce numéro de téléphone.");
                clearLabels();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit être un nombre valide.");
        }
    }
    private void clearLabels() {
        currentNomClient.setText("");
        currentPrenomClient.setText("");
        currentTelephoneClient.setText("");
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

    @FXML
    void supprimerClient(ActionEvent event) {

    }

}
