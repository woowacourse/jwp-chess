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
- [ ] Service와 Controller 리팩토링
    - [x] UserService 에서 UserRepository 사용하도록 변경
    - [x] 게임 진행을 GameService가 전담 
    - [x] ChessService 삭제
    - [x] PieceService 삭제
    - [x] 게임 진행 요청을 GameController에서 전담
    - [x] ChessController 삭제
    - [x] Controller와 RestController 분리
    - [ ] 모든 웹 요청,응답 DTO로 변경
- [x] 패키지 정리
- [ ] 예외 처리 관련 기능 추가
    - [x] data select에 싪패하면 404 에러 발생
 

