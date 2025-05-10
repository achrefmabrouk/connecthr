package tn.esprit.connecthr.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tn.esprit.connecthr.models.Commande;
import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.models.FournisseurPerformance;
import tn.esprit.connecthr.utils.DBConnection;

/**
 * Service pour l'analyse des données et les rapports de performance
 */
public class AnalyseService {
    
    private Connection conn;
    private CommandeService commandeService;
    private FournisseurService fournisseurService;
    
    public AnalyseService() {
        try {
            conn = DBConnection.getInstance().getConnection();
            commandeService = new CommandeService();
            fournisseurService = new FournisseurService();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Récupérer les données de performance d'un fournisseur
     * @param fournisseurId L'ID du fournisseur
     * @return L'objet FournisseurPerformance contenant les données d'analyse
     * @throws SQLException En cas d'erreur SQL
     */
    public FournisseurPerformance getFournisseurPerformance(int fournisseurId) throws SQLException {
        // Récupérer les informations du fournisseur
        Fournisseur fournisseur = fournisseurService.getFournisseurById(fournisseurId);
        if (fournisseur == null) {
            throw new SQLException("Fournisseur introuvable");
        }
        
        // Création de l'objet de performance
        FournisseurPerformance performance = new FournisseurPerformance(fournisseurId, fournisseur.getNom());
        
        // Récupérer les commandes du fournisseur
        List<Commande> commandes = commandeService.getCommandesByFournisseur(fournisseurId);
        
        // Nombre total de commandes
        performance.setTotalCommandes(commandes.size());
        
        // Calcul du montant total et montant moyen
        double montantTotal = 0;
        Map<String, Integer> statuts = new HashMap<>();
        
        // Calcul du délai moyen de livraison et commandes livrées à temps
        int totalJoursLivraison = 0;
        int commandesAvecLivraison = 0;
        int commandesLivreesATemps = 0;
        
        for (Commande commande : commandes) {
            // Montant total
            montantTotal += commande.getMontantTotal();
            
            // Répartition par statut
            String statut = commande.getStatut();
            statuts.put(statut, statuts.getOrDefault(statut, 0) + 1);
            
            // Ajouter un point dans la tendance
            performance.ajouterPointTendance(commande.getDateCommande(), commande.getMontantTotal());
            
            // Calcul des délais de livraison pour les commandes livrées
            if ("Livrée".equals(statut) && commande.getDateLivraison() != null) {
                LocalDate dateCommande = commande.getDateCommande();
                LocalDate dateLivraison = commande.getDateLivraison();
                
                // Calcul du délai en jours
                long delaiJours = ChronoUnit.DAYS.between(dateCommande, dateLivraison);
                totalJoursLivraison += delaiJours;
                commandesAvecLivraison++;
                
                // Vérifier si la livraison est à temps (délai <= date prévue)
                if (commande.getDateLivraisonPrevue() != null) {
                    if (!dateLivraison.isAfter(commande.getDateLivraisonPrevue())) {
                        commandesLivreesATemps++;
                    }
                }
            }
        }
        
        // Mettre à jour les statistiques
        performance.setMontantTotal(montantTotal);
        
        // Montant moyen par commande
        double montantMoyen = performance.getTotalCommandes() > 0 ? 
                montantTotal / performance.getTotalCommandes() : 0;
        performance.setMontantMoyenCommande(montantMoyen);
        
        // Répartition par statut
        performance.setRepartitionParStatut(statuts);
        
        // Délai moyen de livraison
        double delaiMoyen = commandesAvecLivraison > 0 ? 
                (double) totalJoursLivraison / commandesAvecLivraison : 0;
        performance.setDelaiMoyenLivraisonJours(delaiMoyen);
        
        // Pourcentage de livraisons à temps
        performance.setCommandesLivreesATemps(commandesLivreesATemps);
        double pourcentageLivreesATemps = commandesAvecLivraison > 0 ? 
                (double) commandesLivreesATemps / commandesAvecLivraison * 100 : 0;
        performance.setCommandesLivreesATempsPourcentage(pourcentageLivreesATemps);
        
        // Calcul du score global
        performance.mettreAJourScore();
        
        return performance;
    }
    
    /**
     * Récupérer les tendances de commandes pour tous les fournisseurs
     * @return Une liste de points de données pour les tendances
     * @throws SQLException En cas d'erreur SQL
     */
    public List<Map<String, Object>> getTendancesGlobales() throws SQLException {
        List<Map<String, Object>> tendances = new ArrayList<>();
        
        String query = "SELECT DATE(date_commande) as date, SUM(montant_total) as montant_total " +
                       "FROM commandes " +
                       "GROUP BY DATE(date_commande) " +
                       "ORDER BY date_commande";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> point = new HashMap<>();
                Date dateSQL = rs.getDate("date");
                LocalDate date = dateSQL.toLocalDate();
                double montant = rs.getDouble("montant_total");
                
                point.put("date", date);
                point.put("montant", montant);
                tendances.add(point);
            }
        }
        
        return tendances;
    }
    
    /**
     * Récupérer les statistiques globales de tous les fournisseurs
     * @return Une carte contenant les statistiques globales
     * @throws SQLException En cas d'erreur SQL
     */
    public Map<String, Object> getStatistiquesGlobales() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        
        // Requête pour les statistiques globales
        String query = "SELECT " +
                       "COUNT(*) as total_commandes, " +
                       "SUM(montant_total) as montant_total, " +
                       "AVG(montant_total) as montant_moyen, " +
                       "COUNT(DISTINCT id_fournisseur) as total_fournisseurs " +
                       "FROM commandes";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                stats.put("totalCommandes", rs.getInt("total_commandes"));
                stats.put("montantTotal", rs.getDouble("montant_total"));
                stats.put("montantMoyen", rs.getDouble("montant_moyen"));
                stats.put("totalFournisseurs", rs.getInt("total_fournisseurs"));
            }
        }
        
        // Requête pour la répartition par statut
        query = "SELECT statut, COUNT(*) as nombre " +
                "FROM commandes " +
                "GROUP BY statut";
        
        Map<String, Integer> repartitionStatut = new HashMap<>();
        
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String statut = rs.getString("statut");
                int nombre = rs.getInt("nombre");
                repartitionStatut.put(statut, nombre);
            }
        }
        
        stats.put("repartitionStatut", repartitionStatut);
        
        return stats;
    }
}