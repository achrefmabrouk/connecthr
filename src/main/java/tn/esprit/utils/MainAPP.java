package tn.esprit.utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainAPP extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Début lancement de l'application");
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/interfaceadmin.fxml"));
        System.out.println("FXML chargé");
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root );
            primaryStage.setScene(scene);
            primaryStage.setTitle("---- Gestion Personne -----");
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}