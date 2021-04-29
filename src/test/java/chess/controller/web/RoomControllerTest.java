package chess.controller.web;

import chess.controller.RoomRequestDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("Game Start 테스트")
    @Test
    void StartGameTest() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new RoomRequestDto("테스트", "1111"))
                .when().post("/room")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("활성화 중인 게임을 가져올 수 있는지 확인")
    @Test
    void getGamesTest() {
        RestAssured
                .when().get("/room")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("activeRooms", not(nullValue()));
    }
}