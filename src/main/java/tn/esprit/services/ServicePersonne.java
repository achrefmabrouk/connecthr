package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Personne;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePersonne implements IService<Personne> {
    private Connection cnx;

    public ServicePersonne() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    private Personne mapResultSetToPersonne(ResultSet rs) throws SQLException {
        return new Personne(
                rs.getInt("ID"),
                rs.getString("Nom"),
                rs.getString("Prenom"),
                rs.getString("Sex"),
                rs.getString("Telephone"),
                rs.getString("Role"),
                rs.getString("Addresse"),
                rs.getString("Department"),
                rs.getDouble("TarifJournalier"),
                rs.getInt("JoursCongesRestants")
        );
    }

    @Override
    public void add(Personne personne) {
        String qry = "INSERT INTO personne (Nom, Prenom, Sex, Telephone, Role, Department, Addresse, TarifJournalier, JoursCongesRestants) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, personne.getNom());
            pstm.setString(2, personne.getPrenom());
            pstm.setString(3, personne.getSex());
            pstm.setString(4, personne.getTel());
            pstm.setString(5, personne.getRole());
            pstm.setString(6, personne.getDepartment());
            pstm.setString(7, personne.getAddress());
            pstm.setDouble(8, personne.getDailyRate());
            pstm.setInt(9, personne.getRemainingLeaveDays());

            pstm.executeUpdate();
            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    personne.setId(generatedKeys.getInt(1));
                    System.out.println("Personne ajoutée avec succès");
                } else {
                    System.err.println("aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Personne> getAll() {
        List<Personne> personnes = new ArrayList<>();
        String qry = "SELECT * FROM personne";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Personne p = mapResultSetToPersonne(rs);
                personnes.add(p);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return personnes;
    }

    @Override
    public Personne getById(int id) {
        Personne personne = null;
        String qry = "SELECT * FROM personne WHERE ID = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    personne = mapResultSetToPersonne(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return personne;
    }

    @Override
    public void update(Personne personne) {
        if (personne == null || personne.getId() <= 0) {
            System.err.println("L'objet est nul ou l'ID est invalide!");
            return;
        }
        String qry = "UPDATE personne SET Nom=?, Prenom=?, Sex=?, Telephone=?, Role=?, Department=?, Addresse=?, TarifJournalier=?, JoursCongesRestants=? WHERE ID=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, personne.getNom());
            pstm.setString(2, personne.getPrenom());
            pstm.setString(3, personne.getSex());
            pstm.setString(4, personne.getTel());
            pstm.setString(5, personne.getRole());
            pstm.setString(6, personne.getDepartment());
            pstm.setString(7, personne.getAddress());
            pstm.setDouble(8, personne.getDailyRate());
            pstm.setInt(9, personne.getRemainingLeaveDays());
            pstm.setInt(10, personne.getId());

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Personne mise à jour avec succès");
            } else {
                System.out.println("l'ID " + personne.getId() + "est introuvable pour la mise à jour.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Personne personne) {
        if (personne == null || personne.getId() <= 0) {
            System.err.println("L'objet est nul ou l'ID est invalide!");
            return;
        }

        int id = personne.getId();

        String qry = "DELETE FROM personne WHERE ID = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Personne supprimée avec succès");
            } else {
                System.out.println("l'ID " + id + " est introuvable pour la suppression.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}