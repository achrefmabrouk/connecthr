package tn.esprit.connecthr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main application class
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/connecthr/views/main-view.fxml"));
            Parent root = loader.load();
            
            // Set up the scene
            Scene scene = new Scene(root);
            
            // Configure stage
            primaryStage.setTitle("ConnectHR ERP - Gestion des Fournisseurs");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            
            // Set application icon if available
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/tn/esprit/connecthr/images/app-icon.png")));
            } catch (Exception e) {
                System.out.println("Icône d'application non trouvée");
            }
            
            // Show the application
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Main method to launch the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}