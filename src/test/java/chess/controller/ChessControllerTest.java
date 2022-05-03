package chess.controller;


import chess.SpringChessApplication;
import chess.dto.request.CreateRoomDto;
import chess.dto.request.MovePieceDto;
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
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = SpringChessApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChessControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        FakeDao fakeDao = new FakeDao(jdbcTemplate);
        fakeDao.setUpTable();

        fakeDao.createRoom("room1", "passwor1");
        fakeDao.createRoom("room2", "password2");
    }

    @DisplayName("Room - POST")
    @Test
    void createRoom() {
        CreateRoomDto roomDto = new CreateRoomDto( "title",  "password");

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(roomDto)
            .when().post("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("Room - GET")
    @Test
    void getRooms() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Board - GET")
    @Test
    void getBoard() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("move - POST")
    @Test
    void move() {
        MovePieceDto movePieceDto = new MovePieceDto("a2", "a3");

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(movePieceDto)
            .when().put("/board/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("score - GET")
    @Test
    void score() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms/1/score")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }


    @DisplayName("reset - POST")
    @Test
    void reset() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/rooms/2")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("end - POST")
    @Test
    void end() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().patch("/rooms/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("status - GET")
    @Test
    void status() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms/1/status")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }
}
