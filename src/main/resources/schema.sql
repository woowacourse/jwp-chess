drop table rooms if exists;
drop table commands if exists;

create table rooms (
      room_id bigint not null auto_increment primary key,
      name varchar(20) not null,
      password varchar(20) not null
);

create table commands (
    command_id bigint not null auto_increment primary key,
    command varchar(20) not null,
    room_id bigint,
    foreign key(room_id) references rooms(room_id) on update cascade
);
