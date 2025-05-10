package tn.esprit.connecthr.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import tn.esprit.connecthr.models.Commande;
import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.models.FournisseurPerformance;
import tn.esprit.connecthr.services.AnalyseService;
import tn.esprit.connecthr.services.CommandeService;
import tn.esprit.connecthr.services.FournisseurService;

/**
 * Contrôleur pour la vue de rapport et d'analyse des performances des fournisseurs
 */
public class RapportFournisseurController implements Initializable {
    @FXML
    private ComboBox<Fournisseur> comboFournisseur;
    
    public ComboBox<Fournisseur> getComboFournisseur() {
        return comboFournisseur;
    }
    
    @FXML
    private Button btnGenererRapport;
    
    @FXML
    private Label lblScoreGlobal;
    
    @FXML
    private Label lblLivraisonsATemps;
    
    @FXML
    private Label lblDelaiLivraison;
    
    @FXML
    private Label lblCommandesTotales;
    
    @FXML
    private Label lblMontantTotal;
    
    @FXML
    private Label lblMontantMoyen;
    
    @FXML
    private StackPane chartStatuts;
    
    @FXML
    private StackPane chartTendance;
    
    @FXML
    private TableView<Commande> tableCommandes;
    
    @FXML
    private TableColumn<Commande, String> colNumero;
    
    @FXML
    private TableColumn<Commande, LocalDate> colDate;
    
    @FXML
    private TableColumn<Commande, Double> colMontant;
    
    @FXML
    private TableColumn<Commande, String> colStatut;
    
    @FXML
    private TableColumn<Commande, LocalDate> colLivraison;
    
    private AnalyseService analyseService;
    private FournisseurService fournisseurService;
    private CommandeService commandeService;
    private ObservableList<Commande> commandesList = FXCollections.observableArrayList();
    
    public RapportFournisseurController() {
        analyseService = new AnalyseService();
        fournisseurService = new FournisseurService();
        commandeService = new CommandeService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser les colonnes du tableau
        setupTableColumns();
        
        // Configurer le ComboBox des fournisseurs
        setupFournisseurComboBox();
        
        // Configurer le bouton de génération de rapport
        btnGenererRapport.setOnAction(event -> {
            Fournisseur selectedFournisseur = comboFournisseur.getValue();
            if (selectedFournisseur != null) {
                try {
                    genererRapportFournisseur(selectedFournisseur.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la génération du rapport", 
                            "Une erreur est survenue lors de la génération du rapport: " + e.getMessage());
                }
            } else {
                showAlert(AlertType.WARNING, "Avertissement", "Aucun fournisseur sélectionné", 
                        "Veuillez sélectionner un fournisseur pour générer le rapport.");
            }
        });
    }
    
