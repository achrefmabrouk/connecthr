package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Stock;
import tn.esprit.models.employe;
import tn.esprit.services.StockService;
import tn.esprit.services.employeservice;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Interfaceadmin implements Initializable {

    @FXML
    private FlowPane cardlayouuts; // Utilisé pour afficher les employés ou stocks (remplacé par mainContent pour les stocks)

    private List<employe> employeList;
    private List<Stock> stockList;

    @FXML
    private VBox mainContent; // Utilisé pour afficher les stocks ou employés



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




    @FXML
    void afficherEmployes(ActionEvent event) {

        cardlayouuts.getChildren().clear(); // Vide l'affichage actuel dans mainContent
        employeList = listemploye(); // Récupère la liste des employés

        for (employe emp : employeList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cards.fxml"));
                HBox cardBox = fxmlLoader.load();
                cardcontroller controller = fxmlLoader.getController();
                controller.setData(emp); // Affecte les données de l'employé à la carte
                cardlayouuts.getChildren().add(cardBox);
            } catch (IOException e) {
                System.out.println("Erreur lors du chargement des cartes employé : " + e.getMessage());
            }
        }

    }

    // Méthode pour afficher les stocks
    @FXML
    void affichestock(ActionEvent event) {
        cardlayouuts.getChildren().clear(); // Vider le VBox principal (mainContent) pour afficher les stocks

        stockList = listStock(); // Récupère la liste des stocks

        FlowPane stockContainer = new FlowPane(); // Crée un conteneur temporaire pour les cartes de stock
        stockContainer.setHgap(10); // Espacement horizontal
        stockContainer.setVgap(10); // Espacement vertical

        for (Stock stock : stockList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cardstock.fxml"));
                HBox cardBox = fxmlLoader.load();

                // Utilisation du bon contrôleur pour afficher les stocks
                StockController controller = fxmlLoader.getController();
                controller.setData(stock); // Affecte les données du stock à la carte

                stockContainer.getChildren().add(cardBox); // Ajoute la carte au conteneur
            } catch (IOException e) {
                System.out.println("Erreur lors du chargement des cartes stock : " + e.getMessage());
            }
        }

        cardlayouuts.getChildren().add(stockContainer); // Affiche les cartes de stock dans le VBox principal (mainContent)
    }

    // Méthode pour récupérer la liste des employés
    private List<employe> listemploye() {
        employeservice employeservice = new employeservice("responsable", "IT");
        return employeservice.getAll(); // Récupère les employés de la BD
    }

    // Méthode pour récupérer la liste des stocks
    private List<Stock> listStock() {
        StockService stockService = new StockService("admin");
        return stockService.getAll(); // Récupère les stocks de la BD
    }
}
