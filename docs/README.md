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
-- Table `mydb`.`piece`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`piece` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`piece_name` VARCHAR(45) NOT NULL,
`piece_position` VARCHAR(45) NOT NULL,
PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`turn`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`turn` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`current_turn` VARCHAR(45) NOT NULL,
PRIMARY KEY (`id`))
ENGINE = InnoDB;
```