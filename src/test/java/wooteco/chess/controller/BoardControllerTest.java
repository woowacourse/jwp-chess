package wooteco.chess.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.domain.room.Room;
import wooteco.chess.service.RoomService;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
class BoardControllerTest {

    @Autowired
    private RoomService roomService;

    private Long testId;

    @BeforeEach
    void setUp() {
        roomService.createRoom("test room");
        List<Room> rooms = roomService.findAllRoom();
        testId = rooms.get(rooms.size() - 1).getId();
        roomService.initializeBoard(testId);
    }

    @Test
    @DisplayName("이동 확인")
    void move() {
        given()
                .log().all().
        when().
                param("roomId", testId).
                param("fromPiece", "d2").
                param("toPiece", "d3").
                put("/room/" + testId + "/move").
        then().
                log().all().
                body(containsString("d2 " + "d3")).
                statusCode(200);
    }

    @Test
    @DisplayName("이동 불가능 확인")
    void moveException() {
        given()
                .log().all().
        when().
                param("roomId", testId).
                param("fromPiece", "a5").
                param("toPiece", "a5").
                put("/room/" + testId + "/move").
        then().
                log().all().
                statusCode(400);
    }

    @Test
    @DisplayName("점수 표시")
    void scoreStatus() {
        given()
                .log().all().
        when().
                param("roomId", testId).
                get("/room/" + testId + "/score").
        then().
                log().all().
                body(containsString("white")).
                statusCode(200);
    }

}