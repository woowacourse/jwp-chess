DROP TABLE IF EXISTS `chess`;

CREATE TABLE `chess`
(
    `game_id` varchar(100) NOT NULL,
    `data`    text         NOT NULL,
    PRIMARY KEY (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;