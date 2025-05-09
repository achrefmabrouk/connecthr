package tn.esprit.models;

public class Employe {

    private int id, JoursCongesRestants;
    private String nom, prenom, sexe, poste, telephone, grade, adresse, departement, niveau, ImageSrc, password, verification_code;
    private double TarifJournalier;

    // Constructeur sans argument
    public Employe() {}

    // Constructeur avec l'ID et d'autres paramètres
    public Employe(int id, double tarifJournalier, String niveau, String departement, String adresse, String poste, String grade, String sexe, String prenom, String nom, String telephone, int joursCongesRestants) {
        this.id = id;
        this.TarifJournalier = tarifJournalier;
        this.niveau = niveau;
        this.departement = departement;
        this.adresse = adresse;
        this.poste = poste;
        this.grade = grade;
        this.sexe = sexe;
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
        this.JoursCongesRestants = joursCongesRestants;
    }

    // Constructeur sans ID (pour l'ajout d'un employé par exemple)
    public Employe(double tarifJournalier, String telephone, int joursCongesRestants, String nom, String prenom, String sexe, String poste, String grade, String adresse, String departement, String niveau) {
        this.TarifJournalier = tarifJournalier;
        this.telephone = telephone;
        this.JoursCongesRestants = joursCongesRestants;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.poste = poste;
        this.grade = grade;
        this.adresse = adresse;
        this.departement = departement;
        this.niveau = niveau;
    }

    // Getters et Setters
    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public double getTarifJournalier() {
        return TarifJournalier;
    }

    public void setTarifJournalier(double tarifJournalier) {
        this.TarifJournalier = tarifJournalier;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public int getJoursCongesRestants() {
        return JoursCongesRestants;
    }

    public void setJoursCongesRestants(int joursCongesRestants) {
        this.JoursCongesRestants = joursCongesRestants;
    }

    public String getImageSrc() {
        return ImageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.ImageSrc = imageSrc;
    }

    // Constructeur avec departement et grade (utilisé ailleurs dans ton code)
    public Employe(String departement, String grade) {
        this.departement = departement;
        this.grade = grade;
    }
}
