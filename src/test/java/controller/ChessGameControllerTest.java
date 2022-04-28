package controller;


import chess.SpringChessApplication;
import chess.dto.MakeRoomDto;
import chess.dto.MoveDto;

import chess.service.ChessService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;

import static org.hamcrest.core.Is.is;

@SpringBootTest(classes = SpringChessApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChessGameControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ChessService chessService;

    private long id;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        id = chessService.makeGame(new MakeRoomDto("green green", "1234"));
    }

    @AfterEach
    void delete() {
        chessService.endGame(id);
    }

    @DisplayName("Board - GET")
    @Test
    @Order(1)
    void getBoard() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/loadBoard/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @DisplayName("move - POST")
    @Test
    @Order(2)
    void movePiece() {
        MoveDto moveDto = new MoveDto("f2", "f3");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveDto)
                .when().post("/move/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }

    @DisplayName("game status - GET")
    @Test
    @Order(3)
    void statusGame() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }

    @DisplayName("game reset - POST")
    @Test
    @Order(4)
    void resetGame() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reset/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @DisplayName("game end - POST")
    @Test
    @Order(5)
    void endGame() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/end/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }
}
