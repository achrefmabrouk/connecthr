package tn.esprit.controllers;
import javafx.scene.control.Alert;
import tn.esprit.models.*;
import tn.esprit.services.*;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.*;


public class InterfaceAjoutController {

    @FXML
    private Button AjouterclientBtn;

    @FXML
    private Button crmBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button invoiceBtn;

    @FXML
    private TextField nomClientInput;

    @FXML
    private TextField prenomClientInput;

    @FXML
    private Button retourBtn;

    @FXML
    private Button staffBtn;

    @FXML
    private Button stockBtn;

    @FXML
    private Button suppliersBtn;

    @FXML
    private TextField telephoneClientInput;



    private Clientservices clientService = new Clientservices(); // ou injecte selon ton architecture


    @FXML
    public void initialize() {

    }
    public class ClientController {

        @FXML
        private TextField nomClientInput;
        @FXML
        private TextField prenomClientInput;
        @FXML
        private TextField telephoneClientInput;

        private Clientservices clientService; // Assuming this is injected or initialized elsewhere

        @FXML
        void ajouterClient(ActionEvent event) {
            String nom = nomClientInput.getText().trim();
            String prenom = prenomClientInput.getText().trim();
            String telephoneStr = telephoneClientInput.getText().trim();

            // Vérification basique des champs
            if (nom.isEmpty() || prenom.isEmpty() || telephoneStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires.");
                return;
            }

            try {
                int telephone = Integer.parseInt(telephoneStr);

                Clients client = new Clients();
                client.setNom_client(nom);
                client.setPrenom_client(prenom);
                client.setTelephone_client(telephone);

                // Appel à la méthode add pour ajouter le client
                boolean success = clientService.add(client);

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès",
                            "Client ajouté avec succès. ID : " + client.getId_client());
                    // Réinitialiser les champs après ajout
                    nomClientInput.clear();
                    prenomClientInput.clear();
                    telephoneClientInput.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du client.");
                }

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le téléphone doit être un entier valide.");
            }
        }

        private void showAlert(Alert.AlertType alertType, String title, String content) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }



    @FXML
    void retourMain(ActionEvent event) {

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

}}
