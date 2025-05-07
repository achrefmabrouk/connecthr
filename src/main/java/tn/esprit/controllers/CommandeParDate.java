package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Commandes_clients;
import tn.esprit.services.Commandeservices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommandeParDate {

    @FXML
    private VBox commandesContainer;

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

    private Commandeservices commandeServices;
    private LocalDate selectedDate;

    public CommandeParDate() {
        this.commandeServices = new Commandeservices();
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        refreshCommandes();
    }

    @FXML
    private void initialize() {
        // Initialize sidebar buttons if needed
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

    private void refreshCommandes() {
        if (selectedDate == null) return;

        commandesContainer.getChildren().clear();

        // Fetch commands for the selected date
        List<Commandes_clients> commandes = getCommandesByDate(selectedDate);
        if (commandes.isEmpty()) {
            Label noCommandesLabel = new Label("Aucune commande trouvée pour la date " + selectedDate);
            noCommandesLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
            commandesContainer.getChildren().add(noCommandesLabel);
            return;
        }

        // Create a card for each command
        for (Commandes_clients commande : commandes) {
            HBox card = createCommandeCard(commande);
            commandesContainer.getChildren().add(card);
        }
    }

    private List<Commandes_clients> getCommandesByDate(LocalDate date) {
        List<Commandes_clients> commandes = new ArrayList<>();
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
             java.io.PrintStream ps = new java.io.PrintStream(baos)) {
            java.io.PrintStream old = System.out;
            System.setOut(ps);

            commandeServices.afficherCommandesParDate(date);

            System.out.flush();
            System.setOut(old);

            String output = baos.toString();
            if (output.contains("Aucune commande trouvée")) {
                return commandes;
            }

            // Parse output to create Commandes_clients objects
            String[] lines = output.split("\n");
            for (String line : lines) {
                if (line.startsWith("Commandes_clients{")) {
                    try {
                        Commandes_clients cmd = parseCommande(line);
                        if (cmd != null) {
                            commandes.add(cmd);
                        }
                    } catch (Exception e) {
                        System.err.println("Erreur de parsing: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des commandes: " + e.getMessage());
        }
        return commandes;
    }

    private Commandes_clients parseCommande(String line) {
        // Example: Commandes_clients{id_commande=1, id_produit=101, quantite=5, prix=99.99, date_commande=2025-05-07, etat_commande='En attente', id_client=1}
        try {
            String[] parts = line.replace("Commandes_clients{", "").replace("}", "").split(", ");
            int idCommande = Integer.parseInt(parts[0].split("=")[1]);
            int idProduit = Integer.parseInt(parts[1].split("=")[1]);
            int quantite = Integer.parseInt(parts[2].split("=")[1]);
            float prix = Float.parseFloat(parts[3].split("=")[1]);
            LocalDate dateCommande = LocalDate.parse(parts[4].split("=")[1]);
            String etatCommande = parts[5].split("=")[1].replace("'", "");
            int idClient = Integer.parseInt(parts[6].split("=")[1]);

            return new Commandes_clients(idCommande, idProduit, quantite, prix, dateCommande, etatCommande, idClient);
        } catch (Exception e) {
            return null;
        }
    }

    private HBox createCommandeCard(Commandes_clients commande) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        card.setPrefWidth(600);

        VBox details = new VBox(5);
        details.getChildren().addAll(
                new Label("ID Commande: " + commande.getId_commande()),
                new Label("ID Produit: " + commande.getId_produit()),
                new Label("Quantité: " + commande.getQuantite()),
                new Label("Prix: " + commande.getPrix()),
                new Label("Date Commande: " + commande.getDate_commande()),
                new Label("État: " + commande.getEtat_commande()),
                new Label("ID Client: " + commande.getId_client())
        );

        Button deleteBtn = new Button("Supprimer");
        deleteBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        deleteBtn.setOnAction(event -> {
            boolean success = commandeServices.delete(commande);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Commande supprimée avec succès");
                refreshCommandes();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression de la commande");
            }
        });

        card.getChildren().addAll(details, deleteBtn);
        return card;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}