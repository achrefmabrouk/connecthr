package tn.esprit.services;
import tn.esprit.interfaces.Icrud;
import tn.esprit.models.facture;
import tn.esprit.utils.connecthrDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class factureservice implements Icrud <facture> {
    private Connection cnx;

    public factureservice() {
        cnx = connecthrDB.getInstance().getCnx();
    }

    @Override
    public boolean add(facture f) {
        String qry = "INSERT INTO facture  (id_facture, date_emission,date_echeance,cout_unitaire, remise ,mode_paiement,statut) VALUES (?, ?, ?, ?, ?,?,?)";


        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1,f.getId_facture());
            pstm.setDate(2, java.sql.Date.valueOf(f.getDate_emission()));
            pstm.setDate(3, java.sql.Date.valueOf(f.getDate_echeance()));
            pstm.setFloat(4,f.getCout_unitaire());
            pstm.setFloat(5,f.getRemise());
            pstm.setString(6,f.getMode_paiement());
            pstm.setString(7,f.getStatut());

            pstm.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public List<facture> getAll() {
        List<facture>  factures = new ArrayList<>();
        String qry = "SELECT * FROM facture";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                facture f = new facture();
                f.setId_facture(rs.getInt("id_facture"));
                f.setDate_emission(rs.getDate("date_emission").toLocalDate());
                f.setDate_echeance(rs.getDate("date_echeance").toLocalDate());
                f.setCout_unitaire(rs.getFloat("cout_unitaire"));
                f.setRemise(rs.getFloat("Remise"));
                f.setMode_paiement(rs.getString("mode_paiement"));
                f.setStatut(rs.getString("Statut"));

                factures.add(f);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des factures : " + ex.getMessage());
        }

        return factures ;
    }


    @Override
    public boolean update(facture f) {
        String qry = "UPDATE facture SET  date_emission=?, date_echeance=?, cout_unitaire=?, remise=? , mode_paiement=? , statut=? WHERE id_facture=?";

        try (PreparedStatement pstm1 = cnx.prepareStatement(qry)) {

            pstm1.setDate(1, java.sql.Date.valueOf(f.getDate_emission()));
            pstm1.setDate(2, java.sql.Date.valueOf(f.getDate_echeance()));
            pstm1.setFloat(3,f.getCout_unitaire());
            pstm1.setFloat(4,f.getRemise());
            pstm1.setString(5,f.getMode_paiement());
            pstm1.setString(6,f.getStatut());
            pstm1.setInt(7,f.getId_facture());



            int rowsAffected = pstm1.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la mise à jour : " + ex.getMessage());
            return false;
        }
    }


    @Override
    public boolean delete(facture f) {
        String qry = "DELETE FROM facture WHERE id_facture=?";

        try (PreparedStatement pstm2 = cnx.prepareStatement(qry)) {
            pstm2.setInt(1, f.getId_facture());
            pstm2.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression : " + ex.getMessage());
            return false;
        }
    }



}
