package tn.esprit.models;
import java.time.LocalDate;

public class Facture {
    private int id_facture;
    private int numfac;
    private LocalDate date_emission;
    private LocalDate date_echeance;
    private float cout_unitaire;
    private float remise;
    private String statut ;
    private float tva ;
    private float montant_ht;
    private float montant_ttc ;
    private int id_commande;




    public Facture(){
    }

    public Facture(int id_facture,int numfac, LocalDate date_emission, LocalDate date_echeance, float cout_unitaire, float remise, String statut, float tva, float montant_ht, float montant_ttc, int id_commande) {
        this.id_facture = id_facture;
        this.numfac = numfac;
        this.date_emission = date_emission;
        this.date_echeance = date_echeance;
        this.cout_unitaire = cout_unitaire;
        this.remise = remise;
        this.statut = statut;
        this.tva = tva;
        this.montant_ht = montant_ht;
        this.montant_ttc = montant_ttc;
        this.id_commande = id_commande;


    }

    public Facture(int numfac ,LocalDate date_emission, LocalDate date_echeance, float cout_unitaire, float remise,  String statut, float tva, float montant_ht, float montant_ttc,int id_commande) {
        this.numfac = numfac;
        this.date_emission = date_emission;
        this.date_echeance = date_echeance;
        this.cout_unitaire = cout_unitaire;
        this.remise = remise;
        this.statut = statut;
        this.tva = tva;
        this.montant_ht = montant_ht;
        this.montant_ttc = montant_ttc;
        this.id_commande = id_commande;


    }

    public int getId_facture() {
        return id_facture;
    }

    public void setId_facture(int id_facture) {
        this.id_facture = id_facture;
    }

    public int getNumfac() {
        return numfac;
    }

    public void setNumfac(int numfac) {
        this.numfac = numfac;
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


    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public float getMontant_ttc() {
        return montant_ttc;
    }

    public void setMontant_ttc(float montant_ttc) {
        this.montant_ttc = montant_ttc;
    }

    public float getMontant_ht() {
        return montant_ht;
    }

    public void setMontant_ht(float montant_ht) {
        this.montant_ht = montant_ht;
    }

    public float getTva() {
        return tva;
    }

    public void setTva(float tva) {
        this.tva = tva;
    }

    public int getId_commande() {
        return id_commande;
    }

    public void setId_commande(int id_commande) {
        this.id_commande = id_commande;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "id_facture=" + id_facture +
                ", numfac=" + numfac +
                ", date_emission=" + date_emission +
                ", date_echeance=" + date_echeance +
                ", cout_unitaire=" + cout_unitaire +
                ", remise=" + remise +
                ", statut='" + statut + '\'' +
                ", tva=" + tva +
                ", montant_ht=" + montant_ht +
                ", montant_ttc=" + montant_ttc +
                ", id_commande=" + id_commande +
                '}';
    }
}