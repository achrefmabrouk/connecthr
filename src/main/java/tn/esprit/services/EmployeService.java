package tn.esprit.services;

import tn.esprit.interfaces.Icrud;
import tn.esprit.models.Employe;
import tn.esprit.utils.connecthrDB;

import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.*;


public class EmployeService implements Icrud<Employe> {
    private Connection cnx;
    private String grade_emp;
    private String depart_emp;
    private int id_emp;
    private static final String API_KEY = "158e0a5a9109eb527768ce11e43ca39e-a5eb7d5d-d004-46e6-942f-48d80f0f2698";
    private static final String BASE_URL = "https://9k2e8d.api.infobip.com/sms/2/text/advanced";



    public EmployeService(){
        cnx = connecthrDB.getInstance().getCnx();
        this.grade_emp = grade_emp;
        this.depart_emp = depart_emp;
    }

    public EmployeService(String grade_emp, String depart_emp) {
        cnx = connecthrDB.getInstance().getCnx();
        this.depart_emp = depart_emp;
        this.grade_emp = grade_emp;
    }

    public EmployeService(String grade_emp) {
        cnx = connecthrDB.getInstance().getCnx();
        this.grade_emp = grade_emp;
    }

    @Override
    public boolean add(Employe employe) {
        if (grade_emp.equals("admin") ||
                (grade_emp.equals("responsable") && employe.getDepartement().equals(depart_emp))) {

            String qry = "INSERT INTO employe(nom, prenom, sexe, telephone, poste, departement, grade, adresse, TarifJournalier, niveau, JoursCongesRestants) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement pstm = cnx.prepareStatement(qry);
                pstm.setString(1, employe.getNom());
                pstm.setString(2, employe.getPrenom());
                pstm.setString(3, employe.getSexe());
                pstm.setString(4, employe.getTelephone());
                pstm.setString(5, employe.getPoste());
                pstm.setString(6, employe.getDepartement());
                pstm.setString(7, employe.getGrade());
                pstm.setString(8, employe.getAdresse());
                pstm.setDouble(9, employe.getTarifJournalier());
                pstm.setString(10, employe.getNiveau());
                pstm.setInt(11, employe.getJoursCongesRestants());

                pstm.executeUpdate();
                return true;

            } catch (SQLException ex) {
                System.out.println("Erreur SQL : " + ex.getMessage());
                return false;
            }
        } else {
            System.out.println("Action non autorisée.");
            return false;
        }
    }

    @Override
    public List<Employe> getAll() {
        List<Employe> employes = new ArrayList<>();
        String qry;

        if (grade_emp.equals("admin")) {
            qry = "SELECT * FROM employe";
        } else if (grade_emp.equals("responsable")) {
            qry = "SELECT * FROM employe WHERE departement = ?";
        } else {
            qry = "SELECT * FROM employe WHERE id = ?";
        }

        try {
            PreparedStatement stm = cnx.prepareStatement(qry);
            if (grade_emp.equals("responsable")) {
                stm.setString(1, depart_emp);
            } else if (!grade_emp.equals("admin")) {
                stm.setInt(1, id_emp);
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employe emp = new Employe();
                emp.setId(rs.getInt("id"));
                emp.setNom(rs.getString("nom"));
                emp.setPrenom(rs.getString("prenom"));
                emp.setSexe(rs.getString("sexe"));
                emp.setTelephone(rs.getString("telephone"));
                emp.setPoste(rs.getString("poste"));
                emp.setDepartement(rs.getString("departement"));
                emp.setGrade(rs.getString("grade"));
                emp.setAdresse(rs.getString("adresse"));
                emp.setTarifJournalier(rs.getDouble("TarifJournalier"));
                emp.setNiveau(rs.getString("niveau"));
                emp.setJoursCongesRestants(rs.getInt("JoursCongesRestants"));
                emp.setImageSrc(rs.getString("ImageSrc"));

                employes.add(emp);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur SQL : " + ex.getMessage());
        }
        return employes;
    }

    public Employe getEmployeById(int id) {
        Employe emp = null;
        String qry;


        if (grade_emp.equals("admin")) {
            qry = "SELECT * FROM employe WHERE id = ?";
        } else if (grade_emp.equals("responsable")) {
            qry = "SELECT * FROM employe WHERE id = ? AND departement = ?";
        } else {
            qry = "SELECT * FROM employe WHERE id = ? AND id = ?";
        }

        try {
            PreparedStatement stm = cnx.prepareStatement(qry);
            if (grade_emp.equals("responsable")) {
                stm.setInt(1, id);
                stm.setString(2, depart_emp);
            } else if (!grade_emp.equals("admin")) {
                stm.setInt(1, id);
            } else {
                stm.setInt(1, id);
            }

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                emp = new Employe();
                emp.setId(rs.getInt("id"));
                emp.setNom(rs.getString("nom"));
                emp.setPrenom(rs.getString("prenom"));
                emp.setSexe(rs.getString("sexe"));
                emp.setTelephone(rs.getString("telephone"));
                emp.setPoste(rs.getString("poste"));
                emp.setDepartement(rs.getString("departement"));
                emp.setGrade(rs.getString("grade"));
                emp.setAdresse(rs.getString("adresse"));
                emp.setTarifJournalier(rs.getDouble("TarifJournalier"));
                emp.setNiveau(rs.getString("niveau"));
                emp.setJoursCongesRestants(rs.getInt("JoursCongesRestants"));
                emp.setImageSrc(rs.getString("ImageSrc"));
            }
        } catch (SQLException ex) {
            System.out.println("Erreur SQL : " + ex.getMessage());
        }

        return emp;
    }


    @Override
    public boolean update(Employe employe) {
        if (grade_emp.equals("admin") ||
                (grade_emp.equals("responsable") && employe.getDepartement().equals(depart_emp)) ||
                (!grade_emp.equals("admin") && !grade_emp.equals("responsable") && employe.getId() == id_emp)) {

            String qry = "UPDATE employe SET nom=?, prenom=?, sexe=?, telephone=?, poste=?, grade=?, departement=?, adresse=?, TarifJournalier=?, niveau=?, JoursCongesRestants=? WHERE id=?";

            try {
                PreparedStatement pstm = cnx.prepareStatement(qry);
                pstm.setString(1, employe.getNom());
                pstm.setString(2, employe.getPrenom());
                pstm.setString(3, employe.getSexe());
                pstm.setString(4, employe.getTelephone());
                pstm.setString(5, employe.getPoste());
                pstm.setString(6, employe.getGrade());
                pstm.setString(7, employe.getDepartement());
                pstm.setString(8, employe.getAdresse());
                pstm.setDouble(9, employe.getTarifJournalier());
                pstm.setString(10, employe.getNiveau());
                pstm.setInt(11, employe.getJoursCongesRestants());
                pstm.setInt(12, employe.getId());

                pstm.executeUpdate();
                return true;

            } catch (SQLException ex) {
                System.out.println("Erreur SQL : " + ex.getMessage());
                return false;
            }
        } else {
            System.out.println("Action non autorisée. Vous n'avez pas les droits.");
            return false;
        }
    }


    @Override
    public boolean delete(Employe employe) {
        if (grade_emp.equals("admin") ||
                (grade_emp.equals("responsable") && employe.getDepartement().equals(depart_emp))) {
            String qry = "DELETE FROM employe WHERE id=?";
            try {
                PreparedStatement pstm = cnx.prepareStatement(qry);
                pstm.setInt(1, employe.getId());
                pstm.executeUpdate();
                return true;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        } else {
            System.out.println("Action non autorisée. Vous n'avez pas les droits.");
            return false;
        }
    }


    public static String formatPhoneNumber(String phone) {
        // Vérifie si le numéro commence déjà par +216, sinon l'ajoute
        if (!phone.startsWith("+216")) {
            return "+216" + phone;
        }
        return phone;
    }


    public boolean doesPhoneExist(String phone) {
        String qry = "SELECT * FROM employe WHERE telephone = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(qry)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String generateVerificationCode() {
        Random rand = new Random();
        return String.format("%06d", rand.nextInt(1000000));
    }


    public boolean storeVerificationCode(String phone, String code) {
        String qry = "UPDATE employe SET verification_code = ? WHERE telephone = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(qry)) {
            stmt.setString(1, code);
            stmt.setString(2, phone);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean sendVerificationSms(String phone, String code) {
        String formattedPhone = formatPhoneNumber(phone);  // Formate le numéro avant de l'utiliser
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        String requestBody = "{"
                + "\"messages\":[{"
                + "\"destinations\":[{\"to\":\"" + formattedPhone + "\"}],"
                + "\"from\":\"ConnectHR\","
                + "\"text\":\"Votre code de vérification est : " + code + "\""
                + "}]"
                + "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url(BASE_URL)
                .method("POST", body)
                .addHeader("Authorization", "App " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean verifyCode(String phone, String inputCode) {
        String qry = "SELECT verification_code FROM employe WHERE telephone = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(qry)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("verification_code").equals(inputCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean updatePassword(String phone, String newPassword) {
        String qry = "UPDATE employe SET password = ? WHERE telephone = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(qry)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, phone);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
