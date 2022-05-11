package chess.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import chess.database.dto.RoomDto;
import chess.domain.game.GameState;
import chess.service.ChessRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/sql/chess-test.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringChessControllerTest {

    private static final String DEFAULT_ROOM_NAME = "test1";
    private static final String DEFAULT_ROOM_PASSWORD = "1234";
    private static final String CREATE_ROOM_NAME = "test2";
    private static final String CREATE_ROOM_PASSWORD = "1234";

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        String path = "/rooms";
        RoomDto dto = new RoomDto(DEFAULT_ROOM_NAME, DEFAULT_ROOM_PASSWORD);

        RestAssured.given().log().all()
            .when().body(dto)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post(path);
    }

    @Test
    @DisplayName("게임방 목록 페이지를 방문한다.")
    public void index() {
        String path = "/";

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("게임방을 생성한다.")
    public void createRoom() {
        String path = "/rooms";
        RoomDto dto = new RoomDto(CREATE_ROOM_NAME, CREATE_ROOM_PASSWORD);

        RestAssured.given().log().all()
            .when().body(dto)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/rooms/2"));
    }

    @Test
    @DisplayName("게임방에 들어간다.")
    public void enterRoom() {
        String path = "/rooms/1/enter";

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/rooms/1"));
    }

    @Test
    @DisplayName("게임방을 삭제한다.")
    public void deleteRoom() {
        String path = "/rooms/1";

        RoomDto roomDto = new RoomDto(1, null, CREATE_ROOM_PASSWORD);

        RestAssured.given().log().all()
            .when().body(roomDto)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .delete(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/"));
    }

    @Test
    @DisplayName("게임을 시작한다.")
    public void startGame() {
        String path = "/rooms/1/start";

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/rooms/1"));
    }

    @Test
    @DisplayName("게임을 종료하면 첫 화면으로 이동한다.")
    public void endGame() {
        String path = "/rooms/1/end";

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/"));
    }

    @Test
    @DisplayName("게임을 결과를 반환한다.")
    public void checkGameStatus() {
        String path = "/rooms/1/status";

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("score.size()", equalTo(2))
            .body("score.WHITE", equalTo(38.0F));
    }
}
