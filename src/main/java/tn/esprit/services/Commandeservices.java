package tn.esprit.services;

import tn.esprit.interfaces.Icrud;
import tn.esprit.models.Clients;
import tn.esprit.models.Commandes_clients;
import tn.esprit.utils.connecthrDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Commandeservices implements Icrud<Commandes_clients> {
    private Connection cnx;

    public Commandeservices() {
        cnx = connecthrDB.getInstance().getCnx();
    }


    @Override
    public boolean add(Commandes_clients cmd) {
        String ClientQuery = "SELECT COUNT(*) FROM  WHERE id_client = ?";

        try (PreparedStatement checkClientStmt = cnx.prepareStatement(ClientQuery)) {
            checkClientStmt.setInt(1, cmd.getId_client());

            try (ResultSet rs = checkClientStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {

                    String qryCommande = "INSERT INTO commandes_clients (id_produit, quantite, prix, date_commande, etat_commande, id_client) VALUES (?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement pst = cnx.prepareStatement(qryCommande, Statement.RETURN_GENERATED_KEYS)) {
                        pst.setInt(1, cmd.getId_produit());
                        pst.setInt(2, cmd.getQuantite());
                        pst.setFloat(3, cmd.getPrix());
                        pst.setDate(4, java.sql.Date.valueOf(cmd.getDate_commande()));
                        pst.setString(5, cmd.getEtat_commande());
                        pst.setInt(6, cmd.getId_client());

                        int affectedRows = pst.executeUpdate();
                        if (affectedRows > 0) {
                            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    cmd.setId_commande(generatedKeys.getInt(1));
                                }
                            }
                            return true;
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Erreur : le client avec l'ID " + cmd.getId_client() + " n'existe pas.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    @Override
    public List<Commandes_clients> getAll() {
        List<Commandes_clients> list = new ArrayList<>();
        String qry = "SELECT * FROM commandes_clients";

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(qry);

            while (rs.next()) {
                Commandes_clients cmd = new Commandes_clients(
                        rs.getInt("id_commande"),
                        rs.getInt("id_produit"),
                        rs.getInt("quantite"),
                        rs.getFloat("prix"),
                        rs.getDate("date_commande").toLocalDate(),
                        rs.getString("etat_commande"),
                        rs.getInt("id_facture"),
                        rs.getInt("id_client")
                );
                list.add(cmd);
            }

        } catch (SQLException e) {
            System.out.println( e.getMessage());
        }

        return list;
    }

    @Override
    public boolean update(Commandes_clients cmd) {
        String qry = "UPDATE commandes_clients SET id_produit = ?, quantite = ?, prix = ?, date_commande = ? WHERE id_client = ?";
        try (PreparedStatement pst = cnx.prepareStatement(qry)) {
            pst.setInt(1, cmd.getId_produit());
            pst.setInt(2, cmd.getQuantite());
            pst.setFloat(3, cmd.getPrix());
            pst.setDate(4, java.sql.Date.valueOf(cmd.getDate_commande()));
            pst.setString(5,cmd.getEtat_commande());
            pst.setInt(6, cmd.getId_facture());
            pst.setInt(7, cmd.getId_client());
            pst.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Commandes_clients cmd) {
        String qry = "DELETE FROM commandes_clients WHERE id_client = ? ";
        try (PreparedStatement pst = cnx.prepareStatement(qry)) {
            pst.setInt(1, cmd.getId_client());

            pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public Commandes_clients getCommandeParIdFact(int id_facture) {
        Commandes_clients cmd = null;
        String qry = "SELECT * FROM commandes_clients WHERE id_facture = ?";

        try (PreparedStatement pst = cnx.prepareStatement(qry)) {
            pst.setInt(1, id_facture);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cmd = new Commandes_clients(
                            rs.getInt("id_commande"),
                            rs.getInt("id_produit"),
                            rs.getInt("quantite"),
                            rs.getFloat("prix"),
                            rs.getDate("date_commande").toLocalDate(),
                            rs.getString("etat_commande"),
                            rs.getInt("id_facture"),
                            rs.getInt("id_client"));}
            }}
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return cmd;
    }

    /*public void afficherProduits(int id_facture) {
        String qry = "SELECT p.id_produit, p.nom_produit, p.prix_produit, p.description " +
                "FROM produits p " +
                "JOIN commandes_clients c ON p.id_produit = c.id_produit " +
                "WHERE c.id_commande = ?";

        try (PreparedStatement pst = cnx.prepareStatement(qry)) {
            pst.setInt(1, id_commande);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int idProduit = rs.getInt("id_produit");
                    String nomProduit = rs.getString("nom_produit");
                    float prixProduit = rs.getFloat("prix_produit");
                    String description = rs.getString("description");

                    System.out.println("Produit ID: " + idProduit);
                    System.out.println("Nom: " + nomProduit);
                    System.out.println("Prix: " + prixProduit);
                    System.out.println("Description: " + description);
                    System.out.println("---------------------------");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }*/

    public List<Commandes_clients> afficherCommandesParDate(LocalDate date) {
        List<Commandes_clients> commandes = new ArrayList<>();
        String qry = "SELECT * FROM commandes_clients WHERE date_commande = ?";

        try (PreparedStatement pst = cnx.prepareStatement(qry)) {
            pst.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Commandes_clients cmd = new Commandes_clients();
                    cmd.setId_commande(rs.getInt("id_commande"));
                    cmd.setId_produit(rs.getInt("id_produit"));
                    cmd.setQuantite(rs.getInt("quantite"));
                    cmd.setPrix(rs.getFloat("prix"));
                    cmd.setDate_commande(rs.getDate("date_commande").toLocalDate());
                    cmd.setEtat_commande(rs.getString("etat_commande"));
                    cmd.setId_client(rs.getInt("id_client"));
                    commandes.add(cmd);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving orders: " + ex.getMessage());
        }
        return commandes;
    }


}