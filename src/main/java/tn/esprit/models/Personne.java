package tn.esprit.models;

public class Personne {
    private int id;
    private String nom;
    private String prenom;
    private String sex;
    private String tel;
    private String role;
    private String address;
    private String department;
    private double dailyRate;
    private int remainingLeaveDays;
    private String poste;

    public Personne() {
    }

    public Personne(String nom, String prenom, String sex, String tel, String role, String address, String department, double dailyRate, int remainingLeaveDays) {
        this.nom = nom;
        this.prenom = prenom;
        this.sex = sex;
        this.tel = tel;
        this.role = role;
        this.address = address;
        this.department = department;
        this.dailyRate = dailyRate;
        this.remainingLeaveDays = remainingLeaveDays;

    }

    public Personne(int id, String nom, String prenom, String sex, String tel, String role, String address, String department, double dailyRate, int remainingLeaveDays) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sex = sex;
        this.tel = tel;
        this.role = role;
        this.address = address;
        this.department = department;
        this.dailyRate = dailyRate;
        this.remainingLeaveDays = remainingLeaveDays;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getSex() { return sex; }
    public String getTel() { return tel; }
    public String getRole() { return role; }
    public String getAddress() { return address; }
    public String getDepartment() { return department; }
    public double getDailyRate() { return dailyRate; }
    public int getRemainingLeaveDays() { return remainingLeaveDays; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setSex(String sex) { this.sex = sex; }
    public void setTel(String tel) { this.tel = tel; }
    public void setRole(String role) { this.role = role; }
    public void setAddress(String address) { this.address = address; }
    public void setDepartment(String department) { this.department = department; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }
    public void setRemainingLeaveDays(int remainingLeaveDays) { this.remainingLeaveDays = remainingLeaveDays; }

    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", sex='" + sex + '\'' +
                ", tel='" + tel + '\'' +
                ", role='" + role + '\'' +
                ", addresse='" + address + '\'' +
                ", department='" + department + '\'' +
                ", Tarif journalier=" + dailyRate +
                ", jours de congé restants=" + remainingLeaveDays +
                '}';
    }
}