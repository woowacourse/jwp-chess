package chess.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

import chess.dto.RoomRequestDto;
import chess.service.RoomService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RoomControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private RoomService service;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        service.deleteAll();
        service.add("test");
    }

    @Test
    void index() {
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.TEXT_HTML_VALUE)
            .body(containsString("N E W  R O O M"));
    }

    @Test
    void add() {
        RoomRequestDto dto = new RoomRequestDto("test2");

        RestAssured.given()
            .log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(dto)
            .when().post("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.FOUND.value());

        assertThat(service.findAllDesc()).hasSize(2);
    }

    @Test
    void delete() {
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/rooms/{id}",1)
            .then().log().all()
            .statusCode(HttpStatus.FOUND.value());

        assertThat(service.findAllDesc()).hasSize(0);
    }
}