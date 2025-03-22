CREATE TABLE IF NOT EXISTS `budget` (
                                        `budget_id` INT NOT NULL AUTO_INCREMENT,
                                        `customer_id` INT UNSIGNED NOT NULL,
                                        `valeur` DOUBLE PRECISION NOT NULL,
                                        `date_budget` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                                        PRIMARY KEY (`budget_id`),
    CONSTRAINT `budget_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `depenses` (
                                          `depense_id` INT NOT NULL AUTO_INCREMENT,
                                          `montant` DOUBLE NOT NULL,
                                          `date_depense` TIMESTAMP NOT NULL,
                                          `ticket_id` INT UNSIGNED DEFAULT NULL,
                                          `lead_id` INT UNSIGNED DEFAULT NULL,
                                          PRIMARY KEY (`depense_id`),
    CONSTRAINT `fk_depense_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `trigger_ticket` (`ticket_id`) ON DELETE SET NULL,
    CONSTRAINT `fk_depense_lead` FOREIGN KEY (`lead_id`) REFERENCES `trigger_lead` (`lead_id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;