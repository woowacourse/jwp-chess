package chess.controller;

import static org.assertj.core.api.Assertions.assertThat;

import chess.controller.dto.request.MoveRequest;
import chess.domain.GameState;
import chess.service.ChessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessControllerTest {

    private final long testGameId = 1;

    @LocalServerPort
    int port;

    @Autowired
    private ChessService chessService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        chessService.deleteGame(testGameId);
    }

    @DisplayName("GET - load api 테스트")
    @Test
    void load() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/load/" + testGameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("GET - start api 테스트")
    @Test
    void start() {
        chessService.createOrLoadGame(testGameId);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/start/" + testGameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("GET - restart api 테스트")
    @Test
    void restart() {
        chessService.createOrLoadGame(testGameId);
        chessService.startGame(testGameId);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/restart/" + testGameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("POST - move api 테스트")
    @Test
    void move() throws JsonProcessingException {
        chessService.createOrLoadGame(testGameId);
        chessService.startGame(testGameId);

        ObjectMapper objectMapper = new ObjectMapper();
        MoveRequest moveRequest = new MoveRequest("a2", "a3");
        String jsonString = objectMapper.writeValueAsString(moveRequest);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonString)
                .when().post("/api/move/" + testGameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("GET - status api 테스트")
    @Test
    void status() {
        chessService.createOrLoadGame(testGameId);
        chessService.startGame(testGameId);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/status/" + testGameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("GET - end api 테스트")
    @Test
    void end() {
        chessService.createOrLoadGame(testGameId);
        chessService.startGame(testGameId);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/end/" + testGameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        assertThat(chessService.loadGame(testGameId).getGameState()).isEqualTo(GameState.FINISHED);
    }
}
