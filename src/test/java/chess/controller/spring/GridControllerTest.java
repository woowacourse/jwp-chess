package chess.controller.spring;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class GridControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("TRUNCATE TABLE piece");
        jdbcTemplate.execute("TRUNCATE TABLE grid");
        jdbcTemplate.execute("TRUNCATE TABLE room");
    }

    @Test
    @DisplayName("/grid/{roomName} GET")
    void restart() {
        String roomName = "testroom";
        RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(roomName)
                .when()
                    .get("/grid/{roomName}", roomName)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("code", is(200))
                    .body("data.piecesResponseDto.size()", is(64));
    }

    @Test
    @DisplayName("/grid/{gridId}/start POST")
    void start() {
        String roomName = "testroom";
        int gridId = RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(roomName)
                .when()
                    .get("/grid/{roomName}", roomName)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .response()
                    .path("data.gridDto.gridId");

        RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/grid/{gridId}/start", gridId)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("code", is(204));
    }

    @Test
    @DisplayName("/grid/{gridId}/finish POST")
    void finish() {
        String roomName = "testroom";
        int gridId = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(roomName)
                .when()
                .get("/grid/{roomName}", roomName)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .path("data.gridDto.gridId");

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/grid/{gridId}/start", gridId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/grid/{gridId}/finish", gridId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("code", is(204));
    }
}
