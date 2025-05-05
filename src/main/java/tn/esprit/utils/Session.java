package tn.esprit.utils;

import tn.esprit.models.employe;

public class Session {

    private static employe employeConnecte;

    public static void setEmployeConnecte(employe emp) {
        employeConnecte = emp;
    }

    public static employe getEmployeConnecte() {
        return employeConnecte;
    }

    public static void clear() {
        employeConnecte = null;
    }
}
