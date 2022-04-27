package chess.controller;

import static org.hamcrest.core.Is.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import chess.dto.MoveDto;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GameControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("체스 게임을 시작하고 게임보드 데이터가 전송된다.")
    @Test
    void start() {
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/game/0/start")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("values.size()", is(64));
        RestAssured.post("/game/0/end");
    }

    @DisplayName("체스 게임을 종료하고 게임보드 데이터가 전송된다.")
    @Test
    void end() {
        RestAssured.post("/game/0/start");
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/game/0/end")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("values.size()", is(64));
    }

    @DisplayName("체스 게임 중 간에 게임점수 데이터가 전송된다.")
    @Test
    void status() {
        RestAssured.post("/game/0/start");
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/game/0/status")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("score.size()", is(2));
        RestAssured.post("/game/0/end");
    }

    @DisplayName("저장 되어 있는 체스 게임을 불러온다.")
    @Test
    void load() {
        RestAssured.post("/game/0/start");
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/game/0/load")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("values.size()", is(64));
        RestAssured.post("/game/0/end");
    }

    @DisplayName("체스 기물을 이동시킨다.")
    @Test
    void move() {
        MoveDto moveDto = new MoveDto("a2", "a3");

        RestAssured.post("/game/0/start");
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(moveDto)
            .when().post("/game/0/move")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("values.size()", is(64));
        RestAssured.post("/game/0/end");
    }
}
