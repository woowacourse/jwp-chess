package chess.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import chess.domain.piece.PieceColor;
import chess.dto.MoveRequest;
import chess.dto.PathDto;
import chess.service.ChessService;
import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("Board Controller")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BoardControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ChessService service;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        int id = 1;
        service.restartBoardById(id);
    }

    @DisplayName("초기화된 보드를 가져온다 - GET")
    @Test
    void getNewBoard() {
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/board")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(1));
    }

    @DisplayName("게임 종료 여부를 boolean 타입으로 가져온다. - GET")
    @Test
    void isEnd() {
        Boolean response = RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/board/status")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().response().as(Boolean.TYPE);

        assertFalse(response);
    }

    @DisplayName("플레이어 차례를 가져온다. - GET")
    @Test
    void getTurn() {
        PieceColor color = RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/board/turn")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().response().as(PieceColor.class);

        assertThat(color).isEqualTo(PieceColor.WHITE);
    }

    @DisplayName("현재 게임의 플레이어별 점수를 가져온다. - GET")
    @Test
    void getScore() {
        Map scores = RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/board/score")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().response().as(Map.class);

        assertThat(scores.containsValue(38.0));
    }

    @DisplayName("움직일 말이 갈 수 있는 경로를 가져온다. - POST")
    @Test
    void movablePath() {
        PathDto dto = new PathDto("a2");
        List<String> expected = Arrays.asList("a3", "a4");

        List paths = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(dto)
            .when().post("/board/path")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().response().as(List.class);

        assertThat(paths).isEqualTo(expected);
    }

    @DisplayName("움직일 말이 지정한 곳으로 움직일 수 있는지 여부를 가져온다. - POST")
    @Test
    void move() {
        MoveRequest dto = new MoveRequest("a2", "a3");

        Boolean response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(dto)
            .when().post("/board/move")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().response().as(Boolean.TYPE);

        assertTrue(response);
    }
}