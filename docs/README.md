## 접속 방법

- db 정보
    - docker
        - prod db: mysql / port: 13306
            - init.sql
        - test db: h2(tesdb, MySQL mode) / mem
            - schema.sql
    - 접속 방법

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
    - [x] 방 만들기 - 방을 만들고 보드를 생성
    - [x] 방 입장하기 - 생성된 방 검증 정보 전달 
    - [x] 체스보드 정보 - 보드와 현재 팀을 가져온다
    - [x] 움직인다 - 기물을 update하고 방의 팀을 변경한다
    - [x] 게임 종료 - 방을 game over로 변경한다

- controller
    - [x] 방 생성 - post api/chess/rooms
    - [x] 방 전체조회 - get api/chess/rooms
    - [x] 방 입장 - get api/chess/rooms/{id}/enter
    - [x] 체스 현재 정보 조회 - get api/chess/rooms/{id}
    - [x] 체스 기물 이동 - post api/chess/rooms/{id}/move
    - [x] 체스 종료 및 결과계산 - update api/chess/rooms/{id}
    - [x] 체스 점수 출력 - get api/chess/rooms/{id}/status
