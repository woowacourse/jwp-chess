###  기능 요구사항

- [x] controller를 @Controller를 활용하여 요청을 받도록 수정한다.
- [x] Jdbc Template을 이용하여 Connection을 직접 만들어주는 로직을 대체한다.
  - [x] boardDao에 적용한다.
  - [x] pieceDao에 적용한다.
- [x] Jdbc Template을 빈 주입을 받아서 사용한다.
  - [x] boardDao에 적용한다.
  - [x] pieceDao에 적용한다.
- [ ] 체스방 만들기
  - [x] localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.
  - [ ] 체스방 만들기 버튼을 누르고 체스방 제목과 비밀번호를 입력하면 새로운 체스판이 만들어진다.
  - [ ] 체스방에는 고유식별값이 부여된다. (이 고유 식별값은 체스방 주소에서 사용 됨)
- [ ] 체스방 목록 조회하기
  - [ ] localhost:8080 요청 시 체스방 목록을 조회할 수 있다
  - [ ] 체스방 목록에는 체스방 제목과 체스방을 삭제할 수 있는 버튼이 표시된다.
- [ ] 체스방 참여하기
  - [ ] 체스방 목록에서 체스방 제목을 클릭하면 체스 게임을 이어서 진행할 수 있다.
- [ ] 체스방 삭제하기
  - [ ] 체스방 목록에서 체스방 삭제 버튼을 클릭하고 체스방 생성시 설정한 비밀번호를 입력하면 체스 게임을 삭제할 수 있다.
  - [ ] 진행중인 체스방은 삭제할 수 없다.

---

### API 정리

- GET:`/` : 체스 방을 만들 수 있는 버튼이 있는 페이지 출력
- GET:`/chess` : 체스 게임을 할 수 있는 페이지 출력.
- GET:`/api/chess/load` : 체스 기물 상태를 가져온다.
- GET:`/api/chess/restart` : 체스 게임을 재시작한다.
- GET:`/api/chess/status` : 체스 게임의 블랙, 화이트 팀의 점수를 반환한다.
- POST:`/api/chess/move` : 체스 게임에서 시작 위치, 도착 위치를 JSON으로 전송하면 이동한다. 정상적으로 이동 시 수정된 Board를 반환한다.

---

### 피드백, 수정사항 정리

- [x] 말을 이동한 뒤 말이 이동할 수 없는 경우 보드가 먹통이 되는 에러 수정해야 함
- [x] TODO, FIXME는 당장 필요가 없다면 PR에 있을 이유가 없음
- [x] `BoardDaoImpl`에서 `save`는 무조건 white 값을 넣고 있음
- [x] 테스트에서 BeforeEach로 매번 테이블을 만드는 쿼리가 실행되는데 이런 raw한 sql 쿼리문이 노출되지 않도록 구현할 수 있을까?
- [x] 불필요한 spark 의존성을 제거하자

- 기존 `JdbcTemplate`없이 영속성 레이어를 구현할 때와 `JdbcTemplate`를 활용할 때의 차이점은 무엇일까?
- 스프링에서 의존성을 주입하는 방법은 생성자 주입, 필드 주입, 세터 주입이 있다. 왜 생성자 주입으로 구현했는가?
