package tn.esprit.services;
import tn.esprit.interfaces.Icrud;
import tn.esprit.models.Produit;
import tn.esprit.utils.connecthrDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements Icrud<Produit> {
    private Connection cnx  ;
    public ProduitService(){
        cnx = connecthrDB.getInstance().getCnx();
    }

    public Produit getByNomAndCategorie(String nom, String categorie) {
        String qry = "SELECT * FROM produits WHERE nom_prod = ? AND categorie_prod = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, nom);
            pstm.setString(2, categorie);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Produit p = new Produit();
                p.setId_prod(rs.getInt("id_prod"));
                p.setNom_prod(rs.getString("nom_prod"));
                p.setCategorie_prod(rs.getString("categorie_prod"));
                p.setPrix_prod(rs.getFloat("prix_prod"));
                p.setQuantite_stockee(rs.getInt("quantite_stockee"));
                return p;
            }

        } catch (SQLException ex) {
            System.out.println("Erreur dans getByNomAndCategorie : " + ex.getMessage());
        }

        return null;
    }

    @Override
    public boolean add(Produit p) {
        if (getByNomAndCategorie(p.getNom_prod(),p.getCategorie_prod()) == null) {
            String qry = "INSERT INTO  produits ( prix_prod, categorie_prod, nom_prod, quantite_stockee) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
                pstm.setFloat(1, p.getPrix_prod());
                pstm.setString(2, p.getCategorie_prod());
                pstm.setString(3, p.getNom_prod());
                pstm.setInt(4, p.getQuantite_stockee());

                pstm.executeUpdate();
                return true;

            } catch (SQLException ex) {
                System.out.println("Erreur lors de l'ajout : " + ex.getMessage());
                return false;
            }
        }
        else return false;
    }

    @Override
    public List<Produit> getAll() {
        List<Produit> produitList = new ArrayList<>();
        String qry = "SELECT * FROM produits";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Produit p = new Produit();
                p.setId_prod(rs.getInt("id_prod"));
                p.setPrix_prod(rs.getFloat("prix_prod"));
                p.setCategorie_prod(rs.getString("categorie_prod"));
                p.setNom_prod(rs.getString("nom_prod"));
                p.setQuantite_stockee(rs.getInt("quantite_stockee"));

                produitList.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération : " + ex.getMessage());
        }

        return produitList;
    }

    @Override
    public boolean update(Produit p) {
        String qry = "UPDATE produits SET prix_prod=?, categorie_prod=?, nom_prod=?, quantite_stockee=? WHERE id_prod=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setFloat(1, p.getPrix_prod());
            pstm.setString(2, p.getCategorie_prod());
            pstm.setString(3, p.getNom_prod());
            pstm.setInt(4, p.getQuantite_stockee());
            pstm.setInt(5, p.getId_prod());


            return pstm.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Erreur lors de la mise à jour : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Produit p) {
        String qry = "DELETE FROM produits WHERE id_prod=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, p.getId_prod());
            pstm.executeUpdate();
            return true;

        }
        catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression : " + ex.getMessage());
            return false;
        }
    }

    public Produit getById(int id) {
        String qry = "SELECT * FROM produits WHERE id_prod=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Produit p = new Produit();
                p.setId_prod(rs.getInt("id_prod"));
                p.setNom_prod(rs.getString("nom_prod"));
                p.setCategorie_prod(rs.getString("categorie_prod"));
                p.setPrix_prod(rs.getFloat("prix_prod"));
                p.setQuantite_stockee(rs.getInt("quantite_stockee"));
                return p;
            }

        } catch (SQLException ex) {
            System.out.println("Erreur dans getById : " + ex.getMessage());
        }

        return null;
    }

    public boolean verifierQuantiteDisponible(int id_prod, int quantiteDemandee) {
        String qry = "SELECT quantite_stockee FROM produits WHERE id_prod=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id_prod);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                int quantiteActuelle = rs.getInt("quantite_stockee");
                return quantiteActuelle >= quantiteDemandee;
            }

        } catch (SQLException ex) {
            System.out.println("Erreur dans verifierQuantiteDisponible : " + ex.getMessage());
        }
        return false;
    }

    public void retirerQuantite(int id_prod, int quantiteRetirer) {
        String selectQry = "SELECT quantite_stockee FROM produits WHERE id_prod=?";
        String updateQry = "UPDATE produits SET quantite_stockee=? WHERE id_prod=?";

        try (PreparedStatement selectStmt = cnx.prepareStatement(selectQry);
             PreparedStatement updateStmt = cnx.prepareStatement(updateQry)) {

            selectStmt.setInt(1, id_prod);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int quantiteActuelle = rs.getInt("quantite_stockee");
                int nouvelleQuantite = quantiteActuelle - quantiteRetirer;

                if (nouvelleQuantite < 0) {
                    System.out.println("Quantité insuffisante !");
                }
                updateStmt.setInt(1, nouvelleQuantite);
                updateStmt.setInt(2, id_prod);
                updateStmt.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Erreur dans retirerQuantite : " + ex.getMessage());
        }
    }
    public void ajouterQuantite(int id_prod, int quantiteAjouter) {
        String selectQry = "SELECT quantite_stockee FROM produits WHERE id_prod=?";
        String updateQry = "UPDATE produits SET quantite_stockee=? WHERE id_prod=?";

        try (PreparedStatement selectStmt = cnx.prepareStatement(selectQry);
             PreparedStatement updateStmt = cnx.prepareStatement(updateQry)) {

            selectStmt.setInt(1, id_prod);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int quantiteActuelle = rs.getInt("quantite_stockee");
                int nouvelleQuantite = quantiteActuelle + quantiteAjouter;

                updateStmt.setInt(1, nouvelleQuantite);
                updateStmt.setInt(2, id_prod);
                updateStmt.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Erreur dans ajouterQuantite : " + ex.getMessage());
        }
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String qry = "SELECT DISTINCT categorie_prod FROM produits";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                categories.add(rs.getString("categorie_prod"));
            }

        } catch (SQLException ex) {
            System.out.println("Erreur dans getCategories : " + ex.getMessage());
        }

        return categories;
    }


    public List<Produit> getProductsByCategory(Object newValue) {
        List<Produit> produits = new ArrayList<>();

        if (newValue == null) {
            return produits; // retourne une liste vide si aucune catégorie n'est sélectionnée
        }

        String categorie = newValue.toString(); // conversion en String
        String qry = "SELECT * FROM produits WHERE categorie_prod = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, categorie);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Produit p = new Produit();
                p.setId_prod(rs.getInt("id_prod"));
                p.setNom_prod(rs.getString("nom_prod"));
                p.setCategorie_prod(rs.getString("categorie_prod"));
                p.setPrix_prod(rs.getFloat("prix_prod"));
                p.setQuantite_stockee(rs.getInt("quantite_stockee"));
                produits.add(p);
            }

        } catch (SQLException ex) {
            System.out.println("Erreur dans getProductsByCategory : " + ex.getMessage());
        }

        return produits;
    }
    public List<String> getProductsName(Object newValue) {
        List<String> productNames = new ArrayList<>();

        if (newValue == null) {
            return productNames; // retourne une liste vide si la catégorie est nulle
        }

        String categorie = newValue.toString();
        String qry = "SELECT nom_prod FROM produits WHERE categorie_prod = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, categorie);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                productNames.add(rs.getString("nom_prod"));
            }

        } catch (SQLException ex) {
            System.out.println("Erreur dans getProductsName : " + ex.getMessage());
        }

        return productNames;
    }

    public String getCategorieByNom(String nomProduit) {
        String qry = "SELECT categorie_prod FROM produits WHERE nom_prod = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, nomProduit);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return rs.getString("categorie_prod"); // Retourne la catégorie du produit
            }

        } catch (SQLException ex) {
            System.out.println("Erreur dans getCategorieByNom : " + ex.getMessage());
        }

        return null; // Retourne null si aucun produit n'est trouvé avec ce nom
    }
}
