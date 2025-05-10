package tn.esprit.connecthr.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe singleton pour la gestion de connexion à la base de données
 */
public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    
    // Paramètres de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/erp_db";
    private static final String USER = "root";
    private static final String PASSWORD = "toor";
    
    /**
     * Constructeur privé pour le singleton
     */
    private DBConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base de données établie avec succès");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données: " + e.getMessage());
            throw new SQLException("Erreur de connexion à la base de données", e);
        }
    }
    
    /**
     * Obtenir l'instance unique de la connexion
     * @return L'instance de DBConnection
     * @throws SQLException En cas d'erreur de connexion
     */
    public static synchronized DBConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    /**
     * Obtenir la connexion à la base de données
     * @return L'objet Connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Fermer la connexion à la base de données
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion à la base de données fermée avec succès");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
}