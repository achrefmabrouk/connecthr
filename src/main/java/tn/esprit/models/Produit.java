package tn.esprit.models;

public class Produit {
    private int id_prod;
    private float prix_prod;
    private String categorie_prod;
    private String nom_prod;
    private int quantite_stockee;


    public Produit() {
    }

    public Produit(int id_prod, float prix_prod, String categorie_prod, String nom_prod, int quantite_stockee) {
        this.id_prod = id_prod;
        this.prix_prod = prix_prod;
        this.categorie_prod = categorie_prod;
        this.nom_prod = nom_prod;
        this.quantite_stockee = quantite_stockee;
    }

    public Produit(float prix_prod, String categorie_prod, String nom_prod, int quantite_stockee) {
        this.prix_prod = prix_prod;
        this.categorie_prod = categorie_prod;
        this.nom_prod = nom_prod;
        this.quantite_stockee = quantite_stockee;
    }

    public int getId_prod() {
        return id_prod;
    }

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public float getPrix_prod() {
        return prix_prod;
    }

    public void setPrix_prod(float prix_prod) {
        if (prix_prod>0)
            this.prix_prod = prix_prod;
    }

    public String getCategorie_prod() {
        return categorie_prod;
    }

    public void setCategorie_prod(String categorie_prod) {
        this.categorie_prod = categorie_prod;
    }

    public String getNom_prod() {
        return nom_prod;
    }

    public void setNom_prod(String nom_prod) {
        this.nom_prod = nom_prod;
    }

    public int getQuantite_stockee() {
        return quantite_stockee;
    }

    public void setQuantite_stockee(int quantite_stockee) {
        this.quantite_stockee = quantite_stockee;
    }

    @Override
    public String toString() {
        return "produits{" +
                "id_prod=" + id_prod +
                ", prix_prod=" + prix_prod +
                ", categorie_prod='" + categorie_prod + '\'' +
                ", nom_prod='" + nom_prod + '\'' +
                ", quantite_stockee=" + quantite_stockee +
                '}';
    }
}