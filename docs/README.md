## 체스 기능 요구 사항

### 1단계 체스판 초기화
- 체스 게임 시작 문장 출력
- "start"를 입력 받는다.
- "end"를 입력받는다.
  - 이외의 입력이 들어오면 예외

- 체스판을 초기화한다.

## 2단계 체스 말 이동
- "move {source 위치} {target 위치}" 형식으로 입력받는다.
  - 가로 위치는 a~h, 세로 위치는 1~8 사이로 입력 받는다.
- 기물을 각 이동 규칙에 따라 움직이게 한다.

## 3단계 승패 및 점수
- "status"를 입력 받아 각 진영의 점수를 출력한다.
- King이 잡혔을 때 게임을 종료해야 한다.


## 도메인 설계
###체스판
- Board
- BoardInitializer
### 명령어
  - Command
  - End
  - Move
  - Start
  - Status
### 방향
  - 전략
  - DirectionStrategy
    - \+ 모든 말의 DirectionStrategy
  - Direction
  - BasicDirection
  - DiagonalDirection
  - KnightDirection
### 기물
- Color(Black, White)
- King, Queen, Knight, Bishop, Rook, Pawn
### 위치
- Position
- UnitPosition
### 상태
- State
- Ready
- Running
- RunningBlackTurn
- RunningWhiteTurn
- Finished

## 웹 체스 요구사항

## Repository
- BoardDao
    - [x] 기물들의 위치인 location, 기물들의 종류인 name, 기물들의 색상인 color를 필드로 하는 객체로 Wrapping 하는 기능
    - [x] 게임 초기 기물을 DB에 저장하는 기능
    - [x] 전체 기물들을 DB로부터 가져오는 기능
    - [x] 하나의 기물을 DB에 저장하는 기능
    - [x] 하나의 기물을 DB로부터 삭제하는 기능
- StateDao
    - [x] 방의 ID를 통해 저장된 게임의 차례가 누구인지 DB로부터 가져오는 기능
    - [x] 플레이어가 기물을 움직인 후 바뀐 차례를 DB에 저장하는 기능
    - [x] 방의 ID를 통해 저장된 방인지 확인하는 기능
    - [x] 게임이 끝난 경우 방의 ID를 DB로부터 삭제하는 기능
    - [x] 새로운 방의 ID를 DB에 추가하는 기능
    - [x] 첫 차례를 DB에 저장하는 기능
    - [x] 전체 방의 이름을 DB로부터 가져오는 기능

## 컨트롤러
- webChessController
    - [X] 각 상태별 대응되는 hbs로 렌더링하는 기능
    - [X] 입력이 유효하지 않은 경우 대응되는 hbs로 렌더링하는 기능
    - [X] 각 상태별 지원하지 않는 입력인 경우 대응되는 hbs로 렌더링하는 기능

## 서비스
- webChessGame
    - [X] 방의 ID로 현재 상태를 알려주는 기능
    - [X] 저장된 방의 경우 저장된 기물들의 상태를 업데이트 하는 기능
    - [X] 방의 ID가 저장되어 있는지 확인하는 기능
    - [X] 현재 상태의 차례(흑 OR 백)을 알려주는 기능
    - [X] 새로운 게임일 때 기물과 차례를 초기화하는 기능
    - [X] 전체 방의 이름을 알려주는 기능
    - [X] 기물 이동을 완료 시 차례를 바꿔주는 기능
    - [X] 기물을 움직이는 기능