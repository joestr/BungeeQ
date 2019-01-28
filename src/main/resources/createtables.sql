DROP TABLE `bu_unlocks`;

CREATE TABLE `bu_unlocks` (target_uuid TEXT, unlocker_uuid TEXT, start_datetime DATETIME, end_datetime DATETIME, status INT, notice TEXT);