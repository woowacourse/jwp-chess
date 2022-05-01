drop table if exists board_pieces cascade;
drop table if exists games cascade;


create table games (
	game_id 	    char(50)   not null     primary key,
	last_team 		char(20),
	create_at 		datetime
)
ENGINE=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;


create table board_pieces (
	board_piece_id 	char(50)	not null 	primary key,
	game_id 	    char(50),
	`position` 	    char(5),
	piece 		    char(20),
	foreign key (game_id)
	  references games(game_id) on delete cascade
)
ENGINE=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
