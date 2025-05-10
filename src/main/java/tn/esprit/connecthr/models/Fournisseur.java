package tn.esprit.connecthr.models;

import java.time.LocalDateTime;

/**
 * Model class for Fournisseur (Supplier)
 */
public class Fournisseur {
    private int id;
    private String nom;
    private String typeFournisseur;
    private String adresse;
    private String telephone;
    private String email;
    private String contactName;
    private String contactPosition;
    private LocalDateTime dateCreation;
    private LocalDateTime derniereMiseAJour;
    private boolean active;
    private String notes;
    
    // Default constructor
    public Fournisseur() {
        this.dateCreation = LocalDateTime.now();
        this.derniereMiseAJour = LocalDateTime.now();
        this.active = true;
    }
    
    // Parameterized constructor
    public Fournisseur(int id, String nom, String typeFournisseur, String adresse, 
                      String telephone, String email, String contactName, String contactPosition) {
        this.id = id;
        this.nom = nom;
        this.typeFournisseur = typeFournisseur;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.contactName = contactName;
        this.contactPosition = contactPosition;
        this.dateCreation = LocalDateTime.now();
        this.derniereMiseAJour = LocalDateTime.now();
        this.active = true;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTypeFournisseur() {
        return typeFournisseur;
    }

    public void setTypeFournisseur(String typeFournisseur) {
        this.typeFournisseur = typeFournisseur;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", typeFournisseur='" + typeFournisseur + '\'' +
                ", contactName='" + contactName + '\'' +
                '}';
    }
    
    // Contact information formatted for display
    public String getContactInfo() {
        return contactName + " - " + contactPosition;
    }
}
