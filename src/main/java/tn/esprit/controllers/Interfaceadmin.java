package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Stock;
import tn.esprit.models.Employe;
import tn.esprit.services.StockService;
import tn.esprit.services.employeservice;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Interfaceadmin implements Initializable {

    @FXML
    private FlowPane cardlayouuts;

    private List<Employe> employeList;
    private List<Stock> stockList;

    @FXML
    private VBox mainContent;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




    @FXML
    void afficherEmployes(ActionEvent event) {

        cardlayouuts.getChildren().clear();
        employeList = listemploye();

        for (Employe emp : employeList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cards.fxml"));
                HBox cardBox = fxmlLoader.load();
                cardcontroller controller = fxmlLoader.getController();
                controller.setData(emp);
                cardlayouuts.getChildren().add(cardBox);
            } catch (IOException e) {
                System.out.println("Erreur lors du chargement des cartes employé : " + e.getMessage());
            }
        }

    }

    @FXML
    void affichestock(ActionEvent event) {
        cardlayouuts.getChildren().clear();

        stockList = listStock();

        FlowPane stockContainer = new FlowPane();
        stockContainer.setHgap(10);
        stockContainer.setVgap(10);

        for (Stock stock : stockList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cardstock.fxml"));
                HBox cardBox = fxmlLoader.load();


                StockController controller = fxmlLoader.getController();
                controller.setData(stock);

                stockContainer.getChildren().add(cardBox);
            } catch (IOException e) {
                System.out.println("Erreur lors du chargement des cartes stock : " + e.getMessage());
            }
        }

        cardlayouuts.getChildren().add(stockContainer);
    }


    private List<Employe> listemploye() {
        employeservice employeservice = new employeservice("responsable", "IT");
        return employeservice.getAll();
    }


    private List<Stock> listStock() {
        StockService stockService = new StockService("admin");
        return stockService.getAll();
    }
}
