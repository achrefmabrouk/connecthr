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
import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.utils.DBConnection;

/**
 * Service class for Commande (Order) operations
 */
public class CommandeService {
    private Connection connection;
    private FournisseurService fournisseurService;
    
    public CommandeService() {
        try {
            this.connection = DBConnection.getInstance().getConnection();
            this.fournisseurService = new FournisseurService();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Add a new order to the database
     * @param commande The order to add
     * @return The added order with generated ID
     * @throws SQLException If an SQL error occurs
     */
    public Commande addCommande(Commande commande) throws SQLException {
        String query = "INSERT INTO commandes (fournisseur_id, numero_commande, date_commande, " +
                      "date_livraison, statut, montant_total, description, date_creation, " +
                      "derniere_mise_a_jour) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, commande.getFournisseurId());
            ps.setString(2, commande.getNumeroCommande());
            ps.setDate(3, commande.getDateCommande() != null ? Date.valueOf(commande.getDateCommande()) : null);
            ps.setDate(4, commande.getDateLivraison() != null ? Date.valueOf(commande.getDateLivraison()) : null);
            ps.setString(5, commande.getStatut());
            ps.setDouble(6, commande.getMontantTotal());
            ps.setString(7, commande.getDescription());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating commande failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    commande.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating commande failed, no ID obtained.");
                }
            }
        }
        
        // Set the Fournisseur object
        Fournisseur fournisseur = fournisseurService.getFournisseurById(commande.getFournisseurId());
        commande.setFournisseur(fournisseur);
        
        return commande;
    }
    
    /**
     * Get an order by ID
     * @param id The order ID
     * @return The order object
     * @throws SQLException If an SQL error occurs
     */
    public Commande getCommandeById(int id) throws SQLException {
        String query = "SELECT * FROM commandes WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Commande commande = mapResultSetToCommande(rs);
                    Fournisseur fournisseur = fournisseurService.getFournisseurById(commande.getFournisseurId());
                    commande.setFournisseur(fournisseur);
                    return commande;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all orders
     * @return List of orders
     * @throws SQLException If an SQL error occurs
     */
    public List<Commande> getAllCommandes() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commandes ORDER BY date_commande DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Commande commande = mapResultSetToCommande(rs);
                Fournisseur fournisseur = fournisseurService.getFournisseurById(commande.getFournisseurId());
                commande.setFournisseur(fournisseur);
                commandes.add(commande);
            }
        }
        
        return commandes;
    }
    
    /**
     * Get orders by supplier ID
     * @param fournisseurId The supplier ID
     * @return List of orders for the supplier
     * @throws SQLException If an SQL error occurs
     */
    public List<Commande> getCommandesByFournisseur(int fournisseurId) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commandes WHERE fournisseur_id = ? ORDER BY date_commande DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fournisseurId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Commande commande = mapResultSetToCommande(rs);
                    Fournisseur fournisseur = fournisseurService.getFournisseurById(fournisseurId);
                    commande.setFournisseur(fournisseur);
                    commandes.add(commande);
                }
            }
        }
        
        return commandes;
    }
    
    /**
     * Update an existing order
     * @param commande The order to update
     * @return true if successful, false otherwise
     * @throws SQLException If an SQL error occurs
     */
    public boolean updateCommande(Commande commande) throws SQLException {
        String query = "UPDATE commandes SET fournisseur_id = ?, numero_commande = ?, " +
                      "date_commande = ?, date_livraison = ?, statut = ?, montant_total = ?, " +
                      "description = ?, derniere_mise_a_jour = ? WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, commande.getFournisseurId());
            ps.setString(2, commande.getNumeroCommande());
            ps.setDate(3, commande.getDateCommande() != null ? Date.valueOf(commande.getDateCommande()) : null);
            ps.setDate(4, commande.getDateLivraison() != null ? Date.valueOf(commande.getDateLivraison()) : null);
            ps.setString(5, commande.getStatut());
            ps.setDouble(6, commande.getMontantTotal());
            ps.setString(7, commande.getDescription());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(9, commande.getId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Delete an order
     * @param id The order ID to delete
     * @return true if successful, false otherwise
     * @throws SQLException If an SQL error occurs
     */
    public boolean deleteCommande(int id) throws SQLException {
        String query = "DELETE FROM commandes WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Search orders by number, status, or description
     * @param searchTerm The search term
     * @return List of matching orders
     * @throws SQLException If an SQL error occurs
     */
    public List<Commande> searchCommandes(String searchTerm) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT c.* FROM commandes c " +
                      "JOIN fournisseurs f ON c.fournisseur_id = f.id " +
                      "WHERE c.numero_commande LIKE ? OR c.statut LIKE ? " +
                      "OR c.description LIKE ? OR f.nom LIKE ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            String term = "%" + searchTerm + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            ps.setString(4, term);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Commande commande = mapResultSetToCommande(rs);
                    Fournisseur fournisseur = fournisseurService.getFournisseurById(commande.getFournisseurId());
                    commande.setFournisseur(fournisseur);
                    commandes.add(commande);
                }
            }
        }
        
        return commandes;
    }
    
    /**
     * Get the total amount of orders by supplier
     * @param fournisseurId The supplier ID
     * @return The total amount
     * @throws SQLException If an SQL error occurs
     */
    public double getTotalCommandesByFournisseur(int fournisseurId) throws SQLException {
        String query = "SELECT SUM(montant_total) AS total FROM commandes WHERE fournisseur_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fournisseurId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Map a ResultSet to a Commande object
     * @param rs The ResultSet to map
     * @return The Commande object
     * @throws SQLException If an SQL error occurs
     */
    private Commande mapResultSetToCommande(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        
        commande.setId(rs.getInt("id"));
        commande.setFournisseurId(rs.getInt("fournisseur_id"));
        commande.setNumeroCommande(rs.getString("numero_commande"));
        
        Date dateCommande = rs.getDate("date_commande");
        if (dateCommande != null) {
            commande.setDateCommande(dateCommande.toLocalDate());
        }
        
        Date dateLivraison = rs.getDate("date_livraison");
        if (dateLivraison != null) {
            commande.setDateLivraison(dateLivraison.toLocalDate());
        }
        
        // Récupérer la date de livraison prévue si elle existe
        try {
            Date dateLivraisonPrevue = rs.getDate("date_livraison_prevue");
            if (dateLivraisonPrevue != null) {
                commande.setDateLivraisonPrevue(dateLivraisonPrevue.toLocalDate());
            }
        } catch (SQLException e) {
            // La colonne n'existe peut-être pas encore dans la base de données
            // On ignore l'erreur
        }
        
        commande.setStatut(rs.getString("statut"));
        commande.setMontantTotal(rs.getDouble("montant_total"));
        commande.setDescription(rs.getString("description"));
        
        Timestamp dateCreation = rs.getTimestamp("date_creation");
        if (dateCreation != null) {
            commande.setDateCreation(dateCreation.toLocalDateTime());
        }
        
        Timestamp derniereMiseAJour = rs.getTimestamp("derniere_mise_a_jour");
        if (derniereMiseAJour != null) {
            commande.setDerniereMiseAJour(derniereMiseAJour.toLocalDateTime());
        }
        
        return commande;
    }
    
    /**
     * Generate a new order number
     * @return A unique order number
     * @throws SQLException If an SQL error occurs
     */
    public String generateOrderNumber() throws SQLException {
        String prefix = "CMD-";
        String date = LocalDate.now().toString().replace("-", "");
        
        String query = "SELECT COUNT(*) AS count FROM commandes WHERE numero_commande LIKE ?";
        
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
}
