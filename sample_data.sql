-- Base de données du système ERP ConnectHR

-- Création de la base de données
CREATE DATABASE IF NOT EXISTS erp_db;
USE erp_db;

-- Table des fournisseurs
CREATE TABLE IF NOT EXISTS fournisseurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    type_fournisseur VARCHAR(50),
    adresse VARCHAR(255),
    telephone VARCHAR(20),
    email VARCHAR(100),
    contact_name VARCHAR(100),
    contact_position VARCHAR(100),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    derniere_mise_a_jour TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    notes TEXT
);

-- Table des commandes
CREATE TABLE IF NOT EXISTS commandes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fournisseur_id INT NOT NULL,
    numero_commande VARCHAR(20) NOT NULL,
    date_commande DATE,
    date_livraison DATE,
    date_livraison_prevue DATE,
    statut VARCHAR(20) DEFAULT 'En attente', -- En attente, En cours, Livrée, Annulée
    montant_total DECIMAL(10, 2),
    description TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    derniere_mise_a_jour TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (fournisseur_id) REFERENCES fournisseurs(id)
);

-- Table des paiements
CREATE TABLE IF NOT EXISTS paiements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    commande_id INT NOT NULL,
    numero_paiement VARCHAR(20) NOT NULL,
    date_paiement DATE,
    montant DECIMAL(10, 2) NOT NULL,
    methode_paiement VARCHAR(50), -- Virement, Chèque, Espèces, Carte bancaire
    statut VARCHAR(20) DEFAULT 'En attente', -- En attente, Complété, Annulé
    reference VARCHAR(100),
    description TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    derniere_mise_a_jour TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (commande_id) REFERENCES commandes(id)
);

-- Données de démonstration

-- Insertion de fournisseurs
INSERT INTO fournisseurs (nom, type_fournisseur, adresse, telephone, email, contact_name, contact_position, active, notes)
VALUES
    ('Techwave Solutions', 'Matériel informatique', '15 Rue de l''Innovation, Tunis', '+216 71 123 456', 'contact@techwave.tn', 'Mohamed Ali', 'Directeur Commercial', TRUE, 'Fournisseur principal de matériel informatique'),
    ('MobilePro', 'Téléphonie', '25 Avenue Habib Bourguiba, Sfax', '+216 74 987 654', 'info@mobilepro.tn', 'Sami Ben Salem', 'Responsable Grands Comptes', TRUE, 'Fournisseur de téléphones et accessoires mobiles'),
    ('Bureau Plus', 'Fournitures de bureau', '8 Rue des Entrepreneurs, Sousse', '+216 73 456 789', 'ventes@bureauplus.tn', 'Yasmine Trabelsi', 'Directrice des Ventes', TRUE, 'Fournitures et mobilier de bureau'),
    ('InfoSys Tunisie', 'Services IT', '45 Rue du Développement, Tunis', '+216 71 234 567', 'business@infosys.tn', 'Karim Meddeb', 'Directeur Partenariats', TRUE, 'Services informatiques et développement de logiciels'),
    ('DataSecure', 'Sécurité informatique', '12 Rue de la Cybersécurité, Monastir', '+216 73 321 654', 'contact@datasecure.tn', 'Leila Mansouri', 'Responsable Commercial', TRUE, 'Solutions de sécurité informatique');

