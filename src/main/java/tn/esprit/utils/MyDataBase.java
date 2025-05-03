package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private static MyDataBase instance;
    private final String URL = "jdbc:mysql://127.0.0.1:3306/ConnectHR";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private Connection cnx;

    private MyDataBase() {
        try {
            Class.forName(DRIVER_CLASS);
            cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connexion à la base de données établie avec succès !");
        } catch (ClassNotFoundException e) {
            System.err.println("ERREUR FATALE : Driver JDBC MySQL introuvable. Assurez-vous que le JAR du driver est dans le classpath.");
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("ERREUR FATALE : Impossible de se connecter à la base de données : " + e.getMessage());
            System.exit(1);
        }
    }

    public static synchronized MyDataBase getInstance() {
        if (instance == null)
            instance = new MyDataBase();
        return instance;
    }

    public Connection getCnx() {
        try {
            if (cnx == null || cnx.isClosed()) {
                System.err.println("ERREUR FATALE : La connexion à la base de données est fermée ou nulle.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.err.println("ERREUR FATALE : Erreur lors de la vérification de l'état de la connexion à la base de données : " + e.getMessage());
            System.exit(1);
        }
        return cnx;
    }
}