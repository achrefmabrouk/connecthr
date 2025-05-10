package tn.esprit.connecthr.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import tn.esprit.connecthr.models.Commande;
import tn.esprit.connecthr.models.Paiement;
import tn.esprit.connecthr.utils.DBConnection;

/**
 * Service class for Paiement (Payment) operations
 */
public class PaiementService {
    private Connection connection;
    private CommandeService commandeService;
    
    public PaiementService() {
        try {
            this.connection = DBConnection.getInstance().getConnection();
            this.commandeService = new CommandeService();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Add a new payment to the database
     * @param paiement The payment to add
     * @return The added payment with generated ID
     * @throws SQLException If an SQL error occurs
     */
    public Paiement addPaiement(Paiement paiement) throws SQLException {
        String query = "INSERT INTO paiements (commande_id, numero_paiement, date_paiement, " +
                      "montant, methode_paiement, statut, reference, description, " +
                      "date_creation, derniere_mise_a_jour) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, paiement.getCommandeId());
            ps.setString(2, paiement.getNumeroPaiement());
            ps.setDate(3, paiement.getDatePaiement() != null ? Date.valueOf(paiement.getDatePaiement()) : null);
            ps.setDouble(4, paiement.getMontant());
            ps.setString(5, paiement.getMethodePaiement());
            ps.setString(6, paiement.getStatut());
            ps.setString(7, paiement.getReference());
            ps.setString(8, paiement.getDescription());
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating paiement failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paiement.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating paiement failed, no ID obtained.");
                }
            }
        }
        
        // Set the Commande object
        Commande commande = commandeService.getCommandeById(paiement.getCommandeId());
        paiement.setCommande(commande);
        
        return paiement;
    }
    
    /**
     * Get a payment by ID
     * @param id The payment ID
     * @return The payment object
     * @throws SQLException If an SQL error occurs
     */
    public Paiement getPaiementById(int id) throws SQLException {
        String query = "SELECT * FROM paiements WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paiement paiement = mapResultSetToPaiement(rs);
                    Commande commande = commandeService.getCommandeById(paiement.getCommandeId());
                    paiement.setCommande(commande);
                    return paiement;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all payments
     * @return List of payments
     * @throws SQLException If an SQL error occurs
     */
    public List<Paiement> getAllPaiements() throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String query = "SELECT * FROM paiements ORDER BY date_paiement DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Paiement paiement = mapResultSetToPaiement(rs);
                Commande commande = commandeService.getCommandeById(paiement.getCommandeId());
                paiement.setCommande(commande);
                paiements.add(paiement);
            }
        }
        
        return paiements;
    }
    
    /**
     * Get payments by order ID
     * @param commandeId The order ID
     * @return List of payments for the order
     * @throws SQLException If an SQL error occurs
     */
    public List<Paiement> getPaiementsByCommande(int commandeId) throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String query = "SELECT * FROM paiements WHERE commande_id = ? ORDER BY date_paiement DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, commandeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Paiement paiement = mapResultSetToPaiement(rs);
                    Commande commande = commandeService.getCommandeById(commandeId);
                    paiement.setCommande(commande);
                    paiements.add(paiement);
                }
            }
        }
        
        return paiements;
    }
    
    /**
     * Get payments by supplier ID
     * @param fournisseurId The supplier ID
     * @return List of payments for the supplier
     * @throws SQLException If an SQL error occurs
     */
    public List<Paiement> getPaiementsByFournisseur(int fournisseurId) throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String query = "SELECT p.* FROM paiements p " +
                      "JOIN commandes c ON p.commande_id = c.id " +
                      "WHERE c.fournisseur_id = ? " +
                      "ORDER BY p.date_paiement DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fournisseurId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Paiement paiement = mapResultSetToPaiement(rs);
                    Commande commande = commandeService.getCommandeById(paiement.getCommandeId());
                    paiement.setCommande(commande);
                    paiements.add(paiement);
                }
            }
        }
        
        return paiements;
    }
    
    /**
     * Update an existing payment
     * @param paiement The payment to update
     * @return true if successful, false otherwise
     * @throws SQLException If an SQL error occurs
     */
    public boolean updatePaiement(Paiement paiement) throws SQLException {
        String query = "UPDATE paiements SET commande_id = ?, numero_paiement = ?, " +
                      "date_paiement = ?, montant = ?, methode_paiement = ?, statut = ?, " +
                      "reference = ?, description = ?, derniere_mise_a_jour = ? WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, paiement.getCommandeId());
            ps.setString(2, paiement.getNumeroPaiement());
            ps.setDate(3, paiement.getDatePaiement() != null ? Date.valueOf(paiement.getDatePaiement()) : null);
            ps.setDouble(4, paiement.getMontant());
            ps.setString(5, paiement.getMethodePaiement());
            ps.setString(6, paiement.getStatut());
            ps.setString(7, paiement.getReference());
            ps.setString(8, paiement.getDescription());
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(10, paiement.getId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Delete a payment
     * @param id The payment ID to delete
     * @return true if successful, false otherwise
     * @throws SQLException If an SQL error occurs
     */
    public boolean deletePaiement(int id) throws SQLException {
        String query = "DELETE FROM paiements WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Get the total amount paid for an order
     * @param commandeId The order ID
     * @return The total amount paid
     * @throws SQLException If an SQL error occurs
     */
    public double getTotalPaidForCommande(int commandeId) throws SQLException {
        String query = "SELECT SUM(montant) AS total FROM paiements " +
                      "WHERE commande_id = ? AND statut = 'Complété'";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, commandeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Generate a new payment number
     * @return A unique payment number
     * @throws SQLException If an SQL error occurs
     */
    public String generatePaymentNumber() throws SQLException {
        String prefix = "PAY-";
        String date = LocalDate.now().toString().replace("-", "");
        
        String query = "SELECT COUNT(*) AS count FROM paiements WHERE numero_paiement LIKE ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, prefix + date + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count") + 1;
                    return prefix + date + "-" + String.format("%03d", count);
                }
            }
        }
        
        return prefix + date + "-001";
    }
    
    /**
     * Map a ResultSet to a Paiement object
     * @param rs The ResultSet to map
     * @return The Paiement object
     * @throws SQLException If an SQL error occurs
     */
    private Paiement mapResultSetToPaiement(ResultSet rs) throws SQLException {
        Paiement paiement = new Paiement();
        
        paiement.setId(rs.getInt("id"));
        paiement.setCommandeId(rs.getInt("commande_id"));
        paiement.setNumeroPaiement(rs.getString("numero_paiement"));
        
        Date datePaiement = rs.getDate("date_paiement");
        if (datePaiement != null) {
            paiement.setDatePaiement(datePaiement.toLocalDate());
        }
        
        paiement.setMontant(rs.getDouble("montant"));
        paiement.setMethodePaiement(rs.getString("methode_paiement"));
        paiement.setStatut(rs.getString("statut"));
        paiement.setReference(rs.getString("reference"));
        paiement.setDescription(rs.getString("description"));
        
        Timestamp dateCreation = rs.getTimestamp("date_creation");
        if (dateCreation != null) {
            paiement.setDateCreation(dateCreation.toLocalDateTime());
        }
        
        Timestamp derniereMiseAJour = rs.getTimestamp("derniere_mise_a_jour");
        if (derniereMiseAJour != null) {
            paiement.setDerniereMiseAJour(derniereMiseAJour.toLocalDateTime());
        }
        
        return paiement;
    }
}
