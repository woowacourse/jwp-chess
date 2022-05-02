### 기능 요구사항

- [x] controller를 @Controller를 활용하여 요청을 받도록 수정한다.
- [x] Jdbc Template을 이용하여 Connection을 직접 만들어주는 로직을 대체한다.
    - [x] boardDao에 적용한다.
    - [x] pieceDao에 적용한다.
- [x] Jdbc Template을 빈 주입을 받아서 사용한다.
    - [x] boardDao에 적용한다.
    - [x] pieceDao에 적용한다.
- [x] 체스방 만들기
    - [x] localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.
    - [x] 체스방 만들기 버튼을 누르고 체스방 제목과 비밀번호를 입력하면 새로운 체스판이 만들어진다.
    - [x] 체스방에는 고유식별값이 부여된다. (이 고유 식별값은 체스방 주소에서 사용 됨)
    - [x] 없는 체스방으로 요청 시 에러 페이지를 출력
- [x] 체스방 목록 조회하기
    - [x] localhost:8080 요청 시 체스방 목록을 조회할 수 있다
    - [x] 체스방 목록에는 체스방 제목과 체스방을 삭제할 수 있는 버튼이 표시된다.
- [x] 체스방 참여하기
    - [x] 체스방 목록에서 체스방 제목을 클릭하면 체스 게임을 이어서 진행할 수 있다.
- [x] 체스방 삭제하기
    - [x] 체스방 목록에서 체스방 삭제 버튼을 클릭하고 체스방 생성시 설정한 비밀번호를 입력하면 체스 게임을 삭제할 수 있다.
    - [x] 진행중인 체스방은 삭제할 수 없다.

---

### Controller URI 정리

@Controller
- GET:`/` : 체스 방 리스트를 확인하고 새로운 체스 방을 만들 수 있는 페이지 출력.
- GET:`/chess/{boardId}` : 체스 게임을 할 수 있는 페이지 출력.

@RestController
- POST:`/api/board` : 새로운 체스 게임을 생성한다.
- PUT:`/api/board/{boardId}/initialization` : 체스 게임을 초기화하여 재시작한다.
- PATCH:`/api/board/{boardId}` : 체스 게임에서 시작 위치, 도착 위치를 JSON으로 전송하면 이동한다. 정상적으로 이동 시 수정된 Board를 반환한다.
- GET:`/api/board/{boardId}` : 체스 기물 상태를 가져온다.
- GET:`/api/board/{boardId}/status` : 체스 게임의 블랙, 화이트 팀의 점수를 반환한다.
- DELETE:`/api/board/{boardId}` : 체스 방을 제거한다.

---

### 피드백, 수정사항 정리

- [x] 말을 이동한 뒤 말이 이동할 수 없는 경우 보드가 먹통이 되는 에러 수정해야 함
- [x] TODO, FIXME는 당장 필요가 없다면 PR에 있을 이유가 없음
- [x] `BoardDaoImpl`에서 `save`는 무조건 white 값을 넣고 있음
- [x] 테스트에서 BeforeEach로 매번 테이블을 만드는 쿼리가 실행되는데 이런 raw한 sql 쿼리문이 노출되지 않도록 구현할 수 있을까?
- [x] 불필요한 spark 의존성을 제거하자
- [x] Controller에서 발생하는 에러를 `@ExceptionHandler`, `@ControllerAdvice`를 이용하여 처리해보자.
    - `@ControllerAdvice`와 `@ExceptionHandler`를 이용하여 문제가 발생했을 때 에러를 처리할 수 있도록 함
    - 없는 체스방 URL로 요청 시 체스방 페이지가 아닌 잘못 접속했다는 페이지를 출력하도록함
    - 이동할 수 없는 이동을 할 경우 에러를 경고창으로 알려주기위해 예외 메시지를 400에러와 함께 전송하도록 함
