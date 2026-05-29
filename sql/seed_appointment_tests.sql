-- Donnees de test pour la prise de rendez-vous
-- A executer dans la base du projet.
-- Le script ne vide pas toute la base : il ajoute/actualise seulement des donnees de demo.

START TRANSACTION;

-- Specialites
INSERT INTO speciality (name, description)
VALUES
    ('Musculation', 'Entrainement de force et renforcement musculaire'),
    ('Yoga', 'Cours de souplesse et respiration')
ON DUPLICATE KEY UPDATE
    description = VALUES(description);

-- Salles de test avec identifiants fixes
INSERT INTO room (id, name, capacity)
VALUES
    (91, 'Salle test cardio', 20),
    (92, 'Salle test coaching', 8)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    capacity = VALUES(capacity);

-- Coach de test
INSERT INTO person
    (first_name, last_name, birth_date, gender, email, phone, locker_number, username, password)
VALUES
    ('Coach', 'Demo', '1990-01-10', 'MALE', 'coach.demo@test.com', '+32470111222', NULL, 'coachdemo', 'coachdemo123')
ON DUPLICATE KEY UPDATE
    id = LAST_INSERT_ID(id),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    birth_date = VALUES(birth_date),
    gender = VALUES(gender),
    phone = VALUES(phone),
    locker_number = VALUES(locker_number),
    password = VALUES(password);

SET @coach_id = LAST_INSERT_ID();

INSERT INTO coach (person_id, has_degree)
VALUES (@coach_id, b'1')
ON DUPLICATE KEY UPDATE
    has_degree = VALUES(has_degree);

INSERT INTO qualification (coach_id, speciality_name)
VALUES
    (@coach_id, 'Musculation'),
    (@coach_id, 'Yoga')
ON DUPLICATE KEY UPDATE
    speciality_name = VALUES(speciality_name);

-- Membre de test
INSERT INTO person
    (first_name, last_name, birth_date, gender, email, phone, locker_number, username, password)
VALUES
    ('Membre', 'Demo', '2001-05-12', 'FEMALE', 'membre.demo@test.com', '+32470111333', 33, 'membredemo', 'membredemo123')
ON DUPLICATE KEY UPDATE
    id = LAST_INSERT_ID(id),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    birth_date = VALUES(birth_date),
    gender = VALUES(gender),
    phone = VALUES(phone),
    locker_number = VALUES(locker_number),
    password = VALUES(password);

SET @member_id = LAST_INSERT_ID();

-- Abonnement de test.
-- Si la colonne type est unique dans votre BD, cette ligne reutilise l'abonnement STANDARD existant
-- sans modifier son prix ou sa duree.
INSERT INTO subscription (type, price, duration_months)
VALUES ('STANDARD', 24.99, 1)
ON DUPLICATE KEY UPDATE
    id = LAST_INSERT_ID(id);

SET @subscription_id = LAST_INSERT_ID();

INSERT INTO gym_member (person_id, is_active, weight, height, enrollment)
VALUES (@member_id, b'1', 70.50, 175, @subscription_id)
ON DUPLICATE KEY UPDATE
    is_active = b'1',
    weight = VALUES(weight),
    height = VALUES(height),
    enrollment = VALUES(enrollment);

-- Creneaux fixes de test.
-- On nettoie les rendez-vous de demo lies a ces creneaux pour pouvoir relancer le script.
DELETE FROM appointment
WHERE availability_id IN (9101, 9102, 9103);

INSERT INTO coach_availability
    (id, person_id, available_date, start_time, end_time, is_booked)
VALUES
    (9101, @coach_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', b'0'),
    (9102, @coach_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '15:00:00', b'0'),
    (9103, @coach_id, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '16:00:00', '17:00:00', b'0')
ON DUPLICATE KEY UPDATE
    person_id = VALUES(person_id),
    available_date = VALUES(available_date),
    start_time = VALUES(start_time),
    end_time = VALUES(end_time),
    is_booked = b'0';

COMMIT;

-- Compte a utiliser dans l'application :
-- username : membredemo
-- password : membredemo123
-- Specialites a tester : Musculation ou Yoga
-- Dates : de demain a dans 3 jours
