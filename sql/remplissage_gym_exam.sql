-- -------------------------------------------------------
-- Script de remplissage - Projet Java salle de sport
--
-- Les mots de passe sont stockes sous forme de hash PBKDF2.
-- Les mots de passe en clair sont uniquement indiques en commentaire
-- pour permettre les tests dans l'application.
--
-- Comptes crees :
-- Admin :
--   pierre.hontoir / phontoir145
--
-- Comptes sans abonnement :
--   clara.delvaux / cdelvaux21
--   lucas.marechal / lmarechal96
--   emma.vandenberg / evanden73
--
-- Membres avec abonnement :
--   nathan.gilson / ngilson422
--   julie.lambert / jlambert00
--   hugo.fontaine / hfontaine97
--   lea.dumont / ldumont281
--   maxime.renard / mrenard538
--
-- Coachs :
--   camille.evrard / cevrard425
--   antoine.leclercq / aleclercq94
--   sarah.janssens / sjanssens16
-- -------------------------------------------------------

USE gym_db;

SET SQL_SAFE_UPDATES = 0;
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM appointment;
DELETE FROM coach_availability;
DELETE FROM sponsorship;
DELETE FROM payment;
DELETE FROM qualification;
DELETE FROM access;
DELETE FROM gym_member;
DELETE FROM admin;
DELETE FROM coach;
DELETE FROM subscription;
DELETE FROM facility;
DELETE FROM speciality;
DELETE FROM room;
DELETE FROM person;

ALTER TABLE appointment AUTO_INCREMENT = 1;
ALTER TABLE coach_availability AUTO_INCREMENT = 1;
ALTER TABLE sponsorship AUTO_INCREMENT = 1;
ALTER TABLE payment AUTO_INCREMENT = 1;
ALTER TABLE subscription AUTO_INCREMENT = 1;
ALTER TABLE room AUTO_INCREMENT = 1;
ALTER TABLE person AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO person
    (id, first_name, last_name, birth_date, gender, email, phone, locker_number, username, password)
VALUES
    (1, 'Pierre', 'Hontoir', '1998-04-12', 'MALE', 'pierre.hontoir@gmail.com', '+32472631894', NULL, 'pierre.hontoir', 'PBKDF2WithHmacSHA256:65536:d7ElARdoam9wA7I5IuuaRQ==:aUoPgVYMBamGpKEOUn0ffhb0wlLiEWeMaT6hBkerCpU='),
    (2, 'Clara', 'Delvaux', '2001-09-27', 'FEMALE', 'clara.delvaux@hotmail.com', '+32486247931', NULL, 'clara.delvaux', 'PBKDF2WithHmacSHA256:65536:zWXn/9+qVXDWVFub2mY4Ug==:VwANCSkPwyNXhFhNNwxeWzJSYqJmToUcUB0G/tzTwWA='),
    (3, 'Lucas', 'Marechal', '1996-01-18', 'MALE', 'lucas.marechal@outlook.com', '+32491853460', NULL, 'lucas.marechal', 'PBKDF2WithHmacSHA256:65536:xbdNYBDQNNcBuCFVIVP2Iw==:t5L9eXWg+yqLrXoLXzFyjqUj5iG1rZ/zX1kQWExEfe0='),
    (4, 'Emma', 'Vandenberg', '2003-06-05', 'FEMALE', 'emma.vandenberg@gmail.com', '+32478125673', NULL, 'emma.vandenberg', 'PBKDF2WithHmacSHA256:65536:60Cbh4OFV1okHDPSkxuPrg==:GJJ27ErHoispXxnXVzml359uMzb2LjduA3YgfCpapik='),
    (5, 'Nathan', 'Gilson', '1999-11-22', 'MALE', 'nathan.gilson@hotmail.com', '+32465774218', 1, 'nathan.gilson', 'PBKDF2WithHmacSHA256:65536:1n+XV80fmvli/sVTU90b+Q==:Y9ETFEKMtfIo8AKLGh/azJge9/62QChJmDcTzLyjVUE='),
    (6, 'Julie', 'Lambert', '2000-03-14', 'FEMALE', 'julie.lambert@outlook.com', '+32496319025', NULL, 'julie.lambert', 'PBKDF2WithHmacSHA256:65536:SLH/VfNTQS4XwPD8Pk51MQ==:Z6Y/8oAKZpfv8g3ZJeYnKh6ONRIAOZvypNZpskELT08='),
    (7, 'Hugo', 'Fontaine', '1997-08-30', 'MALE', 'hugo.fontaine@gmail.com', '+32473881549', 2, 'hugo.fontaine', 'PBKDF2WithHmacSHA256:65536:kwX9HQgJsxX5GLNfY8mDCQ==:goJLQv6vBd6UBqLRxsq4ODSEBS2p2C1vCfalpfEgcSI='),
    (8, 'Lea', 'Dumont', '2002-12-09', 'FEMALE', 'lea.dumont@hotmail.com', '+32489462781', NULL, 'lea.dumont', 'PBKDF2WithHmacSHA256:65536:Mcdz2X9fqaTPmpsvfjhWbg==:eeT6o/6cVMg/6nsoCD7R+gGHA3vHElC+PiaxXX23HS4='),
    (9, 'Maxime', 'Renard', '1995-05-19', 'MALE', 'maxime.renard@outlook.com', '+32475196438', 3, 'maxime.renard', 'PBKDF2WithHmacSHA256:65536:jU4Fuy8fvVMXFX8F8B89kQ==:NpndsQ46aQxhWwzbsVeDQMHrjyVHRAOg2qa/5iRnv2Q='),
    (10, 'Camille', 'Evrard', '2004-02-25', 'FEMALE', 'camille.evrard@gmail.com', '+32468528317', NULL, 'camille.evrard', 'PBKDF2WithHmacSHA256:65536:fkih84ycYCaZ3nxX4NuoYw==:E6WCQc4P84f94BHVjoUKLHIdAlIrzoNndKfWwcc3JJM='),
    (11, 'Antoine', 'Leclercq', '1994-10-03', 'MALE', 'antoine.leclercq@hotmail.com', '+32498073692', NULL, 'antoine.leclercq', 'PBKDF2WithHmacSHA256:65536:yXXY4H3yVfd0RUmz2hl/Wg==:eb0xJlJjEiRbNutpimCo8Tk/Y+L6VCO5YtBUpR9uvx0='),
    (12, 'Sarah', 'Janssens', '2001-07-16', 'FEMALE', 'sarah.janssens@outlook.com', '+32476934105', NULL, 'sarah.janssens', 'PBKDF2WithHmacSHA256:65536:CH4vAVK3TXbu8EP9P6CtAA==:OsiDA5/wLd0Dc4QNNVvhNWrHVbYriZduBvXoF6UMmPk=');

