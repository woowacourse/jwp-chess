package chess.web;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.StringContains.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import chess.configuration.RepositoryConfiguration;
import chess.service.GameService;
import chess.service.RoomService;
import chess.web.dto.RoomDto;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(RepositoryConfiguration.class)
class RoomControllerTest {

    private static final String testName = "summer";
    private static final String password = "summer";

    @LocalServerPort
    private int port;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RoomService roomService;
    @Autowired
    private GameService gameService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void deleteCreated() {
        roomService.findAll()
            .forEach(room -> roomService.removeById((int) room.getId()));
    }

    @DisplayName("유효한 이름을 받으면 게임방 입장")
    @Test
    void createRoom() {
        RestAssured.given().log().all()
            .formParam("name", testName)
            .formParam("password", "1234")
            .when().post("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.FOUND.value())
            .header("Location", containsString("/rooms/"));
    }

    @Test
    @DisplayName("방 목록을 불러온다.")
    void loadRooms() {
        roomService.create(new RoomDto(testName, password));
        roomService.create(new RoomDto("does", "does"));
        RestAssured.given().log().all()
            .when().get("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(2));
    }

    @DisplayName("부적절한 이름이 입력되면 400 에러 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", "16자를넘는방이름은안되니까돌아가"})
    void nameException(String name) {
        RestAssured.given().log().all()
            .formParam("name", name)
            .when().post("/rooms")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("새로운 게임을 시작하면 200 응답을 받는다.")
    void startNewGame() {
        RoomDto room = roomService.create(new RoomDto(testName, password));

        RestAssured.given().log().all()
            .when().get("/rooms/" + room.getId() + "/start")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("게임을 불러오면 200 응답을 받는다.")
    void loadGame() {
        int roomId = (int) roomService.create(new RoomDto(testName, password)).getId();
        gameService.startNewGame(roomId);

        RestAssured.given().log().all()
            .when().get("/rooms/" + roomId + "/load")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("방을 삭제한다.")
    void deleteRoom() {
        RoomDto room = roomService.create(new RoomDto(testName, password));

        RestAssured.given().log().all()
            .when().delete("/rooms/" + room.getId())
            .then().log().all()
            .header("location", containsString("/"));
    }
}
