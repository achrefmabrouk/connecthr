package tn.esprit.services;
import tn.esprit.interfaces.Icrud;
import tn.esprit.models.Facture;
import tn.esprit.utils.connecthrDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public  class Factureservice implements Icrud<Facture> {
    private Connection cnx;

    public Factureservice() {
        cnx = connecthrDB.getInstance().getCnx();
    }

    @Override
    public boolean add(Facture f) {
        String qry = "INSERT INTO facture (numfac ,date_emission, date_echeance, cout_unitaire, remise, statut, tva, montant_ht, montant_ttc, id_commande) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setInt(1, f.getNumfac());
            pstm.setDate(2, java.sql.Date.valueOf(f.getDate_emission()));
            pstm.setDate(3, java.sql.Date.valueOf(f.getDate_echeance()));
            pstm.setFloat(4, f.getCout_unitaire());
            pstm.setFloat(5, f.getRemise());
            pstm.setString(6, f.getStatut());
            pstm.setFloat(7, f.getTva());
            pstm.setFloat(8, f.getMontant_ht());
            pstm.setFloat(9, f.getMontant_ttc());
            pstm.setInt(10, f.getId_commande());


            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    f.setId_facture(generatedKeys.getInt(1));
                }
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Facture> getAll() {
        List<Facture> factures = new ArrayList<>();
        String qry = "SELECT * FROM facture";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Facture f = new Facture();
                f.setNumfac(rs.getInt("numfac"));
                f.setDate_emission(rs.getDate("date_emission").toLocalDate());
                f.setDate_echeance(rs.getDate("date_echeance").toLocalDate());
                f.setCout_unitaire(rs.getFloat("cout_unitaire"));
                f.setRemise(rs.getFloat("Remise"));
                f.setStatut(rs.getString("Statut"));
                f.setTva(rs.getFloat("TVA"));
                f.setMontant_ht(rs.getFloat("montant_ht"));
                f.setMontant_ttc(rs.getFloat("montant_ttc"));
                f.setId_commande(rs.getInt("Id_commande"));

                factures.add(f);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des factures : " + ex.getMessage());
        }

        return factures;
    }


    @Override
    public boolean update(Facture f) {
        String qry = "UPDATE facture SET numfac=? , date_emission=?, date_echeance=?, cout_unitaire=?, remise=? , statut=? , tva=? , montant_ht=? , montant_ttc=? , id_commande=? WHERE id_facture=?";

        try (PreparedStatement pstm1 = cnx.prepareStatement(qry)) {
            pstm1.setInt(1, f.getNumfac());
            pstm1.setDate(1, java.sql.Date.valueOf(f.getDate_emission()));
            pstm1.setDate(2, java.sql.Date.valueOf(f.getDate_echeance()));
            pstm1.setFloat(3, f.getCout_unitaire());
            pstm1.setFloat(4, f.getRemise());
            pstm1.setString(5, f.getStatut());
            pstm1.setFloat(6, f.getTva());
            pstm1.setFloat(7, f.getMontant_ht());
            pstm1.setFloat(8, f.getMontant_ttc());
            pstm1.setInt(9, f.getId_commande());
            pstm1.setInt(10, f.getId_facture());


            int rowsAffected = pstm1.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la mise à jour : " + ex.getMessage());
            return false;
        }
    }


    @Override
    public boolean delete(Facture f) {
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

    public Facture getById(int id) {
        String qry = "SELECT * FROM facture WHERE id_facture = ?";
        Facture f = null;

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                f = new Facture();
                f.setId_facture(rs.getInt("id_facture"));
                f.setNumfac(rs.getInt("numfac"));
                f.setDate_emission(rs.getDate("date_emission").toLocalDate());
                f.setDate_echeance(rs.getDate("date_echeance").toLocalDate());
                f.setCout_unitaire(rs.getFloat("cout_unitaire"));
                f.setRemise(rs.getFloat("remise"));
                f.setStatut(rs.getString("statut"));
                f.setTva(rs.getFloat("tva"));
                f.setMontant_ht(rs.getFloat("montant_ht"));
                f.setMontant_ttc(rs.getFloat("montant_ttc"));
                f.setId_commande(rs.getInt("Id_commande"));
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération de la facture : " + ex.getMessage());
        }

        return f;
    }

    public Facture rechercheParId(int id) {
        String qry = "SELECT * FROM facture WHERE id_facture = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Facture f = new Facture();
                f.setId_facture(rs.getInt("id_facture"));
                f.setNumfac(rs.getInt("numfac"));
                f.setDate_emission(rs.getDate("date_emission").toLocalDate());
                f.setDate_echeance(rs.getDate("date_echeance").toLocalDate());
                f.setCout_unitaire(rs.getFloat("cout_unitaire"));
                f.setRemise(rs.getFloat("remise"));
                f.setStatut(rs.getString("statut"));
                f.setTva(rs.getFloat("tva"));
                f.setMontant_ht(rs.getFloat("montant_ht"));
                f.setMontant_ttc(rs.getFloat("montant_ttc"));
                f.setId_commande(rs.getInt("Id_commande"));
                return f;
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la recherche de la facture : " + ex.getMessage());
        }
        return null;
    }

    public Facture rechercheParNumfacture(int Numerofacture) {
        String qry = "SELECT * FROM facture WHERE numfac = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, Numerofacture);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Facture f = new Facture();
                f.setNumfac(rs.getInt("numfac"));
                f.setDate_emission(rs.getDate("date_emission").toLocalDate());
                f.setDate_echeance(rs.getDate("date_echeance").toLocalDate());
                f.setCout_unitaire(rs.getFloat("cout_unitaire"));
                f.setRemise(rs.getFloat("remise"));
                f.setStatut(rs.getString("statut"));
                f.setTva(rs.getFloat("tva"));
                f.setMontant_ht(rs.getFloat("montant_ht"));
                f.setMontant_ttc(rs.getFloat("montant_ttc"));
                f.setId_commande(rs.getInt("Id_commande"));
                return f;
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la recherche de la facture : " + ex.getMessage());
        }
        return null;
    }








}