INSERT INTO room (id, name, capacity)
VALUES
    (1, 'Salle musculation', 20),
    (2, 'Salle cardio', 25),
    (3, 'Studio collectif', 15),
    (4, 'Bureau coaching', 4);

INSERT INTO speciality (name, description)
VALUES
    ('Musculation', 'Renforcement musculaire et technique'),
    ('Cardio', 'Endurance et condition physique'),
    ('Yoga', 'Mobilite, respiration et relaxation'),
    ('Pilates', 'Gainage et posture'),
    ('Nutrition', 'Conseils alimentaires et suivi');

INSERT INTO facility (name, description)
VALUES
    ('Zone cardio', 'Tapis, velos, rameurs et machines endurance'),
    ('Salle musculation', 'Machines guidees, poids libres et bancs'),
    ('Studio cours collectifs', 'Espace reserve aux cours en groupe'),
    ('Espace wellness', 'Zone calme avec sauna et recuperation'),
    ('Suivi nutrition', 'Accompagnement nutritionnel personnalise');

INSERT INTO subscription (id, type, price, duration_months)
VALUES
    (1, 'BASIC', 15.99, 1),
    (2, 'STANDARD', 74.97, 3),
    (3, 'PREMIUM', 239.94, 6),
    (4, 'STANDARD', 299.88, 12),
    (5, 'PREMIUM', 959.76, 24);

INSERT INTO admin (person_id, access_level)
VALUES
    (1, 2);

INSERT INTO coach (person_id, has_degree)
VALUES
    (10, b'1'),
    (11, b'1'),
    (12, b'0');

INSERT INTO gym_member (person_id, wants_locker, weight, height, enrollment)
VALUES
    (5, b'1', 72.40, 176, 1),
    (6, b'0', 65.00, 170, 2),
    (7, b'1', 80.30, 184, 3),
    (8, b'0', 54.90, 162, 4),
    (9, b'1', 88.60, 187, 5);


INSERT INTO access (subscription_id, facility_name)
VALUES
    (1, 'Zone cardio'),
    (1, 'Salle musculation'),
    (2, 'Zone cardio'),
    (2, 'Salle musculation'),
    (2, 'Studio cours collectifs'),
    (3, 'Zone cardio'),
    (3, 'Salle musculation'),
    (3, 'Studio cours collectifs'),
    (3, 'Espace wellness'),
    (3, 'Suivi nutrition'),
    (4, 'Zone cardio'),
    (4, 'Salle musculation'),
    (4, 'Studio cours collectifs'),
    (5, 'Zone cardio'),
    (5, 'Salle musculation'),
    (5, 'Studio cours collectifs'),
    (5, 'Espace wellness'),
    (5, 'Suivi nutrition');

