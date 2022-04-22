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
