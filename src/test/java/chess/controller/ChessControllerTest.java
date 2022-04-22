package chess.controller;

import chess.service.ChessService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
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
}
