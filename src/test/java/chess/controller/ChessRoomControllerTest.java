package chess.controller;

import static org.hamcrest.CoreMatchers.containsString;

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

    @DisplayName("게임방을 생성 할 수 있는 홈페이지를 보여준다.")
    @Test
    void home() {
        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
