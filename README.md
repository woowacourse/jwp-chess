# 체스 1단계 - 체스구현하기

## **기능 요구사항**
- 체스판을 초기화 할 수 있다.
- 말 이동을 할 수 있다.
- 승패 및 점수를 판단할 수 있다.
- 웹으로 체스 게임이 가능해야 한다.(스파크 적용)
- 웹 서버를 재시작하더라도 이전에 하던 체스 게임을 다시 시작할 수 있어야 한다.(DB 적용)

## **체스 규칙**
- 체스 게임은 상대편 King이 잡히는 경우 게임에서 진다. **King이 잡혔을 때 게임을 종료해야 한다.**
- **체스 게임은 현재 남아 있는 말에 대한 점수를 구할 수 있어야 한다.**

### **점수 계산 규칙**
- 체스 프로그램에서 현재까지 남아 있는 말에 따라 점수를 계산할 수 있어야 한다.
- 각 말의 점수는 queen은 9점, rook은 5점, bishop은 3점, knight는 2.5점이다.
- pawn의 기본 점수는 1점이다. 하지만 같은 세로줄에 같은 색의 폰이 있는 경우 1점이 아닌 0.5점을 준다.
- king은 잡히는 경우 경기가 끝나기 때문에 점수가 없다.
- 한 번에 한 쪽의 점수만을 계산해야 한다.

# 체스 2단계 - Spring MVC 적용하기

## **프로그래밍 요구사항**
- 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- 웹 UI를 적용할 때 도메인 객체의 변경을 최소화해야한다.
- 스프링 빈임을 나타내는 애너테이션(@Component, @Service 등)을 활용한다.
- 컴포넌트 스캔을 통해 빈 등록하여 사용한다.

# 체스 3단계 - Spring Data JDBC 적용하기

## **프로그래밍 요구사항**
- Spring Data JDBC를 활용하여 기존에 사용하던 DB에 접근하기.
- 엔티티 클래스를 만들어 DB 테이블과 맵핑한다.
- Spring Data JDBC에서 제공하는 Repository를 활용하여 DB에 접근한다.

# 체스 4단계 - 동시에 여러 게임 하기

## **프로그래밍 요구사항**
- 체스 게임을 진행할 수 있는 방을 만들어서 동시에 여러 게임이 가능하도록 하기.

### 체스방 만들기

- localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.
- 체스방 이름 입력하기 input을 추가한다.
- 체스방 만들기 버튼을 누르면 새로운 체스판이 초기화 된다.
- 체스방에는 고유식별값이 랜덤으로 부여된다.

### 체스방 목록 조회하기

- localhost:8080 요청 시 체스방 목록을 조회할 수 있다
- 체스방 목록에는 체스방 제목이 표시된다.
- 방법은 '/'로 GET 요청을 보내면 체스방에  대한 JSON이 반환된다.
- 그리고 해당 JSON을 index.js에서 처리하여 보여준다.

### 체스방 참여하기

- localhost:8080 요청 시 체스방 목록에서 체스방을 클릭하면 체스 게임을 이어서 진행할 수 있다.

### 엔티티

- Game : id(auto increment), UUID, name, canContinue 
- History : id, start, end, gameId