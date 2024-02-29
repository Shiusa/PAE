DROP SCHEMA IF EXISTS proStage CASCADE;
CREATE SCHEMA proStage;

CREATE TABLE proStage.utilisateurs (
    id_utilisateur SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    mot_de_passe VARCHAR(60) NOT NULL,
    date_inscription DATE NOT NULL,
    annee_academique VARCHAR(9) NOT NULL,
    role VARCHAR(13) NOT NULL
);

-- MDP : 123
INSERT INTO proStage.utilisateurs VALUES(DEFAULT, 'alice.dubois@vinci.be', 'Dubois', 'Alice', '+32471234567', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', '2023-08-20', '2023-2024', 'administratif');
INSERT INTO proStage.utilisateurs VALUES(DEFAULT, 'hugo.leroy@vinci.be', 'Leroy', 'Hugo', '+32478987654', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', '2023-11-09', '2023-2024', 'professeur');
INSERT INTO proStage.utilisateurs VALUES(DEFAULT, 'eleonore.martin@vinci.be', 'Martin', 'Éléonore', '+32485123456', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', CURRENT_DATE, '2023-2024', 'etudiant');