INSERT INTO qualification (coach_id, speciality_name)
VALUES
    (10, 'Musculation'),
    (10, 'Cardio'),
    (11, 'Yoga'),
    (11, 'Pilates'),
    (12, 'Nutrition'),
    (12, 'Cardio');

-- ============================================================
-- Paiements
-- ============================================================

INSERT INTO payment (id, amount, date_payment, billing)
VALUES
    (1, 15.99, DATE_SUB(CURDATE(), INTERVAL 18 DAY), 5),
    (2, 74.97, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 6),
    (3, 239.94, DATE_SUB(CURDATE(), INTERVAL 12 DAY), 7),
    (4, 299.88, DATE_SUB(CURDATE(), INTERVAL 9 DAY), 8),
    (5, 959.76, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 9);

-- ============================================================
-- Parrainage
-- Hugo Fontaine parraine Julie Lambert et Lea Dumont.
-- ============================================================

INSERT INTO sponsorship (id, sponsor_id, sponsored_id, start_date)
VALUES
    (1, 7, 6, DATE_SUB(CURDATE(), INTERVAL 12 DAY)),
    (2, 7, 8, DATE_SUB(CURDATE(), INTERVAL 5 DAY));

-- ============================================================
-- Disponibilites coachs
-- Certaines disponibilites sont deja reservees par des rendez-vous.
-- ============================================================

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (1, 10, '2026-06-01', '09:00:00', '10:00:00', b'1');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (2, 11, '2026-06-02', '15:00:00', '16:00:00', b'1');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (3, 10, '2026-06-04', '10:00:00', '11:00:00', b'1');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (4, 11, '2026-06-05', '14:00:00', '15:00:00', b'1');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (5, 12, '2026-06-06', '10:00:00', '11:00:00', b'1');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (6, 10, '2026-06-08', '18:00:00', '19:00:00', b'1');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (7, 11, '2026-06-10', '09:00:00', '10:00:00', b'1');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (8, 12, '2026-06-12', '16:00:00', '17:00:00', b'1');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (9, 10, '2026-06-16', '11:00:00', '12:00:00', b'1');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (10, 10, '2026-06-07', '15:00:00', '16:00:00', b'0');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (11, 11, '2026-06-11', '13:00:00', '14:00:00', b'0');

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (12, 12, '2026-06-15', '10:00:00', '11:00:00', b'0');

-- ============================================================
-- Rendez-vous
-- Statuts : 0 = en attente, 1 = confirme, 2 = fini,
-- 3 = annule par coach, 4 = annule par membre, 5 = annule par admin.
-- ============================================================

INSERT INTO appointment
    (id, member_id, availability_id, objective, room_id, status, cancellation_reason)
VALUES
    (1, 5, 1, 'Seance passee avec Camille', 1, 1, NULL);

INSERT INTO appointment
    (id, member_id, availability_id, objective, room_id, status, cancellation_reason)
VALUES
    (2, 6, 2, 'Seance yoga passee', 3, 1, NULL);

INSERT INTO appointment
    (id, member_id, availability_id, objective, room_id, status, cancellation_reason)
VALUES
    (3, 7, 3, 'Programme de reprise en musculation', 1, 1, NULL);

INSERT INTO appointment
    (id, member_id, availability_id, objective, room_id, status, cancellation_reason)
VALUES
    (4, 8, 4, 'Yoga et mobilite', 3, 1, NULL);

INSERT INTO appointment
    (id, member_id, availability_id, objective, room_id, status, cancellation_reason)
VALUES
    (5, 9, 5, 'Bilan nutrition et cardio', 4, 0, NULL);

INSERT INTO appointment
    (id, member_id, availability_id, objective, room_id, status, cancellation_reason)
VALUES
    (6, 5, 6, 'Correction technique squat', 1, 1, NULL);

INSERT INTO appointment
    (id, member_id, availability_id, objective, room_id, status, cancellation_reason)
VALUES
    (7, 6, 7, 'Pilates et gainage', 3, 0, NULL);

INSERT INTO appointment
    (id, member_id, availability_id, objective, room_id, status, cancellation_reason)
VALUES
    (8, 7, 8, 'Conseils nutrition et cardio', 4, 1, NULL);

INSERT INTO appointment
    (id, member_id, availability_id, objective, room_id, status, cancellation_reason)
VALUES
    (9, 8, 9, 'Suivi musculation', 1, 1, NULL);

SET SQL_SAFE_UPDATES = 1;