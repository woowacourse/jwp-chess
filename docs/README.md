# 스프링 입문 - 체스

## step2 요구사항
- 체스 게임을 진행할 수 있는 방을 만들어서 동시에 여러 게임이 가능하도록 하기

## step2 기능 요구사항
### 체스방 만들기
- localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.
- 체스방 만들기 버튼을 누르면 새로운 체스판이 초기화 된다.
- 체스방에는 고유식별값이 부여된다. (이 고유 식별값은 체스방 주소에서 사용 됨)

### 체스방 목록 조회하기
- localhost:8080 요청 시 체스방 목록을 조회할 수 있다
- 체스방 목록에는 체스방 제목이 표시된다.

### 체스방 참여하기
- localhost:8080 요청 시 체스방 목록에서 체스방을 클릭하면 체스 게임을 이어서 진행할 수 있다.

## step1 프로그래밍 요구사항
- 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.

## DB DDL
```sql
-- -----------------------------------------------------
-- Table `mydb`.`room`
-- -----------------------------------------------------
CREATE TABLE `room` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `current_turn` char(5) DEFAULT NULL,
  `room_name` varchar(20) NOT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8


-- -----------------------------------------------------
-- Table `mydb`.`piece`
-- -----------------------------------------------------
CREATE TABLE `piece` (
  `piece_name` char(1) DEFAULT NULL,
  `piece_position` char(2) DEFAULT NULL,
  `room_id` int(11) NOT NULL,
  KEY `room_id` (`room_id`),
  CONSTRAINT `room_id` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```