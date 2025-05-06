package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import tn.esprit.models.Stock;
import tn.esprit.models.Employe;

public class StockController {

    @FXML private HBox cardStock;
    @FXML private Label nomProduit;
    @FXML private Label categorieProduit;
    @FXML private Label statutProduit;
    @FXML private Label prixProduit;
    @FXML private Label quantiteProduit;

    @FXML private ImageView imageProduit;

    private Employe employeConnecte;

    public void setEmployeConnecte(Employe employeConnecte) {
        this.employeConnecte = employeConnecte;
    }

    public void setData(Stock s) {


        nomProduit.setText(s.getNom_prod());
        categorieProduit.setText("Catégorie : " + s.getCategorie());
        statutProduit.setText("Statut : " + s.getStatut());
        prixProduit.setText("Prix : " + s.getPrix() + " DT");
        quantiteProduit.setText("Quantité : " + s.getQuantite());
    }
}
