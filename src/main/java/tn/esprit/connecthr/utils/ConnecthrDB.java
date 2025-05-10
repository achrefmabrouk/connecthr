package tn.esprit.connecthr.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class for database connection management
 */
public class ConnecthrDB {
    private static final String URL = "jdbc:mysql://localhost:3306/connecthr";
    private static final String USER = "root";
    private static final String PASSWORD = "toor";
    
    private static ConnecthrDB instance;
    private Connection connection;
    
    // Private constructor to prevent instantiation
    private ConnecthrDB() {
        try {
            // Register the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create the connection
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }
    
    /**
     * Get the singleton instance of the database connection
     * @return ConnecthrDB instance
     */
    public static synchronized ConnecthrDB getInstance() {
        if (instance == null) {
            instance = new ConnecthrDB();
        } else {
            try {
                // Verify if connection is still valid or closed
                if (instance.connection == null || instance.connection.isClosed()) {
                    instance = new ConnecthrDB();
                }
            } catch (SQLException e) {
                System.err.println("Error checking connection status: " + e.getMessage());
                instance = new ConnecthrDB();
            }
        }
        return instance;
    }
    
    /**
     * Get the database connection
     * @return Connection object
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Close the database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
