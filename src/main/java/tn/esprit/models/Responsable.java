package tn.esprit.models;

public class Responsable extends Employe {

    public Responsable() {
    }

    public Responsable(int id, int telephone, String nom, String prenom, String sexe, String poste, String departement,
                       String grade, String adresse, double tarifJournalier, String niveau, int joursCongesRestants) {
        super(id, tarifJournalier, niveau, departement, adresse, poste, grade, sexe, prenom, nom, telephone, joursCongesRestants);
    }

    public Responsable(String nom, String prenom, String sexe, int telephone, String poste, String departement,
                       String grade, String adresse, double tarifJournalier, String niveau, int joursCongesRestants) {
        super(0, tarifJournalier, niveau, departement, adresse, poste, grade, sexe, prenom, nom, telephone, joursCongesRestants);
    }
    @Override
    public String toString() {
        return "Responsable{" +
                super.toString() +
                '}';
    }
}
