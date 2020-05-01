DROP TABLE IF EXISTS chess;
CREATE TABLE chess (
  room_id bigint(20) NOT NULL AUTO_INCREMENT,
  title varchar(45) DEFAULT NULL,
  board varchar(64) NOT NULL,
  is_white boolean NOT NULL,
  PRIMARY KEY (room_id)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
