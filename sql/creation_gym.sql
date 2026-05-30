-- ============================================
-- Creation de la base de donnees - Salle de sport
-- ============================================

CREATE DATABASE IF NOT EXISTS gym_db;
USE gym_db;

-- ============================================
-- DROP (ordre inverse)
-- ============================================
DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS coach_availability;
DROP TABLE IF EXISTS sponsorship;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS qualification;
DROP TABLE IF EXISTS access;
DROP TABLE IF EXISTS gym_member;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS coach;
DROP TABLE IF EXISTS subscription;
DROP TABLE IF EXISTS facility;
DROP TABLE IF EXISTS speciality;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS person;

-- ============================================
-- 1. TABLES SANS FK
-- ============================================

CREATE TABLE `person` (
    `id`            INT          NOT NULL AUTO_INCREMENT,
    `first_name`    VARCHAR(30)  NOT NULL,
    `last_name`     VARCHAR(50)  NOT NULL,
    `birth_date`    DATE         NOT NULL,
    `gender`        VARCHAR(10)  NOT NULL,
    `email`         VARCHAR(50)  NOT NULL,
    `phone`         VARCHAR(20)  DEFAULT NULL,
    `locker_number` SMALLINT     DEFAULT NULL,
    `username`      VARCHAR(50)  NOT NULL,
    `password`      VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_email`    (`email`),
    UNIQUE KEY `uq_username` (`username`),
    CONSTRAINT chk_gender        CHECK (`gender` IN ('MALE', 'FEMALE', 'OTHER')),
    CONSTRAINT chk_locker_number CHECK (`locker_number` IS NULL OR `locker_number` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `room` (
    `id`       TINYINT     NOT NULL AUTO_INCREMENT,
    `name`     VARCHAR(30) NOT NULL,
    `capacity` TINYINT     NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT chk_capacity CHECK (`capacity` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `speciality` (
    `name`        VARCHAR(25)  NOT NULL,
    `description` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `facility` (
    `name`        VARCHAR(50)  NOT NULL,
    `description` VARCHAR(300) NOT NULL,
    PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE subscription (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    duration_months INT NOT NULL,

    CHECK (type IN ('BASIC', 'STANDARD', 'PREMIUM')),
    CHECK (price > 0),
    CHECK (duration_months > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 2. SOUS-TYPES (dependent de person)
-- ============================================

CREATE TABLE `coach` (
    `person_id`  INT    NOT NULL,
    `has_degree` BIT(1) NOT NULL DEFAULT b'0',
    PRIMARY KEY (`person_id`),
    CONSTRAINT fk_coach_person FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `admin` (
    `person_id`    INT     NOT NULL,
    `access_level` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`person_id`),
    CONSTRAINT fk_admin_person    FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
    CONSTRAINT chk_access_level   CHECK (`access_level` >= 0 AND `access_level` <= 2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `gym_member` (
    `person_id`    INT          NOT NULL,
    `wants_locker` BIT(1)       NOT NULL DEFAULT b'0',
    `weight`       DECIMAL(5,2) NOT NULL,
    `height`       SMALLINT     NOT NULL,
    `enrollment`   INT          DEFAULT NULL,
    PRIMARY KEY (`person_id`),
    CONSTRAINT fk_member_person       FOREIGN KEY (`person_id`)  REFERENCES `person`       (`id`),
    CONSTRAINT fk_member_subscription FOREIGN KEY (`enrollment`) REFERENCES `subscription` (`id`),
    CONSTRAINT chk_weight             CHECK (`weight` > 0),
    CONSTRAINT chk_height             CHECK (`height` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 3. TABLES DE JONCTION SIMPLES
-- ============================================

CREATE TABLE `access` (
    `subscription_id` INT         NOT NULL,
    `facility_name`   VARCHAR(50) NOT NULL,
    PRIMARY KEY (`subscription_id`, `facility_name`),
    CONSTRAINT fk_access_subscription FOREIGN KEY (`subscription_id`) REFERENCES `subscription` (`id`),
    CONSTRAINT fk_access_facility     FOREIGN KEY (`facility_name`)   REFERENCES `facility`     (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `qualification` (
    `coach_id`        INT         NOT NULL,
    `speciality_name` VARCHAR(25) NOT NULL,
    PRIMARY KEY (`coach_id`, `speciality_name`),
    CONSTRAINT fk_qualification_coach      FOREIGN KEY (`coach_id`)        REFERENCES `coach`      (`person_id`),
    CONSTRAINT fk_qualification_speciality FOREIGN KEY (`speciality_name`) REFERENCES `speciality` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `payment` (
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `amount`       DECIMAL(6,2) NOT NULL,
    `date_payment` DATE         NOT NULL,
    `billing`      INT          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT fk_payment_member FOREIGN KEY (`billing`) REFERENCES `gym_member` (`person_id`),
    CONSTRAINT chk_amount        CHECK (`amount` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 4. PARRAINAGE (depend de gym_member)
-- ============================================

CREATE TABLE `sponsorship` (
    `id`           INT  NOT NULL AUTO_INCREMENT,
    `sponsor_id`   INT  NOT NULL,
    `sponsored_id` INT  NOT NULL,
    `start_date`   DATE NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_sponsored` (`sponsored_id`),
    CONSTRAINT fk_sponsor        FOREIGN KEY (`sponsor_id`)   REFERENCES `gym_member` (`person_id`),
    CONSTRAINT fk_sponsored      FOREIGN KEY (`sponsored_id`) REFERENCES `gym_member` (`person_id`),
    CONSTRAINT chk_not_self      CHECK (`sponsor_id` <> `sponsored_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 5. CALENDRIER COACH (depend de coach)
-- ============================================

CREATE TABLE `coach_availability` (
    `id`             INT    NOT NULL AUTO_INCREMENT,
    `person_id`      INT    NOT NULL,
    `available_date` DATE   NOT NULL,
    `start_time`     TIME   NOT NULL,
    `end_time`       TIME   NOT NULL,
    `is_booked`      BIT(1) NOT NULL DEFAULT b'0',
    PRIMARY KEY (`id`),
    CONSTRAINT fk_availability_coach FOREIGN KEY (`person_id`) REFERENCES `coach` (`person_id`),
    CONSTRAINT chk_times             CHECK (`end_time` > `start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 6. RENDEZ-VOUS
-- ============================================

CREATE TABLE `appointment` (
    `id`                  INT          NOT NULL AUTO_INCREMENT,
    `member_id`           INT          NOT NULL,
    `availability_id`     INT          NOT NULL,
    `objective`           VARCHAR(200) DEFAULT NULL,
    `room_id`             TINYINT      DEFAULT NULL,
    `status`              TINYINT      NOT NULL DEFAULT 0,
    -- 0 = en attente, 1 = confirme, 2 = fini,
    -- 3 = annule par coach, 4 = annule par membre, 5 = annule par admin
    `cancellation_reason` VARCHAR(200) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_availability_id` (`availability_id`),
    CONSTRAINT fk_appointment_member        FOREIGN KEY (`member_id`)       REFERENCES `gym_member`        (`person_id`),
    CONSTRAINT fk_appointment_availability  FOREIGN KEY (`availability_id`) REFERENCES `coach_availability` (`id`),
    CONSTRAINT fk_appointment_room          FOREIGN KEY (`room_id`)         REFERENCES `room`              (`id`),
    CONSTRAINT chk_status                   CHECK (`status` >= 0 AND `status` <= 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
