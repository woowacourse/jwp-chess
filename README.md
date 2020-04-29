## Todo list
### step 2
- [x] score
- [x] finish -> db 지우기
- [x] turn 표시
- [x] Room Model 생성 
    - [x] RoomDAO
    - [x] RoomController
    - [x] RoomService
- [x] Board Table 수정
    - [x] Room id 추가
    - [x] BoardDAO 에서 Room id 로 조회 기능
- [ ] Board 도메인 수정
    - [ ] finish 체크 할 때 보드 반환 리팩토링
    - [x] 폰 2칸 움직이는 거 수정
    - [x] move -> 검증 메서드로 변경
    - [x] initial Board 위치 변경
### step 3
- [x] Room Entity, Room Repository 생성
- [x] Board Entity, Board Repository 생성
- [ ] entity json으로 넘기는 거

# jwp-chess
### 1단계
- [x] 웹으로 체스 게임이 가능해야 한다.(스파크 적용)
- [x] 웹 서버를 재시작하더라도 이전에 하던 체스 게임을 다시 시작할 수 있어야 한다.(DB 적용)

### 2단계
- [x] 스파크 애플리케이션을 유지한 상태에서 스프링 애플리케이션을 추가
- [x] 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- [x] 도메인 객체가 아니며 재사용 할 객체를 스프링 빈을 활용하여 관리하기
    - [x] @Controller나 @RestController를 활용하여 요청을 받아야 한다.
    - [x] 스프링 빈임을 나타내는 애너테이션(@Component, @Service 등)을 활용한다.
    - [x] 컴포넌트 스캔을 통해 빈 등록하여 사용한다.

### 3단계
- [x] Spring Data JDBC를 활용하여 기존에 사용하던 DB에 접근하기
- [x] 엔티티 클래스를 만들어 DB 테이블과 맵핑한다.
- [x] Spring Data JDBC에서 제공하는 Repository를 활용하여 DB에 접근한다.

### 4단계
- [x] 체스 게임을 진행할 수 있는 방을 만들어서 동시에 여러 게임이 가능하도록 하기
- [x] 체스방 만들기
    - [x] localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.    
    - [x] 체스방 만들기 버튼을 누르면 새로운 체스판이 초기화 된다.
    - [x] 체스방에는 고유식별값이 랜덤으로 부여된다.
- [x] 체스방 목록 조회하기
    - [x] localhost:8080 요청 시 체스방 목록을 조회할 수 있다
    - [x] 체스방 목록에는 고유식별값이 표시된다.
- [x] 체스방 참여하기
    - [x] localhost:8080 요청 시 체스방 목록에서 체스방을 클릭하면 체스 게임을 이어서 진행할 수 있다.

# java-chess

## 1. 미션 개요
해당 미션은 콘솔 UI에서 체스 게임을 할 수 있는 기능을 구현한다. 체스판에서 각 진영은 대문자와 소문자로 구분한다.

## 2. 기능목록
1. 체스 게임을 할 수 있는 체스판을 초기화한다.
2. start 입력 시 체스 게임을 시작하고, end 입력 시 종료한다.
3. 체스 말은 각각 이동 규칙을 따라 이동한다.
4. King이 잡혔을 때 게임을 종료한다.
5. 현재 남아있는 말에 대한 점수를 구한다.


## 3. 구현상세
1. 체스판에서 말의 위치 값은 가로 위치는 왼쪽부터 a ~ h이고, 세로는 아래부터 위로 1 ~ 8로 구현한다.
2. 각 말은 알파벳 한 글자로 표현한다. P는 pawn, R은 rook, N은 knight, B는 bishop, Q는 queen, K는 King을 의미한다.
3. move source_위치 target_위치를 실행해 이동한다.
4. "status" 명령을 입력하면 각 진영의 점수를 출력하고 어느 진영이 이겼는지 결과를 볼 수 있어야 한다.
5. queen은 9점, rook은 5점, bishop은 3점, knight는 2.5점이고 pawn의 기본 점수는 1점이다. 
하지만 같은 세로줄에 같은 색의 폰이 있는 경우 1점이 아닌 0.5점을 준다.
6. king은 잡히는 경우 경기가 끝나기 때문에 점수가 없다.
7. 한 번에 한 쪽의 점수만을 계산해야 한다.

## 4. 구현 목록
- [x] Piece
    - [x] 체스 말 표식 representation 으로 화이트/블랙 구분
    - [x] 각 체스 말(Pawn, Rook, Knight, Bishop, Queen, King)이 현재 포지션을 가지고 있음
    - [x] Blank Piece 를 추가하여 .으로 표시되는 보드 상의 공백 부분을 나타냄
    - [x] 이동 가능 한 포지션들 추출
    - [x] to position 을 받아서 이동 가능한지 확인
    - [x] 이동 불가능 하면 예외 발생.
- [x] BoardFactory
    - [x] 초기화된 위치를 갖고 있는 piece 들로 이루어진 List 생성
    - [x] 정적 팩터리 메서드로 초기화된 보드(Board) 생성
- [x] Board
    - [x] a5 같은 문자열 포지션으로 해당 포지션에 있는 piece 를 찾음 (from && to)
    - [x] piece 의 포지션을 바꾼 후 불변 객체 (보드) 반환
    - [x] piece 가 이동 가능하면 board 리스트 에서, 
        - [x] from piece 는 to position 으로 변경
        - [x] from position 을 갖는 Blank piece 추가
    - [x] king 이 잡혔을 경우, 게임 종료
    - [x] 한 열에 pawn 이 2개 있을 경우 0.5점씩 계산
    - [x] 화이트/블랙 번갈아 가면서 게임
- [x] TypeScore
    - [x] 각 피스 타입 별 점수계산을 위한 enum
- [x] Direction
    - [x] 이동 가능한 방향별 enum 요소
    - [x] 현재 위치에서 해당 방향으로 이동한 후의 포지션 반환
- [x] Run
    - [x] 게임 실행 여부를 조정
        - [x] start : 게임시작
        - [x] end : 게임종료
    - [x] 체스 말 이동
        - [x] move : 이동
    - [x] 점수 현황
        - [x] status : 현황

## 5. 체스 말의 이동 규칙
* 폰(PAWN)
> 전진만 가능하며 첫회 한정으로 2칸 이동이 가능하다. 
> 그 다음부터는 1칸씩. 단, 첫 이동을 할 때 바로 앞에 다른 말이 있으면 뛰어 넘을 수 없다. 
> 공격은 앞 대각선으로만 공격할 수 있다.

* 룩(ROOK)
> 전후좌우 직선으로 거리 제한 없이 움직인다.

* 나이트(KNIGHT)
> 한 방향으로 한 칸, 그리고 그 방향의 양 대각선 방향 중 한 방향으로 이동한다. 
> 이동 경로에 다른 말이 있어도 뛰어넘을 수 있다.

* 비숍(BISHOP)
> 대각선으로 거리 제한 없이 움직인다.

* 퀸(QUEEN)
> 좌우, 상하, 대각선 모든 방향으로 거리 제한 없이 이동 가능하다.

* 킹(KING)
> 상대에게 체크메이트를 당하지 않도록 보호해야 하는 말. 
> 어떤 방향으로든 1칸만 이동 가능하다.
