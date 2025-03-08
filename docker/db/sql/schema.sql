CREATE DATABASE IF NOT EXISTS `mind_map` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mind_map`;

CREATE TABLE users (
                       id CHAR(36) PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role ENUM('ROLE_USER', 'ROLE_ADMIN') NOT NULL
);

-- Create Kanban Board Table
CREATE TABLE IF NOT EXISTS `kanban_board`
(
    `uuid` CHAR(36)    NOT NULL,
    `name` VARCHAR(35) NOT NULL,
    PRIMARY KEY (`uuid`)
);

-- Create Task Table
CREATE TABLE IF NOT EXISTS `task`
(
    `uuid`           CHAR(36)                                    NOT NULL,
    `name`           VARCHAR(35)                                 NOT NULL,
    `description`    VARCHAR(255),
    `eisenhower`     ENUM ('DO', 'DELEGATE', 'DECIDE', 'DELETE') NOT NULL,
    `label_id`       CHAR(36),
    `created_date`   DATE,
    `due_date`       DATE,
    `completed_date` DATE,
    `task_status`    ENUM ('TODO', 'INPROGRESS', 'DONE'),
    PRIMARY KEY (`uuid`)
);

-- Create Task-Kanban Relationship Table
CREATE TABLE IF NOT EXISTS `task_kanban`
(
    `task_kanban_id`    CHAR(36) NOT NULL,
    `kanban_board_uuid` CHAR(36) NOT NULL,
    `task_uuid`         CHAR(36) NOT NULL,
    PRIMARY KEY (`task_kanban_id`),
    FOREIGN KEY (`kanban_board_uuid`) REFERENCES `kanban_board` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`task_uuid`) REFERENCES `task` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create Goal Table
CREATE TABLE IF NOT EXISTS `goal`
(
    `uuid`              CHAR(36) NOT NULL,
    `kanban_board_id`   CHAR(36) NOT NULL,
    `specific_steps`    VARCHAR(255),
    `measure_progress`  VARCHAR(255),
    `is_goal_realistic` BOOLEAN,
    `due_date`          DATE,
    `completed_date`    DATE NULL,
    PRIMARY KEY (`uuid`),
    FOREIGN KEY (`kanban_board_id`) REFERENCES `kanban_board` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create Labels Table
CREATE TABLE IF NOT EXISTS `labels`
(
    `id`   CHAR(36)    NOT NULL,
    `name` VARCHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
);

-- Create Chatbot Messages Table
CREATE TABLE IF NOT EXISTS `chatbot_messages`
(
    `id`              CHAR(36) NOT NULL,
    `message`         VARCHAR(255),
    `sentiment_score` FLOAT(4),
    `entry_date`      DATE,
    PRIMARY KEY (`id`)
);

-- Create Label-ChatbotMessage Relationship Table
CREATE TABLE IF NOT EXISTS `label_chatbotmessage`
(
    `label_chatbotmessage_id` CHAR(36) NOT NULL,
    `label_id`                CHAR(36) NOT NULL,
    `chatbot_message_id`      CHAR(36) NOT NULL,
    PRIMARY KEY (`label_chatbotmessage_id`),
    FOREIGN KEY (`label_id`) REFERENCES `labels` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`chatbot_message_id`) REFERENCES `chatbot_messages` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `xp_points`
(
    `id` CHAR(36) NOT NULL,
    `xp_points`      INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);



-- Insert into Kanban Board (5 entries)
INSERT INTO `kanban_board` (`uuid`, `name`) VALUES
                                                ('74461839-f6af-45db-a1c9-1a89b33314d9', 'Project A'),
                                                ('2194efb9-22a3-4485-b97b-2445475b915b', 'Project B'),
                                                ('61284702-9222-48bf-8080-014c9fb37347', 'Project C'),
                                                ('3715d4f5-9dde-4be2-a496-826899eb9583', 'Project D'),
                                                ('0c9cdd69-0d66-424d-af83-2361a8ccd20c', 'Project E');

-- Insert into Goals (linked to Kanban Boards)
INSERT INTO `task` (`uuid`, `name`, `description`, `eisenhower`, `label_id`, `created_date`, `due_date`, `completed_date`, `task_status`)
VALUES
    ('0c9cdd69-0d66-424d-af83-2361a8ccd20c', 'Task 1', 'Description', 'DO', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -60 DAY), DATE_ADD(CURDATE(), INTERVAL -30 DAY), NULL, 'TODO'),
    ('d1bc3734-a213-44b8-8bbd-41fefddd4950', 'Task 2', 'Description', 'DO', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -59 DAY), DATE_ADD(CURDATE(), INTERVAL -29 DAY), NULL, 'TODO'),
    ('0d2089f4-7574-4be7-9ee2-84eb44181802', 'Task 3', 'Description', 'DO', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL -58 DAY), DATE_ADD(CURDATE(), INTERVAL -28 DAY), DATE_ADD(CURDATE(), INTERVAL -26 DAY), 'DONE'),
    ('66c798b3-60f1-4e58-bbad-dc2535632fde', 'Task 4', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL-57 DAY), DATE_ADD(CURDATE(), INTERVAL -27 DAY), DATE_ADD(CURDATE(), INTERVAL -27 DAY), 'DONE'),
    ('9acd56fb-b828-4146-86da-9afd5ed7cf55', 'Task 5', 'Description', 'DO', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -56 DAY), DATE_ADD(CURDATE(), INTERVAL -26 DAY), DATE_ADD(CURDATE(), INTERVAL -25 DAY), 'DONE'),
    ('fdde567c-ea0f-4ccc-8c49-664b85459d77', 'Task 6', 'Description', 'DO', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -55 DAY), DATE_ADD(CURDATE(), INTERVAL -27 DAY), NULL, 'TODO'),
    ('658df2b9-b143-4c8c-a948-b9af0b404831', 'Task 7', 'Description', 'DO', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -54 DAY), DATE_ADD(CURDATE(), INTERVAL -26 DAY), DATE_ADD(CURDATE(), INTERVAL -28 DAY), 'DONE'),
    ('e56016c4-61ee-4a0b-844b-61fe5613814b', 'Task 8', 'Description', 'DO', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL -53 DAY), DATE_ADD(CURDATE(), INTERVAL -25 DAY), NULL, 'TODO'),
    ('4ca5b6f1-a8cd-4832-8002-23e5c0563650', 'Task 9', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -52 DAY), DATE_ADD(CURDATE(), INTERVAL -24 DAY), DATE_ADD(CURDATE(), INTERVAL -25 DAY), 'DONE'),
    ('503923ee-c9ce-4f63-8575-705cbdf1c6b5', 'Task 10', 'Description', 'DO', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -51 DAY), DATE_ADD(CURDATE(), INTERVAL -23 DAY), NULL, 'TODO'),
    ('2afd804b-dd42-4491-9832-2eb0e73055f0', 'Task 12', 'Description', 'DO', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -50 DAY), DATE_ADD(CURDATE(), INTERVAL -22 DAY), DATE_ADD(CURDATE(), INTERVAL -21 DAY), 'DONE'),
    ('d17a8f93-e61a-4810-aa14-ec9724fddb66', 'Task 13', 'Description', 'DO', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -49 DAY), DATE_ADD(CURDATE(), INTERVAL -21 DAY), NULL, 'TODO'),
    ('6044b366-f6d0-4327-930e-4151e40aeed4', 'Task 14', 'Description', 'DO', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL-48 DAY), DATE_ADD(CURDATE(), INTERVAL -20 DAY), NULL, 'TODO'),
    ('f2db5882-9d0d-49f6-b0d2-6f101f342a92', 'Task 15', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -47 DAY), DATE_ADD(CURDATE(), INTERVAL -19 DAY), NULL, 'TODO'),
    ('ce1f55ff-45d2-49fd-8efa-8423b904887a', 'Task 16', 'Description', 'DO', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -46 DAY), DATE_ADD(CURDATE(), INTERVAL -18 DAY), DATE_ADD(CURDATE(), INTERVAL -17 DAY), 'DONE'),
    ('40a6b5a2-a958-4bd5-8bff-bba21fe196ed', 'Task 17', 'Description', 'DO', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -45 DAY), DATE_ADD(CURDATE(), INTERVAL -17 DAY), NULL, 'TODO'),
    ('189ac48b-bbae-4acd-b194-bbc3b7590e74', 'Task 18', 'Description', 'DO', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -44 DAY), DATE_ADD(CURDATE(), INTERVAL -16 DAY), DATE_ADD(CURDATE(), INTERVAL -15 DAY), 'DONE'),
    ('641ec024-f92f-44ec-8f25-74b2de330d56', 'Task 19', 'Description', 'DO', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL -43 DAY), DATE_ADD(CURDATE(), INTERVAL -15 DAY), DATE_ADD(CURDATE(), INTERVAL -13 DAY), 'DONE'),
    ('84a64052-8718-41fa-ae00-4060a9f93fc6', 'Task 20', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -42 DAY), DATE_ADD(CURDATE(), INTERVAL -14 DAY), NULL, 'TODO'),
    ('7b525c3c-69f5-43a8-a105-31137f3eb6d6', 'Task 21', 'Description', 'DO', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -41 DAY), DATE_ADD(CURDATE(), INTERVAL -13 DAY), DATE_ADD(CURDATE(), INTERVAL -10 DAY), 'DONE'),
    ('f4802098-66ec-433d-965b-c8cb66830190', 'Task 22', 'Description', 'DO', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -40 DAY), DATE_ADD(CURDATE(), INTERVAL -12 DAY), NULL, 'TODO'),
    ('ade4f4a6-e873-4ec7-8d55-6c072fcc931f', 'Task 23', 'Description', 'DO', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -39 DAY), DATE_ADD(CURDATE(), INTERVAL -11 DAY), DATE_ADD(CURDATE(), INTERVAL -9 DAY), 'DONE'),
    ('bb91e816-5bdd-4c67-9e1d-a230f700ab83', 'Task 24', 'Description', 'DO', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL -38 DAY), DATE_ADD(CURDATE(), INTERVAL -10 DAY), DATE_ADD(CURDATE(), INTERVAL -9 DAY), 'DONE'),
    ('7cf1f680-75d9-47bf-8021-934130b050fc', 'Task 25', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -37 DAY), DATE_ADD(CURDATE(), INTERVAL -9 DAY), DATE_ADD(CURDATE(), INTERVAL -9 DAY), 'DONE'),
    ('4b223398-9d74-491a-96c1-a55576b68f83', 'Task 26', 'Description', 'DO', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -36 DAY), DATE_ADD(CURDATE(), INTERVAL -8 DAY), DATE_ADD(CURDATE(), INTERVAL -9 DAY), 'DONE'),
    ('31ccea2b-96c4-4a0f-9d12-7bac1449d1a8', 'Task 27', 'Description', 'DELETE', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -35 DAY), DATE_ADD(CURDATE(), INTERVAL -7 DAY), NULL, 'TODO'),
    ('226ba312-8f4d-4aeb-9c3a-c52b2fe8d9ac', 'Task 28', 'Description', 'DELETE', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -34 DAY), DATE_ADD(CURDATE(), INTERVAL -6 DAY), NULL, 'TODO'),
    ('d5f223d2-46a9-41de-acb0-d6296861ea02', 'Task 29', 'Description', 'DELETE', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL -33 DAY), DATE_ADD(CURDATE(), INTERVAL -5 DAY), NULL, 'TODO'),
    ('101dd82a-f3da-4268-91a6-f59496fd9929', 'Task', 'Description', 'DECIDE', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -32 DAY), DATE_ADD(CURDATE(), INTERVAL -4 DAY), DATE_ADD(CURDATE(), INTERVAL -3 DAY), 'DONE'),
    ('68fe5aca-e303-4939-bdaa-e376b9270cd6', 'Task', 'Description', 'DECIDE', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -31 DAY), DATE_ADD(CURDATE(), INTERVAL -3 DAY), NULL, 'TODO'),
    ('25ee5925-2bd7-46a4-83b5-b8b0bd277068', 'Task', 'Description', 'DECIDE', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -30 DAY), DATE_ADD(CURDATE(), INTERVAL -2 DAY), DATE_ADD(CURDATE(), INTERVAL -3 DAY), 'DONE'),
    ('182abdea-6d9b-4505-bbc3-c02bfbc32a17', 'Task', 'Description', 'DECIDE', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -29 DAY), DATE_ADD(CURDATE(), INTERVAL -1 DAY), NULL, 'TODO'),
    ('9a02337c-ba39-4648-8603-69a05f7ed9c4', 'Task', 'Description', 'DECIDE', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL -28 DAY), CURDATE(), NULL, 'TODO'),
    ('9b9c2518-68bd-4a70-bee3-79c73085e25e', 'Task', 'Description', 'DECIDE', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -27 DAY), DATE_ADD(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'DONE'),
    ('500e254b-9c33-44d6-814d-56a40bf49939', 'Task', 'Description', 'DELETE', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -26 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'DONE'),
    ('a49f6298-3031-4111-bffe-1b77568edc26', 'Task', 'Description', 'DO', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -25 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'DONE'),
    ('fcec9013-be89-4d8f-bb58-349b678f3f2e', 'Task', 'Description', 'DO', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -24 DAY), DATE_ADD(CURDATE(), INTERVAL 4 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'DONE'),
    ('f7a1f547-ba98-401f-a9c6-35a2dde433b4', 'Task', 'Description', 'DO', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL -23 DAY), DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'DONE'),
    ('0e38d759-302f-439a-bd9d-0ac5521f3045', 'Task', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -22 DAY), DATE_ADD(CURDATE(), INTERVAL 6 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'DONE'),
    ('2c062385-dba5-4d96-9c1d-71df9672ab32', 'Task', 'Description', 'DO', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -21 DAY), DATE_ADD(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'DONE'),
    ('5d653a2d-f708-4c20-a7e3-80e9df7c3fe8', 'Task', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -20 DAY), DATE_ADD(CURDATE(), INTERVAL 8 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'DONE'),
    ('0b51bc6b-8f71-4262-bbd5-5d4c31ad5cb0', 'Task', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -19 DAY), DATE_ADD(CURDATE(), INTERVAL 9 DAY), NULL, 'TODO'),
    ('7e338af3-8a4c-4e74-baed-c59c7e14ff56', 'Taskhere', 'Description', 'DO', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -18 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), NULL, 'TODO'),
    ('58dbcf4e-92ba-4d05-9bc8-64a43cefb6e8', 'Task', 'Description', 'DO', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -17 DAY), DATE_ADD(CURDATE(), INTERVAL 11 DAY), DATE_ADD(CURDATE(), INTERVAL 9 DAY), 'DONE'),
    ('2541f992-aa32-4b79-aa4b-41b5524d68b4', 'Task', 'Description', 'DO', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -16 DAY), DATE_ADD(CURDATE(), INTERVAL 12 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'DONE'),
    ('acfb440f-a26c-4f5d-a6c8-c3a35902ebdd', 'Task', 'Description', 'DO', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL -15 DAY), DATE_ADD(CURDATE(), INTERVAL 13 DAY), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'DONE'),
    ('2315d6b2-ed2d-47b9-9fca-360aa411b03d', 'Task', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -14 DAY), DATE_ADD(CURDATE(), INTERVAL 14 DAY), DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'DONE'),
    ('568b2336-e8b9-44a7-9ede-e45ce22b7314', 'Task', 'Description', 'DO', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -13 DAY), DATE_ADD(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 16 DAY), 'DONE'),
    ('008cd80d-c2a5-404f-a3b0-6fd20e4d2afa', 'Task', 'Description', 'DO', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -12 DAY), DATE_ADD(CURDATE(), INTERVAL 16 DAY), DATE_ADD(CURDATE(), INTERVAL 9 DAY), 'DONE'),
    ('74aa3dd3-2014-4b7b-8bff-9ad5098b7629', 'Task', 'Description', 'DO', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -11 DAY), DATE_ADD(CURDATE(), INTERVAL 17 DAY), NULL, 'TODO'),
    ('f6c1a781-d62f-432c-b6c9-35461c93614b', 'Task', 'Description', 'DO', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -10 DAY), DATE_ADD(CURDATE(), INTERVAL 18 DAY), NULL, 'TODO'),
    ('008034cf-aef4-48c9-a100-b23edd9ff1d7', 'Task', 'Description', 'DO', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -9 DAY), DATE_ADD(CURDATE(), INTERVAL 19 DAY), DATE_ADD(CURDATE(), INTERVAL 17 DAY), 'DONE'),
    ('e22962e2-67c6-42c0-922b-c491e9ff1ad6', 'Task', 'Description', 'DELEGATE', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -8 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 18 DAY), 'DONE'),
    ('d90af5fc-24f0-4260-9577-6dee4d849973', 'Task', 'Description', 'DELEGATE', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -7 DAY), DATE_ADD(CURDATE(), INTERVAL 21 DAY), NULL, 'TODO'),
    ('9d03ce49-1ce9-4f29-a1d2-8fb1965def20', 'Task', 'Description', 'DELEGATE', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -6 DAY), DATE_ADD(CURDATE(), INTERVAL 22 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), 'DONE'),
    ('1958377c-654d-4ce8-bb5e-f438425fa5b5', 'Task', 'Description', 'DELEGATE', '9e064523-e95b-47ba-b2ba-51deefc11643', DATE_ADD(CURDATE(), INTERVAL -5 DAY), DATE_ADD(CURDATE(), INTERVAL 23 DAY), NULL, 'TODO'),
    ('daeb5c73-76f7-4fb7-ab8f-1f2d672472a5', 'Task', 'Description', 'DELEGATE', '8223d779-3570-4c8b-b66f-a2bd664681fd', DATE_ADD(CURDATE(), INTERVAL -4 DAY), DATE_ADD(CURDATE(), INTERVAL 24 DAY), DATE_ADD(CURDATE(), INTERVAL 22 DAY), 'DONE'),
    ('98ba0980-7272-4ac4-86e3-6b54288b5b21', 'Task', 'Description', 'DELEGATE', 'ebf56277-bc49-4985-9a81-5636ffee7636', DATE_ADD(CURDATE(), INTERVAL -3 DAY), DATE_ADD(CURDATE(), INTERVAL 25 DAY), DATE_ADD(CURDATE(), INTERVAL 24 DAY), 'DONE'),
    ('b7720d11-796d-4a80-894d-521bccd69890', 'Task', 'Description', 'DELEGATE', '9191c11f-07b9-44bf-8f07-e54515b5087a', DATE_ADD(CURDATE(), INTERVAL -2 DAY), DATE_ADD(CURDATE(), INTERVAL 26 DAY), NULL, 'TODO'),
    ('30542f4d-1cb5-4f2d-b125-ca4a7db37d46', 'Task', 'Description', 'DELEGATE', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', DATE_ADD(CURDATE(), INTERVAL -1 DAY), DATE_ADD(CURDATE(), INTERVAL 27 DAY), DATE_ADD(CURDATE(), INTERVAL 22 DAY), 'DONE'),
    ('0ae447dd-5c5a-4e47-971e-7d3057c8cf21', 'Task', 'Description', 'DELEGATE', '9e064523-e95b-47ba-b2ba-51deefc11643', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 28 DAY), NULL, 'TODO');


-- Insert into Labels (20 entries)
INSERT INTO `goal` (`uuid`, `kanban_board_id`, `specific_steps`, `measure_progress`, `is_goal_realistic`, `due_date`, `completed_date`)
VALUES
    ('14157ff6-d55a-4c9b-96ed-4f1f13893fe6', '74461839-f6af-45db-a1c9-1a89b33314d9', 'Step 1,2,3,4,5,6', 'Progress 1', TRUE, '2025-06-01', NULL),
    ('67e91ae4-21c4-40f0-85ca-f19460bae21d', '2194efb9-22a3-4485-b97b-2445475b915b', 'Step 1,2,3,4,5,6', 'Progress 2', TRUE, '2025-07-01', NULL),
    ('e6f4bf99-799a-4d7d-820d-d23cdd496492', '61284702-9222-48bf-8080-014c9fb37347', 'Step 1,2,3,4,5,6', 'Progress 3', FALSE, '2025-08-01', NULL),
    ('774483fc-259c-4bff-9a9e-2bed6c7496e9', '3715d4f5-9dde-4be2-a496-826899eb9583', 'Step 1,2,3,4,5,6', 'Progress 4', TRUE, '2025-09-01', NULL),
    ('f84627a1-f619-4eef-a3e5-81d06f60cc50', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', 'Step 1,2,3,4,5,6', 'Progress 5', FALSE, '2025-10-01', NULL);

-- Insert Tasks (50 tasks - 10 per Kanban Board)
INSERT INTO `labels` (`id`, `name`) VALUES
                                        ('c0e11773-a2ed-4bfd-917d-9782d48d9456', 'label1'),
                                        ('9e064523-e95b-47ba-b2ba-51deefc11643', 'label2'),
                                        ('8223d779-3570-4c8b-b66f-a2bd664681fd', 'label3'),
                                        ('ebf56277-bc49-4985-9a81-5636ffee7636', 'label4'),
                                        ('9191c11f-07b9-44bf-8f07-e54515b5087a', 'label5');

-- Insert Task-Kanban Relationships (Each Task linked to a Kanban Board)
INSERT INTO `task_kanban` (`task_kanban_id`, `kanban_board_uuid`, `task_uuid`)
VALUES
    ('fe8e7620-7f33-43c6-9ea1-cb7bc196b772', '74461839-f6af-45db-a1c9-1a89b33314d9', '0c9cdd69-0d66-424d-af83-2361a8ccd20c'),
    ('516dfb0b-7ba0-4ccc-ac6d-4ec25aebd2b4', '2194efb9-22a3-4485-b97b-2445475b915b', 'd1bc3734-a213-44b8-8bbd-41fefddd4950'),
    ('bed6a506-0e83-41c1-9f51-044542ec55cf', '61284702-9222-48bf-8080-014c9fb37347', '0d2089f4-7574-4be7-9ee2-84eb44181802'),
    ('764acc7b-78ec-4642-9215-6ef329cdf745', '3715d4f5-9dde-4be2-a496-826899eb9583', '66c798b3-60f1-4e58-bbad-dc2535632fde'),
    ('2ca60071-eb22-43f3-ab30-d437ae8af1fb', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '9acd56fb-b828-4146-86da-9afd5ed7cf55'),
    ('bcaa1718-4fde-4bf2-924d-8e1d1bd0af38', '74461839-f6af-45db-a1c9-1a89b33314d9', 'fdde567c-ea0f-4ccc-8c49-664b85459d77'),
    ('a113b12b-b3b9-44e5-9991-7ccb0809eda6', '2194efb9-22a3-4485-b97b-2445475b915b', '658df2b9-b143-4c8c-a948-b9af0b404831'),
    ('24776429-861a-4cd8-b82e-ead30b34446e', '61284702-9222-48bf-8080-014c9fb37347', 'e56016c4-61ee-4a0b-844b-61fe5613814b'),
    ('2a65c749-44c8-4477-8a60-94218d2b5310', '3715d4f5-9dde-4be2-a496-826899eb9583', '4ca5b6f1-a8cd-4832-8002-23e5c0563650'),
    ('1e6c427b-0fc1-4885-8ada-3fdd10e39ed4', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '503923ee-c9ce-4f63-8575-705cbdf1c6b5'),
    ('d8c80cce-eb00-4c19-91b8-c17018b71cf1', '74461839-f6af-45db-a1c9-1a89b33314d9', '2afd804b-dd42-4491-9832-2eb0e73055f0'),
    ('0bca8a6e-0ca9-4438-ab6c-f2111c576b58', '2194efb9-22a3-4485-b97b-2445475b915b', 'd17a8f93-e61a-4810-aa14-ec9724fddb66'),
    ('b7cd6a89-d834-4e17-9bbf-9830deeff593', '61284702-9222-48bf-8080-014c9fb37347', '6044b366-f6d0-4327-930e-4151e40aeed4'),
    ('5646bafc-d80f-4d0a-9252-1ee314b04ab3', '3715d4f5-9dde-4be2-a496-826899eb9583', 'f2db5882-9d0d-49f6-b0d2-6f101f342a92'),
    ('33f60787-b03a-4d03-8c03-1796a00c0b6d', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', 'ce1f55ff-45d2-49fd-8efa-8423b904887a'),
    ('8a20ef4e-fd37-403c-ae37-9831d53c32f8', '74461839-f6af-45db-a1c9-1a89b33314d9', '40a6b5a2-a958-4bd5-8bff-bba21fe196ed'),
    ('f043f68c-f811-4462-bc1d-7d100081a277', '2194efb9-22a3-4485-b97b-2445475b915b', '189ac48b-bbae-4acd-b194-bbc3b7590e74'),
    ('4488eeda-06fe-4602-9a84-f4e5ce85d792', '61284702-9222-48bf-8080-014c9fb37347', '641ec024-f92f-44ec-8f25-74b2de330d56'),
    ('a53f715b-6be4-4596-99a3-e5b54340fa2a', '3715d4f5-9dde-4be2-a496-826899eb9583', '84a64052-8718-41fa-ae00-4060a9f93fc6'),
    ('fe3587ce-86f8-447b-8642-297ee4994996', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '7b525c3c-69f5-43a8-a105-31137f3eb6d6'),
    ('96dea5de-f1bc-4565-b441-598b32a42d53', '74461839-f6af-45db-a1c9-1a89b33314d9', 'f4802098-66ec-433d-965b-c8cb66830190'),
    ('21cdef33-b0d3-4ad1-a7e0-14c4422dc47f', '2194efb9-22a3-4485-b97b-2445475b915b', 'ade4f4a6-e873-4ec7-8d55-6c072fcc931f'),
    ('c42f982e-13f8-40ac-89fc-b46c8d454e2f', '61284702-9222-48bf-8080-014c9fb37347', 'bb91e816-5bdd-4c67-9e1d-a230f700ab83'),
    ('75f81273-2a1c-4732-a971-36e38047f41f', '3715d4f5-9dde-4be2-a496-826899eb9583', '7cf1f680-75d9-47bf-8021-934130b050fc'),
    ('6670ff4c-9a72-41b8-940e-1fa6fb9c7553', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '4b223398-9d74-491a-96c1-a55576b68f83'),
    ('fb8086d6-b784-40eb-bb56-3560904d705c', '74461839-f6af-45db-a1c9-1a89b33314d9', '31ccea2b-96c4-4a0f-9d12-7bac1449d1a8'),
    ('7f8710fe-f334-4853-93d4-03eed3627422', '2194efb9-22a3-4485-b97b-2445475b915b', '226ba312-8f4d-4aeb-9c3a-c52b2fe8d9ac'),
    ('46809090-3478-4b6c-a894-ba39df00778a', '61284702-9222-48bf-8080-014c9fb37347', 'd5f223d2-46a9-41de-acb0-d6296861ea02'),
    ('270e4ab6-f213-4ae0-b49a-2d77ae6be04c', '3715d4f5-9dde-4be2-a496-826899eb9583', '101dd82a-f3da-4268-91a6-f59496fd9929'),
    ('4d0aa6c5-86dd-413c-89b3-c88ab76cac81', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '68fe5aca-e303-4939-bdaa-e376b9270cd6'),
    ('3e0d330f-abe0-48c4-a66d-42a2f2953ddb', '74461839-f6af-45db-a1c9-1a89b33314d9', '25ee5925-2bd7-46a4-83b5-b8b0bd277068'),
    ('5db3f10c-fe1f-4eee-b63e-20f2b4f71444', '2194efb9-22a3-4485-b97b-2445475b915b', '182abdea-6d9b-4505-bbc3-c02bfbc32a17'),
    ('5e7b864b-0b83-445a-9e27-7a4aa85b6180', '61284702-9222-48bf-8080-014c9fb37347', '9a02337c-ba39-4648-8603-69a05f7ed9c4'),
    ('88e15425-7617-461b-b4be-52c687f2e818', '3715d4f5-9dde-4be2-a496-826899eb9583', '9b9c2518-68bd-4a70-bee3-79c73085e25e'),
    ('6f1ba1e5-9fb4-47be-8d6d-bfcf85c4a9c8', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '500e254b-9c33-44d6-814d-56a40bf49939'),
    ('84025cc8-8863-4de2-84c6-edd9cebe493f', '74461839-f6af-45db-a1c9-1a89b33314d9', 'a49f6298-3031-4111-bffe-1b77568edc26'),
    ('9d18810c-be98-44d8-b3ac-b63a7926083c', '2194efb9-22a3-4485-b97b-2445475b915b', 'fcec9013-be89-4d8f-bb58-349b678f3f2e'),
    ('deea3736-5356-49bb-9d27-827bf3670211', '61284702-9222-48bf-8080-014c9fb37347', 'f7a1f547-ba98-401f-a9c6-35a2dde433b4'),
    ('94550fa9-a8bd-42bd-8ce7-8e9e5bfeb300', '3715d4f5-9dde-4be2-a496-826899eb9583', '0e38d759-302f-439a-bd9d-0ac5521f3045'),
    ('89456058-099b-475e-b7f6-7c4ca7bb38a4', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '2c062385-dba5-4d96-9c1d-71df9672ab32'),
    ('8efd2c1a-9b1d-4fb5-ada5-8b9d087a6a46', '3715d4f5-9dde-4be2-a496-826899eb9583', '5d653a2d-f708-4c20-a7e3-80e9df7c3fe8'),
    ('290cd181-becd-4358-b722-fca9796d3383', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '0b51bc6b-8f71-4262-bbd5-5d4c31ad5cb0'),
    ('7e338af3-8a4c-4e74-baed-c59c7e14ff56', '61284702-9222-48bf-8080-014c9fb37347', 'bb91e816-5bdd-4c67-9e1d-a230f700ab83'),
    ('6eccd97f-2c34-4f5b-a350-c528c3ca5d26', '3715d4f5-9dde-4be2-a496-826899eb9583', '7e338af3-8a4c-4e74-baed-c59c7e14ff56'),
    ('7b2ce1b9-66f9-4029-92c1-5bc6ff1d8db1', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '58dbcf4e-92ba-4d05-9bc8-64a43cefb6e8'),
    ('12fe889c-af72-4729-8348-92970ee22b2f', '74461839-f6af-45db-a1c9-1a89b33314d9', '2541f992-aa32-4b79-aa4b-41b5524d68b4'),
    ('78f22923-a601-4ace-a81b-06774bb7806f', '2194efb9-22a3-4485-b97b-2445475b915b', 'acfb440f-a26c-4f5d-a6c8-c3a35902ebdd'),
    ('1aabcd9a-ec14-4b18-bb3e-d69a14e052c4', '61284702-9222-48bf-8080-014c9fb37347', '2315d6b2-ed2d-47b9-9fca-360aa411b03d'),
    ('72bc558b-c070-4462-b0d0-51d2cff24943', '3715d4f5-9dde-4be2-a496-826899eb9583', '568b2336-e8b9-44a7-9ede-e45ce22b7314'),
    ('4f5777d8-e626-4a5d-9b2d-b4b0267f9dcf', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '008cd80d-c2a5-404f-a3b0-6fd20e4d2afa'),
    ('aa05e63e-3fe7-43e7-b63d-b90ef66103e0', '74461839-f6af-45db-a1c9-1a89b33314d9', '74aa3dd3-2014-4b7b-8bff-9ad5098b7629'),
    ('6a8531a5-6a2a-4ba2-b9da-4a1e74980a00', '2194efb9-22a3-4485-b97b-2445475b915b', 'f6c1a781-d62f-432c-b6c9-35461c93614b'),
    ('590deb35-cbc8-4b44-91bf-b8eca2df43d7', '61284702-9222-48bf-8080-014c9fb37347', '008034cf-aef4-48c9-a100-b23edd9ff1d7'),
    ('7474c643-37cc-43fa-9491-78a588b0cae4', '3715d4f5-9dde-4be2-a496-826899eb9583', 'e22962e2-67c6-42c0-922b-c491e9ff1ad6'),
    ('037fe469-83b8-462f-aa5c-e53086233dba', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', 'd90af5fc-24f0-4260-9577-6dee4d849973'),
    ('fe1fb5e3-b9b3-42ae-aab6-bdb3dc8a13cf', '74461839-f6af-45db-a1c9-1a89b33314d9', '9d03ce49-1ce9-4f29-a1d2-8fb1965def20'),
    ('2d0dc327-4d95-4d37-be98-dff2080ee4cd', '2194efb9-22a3-4485-b97b-2445475b915b', '1958377c-654d-4ce8-bb5e-f438425fa5b5'),
    ('a04e7bf9-adfe-47e9-808c-d5cf2eefcd61', '61284702-9222-48bf-8080-014c9fb37347', 'daeb5c73-76f7-4fb7-ab8f-1f2d672472a5'),
    ('08c660ed-d736-4c6d-a11a-9537444a0622', '3715d4f5-9dde-4be2-a496-826899eb9583', '98ba0980-7272-4ac4-86e3-6b54288b5b21'),
    ('7d3e06b1-4e48-4a46-8226-e693176d4e49', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', 'b7720d11-796d-4a80-894d-521bccd69890'),
    ('0e1497f0-237e-44a5-911a-47b7275f1620', '3715d4f5-9dde-4be2-a496-826899eb9583', '30542f4d-1cb5-4f2d-b125-ca4a7db37d46'),
    ('aac2b9df-f364-4e0f-904d-604f28d8c559', '0c9cdd69-0d66-424d-af83-2361a8ccd20c', '0ae447dd-5c5a-4e47-971e-7d3057c8cf21');


-- Insert Chatbot Messages (5 examples)

INSERT INTO `chatbot_messages` (`id`, `message`, `sentiment_score`, `entry_date`)
VALUES
    ('ae5dac01-a526-4851-9cc4-3604ac5b8766', 'This is frustrating!', -1.0, DATE_ADD(CURDATE(), INTERVAL -60 DAY)),
    ('c68cc395-a324-4b36-b670-77464f242422', 'This is frustrating!', -0.9, DATE_ADD(CURDATE(), INTERVAL -59 DAY)),
    ('60519672-faf5-4333-b5d2-19814651f51b', 'This is frustrating!', -0.8, DATE_ADD(CURDATE(), INTERVAL -58 DAY)),
    ('2ea0f5d6-839e-43fd-b0e4-512352bf69bf', 'This is frustrating!', -0.7, DATE_ADD(CURDATE(), INTERVAL -57 DAY)),
    ('f9245358-d8c6-4351-a807-d9aef6f06dbb', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -56 DAY)),
    ('6f69cba2-f58d-42ba-ae31-a98a31424382', 'This is frustrating!', -0.5, DATE_ADD(CURDATE(), INTERVAL -55 DAY)),
    ('afea0ff3-ab85-4c80-8980-4ba29949e30a', 'This is frustrating!', -0.4, DATE_ADD(CURDATE(), INTERVAL -54 DAY)),
    ('93e0736a-4974-4677-90bb-fe2d549848e5', 'This is frustrating!', -0.3, DATE_ADD(CURDATE(), INTERVAL -53 DAY)),
    ('b0bf55d2-238e-4a91-894c-0e7ea49e0740', 'This is frustrating!', -0.1, DATE_ADD(CURDATE(), INTERVAL -52 DAY)),
    ('789e0766-eeeb-4160-9dd5-3b5070c1f70f', 'This is frustrating!',  0.0, DATE_ADD(CURDATE(), INTERVAL -51 DAY)),
    ('a7b15c16-4f88-4461-895f-43ca2faec9db', 'This is frustrating!',  0.1, DATE_ADD(CURDATE(), INTERVAL -50 DAY)),
    ('e20ef467-0462-4e0a-bfaf-bfd04624baa3', 'This is frustrating!',  0.2, DATE_ADD(CURDATE(), INTERVAL -49 DAY)),
    ('a5e5e541-c2e3-468e-a0ab-9badcb7feec4', 'This is frustrating!',  0.2, DATE_ADD(CURDATE(), INTERVAL -48 DAY)),
    ('6d473ac4-4cc0-4733-b27c-0b21ac24cb45', 'This is frustrating!',  0.3, DATE_ADD(CURDATE(), INTERVAL -47 DAY)),
    ('5b176b73-d189-4af4-86b1-3e9fd99c9e8f', 'This is frustrating!',  0.4, DATE_ADD(CURDATE(), INTERVAL -46 DAY)),
    ('1bc299f6-9c6d-4b01-beca-635dadc7bdec', 'This is frustrating!',  0.5, DATE_ADD(CURDATE(), INTERVAL -45 DAY)),
    ('80be25df-4d92-47f5-85cb-838d8d923d14', 'This is frustrating!',  0.6, DATE_ADD(CURDATE(), INTERVAL -44 DAY)),
    ('e1d9e0a7-2b5d-4815-aca0-6378681c93fd', 'This is frustrating!',  0.7, DATE_ADD(CURDATE(), INTERVAL -43 DAY)),
    ('397c448c-6bef-45ea-a6d1-992cafef8090', 'This is frustrating!',  0.8, DATE_ADD(CURDATE(), INTERVAL -42 DAY)),
    ('176c518c-249a-451d-92a0-3d7219965254', 'This is frustrating!',  0.9, DATE_ADD(CURDATE(), INTERVAL -41 DAY)),
    ('b7af4d88-95d8-47ec-a787-5699b9e20149', 'This is frustrating!',  1.0, DATE_ADD(CURDATE(), INTERVAL -40 DAY)),
    ('45653511-a1c3-44a7-a601-3b6a809c658c', 'This is frustrating!',  0.9, DATE_ADD(CURDATE(), INTERVAL -39 DAY)),
    ('8e443c35-b89d-4a13-a72e-0597f1b165d0', 'This is frustrating!',  0.8, DATE_ADD(CURDATE(), INTERVAL -38 DAY)),
    ('7092f5d6-5fe1-4175-bf90-ed85b1479424', 'This is frustrating!',  0.7, DATE_ADD(CURDATE(), INTERVAL -37 DAY)),
    ('b9184529-8b53-4835-ae71-f93bde4b3ccc', 'This is frustrating!',  0.6, DATE_ADD(CURDATE(), INTERVAL -36 DAY)),
    ('a0ed9872-4dc6-4326-8ecc-a292bd9aa47a', 'This is frustrating!',  0.5, DATE_ADD(CURDATE(), INTERVAL -35 DAY)),
    ('eed79e8f-2942-4d4b-82cc-09b7a07d44de', 'This is frustrating!',  0.4, DATE_ADD(CURDATE(), INTERVAL -34 DAY)),
    ('916a285e-81bc-43f2-9394-3e46f9e1dabf', 'This is frustrating!',  0.3, DATE_ADD(CURDATE(), INTERVAL -33 DAY)),
    ('0a640dbd-88d7-4f08-98d0-584095933d35', 'This is frustrating!',  0.2, DATE_ADD(CURDATE(), INTERVAL -32 DAY)),
    ('833e9b34-757e-4ccd-b58c-2215f6485368', 'This is frustrating!',  0.1, DATE_ADD(CURDATE(), INTERVAL -31 DAY)),
    ('63cd87bd-8875-4cee-8c1e-ba8f678c4af7', 'This is frustrating!',  0.0, DATE_ADD(CURDATE(), INTERVAL -30 DAY)),
    ('e16c52e3-c277-455a-b61c-b2b62b94e03a', 'This is frustrating!', -0.9, DATE_ADD(CURDATE(), INTERVAL -29 DAY)),
    ('121cfe31-f375-496d-a758-f5f53db6d220', 'This is frustrating!', -0.8, DATE_ADD(CURDATE(), INTERVAL -28 DAY)),
    ('c0ae682f-4b3b-4839-84e5-a3d56c1f7103', 'This is frustrating!', -0.85, DATE_ADD(CURDATE(), INTERVAL -27 DAY)),
    ('dd4a33ff-45ce-4ac7-a6b7-9fda311118ec', 'This is frustrating!', -0.7, DATE_ADD(CURDATE(), INTERVAL -26 DAY)),
    ('35e3acba-8caa-4604-8bf1-4fbcda35ddf2', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -25 DAY)),
    ('e8db50b7-72aa-466e-9c6f-c146363552f5', 'This is frustrating!', -0.7, DATE_ADD(CURDATE(), INTERVAL -24 DAY)),
    ('0d136227-47be-4a14-93f9-466961b11636', 'This is frustrating!', -0.8, DATE_ADD(CURDATE(), INTERVAL -23 DAY)),
    ('fb83371f-11c1-4bcf-8f15-5d319a6e227c', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -22 DAY)),
    ('49036ecb-9929-44d4-9378-3f569f7e742c', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -21 DAY)),
    ('4ede8575-e9d4-4592-b736-c9fa8340b395', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -20 DAY)),
    ('623897f6-5b8c-4e3a-acc0-614f382802dc', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -19 DAY)),
    ('d7ac91e0-8f94-43a6-bbd1-77cfa4572994', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -18 DAY)),
    ('43b3e631-7b91-4c52-a775-82bd7fffc32f', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -17 DAY)),
    ('9c523331-ee14-4550-a5c1-13e36e41e115', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -16 DAY)),
    ('5f9bcc81-738a-4a89-a39d-1831630057e7', 'This is frustrating!', -0.6, DATE_ADD(CURDATE(), INTERVAL -15 DAY)),
    ('dd5f7fe0-e394-4bfa-a36f-7aa349b1cd4e', 'This is frustrating!',  0.6, DATE_ADD(CURDATE(), INTERVAL -14 DAY)),
    ('6715a1dc-8cbb-421c-bae4-e789983b39a1', 'This is frustrating!',  0.6, DATE_ADD(CURDATE(), INTERVAL -13 DAY)),
    ('8709397f-766c-4974-b163-4f68d9e89a97', 'This is frustrating!',  0.6, DATE_ADD(CURDATE(), INTERVAL -12 DAY)),
    ('e5616e55-f651-401c-bbeb-0287cbc16b88', 'This is frustrating!',  0.1, DATE_ADD(CURDATE(), INTERVAL -11 DAY)),
    ('ac114265-f770-42a3-8983-b9a3d4113861', 'This is frustrating!',  0.2, DATE_ADD(CURDATE(), INTERVAL -10 DAY)),
    ('ab4fe2c8-7a57-43f6-b083-7061f0678050', 'This is frustrating!',  0.1, DATE_ADD(CURDATE(), INTERVAL -9 DAY)),
    ('57b5c70d-dcc5-43a6-a2a7-ffaafa7c9250', 'This is frustrating!',  0.2, DATE_ADD(CURDATE(), INTERVAL -8 DAY)),
    ('b07de6ae-1523-4a01-b864-59fedd7bb727', 'This is frustrating!',  0.3, DATE_ADD(CURDATE(), INTERVAL -7 DAY)),
    ('564eced4-0419-4d74-b600-86a27f1ec03a', 'This is frustrating!',  0.4, DATE_ADD(CURDATE(), INTERVAL -6 DAY)),
    ('26bbf4b1-dc67-40fa-932e-7d5f7b03e6d0', 'This is frustrating!',  0.35, DATE_ADD(CURDATE(), INTERVAL -5 DAY)),
    ('fe702f86-e34f-4d8d-9d74-0dba518eda9b', 'This is frustrating!',  0.12, DATE_ADD(CURDATE(), INTERVAL -4 DAY)),
    ('46502628-7988-4e70-bb24-3ed59ec4b728', 'This is frustrating!',  0.154, DATE_ADD(CURDATE(), INTERVAL -3 DAY)),
    ('b7dd0758-89e1-4762-b21e-6418b2915c05', 'This is frustrating!',  0.24, DATE_ADD(CURDATE(), INTERVAL -2 DAY)),
    ('711cc6ca-fd8d-4a45-9dde-566fc1e2adb1', 'This is frustrating!',  0.245, DATE_ADD(CURDATE(), INTERVAL -1 DAY)),
    ('e39c41c2-8635-4912-ac04-59478029f0cf', 'This is frustrating!',  0.324, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('aff11185-c080-46be-8c02-ecbe84f818a7', 'This is frustrating!',  0.12, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('7356cde7-d05f-4505-8184-4305305f3247', 'This is frustrating!',  0.128, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('f146c8d8-06f9-4c1f-a2a4-787d99f18c63', 'This is frustrating!',  0.1, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('6ac75fee-fc2d-4724-97ae-5e3e2ad269b0', 'This is frustrating!',  0.2, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('cf4b674b-3a18-448a-9953-aedc3e656bfe', 'This is frustrating!',  0.3, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('3d3dcabc-c4c8-4fa7-a06a-5a0ebb48231f', 'This is frustrating!',  0.4, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('cddf3dce-6fd6-49ec-822e-7903761c2e8f', 'This is frustrating!', -0.2, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('2cc7b574-d85d-4fa5-81b4-c5aa9921d2e0', 'This is frustrating!', -0.5, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('f0d62589-2971-4657-a4a4-f21cd84b9504', 'This is frustrating!', -0.4, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('a9182a9c-d4d0-4ce8-9a0e-4059176317cb', 'This is frustrating!', -0.3, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('9844cd84-e0bc-4da3-9053-9b1388f51932', 'This is frustrating!', -0.9, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('74a3e0f1-fe90-491c-a152-106e8e4fb6fb', 'This is frustrating!', -0.8, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('35fe8d16-79ad-4119-8bcf-8cff8e5f0386', 'This is frustrating!', -0.4, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('028a5928-8c26-49ab-9367-59ee3ed7b9c3', 'This is frustrating!', -0.7, DATE_ADD(CURDATE(), INTERVAL 0 DAY)),
    ('5fe383b0-9d82-4fc1-b85a-c3b99bc769e7', 'This is frustrating!', -0.2, DATE_ADD(CURDATE(), INTERVAL 0 DAY));


INSERT INTO `label_chatbotmessage` (`label_chatbotmessage_id`, `label_id`, `chatbot_message_id`)
VALUES
    ('4a8a9d28-5e71-40ef-86a2-dfd60db9e8f3', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', 'ae5dac01-a526-4851-9cc4-3604ac5b8766'),
    ('897c3675-8790-4289-a23a-9d7764b7024e', '9e064523-e95b-47ba-b2ba-51deefc11643', 'c68cc395-a324-4b36-b670-77464f242422'),
    ('8a4b9973-898a-43df-b386-f4790cda39ad', '8223d779-3570-4c8b-b66f-a2bd664681fd', '60519672-faf5-4333-b5d2-19814651f51b'),
    ('678da6d2-1158-485d-8a24-4b86dc8800f1', 'ebf56277-bc49-4985-9a81-5636ffee7636', '2ea0f5d6-839e-43fd-b0e4-512352bf69bf'),
    ('b1bb9a46-308c-443a-8738-6f6e837fa6c1', '9191c11f-07b9-44bf-8f07-e54515b5087a', 'f9245358-d8c6-4351-a807-d9aef6f06dbb'),
    ('e7bc6a28-3a03-47f7-9d63-4fe14a3212a6', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', '6f69cba2-f58d-42ba-ae31-a98a31424382'),
    ('7578959b-ac7c-48c5-b32e-2bc1bbf5c571', '9e064523-e95b-47ba-b2ba-51deefc11643', 'afea0ff3-ab85-4c80-8980-4ba29949e30a'),
    ('ed34f50d-654e-460e-81dc-5a6f7080e167', '8223d779-3570-4c8b-b66f-a2bd664681fd', '93e0736a-4974-4677-90bb-fe2d549848e5'),
    ('f2ad56b8-fb66-4d89-8f21-88da6df587c0', 'ebf56277-bc49-4985-9a81-5636ffee7636', 'b0bf55d2-238e-4a91-894c-0e7ea49e0740'),
    ('1b4fa8d1-defa-4425-81bf-1ab1cc01c545', '9191c11f-07b9-44bf-8f07-e54515b5087a', '789e0766-eeeb-4160-9dd5-3b5070c1f70f'),
    ('ce3f28a1-02c7-4ce3-b155-f8d8367ed96a', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', 'a7b15c16-4f88-4461-895f-43ca2faec9db'),
    ('f4704ed2-6ab5-4ca3-914a-61b313007613', '9e064523-e95b-47ba-b2ba-51deefc11643', 'e20ef467-0462-4e0a-bfaf-bfd04624baa3'),
    ('11e9f615-1f4d-431a-b154-05cf235a10b6', '8223d779-3570-4c8b-b66f-a2bd664681fd', 'a5e5e541-c2e3-468e-a0ab-9badcb7feec4'),
    ('ed134a66-8e5e-4f5e-8180-62fab46932a5', 'ebf56277-bc49-4985-9a81-5636ffee7636', '6d473ac4-4cc0-4733-b27c-0b21ac24cb45'),
    ('a8cdbf90-9dd8-459c-8dac-59e1f2e8578c', '9191c11f-07b9-44bf-8f07-e54515b5087a', '5b176b73-d189-4af4-86b1-3e9fd99c9e8f'),
    ('943da81a-da85-419b-9413-6a8e513e2609', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', '1bc299f6-9c6d-4b01-beca-635dadc7bdec'),
    ('b9b459c4-6547-4bf8-860a-6bdbe703bad1', '9e064523-e95b-47ba-b2ba-51deefc11643', '80be25df-4d92-47f5-85cb-838d8d923d14'),
    ('e283a4be-13a4-4a7f-a10f-6e5e2be3436c', '8223d779-3570-4c8b-b66f-a2bd664681fd', 'e1d9e0a7-2b5d-4815-aca0-6378681c93fd'),
    ('9c83b0ab-84da-4f92-bf4d-0508579dbcdd', 'ebf56277-bc49-4985-9a81-5636ffee7636', '397c448c-6bef-45ea-a6d1-992cafef8090'),
    ('e0e707de-5a86-4988-a64e-98acdde68089', '9191c11f-07b9-44bf-8f07-e54515b5087a', '176c518c-249a-451d-92a0-3d7219965254'),
    ('3fa4383c-c638-43d7-9869-8718ba87c407', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', 'b7af4d88-95d8-47ec-a787-5699b9e20149'),
    ('0dc79151-7d67-43e3-aebd-5716b3ad3b80', '9e064523-e95b-47ba-b2ba-51deefc11643', '45653511-a1c3-44a7-a601-3b6a809c658c'),
    ('3e081479-dc3a-48a9-a7c8-67ff7f804a34', '8223d779-3570-4c8b-b66f-a2bd664681fd', '8e443c35-b89d-4a13-a72e-0597f1b165d0'),
    ('5df0c58b-1fb7-4c3b-bfcf-09974b832b50', 'ebf56277-bc49-4985-9a81-5636ffee7636', '7092f5d6-5fe1-4175-bf90-ed85b1479424'),
    ('d6d51e00-35e6-40c3-bc7f-09e0699f9373', '9191c11f-07b9-44bf-8f07-e54515b5087a', 'b9184529-8b53-4835-ae71-f93bde4b3ccc'),
    ('1af13393-71e9-4131-b2ce-538e2ddb4cfc', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', 'a0ed9872-4dc6-4326-8ecc-a292bd9aa47a'),
    ('bb9d7cec-ad96-4b7b-bdd3-e3780e477b22', '9e064523-e95b-47ba-b2ba-51deefc11643', 'eed79e8f-2942-4d4b-82cc-09b7a07d44de'),
    ('c31f72ed-7b85-46ce-ab25-65f08cb39fc4', '8223d779-3570-4c8b-b66f-a2bd664681fd', '916a285e-81bc-43f2-9394-3e46f9e1dabf'),
    ('d8f3f1ce-1620-4ae2-b8a4-aaedb4b7a680', 'ebf56277-bc49-4985-9a81-5636ffee7636', '0a640dbd-88d7-4f08-98d0-584095933d35'),
    ('19295f78-576e-473b-8330-e5816f2cf823', '9191c11f-07b9-44bf-8f07-e54515b5087a', '833e9b34-757e-4ccd-b58c-2215f6485368'),
    ('58875bcd-4d6a-4952-af78-9acb2ab173fe', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', '63cd87bd-8875-4cee-8c1e-ba8f678c4af7'),
    ('8bb67e7e-5d81-4ab6-a0e5-6c3a6fe790b6', '9e064523-e95b-47ba-b2ba-51deefc11643', 'e16c52e3-c277-455a-b61c-b2b62b94e03a'),
    ('c67c35a6-f1ec-419a-a1dc-3a06f8cc21f0', '8223d779-3570-4c8b-b66f-a2bd664681fd', '121cfe31-f375-496d-a758-f5f53db6d220'),
    ('3df38782-0c50-43d8-97c4-091e5dd9b9f1', 'ebf56277-bc49-4985-9a81-5636ffee7636', 'c0ae682f-4b3b-4839-84e5-a3d56c1f7103'),
    ('d86dbd2f-7873-4efc-8f6d-b3a1fdd9a4d8', '9191c11f-07b9-44bf-8f07-e54515b5087a', 'dd4a33ff-45ce-4ac7-a6b7-9fda311118ec'),
    ('83fa4f80-680d-429b-8dbc-b1dbe75088bd', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', '35e3acba-8caa-4604-8bf1-4fbcda35ddf2'),
    ('f790b9e8-8a65-4072-9307-35bf8426f100', '9e064523-e95b-47ba-b2ba-51deefc11643', 'e8db50b7-72aa-466e-9c6f-c146363552f5'),
    ('aecfdb09-0bf9-476d-8756-936ca2a6c736', '8223d779-3570-4c8b-b66f-a2bd664681fd', '0d136227-47be-4a14-93f9-466961b11636'),
    ('289fc19a-33cb-4d96-8563-dc9801fd1f52', 'ebf56277-bc49-4985-9a81-5636ffee7636', 'fb83371f-11c1-4bcf-8f15-5d319a6e227c'),
    ('86977abb-e35d-4c26-88f1-982a1b1973dd', '9191c11f-07b9-44bf-8f07-e54515b5087a', '49036ecb-9929-44d4-9378-3f569f7e742c'),
    ('33e6310b-f090-411d-ac0d-c33abd819401', '8223d779-3570-4c8b-b66f-a2bd664681fd', '4ede8575-e9d4-4592-b736-c9fa8340b395'),
    ('b348fe64-5be2-4dc9-9404-55d3de75e0ec', '9191c11f-07b9-44bf-8f07-e54515b5087a', '623897f6-5b8c-4e3a-acc0-614f382802dc'),
    ('4257c997-4d81-409d-97eb-0a216f43277d', 'c0e11773-a2ed-4bfd-917d-9782d48d9456', 'd7ac91e0-8f94-43a6-bbd1-77cfa4572994'),
    ('f30aead1-16a3-49ee-b42d-937a36d4137e', '9e064523-e95b-47ba-b2ba-51deefc11643', '43b3e631-7b91-4c52-a775-82bd7fffc32f'),
    ('56cc6ef0-0204-48df-b64a-100dd1d52834', '8223d779-3570-4c8b-b66f-a2bd664681fd', '9c523331-ee14-4550-a5c1-13e36e41e115'),
    ('822b52a6-afc6-48f4-aeee-feb0012f19cc', 'ebf56277-bc49-4985-9a81-5636ffee7636', '5f9bcc81-738a-4a89-a39d-1831630057e7');

