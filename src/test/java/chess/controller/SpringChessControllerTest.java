package chess.controller;

import static org.hamcrest.core.Is.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import chess.service.GameService;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringChessControllerTest {

    private static final String TEST_ROOM_NAME = "SPRING_T";
    private static final String TEST_CREATION_ROOM_NAME = "SPRING_C";

    @Autowired
    private GameService service;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        service.createNewGame(TEST_ROOM_NAME);
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
        String roomName = TEST_ROOM_NAME;

        RestAssured.given().log().all()
            .when().get("/main?room_name=" + roomName)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("게임명을 입력하고 게임을 시작한다.")
    public void create() {
        String path = "/create";
        String roomName = TEST_CREATION_ROOM_NAME;
        String params = String.format("?room_name=%s", roomName);

        RestAssured.given().log().all()
            .when().get(path + params)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/main?room_name=" + roomName));
    }

    @Test
    @DisplayName("기존 게임방에 입장한다.")
    public void enter() {
        String roomName = TEST_ROOM_NAME;

        RestAssured.given().log().all()
            .when().get("/enter?room_name=" + roomName)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/main?room_name=" + roomName));
    }

    @Test
    @DisplayName("게임을 시작한다.")
    public void start() {
        String roomName = TEST_ROOM_NAME;

        RestAssured.given().log().all()
            .when().get("/start?room_name=" + roomName)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/main?room_name=" + roomName));
    }

    @Test
    @DisplayName("게임을 종료한다.")
    public void end() {
        String roomName = TEST_ROOM_NAME;

        service.startGame(roomName);

        RestAssured.given().log().all()
            .when().get("/end?room_name=" + roomName)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/"));
    }

    @Test
    @DisplayName("기물을 이동한다.")
    public void move() {
        String arguments = "{'source':'a2', 'destination':'a3'}";
        String roomName = TEST_ROOM_NAME;

        service.startGame(roomName);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(arguments)
            .when().post("/move?room_name=" + roomName)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("점수를 확인한다.")
    public void status() {
        String roomName = TEST_ROOM_NAME;

        service.startGame(roomName);

        RestAssured.given().log().all()
            .when().get("/status?room_name=" + roomName)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("예외를 잡는다.")
    public void handleException() {
        String roomName = TEST_ROOM_NAME;

        RestAssured.given().log().all()
            .when().get("/create?room_name" + roomName)
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @AfterEach
    void setDown() {
        service.removeGameAndBoard(TEST_ROOM_NAME);
        service.removeGameAndBoard(TEST_CREATION_ROOM_NAME);
    }
}
