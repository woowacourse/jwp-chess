package chess;

import static org.hamcrest.Matchers.is;

import chess.dao.JdbcFixture;
import chess.dto.MoveDto;
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
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        JdbcFixture.dropTable(jdbcTemplate, "square");
        JdbcFixture.dropTable(jdbcTemplate, "room");
        JdbcFixture.createRoomTable(jdbcTemplate);
        JdbcFixture.createSquareTable(jdbcTemplate);
        JdbcFixture.insertRoom(jdbcTemplate, "roma", "white");
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
                .when().get("/room?name=roma")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.TEXT_HTML_VALUE);
    }

    @Test
    void start() {
        RestAssured.given().log().all()
                .when().get("/start?name=roma")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("turn", is("white"))
                .body("board.size()", is(64));
    }

    @Test
    void load() {
        RestAssured.get("/start?name=roma");

        RestAssured.given().log().all()
                .when().get("/load?name=roma")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("turn", is("white"))
                .body("board.size()", is(64));
    }

    @Test
    void move() {
        RestAssured.get("/start?name=roma");
        MoveDto moveDto = new MoveDto("a2", "a4");

        RestAssured.given().log().all()
                .body(moveDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/move?name=roma")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("turn", is("black"))
                .body("board.a2", is("empty"))
                .body("board.a4", is("white_pawn"));
    }

    @Test
    void status() {
        RestAssured.get("/start?name=roma");

        RestAssured.given().log().all()
                .when().get("/status?name=roma")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("whiteScore", is(38.0F))
                .body("blackScore", is(38.0F));
    }

    @Test
    void moveExceptionWrongPosition() {
        RestAssured.get("/start?name=roma");
        MoveDto moveDto = new MoveDto("a2", "a5");

        RestAssured.given().log().all()
                .body(moveDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/move?name=roma")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("해당 Position으로 이동할 수 없습니다."));
    }

    @Test
    void moveExceptionWrongTurn() {
        RestAssured.get("/start?name=roma");
        MoveDto moveDto = new MoveDto("a7", "a6");

        RestAssured.given().log().all()
                .body(moveDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/move?name=roma")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("당신의 차례가 아닙니다."));
    }

    @Test
    void loadExceptionBeforeInit() {
        RestAssured.get("/room?name=sojukang");

        RestAssured.given().log().all()
                .when().get("/load?name=sojukang")
                .then().log().all()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("error", is("Internal Server Error"));
    }
}
