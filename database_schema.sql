-- Database schema for ConnectHR ERP Supplier Management Module

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS connecthr;

-- Use the connecthr database
USE connecthr;

-- Suppliers table
CREATE TABLE IF NOT EXISTS fournisseurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    type_fournisseur VARCHAR(50) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    contact_name VARCHAR(100) NOT NULL,
    contact_position VARCHAR(100),
    date_creation DATETIME NOT NULL,
    derniere_mise_a_jour DATETIME NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    notes TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Orders table
CREATE TABLE IF NOT EXISTS commandes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fournisseur_id INT NOT NULL,
    numero_commande VARCHAR(20) NOT NULL,
    date_commande DATE NOT NULL,
    date_livraison DATE,
    statut VARCHAR(20) NOT NULL,
    montant_total DECIMAL(10, 2) NOT NULL,
    description TEXT,
    date_creation DATETIME NOT NULL,
    derniere_mise_a_jour DATETIME NOT NULL,
    FOREIGN KEY (fournisseur_id) REFERENCES fournisseurs(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Payments table
CREATE TABLE IF NOT EXISTS paiements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    commande_id INT NOT NULL,
    numero_paiement VARCHAR(20) NOT NULL,
    date_paiement DATE NOT NULL,
    montant DECIMAL(10, 2) NOT NULL,
    methode_paiement VARCHAR(50) NOT NULL,
    statut VARCHAR(20) NOT NULL,
    reference VARCHAR(100),
    description TEXT,
    date_creation DATETIME NOT NULL,
    derniere_mise_a_jour DATETIME NOT NULL,
    FOREIGN KEY (commande_id) REFERENCES commandes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create indexes for better performance
CREATE INDEX idx_fournisseur_nom ON fournisseurs(nom);
CREATE INDEX idx_fournisseur_type ON fournisseurs(type_fournisseur);
CREATE INDEX idx_fournisseur_active ON fournisseurs(active);
CREATE INDEX idx_commande_fournisseur ON commandes(fournisseur_id);
CREATE INDEX idx_commande_numero ON commandes(numero_commande);
CREATE INDEX idx_commande_dates ON commandes(date_commande, date_livraison);
CREATE INDEX idx_commande_statut ON commandes(statut);
CREATE INDEX idx_paiement_commande ON paiements(commande_id);
CREATE INDEX idx_paiement_numero ON paiements(numero_paiement);
CREATE INDEX idx_paiement_date ON paiements(date_paiement);
CREATE INDEX idx_paiement_statut ON paiements(statut);

-- Insert some sample supplier types (for reference)
INSERT INTO fournisseurs (nom, type_fournisseur, adresse, telephone, email, contact_name, contact_position, date_creation, derniere_mise_a_jour, active, notes)
VALUES
    ('InfoTech Solutions', 'Matériel informatique', '123 Rue de la Technologie, Tunis', '71123456', 'contact@infotech.com', 'Ahmed Ben Ali', 'Directeur Commercial', NOW(), NOW(), TRUE, 'Fournisseur principal de matériel informatique'),
    ('Bureau Express', 'Fournitures de bureau', '45 Avenue Habib Bourguiba, Sousse', '73456789', 'info@bureauexpress.tn', 'Sarra Trabelsi', 'Responsable Ventes', NOW(), NOW(), TRUE, 'Livraison rapide, bons prix'),
    ('NetServices', 'Services informatiques', '78 Rue Ibn Khaldoun, Sfax', '74789012', 'service@netservices.com', 'Karim Mejri', 'Gérant', NOW(), NOW(), TRUE, 'Services de maintenance et support IT');

-- Suggestions for using this database:
-- 1. Make sure to create the database user with appropriate privileges
-- 2. Set character set to utf8mb4 to support all languages and special characters
-- 3. Create regular backups of the database
-- 4. Consider adding more indexes if query performance becomes an issue
-- 5. For production, consider setting up replication for data redundancy
