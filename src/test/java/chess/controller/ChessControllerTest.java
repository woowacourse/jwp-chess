package chess.controller;

import chess.controller.dto.request.CreateGameRequest;
import chess.controller.dto.request.MoveRequest;
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

    private static final long TEST_GAME_ID = 1;
    private static final CreateGameRequest CREAT_GAME_REQUEST = new CreateGameRequest("game", "password");

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
        chessService.deleteGame(TEST_GAME_ID);
    }

    @DisplayName("GET - 게임 리스트 조회 테스트")
    @Test
    void load_Games() {
        chessService.createGame(TEST_GAME_ID, CREAT_GAME_REQUEST);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/games")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Nested
    @DisplayName("GET - 게임 조회 테스트")
    class LoadTest {
        @DisplayName("게임이 생성되어 있으면 조회에 성공한다.")
        @Test
        void load() {
            chessService.createGame(TEST_GAME_ID, CREAT_GAME_REQUEST);

            RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/games/" + TEST_GAME_ID)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @DisplayName("게임이 생성되어 있지 않으면 조회에 실패한다.")
        @Test
        void load_Fail() {
            RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/games/" + TEST_GAME_ID)
                    .then().log().all()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @DisplayName("POST - 게임 생성 테스트")
    @Test
    void create() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(CREAT_GAME_REQUEST)
                .when().post("/games")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("PATCH - 게임 시작 테스트")
    @Test
    void start() {
        chessService.createGame(TEST_GAME_ID, CREAT_GAME_REQUEST);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/games/" + TEST_GAME_ID)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("gameState", Matchers.equalTo("WHITE_RUNNING"));
    }

    @DisplayName("PUT - 게임 초기화 테스트")
    @Test
    void reset() {
        chessService.createGame(TEST_GAME_ID, CREAT_GAME_REQUEST);
        chessService.startGame(TEST_GAME_ID);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/games/" + TEST_GAME_ID)
                .then().log().all()
                .body("gameState", Matchers.equalTo("READY"));
    }

    @DisplayName("PATCH - 체스 기물 이동 테스트")
    @Test
    void move() throws JsonProcessingException {
        chessService.createGame(TEST_GAME_ID, CREAT_GAME_REQUEST);
        chessService.startGame(TEST_GAME_ID);

        ObjectMapper objectMapper = new ObjectMapper();
        MoveRequest moveRequest = new MoveRequest("a2", "a3");
        String jsonString = objectMapper.writeValueAsString(moveRequest);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonString)
                .when().patch("/games/" + TEST_GAME_ID + "/pieces")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("GET - status 조회 테스트")
    @Test
    void status() {
        chessService.createGame(TEST_GAME_ID, CREAT_GAME_REQUEST);
        chessService.startGame(TEST_GAME_ID);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/games/" + TEST_GAME_ID + "/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("DELETE - 게임 종료 기능 테스트")
    @Test
    void end() {
        chessService.createGame(TEST_GAME_ID, CREAT_GAME_REQUEST);
        chessService.startGame(TEST_GAME_ID);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "password")
                .when().delete("/games/" + TEST_GAME_ID)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("message", Matchers.equalTo("게임이 종료되었습니다."));
    }
}
