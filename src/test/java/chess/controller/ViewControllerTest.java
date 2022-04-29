package chess.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ViewControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Get - url: /")
    void index() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html");
    }

    @Test
    @DisplayName("Get - url: /game/{roomId}")
    void moveGameRoom() {
        RestAssured.given().log().all()
                .when().get("/game/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html");
    }

    @Test
    @DisplayName("Get - url: /game/create")
    void createGame() {
        RestAssured.given().log().all()
                .when().get("/game/create")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html");
    }

    @Test
    @DisplayName("Get - url: /game/list")
    void moveGameList() {
        RestAssured.given().log().all()
                .when().get("/game/list")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html");
    }
}
