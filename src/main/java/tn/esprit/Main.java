package tn.esprit;

import tn.esprit.models.Employe;
import tn.esprit.services.AuthService;
import tn.esprit.services.RegisterService;

public class Main {
    public static void main(String[] args) {
        // Tester l'authentification (login)
        AuthService authService = new AuthService();

        // Tester une connexion avec des informations valides
        Employe loginSuccess = authService.login("djebbi", "password123");
        System.out.println("Login success: " + loginSuccess);

        // Tester l'inscription (register)
        RegisterService registerService = new RegisterService();
        Employe newEmployee = new Employe();
        newEmployee.setNom("chamsi");
        newEmployee.setPrenom("amira");
        newEmployee.setAdresse("manouba");
        newEmployee.setPassword("password123");
        newEmployee.setPoste("Developer");
        newEmployee.setGrade("Junior");
        newEmployee.setDepartement("CRM");
        newEmployee.setSexe("femme");
        newEmployee.setNiveau("Junior");
        newEmployee.setTelephone(21436587);
        newEmployee.setTarifJournalier(45);
        newEmployee.setJoursCongesRestants(45);


        boolean registerSuccess = authService.register(newEmployee);
        System.out.println("Registration success: " + registerSuccess);

        // Tester l'authentification avec un mauvais mot de passe
        loginSuccess = authService.login("ch", "password123");
        System.out.println("Login success (with wrong password): " + loginSuccess);
    }
}
