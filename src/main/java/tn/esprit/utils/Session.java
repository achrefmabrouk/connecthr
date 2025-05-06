package tn.esprit.utils;

import tn.esprit.models.Employe;

public class Session {

    private static Employe employeConnecte;

    public static void setEmployeConnecte(Employe emp) {
        employeConnecte = emp;
    }

    public static Employe getEmployeConnecte() {
        return employeConnecte;
    }

    public static void clear() {
        employeConnecte = null;
    }
}
