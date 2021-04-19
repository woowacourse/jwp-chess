# Step1 

## 1차 피드백
- SpringChessApplication
    - [ ] Controller 어노테이션의 역할? RestController
    
- SpringChessController
    - [ ] 각 상황에 맞는 메소드 활용 (http 메소드가 각 어떤 상황에서 활용되는지): line22
    - [ ] produces의 역할?
    - [ ] final 선언: line 16
    
- PiecePositionDaoConvertor
    - [ ] String.valueOf, Boolean.valueOf 활용하기
    
- SpringChessGameDao
    - [ ] 만약 검색되는 게임이 없다면..?!
    
- ChessGameDto
    - [ ] 맵 활용 이유?! ScoreDto 활용은 어떤지 고려하기
    
## 개인 todos
- [ ] 코드 내용 파악하기
    - [x] domain 구조 파악
    - [ ] js 구조 파악
    - [ ] db 관련 구조 파악
- [x] 테스트 코드 가져오기
- [ ] 코드 도메인 구성 변경해보기
- [ ] 코드 Dto 구성 변경해보기
- [ ] 코드 테이블 구조 변경해보기
- [ ] 클래스 이름 통일하기 (카멜케이스) 
- [ ] 학습로그 정리하기
    - [ ] @Controller, @RestController의 차이
    - [ ] Spring 어노테이션의 전체적인 정리
    - [ ] produces, consumes 정리
    - [ ] Spring 간단한 동작원리 정리
    - [ ] http 메소드와 사용 상황 정리    

## 임시 checkPoints
- Piece interface 만들기
- Dao interface
- Service interface
- CapturedPieces 삭제
- Piece의 moved -> isFisrtMoved로 변경
- (0,0)의 위치정보를 "a1"등의 문자열 형태로 변경해주는 내용 수정