# java-chess

체스 미션 저장소

## 🛠 기능 구현 목록

* [x] 체스판은 초기에 32개의 기물을 가진다.
* [x] 체스판의 가로 위치는 왼쪽부터 a ~ h 이다.
* [x] 체스판의 세로 위치는 위로 1 ~ 8 이다.
* [x] 체스판의 각 진영은 대문자와 소문자로 구분한다.

---

* [x] source 위치에 있는 기물을 target 위치로 이동시킨다. 
  * `Pawn` : 폰 기물
    * [x] 처음에 두 칸 갈 수 있다.
    * [x] 처음을 제외하고는 한 칸 씩만 움직일 수 있다. 
    * [x] 상대 기물이 대각선에 있는 경우, 대각선으로 한 칸 움직일 수 있다.
  * `Bishop` : 비숍 기물
    * [x] 대각선으로만 이동가능하다. 
    * [x] 이동거리에 제한이 없다. 
  * `Rook` : 룩 기물
    * [x] 상하좌우로만 이동가능하다. 
    * [x] 이동거리에 제한이 없다. 
  * `Knight` : 나이트 기물
    * [x] 상하좌우로 한 칸 이동한 이후, 이동한 방향의 대각선으로 한 칸 이동한다.
  * `King` : 킹 기물
    * [x] 모든 방향으로 한 칸이동 가능하다. 
  * `Queen` : 퀸 기물
    * [x] 모든 방향으로 거리 제한없이 이동할 수 있다. 

---

* [x] 킹이 잡히면 King이 잡힌 팀이 패배한다. (게임 종료)
* [x] `status` 명령이 들어오면 각 진영의 점수를 출력해준다.
  * [x] queen은 9점, rook은 5점, bishop은 3점, knight는 2.5점으로 계산한다.
  * [x] pawn의 기본 점수는 1점이지만 같은 file에 같은 색의 폰이 잇는 경우 0.5점으로 계산된다.
  * [x] king은 점수 계산에 포함되지 않는다.

---

* [x] 웹을 통해서 드래그 앤 드롭으로 말을 이동시킨다.
* [x] index.html 에서 게임 이름과 함께 '새로운 게임' 버튼을 누르면 게임을 시작한다.
* [x] 점수 버튼을 통해서 현재까지의 점수를 볼 수 있다.
* [x] 종료 버튼을 통해 게임을 종료할 수 있다.
* [x] '새로운 게임' 버튼을 통해서 게임을 새로 시작할 수 있다.

---

* [x] DB를 통해서 웹 서버를 껏다 켜도 게임 정보들은 기억되어야 한다.
* [x] 저장 버튼을 통해서 현재까지 진행된 게임을 '게임 이름'을 토대로 기억한다.
* [x] 새로운 게임 버튼을 통해서 새로운 '게임 이름'을 토대로 게임을 새로 시작할 수 있다.

---

## 🌱 스프링 요구사항

* [x] Spring Framework를 활용하여 애플리케이션을 구동한다.
* [x] Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
* [x] Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.

### 프로그래밍 요구사항

* [x] 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
* [x] @Controller나 @RestController를 활용하여 요청을 받아야 한다.
* [x] Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
* [x] JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.


## 🚀 2단계 요구사항

* [ ] localhost:8080 에서 체스방을 만들 수 있어야 한다.
  * [ ] chess_game에서 gameName을 primary_key로 사용하던 테이블을 고유 식별값인 id를 primary_key로 사용하도록 바꾼다.
  * [ ] controller의 url 매핑구조도 id를 통해 접근하도록 수정한다.
  * [ ] 체스방 제목과 비밀번호를 입력해 만든다.
    * [ ] 체스방 테이블이 비밀번호를 갖도록 수정한다.
  * [ ] 체스방에는 고유식별값이 부여된다. (체스방 주소에 사용된다.)
* [ ] localhost:8080 에서 체스방 목록을 조회할 수 있어야 한다.
  * [ ] 체스방 제목과 체스방 삭제버튼이 표시된다.
* [ ] 체스방 목록에서 체스방 제목을 클릭하면 체스 게임을 이어서 진행할 수 있어야 한다.
* [ ] 체스방 삭제버튼 클릭시 비밀번호를 입력해 체스게임을 삭제한다.
  * [ ] 진행중인 체스방은 삭제할 수 없다.


## 우아한테크코스 코드리뷰

- [온라인 코드 리뷰 과정](https://github.com/woowacourse/woowacourse-docs/blob/master/maincourse/README.md)
