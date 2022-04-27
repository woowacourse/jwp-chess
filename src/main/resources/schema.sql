create table games (
	game_id 	char(50),
	last_team 		char(20),
	create_at 		datetime,
	primary key(game_id)
)ENGINE=InnoDB default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

create table board_pieces (
	board_piece_id 	char(50),
	game_id 	char(50),
	`position` 	char(5),
	piece 		char(20),
	primary key(board_piece_id)
)ENGINE=InnoDB default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;
