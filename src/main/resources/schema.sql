CREATE TABLE `move_history` (
   `game_id` varchar(12) NOT NULL,
   `moves` int NOT NULL,
   `team` varchar(5) NOT NULL,
   `source_position` varchar(5) NOT NULL,
   `target_position` varchar(5) NOT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `board_status` (
   `game_id` varchar(12) NOT NULL,
   `position` varchar(2) NOT NULL,
   `piece` varchar(1) NOT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
