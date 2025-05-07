package tn.esprit.models;

import java.util.ArrayList;
import java.util.List;

;

public class Clients {



    private int id_client;
    private String nom_client;
    private String prenom_client;
    private int telephone_client;
    private List<Commandes_clients> commandes = new ArrayList<>();

    public Clients(String nom_client, String prenom_client, int telephone_client) {
        this.nom_client = nom_client;
        this.prenom_client = prenom_client;
        this.telephone_client = telephone_client;
    }

    public Clients(int id_client, String nom_client, String prenom_client, int telephone_client) {
        this.id_client = id_client;
        this.nom_client = nom_client;
        this.prenom_client = prenom_client;
        this.telephone_client = telephone_client;
    }

    public Clients() {}

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public String getNom_client() {
        return nom_client;
    }

    public void setNom_client(String nom_client) {
        this.nom_client = nom_client;
    }

    public String getPrenom_client() {
        return prenom_client;
    }

    public void setPrenom_client(String prenom_client) {
        this.prenom_client = prenom_client;
    }

    public void setTelephone_client(int telephone_client) {
        this.telephone_client = telephone_client;
    }

    public int getTelephone_client() {
        return telephone_client;
    }

    public List<Commandes_clients> getCommandes() {
        return commandes;
    }


    public void setCommandes(List<Commandes_clients> commandes) {
        this.commandes = commandes;
    }

    @Override
    public String toString() {
        return "Clients{" +
                "id_client=" + id_client +
                ", nom_client='" + nom_client + '\'' +
                ", prenom_client='" + prenom_client + '\'' +
                ", telephone_client=" + telephone_client +
                ", commandes=" + commandes +
                '}';
    }

}