package tn.esprit.connecthr.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model class for Commande (Order)
 */
public class Commande {
    private int id;
    private int fournisseurId;
    private String numeroCommande;
    private LocalDate dateCommande;
    private LocalDate dateLivraison;
    private LocalDate dateLivraisonPrevue;
    private String statut; // En attente, En cours, Livrée, Annulée
    private double montantTotal;
    private String description;
    private LocalDateTime dateCreation;
    private LocalDateTime derniereMiseAJour;
    
    // Additional fields for relationship
    private Fournisseur fournisseur;
    
    // Default constructor
    public Commande() {
        this.dateCreation = LocalDateTime.now();
        this.derniereMiseAJour = LocalDateTime.now();
        this.dateCommande = LocalDate.now();
    }
    
    // Parameterized constructor
    public Commande(int id, int fournisseurId, String numeroCommande, LocalDate dateCommande, 
                   LocalDate dateLivraison, LocalDate dateLivraisonPrevue, String statut, 
                   double montantTotal, String description) {
        this.id = id;
        this.fournisseurId = fournisseurId;
        this.numeroCommande = numeroCommande;
        this.dateCommande = dateCommande;
        this.dateLivraison = dateLivraison;
        this.dateLivraisonPrevue = dateLivraisonPrevue;
        this.statut = statut;
        this.montantTotal = montantTotal;
        this.description = description;
        this.dateCreation = LocalDateTime.now();
        this.derniereMiseAJour = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(int fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDate getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
    }
    
    public LocalDate getDateLivraisonPrevue() {
        return dateLivraisonPrevue;
    }

    public void setDateLivraisonPrevue(LocalDate dateLivraisonPrevue) {
        this.dateLivraisonPrevue = dateLivraisonPrevue;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(LocalDateTime derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        if (fournisseur != null) {
            this.fournisseurId = fournisseur.getId();
        }
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", numeroCommande='" + numeroCommande + '\'' +
                ", statut='" + statut + '\'' +
                ", montantTotal=" + montantTotal +
                '}';
    }
    
    // Format montant for display
    public String getMontantFormate() {
        return String.format("%,.2f TND", montantTotal);
    }
}
