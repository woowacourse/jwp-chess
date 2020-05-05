package wooteco.chess.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.domain.repository.RoomEntity;
import wooteco.chess.service.RoomService;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
public class BoardControllerTest {

    @Autowired
    RoomService roomService;

    private long testId;


    @BeforeEach
    void setUp(){
        roomService.createRoom("ControllerTest");
        List<RoomEntity> roomEntities = roomService.findAllRoom();
        testId = roomEntities.get(roomEntities.size()-1).getId();
        roomService.initializeBoard(testId);
    }

    @Test
    @DisplayName("정상 이동 확인")
    void moveTest(){
        given()
                .log().all().
        when().
                param("roomId",testId).
                param("fromPiece","a2").
                param("toPiece","a3").
                put("/room/+"+testId+"/move").
        then().
                log().all().
                body(containsString("a2 "+"a3")).
                statusCode(200);
    }

    @Test
    @DisplayName("이동 예외코드 확인")
    void move400Test(){
        given()
                .log().all().
        when().
                param("roomId",testId).
                param("fromPiece","a5").
                param("toPiece","a5").
                put("/room/+"+testId+"/move").
        then().
                log().all().
                statusCode(400);
    }

    @Test
    @DisplayName("점수 표시 확인")
    void statusTest(){
        given()
                .log().all().
        when().
                param("roomId",testId).
                get("/room/+"+testId+"/score").
        then().
                log().all().
                body(containsString("white")).
                statusCode(200);
    }

}
