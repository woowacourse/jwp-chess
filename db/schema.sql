CREATE DATABASE chess DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

DROP TABLE IF EXISTS `Command`;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `Room`;

CREATE TABLE Room
(
    id int not null auto_increment,
    name varchar(100) not null,
    is_full boolean not null default false,
    PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE User
(
    id int not null auto_increment,
    password varchar(50) not null,
    room_id int not null,
    team varchar(10) not null default 'white',
    PRIMARY KEY(id),
    FOREIGN KEY(room_id) REFERENCES Room (id)
)ENGINE=InnoDB;

CREATE TABLE Command
(
    id int not null auto_increment,
    room_id int not null,
    data text not null,
    PRIMARY KEY(id),
    FOREIGN KEY(room_id) REFERENCES Room (id)
) ENGINE=InnoDB;

