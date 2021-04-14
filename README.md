# DDL

```sql
CREATE TABLE game (
	game_id INTEGER NOT NULL AUTO_INCREMENT,
	is_end BOOLEAN NOT NULL DEFAULT false,
	PRIMARY KEY (game_id )
);


CREATE TABLE room (
	room_id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(21) NOT NULL,
	pw VARCHAR(21) NOT NULL,
	game_id INTEGER NOT NULL,
	PRIMARY KEY (room_id),
	FOREIGN KEY (game_id) REFERENCES game (game_id)
	);
	
	
	CREATE TABLE team (
	team_id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(21) NOT NULL,
	is_end BOOLEAN NOT NULL DEFAULT false,
	game_id INTEGER NOT NULL,
	PRIMARY KEY (team_id),
	FOREIGN KEY (game_id) REFERENCES game (game_id)
	);
	
	CREATE TABLE piece (
	piece_id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(21) NOT NULL,
	team_id INTEGER NOT NULL,
	PRIMARY KEY (piece_id),
	FOREIGN KEY (team_id) REFERENCES team (team_id)
	);
```


