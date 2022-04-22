package chess.controller;

import static org.hamcrest.core.Is.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringWebChessControllerTest {

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
                .when().post("/game/start")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("values.size()", is(64));
    }

    @DisplayName("체스 게임을 종료하고 게임보드 데이터가 전송된다.")
    @Test
    void end() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/game/end")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("values.size()", is(64));
    }

    @DisplayName("체스 게임 중 간에 게임점수 데이터가 전송된다.")
    @Test
    void status() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/game/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("score.size()", is(2));
    }
}
