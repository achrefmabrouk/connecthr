package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.models.Employe;
import tn.esprit.services.AuthService;
import tn.esprit.utils.SceneLoader; // Importation de SceneLoader

public class LoginController {

    @FXML private TextField nomField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleLogin() {
        String email = nomField.getText();
        String password = passwordField.getText();

        Employe emp = authService.login(email, password);
        if (emp != null) {
            String grade = emp.getGrade().toLowerCase();
            String dept = emp.getDepartement().toLowerCase();

            Stage stage = (Stage) nomField.getScene().getWindow();

            if ("admin".equals(grade)) {

                Scene scene = SceneLoader.load("/interfaceadmin.fxml");
                if (scene != null) {
                    stage.setScene(scene);
                }
            } else if ("responsable".equals(grade)) {

                Scene scene = SceneLoader.load("/interfaceadmin" + dept + ".fxml");
                if (scene != null) {
                    stage.setScene(scene);
                }
            } else {
                // Interface employé simple
                Scene scene = SceneLoader.load("/tn/esprit/views/Employe" + dept + ".fxml");
                if (scene != null) {
                    stage.setScene(scene);
                }
            }
        } else {
            errorLabel.setText("Email ou mot de passe incorrect !");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleRegisterRedirect() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        Scene scene = SceneLoader.load("/Register.fxml");
        if (scene != null) {
            stage.setScene(scene);
        }
    }

    @FXML
    void handleForgotPassword(ActionEvent event) {

        Stage stage = (Stage) nomField.getScene().getWindow();
        Scene scene = SceneLoader.load("/Oublie.fxml");
        if (scene != null) {
            stage.setScene(scene);
        }
    }
}
