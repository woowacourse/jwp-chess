package chess.web.controller;

import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.MoveDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChessGameRestControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    void create() {
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto("testTitle",  "testPassword");

        RestAssured.given().log().all()
                .body(createRoomRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/room")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @Order(2)
    void getStart() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/start/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @Order(3)
    void play() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/play/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @Order(4)
    void readRooms() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @Order(5)
    void move() {
        MoveDto moveDto = new MoveDto("A2", "A4");

        RestAssured.given().log().all()
                .body(moveDto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/move/1")
                .then().log().all();
//                .statusCode(HttpStatus.OK.value())
//                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @Order(6)
    void delete() {
        DeleteDto deleteDto = new DeleteDto("testpassword");
        RestAssured.given().log().all()
                .body(deleteDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/room/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
