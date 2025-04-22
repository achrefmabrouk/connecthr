package tn.esprit.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class connecthrDB {

    private static connecthrDB instance;
    private final String URL = "jdbc:mysql://127.0.0.1:3306/connecthr";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private Connection cnx;

    private connecthrDB() {
        try {
            cnx = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("Connected .....");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static connecthrDB getInstance() {
        if (instance == null)
            instance = new connecthrDB();

        return instance;

    }

    public Connection getCnx() {
        return cnx;
    }

}
