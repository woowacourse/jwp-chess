create table piece2(
  id bigint auto_increment,
  name varchar(244) not null,
  row varchar(244) not null,
  col varchar(244) not null,
  game_id bigint,
  primary key(id)
);

create table chessgame2(
  id bigint auto_increment,
  white varchar(244) not null,
  black varchar(244) not null,
  turn_is_black boolean not null,
  primary key(id)
);

