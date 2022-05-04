# 환경 구축
## 1. Docker MySql 설치
`/docker` 디렉터리에 이동하여 터미널에 `docker-compose -p chess2 up -d` 명령어를 입력하여 도커에 프로젝트 db설치 및 실행한다.

## 2. MySql 터미널 접속하기
`docker exec -it chess_db_1 bash` 를 통해 docker안에 있는 db 터미널 접속한다.

## 3. DB테이블 생성하기
아래의 DDL을 사용하여 DB에 테이블을 생성한다.
### 테이블을 만들 때 사용한 DDL
```sql
create table room (
    id bigint not null auto_increment primary key,
    name varchar(10) not null,
    password varchar(20) not null,
    turn varchar(5)  not null
);

create table piece(
    position varchar(2) not null,
    name varchar(6) not null,
    team varchar(5) not null,
    room_id bigint not null,
    foreign key (room_id) references room (id) on delete cascade
);
```
