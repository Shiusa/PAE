DROP SCHEMA IF EXISTS proStage CASCADE;
CREATE SCHEMA proStage;

CREATE TABLE proStage.users (
    user_id SERIAL PRIMARY KEY,
    email TEXT NOT NULL,
    lastname TEXT NOT NULL,
    firstname TEXT NOT NULL,
    phone_number TEXT NOT NULL,
    password TEXT NOT NULL,
    registration_date DATE NOT NULL,
    school_year TEXT NOT NULL,
    role TEXT NOT NULL
);

-- MDP : 123
INSERT INTO proStage.users VALUES(DEFAULT, 'alice.dubois@vinci.be', 'Dubois', 'Alice', '+32471234567', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', '2023-08-20', '2023-2024', 'administratif');
INSERT INTO proStage.users VALUES(DEFAULT, 'hugo.leroy@vinci.be', 'Leroy', 'Hugo', '+32478987654', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', '2023-11-09', '2023-2024', 'professeur');
INSERT INTO proStage.users VALUES(DEFAULT, 'eleonore.martin@vinci.be', 'Martin', 'Éléonore', '+32485123456', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', CURRENT_DATE, '2023-2024', 'etudiant');