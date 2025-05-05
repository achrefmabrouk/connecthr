package tn.esprit;

import tn.esprit.models.Stock;
import tn.esprit.services.StockService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Simulation d'un utilisateur avec grade et département
        String grade = "responsable";  // exemple : "admin", "responsable", ou autre
        String departement = "stock"; // exemple : "stock" ou autre département

        // Création de l'instance du service Stock
        StockService stockService = new StockService(grade, departement);

        // Création d'un nouvel objet Stock
        Stock nouveauStock = new Stock();
        nouveauStock.setNom_prod("Ordinateur Portable");
        nouveauStock.setCategorie("Informatique");
        nouveauStock.setStatut("Disponible");
        nouveauStock.setPrix(899.99f);
        nouveauStock.setQuantite(50);

        // Tentative d'ajout du stock
        boolean ajoutReussi = stockService.add(nouveauStock);
        if (ajoutReussi) {
            System.out.println("Stock ajouté avec succès.");
        } else {
            System.out.println("Échec de l'ajout du stock.");
        }

        // Tentative de mise à jour du stock
        Stock stockExist = stockService.getAll().stream()
                .filter(s -> s.getNom_prod().equals("Ordinateur Portable"))
                .findFirst().orElse(null);

        if (stockExist != null) {
            stockExist.setPrix(849.99f);  // Mise à jour du prix
            boolean updateReussi = stockService.update(stockExist);
            if (updateReussi) {
                System.out.println("Stock mis à jour avec succès.");
            } else {
                System.out.println("Échec de la mise à jour du stock.");
            }
        } else {
            System.out.println("Le stock à mettre à jour n'existe pas.");
        }

        // Tentative de suppression du stock
        if (stockExist != null) {
            boolean suppressionReussi = stockService.delete(stockExist);
            if (suppressionReussi) {
                System.out.println("Stock supprimé avec succès.");
            } else {
                System.out.println("Échec de la suppression du stock.");
            }
        } else {
            System.out.println("Le stock à supprimer n'existe pas.");
        }

        // Récupération et affichage de tous les stocks
        List<Stock> stocks = stockService.getAll();
        if (stocks.isEmpty()) {
            System.out.println("Aucun stock trouvé.");
        } else {
            System.out.println("Liste des stocks :");
            for (Stock s : stocks) {
                System.out.println("ID: " + s.getId_prod() + ", Nom: " + s.getNom_prod() +
                        ", Catégorie: " + s.getCategorie() + ", Prix: " + s.getPrix() + ", Quantité: " + s.getQuantite());
            }
        }
    }
}
