# java-chess
체스 게임 구현을 위한 저장소

## 기능 요구 사항
- 1단계는 체스 게임을 할 수 있는 체스판을 초기화한다.
- 체스판에서 말의 위치 값은 가로 위치는 왼쪽부터 a ~ h이고, 세로는 아래부터 위로 1 ~ 8로 구현한다.
- 체스판에서 각 진영은 검은색(대문자)과 흰색(소문자) 편으로 구분한다.

## 기능 목록
### domain
- [x] ChessPiece enum 구현
    - [x] 퀸, 킹, 비숍, 폰, 나이트, 룩 구현
        - [x] 초기 위치
        - [x] 이름
- [x] Player 구현
    - [x] chessPiece의 이름 결정
- [x] GamePiece 구현
    - [x] Player, ChessPiece, name
    - [x] list 생성
- [x] Position 구현
    - [x] row, column
        - [x] opposite 구현
        - [x] next, previous 구현
    - [x] list 생성
    - [x] opposite 구현
    - [x] flip 구현
    - [x] destinationOf 구현
- [x] Board 구현
    - [x] 체스판 초기화 
    - [x] move 구현
    - [x] backWard 구현
    - [x] turn 구현
- [x] Command 구현
    - [x] Start 구현
    - [x] End 구현
    - [x] Move 구현
    - [x] Status 구현
- [x] Direction 구현
    - [x] 다음 File, Rank 찾는 기능 구현
- [x] moveStrategy 구현
    - [x] findMovePath 구현
    - [x] 폰이 기물을 잡는 경우 구현
- [x] 점수 구현
    - [x] 각 진영의 점수를 계산
    
### view
- [x] Command 입력
- [x] Board 출력
- [x] Score 출력
- [ ] 방 목록 출력

### Framework
- [x] Spark
- [x] Spring
- [x] Spring Data JDBC