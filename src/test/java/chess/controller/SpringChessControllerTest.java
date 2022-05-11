package chess.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import chess.database.dto.RoomDto;
import chess.domain.game.GameState;
import chess.service.ChessRoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
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

    @Autowired
    private ChessRoomService chessRoomService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        chessRoomService.createNewRoom(DEFAULT_ROOM_NAME, DEFAULT_ROOM_PASSWORD);
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
    public void createRoom() throws JsonProcessingException {
        String path = "/rooms";
        ObjectMapper mapper = new ObjectMapper();

        RoomDto dto = new RoomDto(CREATE_ROOM_NAME, CREATE_ROOM_PASSWORD);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", CREATE_ROOM_NAME);
        map.put("password", CREATE_ROOM_PASSWORD);

        String body = mapper.writeValueAsString(map);

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
    public void enterRoom() throws JsonProcessingException {
        String path = "/rooms/1/enter";

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/rooms/1"));
    }

    @Test
    @DisplayName("게임방을 삭제한다.")
    public void deleteRoom() throws JsonProcessingException {
        String path = "/rooms/1";
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1);
        map.put("password", DEFAULT_ROOM_PASSWORD);

        String body = mapper.writeValueAsString(map);

        RestAssured.given().log().all()
            .when().body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .delete(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/"));
    }

    @Test
    @DisplayName("게임을 시작한다.")
    public void startGame() throws JsonProcessingException {
        String path = "/rooms/1/start";

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/rooms/1"));
    }

    @Test
    @DisplayName("게임을 종료하면 첫 화면으로 이동한다.")
    public void endGame() throws JsonProcessingException {
        String path = "/rooms/1/end";

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/"));
    }

    @Test
    @DisplayName("게임을 결과를 반환한다.")
    public void checkGameStatus() throws JsonProcessingException {
        String path = "/rooms/1/status";
        GameState state = chessRoomService.readGameState(1);

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("score.size()", equalTo(2))
            .body("score.WHITE", equalTo(38.0F));
    }
}
