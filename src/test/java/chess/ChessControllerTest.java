package chess;

import chess.dao.JdbcFixture;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        JdbcFixture.dropTable(jdbcTemplate, "square");
        JdbcFixture.dropTable(jdbcTemplate, "room");
        JdbcFixture.createRoomTable(jdbcTemplate);
        JdbcFixture.createSquareTable(jdbcTemplate);
        JdbcFixture.insertRoom(jdbcTemplate, "roma", "1234", "white");
    }

    @Test
    void index() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.TEXT_HTML_VALUE);
    }

    @Test
    void room() {
        RestAssured.given().log().all()
                .when().get("/rooms/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.TEXT_HTML_VALUE);
    }
}