-- Insertion de commandes
INSERT INTO commandes (fournisseur_id, numero_commande, date_commande, date_livraison, date_livraison_prevue, statut, montant_total, description)
VALUES
    (1, 'CMD-20230110-001', '2023-01-10', '2023-01-15', '2023-01-15', 'Livrée', 5689.50, '10 ordinateurs portables HP EliteBook'),
    (1, 'CMD-20230215-001', '2023-02-15', '2023-02-28', '2023-02-25', 'Livrée', 3450.00, '5 imprimantes LaserJet Pro'),
    (1, 'CMD-20230320-001', '2023-03-20', '2023-04-05', '2023-04-01', 'Livrée', 8975.25, '15 écrans Dell 27 pouces + accessoires'),
    (1, 'CMD-20230418-001', '2023-04-18', '2023-04-30', '2023-04-28', 'Livrée', 4320.75, 'Matériel réseau et câblage'),
    (1, 'CMD-20230522-001', '2023-05-22', NULL, '2023-06-05', 'En cours', 12750.00, 'Serveur Dell PowerEdge + licences'),
    
    (2, 'CMD-20230105-001', '2023-01-05', '2023-01-12', '2023-01-12', 'Livrée', 4250.00, '10 smartphones Samsung Galaxy A52'),
    (2, 'CMD-20230228-001', '2023-02-28', '2023-03-15', '2023-03-10', 'Livrée', 3180.00, '6 tablettes iPad Pro'),
    (2, 'CMD-20230410-001', '2023-04-10', '2023-04-19', '2023-04-17', 'Livrée', 2340.50, 'Accessoires téléphoniques divers'),
    (2, 'CMD-20230518-001', '2023-05-18', NULL, '2023-06-01', 'En attente', 6890.25, '15 smartphones iPhone 13'),
    (2, 'CMD-20230522-002', '2023-05-22', NULL, '2023-06-05', 'Annulée', 1450.00, 'Casques et écouteurs Bluetooth'),
    
    (3, 'CMD-20230120-001', '2023-01-20', '2023-01-25', '2023-01-27', 'Livrée', 1850.75, 'Fournitures bureau - papeterie'),
    (3, 'CMD-20230227-001', '2023-02-27', '2023-03-05', '2023-03-06', 'Livrée', 4320.00, '5 bureaux ergonomiques'),
    (3, 'CMD-20230315-001', '2023-03-15', '2023-03-22', '2023-03-22', 'Livrée', 2150.50, '10 chaises de bureau'),
    (3, 'CMD-20230412-001', '2023-04-12', '2023-04-20', '2023-04-22', 'Livrée', 3275.25, 'Classeurs et matériel d''archivage'),
    (3, 'CMD-20230510-001', '2023-05-10', NULL, '2023-05-25', 'En cours', 5670.00, 'Mobilier salle de conférence'),
    
    (4, 'CMD-20230125-001', '2023-01-25', '2023-02-10', '2023-02-10', 'Livrée', 8500.00, 'Développement application RH - Phase 1'),
    (4, 'CMD-20230310-001', '2023-03-10', '2023-03-30', '2023-03-25', 'Livrée', 7500.00, 'Développement application RH - Phase 2'),
    (4, 'CMD-20230420-001', '2023-04-20', '2023-05-05', '2023-05-10', 'Livrée', 9200.50, 'Développement application RH - Phase 3'),
    (4, 'CMD-20230515-001', '2023-05-15', NULL, '2023-06-10', 'En cours', 5600.00, 'Maintenance et support'),
    (4, 'CMD-20230520-001', '2023-05-20', NULL, '2023-06-15', 'En attente', 4820.75, 'Formation utilisateurs'),
    
    (5, 'CMD-20230215-002', '2023-02-15', '2023-02-22', '2023-02-25', 'Livrée', 6750.00, 'Licences antivirus entreprise'),
    (5, 'CMD-20230318-001', '2023-03-18', '2023-03-25', '2023-03-28', 'Livrée', 8320.50, 'Solution pare-feu matériel'),
    (5, 'CMD-20230421-001', '2023-04-21', '2023-04-30', '2023-05-01', 'Livrée', 4150.75, 'Audit sécurité informatique'),
    (5, 'CMD-20230510-002', '2023-05-10', NULL, '2023-05-30', 'En cours', 7890.25, 'VPN d''entreprise'),
    (5, 'CMD-20230522-003', '2023-05-22', NULL, '2023-06-05', 'En attente', 5230.00, 'Solution cryptage données');

