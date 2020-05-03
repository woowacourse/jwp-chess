package wooteco.chess;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import wooteco.chess.entity.RoomEntity;
import wooteco.chess.repository.RoomRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResultContollerTest {
    @LocalServerPort
    int port;
    @Autowired
    private RoomRepository roomRepository;
    private RoomEntity roomEntity;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        roomEntity = roomRepository.save(new RoomEntity("우테코 체스"));
    }

    @DisplayName("게임이 끝나지 않은 상태에서 결과를 요청했을 때, 체스 게임방의 입장 상태로 응답을 보내는지 확인")
    @Test
    void validateResponseToResultPageWhenGameIsNotOver() {
        given().
                log().all().
                when().
                get("/chess/" + roomEntity.getId());

        given().
                log().all().
                when().
                get("/result/" + roomEntity.getId()).
                then().
                log().all().
                statusCode(200).
                body(containsString(":: 체스 게임 ::"));
    }
}
