create table room (
  id bigint not null auto_increment primary key,
  name varchar(20) not null,
  team varchar(20) not null,
  game_over boolean not null
);

create table board (
   id bigint not null auto_increment primary key,
   room_id bigint not null,
   position varchar(20) not null,
   piece varchar(20) not null
);
