package wooteco.chess.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.repository.RoomEntity;
import wooteco.chess.service.RoomService;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@SpringBootTest
public class RoomControllerTest {

    @Autowired
    RoomService roomService;

    private long testId;

    @BeforeEach
    void setUp(){
        roomService.createRoom("ControllerTest");
        List<RoomEntity> roomEntities = roomService.findAllRoom();
        testId = roomEntities.get(roomEntities.size()-1).getId();
    }

    @Test
    @DisplayName("첫 화면 렌더링 확인")
    void indexTest() {
        given()
                .log().all().
        when().
                get("/").
        then().
                log().all().
                body(containsString("체스게임 by BossDog & Orange")).
                statusCode(200);
    }

    @Test
    @DisplayName("체스 게임 화면 테스트")
    void createTest(){

        given()
                .log().all().
        when().
                get("/room/"+testId).
        then().
                log().all().
                body(containsString("ControllerTest")).
                statusCode(200);
    }

    @Test
    @DisplayName("턴 표시 테스트")
    void finishTest(){
        String turn = roomService.findTurnById(testId);

        given().
                log().all().
        when().
                get("/room/"+testId+"/turn").
        then().
                log().all().
                body(is(turn)).
                statusCode(200);
    }
}
