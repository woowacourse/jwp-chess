# Spring 체스


## 기능 구현 사항

### PieceDao

- 현재 게임 방의 Piece 정보를 가지고 올 수 있다.
- Piece 정보를 저장할 수 있다.
- Piece 정보를 업데이트 할 수 있다.
- Piece 정보를 삭제할 수 있다.

### ChessGameDao

- 새로운 게임을 생성할 수 있다.
- 현재 게임 상태를 반환 할 수 있다.

### api

- 새로운 체스 게임을 생성 
  - POST "/chessgames"
- 체스 게임 로딩
  - GET "/chessgames/{id}"
- 피스 이동
  - POST "/chessgames/{id}/move"
- 피스 프로모션
  - POST "/chessgames/{id}/promotion"
- 체스 게임 스코어 계산
  - GET "/chessgames/{id}/score"
- 체스 게임 종료 여부
  - GET "/chessgames/{id}/status"
- 체스 게임 우승자 판별
  - GET "/chessgames/{id}/winner"

## 2단계 - 동시에 여러 게임 하기

- 체스방을 만들어 여러 게임이 진행될 수 있다.

- 새로운 체스 방 만들기
  - POST "/chessgames"
  - 체스방 제목과 비밀번호를 입력하면 새로운 체스판이 만들어진다.
  - 체스방에는 고유 식별값이 부여된다.
  - [ERROR] 체스방 제목이 동일하면 예외가 발생한다.
  
- 체스방 목록 조회하기
  - GET "/chessgames"
  - 체스방 제목과 체스방을 삭제할 수 있는 버튼이 표시된다.
  
- 체스방 참여하기
  - GET "/chessgames/{id}"
  - 체스방 제목을 클릭하면 체스게임을 이어서 진행할 수 있다.

- 체스방 삭제하기
  - DELETE "/chessgames/{id}"
  - 설정한 비밀번호를 입력하면 삭제할 수 있다.
  - 종료되지 않은 체스방은 삭제할 수 없다.

