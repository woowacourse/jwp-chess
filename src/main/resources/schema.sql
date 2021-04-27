use mydb;

CREATE TABLE IF NOT EXISTS `room` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `current_turn` char(5) DEFAULT NULL,
  `room_name` varchar(20) NOT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8

CREATE TABLE IF NOT EXISTS `piece` (
  `piece_name` char(1) DEFAULT NULL,
  `piece_position` char(2) DEFAULT NULL,
  `room_id` int(11) NOT NULL,
  KEY `room_id` (`room_id`),
  CONSTRAINT `room_id` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8