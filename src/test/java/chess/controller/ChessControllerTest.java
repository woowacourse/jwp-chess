package chess.controller;

import static org.hamcrest.core.Is.is;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void index() {
        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void createRoom()  {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("roomName", "방생성테스트");

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/createroom/방생성테스트")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(is(responseBody.toString()));
    }
}
