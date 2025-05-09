package tn.esprit.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.services.EmployeService;
import tn.esprit.utils.SceneLoader;

import java.io.IOException;


public class Oubile {

    @FXML private TextField phoneField;
    @FXML private TextField codeField;
    @FXML private TextField newPasswordField;

    private final EmployeService employeService = new EmployeService();
    private String generatedCode;

    public void requestReset() {
        String phone = phoneField.getText();
        if (employeService.doesPhoneExist(phone)) {
            generatedCode = employeService.generateVerificationCode();
            employeService.storeVerificationCode(phone, generatedCode);
            employeService.sendVerificationSms(phone, generatedCode);
            showAlert("✅ Code envoyé !");
        } else {
            showAlert("❌ Numéro non trouvé !");
        }
    }

    public void verifyCode() {
        String phone = phoneField.getText();
        String code = codeField.getText();
        if (employeService.verifyCode(phone, code)) {
            showAlert("✅ Code correct, entrez un nouveau mot de passe.");
        } else {
            showAlert("❌ Code invalide !");
        }
    }

    public void updatePassword() {
        String phone = phoneField.getText();
        String newPassword = newPasswordField.getText();
        if (employeService.updatePassword(phone, newPassword)) {
            showAlert("✅ Mot de passe mis à jour !");
        } else {
            showAlert("❌ Erreur de mise à jour !");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) phoneField.getScene().getWindow();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println(e.getMessage());
            showAlert("Erreur de chargement de la scène !");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }




}
