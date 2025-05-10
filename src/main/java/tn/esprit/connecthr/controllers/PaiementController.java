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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import tn.esprit.connecthr.models.Commande;
import tn.esprit.connecthr.models.Fournisseur;
import tn.esprit.connecthr.models.Paiement;
import tn.esprit.connecthr.services.CommandeService;
import tn.esprit.connecthr.services.FournisseurService;
import tn.esprit.connecthr.services.PaiementService;

/**
 * Controller for the payment management view
 */
public class PaiementController implements Initializable {
    @FXML
    private TableView<Paiement> tablePaiements;
    
    @FXML
    private TableColumn<Paiement, String> colNumero;
    
    @FXML
    private TableColumn<Paiement, String> colCommande;
    
    @FXML
    private TableColumn<Paiement, String> colFournisseur;
    
    @FXML
    private TableColumn<Paiement, LocalDate> colDatePaiement;
    
    @FXML
    private TableColumn<Paiement, Double> colMontant;
    
    @FXML
    private TableColumn<Paiement, String> colMethode;
    
    @FXML
    private TableColumn<Paiement, String> colStatut;
    
    @FXML
    private TableColumn<Paiement, String> colReference;
    
    @FXML
    private TableColumn<Paiement, Void> colActions;
    
    @FXML
    private TextField txtSearchPaiement;
    
    @FXML
    private Button btnSearchPaiement;
    
    @FXML
    private ComboBox<Fournisseur> comboFournisseur;
    
    private PaiementService paiementService;
    private CommandeService commandeService;
    private FournisseurService fournisseurService;
    private ObservableList<Paiement> paiementsList = FXCollections.observableArrayList();
    
    public PaiementController() {
        paiementService = new PaiementService();
        commandeService = new CommandeService();
        fournisseurService = new FournisseurService();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize table columns
        setupTableColumns();
        
        // Set up event handlers
        btnSearchPaiement.setOnAction(this::handleSearchPaiement);
        
        // Initialize supplier filter combobox
        setupFournisseurComboBox();
        
        // Load data
        refreshPaiementsList();
    }
    
