package chess.controller;

import chess.dto.web.PointsDto;
import chess.dto.web.RoomDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringChessApiControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RoomDto roomDto = new RoomDto("1", "fortuneRoom", "fortune", "portune");
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(roomDto)
                .post("/room");
    }

    @DisplayName("방을 만드는데 성공하면, 성공 http 코드와 자동생성된 방 번호를 반환한다.")
    @Test
    void createRoom() {
        RoomDto roomDto = new RoomDto("2", "pobiRoom", "pobi", "povi");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(roomDto)
                .when().post("/room")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("roomId", notNullValue());
    }

    @DisplayName("방 정보를 요청하면, 유저 정보가 담겨있는 정보들을 받는다.")
    @Test
    void usersInRoom() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/room/1/statistics")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("whiteName", is("fortune"))
                .body("whiteWin", is("0"))
                .body("whiteLose", is("0"))
                .body("blackName", is("portune"))
                .body("blackWin", is("0"))
                .body("blackLose", is("0"));
    }

    @DisplayName("게임 상태를 요청하면, id에 해당하는 게임 상태를 받는다.")
    @Test
    void gameStatus() {

        RestAssured.given()
                .when().put("/room/1/start");

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/room/1/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("gameState", is("Running"))
                .body("turn", is("w"))
                .body("winner", is("n"));
    }

    @DisplayName("게임을 시작하면, id에 해당하는 보드 정보를 받는다.")
    @Test
    void startGame() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/room/1/start")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("board", notNullValue());
    }

    @DisplayName("게임을 종료하면, id에 해당하는 게임을 종료하고 200 응답 메시지를 보낸다.")
    @Test
    void exitGame() {
        RestAssured.given().log().all()
                .when().put("/room/1/exit")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("방에에서 나가면, id에 해당하는 진행중인 방의 상태를 닫힘으로 변경한다.")
    @Test
    void closeRoom() {
        RestAssured.given().log().all()
                .when().put("/room/1/close")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("말의 위치를 보내면, 해당 말이 갈 수 있는 경로를 반환한다. ")
    @Test
    void movablePoints() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/room/1/movable/a1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("x", notNullValue());
    }

    @DisplayName("말을 움직이면, 변화된 보드 정보를 리턴받는다")
    @Test
    void move() {
        PointsDto pointsDto = new PointsDto("b2", "b3");

        RestAssured.given()
                .when().put("/room/1/start");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(pointsDto)
                .when().post("/room/1/move")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("board", notNullValue());
    }
}