package tn.esprit.connecthr.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modèle pour les données de performance des fournisseurs
 */
public class FournisseurPerformance {
    private int fournisseurId;
    private String nomFournisseur;
    private double scoreGlobal;
    private int totalCommandes;
    private int commandesLivreesATemps;
    private double commandesLivreesATempsPourcentage;
    private double delaiMoyenLivraisonJours;
    private double montantTotal;
    private double montantMoyenCommande;
    private Map<String, Integer> repartitionParStatut;
    private List<Map<String, Object>> tendanceCommandes;
    
    public FournisseurPerformance() {
        this.repartitionParStatut = new HashMap<>();
        this.tendanceCommandes = new ArrayList<>();
    }
    
    public FournisseurPerformance(int fournisseurId, String nomFournisseur) {
        this();
        this.fournisseurId = fournisseurId;
        this.nomFournisseur = nomFournisseur;
    }

    public int getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(int fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    public double getScoreGlobal() {
        return scoreGlobal;
    }

    public void setScoreGlobal(double scoreGlobal) {
        this.scoreGlobal = scoreGlobal;
    }

    public int getTotalCommandes() {
        return totalCommandes;
    }

    public void setTotalCommandes(int totalCommandes) {
        this.totalCommandes = totalCommandes;
    }

    public int getCommandesLivreesATemps() {
        return commandesLivreesATemps;
    }

    public void setCommandesLivreesATemps(int commandesLivreesATemps) {
        this.commandesLivreesATemps = commandesLivreesATemps;
    }

    public double getCommandesLivreesATempsPourcentage() {
        return commandesLivreesATempsPourcentage;
    }

    public void setCommandesLivreesATempsPourcentage(double commandesLivreesATempsPourcentage) {
        this.commandesLivreesATempsPourcentage = commandesLivreesATempsPourcentage;
    }

    public double getDelaiMoyenLivraisonJours() {
        return delaiMoyenLivraisonJours;
    }

    public void setDelaiMoyenLivraisonJours(double delaiMoyenLivraisonJours) {
        this.delaiMoyenLivraisonJours = delaiMoyenLivraisonJours;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantMoyenCommande() {
        return montantMoyenCommande;
    }

    public void setMontantMoyenCommande(double montantMoyenCommande) {
        this.montantMoyenCommande = montantMoyenCommande;
    }

    public Map<String, Integer> getRepartitionParStatut() {
        return repartitionParStatut;
    }

    public void setRepartitionParStatut(Map<String, Integer> repartitionParStatut) {
        this.repartitionParStatut = repartitionParStatut;
    }
    
    public void ajouterStatut(String statut, int count) {
        this.repartitionParStatut.put(statut, count);
    }

    public List<Map<String, Object>> getTendanceCommandes() {
        return tendanceCommandes;
    }

    public void setTendanceCommandes(List<Map<String, Object>> tendanceCommandes) {
        this.tendanceCommandes = tendanceCommandes;
    }
    
    /**
     * Ajouter un point de données à la tendance des commandes
     * @param date La date de la commande
     * @param montant Le montant de la commande
     */
    public void ajouterPointTendance(LocalDate date, double montant) {
        Map<String, Object> point = new HashMap<>();
        point.put("date", date);
        point.put("montant", montant);
        this.tendanceCommandes.add(point);
    }
    
    /**
     * Calcule le score global de performance
     * Ce score est basé sur plusieurs facteurs pondérés:
     * - Pourcentage de livraisons à temps (40%)
     * - Délai moyen de livraison (30%)
     * - Taux de commandes annulées (30%)
     * @return Le score calculé sur 100
     */
    public double calculerScoreGlobal() {
        // 1. Score pour les livraisons à temps (pondération: 40%)
        double scoreLivraisonsATemps = this.commandesLivreesATempsPourcentage * 0.4;
        
        // 2. Score pour le délai moyen de livraison (pondération: 30%)
        // Plus le délai est court, meilleur est le score
        // On considère qu'un délai de 7 jours ou moins est optimal (100%)
        // et un délai de 30 jours ou plus est minimal (0%)
        double scoreDelai = 0;
        if (this.delaiMoyenLivraisonJours <= 7) {
            scoreDelai = 30; // Score maximal
        } else if (this.delaiMoyenLivraisonJours >= 30) {
            scoreDelai = 0; // Score minimal
        } else {
            // Interpolation linéaire entre 7 et 30 jours
            scoreDelai = 30 * (1 - (this.delaiMoyenLivraisonJours - 7) / 23);
        }
        
        // 3. Score pour les commandes annulées (pondération: 30%)
        // Moins de commandes annulées = meilleur score
        int commandesAnnulees = this.repartitionParStatut.getOrDefault("Annulée", 0);
        double tauxAnnulation = this.totalCommandes > 0 ? 
                (double) commandesAnnulees / this.totalCommandes : 0;
        
        // Un taux d'annulation de 0% est optimal (30 points)
        // Un taux d'annulation de 20% ou plus est minimal (0 points)
        double scoreAnnulation = tauxAnnulation <= 0 ? 30 : 
                tauxAnnulation >= 0.2 ? 0 : 30 * (1 - tauxAnnulation / 0.2);
        
        // Score global (somme des scores pondérés)
        double scoreTotal = scoreLivraisonsATemps + scoreDelai + scoreAnnulation;
        
        // Arrondir à une décimale
        return Math.round(scoreTotal * 10) / 10.0;
    }
    
    /**
     * Met à jour le score global en fonction des données actuelles
     */
    public void mettreAJourScore() {
        this.scoreGlobal = calculerScoreGlobal();
    }
    
    @Override
    public String toString() {
        return "FournisseurPerformance [fournisseurId=" + fournisseurId + ", nomFournisseur=" + nomFournisseur
                + ", scoreGlobal=" + scoreGlobal + ", totalCommandes=" + totalCommandes + ", montantTotal="
                + montantTotal + "]";
    }
}