CREATE TABLE `room` (
`id` bigint NOT NULL AUTO_INCREMENT,
`name` varchar(20) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `move_history` (
`id` bigint NOT NULL AUTO_INCREMENT,
`room` bigint NOT NULL,
`moves` int NOT NULL,
`team` varchar(5) NOT NULL,
`source_position` varchar(5) NOT NULL,
`target_position` varchar(5) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `board_status` (
`id` bigint NOT NULL AUTO_INCREMENT,
`room` bigint NOT NULL,
`position` varchar(2) NOT NULL,
`piece` varchar(1) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
