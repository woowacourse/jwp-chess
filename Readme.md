# Spring을 활용한 체스

## 기능 구현 목록
- [x] 메인 페이지(index.html) Spark -> Spring
- [x] /move POST
- [x] /grid/:roomName GET
- [x] /grid/:gridId/start POST
- [x] /grid/:gridId/finish POST
- [x] /room/:roomId/restart GET

### JDBC
- [ ] Spring 내부 DB를 사용하도록 구현 
  -> schema.sql 활용해서 DDL 실행시키기
- [ ] DAO를 Spring JDBC로 코드 교체하기