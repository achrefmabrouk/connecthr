package tn.esprit.models;

import java.time.LocalDate;

public class Attendance {
    private int attendanceId;
    private int personneId;
    private LocalDate attendanceDate;
    private String status;

    public Attendance() {
    }

    public Attendance(int personneId, LocalDate attendanceDate, String status) {
        this.personneId = personneId;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    public Attendance(int attendanceId, int personneId, LocalDate attendanceDate, String status) {
        this.attendanceId = attendanceId;
        this.personneId = personneId;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    public int getAttendanceId() { return attendanceId; }
    public int getPersonneId() { return personneId; }
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public String getStatus() { return status; }

    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }
    public void setPersonneId(int personneId) { this.personneId = personneId; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId" + attendanceId +
                ", personneId=" + personneId +
                ", attendanceDate=" + attendanceDate +
                ", status='" + status + '\'' +
                '}';
    }
}