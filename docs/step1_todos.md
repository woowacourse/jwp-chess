# Step1 

## 1차 피드백
- SpringChessApplication
    - [x] Controller 어노테이션의 역할? RestController
    
- SpringChessController
    - [x] 각 상황에 맞는 메소드 활용 (http 메소드가 각 어떤 상황에서 활용되는지): line22
    - [x] produces의 역할?
    - [x] final 선언: line 16
    
- PiecePositionDaoConvertor
    - [x] String.valueOf, Boolean.valueOf 활용하기
    
- SpringChessGameDao
    - [x] 만약 검색되는 게임이 없다면..-> Client 에서 처리
    
- ChessGameDto
    - [x] 맵 활용 이유?! ScoreDto 활용은 어떤지 고려하기
    
## 개인 todos
- [x] 코드 내용 파악하기
    - [x] domain 구조 파악
    - [x] js 관련 내용 파악
    - [x] db 관련 구조 파악 (dto, dao, service, controller 등)
- [x] 테스트 코드 가져오기
- [x] 코드 Dto 구성 변경해보기
- [x] 코드 테이블 구조 변경해보기
    - [x] 바로 테이블 매핑이 가능한 방안? (RowMapper 사용가능한 방안으로)
- [x] 클래스 이름 통일하기 (카멜케이스) 
- [x] 학습로그 정리하기
    - [x] @Controller, @RestController의 차이
    - [x] Spring 어노테이션의 전체적인 정리
    - [x] produces, consumes 정리
    - [x] http 메소드와 사용 상황 정리
- [ ] 도메인 이외로직에 대해 test 작성하기

## 임시 checkPoints
[ 도메인 관련 ]
- [x] CapturedPieces 삭제

[ 스프링 관련 ]
- [x] Dao interface
- [x] Dao의 로직 서비스로 분리


## DDL
- 1차 수정 : 이벤트 소싱방식
```sql
CREATE TABLE board (
	board_id bigint NOT NULL AUTO_INCREMENT,
	room_id bigint NOT NULL,
	start char(2) NOT NULL,
	destination char(2) NOT NULL,
	date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(board_id)	
);
```
- 2차 수정
```sql
CREATE TABLE room (
	room_id BIGINT NOT NULL,
	turn CHAR(5) NOT NULL,
	is_playing BOOLEAN NOT NULL,
	name VARCHAR(25) NOT NULL,
	password VARCHAR(16),
	create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(room_id)
)

CREATE TABLE board (
	board_id BIGINT NOT NULL AUTO_INCREMENT,
	team VARCHAR(10) NOT NULL,
	position VARCHAR(10) NOT NULL,
	piece VARCHAR(10) NOT NULL, 
	is_first_moved BOOLEAN NOT NULL,
	room_id BIGINT NOT NULL,
	PRIMARY KEY(board_id),
	FOREIGN KEY (room_id) REFERENCES room (room_id)
)
```

