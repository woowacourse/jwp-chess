# 스프링 입문 - 체스

## 구현할 기능 목록

### Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
- [x] index 페이지 맵핑 기능
- [x] pieces 들을 조회하는 API 기능
- [x] 가장 최근의 체스게임을 조회하는 API 기능
- [x] 체스게임을 생성하는 API 기능
- [x] 체스게임을 종료하는 API 기능
- [x] 체스게임의 점수를 확인하는 API 기능

###  Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.
- [x] ChessgameDAO#findByStateIsBlackTurnOrWhiteTurn 
- [x] ChessgameDAO#create
- [x] ChessgameDAO#updateState
- [x] ChessgameDAO#findIsExistPlayingChessGameStatus

- [x] PieceDAO#save
- [x] PieceDAO#saveAll
- [ ] PieceDAO#findAllPiecesByChessGameId
- [ ] PieceDAO#findOneByPosition
- [ ] PieceDAO#update
- [ ] PieceDAO#delete

## to-do list
* 레벨 1 체스 미션 코드 옮겨오기
* Spring MVC 적용하기
* JDBC to Spring JDBC 

## 오류 사항 
* 특정상황(새로고침 이후 게임을 이어하는 도중에)에 게임을 종료하는 기능 오류  
