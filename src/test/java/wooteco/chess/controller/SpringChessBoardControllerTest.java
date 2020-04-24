package wooteco.chess.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringChessBoardControllerTest {

    @Autowired
    SpringChessBoardController controller;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("index.hbs 렌더링이 된다")
    @Test
    void index() {
        given()
            .log().all()
            .when()
            .get("/")
            .then()
            .log().all()
            .statusCode(200)
            .body(containsString("우아한테크체스"));
    }
}
