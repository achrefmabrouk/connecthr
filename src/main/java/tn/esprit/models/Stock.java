package tn.esprit.models;

public class Stock {

    private int id_prod;
    private String nom_prod,categorie,statut;
    private float prix,quantite;

    public Stock() {}


    public Stock(int id_prod, String nom_prod, String categorie, String statut, float prix, float quantite) {
        this.id_prod = id_prod;
        this.nom_prod = nom_prod;
        this.categorie = categorie;
        this.statut = statut;
        this.prix = prix;
        this.quantite = quantite;
    }

    public Stock(String nom_prod, String categorie, String statut, float prix, float quantite) {
        this.nom_prod = nom_prod;
        this.categorie = categorie;
        this.statut = statut;
        this.prix = prix;
        this.quantite = quantite;
    }

    public int getId_prod() {
        return id_prod;
    }

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public String getNom_prod() {
        return nom_prod;
    }

    public void setNom_prod(String nom_prod) {
        this.nom_prod = nom_prod;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public float getQuantite() {
        return quantite;
    }

    public void setQuantite(float quantite) {
        this.quantite = quantite;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "stock{" +
                "id_prod=" + id_prod +
                ", nom_prod='" + nom_prod + '\'' +
                ", categorie='" + categorie + '\'' +
                ", statut='" + statut + '\'' +
                ", prix=" + prix +
                ", quantite=" + quantite +
                '}';
    }
}
