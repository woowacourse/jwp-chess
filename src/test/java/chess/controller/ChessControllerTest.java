package chess.controller;

import static org.assertj.core.api.Assertions.assertThat;

import chess.controller.dto.request.MoveRequest;
import chess.domain.GameState;
import chess.service.ChessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("GET - 게임 조회 테스트")
    class LoadTest {
        @DisplayName("게임이 생성되어 있으면 조회에 성공한다.")
        @Test
        void load() {
            chessService.createGame(testGameId);

            RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/api/games/" + testGameId)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @DisplayName("게임이 생성되어 있지 않으면 조회에 실패한다.")
        @Test
        void load_Fail() {
            RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/api/games/" + testGameId)
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @DisplayName("POST - 게임 생성 테스트")
    @Test
    void create() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/games/" + testGameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Nested
    @DisplayName("PUT - start api 테스트")
    class StartOrRestartTest {

        @DisplayName("게임이 READY 상태면 시작한다.")
        @Test
        void start() {
            chessService.createGame(testGameId);

            RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().put("/api/games/" + testGameId)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("gameState", Matchers.equalTo("WHITE_RUNNING"));
        }

        @DisplayName("게임이 READY 상태가 아니면 재시작한다.")
        @Test
        void restart() {
            chessService.createGame(testGameId);
            chessService.startGame(testGameId);
            chessService.end(testGameId);

            RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().put("/api/games/" + testGameId)
                    .then().log().all()
                    .body("gameState", Matchers.equalTo("READY"));
        }
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

    @DisplayName("Exception handle 테스트")
    @Test
    void handle_Exception() {
        chessService.createOrLoadGame(testGameId);
        chessService.startGame(testGameId);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/start/" + testGameId)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", Matchers.equalTo("현재 실행할 수 없는 명령입니다."));
    }
}
