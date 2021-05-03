package chess.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import chess.domain.dto.RoomDto;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("room api - 방을 생성하면 json을 통해 방 이름을 반환한다.")
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
                    .body("name", is("pkroom"));
    }

    @DisplayName("room api - 생성된 방에 입장하면 체스 게임에 대한 정보를 전달한다.")
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

    @DisplayName("room api - 생성된 방의 이름을 전달한다.")
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