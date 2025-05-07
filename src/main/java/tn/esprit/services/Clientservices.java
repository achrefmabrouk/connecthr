package tn.esprit.services;

import tn.esprit.interfaces.Icrud;
import tn.esprit.models.Clients;
import tn.esprit.models.Commandes_clients;
import tn.esprit.utils.connecthrDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Clientservices implements Icrud<Clients> {

    private final Connection cnx  ;
    public Clientservices() {
        cnx = connecthrDB.getInstance().getCnx();
    }
    public Clients chercherClient(int id) {
        String sql = "SELECT * FROM clients WHERE id_client = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Clients client = new Clients();
                client.setId_client(rs.getInt("id_client"));
                client.setNom_client(rs.getString("nom_client"));
                client.setPrenom_client(rs.getString("prenom_client"));
                client.setTelephone_client(rs.getInt("telephone_client"));
                return client;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public boolean add(Clients client) {
        String insertClientSQL = "INSERT INTO clients (nom_client, prenom_client, telephone_client) VALUES (?, ?, ?)";
        String insertCommandeSQL = "INSERT INTO commandes_clients (id_produit, quantite, prix, date_commande, id_client, etat_commande) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(insertClientSQL, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, client.getNom_client());
            pst.setString(2, client.getPrenom_client());
            pst.setInt(3, client.getTelephone_client());

            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("❌ Échec lors de l'insertion du client.");
                return false;
            }

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId_client(generatedKeys.getInt(1)); // Associer l'ID généré au client
                    System.out.println("✅ Client ajouté avec succès !");
                } else {
                    System.out.println("❌ Impossible de récupérer l'ID du client.");
                    return false;
                }
            }

            // Sécuriser la liste des commandes même si elle est null
            List<Commandes_clients> commandes = client.getCommandes();
            if (commandes == null) {
                commandes = new ArrayList<>();
            }

            // Ajouter chaque commande associée
            for (Commandes_clients cmd : commandes) {
                cmd.setId_client(client.getId_client()); // Associer le client à chaque commande

                try (PreparedStatement pstCmd = cnx.prepareStatement(insertCommandeSQL)) {
                    pstCmd.setInt(1, cmd.getId_produit());
                    pstCmd.setInt(2, cmd.getQuantite());
                    pstCmd.setFloat(3, cmd.getPrix());
                    pstCmd.setDate(4, java.sql.Date.valueOf(cmd.getDate_commande())); // Assurez-vous que la date est correcte
                    pstCmd.setInt(5, cmd.getId_client()); // Insérer l'ID client dans la commande
                    pstCmd.setString(6, cmd.getEtat_commande()); // Ex: "En attente" ou "Confirmé"

                    pstCmd.executeUpdate();
                }
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du client : " + e.getMessage());
            return false;
        }}


    public void afficherClientEtCommandesParTelephone(int telephone) {
        String clientSql = "SELECT * FROM clients WHERE telephone_client = ?";
        String commandesSql = "SELECT * FROM commandes_clients WHERE id_client = ?";

        try (PreparedStatement psClient = cnx.prepareStatement(clientSql)) {
            // Recherche du client par téléphone
            psClient.setInt(1, telephone);
            ResultSet rsClient = psClient.executeQuery();

            if (rsClient.next()) {
                // Création du client
                Clients client = new Clients();
                client.setId_client(rsClient.getInt("id_client"));
                client.setNom_client(rsClient.getString("nom_client"));
                client.setPrenom_client(rsClient.getString("prenom_client"));
                client.setTelephone_client(rsClient.getInt("telephone_client"));

                System.out.println("Client : " + client);

                // Recherche des commandes associées
                try (PreparedStatement psCommandes = cnx.prepareStatement(commandesSql)) {
                    psCommandes.setInt(1, client.getId_client());
                    ResultSet rsCommandes = psCommandes.executeQuery();

                    // Affichage des commandes du client
                    while (rsCommandes.next()) {
                        Commandes_clients commande = new Commandes_clients();
                        commande.setId_commande(rsCommandes.getInt("id_commande"));
                        commande.setId_produit(rsCommandes.getInt("id_produit"));
                        commande.setQuantite(rsCommandes.getInt("quantite"));
                        commande.setPrix(rsCommandes.getFloat("prix"));
                        commande.setDate_commande(rsCommandes.getDate("date_commande").toLocalDate());
                        commande.setEtat_commande(rsCommandes.getString("etat_commande"));
                        commande.setId_client(rsCommandes.getInt("id_client"));

                        System.out.println("Commande : " + commande);
                    }
                }
            } else {
                System.out.println("❌ Client non trouvé avec ce téléphone.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage du client et de ses commandes par téléphone : " + e.getMessage());
        }
    }

    public void afficherClientEtCommandesParNomEtPrenom(String nom, String prenom) {
        String clientSql = "SELECT * FROM clients WHERE nom_client = ? AND prenom_client = ?";
        String commandesSql = "SELECT * FROM commandes_clients WHERE id_client = ?";

        try (PreparedStatement psClient = cnx.prepareStatement(clientSql)) {
            // Recherche du client par nom et prénom
            psClient.setString(1, nom);
            psClient.setString(2, prenom);
            ResultSet rsClient = psClient.executeQuery();

            if (rsClient.next()) {
                // Création du client
                Clients client = new Clients();
                client.setId_client(rsClient.getInt("id_client"));
                client.setNom_client(rsClient.getString("nom_client"));
                client.setPrenom_client(rsClient.getString("prenom_client"));
                client.setTelephone_client(rsClient.getInt("telephone_client"));

                System.out.println("Client : " + client);

                // Recherche des commandes associées
                try (PreparedStatement psCommandes = cnx.prepareStatement(commandesSql)) {
                    psCommandes.setInt(1, client.getId_client());
                    ResultSet rsCommandes = psCommandes.executeQuery();

                    // Affichage des commandes du client
                    while (rsCommandes.next()) {
                        Commandes_clients commande = new Commandes_clients();
                        commande.setId_commande(rsCommandes.getInt("id_commande"));
                        commande.setId_produit(rsCommandes.getInt("id_produit"));
                        commande.setQuantite(rsCommandes.getInt("quantite"));
                        commande.setPrix(rsCommandes.getFloat("prix"));
                        commande.setDate_commande(rsCommandes.getDate("date_commande").toLocalDate());
                        commande.setEtat_commande(rsCommandes.getString("etat_commande"));
                        commande.setId_client(rsCommandes.getInt("id_client"));

                        System.out.println("Commande : " + commande);
                    }
                }
            } else {
                System.out.println("❌ Client non trouvé avec ce nom et prénom.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage du client et de ses commandes par nom et prénom : " + e.getMessage());
        }
    }







    @Override
    public List<Clients> getAll() {
        List<Clients> clients = new ArrayList<>();
        String qryClients = "SELECT * FROM clients";

        try {
            Statement stmClients = cnx.createStatement();
            ResultSet rsClients = stmClients.executeQuery(qryClients);

            while (rsClients.next()) {
                Clients c = new Clients();
                c.setId_client(rsClients.getInt("id_client"));
                c.setNom_client(rsClients.getString("nom_client"));
                c.setPrenom_client(rsClients.getString("prenom_client"));
                c.setTelephone_client(rsClients.getInt("telephone_client"));

                // Ajout du client à la liste
                clients.add(c);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur dans getAll() : " + ex.getMessage());
        }

        return clients;
    }

    @Override
    public boolean update(Clients c) {
        String qry ="UPDATE clients SET `nom_client`=?,`prenom_client`=?,`telephone_client`=? where `id_client`=?";
        try {

            PreparedStatement pstm1 = cnx.prepareStatement(qry);
            pstm1.setInt(1, c.getId_client());
            pstm1.setString(2,c.getNom_client());
            pstm1.setString(3,c.getPrenom_client());
            pstm1.setInt(4,c.getTelephone_client());

            pstm1.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }


    public boolean delete(Clients c) {
        try {
            String deleteCmds = "DELETE FROM commandes_clients WHERE id_client = ?";
            PreparedStatement pstm1 = cnx.prepareStatement(deleteCmds);
            pstm1.setInt(1, c.getId_client());
            pstm1.executeUpdate();

            String deleteClient = "DELETE FROM clients WHERE id_client = ?";
            PreparedStatement pstm2 = cnx.prepareStatement(deleteClient);
            pstm2.setInt(1, c.getId_client());
            int rowsAffected = pstm2.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public Clients chercherClientParTelephone(int telephone) {
        String sql = "SELECT * FROM clients WHERE telephone_client = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, telephone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Clients client = new Clients();
                client.setId_client(rs.getInt("id_client"));
                client.setNom_client(rs.getString("nom_client"));
                client.setPrenom_client(rs.getString("prenom_client"));
                client.setTelephone_client(rs.getInt("telephone_client"));
                return client;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Clients chercherClientParNomEtPrenom(String nom, String prenom) {
        String sql = "SELECT * FROM clients WHERE nom_client = ? AND prenom_client = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("✅ Compte existant");
                Clients client = new Clients();
                client.setId_client(rs.getInt("id_client"));
                client.setNom_client(rs.getString("nom_client"));
                client.setPrenom_client(rs.getString("prenom_client"));
                client.setTelephone_client(rs.getInt("telephone_client"));
                return client;
            } else {
                System.out.println("❌ Compte introuvable");
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans chercherClientParNomEtPrenom : " + e.getMessage());
        }
        return null;
    }


}