package chess.controller.web;

import chess.controller.web.dto.RoomRequestDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/Game path API test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameRestControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("score Test")
    @Test
    void scoreTest() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new RoomRequestDto("테스트", "1111"))
                .when().post("/room")
                .then()
                .statusCode(HttpStatus.OK.value());

        float whiteScore = (float) RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/game/" + 1 + "/score")
                .jsonPath().getMap("colorsScore")
                .get("WHITE");

        assertThat(whiteScore).isEqualTo(38);
    }

/*
    @DisplayName("move Test")
    @Test
    void movePiece() {
        SessionFilter sessionFilter = new SessionFilter();

        RestAssured
                .given().log().all()
                .filter(sessionFilter)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new RoomRequestDto("테스트", "1111"))
                .when().post("/room")
                .then()
                .statusCode(HttpStatus.OK.value());

        MoveRequestDto moveRequestDto = new MoveRequestDto(1, "c2", "c3");

        RestAssured
                .given().log().all()
                .sessionId(sessionFilter.getSessionId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveRequestDto)
                .when().put("/game/move")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("end", equalTo(false))
                .assertThat().body("nextColor", equalTo("BLACK"));
    }
   */
}