-- Insertion de paiements
INSERT INTO paiements (commande_id, numero_paiement, date_paiement, montant, methode_paiement, statut, reference, description)
VALUES
    (1, 'PAY-20230117-001', '2023-01-17', 5689.50, 'Virement', 'Complété', 'VIR-2301175689', 'Paiement complet commande ordinateurs'),
    (2, 'PAY-20230301-001', '2023-03-01', 3450.00, 'Virement', 'Complété', 'VIR-2303013450', 'Paiement complet commande imprimantes'),
    (3, 'PAY-20230410-001', '2023-04-10', 8975.25, 'Virement', 'Complété', 'VIR-2304108975', 'Paiement complet commande écrans'),
    (4, 'PAY-20230505-001', '2023-05-05', 4320.75, 'Chèque', 'Complété', 'CHQ-4759834', 'Paiement complet commande matériel réseau'),
    (5, 'PAY-20230525-001', '2023-05-25', 6375.00, 'Virement', 'Complété', 'VIR-2305256375', 'Acompte 50% serveur Dell'),
    
    (6, 'PAY-20230115-001', '2023-01-15', 4250.00, 'Carte bancaire', 'Complété', 'CB-23011542500', 'Paiement complet smartphones Samsung'),
    (7, 'PAY-20230315-001', '2023-03-15', 3180.00, 'Virement', 'Complété', 'VIR-2303153180', 'Paiement complet tablettes iPad'),
    (8, 'PAY-20230420-001', '2023-04-20', 2340.50, 'Carte bancaire', 'Complété', 'CB-23042023405', 'Paiement complet accessoires téléphoniques'),
    (9, 'PAY-20230520-001', '2023-05-20', 3445.13, 'Chèque', 'En attente', 'CHQ-5827463', 'Acompte 50% smartphones iPhone'),
    
    (11, 'PAY-20230127-001', '2023-01-27', 1850.75, 'Carte bancaire', 'Complété', 'CB-23012718507', 'Paiement complet fournitures papeterie'),
    (12, 'PAY-20230308-001', '2023-03-08', 4320.00, 'Virement', 'Complété', 'VIR-2303084320', 'Paiement complet bureaux ergonomiques'),
    (13, 'PAY-20230325-001', '2023-03-25', 2150.50, 'Carte bancaire', 'Complété', 'CB-23032521505', 'Paiement complet chaises de bureau'),
    (14, 'PAY-20230425-001', '2023-04-25', 3275.25, 'Chèque', 'Complété', 'CHQ-9384756', 'Paiement complet matériel d''archivage'),
    (15, 'PAY-20230515-001', '2023-05-15', 2835.00, 'Virement', 'Complété', 'VIR-2305152835', 'Acompte 50% mobilier conférence'),
    
    (16, 'PAY-20230210-001', '2023-02-10', 4250.00, 'Virement', 'Complété', 'VIR-2302104250', 'Acompte 50% développement phase 1'),
    (16, 'PAY-20230215-001', '2023-02-15', 4250.00, 'Virement', 'Complété', 'VIR-2302154250', 'Solde développement phase 1'),
    (17, 'PAY-20230330-001', '2023-03-30', 7500.00, 'Virement', 'Complété', 'VIR-2303307500', 'Paiement complet développement phase 2'),
    (18, 'PAY-20230505-002', '2023-05-05', 9200.50, 'Virement', 'Complété', 'VIR-2305059200', 'Paiement complet développement phase 3'),
    (19, 'PAY-20230520-002', '2023-05-20', 2800.00, 'Virement', 'En attente', 'VIR-2305202800', 'Acompte 50% maintenance et support'),
    
    (21, 'PAY-20230222-001', '2023-02-22', 6750.00, 'Virement', 'Complété', 'VIR-2302226750', 'Paiement complet licences antivirus'),
    (22, 'PAY-20230325-002', '2023-03-25', 8320.50, 'Virement', 'Complété', 'VIR-2303258320', 'Paiement complet pare-feu matériel'),
    (23, 'PAY-20230430-001', '2023-04-30', 4150.75, 'Carte bancaire', 'Complété', 'CB-23043041507', 'Paiement complet audit sécurité'),
    (24, 'PAY-20230515-002', '2023-05-15', 3945.13, 'Chèque', 'En attente', 'CHQ-7629384', 'Acompte 50% VPN entreprise');