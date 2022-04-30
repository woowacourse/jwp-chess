package chess.controller;

import chess.controller.dto.request.CreateGameRequest;
import chess.controller.dto.request.MoveRequest;
import chess.dao.GameDao;
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

    private static final CreateGameRequest CREAT_GAME_REQUEST = new CreateGameRequest("game", "password");

    @LocalServerPort
    int port;

    @Autowired
    private ChessService chessService;

    @Autowired
    private GameDao gameDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        gameDao.findAll()
                .forEach(gameEntity -> gameDao.delete(gameEntity.getId()));
    }

    @DisplayName("GET - 게임 리스트 조회 테스트")
    @Test
    void load_Games() {
        chessService.createGame(CREAT_GAME_REQUEST);

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
            Long gameId = chessService.createGame(CREAT_GAME_REQUEST);

            RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/games/" + gameId)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @DisplayName("게임이 존재하지 않으면 조회에 실패한다.")
        @Test
        void load_Fail() {
            Long gameId = chessService.createGame(CREAT_GAME_REQUEST);
            gameDao.updateState(gameId, GameState.WHITE_WIN);
            chessService.deleteGame(gameId, CREAT_GAME_REQUEST.getPassword());

            RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/games/" + gameId)
                    .then().log().all()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @DisplayName("POST - 게임 생성 테스트")
    @Test
    void create() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(CREAT_GAME_REQUEST)
                .when().post("/games")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("POST - 게임 이름 중복 테스트")
    @Test
    void duplicated_Game_Name() {
        chessService.createGame(CREAT_GAME_REQUEST);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(CREAT_GAME_REQUEST)
                .when().post("/games")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", Matchers.equalTo("이미 존재하는 게임입니다."));
    }

    @DisplayName("PATCH - 게임 시작 테스트")
    @Test
    void start() {
        Long gameId = chessService.createGame(CREAT_GAME_REQUEST);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/games/" + gameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("gameState", Matchers.equalTo("WHITE_RUNNING"));
    }

    @DisplayName("PUT - 게임 초기화 테스트")
    @Test
    void reset() {
        Long gameId = chessService.createGame(CREAT_GAME_REQUEST);
        chessService.startGame(gameId);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/games/" + gameId)
                .then().log().all()
                .body("gameState", Matchers.equalTo("READY"));
    }

    @DisplayName("PATCH - 체스 기물 이동 테스트")
    @Test
    void move() throws JsonProcessingException {
        Long gameId = chessService.createGame(CREAT_GAME_REQUEST);
        chessService.startGame(gameId);

        ObjectMapper objectMapper = new ObjectMapper();
        MoveRequest moveRequest = new MoveRequest("a2", "a3");
        String jsonString = objectMapper.writeValueAsString(moveRequest);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonString)
                .when().patch("/games/" + gameId + "/pieces")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("GET - status 조회 테스트")
    @Test
    void status() {
        Long gameId = chessService.createGame(CREAT_GAME_REQUEST);
        chessService.startGame(gameId);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/games/" + gameId + "/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("DELETE - 게임 삭제 테스트")
    @Test
    void end() {
        Long gameId = chessService.createGame(CREAT_GAME_REQUEST);
        gameDao.updateState(gameId, GameState.WHITE_WIN);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "password")
                .when().delete("/games/" + gameId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
