package chess.controller;

import static org.hamcrest.Matchers.notNullValue;

import chess.domain.dto.RoomDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessRoomControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void createRoom_success() {
        RoomDto roomDto = new RoomDto("pkroom");
        RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(roomDto)
                .when()
                    .post("/api/room")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("name", notNullValue());
    }

    @Test
    void enterRoom_success() {
        RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("name", "pkroom")
                .when()
                    .get("/api/room/pkroom")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("pieces", notNullValue())
                    .body("currentTeam", notNullValue())
                    .body("scoreDto", notNullValue());
    }

    @Test
    void showRooms_success() {
        RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/api/room/all")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("rooms", notNullValue());
    }
}