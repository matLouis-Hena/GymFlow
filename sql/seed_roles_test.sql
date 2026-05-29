-- Donnees de test pour la separation des vues par role.
-- Comptes crees :
-- adminrole / adminrole123
-- membrerole / membrerole123
-- coachrole / coachrole123

-- Specialites utiles pour le coach.
INSERT INTO speciality (name, description)
VALUES
    ('Musculation', 'Entrainement de force et renforcement musculaire'),
    ('Cardio', 'Entrainement endurance et condition physique')
ON DUPLICATE KEY UPDATE
    description = VALUES(description);

-- Salles utiles pour les tests de rendez-vous.
INSERT INTO room (id, name, capacity)
VALUES
    (81, 'Salle role cardio', 18),
    (82, 'Salle role coaching', 8)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    capacity = VALUES(capacity);

-- Nettoyage des anciennes donnees de demo.
SET @old_admin_id = (SELECT id FROM person WHERE username = 'adminrole' LIMIT 1);
SET @old_member_id = (SELECT id FROM person WHERE username = 'membrerole' LIMIT 1);
SET @old_coach_id = (SELECT id FROM person WHERE username = 'coachrole' LIMIT 1);
SET @old_subscription_id = (
    SELECT enrollment
    FROM gym_member
    WHERE person_id = @old_member_id
    LIMIT 1
);

DELETE FROM appointment
WHERE member_id = @old_member_id
   OR availability_id IN (
        SELECT id
        FROM coach_availability
        WHERE person_id = @old_coach_id
   );

DELETE FROM coach_availability WHERE person_id = @old_coach_id;
DELETE FROM qualification WHERE coach_id = @old_coach_id;
DELETE FROM sponsorship WHERE sponsor_id = @old_member_id OR sponsored_id = @old_member_id;
DELETE FROM payment WHERE billing = @old_member_id;
DELETE FROM gym_member WHERE person_id = @old_member_id;
DELETE FROM admin WHERE person_id = @old_admin_id;
DELETE FROM coach WHERE person_id = @old_coach_id;
DELETE FROM person WHERE id IN (@old_admin_id, @old_member_id, @old_coach_id);
DELETE FROM subscription
WHERE id = @old_subscription_id
  AND NOT EXISTS (
        SELECT person_id
        FROM gym_member
        WHERE enrollment = @old_subscription_id
  );

-- Admin.
INSERT INTO person
    (first_name, last_name, birth_date, gender, email, phone, locker_number, username, password)
VALUES
    ('Admin', 'Role', '1995-01-10', 'MALE', 'admin.role@test.com', '+32470111001', NULL, 'adminrole', 'adminrole123');

SET @admin_id = LAST_INSERT_ID();

INSERT INTO admin (person_id, access_level)
VALUES (@admin_id, 2);

-- Membre actif avec abonnement.
INSERT INTO person
    (first_name, last_name, birth_date, gender, email, phone, locker_number, username, password)
VALUES
    ('Membre', 'Role', '2002-04-15', 'FEMALE', 'membre.role@test.com', '+32470111002', 12, 'membrerole', 'membrerole123');

SET @member_id = LAST_INSERT_ID();

INSERT INTO subscription (type, price, duration_months)
VALUES ('STANDARD', 24.99, 1)
ON DUPLICATE KEY UPDATE
    id = LAST_INSERT_ID(id);

SET @subscription_id = LAST_INSERT_ID();

INSERT INTO gym_member (person_id, is_active, weight, height, enrollment)
VALUES (@member_id, b'1', 68.50, 172, @subscription_id);

-- Coach.
INSERT INTO person
    (first_name, last_name, birth_date, gender, email, phone, locker_number, username, password)
VALUES
    ('Coach', 'Role', '1990-07-20', 'OTHER', 'coach.role@test.com', '+32470111003', NULL, 'coachrole', 'coachrole123');

SET @coach_id = LAST_INSERT_ID();

INSERT INTO coach (person_id, has_degree)
VALUES (@coach_id, b'1');

INSERT INTO qualification (coach_id, speciality_name)
VALUES
    (@coach_id, 'Musculation'),
    (@coach_id, 'Cardio');

INSERT INTO coach_availability
    (person_id, available_date, start_time, end_time, is_booked)
VALUES
    (@coach_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', b'0'),
    (@coach_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '15:00:00', b'0'),
    (@coach_id, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '16:00:00', '17:00:00', b'0');
