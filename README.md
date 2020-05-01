# 웹 체스 미션

## 1단계 기능 요구사항

 - [x] 체스판을 초기화 할 수 있다.
 - [x] 말 이동을 할 수 있다.
 - [x] 승패 및 점수를 판단할 수 있다.
 - [x] 웹으로 체스 게임이 가능해야 한다.
 - [x] 웹 서버를 재시작하더라도 이전에 하던 체스 게임을 다시 시작할 수 있어야 한다.(DB 적용)
 
 ## 2단계 기능 요구사항
 - [x] 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
 - [x] @Controller나 @RestController를 활용하여 요청을 받아야 한다.
 - [x] 웹 UI를 적용할 때 도메인 객체의 변경을 최소화해야한다.
 - [x] 스프링 빈임을 나타내는 애너테이션(@Component, @Service 등)을 활용한다.
 - [x] 컴포넌트 스캔을 통해 빈 등록하여 사용한다.
 
 ## 3단계 기능 요구사항
 - [x] 엔티티 클래스를 만들어 DB 테이블과 맵핑한다.
 - [x] Spring Data JDBC에서 제공하는 Repository를 활용하여 DB에 접근한다.
 
 ## 4단계 기능 요구사항
 
 **체스방 만들기**
 - [x] localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.
 - [x] 체스방 만들기 버튼을 누르면 새로운 체스판이 초기화 된다.
 - [x] 체스방에는 고유식별값이 부여된다. (이 고유 식별값은 체스방 주소에서 사용 됨)
 
 **체스방 목록 조회하기**
 - [x] localhost:8080 요청 시 체스방 목록을 조회할 수 있다
 - [x] 체스방 목록에는 체스방 제목이 표시된다.
 
 **체스방 참여하기**
 - [x] localhost:8080 요청 시 체스방 목록에서 체스방을 클릭하면 체스 게임을 이어서 진행할 수 있다.