- [x] Post 형식으로 데이터가 생성이 되었을 경우 status code(200)을 반환하는게 맞을까?
    - Post 형식으로 요청을 받아서 어떤 데이터가 생성된 경우 statue code(201)이 반환되어야 한다.
    - ResponseEntity에서는 ResponseEntity.created()를 사용하여 반환할 수 있다.
    - status code는 요청에 대한 처리 결과가 어떤지 알려주는 코드로 1~5xx 로 반환해준다.
    - 각 코드 간단 정리
        - 1xx : 요청이 성공하여 계속 진행될 수 있음을 나타냄
        - 2xx : 요청이 성공함
        - 3xx : 리다이렉션 상태를 나타내어 사용자에게 대안을 제시함
        - 4xx : 클라이언트의 문제로 에러가 발생함
        - 5xx : 서버의 문제로 에러가 발생함
- [x] ChessWebController의 `createRoom`의 newId는 board의 id 인지 room의 id 인지 명확하면 좋을 것 같다
    - board, room 테이블을 합치면서 room의 id를 사용하도록 수정했다.
    - ~~[x] boardId라는 이름을 사용하던 변수 등을 roomId로 수정하기~~
- [x] 보통 자원을 지울때는 pk로 지우게 되는데 Room을 지울 때 pk가 아닌 boardId 값으로 지우게 된다. 혹시 같은 boardId를 여러 자원이 가진다면 모두 지우게 되는 것이 의도한 것일까?
    - board와 room 테이블을 합치면서 board_id가 아닌 roomId로 관리하도록 수정했다.
    - PieceDao의 `deleteByboardId()`는 roomId를 만족하는 모든 기물을 삭제하도록 `deleteAllByRoomId()`로 수정하였다.
    - RoomDao는 `delete()`를 사용하여 pk값인 id와 password값을 파라미터로 받아 삭제한다.
        - DB에 id와 password가 같은 데이터가 있는지 확인하는 방식(예를들면 `findByIdAndPassword()`)으로 패스워드 일치를 확인하고 `delete(id)`를 하는 식으로 할까 생각했었는데 글을 삭제하기 위해 무조건 비밀번호가 필요하다고 생각하여 `delete(id, password)`로 DAO 메서드를 만들었다.
- [x] 여러 DAO를 사용하여 자원을 처리하다가 에러가 발생해서 프로그램이 중단된다면 게임의 데이터는 어떤 상태로 유지 될까요?
    - 원자성과 트랜잭션을 키워드로 학습해보고 문제를 해결해볼까요?
    - `@Transactional`을 이용하여 여러 데이터를 처리하던 중 에러가 발생해도 원래 상태로 롤백할 수 있습니다. 
    - 내가 정한 모든 작업이 모두 성공하거나 모두 실패해야하는 성질을 원자성입니다. 
    - 또 `@Transactional`을 사용하면 서로 같은 자원을 수정하다가 한쪽이 롤백을 한다거나 할 때 발생하는 문제등을 해결할 수 있는데 이런 성질을 격리성이라고 합니다.
    - 여러 비즈니스 로직을 다루는 `Service`나 테스트에서 주로 사용한다고 합니다.
    - 저는 데이터를 읽기만 하는 경우 `@Transactional(readOnly = true)`으로 트랜잭션 어노테이션을 사용했고 나머지는 기본 설정으로 적용했습니다.
- [x] 게임을 하는 페이지에서 목록으로 이동하는 버튼이 있으면 어떨까?
    - 더 많은 가치를 만들어 내는 것이 없는지, 불편함은 없는지 등 조금 더 사용자 관점에서 생각해보도록 하자!
- [x] Controller에 대한 테스트도 추가해보자.
    - 컨트롤러 테스트로 다음 항목이 제대로 동작하는지 확인하겠다!
        1. 값이 제대로 저장, 수정, 호출, 삭제가 되는지 확인하겠다.
        2. 올바른 값이 반환되는지 확인하겠다.
        3. 적절한 status code가 반환되는지 확인하겠다.
        4. 적절한 content type이 사용되는지 확인하겠다.
