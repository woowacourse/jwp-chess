package chess.controller;

import chess.domain.ChessGameManager;
import chess.dto.CreateGameRequest;
import chess.repository.GameRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessRestControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    GameRepository gameRepository;

    private ChessGameManager chessGameManager;
    long gameId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        this.chessGameManager = new ChessGameManager();
        this.chessGameManager.start();
        this.gameId = gameRepository.save(chessGameManager, "test title");
    }

    @AfterEach
    void flush() {
        gameRepository.delete(this.gameId);
    }

    @DisplayName("게임 목록을 불러온다")
    @Test
    void requestGamesTest() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/games")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("message", is("게임 목록을 불러왔습니다."))
                .body("item.games.gameId", hasItem((int)this.gameId))
                .body("item.games.title", hasItem("test title"));
    }

    @DisplayName("게임을 생성한다.")
    @Test
    void createNewGameTest() {
        CreateGameRequest createGameRequest = new CreateGameRequest("test title");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(createGameRequest)
                .when().post("/games")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("item.gameId", is((int)this.gameId + 1));

        this.gameRepository.delete(this.gameId + 1);
    }

//    @DisplayName("게임을 불러온다.")
//    @Test
//    void loadGameTest() {
//        RestAssured.given().log().all()
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .when().get("/games/{gameId}", this.gameId)
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value());

//    }

//    @DisplayName("기물을 이동한다.")
//    @Test
//    void movePieceTest() {
//        MoveRequest moveRequest = new MoveRequest(this.gameId, "a2", "a4");
//
//        RestAssured.given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .body(moveRequest)
//                .when().put("/games/{gameId}/pieces", this.gameId)
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value())
//                .body("item.chessBoard", hasItem("a4"));
//    }

}