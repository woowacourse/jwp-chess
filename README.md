# 웹 체스게임
Spark 기반의 웹 체스게임을 Spring으로 대체하는 프로젝트입니다.

## 프로젝트 설명
동시에 여러 게임을 진행할 수 있도록 체스방을 만들고 각 방에서 체스 게임을 할 수 있습니다.

## 요구사항
- 체스 게임을 진행할 수 있는 방을 만들어서 동시에 여러 게임이 가능하도록 하기
## 기능 요구사항
### 체스방 만들기
- localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.
- 체스방 만들기 버튼을 누르고 체스방 제목과 비밀번호를 입력하면 새로운 체스판이 만들어진다.
- 체스방에는 고유식별값이 부여된다. (이 고유 식별값은 체스방 주소에서 사용 됨)
### 체스방 목록 조회하기
- localhost:8080 요청 시 체스방 목록을 조회할 수 있다
- 체스방 목록에는 체스방 제목과 체스방을 삭제할 수 있는 버튼이 표시된다.
### 체스방 참여하기
- 체스방 목록에서 체스방 제목을 클릭하면 체스 게임을 이어서 진행할 수 있다.
### 체스방 삭제하기
- 체스방 목록에서 체스방 삭제 버튼을 클릭하고 체스방 생성시 설정한 비밀번호를 입력하면 체스 게임을 삭제할 수 있다.
- 진행중인 체스방은 삭제할 수 없다.
## 프로그래밍 요구사항
- 예외가 발생했을 때 사용자가 이해할 수 있는 명시적인 메시지를 응답한다.

## DB 스키마
```sql
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

```

## HTTP API
- [ ] `localhost:8080`
  - GET: 체스방의 목록을 보여주고 체스방을 추가, 참가, 삭제할 수 있는 버튼이 있다.
- [x] `/chess`
  - POST: 이름과 비밀번호를 가진 체스방을 만든다.
- [ ] `/chess/{id}`
  - DELETE: header에 담긴 비밀번호가 일치하면 id 주소값을 가진 체스방과 관련된 체스판 명령어를 삭제한다.
- [x] `/chess/{id}/board`
  - GET: id 주소값을 가진 체스방이 가진 체스판을 출력한다.
  - POST: id 주소값을 가진 체스방이 가진 체스판의 기물을 움직인다.
- [x] `/chess/{id}/result`
  - GET: id 주소값을 가진 체스방의 결과를 알려준다.
- [예외] 사용자가 잘못된 입력을 했을 땐 예외 메시지를 출력한다.
- [예외] 사용자의 잘못된 입력 외의 예외가 발생했을 땐 예외 페이지를 출력한다.
