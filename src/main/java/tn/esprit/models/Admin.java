package tn.esprit.models;

public class Admin extends Employe {

    public Admin() {
    }

    public Admin(int id, String telephone, String nom, String prenom, String sexe, String poste, String departement, String grade,
                 String adresse, double tarifJournalier, String niveau, int joursCongesRestants) {
        super(id, tarifJournalier, niveau, departement, adresse, poste, grade, sexe, prenom, nom, telephone, joursCongesRestants);
    }

    public Admin(String nom, String prenom, String sexe, String telephone, String poste, String departement, String grade,
                 String adresse, double tarifJournalier, String niveau, int joursCongesRestants) {
        super(0, tarifJournalier, niveau, departement, adresse, poste, grade, sexe, prenom, nom, telephone, joursCongesRestants);
    }
    @Override
    public String toString() {
        return "Admin{" +
                super.toString() +
                '}';
    }
}
