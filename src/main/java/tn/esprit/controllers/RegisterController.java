package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import tn.esprit.models.Employe;
import tn.esprit.services.AuthService;
import tn.esprit.utils.SceneLoader;

public class RegisterController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField adresseField;
    @FXML private TextField telephoneField;
    @FXML private TextField sexeField;
    @FXML private TextField posteField;
    @FXML private TextField niveauField;
    @FXML private TextField gradeField;
    @FXML private TextField departementField;
    @FXML private PasswordField passwordField;
    @FXML private TextField tarifField;
    @FXML private TextField joursCongesField;
    @FXML private Label errorLabel;
    @FXML private Button registerButton;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleRegister() {
        try {
            Employe emp = new Employe();
            emp.setNom(nomField.getText());
            emp.setPrenom(prenomField.getText());
            emp.setAdresse(adresseField.getText());
            emp.setPassword(passwordField.getText());
            emp.setGrade(gradeField.getText());
            emp.setDepartement(departementField.getText());
            emp.setTelephone(Integer.parseInt(telephoneField.getText()));
            emp.setSexe(sexeField.getText());
            emp.setPoste(posteField.getText());
            emp.setNiveau(niveauField.getText());
            emp.setTarifJournalier(Double.parseDouble(tarifField.getText()));
            emp.setJoursCongesRestants(Integer.parseInt(joursCongesField.getText()));
            emp.setImageSrc(null);

            boolean success = authService.register(emp);

            if (success) {

                Stage stage = (Stage) nomField.getScene().getWindow();
                Scene scene = SceneLoader.load("/Login.fxml");
                if (scene != null) stage.setScene(scene);
            } else {
                errorLabel.setText("Inscription échouée !");
                errorLabel.setVisible(true);
            }

        } catch (NumberFormatException e) {
            errorLabel.setText("Champs numériques invalides !");
            errorLabel.setVisible(true);
        } catch (Exception e) {
            errorLabel.setText("Erreur : " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleLoginRedirect() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        Scene scene = SceneLoader.load("/Login.fxml");
        if (scene != null) {
            stage.setScene(scene);
        }
    }
}
