
-- -----------------------------------------------------
-- Table `QUESTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `QUESTION` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `text` TEXT NOT NULL,
  `skill_level` INT NOT NULL,
  `question_type` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;