    /**
     * Set up the table columns and their cell factories
     */
    private void setupTableColumns() {
        // Configure column cell value factories
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroPaiement"));
        
        colCommande.setCellValueFactory(cellData -> {
            Paiement paiement = cellData.getValue();
            if (paiement.getCommande() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> paiement.getCommande().getNumeroCommande()
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "N/A");
        });
        
        colFournisseur.setCellValueFactory(cellData -> {
            Paiement paiement = cellData.getValue();
            if (paiement.getCommande() != null && paiement.getCommande().getFournisseur() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> paiement.getCommande().getFournisseur().getNom()
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "N/A");
        });
        
        colDatePaiement.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));
        colDatePaiement.setCellFactory(column -> new TableCell<Paiement, LocalDate>() {
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
        
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colMontant.setCellFactory(column -> new TableCell<Paiement, Double>() {
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
        
        colMethode.setCellValueFactory(new PropertyValueFactory<>("methodePaiement"));
        colMethode.setCellFactory(column -> new TableCell<Paiement, String>() {
            @Override
            protected void updateItem(String methode, boolean empty) {
                super.updateItem(methode, empty);
                if (empty || methode == null) {
                    setText(null);
                } else {
                    setText(methode);
                }
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });
        
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colStatut.setCellFactory(column -> new TableCell<Paiement, String>() {
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
                        case "Complété":
                            setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-padding: 5;");
                            break;
                        case "Échoué":
                            setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-padding: 5;");
                            break;
                        case "Annulé":
                            setStyle("-fx-background-color: #e2e3e5; -fx-text-fill: #383d41; -fx-padding: 5;");
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });
        
        colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colReference.setCellFactory(column -> new TableCell<Paiement, String>() {
            @Override
            protected void updateItem(String reference, boolean empty) {
                super.updateItem(reference, empty);
                if (empty || reference == null) {
                    setText(null);
                } else {
                    setText(reference);
                }
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });
        
        // Set column alignment
        colNumero.setStyle("-fx-alignment: CENTER-LEFT;");
        colCommande.setStyle("-fx-alignment: CENTER-LEFT;");
        colFournisseur.setStyle("-fx-alignment: CENTER-LEFT;");
        colDatePaiement.setStyle("-fx-alignment: CENTER;");
        colMontant.setStyle("-fx-alignment: CENTER-RIGHT;");
        colMethode.setStyle("-fx-alignment: CENTER;");
        colStatut.setStyle("-fx-alignment: CENTER;");
        colReference.setStyle("-fx-alignment: CENTER;");
        colActions.setStyle("-fx-alignment: CENTER;");
        
        // Add action buttons (View, Delete)
        setupActionsColumn();
        
        // Set items
        tablePaiements.setItems(paiementsList);
    }
    
    /**
     * Set up the actions column with buttons
     */
    private void setupActionsColumn() {
        Callback<TableColumn<Paiement, Void>, TableCell<Paiement, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Paiement, Void> call(final TableColumn<Paiement, Void> param) {
                return new TableCell<>() {
                    private final Button viewBtn = new Button();
                    private final Button deleteBtn = new Button();
                    
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
                            Paiement paiement = getTableView().getItems().get(getIndex());
                            showPaiementDetails(paiement);
                        });
                        
                        // Configure delete button with icon
                        deleteBtn.getStyleClass().add("btn-icon");
                        deleteBtn.setStyle("-fx-background-color: transparent;");
                        
                        // Create delete icon
                        javafx.scene.image.ImageView deleteIcon = new javafx.scene.image.ImageView(
                            getClass().getResource("/tn/esprit/connecthr/images/delete.svg").toExternalForm()
                        );
                        deleteIcon.setFitHeight(24);
                        deleteIcon.setFitWidth(24);
                        deleteBtn.setGraphic(deleteIcon);
                        deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Supprimer"));
                        
                        deleteBtn.setOnAction(event -> {
                            Paiement paiement = getTableView().getItems().get(getIndex());
                            handleDeletePaiement(paiement);
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
                            hbox.getChildren().addAll(viewBtn, deleteBtn);
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
                    filterPaymentsBySupplier(newVal);
                }
            });
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de chargement", 
                    "Impossible de charger la liste des fournisseurs: " + e.getMessage());
        }
    }
    
    /**
     * Filter payments by selected supplier
     * @param fournisseur The selected supplier
     */
    private void filterPaymentsBySupplier(Fournisseur fournisseur) {
        try {
            if (fournisseur.getId() == -1) {
                // "All suppliers" option selected
                refreshPaiementsList();
            } else {
                List<Paiement> paiementsFournisseur = paiementService.getPaiementsByFournisseur(fournisseur.getId());
                paiementsList.clear();
                paiementsList.addAll(paiementsFournisseur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de filtrage", 
                    "Impossible de filtrer les paiements: " + e.getMessage());
        }
    }
    
    /**
     * Refresh the list of payments
     */
    public void refreshPaiementsList() {
        try {
            // Clear and reload payments
            paiementsList.clear();
            List<Paiement> paiements = paiementService.getAllPaiements();
            paiementsList.addAll(paiements);
            
            if (paiements.isEmpty()) {
                // Show empty message in the table
                tablePaiements.setPlaceholder(new javafx.scene.control.Label("Aucun paiement trouvé"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur de chargement", 
                    "Impossible de charger les paiements: " + e.getMessage());
        }
    }
    
    /**
     * Handle search payment button click
     * @param event The action event
     */
    @FXML
    private void handleSearchPaiement(ActionEvent event) {
        // For simplicity, we'll just refresh the list and filter by supplier
        // A more complex search functionality would require additional methods in PaiementService
        refreshPaiementsList();
    }
    
    /**
     * Handle delete payment
     * @param paiement The payment to delete
     */
    private void handleDeletePaiement(Paiement paiement) {
        try {
            // Show confirmation dialog
            Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation de suppression");
            confirmDialog.setHeaderText("Supprimer le paiement");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer le paiement " + paiement.getNumeroPaiement() + "?");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    try {
                        // Delete the payment
                        boolean success = paiementService.deletePaiement(paiement.getId());
                        
                        if (success) {
                            // Refresh the list
                            refreshPaiementsList();
                            
                            // Show success message
                            showAlert(AlertType.INFORMATION, "Succès", "Paiement supprimé", 
                                    "Le paiement a été supprimé avec succès.");
                        } else {
                            showAlert(AlertType.ERROR, "Erreur", "Erreur de suppression", 
                                    "Impossible de supprimer le paiement.");
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
     * Show payment details
     * @param paiement The payment to show details for
     */
    private void showPaiementDetails(Paiement paiement) {
        try {
            // Show payment details
            Alert detailsAlert = new Alert(AlertType.INFORMATION);
            detailsAlert.setTitle("Détails du paiement");
            detailsAlert.setHeaderText("Paiement: " + paiement.getNumeroPaiement());
            
            String commandeNumero = paiement.getCommande() != null ? paiement.getCommande().getNumeroCommande() : "N/A";
            String fournisseurName = (paiement.getCommande() != null && paiement.getCommande().getFournisseur() != null) ? 
                                     paiement.getCommande().getFournisseur().getNom() : "N/A";
            
            String content = "Commande: " + commandeNumero + "\n" +
                            "Fournisseur: " + fournisseurName + "\n" +
                            "Date de paiement: " + (paiement.getDatePaiement() != null ? paiement.getDatePaiement().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A") + "\n" +
                            "Montant: " + String.format("%,.2f TND", paiement.getMontant()) + "\n" +
                            "Méthode de paiement: " + paiement.getMethodePaiement() + "\n" +
                            "Statut: " + paiement.getStatut() + "\n" +
                            "Référence: " + paiement.getReference() + "\n\n" +
                            "Description: " + paiement.getDescription();
            
            detailsAlert.setContentText(content);
            detailsAlert.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur d'affichage", 
                    "Une erreur est survenue lors de l'affichage des détails: " + e.getMessage());
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
