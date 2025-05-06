package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import tn.esprit.models.Stock;
import tn.esprit.models.Employe;

public class cardcontroller {

    @FXML private HBox cardemp;
    @FXML private ImageView imgemp;
    @FXML private Label nomemp;
    @FXML private Label gradeemp;
    @FXML private Label departementLabel;
    @FXML private Label adresseLabel;
    @FXML private Label telephoneLabel;
    @FXML private Label sexeLabel;
    @FXML private Label tarifLabel;
    @FXML private Label congesLabel;

    @FXML private Label nomStockLabel;
    @FXML private Label categorieStockLabel;
    @FXML private Label statutStockLabel;
    @FXML private Label prixStockLabel;
    @FXML private Label quantiteStockLabel;


    public void setData(Employe e) {
        try {
            String imageUrl = e.getImageSrc();

            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                Image image = new Image(imageUrl, true);
                imgemp.setImage(image);
            } else {
                System.err.println("URL d'image vide pour l'employé : " + e.getNom());
            }
        } catch (Exception ex) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getImageSrc());
            System.err.println(ex.getMessage());
        }


        nomemp.setText(e.getNom() + " " + e.getPrenom());
        gradeemp.setText(e.getGrade() + " - " + e.getPoste());

        departementLabel.setText(e.getDepartement() + " - " + e.getNiveau());
        adresseLabel.setText(e.getAdresse());
        telephoneLabel.setText("Téléphone : " + e.getTelephone());
        sexeLabel.setText("Sexe : " + e.getSexe());
        tarifLabel.setText("Tarif journalier : " + e.getTarifJournalier() + " DT");
        congesLabel.setText("Congés restants : " + e.getJoursCongesRestants());
    }


    public void setData(Stock stock) {

        nomStockLabel.setText(stock.getNom_prod());
        categorieStockLabel.setText("Catégorie : " + stock.getCategorie());
        statutStockLabel.setText("Statut : " + stock.getStatut());
        prixStockLabel.setText("Prix : " + stock.getPrix() + " DT");
        quantiteStockLabel.setText("Quantité : " + stock.getQuantite());
    }
}
