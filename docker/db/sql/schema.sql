CREATE DATABASE IF NOT EXISTS `mind_map` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mind_map`;

CREATE TABLE IF NOT EXISTS `kanban_board`
(
    `uuid` CHAR(36)    NOT NULL,
    `name` VARCHAR(35) NOT NULL,
    PRIMARY KEY (`uuid`)
);

CREATE TABLE IF NOT EXISTS `task`
(
    `uuid`        CHAR(36)                                    NOT NULL,
    `name`        VARCHAR(35)                                 NOT NULL,
    `description` VARCHAR(255),
    `eisenhower`  ENUM ('DO', 'DELEGATE', 'DECIDE', 'DELETE') NOT NULL,
    `label`       VARCHAR(36),
    `due_date`    DATE,
    `task_status` ENUM ('TODO', 'INPROGRESS', 'DONE'),
    PRIMARY KEY (`uuid`)
);

CREATE TABLE IF NOT EXISTS `goal`
(
    `uuid`              CHAR(36) NOT NULL,
    `kanban_board_id`   CHAR(36) NOT NULL,
    `specific_steps`    VARCHAR(255),
    `measure_progress`  VARCHAR(255),
    `is_goal_realistic` VARCHAR(255),
    `due_date`          DATE,
    PRIMARY KEY (`uuid`),
    FOREIGN KEY (`kanban_board_id`) REFERENCES `kanban_board` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `task_kanban`
(
    `task_kanban_id`    CHAR(36) NOT NULL,
    `kanban_board_uuid` CHAR(36) NOT NULL,
    `task_uuid`         CHAR(36) NOT NULL,
    PRIMARY KEY (`task_kanban_id`),
    FOREIGN KEY (`kanban_board_uuid`) REFERENCES `kanban_board` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`task_uuid`) REFERENCES `task` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `labels`
(
    `id` CHAR(36)    NOT NULL,
    `name`     VARCHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `chatbot_messages`
(
    `id` CHAR(36) NOT NULL,
    `message`            VARCHAR(255),
    `sentiment_score`            DECIMAL(5,4),
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `label_chatbotmessage`
(
    `label_chatbotmessage_id`    CHAR(36) NOT NULL,
    `label_id` CHAR(36) NOT NULL,
    `chatbot_message_id`         CHAR(36) NOT NULL,
    PRIMARY KEY (`label_chatbotmessage_id`),
    FOREIGN KEY (`label_id`) REFERENCES `labels` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`chatbot_message_id`) REFERENCES `chatbot_messages` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

