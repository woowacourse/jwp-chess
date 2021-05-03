## 체스 게임
## API 명세
http://localhost:8080/swagger-ui.html#/

## 1단계
- [x] DB 연결하기
- [x] 홈화면 보여주기
- [x] DB 연동 user 생성 및 조회 
- [x] User get, post 구현
- [x] game 생성 
- [x] chess 방 입장
- [x] chess 판 초기화
- [x] chess 말 이동


## 1단계 피드백 이후 리팩토링
- [x] 기존 Domain으로 변경
- [x] Controller에서 String 대신 Enum을 받도록 변경 (웹 DTO 포함)
- [x] 도메인과 DAO 리팩토링
    - 기존 Game 도메인은 로직이 없는 데이터의 성격을 띠고 있었다.
    - 게임 진행 로직은 ChessService에 있었고 이 부분을 개선하고 싶었다.
    - DB 테이블 데이터의 접근 관련 객체는 dao 패키지 하위에 dto를 생성하여 접근 
      - [x] GameDto 생성 및 적용
      - [x] PieceDto 생성 및 적용
      - [x] UserDto 생성 및 적용
    - [x] Dao의 select 메서드의 반환값 Optional로 변경
    - [x] GameRepository 생성 
    - [x] UserRepository 생성
- [x] Service와 Controller 리팩토링
    - [x] UserService 에서 UserRepository 사용하도록 변경
    - [x] 게임 진행을 GameService가 전담 
    - [x] ChessService 삭제
    - [x] PieceService 삭제
    - [x] 게임 진행 요청을 GameController에서 전담
    - [x] ChessController 삭제
    - [x] Controller와 RestController 분리
    - [x] 모든 웹 요청,응답 DTO로 변경
- [x] 패키지 정리

## 3단계 이후 기능 추가 / 리팩토링 목록
- [x] 게임 리스트를 보여주는 기능 추가
  - [x] Room 도메인, Dao
  - [x] Game 테이블에서 Room 분리
  - [x] 프론트 화면 구성
- [x] 방을 만들 때 호스트 로그인 (비밀번호 확인)
- [x] 방을 검색할 때 게스트 로그인 (비밀번호 확인)
- [x] 세션에 로그인 기록, 쿠키에 로그인 유저ID 저장
- [x] 로그인 인터셉터 추가 (/games, /api/games 접근하려면 인증 필요)
- [x] 방에 참여하고 게임을 진행하는 기능 구현
- [x] SockJS와 STOMP를 사용하여 실시간 게임진행 구현
- [x] 방이 이미 가득 차면 들어가지 못하도록 수정
- [x] 방을 만드는 데 실패한 경우 예외 안내 출력
- [x] 같은 유저가 들어가지 못하게 수정
- [ ] 게임 끝나면 방 지우도록 변경
- [ ] html로 변경
-[ ] Web DTO에 기본 생성자 추가
- [ ] 예외 처리 관련 기능 추가
  - 상황에 맞게 예외를 처리하고 싶은데 방법을 잘 모르겠어서, 고민해봐야 할 듯
    - 상황 예시
    - Get 요청에 실패한 경우 404
    - Post 요청에서 select에서 실패한 경우 400
    - Post 요청에서 create에서 실패한 경우 500
  - [ ] 예외 발생시 로그 출력, 클라이언트에게 커스텀한 정보 주기
