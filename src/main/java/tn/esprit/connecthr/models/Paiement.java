package tn.esprit.connecthr.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model class for Paiement (Payment)
 */
public class Paiement {
    private int id;
    private int commandeId;
    private String numeroPaiement;
    private LocalDate datePaiement;
    private double montant;
    private String methodePaiement; // Virement, Chèque, Espèces, etc.
    private String statut; // En attente, Complété, Échoué, Annulé
    private String reference;
    private String description;
    private LocalDateTime dateCreation;
    private LocalDateTime derniereMiseAJour;
    
    // Additional fields for relationship
    private Commande commande;
    
    // Default constructor
    public Paiement() {
        this.dateCreation = LocalDateTime.now();
        this.derniereMiseAJour = LocalDateTime.now();
        this.datePaiement = LocalDate.now();
    }
    
    // Parameterized constructor
    public Paiement(int id, int commandeId, String numeroPaiement, LocalDate datePaiement, 
                   double montant, String methodePaiement, String statut, 
                   String reference, String description) {
        this.id = id;
        this.commandeId = commandeId;
        this.numeroPaiement = numeroPaiement;
        this.datePaiement = datePaiement;
        this.montant = montant;
        this.methodePaiement = methodePaiement;
        this.statut = statut;
        this.reference = reference;
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

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public String getNumeroPaiement() {
        return numeroPaiement;
    }

    public void setNumeroPaiement(String numeroPaiement) {
        this.numeroPaiement = numeroPaiement;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getMethodePaiement() {
        return methodePaiement;
    }

    public void setMethodePaiement(String methodePaiement) {
        this.methodePaiement = methodePaiement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        if (commande != null) {
            this.commandeId = commande.getId();
        }
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "id=" + id +
                ", numeroPaiement='" + numeroPaiement + '\'' +
                ", montant=" + montant +
                ", statut='" + statut + '\'' +
                '}';
    }
    
    // Format montant for display
    public String getMontantFormate() {
        return String.format("%,.2f TND", montant);
    }
}
