package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Personne;
import tn.esprit.services.ServicePersonne;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffManagementController implements Initializable {

    @FXML private TableView<Personne> staffTableView;
    @FXML private TableColumn<Personne, Integer> colId;
    @FXML private TableColumn<Personne, String> colNom;
    @FXML private TableColumn<Personne, String> colPrenom;
    @FXML private TableColumn<Personne, String> colSex;
    @FXML private TableColumn<Personne, String> colTelephone;
    @FXML private TableColumn<Personne, String> colRole;
    @FXML private TableColumn<Personne, String> colDepartment;
    @FXML private TableColumn<Personne, String> colAddress;

    @FXML private TextField searchField;
    @FXML private Button addButton;
    @FXML private Button refreshButton;
    @FXML private Button searchButton;
    @FXML private ImageView profileImageView;
    @FXML private VBox mainContentArea;
    @FXML private VBox sidebarVBox;
    @FXML private Button dashboardButton;
    @FXML private Button staffButton;
    @FXML private Button logoutButton;

    private final ServicePersonne servicePersonne = new ServicePersonne();
    private ObservableList<Personne> staffList = FXCollections.observableArrayList();
    private FilteredList<Personne> filteredData;
    private List<Node> staffListContentNodes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staffListContentNodes = new ArrayList<>(mainContentArea.getChildren());
        setupTableColumns();
        setupFiltering();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        staffTableView.setRowFactory(tv -> {
            TableRow<Personne> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Personne clickedPersonne = row.getItem();
                    handleEditStaff(clickedPersonne);
                }
            });
            return row;
        });
        loadStaffData();
        updateSidebarSelection(staffButton);
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void setupFiltering() {
        filteredData = new FilteredList<>(staffList, p -> true);
        if(staffTableView != null) {
            staffTableView.setItems(filteredData);
        } else {
            System.err.println("Erreur");
        }
    }

    private void loadStaffData() {
        staffList.setAll(servicePersonne.getAll());
        applyFilter();
    }

    private void applyFilter() {
        if (filteredData == null) return;
        String filterText = searchField.getText() == null ? "" : searchField.getText().toLowerCase().trim();
        filteredData.setPredicate(personne -> {
            if (filterText.isEmpty()) return true;
            String nom = personne.getNom() == null ? "" : personne.getNom().toLowerCase();
            String prenom = personne.getPrenom() == null ? "" : personne.getPrenom().toLowerCase();
            String role = personne.getRole() == null ? "" : personne.getRole().toLowerCase();
            String department = personne.getDepartment() == null ? "" : personne.getDepartment().toLowerCase();
            String tel = personne.getTel() == null ? "" : personne.getTel().toLowerCase();
            String idStr = String.valueOf(personne.getId());
            return nom.contains(filterText) || prenom.contains(filterText) || role.contains(filterText) ||
                    department.contains(filterText) || tel.contains(filterText) || idStr.contains(filterText);
        });
    }

    @FXML
    void handleShowDashboard(ActionEvent event) {
        updateSidebarSelection(dashboardButton);
        try {
            Node dashboardView = FXMLLoader.load(getClass().getResource("/tn/esprit/views/DashboardView.fxml"));
            mainContentArea.getChildren().setAll(dashboardView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleShowStaffList(ActionEvent event) {
        updateSidebarSelection(staffButton);
        showListViewContent();
    }


    @FXML
    void handleAddStaff(ActionEvent event) {
        openStaffForm(null);
    }

    void handleEditStaff(Personne personne) {
        if (personne == null) return;
        openStaffForm(personne);
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        if (!mainContentArea.getChildren().equals(staffListContentNodes)) {
            showListViewContent();
        }
        searchField.clear();
        loadStaffData();
    }

    @FXML
    void handleSearch(ActionEvent event) {
        applyFilter();
    }

    @FXML
    void handleLogout(ActionEvent event) {
    }

    private void openStaffForm(Personne personneToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/views/StaffFormView.fxml"));
            Parent formRoot = loader.load();

            StaffFormController controller = loader.getController();
            controller.setServicePersonne(servicePersonne);
            controller.setStaffManagementController(this);

            if (personneToEdit != null) {
                controller.loadPersonneData(personneToEdit);
            }

            mainContentArea.getChildren().setAll(formRoot);
            updateSidebarSelection(null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListViewContent() {
        mainContentArea.getChildren().setAll(staffListContentNodes);
        updateSidebarSelection(staffButton);
    }

    public void refreshData() {
        if (!mainContentArea.getChildren().equals(staffListContentNodes)) {
            staffList.setAll(servicePersonne.getAll());
        } else {
            loadStaffData();
        }
    }

    private void updateSidebarSelection(Button selectedButton) {
        for (Node node : sidebarVBox.getChildren()) {
            if (node instanceof Button) {
                node.getStyleClass().remove("active");
            }
        }
        if (selectedButton != null) {
            selectedButton.getStyleClass().add("active");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}