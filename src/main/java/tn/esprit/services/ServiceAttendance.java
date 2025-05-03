package tn.esprit.services;

import tn.esprit.models.Attendance;
import tn.esprit.models.Personne;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class ServiceAttendance {

    private Connection cnx;
    private ServicePersonne servicePersonne;

    public ServiceAttendance() {
        this.cnx = MyDataBase.getInstance().getCnx();
        this.servicePersonne = new ServicePersonne();
    }

    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        return new Attendance(
                rs.getInt("AttendanceID"),
                rs.getInt("PersonneID"),
                rs.getDate("AttendanceDate").toLocalDate(),
                rs.getString("Status")
        );
    }

    public void recordOrUpdateAttendance(int personneId, LocalDate date, String status) {
        if (!List.of("Present", "Absent", "Leave").contains(status)) {
            System.err.println("Statut de présence invalide fourni : " + status);
            return;
        }

        String selectQry = "SELECT AttendanceID FROM attendance WHERE PersonneID = ? AND AttendanceDate = ?";
        String insertQry = "INSERT INTO attendance (PersonneID, AttendanceDate, Status) VALUES (?,?,?)";
        String updateQry = "UPDATE attendance SET Status = ? WHERE AttendanceID = ?";

        try {
            Integer existingAttendanceId = null;
            try (PreparedStatement selectPstm = cnx.prepareStatement(selectQry)) {
                selectPstm.setInt(1, personneId);
                selectPstm.setDate(2, Date.valueOf(date));
                try (ResultSet rs = selectPstm.executeQuery()) {
                    if (rs.next()) {
                        existingAttendanceId = rs.getInt("AttendanceID");
                    }
                }
            }

            if (existingAttendanceId != null) {
                try (PreparedStatement updatePstm = cnx.prepareStatement(updateQry)) {
                    updatePstm.setString(1, status);
                    updatePstm.setInt(2, existingAttendanceId);
                    int rowsAffected = updatePstm.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Enregistrement de présence mis à jour pour PersonneID " + personneId + " le " + date + " au statut " + status);
                    } else {
                        System.out.println("Enregistrement de présence trouvé mais la mise à jour a échoué pour PersonneID " + personneId + " le " + date);
                    }
                }
            } else {
                try (PreparedStatement insertPstm = cnx.prepareStatement(insertQry)) {
                    insertPstm.setInt(1, personneId);
                    insertPstm.setDate(2, Date.valueOf(date));
                    insertPstm.setString(3, status);
                    insertPstm.executeUpdate();
                    System.out.println("Nouvelle présence enregistrée pour PersonneID " + personneId + " le " + date + " avec le statut " + status);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement ou de la mise à jour de la présence pour PersonneID " + personneId + " le " + date + " : " + e.getMessage());
        }
    }

    public void ensureDailyAttendanceRecord(int personneId, String status) {
        LocalDate today = LocalDate.now();
        Attendance existing = getAttendanceForDate(personneId, today);
        if (existing == null) {
            System.out.println("Pointage : Aucun enregistrement de présence trouvé pour PersonneID " + personneId + " aujourd'hui ("+today+"). Enregistrement du statut : " + status);
            recordOrUpdateAttendance(personneId, today, status);
        } else {
            System.out.println("Pointage : Présence déjà enregistrée pour PersonneID " + personneId + " aujourd'hui ("+today+"). Statut : " + existing.getStatus());
        }
    }

    public void recordLeave(Personne personne, LocalDate date) {
        if (personne == null || personne.getId() <= 0) {
            System.err.println("Impossible d'enregistrer le congé : Objet Personne invalide fourni.");
            return;
        }

        if (personne.getRemainingLeaveDays() > 0) {
            recordOrUpdateAttendance(personne.getId(), date, "Leave");
            personne.setRemainingLeaveDays(personne.getRemainingLeaveDays() - 1);
            servicePersonne.update(personne);
            System.out.println("Congé enregistré pour " + personne.getPrenom() + " le " + date + ". Jours restants : " + personne.getRemainingLeaveDays());
        } else {
            System.out.println("Impossible d'enregistrer le congé payé pour " + personne.getPrenom() + " le " + date + ". Aucun jour de congé restant. Enregistrement comme 'Absent'.");
            recordOrUpdateAttendance(personne.getId(), date, "Absent");
        }
    }

    public double calculateMonthlySalary(int personneId, YearMonth yearMonth) {
        Personne personne = servicePersonne.getById(personneId);
        if (personne == null) {
            System.err.println("Impossible de calculer le salaire : Personne avec l'ID " + personneId + " introuvable.");
            return 0.0;
        }
        if (personne.getDailyRate() <= 0) {
            System.out.println("Avertissement : Personne " + personne.getPrenom() + " a un taux journalier de 0 ou moins. Le salaire sera de 0.");
            return 0.0;
        }

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        long paidDays = 0;

        String qry = "SELECT COUNT(*) FROM attendance WHERE PersonneID = ? AND (Status = 'Present' OR Status = 'Leave') AND AttendanceDate BETWEEN ? AND ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, personneId);
            pstm.setDate(2, Date.valueOf(startDate));
            pstm.setDate(3, Date.valueOf(endDate));

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    paidDays = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul des jours payés (Présent/Congé) pour PersonneID " + personneId + " en " + yearMonth + " : " + e.getMessage());
            return 0.0;
        }

        double salary = paidDays * personne.getDailyRate();
        System.out.println("Calcul du salaire pour " + personne.getPrenom() + " " + personne.getNom() + " (" + yearMonth + ") : "
                + paidDays + " jours payés (Présent+Congé) * €" + personne.getDailyRate() + "/jour = €" + String.format("%.2f", salary));
        return salary;
    }

    public List<Attendance> getAttendanceForPerson(int personneId) {
        List<Attendance> records = new ArrayList<>();
        String qry = "SELECT * FROM attendance WHERE PersonneID = ? ORDER BY AttendanceDate DESC";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, personneId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToAttendance(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des enregistrements de présence pour PersonneID " + personneId + " : " + e.getMessage());
        }
        return records;
    }

    public Attendance getAttendanceForDate(int personneId, LocalDate date) {
        String qry = "SELECT * FROM attendance WHERE PersonneID = ? AND AttendanceDate = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, personneId);
            pstm.setDate(2, Date.valueOf(date));
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAttendance(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la présence pour PersonneID " + personneId + " à la date " + date + " : " + e.getMessage());
        }
        return null;
    }

    public List<Attendance> getAllAttendanceForDate(LocalDate date) {
        List<Attendance> records = new ArrayList<>();
        String qry = "SELECT * FROM attendance WHERE AttendanceDate = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setDate(1, Date.valueOf(date));
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToAttendance(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de toutes les présences pour la date " + date + " : " + e.getMessage());
        }
        return records;
    }
}