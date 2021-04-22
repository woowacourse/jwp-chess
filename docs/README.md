# 스프링 입문 - 체스

## step1 요구사항
- Spring Framework를 활용하여 애플리케이션을 구동한다.
- Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
- Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.

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
  `room_id` int(11) NOT NULL,
  `current_turn` char(5) DEFAULT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


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