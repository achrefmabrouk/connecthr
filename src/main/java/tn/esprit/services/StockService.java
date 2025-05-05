package tn.esprit.services;

import tn.esprit.interfaces.Icrud;
import tn.esprit.models.Stock;
import tn.esprit.utils.connecthrDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockService implements Icrud<Stock> {
    private Connection cnx;
    private String grade_emp;
    private String depart_emp;

    // Constructeur sans paramètres
    public StockService() {
        cnx = connecthrDB.getInstance().getCnx();
    }

    public StockService(String grade_emp) {
        cnx = connecthrDB.getInstance().getCnx();
        this.grade_emp = grade_emp;
    }

    // Constructeur avec grade et département
    public StockService(String grade_emp, String depart_emp) {
        cnx = connecthrDB.getInstance().getCnx();
        this.grade_emp = grade_emp;
        this.depart_emp = depart_emp;
    }

    // Vérification des droits d'accès
    private boolean aLesDroitsStock() {
        return grade_emp.equals("admin") ||
                (grade_emp.equals("responsable") && depart_emp.equals("stock"));
    }

    @Override
    public boolean add(Stock s) {
        if (!aLesDroitsStock()) {
            System.out.println("Action non autorisée : seul un admin ou un responsable du stock peut ajouter.");
            return false;
        }

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
            System.out.println("Erreur SQL : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Stock s) {
        if (!aLesDroitsStock()) {
            System.out.println("Action non autorisée : seuls un admin ou un responsable du stock peuvent modifier.");
            return false;
        }

        String qry = "UPDATE stock SET nom_prod=?, categorie=?, statut=?, prix=?, quantite=? WHERE id_prod=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, s.getNom_prod());
            pstm.setString(2, s.getCategorie());
            pstm.setString(3, s.getStatut());
            pstm.setFloat(4, s.getPrix());
            pstm.setFloat(5, s.getQuantite());
            pstm.setInt(6, s.getId_prod());
            pstm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Erreur SQL : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Stock s) {
        if (!aLesDroitsStock()) {
            System.out.println("Action non autorisée. Vous n'avez pas les droits.");
            return false;
        }

        String qry = "DELETE FROM stock WHERE id_prod=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, s.getId_prod());
            pstm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Erreur SQL lors de la suppression : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Stock> getAll() {
        List<Stock> stocks = new ArrayList<>();
        String qry;

        if (grade_emp.equals("admin") ||
                (grade_emp.equals("responsable") && depart_emp.equals("stock"))) {
            qry = "SELECT * FROM stock";
        } else {
            System.out.println("Action non autorisée.");
            return new ArrayList<>();
        }

        try (PreparedStatement pstm = cnx.prepareStatement(qry);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Stock s = new Stock();
                s.setId_prod(rs.getInt("id_prod"));
                s.setNom_prod(rs.getString("nom_prod"));
                s.setCategorie(rs.getString("categorie"));
                s.setStatut(rs.getString("statut"));
                s.setPrix(rs.getFloat("prix"));
                s.setQuantite(rs.getFloat("quantite"));

                stocks.add(s);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur SQL : " + ex.getMessage());
        }

        return stocks;
    }
}
