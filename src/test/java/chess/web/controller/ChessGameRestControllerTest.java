package chess.web.controller;

import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.MoveDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessGameRestControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void getStart() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/start/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void play() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/play/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void move() {
        MoveDto moveDto = new MoveDto("a2", "a4");

        RestAssured.given().log().all()
                .body(moveDto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/move")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void create() {
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto("testTitle",  "testPassword");

        RestAssured.given().log().all()
                .body(createRoomRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/create")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void readRooms() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void delete() {
        DeleteDto deleteDto = new DeleteDto("testpassword");
        RestAssured.given().log().all()
                .body(deleteDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/room/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
