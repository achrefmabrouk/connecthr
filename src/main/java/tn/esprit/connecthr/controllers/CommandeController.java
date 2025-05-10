package tn.esprit.connecthr.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.connecthr.models.Commande;
import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.services.CommandeService;
import tn.esprit.connecthr.services.FournisseurService;
import tn.esprit.connecthr.services.PaiementService;

/**
 * Controller for the order management view
 */
public class CommandeController implements Initializable {
    @FXML
    private TableView<Commande> tableCommandes;
    
    @FXML
    private TableColumn<Commande, String> colNumero;
    
    @FXML
    private TableColumn<Commande, String> colFournisseur;
    
    @FXML
    private TableColumn<Commande, LocalDate> colDateCommande;
    
    @FXML
    private TableColumn<Commande, LocalDate> colDateLivraison;
    
    @FXML
    private TableColumn<Commande, String> colStatut;
    
    @FXML
    private TableColumn<Commande, Double> colMontant;
    
    @FXML
    private TableColumn<Commande, Void> colActions;
    
    @FXML
    private Button btnAddCommande;
    
    @FXML
    private TextField txtSearchCommande;
    
    @FXML
    private Button btnSearchCommande;
    
    @FXML
    private ComboBox<Fournisseur> comboFournisseur;
    
    private CommandeService commandeService;
    private FournisseurService fournisseurService;
    private PaiementService paiementService;
    private ObservableList<Commande> commandesList = FXCollections.observableArrayList();
    
    public CommandeController() {
        commandeService = new CommandeService();
        fournisseurService = new FournisseurService();
        paiementService = new PaiementService();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize table columns
        setupTableColumns();
        
        // Set up event handlers
        btnAddCommande.setOnAction(this::handleAddCommande);
        btnSearchCommande.setOnAction(this::handleSearchCommande);
        
        // Initialize supplier filter combobox
        setupFournisseurComboBox();
        
        // Load data
        refreshCommandesList();
    }
    
