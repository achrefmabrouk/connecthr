package tn.esprit.services;

import tn.esprit.models.Employe;
import tn.esprit.utils.connecthrDB;

import java.sql.*;

public class AuthService {
    private Connection cnx;

    public AuthService() {
        cnx = connecthrDB.getInstance().getCnx();
    }

    public Employe login(String nom, String password) {
        String sql = "SELECT * FROM employe WHERE nom = ? AND password = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employe emp = new Employe();
                emp.setId(rs.getInt("id"));
                emp.setNom(rs.getString("nom"));
                emp.setPrenom(rs.getString("prenom"));
                emp.setGrade(rs.getString("grade"));
                emp.setDepartement(rs.getString("departement"));

                return emp;
            }
        } catch (SQLException e) {
            System.out.println("Erreur login : " + e.getMessage());
        }
        return null;
    }

    public boolean register(Employe emp) {
        String sql = "INSERT INTO employe (nom, prenom, adresse, password, grade, departement, telephone, sexe, poste, niveau, TarifJournalier, JoursCongesRestants, ImageSrc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, emp.getNom());
            ps.setString(2, emp.getPrenom());
            ps.setString(3, emp.getAdresse());
            ps.setString(4, emp.getPassword());
            ps.setString(5, emp.getGrade());
            ps.setString(6, emp.getDepartement());
            ps.setString(7, emp.getTelephone());
            ps.setString(8, emp.getSexe());
            ps.setString(9, emp.getPoste());
            ps.setString(10, emp.getNiveau());
            ps.setDouble(11, emp.getTarifJournalier());
            ps.setInt(12, emp.getJoursCongesRestants());
            if (emp.getImageSrc() == null || emp.getImageSrc().isEmpty()) {
                ps.setNull(13, Types.VARCHAR);
            } else {
                ps.setString(13, emp.getImageSrc());
            }

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur inscription : " + e.getMessage());
            return false;
        }
    }
}