- [x] API는 endpoint에 prefix로 `/api`를 붙이기도 한다!
- [x] RESTFul API에 대해 공부해보고 endpoint를 수정해보자![참고자료](https://restfulapi.net/)
    - 다음을 중점으로 생각하면서 endpoint를 수정해봤습니다.
        1. 동사형 사용 금지.
        2. 적절한 HTTP method 사용.
        3. URI에서 의도가 전달이 되는지 확인.
    - `/api/chess`와 `/api/board`중 어느것을 사용할지 고민하다가 `/api/board`로 하기로 했습니다.
        1. 뒤에 올 `{roomId}`는 Room 테이블의 id입니다. Room 테이블의 정보와 room_id를 외래키로 가진 Piece 테이블의 정보로 board가 만들어지기 때문에 따지고 보면 `/room/{roomId}`이 되어야 할 것 같았습니다.
        2. 하지만 `/room/{roomId}`라고 한다면 어떤 방인지 이해하기 어려울 것 같았습니다. (당장은 체스방만 있지만 체스 게임을 하는 방, 채팅만 하는 방, 다시보기를 보는 방 등 여러 종류의 방이 생긴다면?)
        3. 그래서 `/api/chess`와 `/api/board`중 고민하던 중 어차피 데이터베이스의 이름이 chess이기 때문에 `/api/chess`보다는 `/api/board`가 더 명확하다고 생각했습니다. 또 `/api/chess/{chessId}`라는 식의 URI는 맞지 않아보였습니다.
        4. 그래서 Room이라는 테이블의 이름을 Board로 수정하고 테이블과 URI를 수정해봤습니다.
        5. Piece는 Board테의블의 pk값을 외래키로 사용하기 때문에 DELETE:`/api/board/1`이라는 요청이 오고 board의 데이터를 지울 때 그 board_id를 가진 Piece의 데이터도 모두 삭제가 되어야 지울 수 있어서 괜찮다고 생각했습니다. 
    - [x] `/chess/{roomId}/load`는 체스 보드 하나를 반환하는 API이다. RESTFul API를 공부해보고 수정해보자!
        - 동사형인 load대신 명확하게 board를 가져온다는 의미로 board로 수정했습니다.
        - ~~전체적으로 앞에 /api를 추가하여 `/api/chess/{roomId}/board`처럼 수정했습니다.~~
        - GET:`/api/board/{boardId}`로 최종 수정했습니다. 만약 pk키인 id가아니라 다른 값으로 데이터를 찾고싶다면 `@RequestParam`을 이용할 것 같습니다.
    - [x] 자원의 상태를 "변경"하는 경우 Post보다 Put, Patch를 사용해보자.
        - 초기화를 할 때는 PUT으로, 기물을 이동하여 일부 기물의 포지션과 타입을 수정할 때는 PATCH를 사용하도록 수정했습니다.
    - [x] restart는 자원의 상태를 변경하는데 Get이 적합할까?
        - 자원의 상태를 모두 바꿔버리기 때문에 GET이 아닌 PUT으로 수정했습니다.
        - restart라는 동사형이 아닌 initialization라는 명사형으로 수정했습니다.
- [x] Advice에서 특정 에러를 제외한 다른 에러가 발생할 경우 어떻게 처리할 것인가?
    - Exception을 처리하는 ExceptionHandler를 만들어 주었다.
    - ControllerAdvice의 assignableTypes 옵션으로 어떤 컨트롤러에 대한 구체적인 예외 처리를 할 수 있도록 했다.
    - `@Controller`에서는 `NoSuchElementException`이 발생 시 체스방이 없다는 페이지를 출력하고 이 외 다른 에러의 경우 에러 메시지와 함께 error 페이지를 출력하도록 했다.
    - `@RestController`에서는 모든 에러에 대해서 같은 동작을 하게 될 것 같아서 기존의 `IllegalArgumentException`핸들러를 `Exception`핸들러로 수정했다.
- [x] RoomDao의 `updateTurnById()`는 Long을 반환하는데 이것이 사용되는 곳이 있을까?
    - `save()` 처럼 수정하고나서 그 id로 값을 찾아와 무언가를 할 수 있다고 생각했는데 이미 `updateTurnById()`에서 파라미터로 id를 사용하기 때문에 그럴 필요가 없었다.
    - 그래서 반환형을 Long이 아닌 void로 수정하였다.
- [x] 전체적으로 테스트의 이름과 DisplayName 등을 확인하고 수정하기
- [x] RoomDaoTest 파일의 가장 마지막 줄에 개행 문자가 없으면 깃헙에서 경고를 보여주는 이유가 무엇일까?
    - 경고의 내용은 "no newline at end of file"이다.
    - 마지막 줄에 개행 문자가 있어야하는 이유는 "POSIX"라는 표준에서 그렇게 정했기 때문이다. 
    - 단순한 빈 줄이 아닌 개행 문자가 와야한다. (\n, \r, \r\n)
    - 많은 시스템이나 프로그램이 이 표준에 맞추어 구현되어있기 때문에 마지막 줄에 개행문자가 없으면 의도하지 않은 동작이 발생할 수 있다고 한다.
    - 예를 들어 `gcc`의 경우 POSIX에 근거하여 마지막 줄에 개행 문자가 없으면 정상적으로 동작하지 않기도 한다고 한다.
    - 또 `cat`의 경우 실제로 프로그램의 끝까지 출력하고 다시 명령을 받기 위해 쉘 입력창이 출력될 때 마지막 출력 이후 바로 입력을 받기 때문에 개행 문자가 있는 것이 더 보기 좋다.
- [x] 변수명에 Optional인지 적을 필요는 없다!
    - 추가로 여러 검증을 하는 메서드를 하나의 메서드로 묶어 가독성이 좋아지도록 수정
- [x] 제거하려는 체스방이 없으면 false가 아닌 에러를 발생시키고 프론트에서 적절한 정보를 보여주는 것이 어떨까?
    - 제거하려는 체스방이 없으면 `NoSuchElementException`을 발생시키고 "삭제할 체스방이 없습니다."라는 에러 메시지를 400 코드와 프론트로 반환합니다.
    - 프론트는 400 코드가 전송되면 에러 메시지를 출력합니다.
- [x] 체스방 삭제 시 id는 맞지만 비밀번호가 틀린 경우 어떻게 처리할 것인가?
    - 비밀번호가 틀린 경우 `IllegalArgumentException`을 발생시키고 "비밀번호가 틀렸습니다."라는 에러 메시지를 400 코드와 프론트로 반환합니다.
    - 프론트는 400 코드가 전송되면 에러 메시지를 출력합니다.
- [x] `!isDeadKing()`과 `isKingAlive()`의 차이점이 무엇이 있을까? 내 코드와 토니의 코드의 차이점 [리뷰 링크](https://github.com/woowacourse/jwp-chess/pull/426/files/63119ae4ae162dd63282fc6780e8dc83c7b2a7ec#r862330057)
    1. isXXX()메서드를 사용하면서 영향을 주는 메서드들의 결을 맞추기 위해. 추가로 부정(!) 사용을 최소화하기 위해.
    2. 저는 두 줄로 나누어 코드를 작성했는데 한줄로 줄일 수 있을 것 같다.
    ```text
        boolean deadKing = board.isDeadKing();
        return !deadKing; // 수정 전
        return !board.isDeadKing(); // 수정 후
    ```
    - is, has, can 등 boolean형을 반환하는 메서드는 가능하면 연관된 여러 메서드들의 결을 통일시키는 것이 좋다고 들었습니다.
    - 예를 들면 연료가 있어야 자동차가 이동할 수 있다면 자동차가 이동할 수 있는지 확인하는 canMoveCar()라는 메서드에서 hasFuel()이라는 메서드를 쓰는것이 좋다는 의미입니다.
    - 이런 통일성을 맞추기위 위한 기준은 보통 긍정형으로 메서드를 만드는 것이 좋다고 들었습니다.
    - 예를들면 isNotKing()보다 isKing()이 좋다는 의미입니다.
    - 이런 관점에서 `isRunningChess()`메서드에서는 `!isDeadKing()`과 `isKingAlive()`중 `isKingAlive()`를 사용하는 것이 더 좋아보입니다. 왕이 살아있어야 게임이 진행중이기 때문입니다.
    - `isRunningChess()`와 `isDeadKing()`메서드의 특징이 다르기 때문에 반환을 하면서 !를 붙여줘야하고 코드의 이해도가 낮아질 것 같습니다.
    - 심지어 프로덕션 코드에서 `if (!isRunningChess())`와 같이 사용되기 때문에 부정을 두번 하고 있어 더 이해도가 낮아질 것 같습니다.
    - 이 문제를 해결하기 위해 `isKingAlive()`를 만들어 사용하거나 `isRunningChess()`를 `isFinishedChess()`로 수정하고 `isDeadKing()`를 반환하는 것도 방법이 될 것 같습니다.
    - 저는 새로운 Board에 새로운 메서드를 만들지 않아도 되고 상태에 대한 확인이기 때문에 `isRunningChess()`를 `isFinishedChess()`로 수정하는 방식으로 수정해봤습니다.
- [ ] API의 endpoint에서 복수형으로 수정하자
    - RESTFul API는 endpoint에서 복수형을 사용하자는 규칙을 강조한다.
- [ ] ApiController에서 모든 에러가 Bad request를 반환하는데 만약 DB나 서버 내부에서 에러가 발생한다면 적절한 status code일까?
- [ ] 30자 이상의 방 이름, 비밀번호를 입력하면 어떻게 될까요?
- [ ] ChessApiController의 `loadGame()`에서 기물이 없거나 게임이 끝나면 초기화시켜주어 상태를 변화시킨다. 수정하자!
- [ ] 남아있는 Room의 흔적을 지우자. 
- [ ] assertAll()의 장점을 생각해보고 적용해보자

---

- 기존 `JdbcTemplate`없이 영속성 레이어를 구현할 때와 `JdbcTemplate`를 활용할 때의 차이점은 무엇일까?
    - 가장 체감되게 느낀 점 세 가지
        - Connection를 이용하여 열고 닫는 것들을 `JdbcTemplate`가 대신 관리해준다. 가독성을 해치는 try 문이 없어지고 에러가 나도 close 해준다.
        - PreparedStatement를 내가 할 필요없이 파라미터로 값을 주기만 하면 되어서 코드가 간단해지고 가독성이 높아졌다.
        - RowMapper를 따로 만들어주어야 하지만 코드 중간에 갑자기 ResultSet를 이용하는 것 보다 가독성이 좋아보였다. 특히 여러 값을 받아와야 한다면 반복문을 사용하지 않아도 되어서 보기
          편했다.
    - 이번 미션에서는 데이터 소스를 따로 만들지 않아 간단하게 사용할 수 있었지만 더 큰 프로젝트를 하거나 여러 DB에 접속해야 하는 상황에서도 `JdbcTemplate`를 활용하면 더 가독성이 좋은 코드로
      구현할 수 있을 것 같다.
    - 중간중간 `query`, `queryForObject`, `RowMapper` 사용에 익숙하지 않아서 기능 구현보다 에러 수정이 더 오래 걸린 것 같지만 공식 문서나 학습 테스트를 해보면서 해결해보니
      오히려 더 자신감이 생긴 것 같다.
- 스프링에서 의존성을 주입하는 방법은 생성자 주입, 필드 주입, 세터 주입이 있다. 왜 생성자 주입으로 구현했는가?
    - 처음에는 `JdbcTemplate`은 빈 주입을 하라는 요구사항을 만족하기 위해 `@Autowired`를 이요한 필드 주입 방식으로 구현했었다.
    - 그러나 `ChessService`를 테스트 할 때 DAO들에 가짜 객체를 주입해야하는데 그런 상황에서 `@AutoWired`가 적합하지 않았습니다.
    - 이러한 의존성 주입 방식을 찾다보니 생성자 주입, 필드 주입, 세터 주입이 있다고 했는데 미션의 요구사항과 가짜 객체를 적용하기에 생성자 주입이 가장 적합하다고 생각하여 적용했습니다.
    - 세터 주입도 가짜 객체를 주입할 수 있겠지만 불변성을 만족할 수 없다고 생각되어서 적용하지 않았습니다.
    - 이후 크루들과 이야기하면서 생성자 주입을 하면 직접 Null을 주입하지 않는 이상 NullPointerException이 발생하지 않고 스프링에서 생성자 주입 방식을 추천한다고 들었습니다.
- Optional을 사용하는 이유는 무엇일까요?
    - 가장 큰 이유는 null이 올 수 있는 상황을 개발자에게 알려주고 NullPointerException을 방지하기 위해서라고 생각합니다.
    - Optional을 사용하지 않고 null을 처리하는 경우 코드를 작성한 개발자는 알 수 있지만 다른 개발자는 null이 반환될 수 있는 상황인지 알기 어려울 수 있습니다.
    - 하지만 Optional를 사용하면 null이 있을 수 있다는 것을 확실하게 알 수 있고 값이 null인 경우 예외를 발생할지 새로운 값을 만들어 반환할지 등을 정할 수 있습니다.
