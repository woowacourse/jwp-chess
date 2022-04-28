package chess.web.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChessGameControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void getRoot() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    void getPlay() {
        RestAssured.given().log().all()
                .when().get("/play/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    void getEnd() {
        RestAssured.given().log().all()
                .when().get("/end")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }
}
