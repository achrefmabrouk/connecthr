package tn.esprit;

import tn.esprit.models.Clients;
import tn.esprit.models.Commandes_clients;
import tn.esprit.services.Clientservices;
import tn.esprit.services.Commandeservices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Clientservices clientService = new Clientservices();
        Commandeservices commandeService = new Commandeservices();

        // 1. Création d'un nouveau client
        Clients newClient = new Clients("Amira", "Ch", 11111111);
        Clients newClient1 = new Clients("Amira", "Ch", 22222222);
        boolean added = clientService.add(newClient);

        if (added) {
            System.out.println("Client ajouté avec succès !");
        } else {
            System.out.println("Erreur lors de l'ajout du client.");
            return;
        }

        // 2. Récupération de l'ID du client ajouté
        Clients savedClient = clientService.chercherClientParTelephone(11111111); // méthode à implémenter si elle n'existe pas
        if (savedClient == null) {
            System.out.println("Client non trouvé après l'ajout !");
            return;
        }

        int clientId = savedClient.getId_client();

        // 3. Création des commandes pour ce client
        List<Commandes_clients> commandes = new ArrayList<>();
        Commandes_clients commande1 = new Commandes_clients(1, 2, 10.2f, LocalDate.of(2025, 5, 2), "En attente", clientId);
        Commandes_clients commande2 = new Commandes_clients(2, 1, 5.5f, LocalDate.of(2025, 5, 2), "En attente", clientId);

        commandes.add(commande1);
        commandes.add(commande2);

        for (Commandes_clients cmd : commandes) {
            commandeService.add(cmd);
        }

        // 4. Affichage des clients
        System.out.println("\nListe des clients + commandes :");
        for (Clients c : clientService.getAll()) {
            System.out.println(c);
        }

        // 5. Affichage des commandes
        System.out.println("\nListe des commandes :");
        for (Commandes_clients cmd : commandeService.getAll()) {
            System.out.println(cmd);
        }

        // 6. Mise à jour d’un client
        Clients clientToUpdate = clientService.chercherClient(clientId);
        if (clientToUpdate != null) {
            clientToUpdate.setTelephone_client(12345678);
            System.out.println("\nClient mis à jour ? " + clientService.update(clientToUpdate));
        }

        // 7. Suppression du client
        Clients clientToDelete = clientService.chercherClient(clientId);
        if (clientToDelete != null) {
            System.out.println("Client supprimé ? " + clientService.delete(clientToDelete));
        }

        // 8. Affichage des commandes pour une date donnée
        LocalDate dateToSearch = LocalDate.of(2025, 5, 2);
        System.out.println("\nCommandes pour la date " + dateToSearch + " :");
        commandeService.afficherCommandesParDate(dateToSearch);

        // 9. Recherche par nom et prénom
        System.out.println("\n🔍 Recherche par nom et prénom :");
        Clients searchedClient = clientService.chercherClientParNomEtPrenom("Amira", "Ch");
        if (searchedClient != null) {
            System.out.println(searchedClient);
        } else {
            System.out.println("Aucun client trouvé avec ce nom et prénom.");
        }

        // 10. Affichage du client et de ses commandes par téléphone
        System.out.println("\n🔍 Recherche par téléphone :");
        clientService.afficherClientEtCommandesParTelephone(11111111);  // Recherche avec le téléphone

        // 11. Affichage du client et de ses commandes par nom et prénom
        System.out.println("\n🔍 Recherche par nom et prénom :");
        clientService.afficherClientEtCommandesParNomEtPrenom("Amira", "Ch");  // Recherche avec nom et prénom
    }
}