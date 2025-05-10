package tn.esprit.connecthr.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.utils.DBConnection;

/**
 * Service class for Fournisseur (Supplier) operations
 */
public class FournisseurService {
    private Connection connection;
    
    public FournisseurService() {
        try {
            this.connection = DBConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Add a new supplier to the database
     * @param fournisseur The supplier to add
     * @return The added supplier with generated ID
     * @throws SQLException If an SQL error occurs
     */
    public Fournisseur addFournisseur(Fournisseur fournisseur) throws SQLException {
        String query = "INSERT INTO fournisseurs (nom, type_fournisseur, adresse, telephone, email, " +
                       "contact_name, contact_position, date_creation, derniere_mise_a_jour, active, notes) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, fournisseur.getNom());
            ps.setString(2, fournisseur.getTypeFournisseur());
            ps.setString(3, fournisseur.getAdresse());
            ps.setString(4, fournisseur.getTelephone());
            ps.setString(5, fournisseur.getEmail());
            ps.setString(6, fournisseur.getContactName());
            ps.setString(7, fournisseur.getContactPosition());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setBoolean(10, fournisseur.isActive());
            ps.setString(11, fournisseur.getNotes());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating fournisseur failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    fournisseur.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating fournisseur failed, no ID obtained.");
                }
            }
        }
        
        return fournisseur;
    }
    
    /**
     * Get a supplier by ID
     * @param id The supplier ID
     * @return The supplier object
     * @throws SQLException If an SQL error occurs
     */
    public Fournisseur getFournisseurById(int id) throws SQLException {
        String query = "SELECT * FROM fournisseurs WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFournisseur(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all suppliers
     * @return List of suppliers
     * @throws SQLException If an SQL error occurs
     */
    public List<Fournisseur> getAllFournisseurs() throws SQLException {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String query = "SELECT * FROM fournisseurs WHERE active = true ORDER BY nom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                fournisseurs.add(mapResultSetToFournisseur(rs));
            }
        }
        
        return fournisseurs;
    }
    
    /**
     * Update an existing supplier
     * @param fournisseur The supplier to update
     * @return true if successful, false otherwise
     * @throws SQLException If an SQL error occurs
     */
    public boolean updateFournisseur(Fournisseur fournisseur) throws SQLException {
        String query = "UPDATE fournisseurs SET nom = ?, type_fournisseur = ?, adresse = ?, " +
                      "telephone = ?, email = ?, contact_name = ?, contact_position = ?, " +
                      "derniere_mise_a_jour = ?, notes = ? WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, fournisseur.getNom());
            ps.setString(2, fournisseur.getTypeFournisseur());
            ps.setString(3, fournisseur.getAdresse());
            ps.setString(4, fournisseur.getTelephone());
            ps.setString(5, fournisseur.getEmail());
            ps.setString(6, fournisseur.getContactName());
            ps.setString(7, fournisseur.getContactPosition());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, fournisseur.getNotes());
            ps.setInt(10, fournisseur.getId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Delete a supplier (logical delete by setting active = false)
     * @param id The supplier ID to delete
     * @return true if successful, false otherwise
     * @throws SQLException If an SQL error occurs
     */
    public boolean deleteFournisseur(int id) throws SQLException {
        String query = "UPDATE fournisseurs SET active = false, derniere_mise_a_jour = ? WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, id);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Search suppliers by name, type, or contact
     * @param searchTerm The search term
     * @return List of matching suppliers
     * @throws SQLException If an SQL error occurs
     */
    public List<Fournisseur> searchFournisseurs(String searchTerm) throws SQLException {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String query = "SELECT * FROM fournisseurs WHERE active = true AND " +
                      "(nom LIKE ? OR type_fournisseur LIKE ? OR contact_name LIKE ? OR email LIKE ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            String term = "%" + searchTerm + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            ps.setString(4, term);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    fournisseurs.add(mapResultSetToFournisseur(rs));
                }
            }
        }
        
        return fournisseurs;
    }
    
    /**
     * Map a ResultSet to a Fournisseur object
     * @param rs The ResultSet to map
     * @return The Fournisseur object
     * @throws SQLException If an SQL error occurs
     */
    private Fournisseur mapResultSetToFournisseur(ResultSet rs) throws SQLException {
        Fournisseur fournisseur = new Fournisseur();
        
        fournisseur.setId(rs.getInt("id"));
        fournisseur.setNom(rs.getString("nom"));
        fournisseur.setTypeFournisseur(rs.getString("type_fournisseur"));
        fournisseur.setAdresse(rs.getString("adresse"));
        fournisseur.setTelephone(rs.getString("telephone"));
        fournisseur.setEmail(rs.getString("email"));
        fournisseur.setContactName(rs.getString("contact_name"));
        fournisseur.setContactPosition(rs.getString("contact_position"));
        
        Timestamp dateCreation = rs.getTimestamp("date_creation");
        if (dateCreation != null) {
            fournisseur.setDateCreation(dateCreation.toLocalDateTime());
        }
        
        Timestamp derniereMiseAJour = rs.getTimestamp("derniere_mise_a_jour");
        if (derniereMiseAJour != null) {
            fournisseur.setDerniereMiseAJour(derniereMiseAJour.toLocalDateTime());
        }
        
        fournisseur.setActive(rs.getBoolean("active"));
        fournisseur.setNotes(rs.getString("notes"));
        
        return fournisseur;
    }
}
