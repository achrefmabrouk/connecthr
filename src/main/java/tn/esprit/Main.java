/*package tn.esprit;

import tn.esprit.models.Clients;
import tn.esprit.models.Commandes_clients;
import tn.esprit.services.Clientservices;
import tn.esprit.services.Commandeservices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        Commandeservices service = new Commandeservices();

        // Créer une commande avec des données fictives
        Commandes_clients nouvelleCommande = new Commandes_clients(
                1,              // id_produit
                5,              // quantite
                99.99f,         // prix
                LocalDate.now(), // date_commande
                "En cours",     // etat_commande
                5               // id_client
        );

        // Appeler la méthode d'ajout
        boolean ajoutReussi = service.add(nouvelleCommande);

        // Afficher le résultat
        if (ajoutReussi) {
            System.out.println("Commande ajoutée avec succès !");
            System.out.println("Détails : " + nouvelleCommande);
        } else {
            System.out.println("Échec de l'ajout de la commande.");
        }
    }

}*/