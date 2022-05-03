# java-chess

체스 미션 저장소

## 우아한테크코스 코드리뷰

- [온라인 코드 리뷰 과정](https://github.com/woowacourse/woowacourse-docs/blob/master/maincourse/README.md)

## 1단계

spring MVC 를 사용하여 변경한다.

- [x] DAO 를 빈으로 등록되게 한다.
    - [x] BoardDAO
    - [x] GameStatusDAO
    - [x] TurnDAO
- [x] Service 를 빈으로 등록되게 한다.
    - [x] ChessGameService
- [x] Controller 를 빈으로 등록되게 한다.

### DAO

- [x] JDBC Template을 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- [x] JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.

### Service

- [x] @Service 어노테이션을 사용한다.

### Controller

- [x] @Controller 나 @RestController 를 활용하여 요청한다.
- [x] 요청과 컨트롤러를 맵핑하기 위해 어노테이션을 사용한다.
- [x] 반환 타입은 객체로 보낸다.

## 2단계

- [x] 체스방 만들기
    - [x] localhost:8080 요청시, 노출되는 페이지에 체스방을 만드는 버튼 추가
- [x] 체스방 목록 조회하기
    - [x] localhost:8080 요청시, 체스방 목록 조회
    - [x] 체스방 목록에는 체스방 제목과, 체스방을 삭제할 수 있는 버튼이 있음
- [x] 체스방 참여하기
    - [x] 체스방 제목을 클릭하면 게임에 참여
- [x] 체스방 삭제하기
    - [x] 체스방 삭제 버튼을 누르고, 생성시 비밀번호와 일치하면 삭제
    - [x] **예외** 진행중이면 삭제 불가

