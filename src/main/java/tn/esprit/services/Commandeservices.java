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
        String qryCommande = "INSERT INTO commandes_clients (id_produit, quantite, prix, date_commande, id_client) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(qryCommande, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, cmd.getId_produit());
            pst.setInt(2, cmd.getQuantite());
            pst.setFloat(3, cmd.getPrix());
            pst.setDate(4, java.sql.Date.valueOf(cmd.getDate_commande()));
            pst.setInt(5, cmd.getId_client());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cmd.setId_commande(generatedKeys.getInt(1));  // Récupérer l'ID généré
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
            pst.setInt(6, cmd.getId_client());
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

    public void afficherCommandesParDate(LocalDate date) {
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

                    System.out.println(cmd);
                }


            }

        } catch (SQLException ex) {
            System.out.println( ex.getMessage());
        }
    }


}