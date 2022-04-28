# [ Spring 체스 ]

### [ 1 단계 ]

- Spring Framework 적용
  - `SpringController` 생성
  - `.movePiece()` 로직을 변경하여 `SpringChessService` 생성
  - `JdbcTemplate` 를 사용하는 `spring.ChessGameDao`, `spring.BoardDao` 생성
- `spring.ChessGameDao`, `spring.BoardDao` 에 대한 테스트 클래스 생성

### [2단계]
- [x] name, password 이용하여 game 생성 
- [x] password 이용하여 game 삭제
- [x] game 이 끝나지 않은 경우 삭제하지 못하도록 하는 기능
- [x] checkmate 가 되지 않아도 끝낼 수 있는 버튼 생성
- [ ] password 인코딩 디코딩 기능
