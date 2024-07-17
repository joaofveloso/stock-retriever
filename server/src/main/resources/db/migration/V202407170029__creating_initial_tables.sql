-- stock.stock_metadata definition

CREATE TABLE `stock_metadata` (
                                  `symbol` varchar(10) NOT NULL,
                                  `information` varchar(255) DEFAULT NULL,
                                  `last_refreshed` date DEFAULT NULL,
                                  `output_size` varchar(50) DEFAULT NULL,
                                  `time_zone` varchar(50) DEFAULT NULL,
                                  PRIMARY KEY (`symbol`)
);

-- stock.daily_prices definition

CREATE TABLE `daily_prices` (
                                `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                `date` date NOT NULL,
                                `open` decimal(10,4) DEFAULT NULL,
                                `high` decimal(10,4) DEFAULT NULL,
                                `low` decimal(10,4) DEFAULT NULL,
                                `close` decimal(10,4) DEFAULT NULL,
                                `volume` int DEFAULT NULL,
                                `symbol` varchar(10) DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `id` (`id`),
                                UNIQUE KEY `unique_date_symbol` (`date`,`symbol`),
                                KEY `symbol` (`symbol`),
                                CONSTRAINT `daily_prices_ibfk_1` FOREIGN KEY (`symbol`) REFERENCES `stock_metadata` (`symbol`)
);