DROP TABLE `bq_unlocks`;
DROP TABLE `bq_logs`;

CREATE TABLE `bq_unlocks` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `target_uuid` TEXT NOT NULL,
    `unlocker_uuid` TEXT NOT NULL,
    `start_datetime` DATETIME NOT NULL,
    `end_datetime` DATETIME NOT NULL,
    `status` INT NOT NULL,
    `notice` TEXT NOT NULL,
    PRIMARY KEY (`id`) 
) COMMENT '{"version":"${project.version}"}';
CREATE TABLE `bq_logs` (
    `unlock_id` INT NOT NULL,
    `log` TEXT,
    PRIMARY KEY (`unlock_id`),
    FOREIGN KEY (`unlock_id`)
        REFERENCES `bq_unlocks`(`id`)
        ON DELETE CASCADE
) COMMENT '{"version":"${project.version}"}';
