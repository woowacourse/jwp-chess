package chess.controller.web;

import chess.controller.web.dto.game.GameRequestDto;
import chess.controller.web.dto.move.MoveRequestDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringWebChessRestControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        GameRequestDto gameRequestDto =
                new GameRequestDto("user1", "user2", "roomName1");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gameRequestDto)
                .when()
                .post("/games")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("gameId", notNullValue());
    }

    @Test
    @DisplayName("체스 게임 생성 요청, method = post, path = /games")
    void saveGame() {
        GameRequestDto gameRequestDto =
                new GameRequestDto("user3", "user4", "roomName2");
        RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(gameRequestDto)
                .when()
                    .post("/games")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("gameId", notNullValue());
    }

    @Test
    @DisplayName("체스 게임 보드 정보(체스말들의 정보) 가져오기, method = get, path = /games/1/pieces")
    void findPiecesByGameId() {
        RestAssured
                .given().log().all()
                .when()
                    .get("/games/1/pieces")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("size()", is(64));
    }

    @Test
    @DisplayName("체스 게임 점수 불러오기, method = get, path = /games/1/score")
    void findScoreByGameId() {
        RestAssured
                .given().log().all()
                .when()
                    .get("/games/1/score")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("whiteScore", notNullValue())
                    .body("blackScore", notNullValue());
    }

    @Test
    @DisplayName("체스 게임의 상태 불러오기, method = get, path = /games/1/state")
    void findStateByGameId() {
        RestAssured
                .given().log().all()
                .when()
                    .get("/games/1/state")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("turnOwner", notNullValue())
                    .body("turnNumber", notNullValue())
                    .body("playing", notNullValue());
    }

    @Test
    @DisplayName("체스 게임의 기록 불러오기, method = get, path = /games/1/history")
    void findHistoryByGameId() {
        RestAssured
                .given().log().all()
                .when()
                    .get("/games/1/history")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("size()", is(0));
    }

    @Test
    @DisplayName("source 위치에 체스말 이동 가능 경로 불러온다, method = get, path = /games/1/path?source=c2")
    void movablePath() {
        RestAssured
                .given().log().all()
                .when()
                    .get("/games/1/path?source=c2")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("size()", notNullValue());

    }

    @Test
    @DisplayName("requestBody 로 보내는 source 와 target 으로 체스말 이동 요청한다, method = post, path = /games/1/move")
    void move() {
        MoveRequestDto moveRequestDto = new MoveRequestDto("a2", "a3");
        RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(moveRequestDto)
                .when()
                    .post("/games/1/move")
                .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("moveCommand", notNullValue())
                    .body("turnOwner", notNullValue())
                    .body("turnNumber", notNullValue())
                    .body("playing", notNullValue());
    }
}