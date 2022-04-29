package chess;

import static org.hamcrest.Matchers.*;

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

import chess.dao.JdbcFixture;
import chess.dto.MoveDto;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessControllerTest {

    private final Long id = 1L;

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        JdbcFixture jdbcFixture = new JdbcFixture(jdbcTemplate);
        RestAssured.port = port;
        jdbcFixture.dropTable("square");
        jdbcFixture.dropTable("room");
        jdbcFixture.createRoomTable();
        jdbcFixture.createSquareTable();
        jdbcFixture.insertRoom("roma", "white", "pw");
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
            .body("name=sojukang&password=123")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .when().post("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void roomExceptionAlreadyExists() {
        RestAssured.given().log().all()
            .body("name=roma&password=pw")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .when().post("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void start() {
        RestAssured.given().log().all()
            .when().post("/rooms/" + id)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("turn", is("white"))
            .body("board.size()", is(64));
    }

    @Test
    void load() {
        RestAssured.post("/rooms/" + id);

        RestAssured.given().log().all()
            .when().get("/rooms/" + id)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("turn", is("white"))
            .body("board.size()", is(64));
    }

    @Test
    void move() {
        RestAssured.post("/rooms/" + id);
        MoveDto moveDto = new MoveDto("a2", "a4");

        RestAssured.given().log().all()
            .body(moveDto)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().patch("/rooms/" + id + "/move")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("turn", is("black"))
            .body("board.a2", is("empty"))
            .body("board.a4", is("white_pawn"));
    }

    @Test
    void status() {
        RestAssured.post("/rooms/" + id);

        RestAssured.given().log().all()
            .when().get("/rooms/" + id + "/status")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("whiteScore", is(38.0F))
            .body("blackScore", is(38.0F));
    }

    @Test
    void moveExceptionWrongPosition() {
        RestAssured.post("/rooms/" + id);
        MoveDto moveDto = new MoveDto("a2", "a5");

        RestAssured.given().log().all()
            .body(moveDto)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().patch("/rooms/" + id + "/move")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("message", is("해당 Position으로 이동할 수 없습니다."));
    }

    @Test
    void moveExceptionWrongTurn() {
        RestAssured.post("/rooms/" + id);
        MoveDto moveDto = new MoveDto("a7", "a6");

        RestAssured.given().log().all()
            .body(moveDto)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().patch("/rooms/" + id + "/move")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("message", is("당신의 차례가 아닙니다."));
    }

    @Test
    void loadExceptionBeforeInit() {
        RestAssured.given().log().all()
            .when().get("/rooms/" + id)
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("message", is("해당 ID에 체스게임이 초기화되지 않았습니다."));
    }
}
