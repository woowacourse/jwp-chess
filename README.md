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
- [x] Controller에서 String 대신 Enum을 받도록 변경 (DTO 포함)
- [ ] ChessController URI 정리
- [ ] Controller와 API Controller 분리계
- [ ] 모든 응답 DTO로 변경
- [ ] ChessController, ChessService 가 필요한지 검토
- [ ] Game 도메인과 GameRepository를 생성하여 역할을 부여해줄 필요가 있다.
