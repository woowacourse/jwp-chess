package chess.controller;

import static org.hamcrest.Matchers.is;

import chess.dao.JdbcFixture;
import chess.dto.MoveDto;
import chess.dto.PasswordDto;
import chess.dto.RoomCreationDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ChessApiControllerTest extends ControllerTest {

    @Test
    void create() {
        RoomCreationDto roomCreationDto = new RoomCreationDto("test", "1234");
        RestAssured.given().log().all()
                .body(roomCreationDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(2));
    }

    @Test
    void start() {
        RestAssured.given().log().all()
                .when().get("/rooms/1/start")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("turn", is("white"))
                .body("board.size()", is(64));
    }

    @Test
    void load() {
        RestAssured.get("/rooms/1/start");

        RestAssured.given().log().all()
                .when().get("/rooms/1/load")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("turn", is("white"))
                .body("board.size()", is(64));
    }

    @Test
    void move() {
        RestAssured.get("/rooms/1/start");
        MoveDto moveDto = new MoveDto("a2", "a4");

        RestAssured.given().log().all()
                .body(moveDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/rooms/1/board")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("turn", is("black"))
                .body("board.a2", is("empty"))
                .body("board.a4", is("white_pawn"));
    }

    @Test
    void status() {
        RestAssured.get("/rooms/1/start");

        RestAssured.given().log().all()
                .when().get("/rooms/1/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("whiteScore", is(38.0F))
                .body("blackScore", is(38.0F));
    }

    @Test
    void moveExceptionWrongPosition() {
        RestAssured.get("/rooms/1/start");
        MoveDto moveDto = new MoveDto("a2", "a5");

        RestAssured.given().log().all()
                .body(moveDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/rooms/1/board")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("해당 Position으로 이동할 수 없습니다."));
    }

    @Test
    void moveExceptionWrongTurn() {
        RestAssured.get("/rooms/1/start");
        MoveDto moveDto = new MoveDto("a7", "a6");

        RestAssured.given().log().all()
                .body(moveDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/rooms/1/board")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("당신의 차례가 아닙니다."));
    }

    @Test
    void loadExceptionBeforeInit() {
        JdbcFixture.insertRoom(jdbcTemplate, "roma2", "1234", "white");

        RestAssured.given().log().all()
                .when().get("/rooms/2/load")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("해당 ID에 체스게임이 초기화되지 않았습니다."));
    }

    @Test
    void list() {
        JdbcFixture.insertRoom(jdbcTemplate, "roma2", "1234", "white");
        JdbcFixture.insertRoom(jdbcTemplate, "roma3", "1234", "white");

        RestAssured.given().log().all()
                .when().get("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("size()", is(3));
    }

    @Test
    void end() {
        RestAssured.given().log().all()
                .when().patch("/rooms/1/end")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("message", is("게임 종료상태로 변경했습니다."));
    }

    @Test
    void delete() {
        JdbcFixture.insertRoom(jdbcTemplate, "sojukang", "1111", "empty");

        PasswordDto passwordDto = new PasswordDto("1111");
        RestAssured.given().log().all()
                .body(passwordDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/rooms/2")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("message", is("방을 삭제했습니다."));
    }
}
