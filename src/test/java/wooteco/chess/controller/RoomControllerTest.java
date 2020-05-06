package wooteco.chess.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.chess.domain.room.Room;
import wooteco.chess.service.RoomService;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomControllerTest {

    @Autowired
    private RoomService roomService;

    private long testId;

    @BeforeEach
    public void setUp(){
        roomService.createRoom("test room");
        List<Room> rooms = roomService.findAllRoom();
        testId = rooms.get(rooms.size()-1).getId();
    }

    @Test
    @DisplayName("방 리스트 화면")
    public void index() {
        given()
                .log().all().
        when().
                get("/room").
        then().
                log().all().
                body(containsString("체스게임 by BossDog & Orange")).
                statusCode(200);
    }

    @Test
    @DisplayName("방 생성 확인")
    public void create() {
        given()
                .log().all().
        when()
                .get("/room/" + testId).
        then()
                .log().all()
                .body(containsString("test room"))
                .statusCode(200);
    }

    @Test
    @DisplayName("턴 테스트")
    public void finishTest(){
        String turn = roomService.findTurnById(testId);

        given().
                log().all().
        when().
                get("/room/" + testId + "/turn").
        then().
                log().all().
                body(is(turn)).
                statusCode(200);
    }
}