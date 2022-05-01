package chess.controller;


import static org.hamcrest.core.Is.is;

import chess.SpringChessApplication;
import chess.dto.request.CreateRoomDto;
import chess.dto.request.MovePieceDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(classes = SpringChessApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChessControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("Room - POST")
    @Test
    void createRoom() {
        CreateRoomDto roomDto = new CreateRoomDto( "title",  "password");

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(roomDto)
            .when().post("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("Room - GET")
    @Test
    void getRooms() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Board - GET")
    @Test
    void getBoard() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("move - POST")
    @Test
    void move() {
        MovePieceDto movePieceDto = new MovePieceDto("f3", "f4");

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(movePieceDto)
            .when().post("/board/1")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("score - GET")
    @Test
    void score() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms/1/score")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(2));
    }


    @DisplayName("reset - POST")
    @Test
    void reset() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/rooms/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(2));
    }

    @DisplayName("end - POST")
    @Test
    void end() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().patch("/rooms/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("status - GET")
    @Test
    void status() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms/1/status")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }
}
