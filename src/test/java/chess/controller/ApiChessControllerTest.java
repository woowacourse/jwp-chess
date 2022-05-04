package chess.controller;

import static org.hamcrest.Matchers.is;

import chess.service.ChessService;
import chess.service.dto.GameRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("classpath:schema.sql")
class ApiChessControllerTest {

    @LocalServerPort
    private int port;

    private final ChessService chessService;

    @Autowired
    public ApiChessControllerTest(ChessService chessService) {
        this.chessService = chessService;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("해당 게임을 다시 시작한다.")
    @Test
    void initBoard() {
        Long id = chessService.createGame("first", "1234");
        chessService.initGame(id);

        RestAssured.given().log().all()
            .when().post("/game/" + id + "/board")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", is("/game/" + id + "/board"));
    }

    @DisplayName("해당 게임의 board를 가져온다.")
    @Test
    void getBoardPieces() {
        Long id = chessService.createGame("first", "1234");

        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/game/" + id + "/board")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("pieces.size()", is(64));
    }

    @DisplayName("게임을 종료한다.")
    @Test
    void endGame() {
        Long id = chessService.createGame("게임종료", "1234");

        RestAssured.given().log().all()
            .when().put("/game/" + id + "/end")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("게임을 삭제한다")
    @Test
    void deleteGame() {
        Long id = chessService.createGame("game", "1234");
        GameRequest request = new GameRequest("1234");
        chessService.endGame(id);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().delete("/game")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("게임의 결과를 조회한다.")
    @Test
    void getResult() {
        Long id = chessService.createGame("result", "1234");

        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/game/" + id + "/status")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("playerPoints.WHITE", is(38.0F))
            .body("playerPoints.BLACK", is(38.0F))
            .body("winnerColor", is("NOTHING"))
            .body("isDraw", is(true));
    }

    @AfterEach
    void cleanUp() {
        chessService.removeAllGame();
    }
}
