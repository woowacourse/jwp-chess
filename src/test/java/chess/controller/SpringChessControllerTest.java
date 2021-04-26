package chess.controller;

import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringChessControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("index 테스트")
    void index() {
        RestAssured.given().log().all()
            .contentType(MediaType.TEXT_HTML_VALUE)
            .when().get("/")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("enter 테스트")
    void enter() {
        RestAssured.given().log().all()
            .contentType(MediaType.TEXT_HTML_VALUE)
            .when().get("/rooms/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }
}