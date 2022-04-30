create table if not exists rooms (
      room_id bigint not null auto_increment primary key,
      name varchar(20) not null,
      password varchar(20) not null
);

create table if not exists commands (
    command_id bigint not null auto_increment primary key,
    command varchar(20) not null,
    room_id bigint,
    foreign key(room_id) references rooms(room_id) on update cascade
);
