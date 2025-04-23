package tn.esprit.models;
import java.time.LocalDate;

public class facture {
    private int id_facture;
    private LocalDate date_emission,date_echeance;
    private float cout_unitaire,remise;
    private String mode_paiement,statut;

    public facture(){
    }

    public facture(int id_facture, LocalDate date_emission, LocalDate date_echeance, float cout_unitaire, float remise, String mode_paiement, String statut) {
        this.id_facture = id_facture;
        this.date_emission = date_emission;
        this.date_echeance = date_echeance;
        this.cout_unitaire = cout_unitaire;
        this.remise = remise;
        this.mode_paiement = mode_paiement;
        this.statut = statut;
    }

    public facture(LocalDate date_emission, LocalDate date_echeance, float cout_unitaire, float remise, String mode_paiement, String statut) {
        this.date_emission = date_emission;
        this.date_echeance = date_echeance;
        this.cout_unitaire = cout_unitaire;
        this.remise = remise;
        this.mode_paiement = mode_paiement;
        this.statut = statut;
    }

    public int getId_facture() {
        return id_facture;
    }

    public void setId_facture(int id_facture) {
        this.id_facture = id_facture;
    }

    public LocalDate  getDate_emission() {
        return date_emission;
    }

    public void setDate_emission( LocalDate date_emission) {
        this.date_emission = date_emission;
    }

    public LocalDate getDate_echeance() {
        return date_echeance;
    }

    public void setDate_echeance(LocalDate date_echeance) {
        this.date_echeance = date_echeance;
    }

    public float getCout_unitaire() {
        return cout_unitaire;
    }

    public void setCout_unitaire(float cout_unitaire) {
        this.cout_unitaire = cout_unitaire;
    }

    public float getRemise() {
        return remise;
    }

    public void setRemise(float remise) {
        this.remise = remise;
    }

    public String getMode_paiement() {
        return mode_paiement;
    }

    public void setMode_paiement(String mode_paiement) {
        this.mode_paiement = mode_paiement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "facture{" +
                "id_facture=" + id_facture +
                ", date_emission=" + date_emission +
                ", date_echeance=" + date_echeance +
                ", cout_unitaire=" + cout_unitaire +
                ", remise=" + remise +
                ", mode_paiement='" + mode_paiement + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}
