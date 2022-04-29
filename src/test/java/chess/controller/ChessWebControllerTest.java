package chess.controller;

import static org.hamcrest.core.StringContains.containsString;

import chess.RoomEntityFixtures;
import chess.dao.JdbcRoomDao;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessWebControllerTest {

    @LocalServerPort
    private int port;

    private final JdbcRoomDao roomDao;

    @Autowired
    public ChessWebControllerTest(final JdbcRoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("GET /")
    @Test
    void index_page_조회한다() {
        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("GET /rooms/{id}")
    @Test
    void room_page_조회한다() {
        final int id = roomDao.save(RoomEntityFixtures.generateRoomEntity());

        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/rooms/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("GET /rooms/{id} - 404")
    @Test
    void room_page_조회_실패한다() {
        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/rooms/" + 0)
                .then().log().all()
                .body("html.body.div.h2", containsString("페이지를 찾을 수 없습니다!"));
    }
}