    /**
     * Configurer les colonnes du tableau des commandes
     */
    private void setupTableColumns() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroCommande"));
        
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        colDate.setCellFactory(column -> new TableCell<Commande, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });
        
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colMontant.setCellFactory(column -> new TableCell<Commande, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                if (empty || montant == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f TND", montant));
                }
                setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            }
        });
        
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colStatut.setCellFactory(column -> new TableCell<Commande, String>() {
            @Override
            protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(statut);
                    switch(statut) {
                        case "En attente":
                            setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404; -fx-padding: 5;");
                            break;
                        case "En cours":
                            setStyle("-fx-background-color: #cce5ff; -fx-text-fill: #004085; -fx-padding: 5;");
                            break;
                        case "Livrée":
                            setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-padding: 5;");
                            break;
                        case "Annulée":
                            setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-padding: 5;");
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });
        
        colLivraison.setCellValueFactory(new PropertyValueFactory<>("dateLivraison"));
        colLivraison.setCellFactory(column -> new TableCell<Commande, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });
        
        // Définir les données du tableau
        tableCommandes.setItems(commandesList);
    }
    
    /**
     * Configurer le ComboBox des fournisseurs
     */
    private void setupFournisseurComboBox() {
        try {
            List<Fournisseur> fournisseurs = fournisseurService.getAllFournisseurs();
            
            ObservableList<Fournisseur> fournisseursList = FXCollections.observableArrayList(fournisseurs);
            comboFournisseur.setItems(fournisseursList);
            
            // Configurer l'affichage du texte
            comboFournisseur.setCellFactory(param -> new javafx.scene.control.ListCell<Fournisseur>() {
                @Override
                protected void updateItem(Fournisseur item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNom());
                    }
                }
            });
            
            comboFournisseur.setButtonCell(new javafx.scene.control.ListCell<Fournisseur>() {
                @Override
                protected void updateItem(Fournisseur item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNom());
                    }
                }
            });
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de chargement", 
                    "Impossible de charger la liste des fournisseurs: " + e.getMessage());
        }
    }
    
    /**
     * Générer le rapport de performance pour un fournisseur
     * @param fournisseurId L'ID du fournisseur
     * @throws SQLException En cas d'erreur SQL
     */
    public void genererRapportFournisseur(int fournisseurId) throws SQLException {
        // Récupérer les données de performance
        FournisseurPerformance performance = analyseService.getFournisseurPerformance(fournisseurId);
        
        // Mettre à jour les libellés de statistiques
        lblScoreGlobal.setText(String.format("%.1f", performance.getScoreGlobal()));
        lblLivraisonsATemps.setText(performance.getCommandesLivreesATempsPourcentage() + "%");
        lblDelaiLivraison.setText(String.valueOf(performance.getDelaiMoyenLivraisonJours()));
        lblCommandesTotales.setText(String.valueOf(performance.getTotalCommandes()));
        lblMontantTotal.setText(String.format("%,.2f", performance.getMontantTotal()));
        lblMontantMoyen.setText(String.format("%,.2f", performance.getMontantMoyenCommande()));
        
        // Générer le graphique de répartition des statuts
        genererGraphiqueStatuts(performance);
        
        // Générer le graphique de tendance des commandes
        genererGraphiqueTendance(performance);
        
        // Charger les commandes récentes
        commandesList.clear();
        List<Commande> commandes = commandeService.getCommandesByFournisseur(fournisseurId);
        commandesList.addAll(commandes);
    }
    
    /**
     * Générer le graphique de répartition des statuts
     * @param performance Les données de performance du fournisseur
     */
    private void genererGraphiqueStatuts(FournisseurPerformance performance) {
        chartStatuts.getChildren().clear();
        
        Map<String, Integer> repartition = performance.getRepartitionParStatut();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        for (Map.Entry<String, Integer> entry : repartition.entrySet()) {
            if (entry.getValue() > 0) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }
        
        if (pieChartData.isEmpty()) {
            // Aucune donnée disponible
            Label emptyLabel = new Label("Aucune donnée disponible");
            chartStatuts.getChildren().add(emptyLabel);
            return;
        }
        
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setLegendSide(Side.RIGHT);
        pieChart.setLabelsVisible(true);
        pieChart.setTitle("Répartition des Commandes par Statut");
        
        chartStatuts.getChildren().add(pieChart);
    }
    
    /**
     * Générer le graphique de tendance des commandes
     * @param performance Les données de performance du fournisseur
     */
    private void genererGraphiqueTendance(FournisseurPerformance performance) {
        chartTendance.getChildren().clear();
        
        List<Map<String, Object>> tendance = performance.getTendanceCommandes();
        
        if (tendance.isEmpty()) {
            // Aucune donnée disponible
            Label emptyLabel = new Label("Aucune donnée disponible");
            chartTendance.getChildren().add(emptyLabel);
            return;
        }
        
        // Préparer les axes du graphique
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Montant (TND)");
        
        // Créer le graphique en lignes
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Tendance des Commandes");
        
        // Créer la série de données
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Montant des commandes");
        
        // Map pour regrouper les montants par mois
        Map<String, Double> montantParMois = new HashMap<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        
        // Traiter les données de tendance
        for (Map<String, Object> point : tendance) {
            LocalDate date = (LocalDate) point.get("date");
            Double montant = (Double) point.get("montant");
            
            String mois = date.format(monthFormatter);
            
            // Cumuler les montants par mois
            montantParMois.put(mois, montantParMois.getOrDefault(mois, 0.0) + montant);
        }
        
        // Ajouter les points de données au graphique
        for (Map.Entry<String, Double> entry : montantParMois.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        
        lineChart.getData().add(series);
        chartTendance.getChildren().add(lineChart);
    }
    
    /**
     * Afficher une alerte
     * @param type Le type d'alerte
     * @param title Le titre de l'alerte
     * @param header L'en-tête de l'alerte
     * @param content Le contenu de l'alerte
     */
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}