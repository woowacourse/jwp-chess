package chess.controller;

import chess.dto.web.RoomDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringChessControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RoomDto roomDto = new RoomDto("1", "fortuneRoom", "fortune", "portune");
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(roomDto)
                .post("/room");
    }

    @DisplayName("로비에 들어가는데 성공하면, 성공메시지를 받는다.")
    @Test
    void lobby() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("방에 들어가는데 성공하면, 성공메시지를 받는다.")
    @Test
    void joinRoom() {
        RestAssured.given().log().all()
                .when().get("/room/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}