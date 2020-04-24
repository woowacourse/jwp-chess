# 체스 - 스프링 실습
## 

## STEP1



### 기능목록

- 체스판을 초기화 할 수 있다.

- 말 이동을 할 수 있다.

  - 각 말의 이동규칙에 따라 이동해야한다.
  - 체스판 내의 이동 범위로 이동해야한다.

- 승패 및 점수를 판단할 수 있다.

  - 승패는 왕의 생존여부로 확인한다.
  - 점수는 살아있는 말의 점수들을 계산한다.

- 웹으로 체스 게임이 가능해야 한다.(스파크 적용)

  - Controller - Service - DAO, 3-tier으로 분리한다.
  - handlebars를 이용하여 템플릿에서 모델을 사용한다.

- 웹 서버를 재시작하더라도 이전에 하던 체스 게임을 다시 시작할 수 있어야 한다.(DB 적용)

  - Mysql을 사용한다.

  - ```mysql
    create table chessBoard(
        position varchar(10) NOT NULL,
        pieceName varchar(10) NOT NULL
    );
    ```

  - ``` mysql
    create table turn(
        teamName varchar(10) NOT NULL
    );
    ```

    

