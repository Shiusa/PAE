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
                                    name TEXT NOT NULL,
                                    designation TEXT,
                                    address TEXT NOT NULL,
                                    phone_number TEXT,
                                    email TEXT,
                                    is_blacklisted BOOLEAN NOT NULL,
                                    blacklist_motivation TEXT,
                                    version INTEGER NOT NULL
);

CREATE TABLE proStage.contacts (
                                   contact_id SERIAL PRIMARY KEY,
                                   company INTEGER NOT NULL REFERENCES proStage.companies (company_id),
                                   student INTEGER NOT NULL REFERENCES proStage.users (user_id),
                                   meeting TEXT,
                                   contact_state TEXT NOT NULL,
                                   reason_for_refusal TEXT,
                                   school_year TEXT NOT NULL,
                                   version INTEGER NOT NULL
);

CREATE TABLE proStage.supervisors (
                                      supervisor_id SERIAL PRIMARY KEY,
                                      company INTEGER NOT NULL REFERENCES proStage.companies (company_id),
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
                                      school_year TEXT NOT NULL,
    version INTEGER NOT NULL
);

--companies
INSERT INTO prostage.companies VALUES (DEFAULT, 'Assyst Europe', NULL, 'Avenue du Japon, 1/B9 1420 Braine-l Alleud', '02.609.25.00', NULL, FALSE, NULL,1);
INSERT INTO prostage.companies VALUES (DEFAULT, 'LetsBuild', NULL, 'Chaussée de Bruxelles, 135A	1310 La Hulpe', '014 54 67 54', NULL, FALSE, NULL,1);
INSERT INTO prostage.companies VALUES (DEFAULT, 'Niboo', NULL, 'Boulevard du Souverain, 24 1170 Watermael-Boisfort', '0487 02 79 13', NULL, FALSE, NULL,1);
INSERT INTO prostage.companies VALUES (DEFAULT, 'Sopra Steria', NULL, 'Avenue Arnaud Fraiteur, 15/23 1050 Bruxelles', '02 566 66 66', NULL, FALSE, NULL,1);

--users
-- MDP =
-- PROFESSEUR : Prof24,z
-- ADMIN : Admin;10.
-- ETUDIANT : mdpuser.1
INSERT INTO prostage.users VALUES (DEFAULT, 'raphael.baroni@vinci.be', 'Baroni', 'Raphaël', '0481 01 01 01', '$2a$10$C6Wcdn4Vvpd1GyZE/bUrM.GNegKhb1srvXW6AD8ekak3IjkRnQqv2', '2020-09-21', '2020-2021', 'Professeur');
INSERT INTO prostage.users VALUES (DEFAULT, 'brigitte.lehmann@vinci.be', 'Lehmann', 'Brigitte', '0482 02 02 02', '$2a$10$C6Wcdn4Vvpd1GyZE/bUrM.GNegKhb1srvXW6AD8ekak3IjkRnQqv2', '2020-09-21', '2020-2021', 'Professeur');
INSERT INTO prostage.users VALUES (DEFAULT, 'laurent.leleux@vinci.be', 'Leleux', 'Laurent', '0483 03 03 03', '$2a$10$C6Wcdn4Vvpd1GyZE/bUrM.GNegKhb1srvXW6AD8ekak3IjkRnQqv2', '2020-09-21', '2020-2021', 'Professeur');
INSERT INTO prostage.users VALUES (DEFAULT, 'annouck.lancaster@vinci.be', 'Lancaster', 'Annouck', '0484 04 04 04', '$2a$10$NlBpR4SXcDm60PMehDcTlO5RuZrjc1CV5RspIRMa8B9z4wv3n/SBW', '2020-09-21', '2020-2021', 'Administratif');
INSERT INTO prostage.users VALUES (DEFAULT, 'Caroline.line@student.vinci.be', 'Line', 'Caroline', '0486 00 00 01', '$2a$10$NNrcZjmFqEaiigCLgQ9HRuUACG8ePBrKrJko0bVdAcTcIi/SFcGdS', '2023-09-18', '2023-2024', 'Etudiant');
INSERT INTO prostage.users VALUES (DEFAULT, 'Ach.ile@student.vinci.be', 'Ile', 'Achille', '0487 00 00 01', '$2a$10$NNrcZjmFqEaiigCLgQ9HRuUACG8ePBrKrJko0bVdAcTcIi/SFcGdS', '2023-09-18', '2023-2024', 'Etudiant');
INSERT INTO prostage.users VALUES (DEFAULT, 'Basile.Ile@student.vinci.be', 'Ile', 'Basile', '0488 00 00 01', '$2a$10$NNrcZjmFqEaiigCLgQ9HRuUACG8ePBrKrJko0bVdAcTcIi/SFcGdS', '2023-09-18', '2023-2024', 'Etudiant');
INSERT INTO prostage.users VALUES (DEFAULT, 'Achille.skile@student.vinci.be', 'skile', 'Achille', '0490 00 00 01', '$2a$10$NNrcZjmFqEaiigCLgQ9HRuUACG8ePBrKrJko0bVdAcTcIi/SFcGdS', '2023-09-18', '2023-2024', 'Etudiant');
INSERT INTO prostage.users VALUES (DEFAULT, 'Carole.skile@student.vinci.be', 'skile', 'Carole', '0489 00 00 01', '$2a$10$NNrcZjmFqEaiigCLgQ9HRuUACG8ePBrKrJko0bVdAcTcIi/SFcGdS', '2023-09-18', '2023-2024', 'Etudiant');

