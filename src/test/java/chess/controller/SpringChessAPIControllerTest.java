package chess.controller;

import static org.hamcrest.core.Is.is;

import chess.domain.Room;
import dto.MoveDto;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringChessAPIControllerTest {
    private static int createCount = 0;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("방 생성하기 테스트")
    void createRoom() {
        createCount++;
        Room room = new Room(1, "멍토", "비번", 1);
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(room)
            .when().post("/api/rooms")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("모든방 정보 가져오기 테스트")
    void loadAllRoom() {
        createRoom();
        createRoom();
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/rooms")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(createCount));
    }

    @Test
    @DisplayName("게임정보 가져오기 테스트")
    void loadGame() {
        createRoom();
        Room room = new Room(1, "멍토", "비번", 1);
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(room)
            .when().post("/api/rooms/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(3));
    }

    @Test
    @DisplayName("말 이동하기 테스트")
    void movePiece() {
        createRoom();
        Map<String, String> body = new HashMap<>();
        body.put("to", "a4");
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().put("/api/rooms/1/pieces/a2")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(3));
    }
}