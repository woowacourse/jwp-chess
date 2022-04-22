## 기능 구현 목록

- repository (board)
    - [x] 기물을 가져온다.
    - [x] 기물의 위치를 업데이트한다.
    - [x] 기물을 추가한다.
    - [x] 보드 전체를 한 번에 추가한다.
    - [x] 기물의 위치를 한 번에 업데이트한다.

- repository (room)
    - [x] 룸을 생성한다.
    - [x] 룸 정보를 가져온다.
    - [x] 룸의 턴을 업데이트한다.
    - [x] 룸을 종료한다

- service
    - [x] 전체 방 조회
    - [x] 방만들기 - 방을 만들고 보드를 생성
    - [x] 방입장하기 - 보드와 현재 팀을 가져온다
    - [x] 움직인다 - 기물을 update하고 방의 팀을 변경한다
    - [x] 게임 종료 - 방을 game over로 변경한다

- controller
    - [x] 방 생성 - post api/chess/rooms
    - [x] 방 전체조회 - get api/chess/rooms
    - [x] 방 하나 입장 - get api/chess/rooms/{id}
    - [x] 방 종료 및 결과계산 - delete api/chess/rooms/{id}
    - [ ] 말 움직이기 - post api/chess/rooms/{id}/move
    - [ ] 현재 상태 출력하기 - get api/chess/rooms/{id}/status
