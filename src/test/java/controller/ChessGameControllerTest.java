package controller;

import chess.SpringChessApplication;
import chess.dto.request.MakeRoomRequest;
import chess.dto.response.MoveResponse;

import chess.service.ChessService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;

import static org.hamcrest.core.Is.is;

@SpringBootTest(classes = SpringChessApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChessGameControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ChessService chessService;

    private long id;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        id = chessService.initializeGame(new MakeRoomRequest("green green", "1234"));
    }

    @AfterEach
    void delete() {
        chessService.endGame(id);
    }

    @DisplayName("체스 보드 초기화 확인 - GET")
    @Test
    @Order(1)
    void getBoard() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/loadBoard/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("pieces.size()", is(64))
                .body("team", is("WHITE"))
                .body("id", is((int) id));
    }

    @DisplayName("기물 move 확인 - POST")
    @Test
    @Order(2)
    void movePiece() {
        MoveResponse moveResponse = new MoveResponse("f2", "f3");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveResponse)
                .when().post("/move/" + id)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("size()", is(2))
                .body("gameState", is("BLACK"))
                .body("running", is(true));
    }

    @DisplayName("체스 게임 점수 확인 - GET")
    @Test
    @Order(3)
    void statusGame() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/status/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("whiteScore", is((float) 38.0))
                .body("blackScore", is((float) 38.0));
    }

    @DisplayName("체스 게임 reset 확인 - POST")
    @Test
    @Order(4)
    void resetGame() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reset/" + id)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("pieces.size()", is(64))
                .body("team", is("WHITE"))
                .body("id", is((int) id));
    }

    @DisplayName("체스 게임방 정보 확인 - GET")
    @Test
    @Order(5)
    void getGameList() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/loadGames")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("games.id[0]", is((int) id))
                .body("games.name[0]", is("green green"))
                .body("games.password[0]", is("1234"));
    }

    @DisplayName("체스 게임 종료 확인 - POST")
    @Test
    @Order(6)
    void endGame() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/end/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("gameState", is("NONE"))
                .body("running", is(true));
    }
}
