package tn.esprit.models;

import java.time.LocalDate;

public class Commandes_clients {
    private int id_commande;
    private int id_produit;
    private int quantite;
    private float prix;
    private LocalDate date_commande;
    private String etat_commande;
    private int id_facture;
    private int id_client;

    public Commandes_clients() {
    }

    public Commandes_clients(int id_commande, int id_produit, int quantite, float prix, LocalDate date_commande, String etat_commande, int id_facture, int id_client) {
        this.id_commande = id_commande;
        this.id_produit = id_produit;
        this.quantite = quantite;
        this.prix = prix;
        this.date_commande = date_commande;
        this.etat_commande = etat_commande;
        this.id_facture = id_facture;
        this.id_client = id_client;
    }

    public Commandes_clients(int id_produit, int quantite, float prix, LocalDate date_commande, String etat_commande, int id_facture, int id_client) {
        this.id_produit = id_produit;
        this.quantite = quantite;
        this.prix = prix;
        this.date_commande = date_commande;
        this.etat_commande = etat_commande;
        this.id_facture = id_facture;
        this.id_client = id_client;
    }

    public int getId_commande() {
        return id_commande;
    }

    public void setId_commande(int id_commande) {
        this.id_commande = id_commande;
    }

    public int getId_produit() {
        return id_produit;
    }

    public void setId_produit(int id_produit) {
        this.id_produit = id_produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public LocalDate getDate_commande() {
        return date_commande;
    }

    public void setDate_commande(LocalDate date_commande) {
        this.date_commande = date_commande;
    }

    public String getEtat_commande() {
        return etat_commande;
    }

    public void setEtat_commande(String etat_commande) {
        this.etat_commande = etat_commande;
    }

    public int getId_facture() {
        return id_facture;
    }

    public void setId_facture(int id_facture) {
        this.id_facture = id_facture;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }
}
