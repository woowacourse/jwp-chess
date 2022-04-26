package chess.controller;

import chess.TestConfig;
import chess.dto.request.RoomCreationRequestDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
class ChessControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("모든 방을 조회한다.")
    void findAllRoom() {
        RestAssured.given().log().all()
                .when().get("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("방을 생성한다.")
    void createRoom() {
        final RoomCreationRequestDto requestDto = new RoomCreationRequestDto("test", "1234");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/rooms/1");
    }
}