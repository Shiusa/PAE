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

CREATE TABLE proStage.companies (
                                    company_id SERIAL PRIMARY KEY,
                                    name TEXT,
                                    designation TEXT,
                                    address TEXT NOT NULL,
                                    phone_number TEXT,
                                    email TEXT,
                                    is_blacklisted BOOLEAN NOT NULL,
                                    blacklist_motivation TEXT
);

CREATE TABLE proStage.contacts (
                                   contact_id SERIAL PRIMARY KEY,
                                   company INTEGER NOT NULL REFERENCES proStage.companies (company_id),
                                   student INTEGER NOT NULL REFERENCES proStage.users (user_id),
                                   meeting TEXT,
                                   contact_state TEXT NOT NULL,
                                   reason_for_refusal TEXT,
                                   school_year TEXT NOT NULL
);

CREATE TABLE proStage.supervisors (
                                      supervisor_id SERIAL PRIMARY KEY,
                                      lastname TEXT NOT NULL,
                                      firstname TEXT NOT NULL,
                                      phone_number TEXT NOT NULL,
                                      email TEXT
);

CREATE TABLE proStage.internships (
                                      internship_id SERIAL PRIMARY KEY,
                                      contact INTEGER NOT NULL REFERENCES proStage.contacts (contact_id),
                                      supervisor INTEGER NOT NULL REFERENCES proStage.supervisors (supervisor_id),
                                      signature_date DATE NOT NULL,
                                      project TEXT,
                                      school_year TEXT NOT NULL
);


-- MDP : 123
INSERT INTO proStage.users VALUES(DEFAULT, 'alice.dubois@vinci.be', 'Dubois', 'Alice', '+32471234567', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', '2023-08-20', '2023-2024', 'administrative employe');
INSERT INTO proStage.users VALUES(DEFAULT, 'hugo.leroy@vinci.be', 'Leroy', 'Hugo', '+32478987654', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', '2023-11-09', '2023-2024', 'teacher');
INSERT INTO proStage.users VALUES(DEFAULT, 'eleonore.martin@vinci.be', 'Martin', 'Éléonore', '+32485123456', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', CURRENT_DATE, '2023-2024', 'student');
INSERT INTO proStage.users VALUES(DEFAULT, 'marcel.dubois@vinci.be', 'Dubois', 'Marcel', '+3285214365', '$2a$10$PO9RaYGTTyCohyiydzsyOuYMVZr7coRXYw8v4pmknkzGcKKLM/eAO', CURRENT_DATE, '2023-2024', 'student');


INSERT INTO proStage.companies VALUES(DEFAULT, 'DasInfo', NULL, 'Avenue des marais 80, Scharbeek 1500', '0412345678', 'das@auto.com', FALSE, NULL);
INSERT INTO proStage.companies VALUES(DEFAULT, 'LSCustom', 'Bruxelles', 'Avenue des marais 80, Scharbeek 1500', '0412345678', 'das@auto.com', FALSE, NULL);
INSERT INTO proStage.companies VALUES(DEFAULT, 'LSCustom', 'Anvers', 'Avenue des marais 80, Scharbeek 1500', '0412345678', 'das@auto.com', TRUE, 'Anvers est trop loin!');


INSERT INTO proStage.contacts VALUES(DEFAULT, 1, 1, NULL, 'started', NULL, '2023-2024');
INSERT INTO proStage.contacts VALUES(DEFAULT, 3, 2, 'remote', 'turned down', 'Etudiant trop lent', '2023-2024');
INSERT INTO proStage.contacts VALUES(DEFAULT, 2, 4, 'remote', 'accepted', NULL, '2023-2024');


INSERT INTO proStage.supervisors VALUES(DEFAULT, 'Leroux', 'Marc', '+32412345678', NULL);
INSERT INTO proStage.supervisors VALUES(DEFAULT, 'Piavosan', 'Isabelle', '+3287654321', 'isabelle.piavosan@mail.com');

INSERT INTO proStage.internships VALUES(DEFAULT, 3, 2, CURRENT_DATE, 'A new company application!', '2023-2024');