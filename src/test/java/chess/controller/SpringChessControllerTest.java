package chess.controller;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import chess.dto.RoomRequest;
import chess.repository.RoomRepository;
import chess.service.GameService;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class SpringChessControllerTest {

    public static final String TEST_PASSWORD = "PASSWORD";

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GameService service;

    private Long roomId;
    private Long gameId;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        roomId = service.createNewGame(new RoomRequest("HELLOTEST", "PASSWORD"));
        gameId = service.findGameIdByRoomId(roomId);
    }

    @Test
    @DisplayName("인덱스 페이지를 방문한다.")
    public void index() {
        String path = "/";

        RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("게임 화면을 보여준다")
    public void main() {
        RestAssured.given().log().all()
            .when().get("/main/" + roomId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("게임명을 입력하고 게임을 시작한다.")
    public void create() {
        String path = "/room";
        RoomRequest roomRequest = new RoomRequest("TEST_ROOM_NAME", "TEST_PASSWORD");

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(roomRequest)
            .when().post(path)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", containsString("/main"));
    }

    @Test
    @DisplayName("기존 게임방에 입장한다.")
    public void enter() {
        RestAssured.given().log().all()
            .when().get("/room/" + roomId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/main/" + gameId));
    }

    @Test
    @DisplayName("게임을 시작한다.")
    public void start() {
        RestAssured.given().log().all()
            .when().patch("/game/" + roomId + "/start")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/main/" + roomId));
    }

    @Test
    @DisplayName("게임을 종료한다.")
    public void end() {
        RestAssured.given().log().all()
            .when().patch("/game/" + gameId + "/end")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/"));
    }

    @Test
    @DisplayName("기물을 이동한다.")
    public void move() {
        String arguments = "{\"source\": \"a2\", \"destination\":\"a3\"}";

        service.startGame(roomId);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(arguments)
            .when().patch("/board/" + gameId + "/move")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("점수를 확인한다.")
    public void status() {
        Long roomId = this.roomId;

        service.startGame(roomId);

        RestAssured.given().log().all()
            .when().get("/board/" + roomId + "/status")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("예외를 잡는다.")
    public void handleException() {
        Long roomId = this.roomId;
        String arguments = "{'source':'a2', 'destination':'a7'}";

        service.startGame(roomId);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(arguments)
            .when().patch("/board/" + roomId + "/move")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @AfterEach
    void setDown() {
        roomRepository.deleteRoom(roomId);
    }
}
