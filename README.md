# java-chess

체스 미션 저장소

## 우아한테크코스 코드리뷰

# 1단계 - Spring 적용하기 
### 요구사항
- [x] Spring MySQL 연동
- [x] Spring MVC gradle 설정
- [x] Spring JDBC gradle 설정
- [x] 기존 Spark 환경을 Spring으로 변경
- [x] JDBC 템플릿으로 변경


### 체크리스트

- [x] GameRoomResponse에 password 필드가 필요한지 확인하고 아니라면 제거 
- [x] 존재하지 않는 리소스를 요청할 경우 상태코드 404와 NOT FOUND 페이지 반환
- [ ] presentation 레이어까지 Domain이 넘어오는 문제를 해결하고, 그럴때 어떤 문제가 있는지 공부하기
- [ ] 체스방에 입장할 때 비밀번호를 입력하는 기능 추가

### 질문할 내용
- ChessService.movePiece 메서드에 DTO를 적용해야할까? 

- [온라인 코드 리뷰 과정](https://github.com/woowacourse/woowacourse-docs/blob/master/maincourse/README.md)
