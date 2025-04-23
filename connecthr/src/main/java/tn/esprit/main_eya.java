package tn.esprit;

import tn.esprit.models.facture;
import tn.esprit.services.factureservice;

import java.time.LocalDate;
import java.util.List;
public class main_eya {


        public static void main(String[] args) {
            factureservice service = new factureservice();

            // ✅ Création d'une nouvelle facture
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);

            facture facture = new facture(
                    1,                        // id_facture
                    dateEmission,
                    dateEcheance,
                    150.0f,                  // cout_unitaire
                    10.0f,                   // remise
                    "Carte bancaire",        // mode_paiement
                    "En attente"             // statut
            );

            // ✅ Ajout de la facture
            boolean ajout = service.add(facture);
            System.out.println("Facture ajoutée : " + ajout);

            // ✅ Récupération et affichage de toutes les factures
            List<facture> factures = service.getAll();
            for (facture f : factures) {
                System.out.println(f);
            }
        }
    }
