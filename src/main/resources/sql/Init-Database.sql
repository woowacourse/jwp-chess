CREATE DATABASE wooteco.chess DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE wooteco.chess;

CREATE TABLE piece (
	position VARCHAR(2) NOT NULL,
	name VARCHAR(1) NOT NULL,
	PRIMARY KEY (position)
);

CREATE TABLE state (
id int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
turn varchar(5)
);

-- USE chess2;
-- CREATE TABLE chessgame (
-- 	id int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
-- 	board VARCHAR(64) NOT NULL,
--     turn VARCHAR(5) NOT NULL
-- );
