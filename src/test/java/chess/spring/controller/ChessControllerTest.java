package chess.spring.controller;

import chess.dto.MoveRequestDTO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.core.Is.is;

@DisplayName("ChessController Method")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("보드를 가져온다.")
    @Test
    void showBoard() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/chessgame/show")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @DisplayName("체스 기물을 이동한다.")
    @Test
    void move() {
        MoveRequestDTO moveRequestDTO = new MoveRequestDTO("a2", "a3", "WHITE");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveRequestDTO)
                .when().post("/chessgame/move")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }
}
