//package chess.controller;
//
//import chess.dto.MovePositionDto;
//import chess.service.ChessGameService;
//import io.restassured.RestAssured;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//
//import static org.hamcrest.core.Is.is;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class ChessGameControllerTest {
//
//    private final String newGameName = "rexGame";
//
//    @LocalServerPort
//    int port;
//
//    @Autowired
//    private ChessGameService service;
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.port = port;
//    }
//
//    @Nested
//    @DisplayName("[Post] 게임 생성")
//    class CreateGameTest {
//        @Test
//        @DisplayName("새로운 게임을 성공적으로 생성한다.")
//        void createNewGame() {
//            RestAssured.given().log().all()
//                    .when().post("/new-game?name=" + newGameName)
//                    .then().log().all()
//                    .statusCode(HttpStatus.OK.value())
//                    .contentType(MediaType.APPLICATION_JSON_VALUE)
//                    .body("gameName", is(newGameName));
//        }
//
//        @Test
//        @DisplayName("중복된 이름의 게임이 있으면 실패한다.")
//        void failToCreateGame() {
//            service.createNewChessGame(newGameName);
//
//            RestAssured.given().log().all()
//                    .when().post("/new-game?name=" + newGameName)
//                    .then().log().all()
//                    .statusCode(HttpStatus.BAD_REQUEST.value());
//        }
//    }
//
//    @Nested
//    @DisplayName("[Get] 게임 불러오기")
//    class LoadGameTest {
//        @Test
//        @DisplayName("게임을 성공적으로 불러온다.")
//        void loadGame() {
//            service.createNewChessGame(newGameName);
//
//            RestAssured.given().log().all()
//                    .when().get("/load?name=" + newGameName)
//                    .then().log().all()
//                    .statusCode(HttpStatus.OK.value())
//                    .body("gameName", is(newGameName));
//        }
//
//        @Test
//        @DisplayName("생성된 게임이 없으면 게임을 불러오기가 실패한다.")
//        void failToLoadGame() {
//            RestAssured.given().log().all()
//                    .when().get("/load?name=" + newGameName)
//                    .then().log().all()
//                    .statusCode(HttpStatus.BAD_REQUEST.value());
//
//            service.createNewChessGame(newGameName);
//        }
//    }
//
//    @Nested
//    @DisplayName("[Delete] 게임 삭제하기")
//    class DeleteGameTest {
//        @Test
//        @DisplayName("게임을 성공적으로 삭제한다.")
//        void deleteGame() {
//            service.createNewChessGame(newGameName);
//
//            RestAssured.given().log().all()
//                    .when().delete("/delete?name=" + newGameName)
//                    .then().log().all()
//                    .statusCode(HttpStatus.OK.value());
//
//            service.createNewChessGame(newGameName);
//        }
//
//        @Test
//        @DisplayName("게임이 없으면 400번 상태 코드가 반환된다.")
//        void failToDeleteGame() {
//            RestAssured.given().log().all()
//                    .when().delete("/delete?name=" + newGameName)
//                    .then().log().all()
//                    .statusCode(HttpStatus.BAD_REQUEST.value());
//
//            service.createNewChessGame(newGameName);
//        }
//    }
//
//    @Nested
//    @DisplayName("[Get] 게임 점수 조회하기")
//    class FindGameStatusTest {
//        @Test
//        @DisplayName("점수 조회를 성공적으로 조회한다.")
//        void findStatusByGameName() {
//            service.createNewChessGame(newGameName);
//
//            RestAssured.given().log().all()
//                    .when().get("/status?name=" + newGameName)
//                    .then().log().all()
//                    .statusCode(HttpStatus.OK.value())
//                    .body("whitePlayerScore", is(38.0f));
//        }
//
//        @Test
//        @DisplayName("점수 조회 시 게임이 없으면 400번 상태 코드가 반환된다.")
//        void failToFindStatusByGameName() {
//            RestAssured.given().log().all()
//                    .when().get("/status?name=" + newGameName)
//                    .then().log().all()
//                    .statusCode(HttpStatus.BAD_REQUEST.value());
//
//            service.createNewChessGame(newGameName);
//        }
//    }
//
//    @Nested
//    @DisplayName("[Put] 체스 말 움직이기")
//    class MovePieceTest {
//        @Test
//        @DisplayName("말을 성공적으로 이동했다")
//        void movePiece() {
//            service.createNewChessGame(newGameName);
//            MovePositionDto movePositionDto = new MovePositionDto(newGameName, "a2", "a4");
//
//            RestAssured.given().log().all()
//                    .contentType(MediaType.APPLICATION_JSON_VALUE)
//                    .body(movePositionDto)
//                    .when().put("/move")
//                    .then().log().all()
//                    .statusCode(HttpStatus.OK.value());
//        }
//
//        @Test
//        @DisplayName("올바른 명령이 아닐 경우 말 이동이 실패한다.")
//        void failToMovePiece() {
//            service.createNewChessGame(newGameName);
//            MovePositionDto movePositionDto = new MovePositionDto(newGameName, "a3", "a4");
//
//            RestAssured.given().log().all()
//                    .contentType(MediaType.APPLICATION_JSON_VALUE)
//                    .body(movePositionDto)
//                    .when().put("/move")
//                    .then().log().all()
//                    .statusCode(HttpStatus.BAD_REQUEST.value());
//        }
//    }
//
//    @AfterEach
//    void afterEach() {
//        service.deleteGame(newGameName);
//    }
//}
