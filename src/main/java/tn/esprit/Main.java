package tn.esprit;
import tn.esprit.models.stock;
import tn.esprit.services.stockservice;

import java.util.List;
public class Main {
    public static void main(String[] args) {
            // Initialiser le service
            stockservice stockService = new stockservice();

            // Ajouter un stock
            stock nouveauStock = new stock(0, "Produit A", "Catégorie 1", "Disponible", 99.99f, 10);
            boolean ajoutOK = stockService.add(nouveauStock);
            System.out.println("Ajout effectué ? " + ajoutOK);

            // Afficher tous les stocks
            List<stock> liste = stockService.getAll();
            System.out.println("Liste des produits :");
            for (stock s : liste) {
                System.out.println(s);
            }

            // Mettre à jour un produit (par exemple, le premier)
            if (!liste.isEmpty()) {
                stock s = liste.get(0);
                s.setPrix(149.99f);
                s.setQuantite(20);
                boolean updateOK = stockService.update(s);
                System.out.println("Mise à jour ? " + updateOK);
            }

            // Supprimer un produit (par exemple, le dernier)
           /* if (!liste.isEmpty()) {
                stock s = liste.get(liste.size() - 1);
                boolean deleteOK = stockService.delete(s);
                System.out.println("Suppression ? " + deleteOK);
            }*/
        stock s = stockService.getById(3);
        if (s != null) {
            System.out.println("Produit trouvé : " + s.getNom_prod());
        } else {
            System.out.println("Aucun produit avec cet ID.");
        }


    }
}
