package chess.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void indexPage() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void startGame() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/start")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(containsString("positionsAndPieces"))
                .body(containsString("whiteScore"))
                .body(containsString("blackScore"))
                .body(containsString("result"));
    }

    @Test
    void restart() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/restart")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(containsString("positionsAndPieces"))
                .body(containsString("38"))
                .body(containsString("EMPTY"));
    }

//    @Test
//    void move() {
//        RestAssured.given().get("/start");
//        RestAssured.given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(new MoveDto("A2", "A3"))
//                .when().put("/move")
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value())
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(containsString("A3"));
//    }

//    @Test
//    void requestMoveBeforeStart() {
//        RestAssured.given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(new MoveDto("A2", "A3"))
//                .when().put("/move")
//                .then().log().all()
//                .statusCode(HttpStatus.BAD_REQUEST.value())
//                .body(is(""));
//    }
}
