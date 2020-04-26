# 체스 - 스프링 실습
## STEP 1
### 기능목록

- 체스판을 초기화 할 수 있다.

- 말 이동을 할 수 있다.

  - 각 말의 이동규칙에 따라 이동해야한다.
  - 체스판 내의 이동 범위로 이동해야한다.

- 승패 및 점수를 판단할 수 있다.

  - 승패는 왕의 생존여부로 확인한다.
  - 점수는 살아있는 말의 점수들을 계산한다.

- 웹으로 체스 게임이 가능해야 한다.(스파크 적용)

  - Controller - Service - DAO, 3-tier으로 분리한다.
  - handlebars를 이용하여 템플릿에서 모델을 사용한다.

- 웹 서버를 재시작하더라도 이전에 하던 체스 게임을 다시 시작할 수 있어야 한다.(DB 적용)

  - Mysql을 사용한다.

  - ```mysql
    create table chessBoard(
        position varchar(10) NOT NULL,
        pieceName varchar(10) NOT NULL
    );
    ```

  - ``` mysql
    create table turn(
        teamName varchar(10) NOT NULL
    );
    ```

## STEP 2
### 요구사항
- 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- 웹 UI를 적용할 때 도메인 객체의 변경을 최소화해야한다.
- 스프링 빈임을 나타내는 애너테이션(@Component, @Service 등)을 활용한다.
- 컴포넌트 스캔을 통해 빈 등록하여 사용한다.

### 구현할 URL 및 기능
1. "/"
    - 시작 페이지를 로드한다.
2. "/chess-game"
    - 데이터베이스에서 게임을 로드한다.
3. "/new-chess-game"
    - 새로운 체스게임을 시작한다.
4. "/move"  Request parameter : source, target
    - 체스게임에서 말을 이동한다
5. "/winner"
    - 승패 결과 및 점수 페이지를 로드한다.



## STEP 3

### 요구사항

- 엔티티 클래스를 만들어 DB 테이블과 맵핑한다.
- Spring Data JDBC에서 제공하는 Repository를 활용하여 DB에 접근한다.