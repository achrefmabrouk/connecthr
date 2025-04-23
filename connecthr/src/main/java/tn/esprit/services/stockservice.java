package tn.esprit.services;
import tn.esprit.interfaces.Icrud;
import tn.esprit.models.stock;
import tn.esprit.utils.connecthrDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class stockservice implements Icrud<stock> {
    private Connection cnx  ;
    public stockservice(){ cnx = connecthrDB.getInstance().getCnx();
    }

    @Override
    public boolean add(stock s) {
        String qry = "INSERT INTO stock (nom_prod, categorie, statut, prix, quantite) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, s.getNom_prod());
            pstm.setString(2, s.getCategorie());
            pstm.setString(3, s.getStatut());
            pstm.setFloat(4, s.getPrix());
            pstm.setFloat(5, s.getQuantite());

            pstm.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout : " + ex.getMessage());
            return false;
        }
    }


    @Override
    public List<stock> getAll() {
        List<stock> stocks = new ArrayList<>();
        String qry = "SELECT * FROM stock";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                stock s = new stock();
                s.setId_prod(rs.getInt("id_prod"));
                s.setNom_prod(rs.getString("nom_prod"));
                s.setCategorie(rs.getString("categorie"));
                s.setStatut(rs.getString("statut"));
                s.setPrix(rs.getFloat("prix"));
                s.setQuantite(rs.getFloat("quantite"));

                stocks.add(s);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des stocks : " + ex.getMessage());
        }

        return stocks;
    }


    @Override
    public boolean update(stock s) {
        String qry = "UPDATE stock SET nom_prod=?, categorie=?, statut=?, prix=?, quantite=? WHERE id_prod=?";

        try (PreparedStatement pstm1 = cnx.prepareStatement(qry)) {
            pstm1.setString(1, s.getNom_prod());
            pstm1.setString(2, s.getCategorie());
            pstm1.setString(3, s.getStatut());
            pstm1.setFloat(4, s.getPrix());
            pstm1.setFloat(5, s.getQuantite());
            pstm1.setInt(6, s.getId_prod()); // N'oublie pas d'ajouter ça !

            int rowsAffected = pstm1.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la mise à jour : " + ex.getMessage());
            return false;
        }
    }


    @Override
    public boolean delete(stock s) {
        String qry = "DELETE FROM stock WHERE id_prod=?";

        try (PreparedStatement pstm2 = cnx.prepareStatement(qry)) {
            pstm2.setInt(1, s.getId_prod());
            pstm2.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression : " + ex.getMessage());
            return false;
        }
    }



}