    /**
     * Set up the table columns and their cell factories
     */
    private void setupTableColumns() {
        // Configure column cell value factories
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroCommande"));
        
        colFournisseur.setCellValueFactory(cellData -> {
            Commande commande = cellData.getValue();
            if (commande.getFournisseur() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> commande.getFournisseur().getNom()
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "N/A");
        });
        
        colDateCommande.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        colDateCommande.setCellFactory(column -> new TableCell<Commande, LocalDate>() {
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
        
        colDateLivraison.setCellValueFactory(new PropertyValueFactory<>("dateLivraison"));
        colDateLivraison.setCellFactory(column -> new TableCell<Commande, LocalDate>() {
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
        
        // Set column alignment
        colNumero.setStyle("-fx-alignment: CENTER-LEFT;");
        colFournisseur.setStyle("-fx-alignment: CENTER-LEFT;");
        colDateCommande.setStyle("-fx-alignment: CENTER;");
        colDateLivraison.setStyle("-fx-alignment: CENTER;");
        colStatut.setStyle("-fx-alignment: CENTER;");
        colMontant.setStyle("-fx-alignment: CENTER-RIGHT;");
        colActions.setStyle("-fx-alignment: CENTER;");
        
        // Add action buttons (View, Edit, Delete)
        setupActionsColumn();
        
        // Set items
        tableCommandes.setItems(commandesList);
    }
    
    /**
     * Set up the actions column with buttons
     */
    private void setupActionsColumn() {
        Callback<TableColumn<Commande, Void>, TableCell<Commande, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Commande, Void> call(final TableColumn<Commande, Void> param) {
                return new TableCell<>() {
                    private final Button viewBtn = new Button();
                    private final Button payBtn = new Button();
                    
                    {
                        // Configure view button with icon
                        viewBtn.getStyleClass().add("btn-icon");
                        viewBtn.setStyle("-fx-background-color: transparent;");
                        
                        // Create view icon
                        javafx.scene.image.ImageView viewIcon = new javafx.scene.image.ImageView(
                            getClass().getResource("/tn/esprit/connecthr/images/view.svg").toExternalForm()
                        );
                        viewIcon.setFitHeight(24);
                        viewIcon.setFitWidth(24);
                        viewBtn.setGraphic(viewIcon);
                        viewBtn.setTooltip(new javafx.scene.control.Tooltip("Voir les détails"));
                        
                        viewBtn.setOnAction(event -> {
                            Commande commande = getTableView().getItems().get(getIndex());
                            showCommandeDetails(commande);
                        });
                        
                        // Configure pay/edit button with icon
                        payBtn.getStyleClass().add("btn-icon");
                        payBtn.setStyle("-fx-background-color: transparent;");
                        
                        // Create edit icon
                        javafx.scene.image.ImageView editIcon = new javafx.scene.image.ImageView(
                            getClass().getResource("/tn/esprit/connecthr/images/edit.svg").toExternalForm()
                        );
                        editIcon.setFitHeight(24);
                        editIcon.setFitWidth(24);
                        payBtn.setGraphic(editIcon);
                        payBtn.setTooltip(new javafx.scene.control.Tooltip("Payer/Modifier"));
                        
                        payBtn.setOnAction(event -> {
                            Commande commande = getTableView().getItems().get(getIndex());
                            openAddPaiementDialog(commande);
                        });
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(10);
                            hbox.setAlignment(javafx.geometry.Pos.CENTER);
                            hbox.getChildren().addAll(viewBtn, payBtn);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };
        
        colActions.setCellFactory(cellFactory);
    }
    
    /**
     * Set up the supplier filter combobox
     */
    private void setupFournisseurComboBox() {
        try {
            List<Fournisseur> fournisseurs = fournisseurService.getAllFournisseurs();
            
            // Add an "All suppliers" option
            Fournisseur allFournisseurs = new Fournisseur();
            allFournisseurs.setId(-1);
            allFournisseurs.setNom("Tous les fournisseurs");
            
            ObservableList<Fournisseur> fournisseursList = FXCollections.observableArrayList();
            fournisseursList.add(allFournisseurs);
            fournisseursList.addAll(fournisseurs);
            
            comboFournisseur.setItems(fournisseursList);
            comboFournisseur.getSelectionModel().selectFirst();
            
            // Configure display text
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
            
            // Add change listener
            comboFournisseur.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    filterOrdersBySupplier(newVal);
                }
            });
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de chargement", 
                    "Impossible de charger la liste des fournisseurs: " + e.getMessage());
        }
    }
    
    /**
     * Filter orders by selected supplier
     * @param fournisseur The selected supplier
     */
    private void filterOrdersBySupplier(Fournisseur fournisseur) {
        try {
            if (fournisseur.getId() == -1) {
                // "All suppliers" option selected
                refreshCommandesList();
            } else {
                List<Commande> commandesFournisseur = commandeService.getCommandesByFournisseur(fournisseur.getId());
                commandesList.clear();
                commandesList.addAll(commandesFournisseur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de filtrage", 
                    "Impossible de filtrer les commandes: " + e.getMessage());
        }
    }
    
    /**
     * Refresh the list of orders
     */
    public void refreshCommandesList() {
        try {
            // Clear and reload orders
            commandesList.clear();
            List<Commande> commandes = commandeService.getAllCommandes();
            commandesList.addAll(commandes);
            
            if (commandes.isEmpty()) {
                // Show empty message in the table
                tableCommandes.setPlaceholder(new javafx.scene.control.Label("Aucune commande trouvée"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de chargement", 
                    "Impossible de charger les commandes: " + e.getMessage());
        }
    }
    
    /**
     * Handle add order button click
     * @param event The action event
     */
    @FXML
    private void handleAddCommande(ActionEvent event) {
        try {
            // Load the add order dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/connecthr/views/add-commande.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter une commande");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            // Set the scene
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            
            // Get the controller and set callback for refreshing the list
            Object controller = loader.getController();
            if (controller instanceof javafx.fxml.Initializable) {
                if (controller.getClass().getSimpleName().equals("AddCommandeController")) {
                    java.lang.reflect.Method method = controller.getClass().getMethod("setParentController", CommandeController.class);
                    method.invoke(controller, this);
                }
            }
            
            // Show the dialog
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire", 
                    "Une erreur est survenue lors de l'ouverture du formulaire d'ajout: " + e.getMessage());
        }
    }
    
    /**
     * Handle search order button click
     * @param event The action event
     */
    @FXML
    private void handleSearchCommande(ActionEvent event) {
        String searchTerm = txtSearchCommande.getText().trim();
        
        if (searchTerm.isEmpty()) {
            refreshCommandesList();
            return;
        }
        
        try {
            // Search orders
            List<Commande> commandes = commandeService.searchCommandes(searchTerm);
            
            // Update the table
            commandesList.clear();
            commandesList.addAll(commandes);
            
            if (commandes.isEmpty()) {
                // Show empty search results message
                tableCommandes.setPlaceholder(new javafx.scene.control.Label("Aucun résultat pour \"" + searchTerm + "\""));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de recherche", 
                    "Une erreur est survenue lors de la recherche: " + e.getMessage());
        }
    }
    
    /**
     * Handle delete order
     * @param commande The order to delete
     */
    private void handleDeleteCommande(Commande commande) {
        try {
            // Show confirmation dialog
            Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation de suppression");
            confirmDialog.setHeaderText("Supprimer la commande");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer la commande " + commande.getNumeroCommande() + "?");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    try {
                        // Delete the order
                        boolean success = commandeService.deleteCommande(commande.getId());
                        
                        if (success) {
                            // Refresh the list
                            refreshCommandesList();
                            
                            // Show success message
                            showAlert(AlertType.INFORMATION, "Succès", "Commande supprimée", 
                                    "La commande a été supprimée avec succès.");
                        } else {
                            showAlert(AlertType.ERROR, "Erreur", "Erreur de suppression", 
                                    "Impossible de supprimer la commande.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert(AlertType.ERROR, "Erreur", "Erreur de suppression", 
                                "Une erreur est survenue lors de la suppression: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de suppression", 
                    "Une erreur est survenue: " + e.getMessage());
        }
    }
    
    /**
     * Show order details
     * @param commande The order to show details for
     */
    private void showCommandeDetails(Commande commande) {
        try {
            // Show order details
            Alert detailsAlert = new Alert(AlertType.INFORMATION);
            detailsAlert.setTitle("Détails de la commande");
            detailsAlert.setHeaderText("Commande: " + commande.getNumeroCommande());
            
            String fournisseurName = commande.getFournisseur() != null ? commande.getFournisseur().getNom() : "N/A";
            
            // Get payment info
            double totalPaid = paiementService.getTotalPaidForCommande(commande.getId());
            double remaining = commande.getMontantTotal() - totalPaid;
            
            String content = "Fournisseur: " + fournisseurName + "\n" +
                            "Date de commande: " + (commande.getDateCommande() != null ? commande.getDateCommande().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A") + "\n" +
                            "Date de livraison: " + (commande.getDateLivraison() != null ? commande.getDateLivraison().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A") + "\n" +
                            "Statut: " + commande.getStatut() + "\n" +
                            "Montant total: " + String.format("%,.2f TND", commande.getMontantTotal()) + "\n" +
                            "Montant payé: " + String.format("%,.2f TND", totalPaid) + "\n" +
                            "Reste à payer: " + String.format("%,.2f TND", remaining) + "\n\n" +
                            "Description: " + commande.getDescription();
            
            detailsAlert.setContentText(content);
            detailsAlert.showAndWait();
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur d'affichage", 
                    "Une erreur est survenue lors de l'affichage des détails: " + e.getMessage());
        }
    }
    
    /**
     * Open add payment dialog
     * @param commande The order to add payment for
     */
    private void openAddPaiementDialog(Commande commande) {
        try {
            // Load the add payment dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/connecthr/views/add-paiement.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un paiement");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            // Set the scene
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            
            // Get the controller and set the order
            Object controller = loader.getController();
            if (controller instanceof javafx.fxml.Initializable) {
                if (controller.getClass().getSimpleName().equals("AddPaiementController")) {
                    java.lang.reflect.Method setCommandeMethod = controller.getClass().getMethod("setCommande", Commande.class);
                    setCommandeMethod.invoke(controller, commande);
                    
                    java.lang.reflect.Method setParentControllerMethod = controller.getClass().getMethod("setParentController", CommandeController.class);
                    setParentControllerMethod.invoke(controller, this);
                }
            }
            
            // Show the dialog
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire", 
                    "Une erreur est survenue lors de l'ouverture du formulaire d'ajout de paiement: " + e.getMessage());
        }
    }
    
    /**
     * Show an alert dialog
     * @param type Alert type
     * @param title Alert title
     * @param header Alert header
     * @param content Alert content
     */
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
