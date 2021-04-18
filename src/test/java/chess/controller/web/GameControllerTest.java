package chess.controller.web;

import chess.controller.web.dto.MoveRequestDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("/Game path API test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("Game Start 테스트")
    @Test
    void StartGameTest() {
        RestAssured
                .when().get("/game/start")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("color", equalTo("WHITE"))
                .assertThat().body("piecesAndPositions", not(nullValue()));
    }

    @DisplayName("score Test")
    @Test
    void scoreTest() {
        Integer userId = RestAssured.when().get("/game/start")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().path("gameId");

        float whiteScore = (float) RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/game/score/" + userId)
                .jsonPath().getMap("colorsScore")
                .get("WHITE");

        assertThat(whiteScore).isEqualTo(38.0);
    }

    @DisplayName("move Test")
    @Test
    void movePiece() {
        Integer userId = RestAssured.when().get("/game/start")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().path("gameId");

        MoveRequestDto moveRequestDto = new MoveRequestDto(userId,"c2", "c3");

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveRequestDto)
                .when().post("/game/move")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("end", equalTo(false))
                .assertThat().body("nextColor", equalTo("BLACK"));
    }
}