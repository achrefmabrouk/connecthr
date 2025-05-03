package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import tn.esprit.models.Attendance;
import tn.esprit.models.Personne;
import tn.esprit.services.ServiceAttendance;
import tn.esprit.services.ServicePersonne;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController implements Initializable {

    @FXML private PieChart departmentPieChart;
    @FXML private PieChart rolePieChart;
    @FXML private PieChart todayStatusPieChart;

    private final ServicePersonne servicePersonne = new ServicePersonne();
    private final ServiceAttendance serviceAttendance = new ServiceAttendance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialisation du contrôleur Dashboard...");
        loadDepartmentChartData();
        loadRoleChartData();
        loadTodayStatusChartData();
        System.out.println("Données du Dashboard chargées.");
    }

    private void loadDepartmentChartData() {
        try {
            List<Personne> personnes = servicePersonne.getAll();
            if (personnes == null || personnes.isEmpty()) {
                System.out.println("Aucune donnée de personnel pour le graphique Département.");
                departmentPieChart.setTitle("Aucune Donnée");
                return;
            }

            Map<String, Long> departmentCounts = personnes.stream()
                    .filter(p -> p.getDepartment() != null && !p.getDepartment().isEmpty())
                    .collect(Collectors.groupingBy(Personne::getDepartment, Collectors.counting()));

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            departmentCounts.forEach((department, count) ->
                    pieChartData.add(new PieChart.Data(department + " (" + count + ")", count))
            );

            departmentPieChart.setData(pieChartData);
            departmentPieChart.setTitle("Employés par Département");

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des données du graphique Département : " + e.getMessage());
            e.printStackTrace();
            departmentPieChart.setTitle("Erreur Chargement");
        }
    }

    private void loadRoleChartData() {
        try {
            List<Personne> personnes = servicePersonne.getAll();
            if (personnes == null || personnes.isEmpty()) {
                System.out.println("Aucune donnée de personnel pour le graphique Rôle.");
                rolePieChart.setTitle("Aucune Donnée");
                return;
            }

            Map<String, Long> roleCounts = personnes.stream()
                    .filter(p -> p.getRole() != null && !p.getRole().isEmpty())
                    .collect(Collectors.groupingBy(Personne::getRole, Collectors.counting()));

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            roleCounts.forEach((role, count) ->
                    pieChartData.add(new PieChart.Data(role + " (" + count + ")", count))
            );

            rolePieChart.setData(pieChartData);
            rolePieChart.setTitle("Employés par Rôle");

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des données du graphique Rôle : " + e.getMessage());
            e.printStackTrace();
            rolePieChart.setTitle("Erreur Chargement");
        }
    }

    private void loadTodayStatusChartData() {
        try {
            LocalDate today = LocalDate.now();
            List<Attendance> todayAttendance = serviceAttendance.getAllAttendanceForDate(today);

            if (todayAttendance == null || todayAttendance.isEmpty()) {
                System.out.println("Aucune donnée de présence pour aujourd'hui.");
                todayStatusPieChart.setTitle("Aucune Donnée Aujourd'hui");
                return;
            }

            Map<String, Long> statusCounts = todayAttendance.stream()
                    .filter(a -> a.getStatus() != null && !a.getStatus().isEmpty())
                    .collect(Collectors.groupingBy(Attendance::getStatus, Collectors.counting()));

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            long presentCount = statusCounts.getOrDefault("Present", 0L);
            long leaveCount = statusCounts.getOrDefault("Leave", 0L);
            long absentCount = statusCounts.getOrDefault("Absent", 0L);

            if (presentCount > 0) pieChartData.add(new PieChart.Data("Présent (" + presentCount + ")", presentCount));
            if (leaveCount > 0) pieChartData.add(new PieChart.Data("Congé (" + leaveCount + ")", leaveCount));
            if (absentCount > 0) pieChartData.add(new PieChart.Data("Absent (" + absentCount + ")", absentCount));

            if(pieChartData.isEmpty()){
                todayStatusPieChart.setTitle("Aucune Donnée Aujourd'hui");
            } else {
                todayStatusPieChart.setData(pieChartData);
                todayStatusPieChart.setTitle("Statut du " + today.toString());
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des données du graphique Statut Aujourd'hui : " + e.getMessage());
            e.printStackTrace();
            todayStatusPieChart.setTitle("Erreur Chargement");
        }
    }
}