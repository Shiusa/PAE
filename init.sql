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

INSERT INTO proStage.utilisateurs VALUES(DEFAULT, 'alice.dubois@vinci.be', 'Dubois', 'Alice', '+32471234567', '123', '2023-08-20', '2023-2024', 'administratif');
INSERT INTO proStage.utilisateurs VALUES(DEFAULT, 'hugo.leroy@vinci.be', 'Leroy', 'Hugo', '+32478987654', '123', '2023-11-09', '2023-2024', 'professeur');
INSERT INTO proStage.utilisateurs VALUES(DEFAULT, 'eleonore.martin@vinci.be', 'Martin', 'Éléonore', '+32 485 12 34 56', '123', CURRENT_DATE, '2023-2024', 'etudiant');