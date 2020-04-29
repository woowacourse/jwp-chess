# 웹 기반 체스 게임 구현을 위한 저장소
## 작은곰 - 코일 페어
### 기존 코드에 대한 변경사항

- 이전 미션 코일의 코드를 사용하기로 합니다.

    - [이전 미션 코일의 Repository](https://github.com/slowcoyle/java-chess)
- 추가적인 변경 사항은 다음과 같습니다.
    
    - [x] MVC 적용 -> Controller, DTO, Service, DAO 분리.
    - [ ] 테스트 코드 추가 작성.
        - fake DAO를 사용하여 추가하는 방향으로 이동.

- 수정 사항은 다음과 같습니다.

    - [x] 폰의 이동 중, 대각선으로의 이동이 규칙에 맞지 않게 이동이 가능하다. 이에 대한 수정이 필요하다.
    - [x] ChessGame을 뷰에 뿌릴 때 Turn과 스코어가 갱신되는 기능 
    
- 도메인 변경사항

    - [x] 추 후 다중 게임 환경에 유연하게 대처하기 위한 도메인 리팩토링
        - [x] 매 API 통신마다 ChessGame을 생성하여 도메인 로직 구동 
        
        
## 시즌 3 - JDBC 적용하기

## To-Do List

 - [x] resources 패키지 내의 테이블을 생성하는 sql 파일 작성
 - [x] Entity 클래스 작성
 - [x] Repository 작성
 - [x] Entity to Domain 로직 구현
 - [ ] Service 구현