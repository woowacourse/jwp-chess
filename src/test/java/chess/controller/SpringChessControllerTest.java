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
import org.springframework.test.context.jdbc.Sql;

import chess.dto.RoomRequest;
import chess.service.GameService;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Sql("classpath:ddl.sql")
public class SpringChessControllerTest {

    @Autowired
    private GameService service;
    private Long id;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        id = service.createNewGame(new RoomRequest("HELLOTEST", "PASSWORD"));
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
        Long roomId = id;

        RestAssured.given().log().all()
            .when().get("/main/" + roomId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("게임명을 입력하고 게임을 시작한다.")
    public void create() {
        String path = "/create";
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
        Long roomId = id;

        RestAssured.given().log().all()
            .when().get("/enter/" + roomId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/main/" + roomId));
    }

    @Test
    @DisplayName("게임을 시작한다.")
    public void start() {
        Long roomId = id;

        RestAssured.given().log().all()
            .when().patch("/start/" + roomId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/main/" + roomId));
    }

    @Test
    @DisplayName("게임을 종료한다.")
    public void end() {
        Long roomId = id;

        service.startGame(roomId);

        RestAssured.given().log().all()
            .when().patch("/end/" + roomId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("url", is("/"));
    }

    @Test
    @DisplayName("기물을 이동한다.")
    public void move() {
        String arguments = "{'source':'a2', 'destination':'a3'}";
        Long roomId = id;

        service.startGame(roomId);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(arguments)
            .when().patch("/move/" + roomId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("점수를 확인한다.")
    public void status() {
        Long roomId = id;

        service.startGame(roomId);

        RestAssured.given().log().all()
            .when().get("/status/" + roomId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("예외를 잡는다.")
    public void handleException() {
        Long roomId = id;
        String arguments = "{'source':'a2', 'destination':'a7'}";

        service.startGame(roomId);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(arguments)
            .when().patch("/move/" + roomId)
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @AfterEach
    void setDown() {
        service.removeGameAndBoard(id);
    }
}
