# Spring 체스

## 플레이 방법

- localhost:8080 으로 접속하여 게임 방을 만들고 체스 게임을 플레이한다.

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
- 체스 게임 목록 로딩
  - GET "/chessgames"
- 체스 게임 로딩
  - POST "/chessgames/{id}"
- 체스 게임 삭제
  - DELETE "/chessgames/{id}"
- 체스 게임 종료
  - PATCH "/chessgames/{id}/end"
- 피스 이동
  - PATCH "/chessgames/{id}/move"
- 피스 프로모션
  - PATCH "/chessgames/{id}/promotion"
- 체스 게임 스코어 계산
  - GET "/chessgames/{id}/score"
- 체스 게임 종료 여부
  - GET "/chessgames/{id}/status"
- 체스 게임 우승자 판별
  - GET "/chessgames/{id}/winner"

### 체스 게임 방

- 체스 게임 방을 제목과 비밀번호를 입력하여 만들 수 있다.
- 체스방 목록을 조회할 수 있다.
- 체스방 목록에서 체스방 제목을 클릭하고 비밀번호를 입력하면 체스 게임을 이어서 진행할 수 있다.
- 체스방 목록에서 체스방 삭제 버튼을 클릭하고 체스방 생성시 설정한 비밀번호를 입력하면 체스 게임을 삭제할 수 있다.
- 진행중인 체스방은 삭제할 수 없다.