--supervisors
INSERT INTO proStage.supervisors VALUES (DEFAULT,2,'Dossche','Stéphanie','014.54.67.54','stephanie.dossche@letsbuild.com');
INSERT INTO proStage.supervisors VALUES (DEFAULT,4,'ALVAREZ CORCHETE','Roberto','02.566.60.14',NULL);
INSERT INTO proStage.supervisors VALUES (DEFAULT,1,'Assal','Farid','0474 39 69 09','f.assal@assyst-europe.com');

--contacts
INSERT INTO proStage.contacts VALUES (DEFAULT,2,9,'A distance','accepté',NULL,'2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,4,6,'Dans l entreprise','accepté',NULL,'2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,3,6,'A distance','refusé','N ont pas accepté d avoir un entretien','2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,1,7,'Dans l entreprise','accepté',NULL,'2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,2,7,'A distance','suspendu',NULL,'2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,4,7,NULL,'suspendu',NULL,'2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,3,7,'Dans l entreprise','refusé','ne prennent qu un seul étudiant','2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,3,5,'A distance','pris',NULL,'2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,4,5,NULL,'initié',NULL,'2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,2,5,NULL,'initié',NULL,'2023-2024',1);
INSERT INTO proStage.contacts VALUES (DEFAULT,4,8,NULL,'initié',NULL,'2023-2024',1);

--internships
INSERT INTO proStage.internships VALUES (DEFAULT,1,1,'2023-10-10','Un ERP : Odoo','2023-2024', 1);
INSERT INTO proStage.internships VALUES (DEFAULT,2,2,'2023-11-23','sBMS project - a complex environment','2023-2024', 1);
INSERT INTO proStage.internships VALUES (DEFAULT,4,3,'2023-10-12','CRM : Microsoft Dynamics 365 For Sales','2023-2024', 1);

SELECT COUNT(*) AS nbUsers FROM proStage.users;
SELECT COUNT(*) AS nbCompanies FROM proStage.companies;
SELECT i.school_year, COUNT(*) AS nbStages FROM proStage.internships i GROUP BY i.school_year;
SELECT c.school_year, COUNT(*) AS nbStages FROM proStage.contacts c GROUP BY c.school_year;
SELECT c.contact_state, COUNT(*) FROM proStage.contacts c GROUP BY c.contact_state;