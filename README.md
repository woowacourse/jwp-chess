<p align="center">
    <img src="./woowacourse.png" alt="우아한테크코스" width="250px">
</p>

# Level 2, 웹 체스

---

![Generic badge](https://img.shields.io/badge/Level2-chess-green.svg)
![Generic badge](https://img.shields.io/badge/test-142_passed-blue.svg)
![Generic badge](https://img.shields.io/badge/version-1.0.0-brightgreen.svg)

> 우아한테크코스 웹 백엔드 4기, 웹 체스 저장소입니다.

[![Video Label](http://img.youtube.com/vi/xGw8WqAIQDY/0.jpg)](https://youtu.be/xGw8WqAIQDY)

# How to Start

### 1. Run Docker

```
docker run -d -p 80:80 docker/getting-started 
```

### 2. Run Docker Container

```
cd docker
docker-compose -p chess up -d
```

### 3. Run SpringBootApplication

```
cd ..
./gradlew bootRun
```

### 4. [Open in Browser - http://localhost:8080/](http://localhost:8080/)

<br><br>

## 기능 요구사항

### 체스방 만들기
- 

- [x] localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.
- [x] 체스방 만들기 버튼을 누르고 체스방 제목과 비밀번호를 입력하면 새로운 체스판이 만들어진다.
- [x] 체스방에는 고유식별값이 부여된다. (이 고유 식별값은 체스방 주소에서 사용 됨)

### 체스방 목록 조회하기

- [x] localhost:8080 요청 시 체스방 목록을 조회할 수 있다
- [x] 체스방 목록에는 체스방 제목과 체스방을 삭제할 수 있는 버튼이 표시된다.

### 체스방 참여하기

- [x] 체스방 목록에서 체스방 제목을 클릭하면 체스 게임을 이어서 진행할 수 있다.

### 체스방 삭제하기

- [x] 체스방 목록에서 체스방 삭제 버튼을 클릭하고 체스방 생성시 설정한 비밀번호를 입력하면 체스 게임을 삭제할 수 있다.
- [x] 진행중인 체스방은 삭제할 수 없다.

## 프로그래밍 요구사항

- [x] 예외가 발생했을 때 사용자가 이해할 수 있는 명시적인 메시지를 응답한다.

## 요구사항

- [x] Spring Framework를 활용하여 애플리케이션을 구동한다.
- [x] Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
- [x] Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.

<br>

## 프로그래밍 요구사항

- [x] 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- [x] @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- [x] Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- [x] JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.

### [레벨 1 - 스파크 체스](https://github.com/hj-Rich/java-chess/tree/step4,5)

<br><br>
