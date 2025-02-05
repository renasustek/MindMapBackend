CREATE DATABASE IF NOT EXISTS `mind_map` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mind_map`;

CREATE TABLE IF NOT EXISTS `kanban_board` (
                                             `uuid` CHAR(36) NOT NULL,
                                             `name` VARCHAR(35) NOT NULL,
                                             PRIMARY KEY (`uuid`)
);

CREATE TABLE IF NOT EXISTS `task` (
                                      `uuid` CHAR(36) NOT NULL,
                                      `name` VARCHAR(35) NOT NULL,
                                      `description` VARCHAR(255),
                                      `eisenhower` ENUM('DO', 'DELEGATE', 'DECIDE', 'DELETE') NOT NULL,
                                      `label` VARCHAR(36),
                                      `due_date` DATE,
                                      `task_status` ENUM('TODO', 'INPROGRESS', 'DONE'),  -- ✅ Fixed ENUM Syntax
                                      PRIMARY KEY (`UUID`)
);

CREATE TABLE IF NOT EXISTS `label` (
                                       `uuid` CHAR(36) NOT NULL,
                                       `name` VARCHAR(35) NOT NULL,
                                       PRIMARY KEY (`uuid`)
);
