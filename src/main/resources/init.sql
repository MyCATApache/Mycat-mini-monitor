CREATE TABLE IF NOT EXISTS `jmx_metric`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `ip` VARCHAR(16),
  `used_memory` BIGINT,
  `committed_memory` BIGINT,
  `max_memory` BIGINT,
  `process_cpu_load` DECIMAL(4,2),
  `system_cpu_load` DECIMAL(4,2),
  `system_load_average` DECIMAL(4,2),
  `duration` INT,
  `created_date` DATETIME,
  `last_modified_date` DATETIME,
  PRIMARY KEY (`id`)
);