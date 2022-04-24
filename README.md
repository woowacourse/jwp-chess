<p align="center">
    <img src="./woowacourse.png" alt="우아한테크코스" width="250px">
</p>

# Level 2, 웹 체스

---

![Generic badge](https://img.shields.io/badge/Level2-chess-green.svg)
![Generic badge](https://img.shields.io/badge/test-129_passed-blue.svg)
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
