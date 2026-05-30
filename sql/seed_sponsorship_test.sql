-- Donnees de test pour ajouter un parrainage.
-- A executer apres seed_roles_test.sql si tu veux utiliser membrerole comme parrain.
-- Compte filleul cree :
-- filleulrole / filleulrole123

SET @sponsor_id = (SELECT id FROM person WHERE username = 'membrerole' LIMIT 1);
SET @old_sponsored_id = (SELECT id FROM person WHERE username = 'filleulrole' LIMIT 1);
SET @old_subscription_id = (
    SELECT enrollment
    FROM gym_member
    WHERE person_id = @old_sponsored_id
    LIMIT 1
);

DELETE FROM sponsorship
WHERE sponsor_id = @sponsor_id
   OR sponsored_id = @old_sponsored_id;

DELETE FROM payment WHERE billing = @old_sponsored_id;
DELETE FROM appointment WHERE member_id = @old_sponsored_id;
DELETE FROM gym_member WHERE person_id = @old_sponsored_id;
DELETE FROM person WHERE id = @old_sponsored_id;
DELETE FROM subscription
WHERE id = @old_subscription_id
  AND NOT EXISTS (
        SELECT person_id
        FROM gym_member
        WHERE enrollment = @old_subscription_id
  );

INSERT INTO person
    (first_name, last_name, birth_date, gender, email, phone, locker_number, username, password)
VALUES
    ('Filleul', 'Role', '2004-03-12', 'MALE', 'filleul.role@test.com', '+32470111104', 41, 'filleulrole', 'filleulrole123');

SET @sponsored_id = LAST_INSERT_ID();

INSERT INTO subscription (type, price, duration_months)
VALUES ('BASIC', 15.99, 1)
ON DUPLICATE KEY UPDATE
    id = LAST_INSERT_ID(id);

SET @subscription_id = LAST_INSERT_ID();

INSERT INTO gym_member (person_id, wants_locker, weight, height, enrollment)
VALUES (@sponsored_id, b'1', 74.00, 181, @subscription_id);

INSERT INTO sponsorship (sponsor_id, sponsored_id, start_date)
VALUES (@sponsor_id, @sponsored_id, CURDATE());

-- Test :
-- Connecte-toi avec filleulrole / filleulrole123.
-- Dans Mon compte, le parrainage doit indiquer membrerole.
